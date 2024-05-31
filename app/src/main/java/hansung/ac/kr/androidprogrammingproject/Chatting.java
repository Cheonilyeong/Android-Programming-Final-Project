package hansung.ac.kr.androidprogrammingproject;

public class Chatting {
    private String message;
    private String u_id;
    private String nickname;
    private String time;
    private String imageURL;

    public Chatting() {}

    public Chatting(String message, String u_id, String nickname, String time, String imageURL) {
        this.message = message;
        this.u_id = u_id;
        this.nickname = nickname;
        this.time = time;
        this.imageURL = imageURL;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getU_id() { return u_id; }
    public void setU_id(String u_id) { this.u_id = u_id; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}
