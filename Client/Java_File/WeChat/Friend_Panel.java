package WeChat;
import javax.swing.*;
import java.util.*;

public class Friend_Panel extends JSplitPane {
    public String text;
    public Friend_Info friend;
    public JPanel panel;
    public JLabel label;
    public Mouse_Police mouse_police;
    public LinkedList<Mouse_Police> list_mouse_police;
    public LinkedList<Friend_Info> list_friend_info;
    //添加参数指定标签的联系人模块
    public Friend_Panel(String text) {
        list_friend_info = new LinkedList<>();
        this.text = text;
        setName(text);
        label = new JLabel(text);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        list_mouse_police = new LinkedList<>();
        setOrientation(JSplitPane.VERTICAL_SPLIT);
        setTopComponent(label);
        setBottomComponent(panel);
        setEnabled(false);
        setDividerSize(0);
        validate();
    }
    //创建新的朋友通知模块
    public Friend_Panel() {
        list_friend_info = new LinkedList<>();
        text = "无";
        label = new JLabel("新的朋友");
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        User_message user_message = new User_message();
        user_message.setFriend_name("新的朋友");
        user_message.setFriend_nickName("");
        user_message.setFriend_account("无效用户");
        user_message.object_type = "联系人";
        friend = new Friend_Info(user_message);
        mouse_police = new Mouse_Police();
        list_mouse_police = new LinkedList<>();
        mouse_police.setAvatar(friend.avatar);
        mouse_police.setFriend(friend);
        friend.avatar.addMouseListener(mouse_police);
        friend.addMouseListener(mouse_police);
        list_mouse_police.add(mouse_police);
        panel.add(friend);
        panel.add(Box.createVerticalStrut(10));
        setOrientation(JSplitPane.VERTICAL_SPLIT);
        setTopComponent(label);
        setBottomComponent(panel);
        setEnabled(false);
        setDividerSize(0);
        validate();
    }
    //添加联系人
    public void newContacts(Friend_Info friend) {
        this.friend = friend;
        AtContacts();
        panel.add(this.friend);
        list_friend_info.add(friend);
        panel.add(Box.createHorizontalStrut(10));
    }
    public void AtContacts() {
        mouse_police = new Mouse_Police();
        mouse_police.setAvatar(friend.avatar);
        mouse_police.setFriend(friend);
        friend.avatar.addMouseListener(mouse_police);
        friend.addMouseListener(mouse_police);
        list_mouse_police.add(mouse_police);
    }
}
