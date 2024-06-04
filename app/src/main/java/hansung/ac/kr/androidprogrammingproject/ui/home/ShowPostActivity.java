package hansung.ac.kr.androidprogrammingproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.ui.chatting.Message;
import hansung.ac.kr.androidprogrammingproject.ui.chatting.RoomActivity;
import hansung.ac.kr.androidprogrammingproject.ui.chatting.RoomList;

public class ShowPostActivity extends AddPostActivity{

    private FirebaseDatabase database;              // 데이터베이스 인스턴스
    private DatabaseReference databaseRef;          // 데이터베이스 레퍼런스

    private String post_id;                         // 현재 게시물
    private String u_id;                            // 현재 게시물 작성자

    private ImageView iv_back, iv_edit, iv_delete;
    private TextView tv_title, tv_kindOf, tv_food, tv_person, tv_day, tv_time, tv_chatting;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpost);

        Intent intent = getIntent();
        post_id = intent.getStringExtra("post_id");
        u_id = intent.getStringExtra("u_id");

        tv_title = findViewById(R.id.tv_title);
        tv_kindOf = findViewById(R.id.tv_kindOf);
        tv_food = findViewById(R.id.tv_food);
        tv_day = findViewById(R.id.tv_day);
        tv_time = findViewById(R.id.tv_time);
        et_content = findViewById(R.id.et_content);
        tv_person = findViewById(R.id.tv_person);

        // 게시물 정보 가져오기
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("Post").child(post_id);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                tv_title.setText(post.getTitle());
                tv_kindOf.setText(post.getKindOf());
                tv_food.setText(post.getFood());
                tv_day.setText(post.getDay());
                tv_time.setText(post.getTime());
                et_content.setText(post.getContent());
                tv_person.setText(post.getPerson());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 내가 작성한 게시물인지
        if(u_id.equals(LoginActivity.u_id)) {
            // 수정 버튼
            iv_edit = findViewById(R.id.iv_edit);
            iv_edit.setVisibility(View.VISIBLE);
            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 수정
                    Intent intent = new Intent(ShowPostActivity.this, EditPostActivity.class);
                    intent.putExtra("post_id", post_id);
                    startActivity(intent);
                }
            });


            // 삭제 버튼
            iv_delete = findViewById(R.id.iv_delete);
            iv_delete.setVisibility(View.VISIBLE);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 삭제
                }
            });
        }

        // 뒤로 가기 버튼
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 채팅하러 가기
        tv_chatting = findViewById(R.id.tv_chatting);
        tv_chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // post_id = room_id
                Intent intent = new Intent(ShowPostActivity.this, RoomActivity.class);
                intent.putExtra("room_id", post_id);

                // 게시물 채팅 방에 이미 포함되어있는가?
                database = FirebaseDatabase.getInstance();
                databaseRef = database.getReference("project").child("RoomUsers").child(post_id);
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean flag = true;
                        int cnt = 1;

                        if(snapshot.getChildren() != null) {
                            for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                                Log.d("참여 유저 확인" , "LoginActivity.u_id: " + LoginActivity.u_id
                                        + " // RoomUsers" + cnt + ": " + childSnapshot.getValue());
                                // 이미 참여 중인 채팅 방이면
                                if(LoginActivity.u_id.equals(childSnapshot.getValue())) {
                                    flag = false;
                                    break;
                                }
                            }
                        }

                        if(flag) {
                            // 참여 중이 아니면 방에 입장하기
                            databaseRef.push().setValue(LoginActivity.u_id);

                            // 나의 참여 중인 채팅 방에 추가하기
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            RoomList roomList = new RoomList(post_id, timeStamp, "");
                            databaseRef = database.getReference("project").child("UsersRoom");
                            databaseRef.child(LoginActivity.u_id).push().setValue(roomList);

                            // 방에 입장하고 입장 메세지 전달
                            Message message = new Message(1, "님이 입장하였습니다.", LoginActivity.u_id, timeStamp);
                            databaseRef = database.getReference("project").child("Room");
                            databaseRef.child(post_id).child(timeStamp).setValue(message, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    startActivity(intent);
                                }
                            });
                        }
                        else {
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }
}
