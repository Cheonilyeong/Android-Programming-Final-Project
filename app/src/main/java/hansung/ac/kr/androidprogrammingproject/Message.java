package hansung.ac.kr.androidprogrammingproject;

public class Message {
    private String message;
    private String u_id;
    private String time;

    public Message() {}

    public Message(String message, String u_id, String tim) {
        this.message = message;
        this.u_id = u_id;
        this.time = time;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getU_id() { return u_id; }
    public void setU_id(String u_id) { this.u_id = u_id; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
