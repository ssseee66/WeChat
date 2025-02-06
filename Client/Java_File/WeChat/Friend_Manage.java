package WeChat;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Friend_Manage extends Box {
    public Mouse_Police mouse_police;
    public JTextField friend_name_field;
    public JButton delete_button;
    public JTextPane friend_nickName_panel;
    public User_message user_message;
    public Friend_Manage(User_message user_message) {
        super(BoxLayout.X_AXIS);
        this.user_message = user_message;
        delete_button = new JButton();
        delete_button.setBorderPainted(false);
        delete_button.setBorder(null);
        delete_button.setContentAreaFilled(false);
        String path = "image/function/";
        SetImageIcon.SetIcon(path, delete_button, 25, 25, "删除");
        mouse_police = new Mouse_Police();
        mouse_police.setFriend_manage(this);
        delete_button.addMouseListener(mouse_police);
        JButton avatar = new JButton();
        avatar.setContentAreaFilled(false);
        avatar.setBorderPainted(false);
        avatar.setBorder(null);
        BufferedImage bufferedImage = SetImageIcon.getBufferedImage(user_message.friend_avatar);
        SetImageIcon.SetIcon(bufferedImage, avatar, 42, 42);
        Font font = new Font("宋体", Font.BOLD, 16);
        friend_nickName_panel = new JTextPane();
        friend_nickName_panel.setText("\n" + this.user_message.friend_nickName);
        friend_nickName_panel.setFont(font);
        friend_nickName_panel.setEnabled(false);
        friend_name_field = new JTextField();
        friend_name_field.setText(this.user_message.friend_name);
        friend_name_field.setBorder(null);
        friend_name_field.setFont(font);
        add(delete_button);
        add(Box.createHorizontalStrut(5));
        add(avatar);
        add(friend_nickName_panel);
        friend_nickName_panel.setMinimumSize(new Dimension(210, 60));
        friend_nickName_panel.setMaximumSize(new Dimension(210, 60));
        add(Box.createHorizontalStrut(2));
        add(friend_name_field);
        friend_name_field.setMinimumSize(new Dimension(230, 60));
        friend_name_field.setMaximumSize(new Dimension(230, 60));
        GradientPaint gradientPaint = new GradientPaint(
                200f, 60f, Color.GREEN,
                350, 60f, Color.RED, false);//建立渐变颜色
        Border border = BorderFactory.createDashedBorder(
                gradientPaint, 10f, 10);
        setBorder(border);
    }
}
