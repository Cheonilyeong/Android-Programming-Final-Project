package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;
import hansung.ac.kr.androidprogrammingproject.ui.chatting.Message;
import hansung.ac.kr.androidprogrammingproject.ui.chatting.MessageAdapter;

public class RoomActivity extends AppCompatActivity {

    private FirebaseDatabase database;          // 데이터베이스 인스턴스
    private DatabaseReference databaseRef;      // 데이터베이스 레퍼런스

    // Recycler View
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Message> messageDataset = new ArrayList<>(); // 데이터 리스트를 멤버 변수로 선언

    private ImageView iv_back;                  // 뒤로 가기 버튼
    private TextView tv_nickname;               // 대화 상대 nickname

    private String room_id;                     // 게시물 채팅 방 room_id (post_id)
    private String u_id;                        // 게시물 채팅 방장(작성자) u_id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        // Recycler View
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        messageAdapter = new MessageAdapter(messageDataset);
        recyclerView.setAdapter(messageAdapter);

        // 뒤로 가기 버튼
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Get room_id
        Intent intent = getIntent();
        room_id = intent.getStringExtra("room_id");
        Log.d("Enter Room", "Room_id: " + room_id);


        // 채팅 방 대화 가져오기
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("Room").child(room_id);

        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                messageAdapter.addChatting(message);
                recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        EditText et_msg = findViewById(R.id.et_msg);
        Button btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_msg.getText().toString();
                if(msg.equals("") || msg.trim().equals("")) return;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                Message message = new Message(3, msg, LoginActivity.u_id, timeStamp);
                databaseRef = database.getReference("project").child("Room").child(room_id);
                databaseRef.push().setValue(message);
                // lastMessage(message.getMessage());

                et_msg.setText("");
            }
        });
    }

    // lastMessage 수정
    public void lastMessage(String msg) {
//        databaseRef = database.getReference("project").child("UsersRoom").child(u_id);
//        databaseRef.child(room_id).child("lastMessage").setValue(msg);
//
//        databaseRef = database.getReference("project").child("UsersRoom").child(LoginActivity.u_id);
//        databaseRef.child(room_id).child("lastMessage").setValue(msg);
    }
}
