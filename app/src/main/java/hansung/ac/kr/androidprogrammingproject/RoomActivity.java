package hansung.ac.kr.androidprogrammingproject;

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

public class RoomActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Message> messageDataset = new ArrayList<>(); // 데이터 리스트를 멤버 변수로 선언

    private TextView tv_nickname;
    private ImageView iv_profile;

    private String room_id;
    private String u_id;
    private String nickname;
    private String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        messageAdapter = new MessageAdapter(messageDataset);

        Intent intent = getIntent();
        room_id = intent.getStringExtra("room_id");
        u_id = intent.getStringExtra("u_id");
        Log.d("Enter Room", "Room_id: " + room_id + " u_id: " + u_id);

        // Room 상대방 nickname, imageURL 구하기
        tv_nickname = findViewById(R.id.tv_nickname);
        iv_profile = findViewById(R.id.iv_profile);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("UserAccount").child(u_id);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                nickname = userAccount.getNickName();
                imageURL = userAccount.getImageURL();

                tv_nickname.setText(nickname);
                downloadImage(imageURL);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        //

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

        recyclerView.setAdapter(messageAdapter);

        EditText et_msg = findViewById(R.id.et_msg);
        Button btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_msg.getText().toString();
                if(msg.equals("") || msg.trim().equals("")) return;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                Message message = new Message(msg, LoginActivity.u_id, timeStamp);
                databaseRef = database.getReference("project").child("Room").child(room_id);
                databaseRef.push().setValue(message);
                lastMessage(message.getMessage());

                et_msg.setText("");
            }
        });
    }

    // lastMessage 수정
    public void lastMessage(String msg) {
        databaseRef = database.getReference("project").child("UsersRoom").child(u_id);
        databaseRef.child(room_id).child("lastMessage").setValue(msg);

        databaseRef = database.getReference("project").child("UsersRoom").child(LoginActivity.u_id);
        databaseRef.child(room_id).child("lastMessage").setValue(msg);
    }
    // 이미지 로드
    public void downloadImage(String imageUrl) {
        StorageReference imageRef = storageRef.child(imageUrl);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // 다운로드 URL이 성공적으로 가져와졌을 때
                // uri.toString()을 사용하여 이미지의 URL을 가져올 수 있습니다.
                loadImageIntoImageView(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // URL을 가져오는 데 실패했을 때의 처리
                // 기본 사진으로
                loadImageIntoImageView("/profile/NULL.jpg");
            }
        });
    }
    // 이미지 저장
    public void loadImageIntoImageView(String imageUrl) {

        Glide.with(this)
                .load(imageUrl)
                .into(iv_profile);
    }
}
