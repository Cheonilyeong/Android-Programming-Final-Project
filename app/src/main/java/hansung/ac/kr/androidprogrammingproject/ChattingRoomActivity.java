package hansung.ac.kr.androidprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hansung.ac.kr.androidprogrammingproject.databinding.FragmentChattingBinding;

public class ChattingRoomActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;     // 파이어베이스 인증처리
    private DatabaseReference databaseRef; // 실시간 데이터베이스

    private RecyclerView recyclerView;
    private ChattingAdapter chattingAdapter;
    //private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Chatting> chattingDataset = new ArrayList<>(); // 데이터 리스트를 멤버 변수로 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        chattingAdapter = new ChattingAdapter(chattingDataset);

        //Intent intent = getIntent();
        //String RoomID = intent.getStringExtra("RoomID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("Room");

        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chatting chatting = snapshot.getValue(Chatting.class);
                chattingAdapter.addChatting(chatting);
                recyclerView.scrollToPosition(chattingAdapter.getItemCount() - 1);
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

        recyclerView.setAdapter(chattingAdapter);

        EditText et_msg = findViewById(R.id.et_msg);
        Button btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_msg.getText().toString();
                if(msg.equals("") || msg.trim().equals("")) return;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                Chatting chatting = new Chatting(msg,"123","nickname",timeStamp, "imageURL");
                databaseRef.push().setValue(chatting);

                et_msg.setText("");
            }
        });
    }

}
