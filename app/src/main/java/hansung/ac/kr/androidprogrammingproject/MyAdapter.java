package hansung.ac.kr.androidprogrammingproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<ChattingRoomList> dataList;
    //Context context;

    public MyAdapter(ArrayList<ChattingRoomList> dataList) {
        this.dataList = dataList;
        //this.context = context;
    }

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1, textView2;
        public ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            // itemView에서 textView의 참조를 얻습니다. textView의 ID는 레이아웃 파일에서 정의해야 합니다.
            imgView= itemView.findViewById(R.id.img);
            textView1 = itemView.findViewById(R.id.tv1);
            textView2 = itemView.findViewById(R.id.tv2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 새로운 뷰를 생성합니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_room_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 데이터를 뷰 홀더의 뷰에 바인딩합니다.
        ChattingRoomList dataModel = dataList.get(position);

        holder.textView1.setText(dataModel.getNickname());
        holder.textView2.setText(dataModel.getLastMessage());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
