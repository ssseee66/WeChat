package WeChat;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static WeChat.WeChat.request_socket;
import static WeChat.WeChat.info_socket;
import static WeChat.WeChat.object_socket;
import static WeChat.WeChat.image_socket;
import static WeChat.WeChatWindow.panel_chat_view;
import static WeChat.WeChatWindow.panel_contacts_view;
import static WeChat.WeChatWindow.panel_add_view;

public class My_InputDialog {
    public static User_message showInputDialog(User_message user_message) {
        var ref = new Object() {
            User_message user_messages = null;
        };
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setLayout(null);
        dialog.setResizable(false);
        JLabel message_label = new JLabel("为朋友设置备注");
        Font font = new Font("幼圆", Font.BOLD, 13);
        message_label.setFont(font);
        JButton yes_button = new JButton("确认");
        yes_button.setBackground(Color.GREEN);
        JButton no_button = new JButton("取消");
        no_button.setBackground(Color.GREEN);
        JTextField input_field = new JTextField();
        input_field.setFont(font);
        input_field.setDocument(new JTextFieldLimit(12));
        input_field.setText(user_message.friend_nickName);
        dialog.add(message_label);
        message_label.setBounds(80, 20, 100, 20);
        dialog.add(input_field);
        input_field.setBounds(50, 60, 200, 30);
        dialog.add(yes_button);
        yes_button.setBounds(70, 100, 70, 30);
        dialog.add(no_button);
        no_button.setBounds(150, 100, 70, 30);
        yes_button.addActionListener(e -> {
            ref.user_messages = user_message;
            ref.user_messages.friend_name = input_field.getText();
            dialog.setVisible(false);
        });
        no_button.addActionListener(e -> {
            ref.user_messages.friend_name = user_message.friend_nickName;
            dialog.setVisible(false);
        });
        dialog.setBounds(600, 100, 300, 200);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return ref.user_messages;
    }
    public static User_message showAddFriend_Dialog(User_message user_message) {
        var ref = new Object() {
            User_message user_messages;
        };
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setLayout(null);
        dialog.setTitle("发送好友申请");
        Font font  = new Font("幼圆", Font.BOLD, 13);
        JLabel hello_text_label = new JLabel("发送添加朋友申请");
        hello_text_label.setFont(font);
        JLabel friend_name_label = new JLabel("备注名");
        friend_name_label.setFont(font);
        JTextField hello_text_field = new JTextField();
        hello_text_field.setDocument(new JTextFieldLimit(30));
        JTextField friend_name_field = new JTextField();
        friend_name_field.setDocument(new JTextFieldLimit(12));
        friend_name_field.setText(user_message.friend_nickName);
        JButton yes_button = new JButton("确认");
        JButton no_button = new JButton("取消");
        dialog.add(hello_text_label);
        hello_text_label.setBounds(50, 20, 200, 20);
        dialog.add(hello_text_field);
        hello_text_field.setBounds(50, 50, 200, 30);
        dialog.add(friend_name_label);
        friend_name_label.setBounds(50, 90, 100, 20);
        dialog.add(friend_name_field);
        friend_name_field.setBounds(50, 120, 200, 30);
        dialog.add(yes_button);
        yes_button.setBounds(70, 160, 70, 30);
        dialog.add(no_button);
        no_button.setBounds(150, 160, 70, 30);
        yes_button.addActionListener(e -> {
            user_message.friend_name_for_add = friend_name_field.getText();
            user_message.hello_text = hello_text_field.getText();
            ref.user_messages = (User_message)cloneObject(user_message);
            dialog.setVisible(false);
        });
        no_button.addActionListener(e -> dialog.setVisible(false));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setBounds(600, 100, 300, 300);
        dialog.setVisible(true);
        return ref.user_messages;
    }
    public static void showSettingDialog() {
        User_message user_message = null;
        BufferedImage bufferedImage = null;
        try {
            DataOutputStream request_dataOut = new DataOutputStream(
                    request_socket.getOutputStream());
            request_dataOut.writeUTF("获取个人信息");
            DataOutputStream info_dataOut = new DataOutputStream(
                    info_socket.getOutputStream());
            info_dataOut.writeUTF(User.user_account);
            ObjectInputStream object_in = new ObjectInputStream(
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
        var ref = new Object() {
            User_message user_message;
            BufferedImage bufferedImage;
        };
        if (user_message != null) {
            JDialog dialog = new JDialog();
            dialog.setModal(true);
            dialog.setResizable(false);
            dialog.setLayout(null);
            dialog.setTitle("设置个人信息");
            JButton avatar = new JButton();
            avatar.setBorderPainted(false);
            avatar.setBorder(null);
            avatar.setContentAreaFilled(false);
            SetImageIcon.SetIcon(bufferedImage, avatar, 60, 60);
            Font font = new Font("幼圆", Font.BOLD, 13);
            JLabel nickName_label = new JLabel("昵称");
            nickName_label.setFont(font);
            JLabel signature_label = new JLabel("个性签名");
            signature_label.setFont(font);
            JLabel region_label = new JLabel("区域");
            region_label.setFont(font);
            JLabel city_label = new JLabel("地区");
            city_label.setFont(font);
            JTextField nickName_field = new JTextField();
            nickName_field.setDocument(new JTextFieldLimit(12));
            nickName_field.setText(user_message.me_nickName);
            nickName_field.setFont(font);
            JTextField signature_field = new JTextField();
            signature_field.setDocument(new JTextFieldLimit(30));
            signature_field.setText(user_message.me_signature);
            signature_field.setFont(font);
            JTextField region_field = new JTextField();
            region_field.setDocument(new JTextFieldLimit(10));
            region_field.setText(user_message.me_region);
            region_field.setFont(font);
            JTextField city_field = new JTextField();
            city_field.setDocument(new JTextFieldLimit(10));
            city_field.setText(user_message.me_city);
            city_field.setFont(font);
            JButton yes_button = new JButton("确认");
            yes_button.setBackground(Color.GREEN);
            JButton no_button = new JButton("取消");
            no_button.setBackground(Color.GREEN);
            dialog.add(avatar);
            avatar.setBounds(120, 20, 60, 60);
            dialog.add(nickName_label);
            nickName_label.setBounds(50, 90, 200, 20);
            dialog.add(nickName_field);
            nickName_field.setBounds(50, 120, 200, 30);

            dialog.add(signature_label);
            signature_label.setBounds(50, 160, 200, 20);
            dialog.add(signature_field);
            signature_field.setBounds(50, 190, 200, 30);

            dialog.add(region_label);
            region_label.setBounds(50, 230, 100, 20);
            dialog.add(region_field);
            region_field.setBounds(50, 260, 200, 30);

            dialog.add(city_label);
            city_label.setBounds(50, 300, 200, 20);
            dialog.add(city_field);
            city_field.setBounds(50, 330, 200, 30);

            dialog.add(yes_button);
            yes_button.setBounds(70, 370, 70, 30);
            dialog.add(no_button);
            no_button.setBounds(150, 370, 70, 30);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setBounds(600, 100, 300, 500);
            avatar.addActionListener(e -> {
                JFileChooser fileDialog = new JFileChooser();
                fileDialog.setDialogTitle("设置头像");
                FileNameExtensionFilter filter = new FileNameExtensionFilter
                        ("图像文件", "jpg", "gif", "png");
                fileDialog.setFileFilter(filter);
                int state = fileDialog.showOpenDialog(dialog);
                if (state == JFileChooser.APPROVE_OPTION) {
                    File image_file = new File(fileDialog.getCurrentDirectory(),
                            fileDialog.getSelectedFile().getName());
                    SetImageIcon.SetIcon(image_file, avatar, 60, 60);
                    try {
                        ref.bufferedImage = ImageIO.read(image_file);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
            if (ref.bufferedImage == null) {
                try {
                    if (bufferedImage != null) {
                        ref.bufferedImage = SetImageIcon.getBufferedImage(bufferedImage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            yes_button.addActionListener(e -> {
                ref.user_message = new User_message();
                ref.user_message.me_nickName = nickName_field.getText();
                ref.user_message.me_signature = signature_field.getText();
                ref.user_message.me_region = region_field.getText();
                ref.user_message.me_city = city_field.getText();
                ref.user_message.me_account = User.user_account;
                User.user_nickName = ref.user_message.me_nickName;
                if (ref.bufferedImage != null) {
                    File image_old = new File(User.user_file_path + "/" + User.user_account  +
                            "/images/我.png");
                    if (image_old.delete()) {
                        File image_new = new File(User.user_file_path + "/" + User.user_account  +
                                "/images/我.png");
                        // create a blank, RGB, same width and height, and a white background
                        ref.bufferedImage = SetImageIcon.getBufferedImage(ref.bufferedImage);
                        try {
                            ImageIO.write(ref.bufferedImage, "png", image_new);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        File path = new File(User.user_file_path + "/" + User.user_account + "/images");
                        SetImageIcon.SetIcon(path, panel_contacts_view.panel_function.myself,
                                60, 60, "我");
                        SetImageIcon.SetIcon(path, panel_chat_view.panel_function.myself,
                                60, 60, "我");
                        SetImageIcon.SetIcon(path, panel_add_view.panel_function.myself,
                                60, 60, "我");
                    }
                    try {
                        DataOutputStream request_dataOut = new DataOutputStream(
                                request_socket.getOutputStream());
                        request_dataOut.writeUTF("更新用户个人信息");
                        ObjectOutputStream object_out = new ObjectOutputStream(
                                object_socket.getOutputStream());
                        object_out.writeObject(ref.user_message);
                        ObjectOutputStream image_byte_out =
                                new ObjectOutputStream(image_socket.getOutputStream());
                        ByteArrayOutputStream image_binary_out =
                                new ByteArrayOutputStream();
                        ImageIO.write(ref.bufferedImage, "png",
                                image_binary_out);
                        image_byte_out.writeObject(image_binary_out.toByteArray());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    dialog.dispose();
                }
            });
            no_button.addActionListener(e -> dialog.dispose());
            dialog.setVisible(true);
        }
    }
    public static void showUserInformationDialog() {
        User_message user_message = null;
        BufferedImage bufferedImage = null;
        try {
            DataOutputStream request_dataOut = new DataOutputStream(
                    request_socket.getOutputStream());
            request_dataOut.writeUTF("获取个人信息");
            DataOutputStream info_dataOut = new DataOutputStream(
                    info_socket.getOutputStream());
            info_dataOut.writeUTF(User.user_account);
            if (image_socket != null) {
                ObjectInputStream image_byte_in = new ObjectInputStream(
                        image_socket.getInputStream());
                byte[] image_binary_byte = (byte[])image_byte_in.readObject();
                ByteArrayInputStream image_binary_in =
                        new ByteArrayInputStream(image_binary_byte);
                bufferedImage = ImageIO.read(image_binary_in);
            }
            ObjectInputStream object_in = new ObjectInputStream(
                    object_socket.getInputStream());
            user_message =
                    (User_message) object_in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user_message != null) {
            JDialog dialog = new JDialog();
            dialog.setModal(true);
            dialog.setResizable(false);
            dialog.setLayout(null);
            dialog.setTitle("查看个人信息");
            JButton avatar = new JButton();
            avatar.setBorderPainted(false);
            avatar.setBorder(null);
            avatar.setContentAreaFilled(false);
            SetImageIcon.SetIcon(bufferedImage, avatar, 60, 60);
            Font font = new Font("幼圆", Font.BOLD, 13);
            JLabel nickName_label = new JLabel("昵称");
            nickName_label.setFont(font);
            JLabel signature_label = new JLabel("个性签名");
            signature_label.setFont(font);
            JLabel region_label = new JLabel("区域");
            region_label.setFont(font);
            JLabel city_label = new JLabel("地区");
            city_label.setFont(font);
            JTextField nickName_field = new JTextField();
            nickName_field.setDocument(new JTextFieldLimit(12));
            nickName_field.setText(user_message.me_nickName);
            nickName_field.setFont(font);
            nickName_field.setDisabledTextColor(Color.CYAN);
            nickName_field.setEnabled(false);
            JTextField signature_field = new JTextField();
            signature_field.setDocument(new JTextFieldLimit(30));
            signature_field.setText(user_message.me_signature);
            signature_field.setFont(font);
            signature_field.setDisabledTextColor(Color.CYAN);
            signature_field.setEnabled(false);
            JTextField region_field = new JTextField();
            region_field.setDocument(new JTextFieldLimit(10));
            region_field.setText(user_message.me_region);
            region_field.setFont(font);
            region_field.setDisabledTextColor(Color.CYAN);
            region_field.setEnabled(false);
            JTextField city_field = new JTextField();
            city_field.setDocument(new JTextFieldLimit(10));
            city_field.setText(user_message.me_city);
            city_field.setFont(font);
            city_field.setDisabledTextColor(Color.CYAN);
            city_field.setEnabled(false);
            dialog.add(avatar);
            avatar.setBounds(120, 20, 60, 60);
            dialog.add(nickName_label);
            nickName_label.setBounds(50, 90, 200, 20);
            dialog.add(nickName_field);
            nickName_field.setBounds(50, 120, 200, 30);

            dialog.add(signature_label);
            signature_label.setBounds(50, 160, 200, 20);
            dialog.add(signature_field);
            signature_field.setBounds(50, 190, 200, 30);

            dialog.add(region_label);
            region_label.setBounds(50, 230, 100, 20);
            dialog.add(region_field);
            region_field.setBounds(50, 260, 200, 30);

            dialog.add(city_label);
            city_label.setBounds(50, 300, 200, 20);
            dialog.add(city_field);
            city_field.setBounds(50, 330, 200, 30);

            dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            dialog.setBounds(600, 100, 300, 430);
            dialog.setVisible(true);
        }
    }
    public static Object cloneObject(Object object) {
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
    static class JTextFieldLimit extends PlainDocument {
        private final int limit;
        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null)
                return;
            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }
}
