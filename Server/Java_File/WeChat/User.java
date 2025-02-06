package WeChat;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

public class User implements Serializable {
    public static String user_account = null;
    public static String user_nickName = null;
    public static String user_file_path =
            System.getProperty("user.dir") + "/";
    public static User_message user_message;
    public static LinkedList<User_message> user_message_list = new LinkedList<>();
    @Serial
    private static final long serialVersionUID = 1L;
    public String nickName;
    public String account;
    public String password;
    public String  avatar_file_path;
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAvatar_file_path(String avatar_file_path) {
        this.avatar_file_path = avatar_file_path;
    }
}
