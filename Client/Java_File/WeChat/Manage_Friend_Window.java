package WeChat;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.LinkedList;

public class Manage_Friend_Window extends JFrame {
    public JPanel panel;
    public LinkedList<Friend_Manage> friend_manage_list;
    public Friend_Manage friend_manage;
    public Manage_Friend_Window() {
        setVisible(true);
        setResizable(false);
        friend_manage_list = new LinkedList<>();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JScrollPane scrollPanel = new JScrollPane(panel);
        add(scrollPanel);
        Font font = new Font("幼圆", Font.BOLD, 18);
        Border border = BorderFactory.createBevelBorder(
                0, Color.red, Color.BLUE);
        JLabel nickName_label = new JLabel("昵        称");
        nickName_label.setBorder(border);
        nickName_label.setFont(font);
        JLabel name_label = new JLabel("备        注");
        name_label.setBorder(border);
        name_label.setFont(font);
        FontMetrics fontMetrics = nickName_label.getFontMetrics(font);
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalStrut(40));
        box.add(nickName_label);
        box.add(Box.createHorizontalStrut(130));
        box.add(name_label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(box);
        box.setMaximumSize(new Dimension(
                500, fontMetrics.getHeight()));
    }
    public void addFriend_Manage(Friend_Manage friend_manage) {
        this.friend_manage = friend_manage;
        panel.add(Box.createVerticalStrut(10));
        panel.add(this.friend_manage);
        friend_manage_list.add(this.friend_manage);
        panel.updateUI();
        friend_manage.setMaximumSize(new Dimension(520, 60));
    }
}
