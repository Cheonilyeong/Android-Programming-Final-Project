package hansung.ac.kr.androidprogrammingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private DatabaseReference databaseRef; // 실시간 데이터베이스

    private EditText etEmail, etPwd;        // ID and PWD
    private Button btnRegister;             // 회원 가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resigter);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("project");

        // 이메일, 패스워드
        etEmail = findViewById(R.id.et_email);
        etPwd = findViewById(R.id.et_pwd);

        // 회원가입 버튼
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = etEmail.getText().toString();
                String strPwd = etPwd.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 회원가입 성공
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            // 유저 객체 만들기
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseAuth.getUid());
                            account.setEmail(firebaseUser.getEmail());
                            account.setPassword(strPwd);

                            // 데이터베이스에 저장
                            databaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        }
                        // 회원가입 실패
                        else {
                            // 오류 메시지 출력
                            // 비밀번호가 6글자 아래면 오류가 뜨더라
                            String errorCode = task.getException().getMessage();
                            Log.e("FirebaseAuth", "회원가입 실패: " + errorCode);
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}