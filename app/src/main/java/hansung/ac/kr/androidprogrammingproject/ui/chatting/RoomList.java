package hansung.ac.kr.androidprogrammingproject.ui.chatting;

public class RoomList {

    private String room_id;
    private String time;
    private String lastMessage;

    public RoomList() {}

    public RoomList(String room_id, String time, String lastMessage) {
        this.room_id = room_id;
        this.time = time;
        this.lastMessage = lastMessage;
    }

    public String getRoom_id() { return room_id; }
    public void setRoom_id(String room_id) { this.room_id = room_id; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
}
