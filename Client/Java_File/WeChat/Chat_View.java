package WeChat;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.TreeSet;

public class Chat_View extends JSplitPane {
    public ChatBox_Panel panel_ChatBox;
    public Chat_Panel panel_chat;
    public Function_Panel panel_function;
    public JScrollPane scrollPane;
    public JSplitPane splitPane;
    public JPanel panel;
    public CardLayout card;
    public LinkedList<ChatBox_Panel> list_ChatBox;
    public LinkedList<ChatBox_Panel> list_ChatBox_last;
    public TreeSet<Friend_Info> friend_chat_tree;
    public TreeSet<Friend_Info> friend_chat_tree_last;
    public LinkedList<User_message> user_message_list;
    public Chat_View() {
        panel = new JPanel();
        card = new CardLayout();
        panel.setLayout(card);
        list_ChatBox = new LinkedList<>();
        list_ChatBox_last = new LinkedList<>();
        user_message_list = new LinkedList<>();
        friend_chat_tree = new TreeSet<>();
        friend_chat_tree_last = new TreeSet<>();
        User_message user_message = new User_message();
        user_message.friend_name = "空白";
        user_message.friend_account = "无效用户";
        user_message.object_type = "联系人";
        panel_ChatBox = new ChatBox_Panel(user_message);
        panel.add("null", panel_ChatBox);
        card.show(panel, "null");
        panel_chat = new Chat_Panel();
        scrollPane = new JScrollPane(panel_chat);
        //拆分窗格的横向滚动条设置为不可见
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel_function = new Function_Panel();
        //此拆分窗格左边为功能面板，右边为联系人聊天对话面板
        splitPane = new JSplitPane
                (JSplitPane.HORIZONTAL_SPLIT, panel_function, scrollPane);
        //拆分窗格的大小以及拆分线大小、位置，
        splitPane.setDividerSize(2);
        splitPane.setSize(400, 600);
        splitPane.setDividerLocation(0.15);
        //拆分线不允许拖动
        splitPane.setEnabled(false);
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        //右边为对话框面板
        setLeftComponent(splitPane);
        setRightComponent(panel);
        setDividerSize(0);
        setEnabled(false);
        setSize(1100, 600);
        setDividerLocation(0.34);
    }
}

