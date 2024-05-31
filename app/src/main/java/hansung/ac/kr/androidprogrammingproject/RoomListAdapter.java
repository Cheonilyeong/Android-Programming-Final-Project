package hansung.ac.kr.androidprogrammingproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {
    ArrayList<ChattingRoomList> dataList;
    //Context context;

    public RoomListAdapter(ArrayList<ChattingRoomList> dataList) {
        this.dataList = dataList;
        //this.context = context;
    }

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_userImage;
        public TextView textView1, textView2;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_userImage = itemView.findViewById(R.id.img);
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
