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
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import hansung.ac.kr.androidprogrammingproject.ModificationActivity;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;
import hansung.ac.kr.androidprogrammingproject.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;          // 파이어베이스 인증처리
    private FirebaseUser firebaseUser;          // 파이어베이스 유저
    private FirebaseDatabase database;          // 데이터베이스 인스턴스
    private DatabaseReference databaseRef;      // 데이터베이스 레퍼런스
    private FirebaseStorage storage;            // 스토리지 인스턴스
    private StorageReference storageRef;        // 스토리지 레퍼런스

    private FragmentProfileBinding binding;     // ProfileFragment 바인딩

    private CircleImageView iv_profile;         // profile
    private TextView tv_email;                  // e-mail
    private TextView tv_nickname;               // nickname
    private TextView tv_information;            // information
    
    private Button btn_modification;            // 프로필 수정 버튼
    private Button btn_logout;                  // 로그아웃 버튼
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "Profile onCreate 호출됨");
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("onCreateView", "Profile onCreateView 호출됨");

        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        // ProfileFragment 바인딩
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // User Profile 정보
        iv_profile = root.findViewById(R.id.iv_profile);
        tv_email = root.findViewById(R.id.tv_email);
        tv_nickname = root.findViewById(R.id.tv_nickname);
        tv_information = root.findViewById(R.id.tv_information);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project").child("UserAccount");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        if (firebaseUser != null) {
            String currentUserId = firebaseUser.getUid(); // 현재 로그인한 사용자의 UID

            Log.d("Profile", "Get FirebaseUser");
            // 현재 로그인한 사용자의 정보를 Database에서 읽어오기
            databaseRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);

                    // 로그인한 사용자의 정보를 활용합니다.
                    if (userAccount != null) {
                        String email = userAccount.getEmail();
                        String nickname = userAccount.getNickName();
                        String information = userAccount.getInformation();
                        String imageURL = userAccount.getImageURL();
                        // 예: 화면에 사용자 정보를 표시
                        Log.d("UserInfo", "Email: " + email + ", Nickname: " + nickname + ", Information: " + information + ", ImageUri: " + imageURL);

                        if(imageURL.equals("")) iv_profile.setImageResource(R.drawable.basicimage);
                        else downloadImage(imageURL);
                        tv_email.setText(email);
                        tv_nickname.setText(nickname);
                        tv_information.setText(information);
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
        btn_modification = root.findViewById(R.id.btn_modification);
        btn_modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ModificationActivity.class);
                startActivity(intent);
            }
        });

        // 로그아웃 버튼
        btn_logout = root.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
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
    public void downloadImage(String imageURL) {
        StorageReference imageRef = storageRef.child(imageURL);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // 다운로드 URL이 성공적으로 가져왔으면
                // 이미지의 URL 가져오기
                loadImageIntoImageView(uri.toString());
                Log.d("downloadImage", "downloadImage onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // URL을 가져오는 데 실패했을 때
                // 기본사진으로
                iv_profile.setImageResource(R.drawable.basicimage);
                Log.e("downloadImage", "downloadImage onFailure");
            }
        });
    }
    // 이미지 저장
    public void loadImageIntoImageView(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(iv_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("onViewCreated", "Profile onViewCreated 호출됨");
        super.onViewCreated(view ,savedInstanceState);
    }
    @Override
    public void onStart() {
        Log.d("onStart", "Profile onStart 호출됨");
        super.onStart();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}