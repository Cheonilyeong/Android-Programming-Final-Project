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

import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;
import hansung.ac.kr.androidprogrammingproject.ui.home.Post;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {

    private OnItemClickListener itemClickListener; // OnItemClickListener
    private List<RoomList> dataList;                  // RoomList

    public RoomListAdapter() {
        dataList = new ArrayList<>();
    }

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_lastMessage, tv_time;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_lastMessage = itemView.findViewById(R.id.tv_lastMessage);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

    @NonNull
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

                    Log.d("RecyclerView Click", "position: " +
                            Integer.toString(position) + " room_id: " + room_id);

                    itemClickListener.onItemClicked(room_id);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomList dataModel = dataList.get(position);

        // Post title 가져오기
        String room_id = dataModel.getRoom_id();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("project").child("Post").child(room_id);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                holder.tv_title.setText("\"" + post.getTitle() + "\" 채팅 방");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // lastMessage & time
        holder.tv_time.setText(dataModel.getTime());
        holder.tv_lastMessage.setText(dataModel.getLastMessage());
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void setRoomList(List<RoomList> RoomList) {
        this.dataList = RoomList;
        notifyDataSetChanged();
    }

    // OnItemClickListener 인터페이스 선언
    public interface OnItemClickListener {
        void onItemClicked(String room_id);
    }
    // OnItemClickListener 전달 메소드
    public void setOnItemClickListener (OnItemClickListener listener) {
        itemClickListener = listener;
    }
}
