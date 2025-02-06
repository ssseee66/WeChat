package WeChat;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serial;
import java.io.Serializable;

public class ChatBox_Panel extends JSplitPane implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    public JTextArea inputArea;
    public JButton button_come;
    public JSplitPane pane;
    public JPanel show_panel;
    public JScrollPane showPanel;
    public JLabel label_friend_name;
    public User_message user_message;
    public File FRIEND_CHAT_FILE = new File(
            User.user_file_path + "/" + User.user_account  + "/file/friend_chat.txt");
    public ChatBox_Panel(User_message user_message) {
        this.user_message = user_message;
        show_panel = new JPanel();
        show_panel.setSize(700, 400);
        show_panel.setLayout(new BoxLayout(show_panel, BoxLayout.Y_AXIS));
        showPanel = new JScrollPane(show_panel);
        label_friend_name = new JLabel(user_message.friend_name);
        label_friend_name.setFont(new Font("宋体", Font.BOLD, 20));
        JSplitPane splitPane_show = new JSplitPane
                (JSplitPane.VERTICAL_SPLIT, label_friend_name, showPanel);
        splitPane_show.setDividerSize(0);
        splitPane_show.setEnabled(false);
        //横向滚动条不可见
        showPanel.setHorizontalScrollBarPolicy
                (JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //纵向滚动条需要滚动时可见
        showPanel.setVerticalScrollBarPolicy
                (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        showPanel.setSize(700, 400);
        inputArea = new JTextArea();
        inputArea.setFont(new Font("宋体", Font.BOLD, 20));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        button_come = new JButton("发送");
        button_come.setName(this.user_message.friend_name + ":发送");
        //对话框输入区域
        inputArea.setSize(700, 180);
        inputArea.setBorder(null);
        button_come.setSize(80, 20);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(731, 20);
        panel.setBackground(Color.WHITE);
        panel.add(button_come);
        //发送按钮的位置大小
        button_come.setBounds(panel.getWidth() - 100, panel.getHeight(), 80, 25);
        pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(inputArea), panel);
        pane.setSize(800, 200);
        pane.setDividerSize(0);
        pane.setDividerLocation(0.5);
        pane.setEnabled(false);
        //设置窗格拆分方向为垂直拆分
        setOrientation(JSplitPane.VERTICAL_SPLIT);
        //添加窗格上方组件
        JButton buttons = new JButton();
        String path = "image/function/";
        SetImageIcon.SetIcon(path, buttons, 700, 600, "空白");
        buttons.setContentAreaFilled(false);
        buttons.setBorderPainted(false);
        buttons.setBorder(null);
        if (user_message.friend_name.equals("空白")) {
            setTopComponent(buttons);
            setBottomComponent(new Panel());
        } else {
            setTopComponent(splitPane_show);
            //添加窗格下方组件
            setBottomComponent(pane);
        }
        setSize(700, 600);
        //拆分线不可拖动
        setEnabled(false);
        //拆分线高度
        setDividerSize(1);
        //拆分线位置
        setDividerLocation(0.7);
        try {
            FileOutputStream fileOut = new FileOutputStream(FRIEND_CHAT_FILE, true);
            MyObjectOutputStream objectOut = new MyObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            fileOut.close();
            objectOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
