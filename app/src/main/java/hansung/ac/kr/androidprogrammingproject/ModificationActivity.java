package hansung.ac.kr.androidprogrammingproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;

public class ModificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);

        Button btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("project").child("UserAccount");

                if (firebaseUser != null) {
                    String currentUserId = firebaseUser.getUid(); // 현재 로그인한 사용자의 UID

                    Log.d("Update", "Get FirebaseUser");
                    // 현재 로그인한 사용자의 정보를 Database에서 읽어온다.
                    databaseRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);

                            // 로그인한 사용자의 정보
                            if (userAccount != null) {
                                EditText et_nickname = findViewById(R.id.et_nickname);
                                EditText et_information = findViewById(R.id.et_information);
                                userAccount.setNickName(et_nickname.getText().toString());
                                userAccount.setInformation(et_information.getText().toString());

                                // 다시 저장
                                databaseRef.child(currentUserId).setValue(userAccount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // 데이터베이스 업데이트 성공
                                            Log.d("Database Update", "User information updated successfully.");
                                        } else {
                                            // 데이터베이스 업데이트 실패
                                            Log.w("Database Update", "Error updating user information.", task.getException());
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("UserInfo", "loadUser:onCancelled", databaseError.toException());
                        }
                    });
                }
                else Log.w("Update", "No FirebaseUser");

                finish();
            }
        });


    }
}
