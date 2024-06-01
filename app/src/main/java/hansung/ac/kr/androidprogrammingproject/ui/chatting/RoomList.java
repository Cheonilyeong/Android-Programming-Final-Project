package hansung.ac.kr.androidprogrammingproject.ui.chatting;

public class RoomList {

    private String room_id;
    private String u_id;
    private String lastMessage;

    public RoomList() {}

    public RoomList(String room_id, String u_id, String lastMessage) {
        this.room_id = room_id;
        this.u_id = u_id;
        this.lastMessage = lastMessage;
    }

    public String getRoom_id() { return room_id; }
    public void setRoom_id(String room_id) { this.room_id = room_id; }
    public String getU_id() { return u_id; }
    public void setU_id(String u_id) { this.u_id = u_id; }
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
}
