package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hansung.ac.kr.androidprogrammingproject.ChattingRoomList;
import hansung.ac.kr.androidprogrammingproject.MyAdapter;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.databinding.FragmentChattingBinding;

public class ChattingFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ChattingRoomList> myDataset = new ArrayList<>(); // 데이터 리스트를 멤버 변수로 선언
    private FragmentChattingBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //    private String room_id;
        //    private String nickname;
        //    private String u_id;
        //    private String imageURL;
        //    private String lastMessage;
        //    private String time;

        // 나머지 데이터 추가...
        myDataset.add(new ChattingRoomList("0001","홍길동","1", "", "뭐해?", "오후 04:53"));
        myDataset.add(new ChattingRoomList("0002","고길동","2", "", "배고파ㅋㅋ", "오후 04:53"));
        myDataset.add(new ChattingRoomList("0003","철수","3", "", "ㅋㅋㅋㅊㅋ", "오후 04:53"));
        myDataset.add(new ChattingRoomList("0004","영희","4", "", "헐~", "오후 04:53"));
        myDataset.add(new ChattingRoomList("0005","고등어","5", "", "대박!", "오후 04:53"));
        myDataset.add(new ChattingRoomList("0006","참치","6", "", "내일 어때?", "오후 04:53"));
        myDataset.add(new ChattingRoomList("0007","꽁치","7", "", "ㅠㅠ", "오후 04:53"));
        myDataset.add(new ChattingRoomList("0008","말미잘","8", "", "뭐해?", "오후 04:53"));
        myDataset.add(new ChattingRoomList("0009","홍어","9", "", "아...", "오후 04:53"));
        myDataset.add(new ChattingRoomList("00010","호랑이","10", "", "진짜?", "오후 04:53"));
        myDataset.add(new ChattingRoomList("00011","고라니","11", "", "크크쿸ㅋㅋ", "오후 04:53"));

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

        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}