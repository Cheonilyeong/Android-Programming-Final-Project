package hansung.ac.kr.androidprogrammingproject;

public class DataModel {
    String str[] = new String[3];
    int image_path;

    public String getStr0() {
        return str[0];
    }

    public void setStr0(String str0) {
        this.str[0] = str0;
    }

    public String getStr1() {
        return str[1];
    }

    public void setStr1(String str1) {
        this.str[1] = str1;
    }

    public String getStr2() {
        return str[2];
    }

    public void setStr2(String str2) {
        this.str[2] = str2;
    }

    public DataModel(String a, String b, String c) {
        this.str[0] = a;
        this.str[1] = b;
        this.str[2] = c;
    }
}