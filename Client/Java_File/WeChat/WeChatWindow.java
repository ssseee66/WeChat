package WeChat;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class WeChatWindow extends JFrame{
    public static Contacts_View panel_contacts_view;
    public static Chat_View panel_chat_view;
    public static AddFriend_View panel_add_view;
    public static JPanel panel;
    public static CardLayout card;
    public Mouse_Police_Thread setting;
    public Thread thread_contacts;
    public Thread thread_chat;
    public Window_Police window_police;
    WeChatWindow() {
        card = new CardLayout();
        panel = new JPanel();
        panel.setLayout(card);
        //联系人界面
        panel_contacts_view = new Contacts_View();
        //联系人聊天界面
        panel_chat_view = new Chat_View();
        panel_add_view = new AddFriend_View();
        panel_add_view.mouse_police.setCard(card);
        panel_add_view.mouse_police.setPanel(panel);
        panel_add_view.mouse_police.setPanel_chat_view(panel_chat_view);
        panel_add_view.mouse_police.setPanel_contacts_view(panel_contacts_view);
        panel.add(panel_contacts_view, "好友信息界面");
        panel.add(panel_chat_view, "聊天界面");
        panel.add(panel_add_view, "添加好友界面");
        //线程目标对象
        setting = new Mouse_Police_Thread();
        setting.setPanel(panel);
        setting.setCard(card);
        setting.getUser_message_list();
        setting.getApply_user_message_list();
        setting.setPanel_chat_view(panel_chat_view);
        setting.setPanel_contacts_view(panel_contacts_view);
        setting.setPanel_add_view(panel_add_view);
        setting.setMainWindow(this);
        thread_contacts = new Thread(setting);
        thread_contacts.setName("联系人");
        thread_chat = new Thread(setting);
        thread_chat.setName("聊天");
        thread_chat.start();
        try {
            Thread.sleep(1000);
            thread_contacts.start();
            thread_contacts.join();
            setVisible(true);
            validate();
            card.show(panel, "聊天界面");
            add(panel);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(false);
            //窗口事件监视器对象
            window_police = new Window_Police();
            window_police.setMouse_police_thread(setting);
            window_police.setWeChatWindow(this);
            //为此窗口注册窗口事件监视器
            addWindowListener(window_police);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void update_panel_chat() {
        panel_chat_view.panel_chat.removeAll();
        panel_chat_view.friend_chat_tree_last.clear();
        panel_chat_view.panel_chat.list_mouse_police.clear();
        for (Friend_Info friend_info : panel_chat_view.friend_chat_tree) {
            Friend_Info friend_info_clone = (Friend_Info) cloneObject(friend_info);
            panel_chat_view.panel_chat.addContacts
                    (friend_info_clone);
            panel_chat_view.friend_chat_tree_last.add(friend_info_clone);
            //更新联系人聊天面板内容
        }
        panel_chat_view.panel_chat.updateUI();
        panel_chat_view.friend_chat_tree = CastList.castTree(
                cloneObject(panel_chat_view.friend_chat_tree_last),
                Friend_Info.class);
        for (Mouse_Police mouse_police :
                panel_chat_view.panel_chat.list_mouse_police) {
            mouse_police.setPanel(panel);
            mouse_police.setCard(card);
            mouse_police.setPanel_contacts_view(panel_contacts_view);
            mouse_police.setPanel_chat_view(panel_chat_view);
        }
    }
    public static void update_contact_view() {
        for (Component com : panel_contacts_view.panel_for_contacts.getComponents()) {
            if (com != panel_contacts_view.splitPane_add_manage)
                panel_contacts_view.panel_for_contacts.remove(com);
        }
        panel_contacts_view.list_friend_panel_text.clear();
        panel_contacts_view.list_friend_panel.clear();
        Friend_Panel panel_friend = new Friend_Panel();
        panel_contacts_view.list_friend_panel.add(panel_friend);
        panel_contacts_view.list_friend_panel_text.add(panel_friend.text);
        panel_contacts_view.panel_for_contacts.add(panel_friend);
        panel_contacts_view.friend_info_tree_last.clear();
        panel_friend.setMaximumSize(new Dimension(310, 95));
        panel_friend = new Friend_Panel("所有朋友");
        panel_contacts_view.list_friend_panel.add(panel_friend);
        panel_contacts_view.list_friend_panel_text.add(panel_friend.text);
        panel_contacts_view.panel_for_contacts.add(panel_friend);
        for (Friend_Panel friend_panel : panel_contacts_view.list_friend_panel) {
            if (friend_panel.text.equals("所有朋友")) {
                friend_panel.panel.removeAll();
                for (Friend_Info friend_info : panel_contacts_view.friend_info_tree) {
                    Friend_Info friend_info_clone = (Friend_Info) cloneObject(friend_info);
                    friend_panel.newContacts(friend_info_clone);
                    panel_contacts_view.friend_info_tree_last.add(friend_info_clone);
                }
            }
        }
        panel_contacts_view.friend_info_tree = CastList.castTree(
                cloneObject(panel_contacts_view.friend_info_tree_last),
                Friend_Info.class);
        for (Friend_Info friend_info : panel_contacts_view.friend_info_tree) {
            if (!panel_contacts_view.list_friend_panel_text.contains(friend_info.Letter)) {
                //创建相应标签的子面板对象
                Friend_Panel friend_panel = new Friend_Panel(friend_info.Letter);
                //将此联系人对象添加至新建的子面板对象
                friend_panel.newContacts((Friend_Info)cloneObject(friend_info));
                //将新建的子面板对象添加至联系人界面的联系人面板中
                panel_contacts_view.panel_for_contacts.add(friend_panel);
                panel_contacts_view.panel_for_contacts.updateUI();
                //子面板标签链表添加新建子面板对象标签
                panel_contacts_view.list_friend_panel_text.add(friend_info.Letter);
                //将新建的子面板对象添加至子面板链表中
                panel_contacts_view.list_friend_panel.add(friend_panel);
            } else {          //子面板标签链表中已包含该联系人对象的名字首字母时
                //子面板链表迭代器
                for (Friend_Panel friend_panel : panel_contacts_view.list_friend_panel) {
                    if (friend_panel.text.equals(friend_info.Letter)) {
                        //在对应其联系人对象名字首字母缩写的标签子面板添加联系人对象
                        friend_panel.newContacts
                                ((Friend_Info)cloneObject(friend_info));
                        friend_panel.updateUI();
                    }
                }
            }
        }
        panel_contacts_view.panel_for_contacts.updateUI();
        for (Friend_Panel friend_panel : panel_contacts_view.list_friend_panel) {
            for (Mouse_Police mouse_police : friend_panel.list_mouse_police) {
                mouse_police.setPanel_contacts_view(panel_contacts_view);
                mouse_police.setPanel_chat_view(panel_chat_view);
                mouse_police.setFriend_message(panel_contacts_view.friend_message);
            }
        }
        for (Component com : panel_contacts_view.friend_message.getComponents()) {
            if (com instanceof Friend_message.friend_message_panel) {
                panel_contacts_view.friend_message.remove(com);
            }
        }
        if (! panel_contacts_view.friend_info_tree.isEmpty()) {
            for (Friend_Info friend_info : panel_contacts_view.friend_info_tree) {
                User_message user_message =
                        (User_message) cloneObject(friend_info.user_message);
                panel_contacts_view.friend_message.addFriend_message(user_message);
                Mouse_Police mouse_police = new Mouse_Police();
                mouse_police.setPanel_chat_view(panel_chat_view);
                mouse_police.setPanel_contacts_view(panel_contacts_view);
                mouse_police.setButton(panel_contacts_view.friend_message.send_button);
                mouse_police.setPanel(panel);
                mouse_police.setCard(card);
                panel_contacts_view.friend_message.send_button
                        .addMouseListener(mouse_police);
            }
        }
    }
    public static Object cloneObject(Object object) {
        Object object_clone = new Object();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objectOut =
                    new ObjectOutputStream(out);
            objectOut.writeObject(object);
            out.close();
            objectOut.close();
            ByteArrayInputStream in =
                    new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream objectIn =
                    new ObjectInputStream(in);
            object_clone = objectIn.readObject();
            in.close();
            objectIn.close();
        } catch (Exception ignored) {}
        return object_clone;
    }
}
