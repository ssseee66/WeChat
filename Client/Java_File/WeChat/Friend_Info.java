package WeChat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import net.sourceforge.pinyin4j.PinyinHelper;

public class Friend_Info extends Box implements Comparable<Friend_Info>, Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    public Box box;
    public StringBuilder letter = new StringBuilder();
    //联系人对象备注名字的首字母
    public String Letter;
    //联系人对象头像
    public JButton avatar;
    //联系人备注名字标签
    public JLabel label_name;
    //聊天时间标签
    public JLabel label_time;
    //与联系人聊天时最后一句聊天内容
    public JLabel label_text;
    public LocalDateTime time;
    public User_message user_message;
    //联系人信息存放文件路径
    public Friend_Info(User_message user_message) {
        super(BoxLayout.X_AXIS);
        this.user_message = user_message;
        if (user_message.object_type.equals("聊天"))
            friendChat_init();
        else if (user_message.object_type.equals("联系人"))
            contacts_init();
    }
    public void contacts_init() {
        update_pinyin();
        setName(user_message.friend_name + "信息");
        box = createVerticalBox();
        avatar = new JButton();
        if (user_message.friend_name.equals("新的朋友")) {
            String path = "image/function/";
            SetImageIcon.SetIcon(path, avatar, 48, 48, user_message.friend_name);
        } else {
            BufferedImage bufferedImage = SetImageIcon.getBufferedImage(user_message.friend_avatar);
            SetImageIcon.SetIcon(bufferedImage, avatar, 48, 48);
        }
        avatar.setName(user_message.friend_name);
        //按钮背景为透明色
        avatar.setContentAreaFilled(false);
        //不绘制按钮边框
        avatar.setBorderPainted(false);
        avatar.setName(user_message.friend_name);
        String name;
        if (user_message.friend_name.length() >= 8)
            name = user_message.friend_name.substring(0, 8) + "...";
        else
            name = user_message.friend_name;
        Font font = new Font("幼圆", Font.BOLD, 18);
        label_name = new JLabel(name);
        label_name.setFont(font);
        box.add(label_name);
        box.add(createVerticalStrut(10));
        add(avatar);
        add(createHorizontalStrut(10));
        add(box);
        setMaximumSize(new Dimension(295, 60));
    }
    public void friendChat_init() {
        update_pinyin();
        setName(user_message.friend_name + "聊天");
        box = createVerticalBox();
        avatar = new JButton();
        avatar.setName(user_message.friend_name);
        BufferedImage bufferedImage = SetImageIcon.getBufferedImage(user_message.friend_avatar);
        SetImageIcon.SetIcon(bufferedImage, avatar, 48, 48);
        //按钮背景为透明色
        avatar.setContentAreaFilled(false);
        avatar.setBorderPainted(false);
        avatar.setName(user_message.friend_name);
        //设置显示最后聊天内容的最大显示长度，超过最大长度则截取字符串并在后面添加...
        String last_text = null;
        if (user_message.chat_text != null)
            if (user_message.chat_text.length() >= 10) {
                last_text = user_message.chat_text.substring(0, 10) + "...";
                user_message.last_text = last_text;
            }
            else {
                last_text = user_message.chat_text;
                user_message.last_text = user_message.chat_text;
            }
        String name;
        if (user_message.friend_name.length() >= 8)
            name = user_message.friend_name.substring(0, 8) + "...";
        else
            name = user_message.friend_name;
        label_name = new JLabel(name);
        Font font = new Font("幼圆", Font.BOLD, 16);
        label_name.setFont(font);
        time = user_message.last_chat_time;
        user_message.last_time = String.format("%ty.%<tm.%<td %<tT",
                user_message.last_chat_time);
        label_text = new JLabel(last_text);
        label_time = new JLabel(user_message.last_time);
        label_time.setFont(new Font("幼圆", Font.BOLD, 10));
        box.add(label_name);
        box.add(createVerticalStrut(10));
        box.add(label_text);
        add(avatar);
        add(box);
        add(label_time);
        add(createHorizontalStrut(3));
        setMaximumSize(new Dimension(295, 60));
    }
    public void update_pinyin() {
        for (char c : user_message.friend_name.toCharArray()) {
            //StringBuilder类的append()方法运行速度快
            //String类的concat()方法相对比较慢(返回值为一个新的String对象)
            /* "+"运算符在循环中连接字符串运行速度较慢（每次都会返回新的对象）
            编译器对+进行了优化，它是使用StringBuilder的append()方法来进行处理的，
            编译器使用append()方法追加后要同toString()转换成String字符串，也就说
            str +=”b”等同于str = new StringBuilder(str).append(“b”).toString();
            它变慢的关键原因就在于new StringBuilder()和toString()*/
            if (c <= 255) {
                letter.append(c);
            } else {
                letter.append((PinyinHelper.toHanyuPinyinStringArray(c)[0]));
            }
        }
        Letter = letter.substring(0, 1);
    }
    //重写comparable<T>接口，以便添加至相应的树集中
    public int compareTo(Friend_Info box) {
        if (time == null)
            return this.letter.compareTo(box.letter);
        else
            return - this.time.compareTo(box.time);
    }
}
