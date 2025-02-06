package WeChat;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.TreeSet;

public class Contacts_View extends JSplitPane {
    public Friend_Panel panel_friend;
    public JPanel panel;
    public JPanel panel_for_contacts;
    public JPanel panel_for_add;
    public Function_Panel panel_function;
    public JSplitPane splitPane_add_manage;
    public JSplitPane splitPane_function_friend;
    public JScrollPane scrollPane;
    public JButton button_add;
    public JButton button_manage;
    public Friend_message friend_message;
    public LinkedList<String> list_friend_panel_text;
    public LinkedList<Friend_Panel> list_friend_panel;
    public TreeSet<Friend_Info> friend_info_tree;
    public TreeSet<Friend_Info> friend_info_tree_last;
    public Mouse_Police mouse_polices;
    public Contacts_View() {
        //用于存放联系人面板的标签
        list_friend_panel_text = new LinkedList<>();
        friend_info_tree_last = new TreeSet<>();
        friend_info_tree = new TreeSet<>();
        //功能面板
        panel_function = new Function_Panel();
        panel_for_contacts = new JPanel();
        panel_for_contacts.setLayout(new BoxLayout(panel_for_contacts, BoxLayout.Y_AXIS));
        panel_for_add = new JPanel();
        panel_for_add.setLayout(new BorderLayout());
        button_add = new JButton();
        String path = "image/function/";
        SetImageIcon.SetIcon(path, button_add, 18, 18, "添加好友");
        button_add.setBorderPainted(false);
        button_add.setContentAreaFilled(false);
        button_add.setName("contacts_view:添加好友");
        mouse_polices = new Mouse_Police();
        mouse_polices.setButton(button_add);
        mouse_polices.setPanel_contacts_view(this);
        button_add.addMouseListener(mouse_polices);
        //将添加好友的按钮放置在联系人面板右上角，该面板（panel_2）的东边
        panel_for_add.add(button_add, BorderLayout.EAST);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        button_manage = new JButton("通讯录管理");
        button_manage.setName("contacts_view:通讯录管理");
        //通讯录管理按钮放置在添加好友按钮的下方
        panel.add(button_manage, BorderLayout.NORTH);
        splitPane_add_manage = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                panel_for_add, panel);
        splitPane_add_manage.setBorder(null);
        splitPane_add_manage.setDividerSize(0);
        splitPane_add_manage.setEnabled(false);
        panel_for_contacts.add(splitPane_add_manage);
        splitPane_add_manage.setMaximumSize(new Dimension(310, 60));
        list_friend_panel = new LinkedList<>();
        panel_friend = new Friend_Panel();
        list_friend_panel.add(panel_friend);
        panel_friend.setMaximumSize(new Dimension(310, 95));
        list_friend_panel_text.add(panel_friend.text);
        panel_for_contacts.add(panel_friend);
        panel_friend = new Friend_Panel("所有朋友");
        list_friend_panel.add(panel_friend);
        list_friend_panel_text.add(panel_friend.text);
        panel_for_contacts.add(panel_friend);
        scrollPane = new JScrollPane(panel_for_contacts);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //拆分功能面板和联系人面板
        splitPane_function_friend = new JSplitPane
                (JSplitPane.HORIZONTAL_SPLIT, panel_function, scrollPane);
        splitPane_function_friend.setDividerSize(1);
        splitPane_function_friend.setSize(400, 600);
        splitPane_function_friend.setEnabled(false);
        splitPane_function_friend.setDividerLocation(0.15);
        //联系人详细信息面板
        friend_message = new Friend_message();
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setLeftComponent(splitPane_function_friend);
        setRightComponent(friend_message);
        setDividerSize(0);
        setEnabled(false);
        setSize(1200, 600);
        setDividerLocation(0.31);
    }
}
