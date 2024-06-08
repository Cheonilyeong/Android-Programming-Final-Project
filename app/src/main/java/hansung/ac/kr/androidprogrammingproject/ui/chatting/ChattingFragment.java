package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.databinding.FragmentChattingBinding;

public class ChattingFragment extends Fragment {

    private RecyclerView recyclerView;          // 리사이클러 뷰 
    private RoomListAdapter roomListAdapter;    // 어뎁터
    private RecyclerView.LayoutManager layoutManager;
    
    private FragmentChattingBinding binding;    // ChattingFragment 바인딩

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "Chatting onCreate 호출됨");
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("onCreateView", "Chatting onCreateView 호출됨");

        ChattingViewModel chattingViewModel =
                new ViewModelProvider(this).get(ChattingViewModel.class);
        // ChattingFragment 바인딩
        binding = FragmentChattingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // RecyclerView
        recyclerView = root.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        roomListAdapter = new RoomListAdapter();
        roomListAdapter.setOnItemClickListener(new RoomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String room_id) {
                Intent intent = new Intent(getActivity(), RoomActivity.class);
                intent.putExtra("room_id", room_id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(roomListAdapter);

        // roomList
        chattingViewModel.getRoomListDataset().observe(getViewLifecycleOwner(), roomList -> {
            roomListAdapter.setRoomList(roomList);
        });

        chattingViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if(isLoading) {
                root.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            else {
                root.findViewById(R.id.progressbar).setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("onViewCreated", "Chatting onViewCreated 호출됨");
        super.onViewCreated(view ,savedInstanceState);
    }
    @Override
    public void onStart() {
        Log.d("onStart", "Chatting onStart 호출됨");
        super.onStart();
    }
    @Override
    public void onDestroyView() {
        Log.d("onDestroyView", "Chatting onDestroyView 호출됨");
        super.onDestroyView();
        binding = null;
    }
}