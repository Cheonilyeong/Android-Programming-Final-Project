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

    private String post_id;
    private String u_id;
    private String title;
    private String kindOf;
    private String food;
    private String content;
    private String preson;
    private String day;
    private String time;

    private ImageView iv_back;
    private TextView tv_title, tv_kindOf, tv_food, tv_person, tv_day, tv_time, tv_chatting;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpost);

        Intent intent = getIntent();
        String post_id = intent.getStringExtra("post_id");
        String u_id = intent.getStringExtra("u_id");
        String title = intent.getStringExtra("title");
        String kindOf = intent.getStringExtra("kindOf");
        String food = intent.getStringExtra("food");
        String day = intent.getStringExtra("day");
        String time = intent.getStringExtra("time");
        String content = intent.getStringExtra("content");
        String person = intent.getStringExtra("person");
        // Log.d("showpost 입장", post_id);

        // 뒤로 가기 버튼
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 제목
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);

        // 분야
        tv_kindOf = findViewById(R.id.tv_kindOf);
        tv_kindOf.setText(kindOf);

        // 음식
        tv_food = findViewById(R.id.tv_food);
        tv_food.setText(food);

        // 날짜
        tv_day = findViewById(R.id.tv_day);
        tv_day.setText(day);

        // 시간
        tv_time = findViewById(R.id.tv_time);
        tv_time.setText(time);

        // 본문
        et_content = findViewById(R.id.et_content);
        et_content.setText(content);

        // 인원
        tv_person = findViewById(R.id.tv_person);
        tv_person.setText(person);

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
