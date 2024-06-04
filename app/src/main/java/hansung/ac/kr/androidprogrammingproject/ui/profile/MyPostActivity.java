package hansung.ac.kr.androidprogrammingproject.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.ui.home.Post;
import hansung.ac.kr.androidprogrammingproject.ui.home.PostListAdapter;
import hansung.ac.kr.androidprogrammingproject.ui.home.ShowPostActivity;

public class MyPostActivity extends AppCompatActivity {

    private FirebaseDatabase database;          // 데이터베이스 인스턴스
    private DatabaseReference databaseRef;      // 데이터베이스 레퍼런스

    private RecyclerView recyclerView;                  // 리사이클러 뷰
    private RecyclerView.LayoutManager layoutManager;   // 레이아웃 매니저
    private PostListAdapter postListAdapter;            // 어뎁터

    private ArrayList<Post> postDataset = new ArrayList<>();

    private ImageView iv_back;                  // 뒤로 가기 버튼

    public void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "Profile onCreate 호출됨");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);

        // 리사이클러 뷰
        recyclerView = findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        postListAdapter = new PostListAdapter();
        postListAdapter.setOnItemClickListener(new PostListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Post post) {
                Intent intent = new Intent(MyPostActivity.this, ShowPostActivity.class);
                intent.putExtra("post_id", post.getPost_id());
                intent.putExtra("u_id", post.getU_id());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(postListAdapter);

        // 데이터 가져오기
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("UsersPost").child(LoginActivity.u_id);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 나의 게시물 post_id 구하기
                ArrayList<String> post_idList = new ArrayList<>();
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String post_id = childSnapshot.getKey();
                    post_idList.add(post_id);
                }

                // post_id로 post 정보 받기
                for(String post_id : post_idList) {
                    Log.d("post_id", post_id);
                    databaseRef = database.getReference("project").child("Post").child(post_id);
                    databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Post post = snapshot.getValue(Post.class);
                            Log.d("post 정보 확인", post.getPost_id());
                            postListAdapter.addPost(post);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 뒤로 가기 버튼
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
