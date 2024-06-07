package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.ui.home.Post;

public class RoomViewModel extends ViewModel {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private String room_id;
    private MutableLiveData<Post> post;
    private MutableLiveData<List<Message>> messageDataset;

    public RoomViewModel() {
        post = new MutableLiveData<>();
        messageDataset = new MutableLiveData<>();
    }

    public LiveData<Post> getPost() { return post; }
    public LiveData<List<Message>> getMessageDataset() { return messageDataset; }

    public void setRoom_id(String room_id) { this.room_id = room_id; }

    public void loadRoomListFromFirebase() {
        // title 가져오기
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("Post").child(room_id);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post p = snapshot.getValue(Post.class);
                post.setValue(p);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 채팅 방 대화 가져오기
        databaseRef = database.getReference("project").child("Room").child(room_id);

        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                List<Message> messageList;

                if(messageDataset.getValue() == null) messageList = new ArrayList<>();
                else messageList = messageDataset.getValue();

                messageList.add(message);
                messageDataset.setValue(messageList);
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
    }
}
