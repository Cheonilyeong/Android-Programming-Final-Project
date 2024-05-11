package hansung.ac.kr.androidprogrammingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import hansung.ac.kr.androidprogrammingproject.ui.chatting.ChattingFragment;
import hansung.ac.kr.androidprogrammingproject.ui.home.HomeFragment;
import hansung.ac.kr.androidprogrammingproject.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 첫번째 프레그먼트 띄우기
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
                }
                else if (itemId == R.id.navigation_chatting) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ChattingFragment()).commit();
                }
                else if (itemId == R.id.navigation_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ProfileFragment()).commit();
                }
                return true;
            }
        });
    }
}