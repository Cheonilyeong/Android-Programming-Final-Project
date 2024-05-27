package hansung.ac.kr.androidprogrammingproject;

import android.net.Uri;

import java.net.URI;

public class UserAccount {

    private String idToken;     // firebase의 토큰 (Uid)
    private String email;       // 이메일 ID
    private String password;    // 비밀번호
    private String nickname;    // 별명
    private String information; // 한 줄 소개
    private String imageURL;    // 사진

    public UserAccount() { }

    public String getIdToken() { return idToken;}
    public void setIdToken(String idToken) { this.idToken = idToken;}
    public String getEmail() { return email;}
    public void setEmail(String email) { this.email = email;}
    public String getPassword(){ return password;}
    public void setPassword(String password) { this.password = password;}
    public String getNickName() { return nickname; };
    public void setNickName(String nickname) { this.nickname = nickname; }
    public String getInformation() { return information; }
    public void setInformation(String information) { this.information = information; }
    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
}
