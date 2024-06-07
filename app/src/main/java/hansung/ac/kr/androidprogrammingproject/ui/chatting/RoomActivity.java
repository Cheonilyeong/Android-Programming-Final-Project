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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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
import hansung.ac.kr.androidprogrammingproject.ui.home.Post;
import hansung.ac.kr.androidprogrammingproject.ui.home.ShowPostActivity;

public class RoomActivity extends AppCompatActivity {

    private FirebaseDatabase database;          // 데이터베이스 인스턴스
    private DatabaseReference databaseRef;      // 데이터베이스 레퍼런스

    // Recycler View
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Message> messageDataset = new ArrayList<>(); // 데이터 리스트를 멤버 변수로 선언

    private ImageView iv_back;                  // 뒤로 가기 버튼
    private ImageView iv_exit;                  // 채팅 방 나가기 버튼
    private TextView tv_title;                  // 채팅 방 이름 (post title)
    private EditText et_msg;                    // 메세지
    private Button btn_send;                    // 메세지 전송 버튼

    private String room_id;                     // 게시물 채팅 방 room_id (post_id)
    private Post post;                          // 어떤 게시물의 채팅 방인가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        // Get room_id
        Intent intent = getIntent();
        room_id = intent.getStringExtra("room_id");
        Log.d("Enter Room", "Room_id: " + room_id);

        // ViewModel
        RoomViewModel roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        roomViewModel.setRoom_id(room_id);
        roomViewModel.loadRoomListFromFirebase();

        // Recycler View
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        messageAdapter = new MessageAdapter(messageDataset);
        recyclerView.setAdapter(messageAdapter);

        // 데이터 관찰 및 로딩 상태 처리
        // title 바꾸기
        tv_title = findViewById(R.id.tv_title);
        roomViewModel.getPost().observe(this, post -> {
            this.post = post;
            tv_title.setText(post.getTitle());
        });

        // message 받아서 어뎁터에
        roomViewModel.getMessageDataset().observe(this, messageList -> {
            messageAdapter.setMessageList(messageList);
        });

        // 메세지 전송 버튼
        et_msg = findViewById(R.id.et_msg);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_msg.getText().toString();
                if(msg.equals("") || msg.trim().equals("")) return;
                String timeStamp1 = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String timeStamp2 = new SimpleDateFormat("HH:mm").format(new Date());

                Message message = new Message(3, msg, LoginActivity.u_id, timeStamp2);
                database = FirebaseDatabase.getInstance();
                databaseRef = database.getReference("project").child("Room").child(room_id);
                databaseRef.child(timeStamp1).setValue(message);

                et_msg.setText("");
            }
        });
        // 뒤로 가기 버튼
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 채팅 방 나가기 버튼
        iv_exit = findViewById(R.id.iv_exit);
        iv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = View.inflate(RoomActivity.this, R.layout.dialog_exit, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(RoomActivity.this);
                dlg.setView(dialogView);

                final AlertDialog dialog = dlg.create();

                Button btn_no = dialogView.findViewById(R.id.btn_no);
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                Button btn_yes = dialogView.findViewById(R.id.btn_yes);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 혹시 작성자인가?
                        String u_id = post.getU_id();
                        if(u_id.equals(LoginActivity.u_id)) {
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "작성자는 방을 나갈 수 없습니다", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 퇴장 메세지 보내기
                        String timeStamp1 = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String timeStamp2 = new SimpleDateFormat("HH:mm").format(new Date());

                        Message message = new Message(2, "님이 방을 나갔습니다", LoginActivity.u_id, timeStamp2);
                        database = FirebaseDatabase.getInstance();
                        databaseRef = database.getReference("project").child("Room").child(room_id);
                        databaseRef.child(timeStamp1).setValue(message);

                        // RoomUsers에서 해당 u_id 제거
                        databaseRef = database.getReference("project").child("RoomUsers").child(room_id).child(LoginActivity.u_id);
                        databaseRef.removeValue();

                        // UsersRoom에서 해당 room_id 제거
                        databaseRef = database.getReference("project").child("UsersRoom").child(LoginActivity.u_id).child(room_id);
                        databaseRef.removeValue();

                        dialog.cancel();
                        finish();
                    }
                });
                dialog.show();
            }
        });
    }
}
