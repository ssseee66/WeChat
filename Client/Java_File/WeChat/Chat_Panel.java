package WeChat;

import javax.swing.*;
import java.util.LinkedList;

public class Chat_Panel extends JPanel {
    //联系人聊天对话对象
    public Friend_Info friend;
    //监视器
    public Mouse_Police mouse_police;
    //存放监视器对象的链表
    public LinkedList<Mouse_Police> list_mouse_police;
    Chat_Panel() {
        //此面板的布局为列式盒式布局
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(null);
        list_mouse_police = new LinkedList<>();

    }
    public void addContacts(Friend_Info friend) {
        this.friend = friend;
        AtContacts();
        add(this.friend);
        add(Box.createVerticalStrut(10));
    }
    public void AtContacts() {
        mouse_police = new Mouse_Police();
        mouse_police.setAvatar(friend.avatar);
        mouse_police.setFriend(friend);
        friend.addMouseListener(mouse_police);
        friend.avatar.addMouseListener(mouse_police);
        list_mouse_police.add(mouse_police);
    }
}
