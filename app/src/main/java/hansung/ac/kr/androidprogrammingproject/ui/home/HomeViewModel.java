package hansung.ac.kr.androidprogrammingproject.ui.home;

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

public class HomeViewModel extends ViewModel {

    private FirebaseDatabase database;          // 데이터베이스 인스턴스
    private DatabaseReference databaseRef;      // 데이터베이스 레퍼런스

    private MutableLiveData<List<Post>> postDataset;   // Post 정보
    private MutableLiveData<Boolean> isLoading;             // Loading 정보

    public HomeViewModel() {
        postDataset = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(true);
        loadPostsFromFirebase();
    }

    // HomeFragment에 postDataset 전달
    // LiveData는 불변 객체라 HomeFragment에서 변경 불가능
    public LiveData<List<Post>> getPostDataset() {
        return postDataset;
    }

    // HomeFragment에 isLoading 전달
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    // 데이터 읽기
    private void loadPostsFromFirebase() {
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("Post");
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
        databaseRef.orderByKey().addChildEventListener(new ChildEventListener() {
            // 게시물 등록
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post post = snapshot.getValue(Post.class);
                List<Post> posts;

                if(postDataset.getValue() == null) posts = new ArrayList<>();
                else posts = postDataset.getValue();

                posts.add(post);
                postDataset.setValue(posts);
                isLoading.setValue(false);
            }
            // 게시물 수정
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue() == null) return;

                Post post = snapshot.getValue(Post.class);
                List<Post> posts = postDataset.getValue();

                for(int i = 0; i < posts.size(); i++) {
                    if(posts.get(i).getPost_id().equals(post.getPost_id())) {
                        posts.set(i, post);
                    }
                }

                postDataset.setValue(posts);
                isLoading.setValue(false);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                List<Post> posts = postDataset.getValue();

                for(int i = 0; i < posts.size(); i++) {
                    if(posts.get(i).getPost_id().equals(post.getPost_id())) {
                        posts.remove(i);
                        break;
                    }
                }

                postDataset.setValue(posts);
                isLoading.setValue(false);
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}