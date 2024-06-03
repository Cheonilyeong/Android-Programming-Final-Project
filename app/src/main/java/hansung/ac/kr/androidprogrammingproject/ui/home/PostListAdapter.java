package hansung.ac.kr.androidprogrammingproject.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;


public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private FirebaseDatabase database;             // 데이터베이스 인스턴스
    private DatabaseReference databaseRef;         // 데이터베이스 레퍼런스

    private List<Post> dataList;                   // PostList

    public PostListAdapter() {
        this.dataList = new ArrayList<>();
    }

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_kindOf, tv_day, tv_time, tv_nickname;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_kindOf = itemView.findViewById(R.id.tv_kindOf);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 새로운 뷰 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post dataModel = dataList.get(position);
        Log.d("u_id()", dataModel.getU_id());

        holder.tv_title.setText(dataModel.getTitle());
        holder.tv_kindOf.setText(dataModel.getKindOf());
        holder.tv_day.setText(dataModel.getDay());
        holder.tv_time.setText(dataModel.getTime());

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("UserAccount").child(dataModel.getU_id());
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                holder.tv_nickname.setText(userAccount.getNickName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void setPostList(List<Post> postList) {
        this.dataList = postList;
        notifyDataSetChanged();
    }

}
