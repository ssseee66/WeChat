package WeChat;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Friend_message extends JPanel implements Serializable {
    //联系人账号
    public String friend_account;
    //联系人昵称
    public String friend_nickname;
    //联系人备注名
    public String friend_name;
    //联系人的个性签名
    public String friend_signature;
    //联系人地区
    public String friend_region;
    //联系人所在城市
    public String friend_city;
    public JButton avatar;
    public friend_message_panel panel;
    public JPanel newFriend_panel;
    public JButton send_button;
    //CardLayout布局对象
    public CardLayout card;
    public JLabel label_friend_name;
    //为每个联系人信息对象创建一个相应的面板，并全都添加至此面板（CardLayout布局）
    static class friend_message_panel extends JPanel implements Serializable {
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g_2d = (Graphics2D)g;
            g_2d.setColor(Color.GRAY);
            BasicStroke bs = new BasicStroke
                    (3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
            g_2d.setStroke(bs);
            //在对应位置绘制一条直线
            Line2D line = new Line2D.Double(100, 220, 550, 220);
            g_2d.draw(line);
            line = new Line2D.Double(100, 320, 550, 320);
            g_2d.draw(line);
        }
    }
    public Friend_message() {
        card = new CardLayout();
        setLayout(card);
        JButton button = new JButton();
        String path = "image/function/";
        SetImageIcon.SetIcon(path, button, 680, 600, "空白");
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBorder(null);
        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.add(button);
        button.setBounds(0, 0, 680, 600);
        JLabel newFriend_label = new JLabel("新的朋友");
        newFriend_label.setFont(new Font("宋体", Font.BOLD, 20));
        newFriend_panel = new JPanel();
        newFriend_panel.setLayout(new BoxLayout(newFriend_panel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(newFriend_panel);
        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(newFriend_label);
        splitPane.setBottomComponent(scrollPane);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);
        add(panel_1, "空白");
        add(splitPane, "新的朋友");
        //连接数据库获取联系人数据信息
        card.show(this, "空白");
    }
    public void addFriend_message(User_message user_message) {
        friend_account = user_message.getFriend_account();
        friend_nickname = user_message.getFriend_nickName();
        friend_name = user_message.getFriend_name();
        friend_signature = user_message.getFriend_signature();
        friend_region = user_message.getFriend_region();
        friend_city = user_message.getFriend_city();
        panel = new friend_message_panel();
        panel.setName(friend_name);
        panel.setLayout(null);
        avatar = new JButton();
        Action_Police action_police = new Action_Police();
        BufferedImage bufferedImage = SetImageIcon.getBufferedImage(user_message.friend_avatar);
        SetImageIcon.SetIcon(bufferedImage, avatar, 100, 100);
        //头像背景为透明色
        avatar.setName(friend_name);
        avatar.setContentAreaFilled(false);
        avatar.setBorderPainted(false);
        avatar.setBorder(null);
        action_police.setAvatar(avatar);
        avatar.addActionListener(action_police);
        Box box_h = Box.createHorizontalBox();
        Box box_v = Box.createVerticalBox();

        box_h.add(avatar);
        box_h.add(Box.createHorizontalStrut(10));
        label_friend_name = new JLabel(friend_name);
        Font font = new Font("宋体", Font.BOLD, 18);
        label_friend_name.setFont(font);
        box_v.add(label_friend_name);
        box_v.add(Box.createVerticalStrut(6));
        JLabel label;
        label = new JLabel("昵称：" + friend_nickname);
        box_v.add(label);
        box_v.add(Box.createVerticalStrut(6));
        label = new JLabel("账号：" + friend_account);
        box_v.add(label);
        box_v.add(Box.createVerticalStrut(6));
        if (friend_signature != null) {
            label = new JLabel("个性签名：" + friend_signature);
            box_v.add(label);
            box_v.add(Box.createVerticalStrut(6));
        }
        label = new JLabel("地区：" + friend_region + "  " + friend_city);
        box_v.add(label);
        box_v.add(Box.createVerticalStrut(6));
        box_h.add(box_v);
        panel.add(box_h);
        box_h.setBounds(100, 70, 450, 120);
        Font font_1 = new Font("宋体", Font.BOLD, 16);
        JLabel label_1 = new JLabel("备注：" + friend_name);
        label_1.setFont(font_1);
        panel.add(label_1);
        label_1.setBounds(200, 240, 450, 16);
        label_1 = new JLabel("账号：" + friend_account);
        label_1.setFont(font_1);
        panel.add(label_1);
        label_1.setBounds(200, 260, 450, 16);
        label_1 = new JLabel("地区：" + friend_region + "  " + friend_city);
        label_1.setFont(font_1);
        panel.add(label_1);
        label_1.setBounds(200, 280, 450, 16);
        send_button = new JButton("发消息");
        send_button.setName(friend_name);
        panel.add(send_button);
        send_button.setBounds(250, 350, 100, 30);
        send_button.setBackground(Color.GREEN);
        add(friend_name, panel);
    }
}
