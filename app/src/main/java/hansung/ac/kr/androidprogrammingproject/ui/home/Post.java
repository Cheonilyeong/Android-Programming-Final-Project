package hansung.ac.kr.androidprogrammingproject.ui.home;

public class Post {

    private String post_id;
    private String u_id;
    private String title;
    private String kindOf;
    private String food;
    private String content;
    private String person;
    private String day;
    private String time;

    public Post() {}
    public Post(String u_id, String title, String kindOf, String food, String content, String person, String day, String time) {
        this.u_id = u_id;
        this.title = title;
        this.kindOf = kindOf;
        this.food = food;
        this.content = content;
        this.person = person;
        this.day = day;
        this.time = time;
    }

    public String getPost_id() { return post_id; }
    public void setPost_id(String post_id) { this.post_id = post_id; }
    public String getU_id() { return u_id; }
    public void setU_id(String u_id) { this.u_id = u_id;}
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getKindOf() { return kindOf; }
    public void setKindOf(String kindOf) { this.kindOf = kindOf; }
    public String getFood() { return food; }
    public void setFood(String food) { this.food = food; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getPerson() { return person; }
    public void setPerson(String person) { this.person = person; }
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
