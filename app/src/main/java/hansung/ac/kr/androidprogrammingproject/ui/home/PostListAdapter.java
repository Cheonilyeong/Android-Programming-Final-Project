package hansung.ac.kr.androidprogrammingproject.ui.home;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;
import hansung.ac.kr.androidprogrammingproject.ui.chatting.MessageAdapter;
import hansung.ac.kr.androidprogrammingproject.ui.chatting.RoomListAdapter;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private FirebaseDatabase database;             // 데이터베이스 인스턴스
    private DatabaseReference databaseRef;         // 데이터베이스 레퍼런스

    private OnItemClickListener itemClickListener; // OnItemClickListener
    private List<Post> dataList;                   // PostList

    public PostListAdapter() {
        this.dataList = new ArrayList<>();
    }

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView iv_profile;
        public TextView tv_title,tv_content, tv_kindOf, tv_day, tv_time, tv_nickname, tv_person;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_kindOf = itemView.findViewById(R.id.tv_kindOf);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_person = itemView.findViewById(R.id.tv_person);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 새로운 뷰 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        // setOnClickListener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {                 // 유효한 위치인지 확인
                    Post post = dataList.get(position);
                    itemClickListener.onItemClicked(post);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post dataModel = dataList.get(position);
        Log.d("u_id()", dataModel.getU_id());

        holder.tv_title.setText(dataModel.getTitle());
        holder.tv_content.setText(dataModel.getContent());
        holder.tv_kindOf.setText(dataModel.getKindOf());
        holder.tv_day.setText(dataModel.getDay());
        holder.tv_time.setText(dataModel.getTime());
        holder.tv_person.setText(dataModel.getPerson());

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("UserAccount").child(dataModel.getU_id());
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                String nickname = userAccount.getNickName();
                String imageURL = userAccount.getImageURL();

                holder.tv_nickname.setText(nickname);
                if(imageURL.equals("profile/NULL.jpg")) holder.iv_profile.setImageResource(R.drawable.basicimage);
                else downloadImage(holder, imageURL);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void addPost(Post post) {
        dataList.add(post);
        notifyItemInserted(dataList.size()-1);
    }
    public void setPostList(List<Post> postList) {
        this.dataList = postList;
        notifyDataSetChanged();
    }

    // 이미지 로드
    public void downloadImage(PostListAdapter.ViewHolder holder, String imageURL) {
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
    public void loadImageIntoImageView(PostListAdapter.ViewHolder holder, String imageUrl) {
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.iv_profile);
    }
    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(Post post);
    }
    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }
}
