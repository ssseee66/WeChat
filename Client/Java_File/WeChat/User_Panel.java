package WeChat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class User_Panel extends JPanel {
    /**
     * 此面板中的user_message中只包含了搜索到的用户的账号及其昵称
     */
    public  JButton add_user;
    public User_message user_message;
    public Action_Police action_police;
    public void setUser_message(User_message user_message) {
        this.user_message = user_message;
    }
    User_Panel(BufferedImage bufferedImage, String nickName) {
        user_message = new User_message();
        Box user_box = Box.createHorizontalBox();
        JButton avatar = new JButton();
        avatar.setBorderPainted(false);
        avatar.setContentAreaFilled(false);
        avatar.setName(nickName);
        SetImageIcon.SetIcon(bufferedImage, avatar, 60, 60);
        Font font = new Font("幼圆", Font.BOLD, 18);
        JLabel label = new JLabel();
        label.setFont(font);
        label.setText(nickName);
        user_box.add(avatar);
        user_box.add(Box.createHorizontalStrut(10));
        user_box.add(label);
        setLayout(null);
        setBorder(
                BorderFactory.createLineBorder(
                        Color.GREEN, 3, true));
        setBackground(Color.PINK);
        setSize(310, 120);
        add_user = new JButton("添加到通讯录");
        action_police = new Action_Police();
        action_police.setUser_panel(this);
        add_user.addActionListener(action_police);
        JPanel panels = new JPanel();
        panels.setLayout(null);
        panels.setBorder(
                BorderFactory.createMatteBorder(
                        3, 0, 0, 0, Color.CYAN));
        add_user.setBackground(Color.GREEN);
        add(user_box);
        user_box.setBounds(3, 5, 310, 60);
        panels.add(add_user);
        add_user.setBorder(
                BorderFactory.createRaisedBevelBorder());
        add_user.setBounds(80, 20, 150, 30);
        add(panels);
        panels.setBounds(3, 70, 304, 77);
    }
}
