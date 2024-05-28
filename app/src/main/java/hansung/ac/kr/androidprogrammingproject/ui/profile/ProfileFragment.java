package hansung.ac.kr.androidprogrammingproject.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;
import hansung.ac.kr.androidprogrammingproject.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;          // 파이어베이스 인증처리
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseRef;      // 실시간 데이터 베이스
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private FragmentProfileBinding binding;

    // User Profile
    private TextView tv_userName;
    private TextView tv_userNickname;
    private TextView tv_userInformation;
    private CircleImageView iv_userImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        // ProfileFragment 바인딩
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // User Profile 정보
        iv_userImage = root.findViewById(R.id.iv_userImage);
        tv_userName = root.findViewById(R.id.tv_name);
        tv_userNickname = root.findViewById(R.id.tv_nickname);
        tv_userInformation = root.findViewById(R.id.tv_information);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseRef = FirebaseDatabase.getInstance().getReference("project").child("UserAccount");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        if (firebaseUser != null) {
            String currentUserId = firebaseUser.getUid(); // 현재 로그인한 사용자의 UID

            Log.d("Profile", "Get FirebaseUser");
            // 현재 로그인한 사용자의 정보를 Database에서 읽어오기
            databaseRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserAccount account = dataSnapshot.getValue(UserAccount.class);

                    // 로그인한 사용자의 정보를 활용합니다.
                    if (account != null) {
                        String email = account.getEmail();
                        String nickname = account.getNickName();
                        String information = account.getInformation();
                        String imageUrl = account.getImageURL();
                        // 예: 화면에 사용자 정보를 표시
                        Log.d("UserInfo", "Email: " + email + ", Nickname: " + nickname + ", Information: " + information + ", ImageUri: " + imageUrl);

                        downloadImage(imageUrl);
                        tv_userName.setText(email);
                        tv_userNickname.setText(nickname);
                        tv_userInformation.setText(information);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("UserInfo", "loadUser:onCancelled", databaseError.toException());
                }
            });
        }
        else Log.w("Profile", "No FirebaseUser");

        // 정보 수정으로 이동
        Button btn_modification = root.findViewById(R.id.btn_modification);
        btn_modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ModificationActivity.class);
                startActivity(intent);
            }
        });

        // 로그아웃 버튼
        Button btnLogout = root.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return root;
    }

    // 이미지 로드
    public void downloadImage(String imageUrl) {
        StorageReference imageRef = storageRef.child(imageUrl);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // 다운로드 URL이 성공적으로 가져와졌을 때
                // uri.toString()을 사용하여 이미지의 URL을 가져올 수 있습니다.
                loadImageIntoImageView(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // URL을 가져오는 데 실패했을 때의 처리
                // 기본 사진으로
            }
        });
    }
    // 이미지 저장
    public void loadImageIntoImageView(String imageUrl) {

        Glide.with(this)
                .load(imageUrl)
                .into(iv_userImage);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}