package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {

    private FirebaseDatabase database;             // 데이터베이스 인스턴스
    private DatabaseReference databaseRef;         // 데이터베이스 레퍼런스
    private FirebaseStorage storage;               // 스토리지 인스턴스
    private StorageReference storageRef;           // 스토리지 레퍼런스

    private OnItemClickListener itemClickListener; // OnItemClickListener
    ArrayList<RoomList> dataList;                  // RoomList

    public RoomListAdapter(ArrayList<RoomList> dataList) {
        this.dataList = dataList;
    }

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_profile;
        public TextView tv_nickname, tv_lastMessage;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_lastMessage = itemView.findViewById(R.id.tv_lastMessage);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 새로운 뷰 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        // setOnClickListener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {                 // 유효한 위치인지 확인
                    String room_id = dataList.get(position).getRoom_id();
                    String u_id = dataList.get(position).getU_id();

                    Log.d("RecyclerView Click", "position: " + Integer.toString(position)
                    + " room_id: " + room_id + " u_id: " + u_id);

                    itemClickListener.onItemClicked(room_id, u_id);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoomList dataModel = dataList.get(position);
        // Room 상대방 nickname, imageURL 구하기
        String u_id = dataModel.getU_id();

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("UserAccount").child(u_id);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                // 데이터를 뷰 홀더의 뷰에 바인딩
                downloadImage(holder, userAccount.getImageURL());
                holder.tv_nickname.setText(userAccount.getNickName());
                holder.tv_lastMessage.setText(dataModel.getLastMessage());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void downloadImage(ViewHolder holder, String imageUrl) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(imageUrl);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // 다운로드 URL이 성공적으로 가져왔으면
                // 이미지의 URL 가져오기
                loadImageIntoImageView(holder, uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // URL을 가져오는 데 실패했을 때
                // 기본사진으로
                loadImageIntoImageView(holder, "/profile/NULL.jpg");
            }
        });
    }
    // 이미지 저장
    public void loadImageIntoImageView(ViewHolder holder, String imageUrl) {
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.iv_profile);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void addChattingRoom(RoomList roomList) {
        dataList.add(roomList);
        notifyItemInserted(dataList.size()-1);
    }
    public void changeLastMessage(RoomList roomList) {
        for(int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getRoom_id().equals(roomList.getRoom_id())) {
                dataList.get(i).setLastMessage(roomList.getLastMessage());
                notifyItemChanged(i);
            }
            break;
        }
    }

    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(String room_id, String u_id);
    }
    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }
}
