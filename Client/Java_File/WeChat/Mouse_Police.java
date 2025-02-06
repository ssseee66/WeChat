package WeChat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import static WeChat.WeChat.request_socket;
import static WeChat.WeChat.info_socket;
import static WeChat.WeChat.object_socket;


public class Mouse_Police extends MouseAdapter {
    public Friend_Info friend;
    public JButton avatar;
    public JPanel panel;
    public JButton function;
    public JButton button;
    public Login login_view;
    public AddFriend_View addFriend_view;
    public CardLayout card;
    public Friend_message friend_message;
    public Contacts_View panel_contacts_view;
    public Chat_View panel_chat_view;
    public ChatBox_Panel chatBox_panel;
    public Friend_Manage friend_manage;
    public Friend_Window friend_window;
    public Manage_Friend_Window manage_friend_window;
    public void setFriend_window(Friend_Window friend_window) {
        this.friend_window = friend_window;
    }
    public void setFriend(Friend_Info friend) {
        this.friend = friend;
    }
    public void setAvatar(JButton avatar) {
        this.avatar = avatar;
    }
    public void setFunction(JButton function) {
        this.function = function;
    }
    public void setLogin_view(Login login_view) {
        this.login_view = login_view;
    }
    public void setButton(JButton button) {
        this.button = button;
    }
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
    public void setCard(CardLayout card) {
        this.card = card;
    }
    public void setPanel_contacts_view(Contacts_View panel_contacts_view) {
        this.panel_contacts_view = panel_contacts_view;
    }
    public void setFriend_message(Friend_message friend_message) {
        this.friend_message = friend_message;
    }
    public void setPanel_chat_view(Chat_View panel_chat_view) {
        this.panel_chat_view = panel_chat_view;
    }
    public void setAddFriend_view(AddFriend_View addFriend_view) {
        this.addFriend_view = addFriend_view;
    }
    public void setChatBox_panel(ChatBox_Panel chatBox_panel) {
        this.chatBox_panel = chatBox_panel;
    }
    public void setFriend_manage(Friend_Manage friend_manage) {
        this.friend_manage = friend_manage;
    }
    public void setManage_friend_window(Manage_Friend_Window manage_friend_window) {
        this.manage_friend_window = manage_friend_window;
    }
    public Object cloneObject(Object object) {
        Object object_clone = new Object();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objectOut =
                    new ObjectOutputStream(out);
            objectOut.writeObject(object);
            ByteArrayInputStream in =
                    new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream objectIn =
                    new ObjectInputStream(in);
            object_clone = objectIn.readObject();
        } catch (Exception ignored) {}
        return object_clone;
    }

    public void mouseClicked(MouseEvent event) {
        if (event.getSource() == friend || event.getSource() == avatar) {
            if (friend_message != null) {
                friend_message.card.show(friend_message, avatar.getName());
            }
            else {
                if (event.getClickCount() == 2) {
                    for (ChatBox_Panel chatBox_panel : panel_chat_view.list_ChatBox) {
                        if (avatar.getName().equals(chatBox_panel.user_message.friend_name)) {
                            Friend_Window friend_window = new Friend_Window();
                            friend_window.setName(chatBox_panel.user_message.friend_name);
                            ChatBox_Panel chatBox_panel_clone =
                                    (ChatBox_Panel)cloneObject(chatBox_panel);
                            friend_window.setChatBox_panel(chatBox_panel_clone);
                            friend_window.panel.add(chatBox_panel_clone.user_message.friend_name,
                                    chatBox_panel_clone);
                            friend_window.card.show(friend_window.panel,
                                    chatBox_panel_clone.user_message.friend_name);
                            chatBox_panel_clone.setBounds(0, 0,
                                    chatBox_panel_clone.getWidth(),
                                    chatBox_panel_clone.getHeight());
                            Mouse_Police mouse_police = new Mouse_Police();
                            mouse_police.setChatBox_panel(chatBox_panel_clone);
                            mouse_police.setFriend_window(friend_window);
                            mouse_police.setPanel(panel);
                            mouse_police.setCard(card);
                            mouse_police.setPanel_contacts_view(panel_contacts_view);
                            mouse_police.setPanel_chat_view(panel_chat_view);
                            chatBox_panel_clone.button_come
                                    .addMouseListener(mouse_police);
                            Window_Police window_police = new Window_Police();
                            window_police.setPanel(panel);
                            window_police.setCard(card);
                            window_police.setPanel_contacts_view(
                                    panel_contacts_view);
                            window_police
                                    .setPanel_chat_view(panel_chat_view);
                            window_police.setFriend_window(friend_window);
                            friend_window.addWindowListener(window_police);
                            panel_chat_view.panel.remove(chatBox_panel);
                        }
                    }
                    panel_chat_view.card.show(panel_chat_view.panel, "null");
                    panel_chat_view.panel.updateUI();
                    panel_chat_view.panel_chat.remove(friend);
                    panel_chat_view.panel_chat.updateUI();
                } else {
                    panel_chat_view.card.show(
                            panel_chat_view.panel, avatar.getName());
                }
            }
        }
        else if (event.getSource() == function) {
            if (function.getName().equals("信息")) {
                panel_contacts_view.friend_message.card.show(
                        panel_contacts_view.friend_message, "空白");
                card.show(panel, "聊天界面");
            }
            else if (function.getName().equals("联系人")) {
                panel_contacts_view.friend_message.card.show(
                        panel_contacts_view.friend_message, "空白");
                card.show(panel, "好友信息界面");
            }
            else if (function.getName().equals("设置")) {
                My_InputDialog.showSettingDialog();
            }
            else if (function.getName().equals("我的")) {
                My_InputDialog.showUserInformationDialog();
            }
        }
        else if (event.getSource() == button) {
            if (chatBox_panel != null &&
                    !chatBox_panel.inputArea.getText().equals("") &&
                    button == chatBox_panel.button_come) {
                String chat_text = chatBox_panel.inputArea.getText();
                chatBox_panel.user_message.chat_text = chat_text;
                Font font = new Font("宋体", Font.BOLD, 20);
                File file = new File(User.user_file_path + "/" + User.user_account +
                        "/images/我.png");
                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Chat_Info chat_info = new Chat_Info(
                        chatBox_panel.getWidth() + 10,
                        bufferedImage, "我", chat_text, font);
                LocalDateTime last_chat_time = LocalDateTime.now();
                String last_time = String.format("%tD %<tT", last_chat_time);
                JLabel label = new JLabel(last_time);
                User_message user_message_clone =
                        (User_message)cloneObject(chatBox_panel.user_message);
                user_message_clone.chat_text = chat_text;
                user_message_clone.last_chat_time = last_chat_time;
                user_message_clone.me_account = User.user_account;
                user_message_clone.me_nickName = User.user_nickName;
                try {
                    DataOutputStream request_dataOut = new DataOutputStream(
                            request_socket.getOutputStream());
                    request_dataOut.writeUTF("发送信息");
                    ObjectOutputStream object_out = new ObjectOutputStream(
                            object_socket.getOutputStream());
                    object_out.writeObject(user_message_clone);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                chatBox_panel.show_panel.add(label);
                chatBox_panel.show_panel.add(chat_info);
                chatBox_panel.inputArea.setText(null);
                chat_info.setMaximumSize(new Dimension(
                        chat_info.getWidth(), chat_info.getHeight()));
                chatBox_panel.show_panel.add(Box.createVerticalStrut(5));
                panel_chat_view.friend_chat_tree.removeIf(friend_info ->
                    friend_info.user_message.friend_account
                            .equals(chatBox_panel.user_message.friend_account));
                Friend_Info friend_info = new Friend_Info(user_message_clone);
                panel_chat_view.friend_chat_tree.add(friend_info);
                panel_chat_view.panel_chat.removeAll();
                panel_chat_view.panel_chat.list_mouse_police.clear();
                for (Friend_Info friend_info_new : panel_chat_view.friend_chat_tree) {
                    panel_chat_view.panel_chat.addContacts(friend_info_new);
                }
                for (Mouse_Police mouse_police :
                        panel_chat_view.panel_chat.list_mouse_police) {
                    mouse_police.setPanel(panel);
                    mouse_police.setCard(card);
                    mouse_police.setPanel_contacts_view(panel_contacts_view);
                    mouse_police.setPanel_chat_view(panel_chat_view);
                }
                panel_chat_view.panel_chat.updateUI();
                chatBox_panel.show_panel.updateUI();
                chatBox_panel.inputArea.setText(null);
            }
            else if (panel_contacts_view != null &&
                    button == panel_contacts_view.button_add) {
                card.show(panel, "添加好友界面");
            }
            else if (panel_contacts_view != null &&
                    button == panel_contacts_view.button_manage) {
                Manage_Friend_Window manage_friend_window =
                       new Manage_Friend_Window();
                Window_Police window_police = new Window_Police();
                window_police.setPanel_chat_view(panel_chat_view);
                window_police.setPanel_contacts_view(panel_contacts_view);
                window_police.setManage_friend_window(manage_friend_window);
                manage_friend_window.addWindowListener(window_police);
                manage_friend_window.setBounds(300, 100, 520, 500);
               for (Friend_Info friend_info :
                        panel_chat_view.friend_chat_tree) {
                   Friend_Manage friend_manage = new Friend_Manage(
                           (User_message) cloneObject(friend_info.user_message));
                   friend_manage.mouse_police.setPanel_chat_view(panel_chat_view);
                   friend_manage.mouse_police.setPanel_contacts_view(panel_contacts_view);
                   friend_manage.mouse_police.setPanel(panel);
                   friend_manage.mouse_police.setCard(card);
                   friend_manage.mouse_police.setManage_friend_window(manage_friend_window);
                   manage_friend_window.addFriend_Manage(friend_manage);
               }
            }
            else if (addFriend_view != null &&
                    button == addFriend_view.button_cancel) {
                addFriend_view.field.setText(null);
                card.show(panel, "好友信息界面");
            }
            else if (button.getText().equals("发消息")) {
                card.show(panel, "聊天界面");
                panel_chat_view.card.show(panel_chat_view.panel, button.getName());
            }
        }
        if (login_view != null ) {
            if (event.getSource() == login_view.eye_login) {
                if (!login_view.password_field_text.equals("请输入密码") &&
                        login_view.eye_login.getName().equals("隐藏")) {
                    String path = "image/function/";
                    SetImageIcon.SetIcon(path, login_view.eye_login,
                            20, 20, "显示");
                    login_view.eye_login.setName("显示");
                    login_view.password_field.setEchoChar((char)0);
                }
                else if (login_view.eye_login.getName().equals("显示")) {
                    String path = "image/function/";
                    SetImageIcon.SetIcon(path, login_view.eye_login,
                            20, 20, "隐藏");
                    login_view.eye_login.setName("隐藏");
                    login_view.password_field.setEchoChar('*');
                }
            }
            else if (event.getSource() == login_view.eye_register) {
                if (login_view.eye_register.getName().equals("隐藏")) {
                    String path = "image/function/";
                    SetImageIcon.SetIcon(path, login_view.eye_register,
                            20, 20, "显示");
                    login_view.eye_register.setName("显示");
                    login_view.new_password_field.setEchoChar((char)0);
                }
                else if (login_view.eye_register.getName().equals("显示")) {
                    String path = "image/function/";
                    SetImageIcon.SetIcon(path, login_view.eye_register,
                            20, 20, "隐藏");
                    login_view.eye_register.setName("隐藏");
                    login_view.new_password_field.setEchoChar('*');
                }
            }
            else if (event.getSource() == login_view.back_button) {
                login_view.card.show(login_view.panels, "登录");
            }
        }
        if (addFriend_view !=null) {
            if (event.getSource() == addFriend_view.panel.box) {
                for (Component com : addFriend_view.panel.getComponents()) {
                    if (com instanceof User_Panel) {
                        addFriend_view.panel.remove(com);
                    }
                }
                addFriend_view.panel.updateUI();
                try {
                    String account = addFriend_view.panel.label.getText().substring(3);
                    if (account.equals(User.user_account)) {
                        File file = new File(User.user_file_path + "/" + User.user_account +
                                "/images/我.png");
                        BufferedImage bufferedImage = ImageIO.read(file);
                        User_Panel user_panel = new User_Panel(bufferedImage, User.user_nickName);
                        user_panel.add_user.setText("查看个人信息");
                        addFriend_view.panel.add(user_panel);
                        user_panel.setBounds(0, 90, 310, 150);
                        addFriend_view.panel.updateUI();
                    }
                    boolean isFriend = false;
                    Friend_Info friendInfo = null;
                    if (! panel_chat_view.friend_chat_tree.isEmpty()) {
                        for (Friend_Info friend_info : panel_chat_view.friend_chat_tree) {
                            if (friend_info.user_message.friend_account.equals(account)) {
                                isFriend = true;
                                friendInfo = friend_info;
                                break;
                            }
                        }
                    }
                    if (isFriend) {
                        BufferedImage bufferedImage = SetImageIcon.getBufferedImage(
                                friendInfo.user_message.friend_avatar);
                        User_Panel user_panel = new User_Panel(bufferedImage, User.user_nickName);
                        user_panel.add_user.setText("发送信息");
                        user_panel.action_police.setPanel(panel);
                        user_panel.action_police.setCard(card);
                        user_panel.action_police.setPanel_chat_view(panel_chat_view);
                        user_panel.action_police.setPanel_contacts_view(panel_contacts_view);
                        addFriend_view.panel.add(user_panel);
                        user_panel.setBounds(0, 90, 310, 150);
                        addFriend_view.panel.updateUI();
                    } else {
                        DataOutputStream request_dataOut = new DataOutputStream(
                                request_socket.getOutputStream());
                        request_dataOut.writeUTF("搜索用户");
                        DataOutputStream info_dataOut = new DataOutputStream(
                                info_socket.getOutputStream());
                        info_dataOut.writeUTF(account);
                        DataInputStream info_dataIn = new DataInputStream(
                                info_socket.getInputStream());
                        if (!info_dataIn.readBoolean()) {
                            JOptionPane.showMessageDialog(
                                    addFriend_view,
                                    "没有该用户，请检查拼写是否正确！",
                                    "账号不存在！", JOptionPane.INFORMATION_MESSAGE);
                            panel_chat_view.card.show(panel_chat_view.panel, "添加好友界面");
                        } else {
                            ObjectInputStream objectIn = new ObjectInputStream(
                                    object_socket.getInputStream());
                            User_message user_message =
                                    (User_message) objectIn.readObject();
                            BufferedImage bufferedImage = SetImageIcon.getBufferedImage(
                                    user_message.friend_avatar);
                            User_Panel user_panel = new User_Panel(bufferedImage,
                                    user_message.friend_nickName);
                            user_panel.setUser_message(
                                    (User_message) cloneObject(user_message));
                            addFriend_view.panel.add(user_panel);
                            user_panel.setBounds(0, 90, 310, 150);
                            addFriend_view.panel.updateUI();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (friend_manage != null) {
            if (event.getSource() == friend_manage.delete_button) {
                int isDeleteFriend = JOptionPane.showConfirmDialog(friend_manage,
                        "是否删除好友:" + friend_manage.user_message.friend_name,
                        "删除好友", JOptionPane.YES_NO_OPTION);
                if (isDeleteFriend == JOptionPane.YES_OPTION) {
                    for (Component com : manage_friend_window.panel.getComponents()) {
                        if (com instanceof Friend_Manage) {
                            manage_friend_window.panel.remove(com);
                        }
                    }
                    manage_friend_window.panel.updateUI();
                    manage_friend_window.friend_manage_list.clear();
                    panel_chat_view.friend_chat_tree.removeIf(friend_info ->
                            friend_manage.user_message.friend_account
                                    .equals(friend_info.user_message.friend_account));
                    WeChatWindow.update_panel_chat();
                    for (Friend_Info friend_info :
                            panel_chat_view.friend_chat_tree) {
                        Friend_Manage friend_manage = new Friend_Manage(
                                (User_message) cloneObject(friend_info.user_message));
                        friend_manage.mouse_police.setPanel_chat_view(panel_chat_view);
                        friend_manage.mouse_police.setPanel_contacts_view(panel_contacts_view);
                        friend_manage.mouse_police.setPanel(panel);
                        friend_manage.mouse_police.setCard(card);
                        manage_friend_window.addFriend_Manage(friend_manage);
                    }
                    panel_chat_view.list_ChatBox.removeIf(chatBox_panel ->
                            friend_manage.user_message.friend_account
                                    .equals(chatBox_panel.user_message.friend_account));
                    for (Component com : panel_chat_view.panel.getComponents()) {
                        ChatBox_Panel chatBox_panel = (ChatBox_Panel) com;
                        if (chatBox_panel.user_message.friend_account
                                .equals(friend_manage.user_message.friend_account)) {
                            panel_chat_view.panel.remove(chatBox_panel);
                        }
                    }
                    panel_contacts_view.friend_info_tree.removeIf(friend_info ->
                            friend_manage.user_message.friend_account
                                    .equals(friend_info.user_message.friend_account));
                    WeChatWindow.update_contact_view();
                    try {
                        DataOutputStream request_dataOut = new DataOutputStream(
                                request_socket.getOutputStream());
                        request_dataOut.writeUTF("删除好友");
                        ObjectOutputStream object_out = new ObjectOutputStream(
                                object_socket.getOutputStream());
                        User_message user_message = new User_message();
                        user_message.friend_account = friend_manage.user_message.friend_account;
                        user_message.friend_nickName = friend_manage.user_message.friend_nickName;
                        user_message.me_account = User.user_account;
                        user_message.me_nickName = User.user_nickName;
                        object_out.writeObject(user_message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (friend_window != null) {
            if (event.getSource() == friend_window.chatBox_panel.button_come) {
                if (!chatBox_panel.inputArea.getText().equals("")) {
                    String chat_text = chatBox_panel.inputArea.getText();
                    Font font = new Font("宋体", Font.BOLD, 20);
                    File file = new File(User.user_file_path + "/" + User.user_account +
                            "/images/我.png");
                    BufferedImage bufferedImage = null;
                    try {
                        bufferedImage = ImageIO.read(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Chat_Info chat_info = new Chat_Info(
                            chatBox_panel.getWidth() + 10,
                            bufferedImage,"我", chat_text, font);
                    LocalDateTime last_chat_time = LocalDateTime.now();
                    String last_time = String.format("%tD %<tT", last_chat_time);
                    JLabel label = new JLabel(last_time);
                    User_message user_message_clone =
                            (User_message)cloneObject(chatBox_panel.user_message);
                    user_message_clone.chat_text = chat_text;
                    user_message_clone.last_chat_time = last_chat_time;
                    user_message_clone.me_account = User.user_account;
                    user_message_clone.me_nickName = User.user_nickName;
                    try {
                        DataOutputStream request_dataOut = new DataOutputStream(
                                request_socket.getOutputStream());
                        request_dataOut.writeUTF("发送信息");
                        ObjectOutputStream object_out = new ObjectOutputStream(
                                object_socket.getOutputStream());
                        object_out.writeObject(user_message_clone);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    chatBox_panel.show_panel.add(label);
                    chatBox_panel.show_panel.add(chat_info);
                    chatBox_panel.inputArea.setText(null);
                    chat_info.setMaximumSize(new Dimension(
                            chat_info.getWidth(), chat_info.getHeight()));
                    chatBox_panel.show_panel.add(Box.createVerticalStrut(5));
                    panel_chat_view.friend_chat_tree.removeIf(friend_info ->
                            friend_info.user_message.friend_account
                                    .equals(chatBox_panel.user_message.friend_account));
                    Friend_Info friend_info = new Friend_Info(user_message_clone);
                    panel_chat_view.friend_chat_tree.add(friend_info);
                    chatBox_panel.show_panel.updateUI();
                }
            }
        }
    }
}
