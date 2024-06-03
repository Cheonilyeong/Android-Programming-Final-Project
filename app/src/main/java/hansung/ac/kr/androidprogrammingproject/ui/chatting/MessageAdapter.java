package hansung.ac.kr.androidprogrammingproject.ui.chatting;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hansung.ac.kr.androidprogrammingproject.LoginActivity;
import hansung.ac.kr.androidprogrammingproject.R;
import hansung.ac.kr.androidprogrammingproject.UserAccount;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    ArrayList<Message> dataList;    // message

    public MessageAdapter(ArrayList<Message> dataList) {
        this.dataList = dataList;
    }

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_msg0;
        public TextView tv_nickname1, tv_msg1;
        public TextView tv_nickname2, tv_msg2;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_msg0 = itemView.findViewById(R.id.tv_msg0);
            tv_nickname1 = itemView.findViewById(R.id.tv_nickname1);
            tv_msg1 = itemView.findViewById(R.id.tv_msg1);
            tv_nickname2 = itemView.findViewById(R.id.tv_nickname2);
            tv_msg2 = itemView.findViewById(R.id.tv_msg2);
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

        Log.d("u_id", dataModel.getU_id());
        Log.d("LoginActivity.u_id", LoginActivity.u_id);

        // 사용자 닉네임 받아오기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("project").child("UserAccount").child(dataModel.getU_id());
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                String nickname = userAccount.getNickName().toString();

                // 비동기니 여기서 처리하자.
                if(dataModel.getMessageType() == Message.ENTER) {}
                else if(dataModel.getU_id().equals(LoginActivity.u_id)) {
                    holder.tv_nickname2.setText(nickname);
                }
                else {
                    holder.tv_nickname1.setText(nickname);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 입장 메세지면
        if(dataModel.getMessageType() == Message.ENTER) {
            ViewGroup parent = (ViewGroup) holder.tv_nickname1.getParent();
            parent.removeView(holder.tv_nickname1);
            parent.removeView(holder.tv_msg1);
            parent.removeView(holder.tv_nickname2);
            parent.removeView(holder.tv_msg2);

            holder.tv_msg0.setText(dataModel.getMessage());
        }
        // 내가 쓴 Message면 오른쪽에 배치
        else if(dataModel.getU_id().equals(LoginActivity.u_id)) {
            ViewGroup parent = (ViewGroup) holder.tv_nickname1.getParent();
            parent.removeView(holder.tv_msg0);
            parent.removeView(holder.tv_nickname1);
            parent.removeView(holder.tv_msg1);

            holder.tv_msg2.setText(dataModel.getMessage());
        }
        // 상대방이 쓴 Message면 왼쪽에 배치
        else {
            ViewGroup parent = (ViewGroup) holder.tv_nickname1.getParent();
            parent.removeView(holder.tv_msg0);
            parent.removeView(holder.tv_nickname2);
            parent.removeView(holder.tv_msg2);

            holder.tv_msg1.setText(dataModel.getMessage());
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
