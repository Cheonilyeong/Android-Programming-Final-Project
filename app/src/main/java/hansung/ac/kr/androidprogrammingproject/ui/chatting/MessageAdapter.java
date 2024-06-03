package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    ArrayList<Message> dataList;    // message

    public MessageAdapter(ArrayList<Message> dataList) {
        this.dataList = dataList;
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
        // 새로운 뷰를 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 데이터를 뷰 홀더의 뷰에 바인딩
        Message dataModel = dataList.get(position);

        holder.tv_msg.setText(dataModel.getMessage());

        Log.d("u_id", dataModel.getU_id());
        Log.d("LoginActivity.u_id", LoginActivity.u_id);
        // 내가 쓴 Message면 오른쪽에 배치
        if(dataModel.getU_id().equals(LoginActivity.u_id)) {
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.tv_nickname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void addChatting(Message message) {
        dataList.add(message);
        notifyItemInserted(dataList.size()-1);
    }
}
