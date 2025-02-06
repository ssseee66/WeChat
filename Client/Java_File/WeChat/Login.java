package WeChat;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Login extends JFrame {
    public JPanel panel;
    public JPanel panels;
    public CardLayout card;
    public Focus_Police focus_police;
    public JTextField account_field;
    public JPasswordField password_field;
    public JTextField new_account_field;
    public JPasswordField new_password_field;
    public JTextField nickName_field;
    public JButton eye_login;
    public JButton eye_register;
    public Mouse_Police police;
    public JButton back_button;
    public String password_field_text;
    public JButton avatar;
    public boolean isFormat_account = false;
    public boolean isFormat_password = false;
    public boolean isFormat_nickName = false;
    public JButton confirm_button;
    public JButton login_button;
    public JButton register_button;
    public User user;
    //限制输入框的字符串长度
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
    public Login() {
        user = new User();
        panels = new JPanel();
        card = new CardLayout();
        panels.setLayout(card);
        panel = new JPanel();
        panel.setLayout(null);
        //登录界面大小
        panel.setSize(400, 400);
        JLabel account_label = new JLabel("账号");
        account_field = new JTextField();
        focus_police = new Focus_Police();
        focus_police.setView(this);
        //先让面板获得焦点
        panel.setFocusable(true);
        account_field.setDocument(new JTextFieldLimit(20));
        account_field.setText("请输入账号");
        account_field.setFont(new Font("宋体", Font.BOLD, 14));
        account_field.addFocusListener(focus_police);
        JLabel password_label = new JLabel("密码");
        password_field = new JPasswordField();
        password_field.setDocument(new JTextFieldLimit(20));
        password_field_text = "请输入密码";
        password_field.setText("请输入密码");
        password_field.setFont(new Font("宋体", Font.BOLD, 14));
        password_field.setEchoChar((char)0);
        password_field.addFocusListener(focus_police);
        //登录界面密码框小眼睛按钮
        eye_login = new JButton();
        String path = "image/function/";
        SetImageIcon.SetIcon(path, eye_login, 20, 20, "隐藏");
        eye_login.setContentAreaFilled(false);
        eye_login.setBorder(null);
        eye_login.setBorderPainted(false);
        eye_login.setName("隐藏");
        police = new Mouse_Police();
        police.setLogin_view(this);
        eye_login.addMouseListener(police);

        Box account_box = Box.createHorizontalBox();
        account_box.add(account_label);
        account_box.add(Box.createHorizontalStrut(10));
        account_box.add(account_field);

        Box password_box = Box.createHorizontalBox();
        password_box.add(password_label);
        password_box.add(Box.createHorizontalStrut(10));
        password_box.add(password_field);
        password_box.add(Box.createHorizontalStrut(2));
        password_box.add(eye_login);
        panel.add(account_box);
        account_box.setBounds(50, 120, 300, 30);
        panel.add(password_box);
        password_box.setBounds(50, 160, 320, 30);
        login_button = new JButton("登录");
        Action_Police action_police = new Action_Police();
        login_button.addActionListener(action_police);
        InputMap inputMap = login_button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "登录");
        ActionMap actionMap = login_button.getActionMap();
        actionMap.put("登录", action_police);
        action_police.setView(this);
        action_police.setButton(login_button);
        login_button.setFont(new Font("宋体", Font.BOLD, 18));
        login_button.setBackground(Color.GREEN);
        panel.add(login_button);
        login_button.setBounds(80, 250, 80, 40);
        panels.add("登录", panel);
        register_button = new JButton("注册");
        action_police = new Action_Police();
        register_button.addActionListener(action_police);
        action_police.setView(this);
        action_police.setButton(register_button);
        register_button.setFont(new Font("宋体", Font.BOLD, 18));
        register_button.setBackground(Color.GREEN);
        panel.add(register_button);
        register_button.setBounds(240, 250, 80, 40);
        add(panels);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        back_button = new JButton();
        SetImageIcon.SetIcon(path, back_button, 24, 24, "返回");
        back_button.setContentAreaFilled(false);
        back_button.setBorderPainted(false);
        back_button.setBorder(null);
        police = new Mouse_Police();
        police.setLogin_view(this);
        back_button.addMouseListener(police);
        panel.add(back_button);
        back_button.setBounds(0, 0, 24, 24);
        avatar = new JButton();
        action_police = new Action_Police();
        avatar.addActionListener(action_police);
        action_police.setAvatar(avatar);
        action_police.setView(this);
        SetImageIcon.SetIcon(path, avatar, 48, 48, "空");
        avatar.setBorder(null);
        panel.add(avatar);
        avatar.setBounds(180, 50, 48, 48);
        Font font = new Font("宋体", Font.BOLD, 13);
        JLabel nickName = new JLabel("昵称");
        nickName.setFont(font);
        nickName_field = new JTextField();
        nickName_field.setDocument(new JTextFieldLimit(12));
        nickName_field.setFont(font);
        nickName_field.setText("昵称（1~12位字符）");
        nickName_field.addFocusListener(focus_police);
        Box nickName_box = Box.createHorizontalBox();
        nickName_box.add(nickName);
        nickName_box.add(Box.createHorizontalStrut(10));
        nickName_box.add(nickName_field);
        JLabel new_account = new JLabel("账号");
        new_account.setFont(font);
        new_account_field = new JTextField();
        new_account_field.setDocument(new JTextFieldLimit(20));
        new_account_field.setFont(font);
        new_account_field.setText("账号为8~20位的大、小写字母与数字组合");
        new_account_field.addFocusListener(focus_police);
        Box new_account_box = Box.createHorizontalBox();
        new_account_box.add(new_account);
        new_account_box.add(Box.createHorizontalStrut(10));
        new_account_box.add(new_account_field);
        JLabel new_password = new JLabel("密码");
        new_password.setFont(font);
        new_password_field = new JPasswordField();
        new_password_field.setDocument(new JTextFieldLimit(20));
        new_password_field.setEchoChar((char)0);
        new_password_field.setFont(font);
        new_password_field.setText("密码格式为8~20位的字母与数字组合");
        new_password_field.addFocusListener(focus_police);
        eye_register = new JButton();
        SetImageIcon.SetIcon(path, eye_register, 20, 20, "隐藏");
        eye_register.setContentAreaFilled(false);
        eye_register.setBorder(null);
        eye_register.setBorderPainted(false);
        eye_register.setName("隐藏");
        eye_register.addMouseListener(police);
        Box new_password_box = Box.createHorizontalBox();
        new_password_box.add(new_password);
        new_password_box.add(Box.createHorizontalStrut(10));
        new_password_box.add(new_password_field);
        new_password_box.add(Box.createHorizontalStrut(2));
        new_password_box.add(eye_register);
        confirm_button = new JButton("确认");
        action_police = new Action_Police();
        confirm_button.addActionListener(action_police);
        action_police.setView(this);
        confirm_button.setFont(new Font("宋体", Font.BOLD, 20));
        confirm_button.setBorderPainted(false);
        confirm_button.setBackground(Color.GREEN);
        panel.add(nickName_box);
        nickName_box.setBounds(50, 110, 300, 30);
        panel.add(new_account_box);
        new_account_box.setBounds(50, 150, 300, 30);
        panel.add(new_password_box);
        new_password_box.setBounds(50, 190, 320, 30);
        panel.add(confirm_button);
        confirm_button.setBounds(150, 250, 100, 30);
        panels.add("注册", panel);
        card.show(panels, "登录");
    }
}
