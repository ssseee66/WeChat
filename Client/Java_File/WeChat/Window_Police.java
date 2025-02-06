package WeChat;

import net.sourceforge.pinyin4j.PinyinHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import static WeChat.WeChat.request_socket;
import static WeChat.WeChat.object_socket;

public class Window_Police extends WindowAdapter {
    public WeChatWindow weChatWindow;
    public Friend_Window friend_window;
    public Chat_View panel_chat_view;
    public Contacts_View panel_contacts_view;
    public Manage_Friend_Window manage_friend_window;
    public JPanel panel;
    public CardLayout card;
    public Mouse_Police_Thread mouse_police_thread;
    public final File FRIEND_CHAR_FILE = new File(
            User.user_file_path + "/" + User.user_account  + "/file/friend_chat.txt");
    public void setWeChatWindow(WeChatWindow weChatWindow) {
        this.weChatWindow = weChatWindow;
    }
    public void setFriend_window(Friend_Window friend_window) {
        this.friend_window = friend_window;
    }
    public void setPanel_chat_view(Chat_View panel_chat_view) {
        this.panel_chat_view = panel_chat_view;
    }
    public void setPanel_contacts_view(Contacts_View panel_contacts_view) {
        this.panel_contacts_view = panel_contacts_view;
    }
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
    public void setCard(CardLayout card) {
        this.card = card;
    }
    public void setMouse_police_thread(Mouse_Police_Thread mouse_police_thread) {
        this.mouse_police_thread = mouse_police_thread;
    }
    public void setManage_friend_window(Manage_Friend_Window manage_friend_window) {
        this.manage_friend_window = manage_friend_window;
    }
    public void windowClosing(WindowEvent event) {
        if (event.getSource() == weChatWindow) {
            if (!mouse_police_thread.panel_chat_view.friend_chat_tree.isEmpty()) {
                LinkedList<User_message> user_message_list =
                        new LinkedList<>();
                for (Friend_Info friend_info : mouse_police_thread
                        .panel_chat_view.friend_chat_tree) {
                    User_message user_message =
                            (User_message) cloneObject(friend_info.user_message);
                    user_message_list.add(user_message);
                }
                try {
                    DataOutputStream request_dataOut = new DataOutputStream(
                            request_socket.getOutputStream());
                    request_dataOut.writeUTF("保存");
                    ObjectOutputStream object_out = new ObjectOutputStream(
                            object_socket.getOutputStream());
                    object_out.writeObject(user_message_list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //将所有聊天框对象写入相应文件
            if (!mouse_police_thread.panel_chat_view.list_ChatBox.isEmpty()) {
                Iterator<ChatBox_Panel> iterator =
                        mouse_police_thread.panel_chat_view.list_ChatBox.iterator();
                try {
                    FileOutputStream fileOut =
                            new FileOutputStream(FRIEND_CHAR_FILE, false);
                    MyObjectOutputStream objectOut = new MyObjectOutputStream(fileOut);
                    while (iterator.hasNext()) {
                        ChatBox_Panel chatBox_panel = iterator.next();
                        chatBox_panel.inputArea.setText(null);
                        objectOut.writeObject(chatBox_panel);
                    }
                    fileOut.close();
                    objectOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    RandomAccessFile file = new RandomAccessFile(FRIEND_CHAR_FILE, "rw");
                    file.setLength(0);
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (event.getSource() == friend_window) {
            WeChatWindow.update_panel_chat();
            panel_chat_view.list_ChatBox.removeIf(chatBox_panel ->
                    chatBox_panel.user_message.friend_account.equals(
                            friend_window.chatBox_panel.user_message.friend_account));
            for (Component com : panel_chat_view.panel.getComponents()) {
                ChatBox_Panel chatBox_panel = (ChatBox_Panel) com;
                if (chatBox_panel.user_message.friend_account.equals(
                        friend_window.chatBox_panel.user_message.friend_account)) {
                    panel_chat_view.panel.remove(chatBox_panel);
                }
            }
            ChatBox_Panel chatBox_panel_clone =
                    (ChatBox_Panel)cloneObject(friend_window.chatBox_panel);
            panel_chat_view.list_ChatBox.add(chatBox_panel_clone);
            panel_chat_view.panel.add(chatBox_panel_clone.user_message.friend_name,
                    chatBox_panel_clone);
            panel_chat_view.panel.updateUI();
            card.show(panel, "聊天界面");
            panel_chat_view.card.show(panel_chat_view.panel,
                    friend_window.chatBox_panel.user_message.friend_name);
        }
        else if (event.getSource() == manage_friend_window) {
            //修改联系人备注名
            LinkedList<User_message> user_messageOfNew_list = new LinkedList<>();
            for (Friend_Manage friend_manage :
                    manage_friend_window.friend_manage_list) {
                User_message user_message = new User_message();
                user_message.friend_account = friend_manage.user_message.friend_account;
                user_message.friend_name =
                        friend_manage.friend_name_field.getText();
                user_messageOfNew_list.add(user_message);
            }
            panel_chat_view.panel.removeAll();
            for (User_message user_message : user_messageOfNew_list) {
                for (Friend_Info friend_info : panel_chat_view.friend_chat_tree) {
                    if (user_message.friend_account
                            .equals(friend_info.user_message.friend_account)) {
                        friend_info.user_message.friend_name = user_message.friend_name;
                        friend_info.avatar.setName(user_message.friend_name);
                        friend_info.label_name.setText(user_message.friend_name);
                    }
                }
                panel_chat_view.panel_chat.updateUI();
                for (ChatBox_Panel chatBox_panel :
                        panel_chat_view.list_ChatBox) {
                    if (chatBox_panel.user_message.friend_account
                            .equals(user_message.friend_account)) {
                        chatBox_panel.setName(user_message.friend_name);
                        chatBox_panel.user_message.friend_name =
                                user_message.friend_name;
                        chatBox_panel.label_friend_name
                                .setText(user_message.friend_name);
                        panel_chat_view.panel.add(user_message.friend_name, chatBox_panel);
                    }
                }
                //将本地联系人头像重命名
                for (Friend_Info friend_info : panel_contacts_view.friend_info_tree) {
                    if (friend_info.user_message.friend_account
                            .equals(user_message.friend_account)) {
                        friend_info.user_message.friend_name = user_message.friend_name;
                        friend_info.label_name.setText(user_message.friend_name);
                        friend_info.letter = new StringBuilder();
                        friend_info.avatar.setName(user_message.friend_name);
                        for (char c : user_message.friend_name.toCharArray()) {
                            if (c <= 255) {
                                friend_info.letter.append(c);
                            } else {
                                friend_info.letter.append(
                                        (PinyinHelper.toHanyuPinyinStringArray(c)[0]));
                            }
                        }
                        friend_info.Letter = friend_info.letter.substring(0, 1);
                    }
                }
            }
            WeChatWindow.update_contact_view();
        }
    }
    public void windowClosed(WindowEvent event) {
        if (event.getSource() == weChatWindow) {
            if (!mouse_police_thread.panel_chat_view.friend_chat_tree.isEmpty()) {
                LinkedList<User_message> user_message_list =
                        new LinkedList<>();
                for (Friend_Info friend_info : mouse_police_thread
                        .panel_chat_view.friend_chat_tree) {
                    User_message user_message =
                            (User_message) cloneObject(friend_info.user_message);
                    user_message_list.add(user_message);
                }
                try {
                    DataOutputStream request_dataOut = new DataOutputStream(
                            request_socket.getOutputStream());
                    request_dataOut.writeUTF("保存");
                    ObjectOutputStream object_out = new ObjectOutputStream(
                            object_socket.getOutputStream());
                    object_out.writeObject(user_message_list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //将所有聊天框对象写入相应文件
            if (!mouse_police_thread.panel_chat_view.list_ChatBox.isEmpty()) {
                Iterator<ChatBox_Panel> iterator =
                        mouse_police_thread.panel_chat_view.list_ChatBox.iterator();
                try {
                    FileOutputStream fileOut =
                            new FileOutputStream(FRIEND_CHAR_FILE, false);
                    MyObjectOutputStream objectOut = new MyObjectOutputStream(fileOut);
                    while (iterator.hasNext()) {
                        ChatBox_Panel chatBox_panel = iterator.next();
                        chatBox_panel.inputArea.setText(null);
                        objectOut.writeObject(chatBox_panel);
                    }
                    fileOut.close();
                    objectOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    RandomAccessFile file = new RandomAccessFile(FRIEND_CHAR_FILE, "rw");
                    file.setLength(0);
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);
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
}
