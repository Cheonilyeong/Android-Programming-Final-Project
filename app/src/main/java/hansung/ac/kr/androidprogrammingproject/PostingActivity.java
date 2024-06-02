package hansung.ac.kr.androidprogrammingproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        EditText et_title = findViewById(R.id.et_title);
        RadioGroup rg = findViewById(R.id.rg);
        EditText et_food = findViewById(R.id.et_food);
        EditText et_content = findViewById(R.id.et_content);
        Spinner sp_person = findViewById(R.id.sp_person);
        TextView tv_day = findViewById(R.id.tv_day);
        Button btn_showDay = findViewById(R.id.btn_day);
        TextView tv_time = findViewById(R.id.tv_time);
        Button btn_showTime = findViewById(R.id.btn_time);
        Button btn_post = findViewById(R.id.btn_post);

        // 캘린더 뷰
        btn_showDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = View.inflate(PostingActivity.this, R.layout.dialog_day, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(PostingActivity.this);
                dlg.setView(dialogView);

                final AlertDialog dialog = dlg.create();

                CalendarView cv_day = dialogView.findViewById(R.id.cv_day);

                Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                Button btn_save = dialogView.findViewById(R.id.btn_save);
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 선택된 날짜 가져오기
                        long selectedDateMillis = cv_day.getDate();
                        Date selectedDate = new Date(selectedDateMillis);
                        // 선택된 날짜 정보 출력
                        String selectedDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate);

                        tv_day.setText(selectedDateString);

                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

        // 타임피커
        btn_showTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = View.inflate(PostingActivity.this, R.layout.dialog_time, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(PostingActivity.this);
                dlg.setView(dialogView);

                final AlertDialog dialog = dlg.create();

                TimePicker tp_time = dialogView.findViewById(R.id.tp_time);

                Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                Button btn_save = dialogView.findViewById(R.id.btn_save);
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 선택된 날짜 가져오기
                        String hour = Integer.toString(tp_time.getHour()%12);
                        String minute = Integer.toString(tp_time.getMinute());
                        if(tp_time.getHour()/12 == 0) tv_time.setText(hour + ":" + minute + " AM");
                        if(tp_time.getHour()/12 == 1) tv_time.setText(hour + ":" + minute + " PM");

                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

        // 글 등록
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 제목 확인
                String str_et_title = et_title.getText().toString();
                if(str_et_title.equals("") || str_et_title.trim().equals("")) {
                    Toast.makeText(PostingActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 분야 확인
                if(rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(PostingActivity.this, "분야를 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 음식 확인
                String str_et_food = et_food.getText().toString();
                if(str_et_food.equals("") || str_et_food.trim().equals("")) {
                    Toast.makeText(PostingActivity.this, "음식을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 본문 확인
                String str_et_content = et_content.getText().toString();
                if(str_et_content.equals("") || str_et_content.trim().equals("")) {
                    Toast.makeText(PostingActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 인원 확인
                if(sp_person.getSelectedItemPosition() == 0) {
                    Toast.makeText(PostingActivity.this, "인원을 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 날짜 확인
                String str_tv_day = tv_day.getText().toString();
                if(str_tv_day.equals("")) {
                    Toast.makeText(PostingActivity.this, "날짜를 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 시간 확인
                String str_tv_time = tv_time.getText().toString();
                if(str_tv_time.equals("")) {
                    Toast.makeText(PostingActivity.this, "시간을 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 등록 글 올리기
                // firebase push

            }
        });

    }
}
