package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hansung.ac.kr.androidprogrammingproject.ChattingRoomList;
import hansung.ac.kr.androidprogrammingproject.RoomListAdapter;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.databinding.FragmentChattingBinding;

public class ChattingFragment extends Fragment {

    private FirebaseAuth firebaseAuth;     // 파이어베이스 인증처리
    private DatabaseReference databaseRef; // 실시간 데이터베이스

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ChattingRoomList> chattingRoomDataset = new ArrayList<>(); // 데이터 리스트를 멤버 변수로 선언
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

        databaseRef = FirebaseDatabase.getInstance().getReference("project").child("UsersRoom");

        // 데이터 읽기
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터를 읽어올 수 있음
                chattingRoomDataset.clear(); // 기존 데이터 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChattingRoomList chattingRoom = snapshot.getValue(ChattingRoomList.class);
                    chattingRoomDataset.add(chattingRoom);
                }

                // 데이터가 갱신되었음을 알림
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 읽기 실패 시 호출
                Log.w("DatabaseError", "loadPost:onCancelled", databaseError.toException());
            }
        });

        mAdapter = new RoomListAdapter(chattingRoomDataset);
        recyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}