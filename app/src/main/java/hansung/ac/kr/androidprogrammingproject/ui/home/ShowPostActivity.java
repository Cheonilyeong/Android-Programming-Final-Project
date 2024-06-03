package hansung.ac.kr.androidprogrammingproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.ui.chatting.RoomActivity;

public class ShowPostActivity extends AddPostActivity{

    private String u_id;
    private String title;
    private String kindOf;
    private String food;
    private String content;
    private String preson;
    private String day;
    private String time;

    private ImageView iv_back;
    private TextView tv_title, tv_kindOf, tv_food, tv_person, tv_day, tv_time, tv_chatting;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpost);

        Intent intent = getIntent();
        String u_id = intent.getStringExtra("u_id");
        String title = intent.getStringExtra("title");
        String kindOf = intent.getStringExtra("kindOf");
        String food = intent.getStringExtra("food");
        String day = intent.getStringExtra("day");
        String time = intent.getStringExtra("time");
        String content = intent.getStringExtra("content");
        String person = intent.getStringExtra("person");


        // 뒤로 가기 버튼
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 제목
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);

        // 분야
        tv_kindOf = findViewById(R.id.tv_kindOf);
        tv_kindOf.setText(kindOf);

        // 음식
        tv_food = findViewById(R.id.tv_food);
        tv_food.setText(food);

        // 날짜
        tv_day = findViewById(R.id.tv_day);
        tv_day.setText(day);

        // 시간
        tv_time = findViewById(R.id.tv_time);
        tv_time.setText(time);

        // 본문
        et_content = findViewById(R.id.et_content);
        et_content.setText(content);

        // 인원
        tv_person = findViewById(R.id.tv_person);
        tv_person.setText(person);

        // 채팅하러 가기
        tv_chatting = findViewById(R.id.tv_chatting);
        tv_chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowPostActivity.this, RoomActivity.class);
                // 이미 채팅 방에 속해있는지

                // 방은 포스트에 매칭시켜 하나씩

                // 내가 작성자라면?
                startActivity(intent);
            }
        });
    }
}
