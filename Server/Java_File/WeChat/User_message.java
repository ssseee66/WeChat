package WeChat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class User_message implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    public String friend_account;
    public String friend_nickName;
    public String friend_name;
    public String friend_name_for_add;
    public String friend_signature = "";
    public String friend_region = "";
    public String friend_city = "";
    public String hello_text = "";
    public String user_account;
    public String user_nickName;
    public String chat_text;
    public byte[] friend_avatar;
    public String last_text;
    public String last_time;
    public String object_type;
    public String me_account;
    public String me_nickName;
    public String me_signature = "";
    public String me_region = "";
    public String me_city = "";
    public boolean accept = false;
    public LocalDateTime last_chat_time;
    public void setFriend_account(String friend_account) {
        this.friend_account = friend_account;
    }
    public String getFriend_account() {
        return friend_account;
    }
    public void setFriend_nickName(String friend_nickName) {
        this.friend_nickName = friend_nickName;
    }
    public String getFriend_nickName() {
        return friend_nickName;
    }
    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }
    public String getFriend_name() {
        return friend_name;
    }
    public void setFriend_signature(String friend_signature) {
        this.friend_signature = friend_signature;
    }
    public String getFriend_signature() {
        return friend_signature;
    }
    public void setFriend_region(String friend_region) {
        this.friend_region = friend_region;
    }
    public String getFriend_region() {
        return friend_region;
    }
    public void setFriend_city(String friend_city) {
        this.friend_city = friend_city;
    }
    public String getFriend_city() {
        return friend_city;
    }
    public void setHello_text(String hello_text) {
        this.hello_text = hello_text;
    }
    public String getHello_text() {
        return hello_text;
    }
    public void setMe_account(String me_account) {
        this.me_account = me_account;
    }
    public String getMe_account() {
        return me_account;
    }
    public void setChat_text(String chat_text) {
        this.chat_text = chat_text;
    }
    public void setLast_text(String last_text) {
        this.last_text = last_text;
    }
    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }
    public void setMe_nickName(String me_nickName) {
        this.me_nickName = me_nickName;
    }
    public void setAccept(boolean accept) {
        this.accept = accept;
    }
    public void setLast_chat_time(LocalDateTime last_chat_time) {
        this.last_chat_time = last_chat_time;
    }
    public void setMe_signature(String me_signature) {
        this.me_signature = me_signature;
    }
    public void setMe_region(String me_region) {
        this.me_region = me_region;
    }
    public void setMe_city(String me_city) {
        this.me_city = me_city;
    }
}
