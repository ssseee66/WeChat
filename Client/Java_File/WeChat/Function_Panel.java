package WeChat;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.LinkedList;

public class Function_Panel extends JPanel {
    public JButton message;
    public JButton contacts;
    public JButton myself;
    public JButton setting;
    public LinkedList<Mouse_Police> list_mouse_police;
    public Mouse_Police mouse_police;
    public Function_Panel() {
        setBackground(Color.CYAN);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        list_mouse_police = new LinkedList<>();
        mouse_police = new Mouse_Police();
        myself = new JButton();
        myself.setName("我的");
        File path = new File(User.user_file_path + "/" + User.user_account + "/images");
        SetImageIcon.SetIcon(path, myself, 60, 60, "我");
        myself.setContentAreaFilled(false);
        myself.setBorderPainted(false);
        myself.setBorder(null);
        mouse_police.setFunction(myself);
        myself.addMouseListener(mouse_police);
        list_mouse_police.add(mouse_police);
        message = new JButton();
        message.setName("信息");
        String paths = "image/function/";
        SetImageIcon.SetIcon(paths, message, 60, 60, "信息");
        //将按钮背景色设置为透明色
        mouse_police = new Mouse_Police();
        message.setContentAreaFilled(false);
        //不绘制按钮边框
        message.setBorderPainted(false);
        message.setBorder(null);
        //为按钮添加监视器
        message.addMouseListener(mouse_police);
        //将按钮传给监视器

        mouse_police.setFunction(message);
        //将监视器用链表储存起来，后面需要利用其传CardLayout布局对象以及布局为CardLayout的面板对象
        list_mouse_police.add(mouse_police);


        mouse_police = new Mouse_Police();
        contacts = new JButton();
        contacts.setName("联系人");
        SetImageIcon.SetIcon(paths, contacts, 60, 60, "联系人");
        //按钮背景为透明色
        contacts.setContentAreaFilled(false);
        //不绘制按钮边框
        contacts.setBorderPainted(false);
        contacts.setBorder(null);
        mouse_police.setFunction(contacts);
        list_mouse_police.add(mouse_police);
        contacts.addMouseListener(mouse_police);

        mouse_police = new Mouse_Police();
        setting = new JButton();
        setting.setName("设置");
        SetImageIcon.SetIcon(paths, setting, 60, 60, "设置");
        setting.setContentAreaFilled(false);
        setting.setBorderPainted(false);
        setting.setBorder(null);
        mouse_police.setFunction(setting);
        list_mouse_police.add(mouse_police);
        setting.addMouseListener(mouse_police);
        add(myself);
        add(message);
        add(contacts);
        add(Box.createVerticalStrut(340));
        add(setting);
    }
}
