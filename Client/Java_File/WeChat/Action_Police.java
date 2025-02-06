package WeChat;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.zip.ZipFile;

import static WeChat.WeChat.request_socket;
import static WeChat.WeChat.image_socket;
import static WeChat.WeChat.object_socket;
import static WeChat.WeChat.info_socket;
import static WeChat.WeChat.isNotRecord;

public class Action_Police extends AbstractAction implements ActionListener{
    public JButton avatar;
    public JButton button;
    public Login login_view;
    public User_Panel user_panel;
    public JPanel panel;
    public CardLayout card;
    public Chat_View panel_chat_view;
    public Friend_Apply friend_apply;
    public Contacts_View panel_contacts_view;
    public void setView(Login login_view) {
        this.login_view = login_view;
    }
    public void setAvatar(JButton avatar) {
        this.avatar = avatar;
    }
    public void setButton(JButton button) {
        this.button = button;
    }
    public void setUser_panel(User_Panel user_panel) {
        this.user_panel = user_panel;
    }
    public void setPanel_chat_view(Chat_View panel_chat_view) {
        this.panel_chat_view = panel_chat_view;
    }
    public void setFriend_apply(Friend_Apply friend_apply) {
        this.friend_apply = friend_apply;
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
    public void actionPerformed(ActionEvent event) {
        if (login_view != null) {
            if (event.getSource() == login_view.avatar) {
                JFileChooser fileDialog = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter
                        ("图像文件", "jpg", "gif", "png");
                fileDialog.setFileFilter(filter);
                int state = fileDialog.showOpenDialog(login_view);
                if (state == JFileChooser.APPROVE_OPTION) {
                    File path = fileDialog.getCurrentDirectory();
                    String name = fileDialog.getSelectedFile().getName();
                    login_view.user.setAvatar_file_path(path + "/" + name);
                    File file = new File(path, name);
                    SetImageIcon.SetIcon(
                            file, login_view.avatar, 48, 48);
                }
            }
            else if (event.getSource() == login_view.register_button) {
                login_view.panel.requestFocusInWindow();
                try {
                    DataOutputStream request_dataOut = new DataOutputStream(
                            request_socket.getOutputStream());
                    request_dataOut.writeUTF("注册");
                    ObjectInputStream object_in = new ObjectInputStream(
                            object_socket.getInputStream());
                    User.user_message_list =
                            CastList.castList(object_in.readObject(), User_message.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                login_view.avatar.requestFocus();
                login_view.card.show(login_view.panels, "注册");
            }
            else if (event.getSource() == login_view.login_button) {
                User user = new User();
                user.setAccount(login_view.account_field.getText());
                String password = new String(
                        login_view.password_field.getPassword());
                char[] chars = password.toCharArray();
                int num = 0;
                for (char c : chars) {
                    chars[num++] = (char)(c * 5 + '@');
                }
                user.setPassword(new String(chars));
                try {
                    DataOutputStream request_dataOut = new DataOutputStream(
                            request_socket.getOutputStream());
                    request_dataOut.writeUTF("登录");
                    OutputStream object_outPut = object_socket.getOutputStream();
                    ObjectOutputStream objectOut = new ObjectOutputStream(object_outPut);
                    objectOut.writeObject(user);
                    InputStream info_inPut = info_socket.getInputStream();
                    DataInputStream infoIn = new DataInputStream(info_inPut);
                    if (infoIn.readBoolean()) {
                        ObjectInputStream object_in = new ObjectInputStream(
                                object_socket.getInputStream());
                        User_message user_message  =
                                (User_message) object_in.readObject();
                        User.user_account = user_message.me_account;
                        User.user_nickName = user_message.me_nickName;
                        User.user_message =
                                (User_message) cloneObject(user_message);
                        login_view.dispose();
                        user_message = null;
                        BufferedImage bufferedImage = null;
                        try {
                            request_dataOut = new DataOutputStream(
                                    request_socket.getOutputStream());
                            request_dataOut.writeUTF("获取个人信息");
                            DataOutputStream info_dataOut = new DataOutputStream(
                                    info_socket.getOutputStream());
                            info_dataOut.writeUTF(User.user_account);
                            object_in = new ObjectInputStream(
                                    object_socket.getInputStream());
                            user_message =
                                    (User_message) object_in.readObject();
                            if (image_socket != null) {
                                ObjectInputStream image_byte_in = new ObjectInputStream(
                                        image_socket.getInputStream());
                                byte[] image_binary_byte = (byte[])image_byte_in.readObject();
                                ByteArrayInputStream image_binary_in =
                                        new ByteArrayInputStream(image_binary_byte);
                                bufferedImage = ImageIO.read(image_binary_in);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        User.user_message = user_message;
                        File fileDir = new File(User.user_file_path + "/" + User.user_account );
                        boolean boo = fileDir.mkdir();
                        if (boo) {
                            isNotRecord = true;
                        }
                        fileDir = new File(fileDir, "/file");
                        boo = fileDir.mkdir();
                        try {
                            File file = new File(fileDir, "friend_chat.txt");
                            boo = file.createNewFile();
                            fileDir = new File(fileDir, "../images");
                            boo = fileDir.mkdir();
                        } catch (IOException e) {
                            System.out.println("fileError: " + e.getMessage());
                        }
                        File file = new File(User.user_file_path + "/" + User.user_account +
                                "/images/我.png");
                        if (bufferedImage != null) {
                            ImageIO.write(bufferedImage, "png", file);
                        }
                        WeChatWindow window = new WeChatWindow();
                        window.setBounds(300, 100, 1120, 620);
                    }
                    else {
                        JOptionPane.showMessageDialog(
                                login_view, "账号或密码错误请重新输入",
                                "账号或密码错误", JOptionPane.WARNING_MESSAGE);
                        login_view.account_field.setText("请输入账号");
                        login_view.password_field.setText("请输入密码");
                        login_view.password_field.setEchoChar((char)0);
                        login_view.panels.requestFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (event.getSource() == login_view.confirm_button) {
                if (login_view.isFormat_account && login_view.isFormat_password) {
                    login_view.user.setNickName(login_view.nickName_field.getText());
                    login_view.user.setAccount(login_view.new_account_field.getText());
                    //加密
                    String password = new String(
                            login_view.new_password_field.getPassword());
                    char[] chars = password.toCharArray();
                    int num = 0;
                    for (char c : chars) {
                        chars[num++] = (char)(c * 5 + '@');
                    }
                    login_view.user.setPassword(new String(chars));
                    User.user_account = login_view.new_account_field.getText();
                    File fileDir = new File(User.user_file_path + "/" + User.user_account );
                    boolean boo = fileDir.mkdir();
                    fileDir = new File(fileDir, "/file");
                    boo = fileDir.mkdir();
                    try {
                        File file = new File(fileDir, "friend_chat.txt");
                        boo = file.createNewFile();
                        fileDir = new File(fileDir, "../images");
                        boo = fileDir.mkdir();
                    } catch (IOException e) {
                        System.out.println("fileError: " + e.getMessage());
                    }
                    BufferedImage bufferedImage = null;
                    try {
                        if (login_view.user.avatar_file_path != null) {
                            bufferedImage = ImageIO.read(
                                    new File(login_view.user.avatar_file_path));
                        } else {
                            try {
                                ZipFile zipFile = new ZipFile(
                                        System.getProperty("user.dir") + "/nice.jar",
                                        Charset.forName("GBK"));
                                bufferedImage = ImageIO.read(zipFile.getInputStream(
                                        zipFile.getEntry("image/function/默认头像.png")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    File image_file = new File(User.user_file_path + "/" + User.user_account , "/images/我.png");
                    try {
                        if (bufferedImage != null) {
                            bufferedImage = SetImageIcon.getBufferedImage(bufferedImage);
                            ImageIO.write(bufferedImage, "png", image_file);
                        }
                    } catch (IOException e) {
                        System.out.println("fileError: " + e.getMessage());
                    }
                    try {
                        DataOutputStream request_dataOut = new DataOutputStream(
                                request_socket.getOutputStream());
                        request_dataOut.writeUTF("注册成功");
                        ObjectOutputStream object_Out = new ObjectOutputStream(
                                object_socket.getOutputStream());
                        object_Out.writeObject(login_view.user);
                        BufferedImage newBufferedImage =
                                ImageIO.read(
                                        new File(User.user_file_path + "/" + User.user_account ,
                                                    "/images/我.png"));
                        ByteArrayOutputStream image_binary_out =
                                new ByteArrayOutputStream();
                        ImageIO.write(
                                newBufferedImage, "png",
                                image_binary_out);
                        ObjectOutputStream image_byte_out =
                                new ObjectOutputStream(
                                        image_socket.getOutputStream());
                        image_byte_out.writeObject(image_binary_out.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("服务器已断开！......");
                    } finally {
                        try {
                            InetAddress inetAddress = InetAddress.getByName("192.168.95.95");
                            if (inetAddress.isReachable(5000))
                                System.out.println("主机是可达的");
                            else
                                System.out.println("主机是不可达的");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    String path = "image/function/";
                    SetImageIcon.SetIcon(path, login_view.avatar, 48, 48, "空");
                    login_view.nickName_field.setText("请填写您的昵称（1~12位字符）");
                    login_view.new_account_field.setText("账号为8~20位的大、小写字母与数字组合");
                    login_view.new_password_field.setText("密码格式为8~20位的字母与数字组合");
                    login_view.panels.requestFocus();
                    User.user_account = "";
                    int isBack = JOptionPane.showConfirmDialog(login_view,
                            "恭喜注册成功,是否返回登录?", "注册成功",
                            JOptionPane.YES_NO_OPTION);
                    if (isBack == JOptionPane.YES_OPTION)
                        login_view.card.show(login_view.panels, "登录");
                    else if (isBack == JOptionPane.NO_OPTION)
                        login_view.card.show(login_view.panels, "注册");
                }
            }
        }
        if (user_panel != null) {
            if (event.getSource() == user_panel.add_user) {
                if (user_panel.add_user.getText().equals("添加到通讯录")) {
                    User_message user_message = My_InputDialog
                            .showAddFriend_Dialog(user_panel.user_message);
                    //user_message携带了索要添加的好友账号、昵称和本人的账号、昵称以及
                    //本人为其设置的备注名（未设置则默认为好友昵称)还有招呼语。
                    if (user_message != null) {
                        try {
                            DataOutputStream request_dataOut = new DataOutputStream(
                                    request_socket.getOutputStream());
                            request_dataOut.writeUTF("添加好友");
                            ObjectOutputStream objectOut = new ObjectOutputStream(
                                    object_socket.getOutputStream());
                            user_message.me_account = User.user_account;
                            user_message.me_nickName = User.user_nickName;
                            objectOut.writeObject(user_message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (user_panel.add_user.getText().equals("查看个人信息")) {
                    My_InputDialog.showUserInformationDialog();
                } else if (user_panel.add_user.getText().equals("发送信息")) {
                    card.show(panel, "聊天界面");
                    panel_chat_view.card.show(panel_chat_view.panel, user_panel.user_message.friend_name);
                }
            }
        }
        if (friend_apply != null) {
            if (event.getSource() == friend_apply.yes) {
                User_message user_message = My_InputDialog
                        .showInputDialog(friend_apply.user_message);
                if (user_message != null) {
                    friend_apply.user_message = user_message;
                } else {
                    friend_apply.user_message.friend_name =
                            friend_apply.user_message.friend_nickName;
                }
                User_message user_message_clone =
                        (User_message) cloneObject(friend_apply.user_message);
                user_message_clone.me_account = User.user_account;
                user_message_clone.me_nickName = User.user_nickName;
                user_message_clone.chat_text =
                        friend_apply.user_message.hello_text;
                user_message_clone.last_chat_time = LocalDateTime.now();
                user_message_clone.object_type = "聊天";
                Friend_Info friend_info = new Friend_Info(user_message_clone);
                panel_chat_view.friend_chat_tree.add(friend_info);
                WeChatWindow.update_panel_chat();
                user_message_clone =
                        (User_message) cloneObject(user_message_clone);
                ChatBox_Panel chatBox_panel = new ChatBox_Panel(user_message_clone);
                Mouse_Police mouse_police = new Mouse_Police();
                mouse_police.setButton(chatBox_panel.button_come);
                mouse_police.setChatBox_panel(chatBox_panel);
                mouse_police.setPanel_chat_view(panel_chat_view);
                chatBox_panel.button_come.addMouseListener(mouse_police);
                panel_chat_view.list_ChatBox.add(chatBox_panel);
                panel_chat_view.panel.add(friend_info.user_message.friend_name, chatBox_panel);
                card.show(panel, "聊天界面");
                Font font = new Font("宋体", Font.BOLD, 20);
                BufferedImage bufferedImage = SetImageIcon.getBufferedImage(
                        user_message_clone.friend_avatar);
                Chat_Info chat_info = new Chat_Info(
                        chatBox_panel.getWidth() + 25,
                        bufferedImage, user_message_clone.friend_name,
                        user_message_clone.chat_text, font);
                JLabel label = new JLabel(
                        String.format("%tD %<tT", user_message_clone.last_chat_time));
                chatBox_panel.show_panel.add(label);
                chatBox_panel.show_panel.add(chat_info);
                chatBox_panel.inputArea.setText(null);
                chat_info.setMaximumSize(new Dimension(
                        chat_info.getWidth(), chat_info.getHeight()));
                chatBox_panel.show_panel.add(Box.createVerticalStrut(5));
                panel_chat_view.card.show(panel_chat_view.panel,
                        user_message_clone.friend_name);
                user_message_clone =
                        (User_message) cloneObject(user_message_clone);
                user_message_clone.object_type = "联系人";
                friend_info = new Friend_Info(user_message_clone);
                panel_contacts_view.friend_info_tree.add(friend_info);
                WeChatWindow.update_contact_view();
                friend_apply.user_message.me_account = User.user_account;
                friend_apply.user_message.me_nickName = User.user_nickName;
                friend_apply.user_message.accept = true;
                friend_apply.yes.setText("已接受");
                friend_apply.yes.setEnabled(false);
                panel_contacts_view.friend_message.newFriend_panel.updateUI();
                try {
                    DataOutputStream request_dataOut = new DataOutputStream(
                            request_socket.getOutputStream());
                    request_dataOut.writeUTF("同意申请");
                    ObjectOutputStream object_out = new ObjectOutputStream(
                            object_socket.getOutputStream());
                    object_out.writeObject(cloneObject(user_message_clone));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
