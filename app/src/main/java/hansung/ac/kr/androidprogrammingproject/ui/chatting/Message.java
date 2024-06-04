package hansung.ac.kr.androidprogrammingproject.ui.chatting;

public class Message {

    public static final int ENTER = 1;
    public static final int COMEOUT = 2;
    public static final int MESSAGE = 3;
    private int messageType;
    private String message;
    private String u_id;
    private String time;

    public Message() {}

    public Message(int messageType, String message, String u_id, String time) {
        this.messageType = messageType;
        this.message = message;
        this.u_id = u_id;
        this.time = time;
    }

    public int getMessageType() { return messageType; }
    public void setMessageType(int messageType) { this.messageType = messageType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getU_id() { return u_id; }
    public void setU_id(String u_id) { this.u_id = u_id; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
