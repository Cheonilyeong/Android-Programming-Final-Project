package hansung.ac.kr.androidprogrammingproject.ui.home;

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

import hansung.ac.kr.androidprogrammingproject.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;      
    private RecyclerView.Adapter mAdapter;  
    private RecyclerView.LayoutManager layoutManager;

    private FragmentHomeBinding binding;
    // private ArrayList<DataModel> myDataset = new ArrayList<>(); // 데이터 리스트를 멤버 변수로 선언

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // 나머지 데이터 추가...
//        myDataset.add(new DataModel("이미지1","텍스트1","텍스트1"));
//        myDataset.add(new DataModel("이미지2","텍스트2","텍스트2"));
//        myDataset.add(new DataModel("이미지3","텍스트3","텍스트3"));
//        myDataset.add(new DataModel("이미지4","텍스트4","텍스트4"));
//        myDataset.add(new DataModel("이미지5","텍스트5","텍스트5"));
//        myDataset.add(new DataModel("이미지6","텍스트6","텍스트6"));
//        myDataset.add(new DataModel("이미지7","텍스트7","텍스트7"));
//        myDataset.add(new DataModel("이미지8","텍스트8","텍스트8"));
//        myDataset.add(new DataModel("이미지9","텍스트9","텍스트9"));
//        myDataset.add(new DataModel("이미지10","텍스트10","텍스트10"));
//        myDataset.add(new DataModel("이미지11","텍스트11","텍스트11"));

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        // HomeFragment 바인딩
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        recyclerView = root.findViewById(R.id.rv);
//        recyclerView.setHasFixedSize(true);
//
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//
//        mAdapter = new MyAdapter(myDataset);
//        recyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}