package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.ui.home.Post;

public class ChattingViewModel extends ViewModel {

    private FirebaseDatabase database;          // 데이터베이스 인스턴스
    private DatabaseReference databaseRef;      // 데이터베이스 레퍼런스

    private MutableLiveData<List<RoomList>> roomListDataset;// RoomList 정보
    private MutableLiveData<Boolean> isLoading;             // Loading 정보

    public ChattingViewModel() {
        roomListDataset = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(true);
        loadRoomListFromFirebase();
    }

    // HomeFragment에 postDataset 전달
    // LiveData는 불변 객체라 HomeFragment에서 변경 불가능
    public LiveData<List<RoomList>> getRoomListDataset() {
        return roomListDataset;
    }

    // HomeFragment에 isLoading 전달
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // 데이터 읽기
    public void loadRoomListFromFirebase() {
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("UsersRoom").child(LoginActivity.u_id);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0) {
                    // 데이터가 없는 경우 처리
                    isLoading.setValue(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomList room = snapshot.getValue(RoomList.class);
                List<RoomList> roomList;
                if(roomListDataset.getValue() == null) roomList = new ArrayList<>();
                else roomList = roomListDataset.getValue();
                roomList.add(room);
                roomListDataset.setValue(roomList);
                isLoading.setValue(false);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomList room = snapshot.getValue(RoomList.class);
                List<RoomList> roomList;
                if(roomListDataset.getValue() == null) roomList = new ArrayList<>();
                else roomList = roomListDataset.getValue();

                for(int i = 0; i < roomList.size(); i++) {
                    if(roomList.get(i).getRoom_id().equals(room.getRoom_id())) {
                        roomList.set(i, room);
                    }
                }

                roomListDataset.setValue(roomList);
                isLoading.setValue(false);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                RoomList room = snapshot.getValue(RoomList.class);
                List<RoomList> roomList;
                if(roomListDataset.getValue() == null) roomList = new ArrayList<>();
                else roomList = roomListDataset.getValue();

                roomList.remove(room);

                roomListDataset.setValue(roomList);
                isLoading.setValue(false);
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

}