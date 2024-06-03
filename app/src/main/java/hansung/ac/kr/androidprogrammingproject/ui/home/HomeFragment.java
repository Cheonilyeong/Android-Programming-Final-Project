package hansung.ac.kr.androidprogrammingproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.databinding.FragmentHomeBinding;
import hansung.ac.kr.androidprogrammingproject.ui.chatting.RoomList;
import hansung.ac.kr.androidprogrammingproject.ui.chatting.RoomListAdapter;

public class HomeFragment extends Fragment {

    
    private RecyclerView recyclerView;          // 리사이클러 뷰 
    private PostListAdapter postListAdapter;    // 어뎁터
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Post> postDataset = new ArrayList<>(); // 데이터 리스트를 멤버 변수로 선언
    
    private FragmentHomeBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "Home onCreate 호출됨");
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("onCreateView", "Home onCreateView 호출됨");

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        // HomeFragment 바인딩
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 게시물 추가 버튼
        ImageView iv_addPost = root.findViewById(R.id.iv_addPost);
        iv_addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        // RecyclerView
        recyclerView = binding.rv;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        postListAdapter = new PostListAdapter();
        recyclerView.setAdapter(postListAdapter);

        // 데이터 관찰 및 로딩 상태 처리
        homeViewModel.getPostDataset().observe(getViewLifecycleOwner(), posts -> {
            postListAdapter.setPostList(posts);
        });

        homeViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if(isLoading) { recyclerView.setVisibility(View.GONE); }
            else { recyclerView.setVisibility(View.VISIBLE); }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("onViewCreated", "Home onViewCreated 호출됨");
        super.onViewCreated(view ,savedInstanceState);
    }
    @Override
    public void onStart() {
        Log.d("onStart", "Home onStart 호출됨");
        super.onStart();
    }
    @Override
    public void onDestroyView() {
        Log.d("onDestroyView", "Home onDestroyView 호출됨");
        super.onDestroyView();
        binding = null;
    }
}