package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import hansung.ac.kr.androidprogrammingproject.databinding.FragmentChattingBinding;

public class ChattingFragment extends Fragment {

    private FragmentChattingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChattingViewModel chattingViewModel =
                new ViewModelProvider(this).get(ChattingViewModel.class);

        binding = FragmentChattingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}