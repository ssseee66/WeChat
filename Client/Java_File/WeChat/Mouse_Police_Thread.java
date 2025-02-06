package WeChat;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

import static WeChat.WeChat.*;

public class Mouse_Police_Thread implements Runnable {
    public JPanel panel;
    public CardLayout card;
    public Contacts_View panel_contacts_view;
    public Chat_View panel_chat_view;
    public AddFriend_View panel_add_view;
    public WeChatWindow mainWindow;
    public LinkedList<String> list_friend_name;
    private LinkedList<User_message> user_messages_list = new LinkedList<>();
    private LinkedList<User_message> apply_user_message_list = new LinkedList<>();
    public final File FRIEND_CHAT_FILE = new File(
            User.user_file_path + "/" + User.user_account  + "/file/friend_chat.txt");
    public void setPanel_contacts_view(Contacts_View panel_contacts_view) {
        this.panel_contacts_view = panel_contacts_view;
    }
    public void setPanel_chat_view(Chat_View panel_chat_view) {
        this.panel_chat_view = panel_chat_view;
    }
    public void setPanel_add_view(AddFriend_View panel_add_view) {
        this.panel_add_view = panel_add_view;
    }
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public void setCard(CardLayout card) {
        this.card = card;
    }
    public void setMainWindow(WeChatWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void run() {
        if (Thread.currentThread().getName().equals("聊天")) {
            settingPolice();
        } else if (Thread.currentThread().getName().equals("联系人")) {
            settingPolice();
            Client_Monitor client_monitor = new Client_Monitor();
            client_monitor.setCard(card);
            client_monitor.setPanel(panel);
            client_monitor.setPanel_chat_view(panel_chat_view);
            client_monitor.setPanel_contacts_view(panel_contacts_view);
            client_monitor.setMainWindow(mainWindow);
            Thread monitor_chat_add_thread = new Thread(client_monitor);
            monitor_chat_add_thread.setName(User.user_nickName);
            monitor_chat_add_thread.start();
        }
    }

    public synchronized void settingPolice() {
        if (Thread.currentThread().getName().equals("聊天")) {
            for (Mouse_Police mouse_police :
                    panel_chat_view.panel_function.list_mouse_police) {
                mouse_police.setPanel(panel);
                mouse_police.setCard(card);
                mouse_police.setPanel_contacts_view(panel_contacts_view);
                mouse_police.setPanel_chat_view(panel_chat_view);
            }
            update_panel_chat();
            //从对应文件中读取联系人聊天对话框对象
            readChatFile();
            update_chatBox_panel();
            for (Friend_Info friend_info : panel_chat_view.friend_chat_tree) {
                for (ChatBox_Panel chatBox_panel : panel_chat_view.list_ChatBox) {
                    if (friend_info.user_message.friend_account
                            .equals(chatBox_panel.user_message.friend_account)) {
                        chatBox_panel.user_message =
                                (User_message) cloneObject(friend_info.user_message);
                    }
                }

            }
        }
        else if (Thread.currentThread().getName().equals("联系人")) {
            //得到联系人面板中的功能面板中的监视器链表迭代器，为其一一传递相应属性
            for (Mouse_Police mouse_police :
                    panel_contacts_view.panel_function.list_mouse_police) {
                //传递使用CardLayout布局的面板对象
                mouse_police.setPanel(panel);
                //传递CArdLayout布局对象
                mouse_police.setCard(card);
                //传递联系人界面对象
                mouse_police.setPanel_contacts_view(panel_contacts_view);
                //传递聊天界面对象
                mouse_police.setPanel_chat_view(panel_chat_view);
            }
            //将所有联系人对象添加至标签为“所有朋友”的子面板中
            update_contact_view();
            panel_contacts_view.mouse_polices.setPanel(panel);
            panel_contacts_view.mouse_polices.setCard(card);
            for (Mouse_Police mouse_police : panel_add_view
                   .panel_function.list_mouse_police) {
                //传递使用CardLayout布局的面板对象
                mouse_police.setPanel(panel);
                //传递CArdLayout布局对象
                mouse_police.setCard(card);
                mouse_police.setPanel_chat_view(panel_chat_view);
                mouse_police.setPanel_contacts_view(panel_contacts_view);
            }
           for (Mouse_Police mouse_police : panel_add_view.mouse_police_list) {
                //传递使用CardLayout布局的面板对象
                mouse_police.setPanel(panel);
                //传递CArdLayout布局对象
                mouse_police.setCard(card);
           }
           Mouse_Police mouse_police = new Mouse_Police();
           mouse_police.setButton(panel_contacts_view.button_manage);
           mouse_police.setPanel_contacts_view(panel_contacts_view);
           mouse_police.setPanel_chat_view(panel_chat_view);
           panel_contacts_view.button_manage.addMouseListener(mouse_police);
        }
    }

    public void readChatFile() {
        list_friend_name = new LinkedList<>();
        //联系人聊天框对象链表事先清空，避免重复
        panel_chat_view.list_ChatBox.clear();
        try {
            FileInputStream fileIn = new FileInputStream(FRIEND_CHAT_FILE);
            MyObjectInputStream objectInput = new MyObjectInputStream(fileIn);
            while (fileIn.available() > 0) {
                ChatBox_Panel chatBox_panel = (ChatBox_Panel) objectInput.readObject();
                if (! list_friend_name.contains(chatBox_panel.user_message.friend_name)) {
                    if (! chatBox_panel.user_message.friend_name.equals("空白")) {
                        panel_chat_view.list_ChatBox.add(chatBox_panel);
                        list_friend_name.add(chatBox_panel.user_message.friend_name);
                    }
                }
            }
            fileIn.close();
            objectInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Object cloneObject(Object object) {
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
    public void update_panel_chat() {
        if (! user_messages_list.isEmpty()) {
            panel_chat_view.panel_chat.removeAll();
            //根据树集中的联系人信息将联系人聊天对话对象的副本放入联系人聊天面板中
            //树集中的联系人聊天对象按照其封装的时间排序
            panel_chat_view.friend_chat_tree.clear();
            for (User_message user_message : user_messages_list) {
                User_message user_message_clone =
                        (User_message) cloneObject(user_message);
                user_message_clone.object_type = "聊天";
                Friend_Info friend_info = new Friend_Info(user_message_clone);
                panel_chat_view.panel_chat.addContacts(friend_info);
                panel_chat_view.friend_chat_tree.add(friend_info);
                if (isNotRecord) {
                    ChatBox_Panel chatBox_panel = new ChatBox_Panel(
                            (User_message)cloneObject(user_message));
                }
            }
            //更新联系人聊天面板内容
            panel_chat_view.panel_chat.updateUI();
        }
    }
    public void update_chatBox_panel() {
        if (!panel_chat_view.list_ChatBox.isEmpty()) {
            panel_chat_view.panel.removeAll();
            panel_chat_view.list_ChatBox_last.clear();
            for (ChatBox_Panel chatBox_panel : panel_chat_view.list_ChatBox) {
                ChatBox_Panel chatBox_panel_clone =
                        (ChatBox_Panel) cloneObject(chatBox_panel);
                //获取联系人聊天框对象的克隆对象（将对象写入内存再读取出来）
                panel_chat_view.panel.add(chatBox_panel_clone.user_message.friend_name,
                        chatBox_panel_clone);
                Mouse_Police mouse_police = new Mouse_Police();
                mouse_police.setButton(chatBox_panel_clone.button_come);
                mouse_police.setChatBox_panel(chatBox_panel_clone);
                mouse_police.setPanel_chat_view(panel_chat_view);
                chatBox_panel_clone.button_come.addMouseListener(mouse_police);
                panel_chat_view.list_ChatBox_last.add(chatBox_panel_clone);
            }
            panel_chat_view.card.show(panel_chat_view.panel, "null");
            panel_chat_view.list_ChatBox = CastList.castList(
                    panel_chat_view.list_ChatBox_last,
                    ChatBox_Panel.class);
            //更新聊天框面板内容
            panel_chat_view.panel.updateUI();
            //得到聊天面板中的监视器链表的迭代器，为其一一传递相应属性
            for (Mouse_Police mouse_police :
                    panel_chat_view.panel_chat.list_mouse_police) {
                mouse_police.setPanel(panel);
                mouse_police.setCard(card);
                mouse_police.setPanel_contacts_view(panel_contacts_view);
                mouse_police.setPanel_chat_view(panel_chat_view);
            }
        }
        User_message user_message = new User_message();
        user_message.friend_name = "空白";
        user_message.friend_account = "无效用户";
        user_message.object_type = "联系人";
        ChatBox_Panel panel_ChatBox = new ChatBox_Panel(user_message);
        panel_chat_view.panel.add("null", panel_ChatBox);
        panel_chat_view.card.show(panel_chat_view.panel, "null");
        panel_chat_view.panel.updateUI();
        panel.updateUI();
    }
    public void update_contact_view() {
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
        panel_friend.setMaximumSize(new Dimension(310, 95));
        panel_friend = new Friend_Panel("所有朋友");
        panel_contacts_view.list_friend_panel.add(panel_friend);
        panel_contacts_view.list_friend_panel_text.add(panel_friend.text);
        panel_contacts_view.panel_for_contacts.add(panel_friend);
        for (Friend_Panel friend_panel : panel_contacts_view.list_friend_panel) {
            if (friend_panel.text.equals("所有朋友")) {
                friend_panel.panel.removeAll();
                panel_contacts_view.friend_info_tree.clear();
                for (User_message user_message : user_messages_list) {
                    User_message user_message_clone =
                            (User_message) cloneObject(user_message);
                    user_message_clone.object_type = "联系人";
                    Friend_Info friend_info = new Friend_Info(user_message_clone);
                    panel_contacts_view.friend_info_tree.add(friend_info);
                }
                panel_contacts_view.friend_info_tree_last.clear();
                for (Friend_Info friend_info : panel_contacts_view.friend_info_tree) {
                    Friend_Info friend_info_clone =
                            (Friend_Info) cloneObject(friend_info);
                    friend_panel.newContacts(friend_info_clone);
                    panel_contacts_view.friend_info_tree_last.add(friend_info_clone);
                }
            }
        }
        panel_contacts_view.friend_info_tree = CastList.castTree(
                panel_contacts_view.friend_info_tree_last,
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
        for (Friend_Info friend_info : panel_contacts_view.friend_info_tree) {
            User_message user_message =
                    (User_message)cloneObject(friend_info.user_message);
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
        panel_contacts_view.friend_message.newFriend_panel.removeAll();
        if (!apply_user_message_list.isEmpty()) {
            for (User_message user_message : apply_user_message_list) {
                Friend_Apply friend_apply = new Friend_Apply(user_message);
                panel_contacts_view.friend_message.newFriend_panel
                        .add(Box.createVerticalStrut(10));
                panel_contacts_view.friend_message.newFriend_panel
                        .add(friend_apply);
                Action_Police action_police = new Action_Police();
                action_police.setPanel(panel);
                action_police.setCard(card);
                action_police.setPanel_chat_view(panel_chat_view);
                action_police.setPanel_contacts_view(panel_contacts_view);
                action_police.setFriend_apply(friend_apply);
                action_police.setButton(friend_apply.yes);
                friend_apply.yes.addActionListener(action_police);
            }
        }
        panel_contacts_view.friend_message.newFriend_panel.updateUI();
    }
    public void getUser_message_list() {
        user_messages_list =
                new LinkedList<>();
        try {
            DataOutputStream request_dataOut = new DataOutputStream(
                    request_socket.getOutputStream());
            request_dataOut.writeUTF("初始化");
            OutputStream out_object = object_socket.getOutputStream();
            ObjectOutputStream object_out = new ObjectOutputStream(
                    out_object);
            User user = new User();
            user.account = User.user_account;
            user.nickName = User.user_nickName;
            object_out.writeObject(user);
            ObjectInputStream objectIn = new ObjectInputStream(object_socket.getInputStream());
            user_messages_list = CastList.castList(objectIn.readObject(), User_message.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getApply_user_message_list() {
        apply_user_message_list =
                new LinkedList<>();
        try {
            DataOutputStream request_dataOut = new DataOutputStream(
                    request_socket.getOutputStream());
            request_dataOut.writeUTF("申请列表");
            User user = new User();
            user.account = User.user_account;
            user.nickName = User.user_nickName;
            ObjectOutputStream object_out = new ObjectOutputStream(
                    object_socket.getOutputStream());
            object_out.writeObject(user);
            ObjectInputStream object_in = new ObjectInputStream(
                    object_socket.getInputStream());
            apply_user_message_list =
                    CastList.castList(object_in.readObject(), User_message.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}