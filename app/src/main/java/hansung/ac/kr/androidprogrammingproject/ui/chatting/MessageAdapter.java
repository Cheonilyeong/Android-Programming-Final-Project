package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    ArrayList<Message> dataList;    // message

    public MessageAdapter(ArrayList<Message> dataList) {
        this.dataList = dataList;
    }

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_profile;
        public TextView tv_nickname1, tv_msg1;
        public ViewHolder(View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_nickname1 = itemView.findViewById(R.id.tv_nickname1);
            tv_msg1 = itemView.findViewById(R.id.tv_msg1);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 새로운 뷰를 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 데이터를 뷰 홀더의 뷰에 바인딩
        Message dataModel = dataList.get(position);

        //Log.d("u_id", dataModel.getU_id());
        //Log.d("LoginActivity.u_id", LoginActivity.u_id);

        // 사용자 닉네임 받아오기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("project").child("UserAccount").child(dataModel.getU_id());
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                String nickname = userAccount.getNickName();
                String imageURL = userAccount.getImageURL();

                // 비동기니 여기서 처리하자.
                if(dataModel.getMessageType() == Message.ENTER
                        || dataModel.getMessageType() == Message.EXIT) {
                    holder.iv_profile.setVisibility(View.INVISIBLE);
                    holder.tv_nickname1.setText("");
                    holder.tv_msg1.setText('\"' + nickname + '\"' + dataModel.getMessage());
                    holder.tv_msg1.setGravity(Gravity.CENTER);
                }
                else if(dataModel.getU_id().equals(LoginActivity.u_id)) {
                    holder.iv_profile.setVisibility(View.INVISIBLE);
                    holder.tv_nickname1.setText(nickname);
                    holder.tv_msg1.setText(dataModel.getMessage());
                    holder.tv_nickname1.setGravity(Gravity.END);
                    holder.tv_msg1.setGravity(Gravity.END);
                }
                else {
                    if(imageURL.equals("")) holder.iv_profile.setImageResource(R.drawable.basicimage);
                    else downloadImage(holder, imageURL);
                    holder.iv_profile.setVisibility(View.VISIBLE);
                    holder.tv_nickname1.setText(nickname);
                    holder.tv_msg1.setText(dataModel.getMessage());
                    holder.tv_nickname1.setGravity(Gravity.START);
                    holder.tv_msg1.setGravity(Gravity.START);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void addChatting(Message message) {
        dataList.add(message);
        notifyItemInserted(dataList.size()-1);
    }

    // 이미지 로드
    public void downloadImage(ViewHolder holder, String imageURL) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(imageURL);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // 다운로드 URL이 성공적으로 가져왔으면
                // 이미지의 URL 가져오기
                loadImageIntoImageView(holder, uri.toString());
                Log.d("downloadImage", "downloadImage onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // URL을 가져오는 데 실패했을 때
                // 기본사진으로
                holder.iv_profile.setImageResource(R.drawable.basicimage);
                Log.e("downloadImage", "downloadImage onFailure");
            }
        });
    }
    // 이미지 저장
    public void loadImageIntoImageView(ViewHolder holder, String imageUrl) {
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.iv_profile);
    }

}
