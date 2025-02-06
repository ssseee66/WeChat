package WeChat;

/*
实时监控自己的好友申请是否通过对方的同意，以及是否有别的用户申请添加自己为好友或者讲自己删除
同时监控聊天信息接收
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;

import static WeChat.WeChat.request_socket;
import static WeChat.WeChat.object_socket;
import static WeChat.WeChat.image_socket;
public class Client_Monitor implements Runnable {
    public Chat_View panel_chat_view;
    public Contacts_View panel_contacts_view;
    public JPanel panel;
    public CardLayout card;
    public WeChatWindow mainWindow;
    public void setMainWindow(WeChatWindow mainWindow) {
        this.mainWindow = mainWindow;
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

    public void run() {
        while (true) {
            try {
                if (request_socket != null) {
                    DataInputStream request_dataIn = new DataInputStream(
                            request_socket.getInputStream());
                    String request = request_dataIn.readUTF();
                    switch (request) {
                        case "对方同意添加申请" -> {
                            if (object_socket != null && image_socket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_socket.getInputStream());
                                User_message user_message =
                                        (User_message) object_in.readObject();
                                User_message user_message_clone = (User_message) cloneObject(user_message);
                                user_message_clone.object_type = "聊天";
                                Friend_Info friend_info = new Friend_Info(user_message_clone);
                                panel_chat_view.friend_chat_tree.add(friend_info);
                                WeChatWindow.update_panel_chat();
                                ChatBox_Panel chatBox_panel = new ChatBox_Panel(
                                        (User_message) cloneObject(friend_info.user_message));
                                Mouse_Police mouse_police = new Mouse_Police();
                                mouse_police.setButton(chatBox_panel.button_come);
                                mouse_police.setChatBox_panel(chatBox_panel);
                                mouse_police.setPanel_chat_view(panel_chat_view);
                                chatBox_panel.button_come.addMouseListener(mouse_police);
                                Font font = new Font("宋体", Font.BOLD, 20);
                                File file = new File(User.user_file_path + "/" +
                                        User.user_account + "/images/我.png");
                                BufferedImage bufferedImage = null;
                                try {
                                    bufferedImage = ImageIO.read(file);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Chat_Info chat_info = new Chat_Info(
                                        chatBox_panel.getWidth() + 55,
                                        bufferedImage, "我", user_message_clone.chat_text, font);
                                LocalDateTime last_chat_time = user_message_clone.last_chat_time;
                                String last_time = String.format("%tD %<tT", last_chat_time);
                                JLabel label = new JLabel(last_time);
                                chatBox_panel.show_panel.add(label);
                                chatBox_panel.show_panel.add(chat_info);
                                chat_info.setMaximumSize(new Dimension(
                                        chat_info.getWidth(), chat_info.getHeight()));
                                chatBox_panel.show_panel.add(Box.createVerticalStrut(5));
                                panel_chat_view.list_ChatBox.add(chatBox_panel);
                                panel_chat_view.panel.add(user_message.friend_name, chatBox_panel);
                                card.show(panel, "聊天界面");
                                panel_chat_view.card.show(panel_chat_view.panel,
                                        user_message.friend_name);
                                user_message_clone =
                                        (User_message) cloneObject(user_message);
                                user_message_clone.object_type = "联系人";
                                Friend_Info contacts = new Friend_Info(user_message_clone);
                                panel_contacts_view.friend_info_tree.add(contacts);    //last
                                WeChatWindow.update_contact_view();
                                card.show(panel, "聊天界面");
                                panel_chat_view.card.show(panel_chat_view.panel, user_message.friend_name);
                                JOptionPane.showMessageDialog(null,
                                        "用户:" + user_message.friend_nickName +
                                                "同意了您的好友申请!\n你们可以聊天了!", "好友申请通过",
                                        JOptionPane.QUESTION_MESSAGE);
                            }
                        }
                        case "对方发来好友申请" -> {
                            if (object_socket != null && image_socket != null) {
                                ObjectInputStream objectIn = new ObjectInputStream(
                                        object_socket.getInputStream());
                                User_message user_message =
                                        (User_message) objectIn.readObject();
                                Friend_Apply friend_apply = new Friend_Apply(user_message);
                                panel_contacts_view.friend_message.newFriend_panel
                                        .add(Box.createVerticalStrut(10));
                                Action_Police action_police = new Action_Police();
                                action_police.setPanel(panel);
                                action_police.setCard(card);
                                action_police.setPanel_chat_view(panel_chat_view);
                                action_police.setPanel_contacts_view(panel_contacts_view);
                                action_police.setFriend_apply(friend_apply);
                                action_police.setButton(friend_apply.yes);
                                friend_apply.yes.addActionListener(action_police);
                                panel_contacts_view.friend_message.newFriend_panel
                                        .add(friend_apply);
                                panel_contacts_view.friend_message.newFriend_panel
                                        .updateUI();
                                card.show(panel, "好友信息界面");
                                panel_contacts_view.friend_message
                                        .card.show(panel_contacts_view.friend_message, "新的朋友");
                                JOptionPane.showMessageDialog(null,
                                        "用户:" + user_message.friend_nickName + "申请添加您为好友");
                            }
                        }
                        case "对方发来信息" -> {
                            if (object_socket != null) {
                                ObjectInputStream objectIn = new ObjectInputStream(
                                        object_socket.getInputStream());
                                User_message user_message = (User_message) objectIn.readObject();
                                if (user_message != null) {
                                    ChatBox_Panel chatBox_panel = null;
                                    for (ChatBox_Panel chatBox_panel1 :
                                            panel_chat_view.list_ChatBox) {
                                        if (chatBox_panel1.user_message.friend_account
                                                .equals(user_message.friend_account))
                                            chatBox_panel = chatBox_panel1;
                                    }
                                    Font font = new Font("宋体", Font.BOLD, 20);
                                    Chat_Info chat_info;
                                    if (chatBox_panel != null) {
                                        BufferedImage bufferedImage = SetImageIcon.getBufferedImage(
                                                user_message.friend_avatar);
                                        chat_info = new Chat_Info(
                                                chatBox_panel.getWidth() + 25,
                                                bufferedImage, user_message.friend_name, user_message.chat_text, font);
                                        JLabel label = new JLabel(
                                                String.format("%tD %<tT", user_message.last_chat_time));
                                        chatBox_panel.show_panel.add(label);
                                        chatBox_panel.show_panel.add(chat_info);
                                        chatBox_panel.inputArea.setText(null);
                                        chat_info.setMaximumSize(new Dimension(
                                                chat_info.getWidth(), chat_info.getHeight()));
                                        chatBox_panel.show_panel.add(Box.createVerticalStrut(5));
                                        Friend_Info friend_info = null;
                                        for (Friend_Info friend_info1 : panel_chat_view.friend_chat_tree) {
                                            if (friend_info1.user_message.friend_account
                                                    .equals(chatBox_panel.user_message.friend_account))
                                                friend_info = friend_info1;
                                        }
                                        for (Friend_Info friend_info1 : panel_chat_view.friend_chat_tree) {
                                            if (friend_info != null) {
                                                if (friend_info1.user_message.friend_account
                                                        .equals(friend_info.user_message.friend_account))
                                                    panel_chat_view.friend_chat_tree.remove(friend_info1);
                                            }

                                        }
                                        User_message user_messages =
                                                (User_message)cloneObject(user_message);
                                        user_messages.object_type = "聊天";
                                        friend_info = new Friend_Info(user_messages);
                                        friend_info.user_message.setFriend_account(
                                                chatBox_panel.user_message.friend_account);
                                        panel_chat_view.friend_chat_tree.add(friend_info);
                                    }
                                    WeChatWindow.update_panel_chat();
                                    if (chatBox_panel != null) {
                                        chatBox_panel.show_panel.updateUI();
                                        chatBox_panel.inputArea.setText(null);
                                    }
                                    card.show(panel, "聊天界面");
                                    panel_chat_view.card.show(panel_chat_view.panel, user_message.friend_name);
                                }
                            }
                        }
                        case "对方将您删除" -> {
                            if (object_socket != null) {
                                ObjectInputStream object_in = new ObjectInputStream(
                                        object_socket.getInputStream());
                                String deleteYou_friend_account =
                                        (String) object_in.readObject();
                                String friend_name = null;
                                for (Friend_Info friend_info : panel_chat_view.friend_chat_tree) {
                                    if (friend_info.user_message.friend_account.equals(deleteYou_friend_account)) {
                                        friend_name = friend_info.user_message.friend_name;
                                    }
                                }
                                panel_chat_view.friend_chat_tree.removeIf(friend_info ->
                                        friend_info.user_message.friend_account
                                                .equals(deleteYou_friend_account));
                                WeChatWindow.update_panel_chat();
                                panel_chat_view.list_ChatBox.removeIf(chatBox_panel ->
                                        chatBox_panel.user_message.friend_account
                                                .equals(deleteYou_friend_account));
                                for (ChatBox_Panel chatBox_panel :
                                        panel_chat_view.list_ChatBox) {
                                    if (chatBox_panel.user_message.friend_account
                                            .equals(deleteYou_friend_account)) {
                                        panel_chat_view.panel.remove(chatBox_panel);
                                        card.show(panel, "聊天界面");
                                        panel_chat_view.card.show(
                                                panel_chat_view.panel, "空白");
                                    }
                                }
                                panel_contacts_view.friend_info_tree.removeIf(friend_info ->
                                        friend_info.user_message.friend_account
                                                .equals(deleteYou_friend_account));
                                WeChatWindow.update_contact_view();
                                card.show(panel, "聊天界面");
                                panel_chat_view.card.show(panel_chat_view.panel, "null");
                                JOptionPane.showMessageDialog(null, "您的好友:" + friend_name +
                                        "将您删除了!", "好友删除了您!", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        case "二次登录" -> mainWindow.dispose();
                    }
                }
            } catch (Exception e) {
                System.out.println("暂时无数据.....");
                System.out.println(e.getMessage());
                return;
            }
        }
    }
    public Object cloneObject(Object object) {
        Object object_clone = new Object();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objectOut =
                    new ObjectOutputStream(out);
            objectOut.writeObject(object);
            objectOut.close();
            ByteArrayInputStream in =
                    new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream objectIn =
                    new ObjectInputStream(in);
            object_clone = objectIn.readObject();
            in.close();
            objectIn.close();
        } catch (Exception ignored) {
        }
        return object_clone;
    }
}

