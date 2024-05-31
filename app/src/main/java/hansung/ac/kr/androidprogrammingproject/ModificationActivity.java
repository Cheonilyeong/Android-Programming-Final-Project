package hansung.ac.kr.androidprogrammingproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ModificationActivity extends Activity {

    private final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri = null;
    private String uploadedImageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);

        // Image 수정 버튼
        Button btn_image = findViewById(R.id.btn_image);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        // 데이터베이스 데이터 수정하기
        Button btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미지 업로드
                uploadImage();
                save();
                finish();
            }
        });
    }

    // 이미지 업로드 (스토리지에서 고르기)
    private void uploadImage() {
        if(selectedImageUri == null) return;

        // Storage 참조 생성
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference fileReference = storageReference.child("profile/" + uploadedImageUri + ".jpg");

        try {
            // 파일이 존재하는지 확인하기 위해 InputStream 열기
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            if (inputStream != null) {
                inputStream.close();

                // 업로드 작업 시작
                fileReference.putFile(selectedImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // 업로드 성공 시
                                Log.d("putFile", "Upload Success: " + uploadedImageUri);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // 업로드 실패 시
                                Log.d("putFile", "Upload Failed: " + e.getMessage());
                            }
                        });
            } else {
                throw new FileNotFoundException("Selected image file not found.");
            }
        } catch (FileNotFoundException e) {
            Log.e("uploadImage", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e("uploadImage", "Error closing InputStream: " + e.getMessage());
        }
    }

    // 프로필 수정
    private void save() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("project").child("UserAccount");

        if (firebaseUser != null) {
            String currentUserId = firebaseUser.getUid(); // 현재 로그인한 사용자의 UID

            Log.d("Update", "Get FirebaseUser");
            // 현재 로그인한 사용자 정보 읽어오기
            databaseRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserAccount account = dataSnapshot.getValue(UserAccount.class);
                    // 로그인한 사용자의 정보
                    if (account != null) {
                        EditText et_nickname = findViewById(R.id.et_nickname);
                        EditText et_information = findViewById(R.id.et_information);

                        account.setNickName(et_nickname.getText().toString());
                        account.setInformation(et_information.getText().toString());
                        if(uploadedImageUri != null) account.setImageURL("/profile/" + uploadedImageUri + ".jpg");
                        // 다시 저장
                        databaseRef.child(currentUserId).setValue(account);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("UserInfo", "loadUser:onCancelled", databaseError.toException());
                }
            });
        }
        else Log.w("Update", "No FirebaseUser");
    }

    // 프로필 이미지 고르기
    private void selectImage() {
        // 이미지를 고르는 매니저(Intent)를 시작하고 결과를 ActivityResult로 줘라
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    // 선택하면
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Image 선택의 경우면
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            uploadedImageUri = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            // Image 바꾸기 (아직 서버에 저장은 X)
            CircleImageView iv_image = findViewById(R.id.iv_userImage);
            iv_image.setImageURI(selectedImageUri);
        }
    }

}
