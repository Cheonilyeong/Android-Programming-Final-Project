package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hansung.ac.kr.androidprogrammingproject.RoomActivity;
import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.databinding.FragmentChattingBinding;

public class ChattingFragment extends Fragment {

    private DatabaseReference databaseRef;

    private RecyclerView recyclerView;
    private RoomListAdapter roomListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RoomList> roomDataset = new ArrayList<>(); // 데이터 리스트를 멤버 변수로 선언
    private FragmentChattingBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChattingViewModel chattingViewModel =
                new ViewModelProvider(this).get(ChattingViewModel.class);
        // ChattingFragment 바인딩
        binding = FragmentChattingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        databaseRef = FirebaseDatabase.getInstance().getReference("project").child("UsersRoom").child(LoginActivity.u_id);

        // ChattingRoomList chattingRoomList = new ChattingRoomList("DVNyi6AOOqgKwbTN5dNsSSzT9Pw2", "nickname", "DVNyi6AOOqgKwbTN5dNsSSzT9Pw1", "/profile/20240527_113111.jpg",
        // "뭐해?", "20240531");
        // databaseRef.push().setValue(chattingRoomList);
        // roomDataset.add(chattingRoomList);

        roomListAdapter = new RoomListAdapter(roomDataset);
        roomListAdapter.setOnItemClickListener(new RoomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String room_id, String u_id) {
                databaseRef = FirebaseDatabase.getInstance().getReference("project").child("Room");
                databaseRef.push();

                Intent intent = new Intent(getActivity(), RoomActivity.class);
                intent.putExtra("room_id", room_id);
                intent.putExtra("u_id", u_id);
                startActivity(intent);
            }
        });


        // 데이터 읽기
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomList roomList = snapshot.getValue(RoomList.class);
                roomListAdapter.addChattingRoom(roomList);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RoomList roomList = snapshot.getValue(RoomList.class);
                roomListAdapter.changeLastMessage(roomList);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        recyclerView.setAdapter(roomListAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}