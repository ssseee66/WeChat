package WeChat;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Friend_Apply extends Box {
    public JButton avatar;
    public JButton yes;
    public User_message user_message;
    public Friend_Apply(User_message user_message) {
        super(BoxLayout.X_AXIS);
        this.user_message = user_message;
        avatar = new JButton();
        avatar.setBorderPainted(false);
        avatar.setBorder(null);
        avatar.setContentAreaFilled(false);
        avatar.setName(this.user_message.friend_nickName);
        BufferedImage bufferedImage = SetImageIcon.getBufferedImage(user_message.friend_avatar);
        SetImageIcon.SetIcon(bufferedImage, avatar, 58, 58);
        JLabel nickName_label = new JLabel(user_message.friend_nickName);
        Font font = new Font("宋体", Font.BOLD, 18);
        nickName_label.setFont(font);
        JLabel helloText_label = new JLabel(this.user_message.hello_text);
        helloText_label.setFont(font);
        Box box = Box.createVerticalBox();
        box.add(nickName_label);
        box.add(Box.createVerticalStrut(10));
        box.add(helloText_label);
        yes = new JButton("接受");
        yes.setBackground(Color.GREEN);
        add(Box.createHorizontalStrut(150));
        add(avatar);
        avatar.setMaximumSize(new Dimension(60, 60));
        add(Box.createHorizontalStrut(10));
        add(box);
        box.setMinimumSize(new Dimension(250, 60));
        add(Box.createHorizontalStrut(10));
        add(yes);
        yes.setMaximumSize(new Dimension(60, 60));
        yes.setEnabled(!user_message.accept);
        if (user_message.accept) {
            yes.setText("已接受");
        }
        add(Box.createHorizontalStrut(150));
        setBackground(Color.CYAN);
        setMaximumSize(new Dimension(750, 60));
        GradientPaint gradientPaint = new GradientPaint(
                200f, 60f, Color.GREEN,
                350, 60f, Color.RED, false);//建立渐变颜色
        Border border = BorderFactory.createDashedBorder(
                gradientPaint, 10f, 10);
        setBorder(border);
    }
}
