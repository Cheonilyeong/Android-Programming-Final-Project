package hansung.ac.kr.androidprogrammingproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ViewHolder> {
    ArrayList<Chatting> dataList;
    //Context context;

    public ChattingAdapter(ArrayList<Chatting> dataList) {
        this.dataList = dataList;
        //this.context = context;
    }

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_nickname, tv_msg;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_msg = itemView.findViewById(R.id.tv_msg);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 새로운 뷰를 생성합니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 데이터를 뷰 홀더의 뷰에 바인딩합니다.
        Chatting dataModel = dataList.get(position);

        holder.tv_nickname.setText(dataModel.getNickname());
        holder.tv_msg.setText(dataModel.getMessage());

        if(dataModel.getU_id() ==  "123") {
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void addChatting(Chatting chatting) {
        dataList.add(chatting);
        notifyItemInserted(dataList.size()-1);
    }
}
