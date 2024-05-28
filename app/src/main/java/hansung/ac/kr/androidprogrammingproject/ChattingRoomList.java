package hansung.ac.kr.androidprogrammingproject;

public class ChattingRoomList {

    private String room_id;
    private String nickname;
    private String u_id;
    private String imageURL;
    private String lastMessage;
    private String time;

    public ChattingRoomList() {}

    public ChattingRoomList(String room_id, String nickname, String u_id, String imageURL, String lastMessage, String time) {
        this.room_id = room_id;
        this.nickname = nickname;
        this.u_id = u_id;
        this.imageURL = imageURL;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public String getRoom_id() { return room_id; }
    public void setRoom_id(String room_id) { this.room_id = room_id; }
    public String getNickname() {return nickname; }
    public void setNickname(String nickname) {this.nickname = nickname; }
    public String getU_id() { return u_id; }
    public void setU_id(String u_id) { this.u_id = u_id; }
    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
