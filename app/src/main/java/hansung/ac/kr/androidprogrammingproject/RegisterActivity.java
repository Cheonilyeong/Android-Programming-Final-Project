package hansung.ac.kr.androidprogrammingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;     // 파이어베이스 인증처리
    private FirebaseDatabase database;     // 데이터베이스 인스턴스
    private DatabaseReference databaseRef; // 데이터베이스 레퍼런스

    private EditText et_email, et_passwd;        // 회원가입 e_mail, 회원가입 passwd
    private EditText et_nickname, et_information; // 회원가입 nickname, 회원가입 information

    private ImageView iv_back;              // 뒤로 가기 버튼
    private Button btnRegister;             // 회원 가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resigter);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("project");

        // 이메일, 패스워드
        et_email = findViewById(R.id.et_e_mail);
        et_passwd = findViewById(R.id.et_passwd);
        et_nickname = findViewById(R.id.et_nickname);
        et_information = findViewById(R.id.et_information);

        // 뒤로 가기 버튼
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 회원가입 버튼
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = et_email.getText().toString();
                String passwd = et_passwd.getText().toString();
                String nickname = et_nickname.getText().toString();
                String information = et_information.getText().toString();

                if(email.equals("") || email.trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd.equals("") || passwd.trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(nickname.equals("") || nickname.trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(information.equals("") || information.trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "한 줄 소개를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 회원가입 성공
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            // 유저 객체 만들기
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseAuth.getUid());
                            account.setEmail(firebaseUser.getEmail());
                            account.setPassword(passwd);
                            account.setNickName(nickname);
                            account.setInformation(information);
                            account.setImageURL("/profile/NULL.jpg");

                            // 데이터베이스에 저장
                            databaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        // 회원가입 실패
                        else {
                            // 오류 메시지 출력
                            String errorCode = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, errorCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}