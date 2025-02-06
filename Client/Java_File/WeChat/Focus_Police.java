package WeChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Pattern;

public class Focus_Police implements FocusListener {
    public Login view;
    public JTextField field;
    public AddFriend_View.panel_add panel;
    public final Pattern pattern_lower = Pattern.compile("\\p{Lower}+");
    public final Pattern pattern_upper = Pattern.compile("\\p{Upper}+");
    public final Pattern pattern_alpha = Pattern.compile("\\p{Alpha}+");
    public final Pattern pattern_digit = Pattern.compile("\\d+");
    public void setView(Login view) {
        this.view = view;
    }
    public void setPanel(AddFriend_View.panel_add panel) {
        this.panel = panel;
    }
    public void focusGained(FocusEvent event) {
        if (view != null) {
            if (event.getSource() == view.account_field) {
                if (view.account_field.getText().equals("请输入账号")) {
                    view.account_field.setText(null);
                }
            } else if (event.getSource() == view.password_field) {
                if (new String(view.password_field.getPassword()).equals("请输入密码")) {
                    view.password_field.setText(null);
                    view.password_field_text = "";
                    view.password_field.setFont(new Font("幼圆", Font.BOLD, 18));
                    view.password_field.setEchoChar('*');
                }
            } else if (event.getSource() == view.nickName_field) {
                if (view.nickName_field.getText().equals("昵称（1~12位字符）")) {
                    view.nickName_field.setText(null);
                }
            } else if (event.getSource() == view.new_account_field) {
                if (view.new_account_field.getText().equals
                        ("账号为8~20位的大、小写字母与数字组合")) {
                    view.new_account_field.setText(null);
                }
            } else if (event.getSource() == view.new_password_field) {
                if (new String(view.new_password_field.getPassword()).equals
                        ("密码格式为8~20位的字母与数字组合")) {
                    view.new_password_field.setText(null);
                    view.new_password_field.setFont(
                            new Font("幼圆", Font.BOLD, 18));
                    view.new_password_field.setEchoChar('*');
                }
            } else if (event.getSource() == view.eye_register) {
                view.new_password_field.requestFocus();
            }
        } else {
            if (event.getSource() == field) {
                if (field.getName().equals("添加好友")) {
                    if (field.getText() == null)
                        panel.input_count = 0;
                }
            }
        }
    }
    public void focusLost(FocusEvent event) {
        if (view != null) {
            if (event.getSource() == view.account_field) {
                if (view.account_field.getText().equals("")) {
                    view.account_field.setFont(
                            new Font("宋体", Font.BOLD, 14));
                    view.account_field.setText("请输入账号");
                }
            } else if (event.getSource() == view.password_field) {
                if (new String(view.password_field.getPassword()).equals("")) {
                    view.password_field.setText("请输入密码");
                    view.password_field_text = "请输入密码";
                    view.password_field.setFont(
                            new Font("宋体", Font.BOLD, 14));
                    view.password_field.setEchoChar((char) 0);
                }
            } else if (event.getSource() == view.nickName_field) {
                if (view.nickName_field.getText().equals("")) {
                    view.nickName_field.setText("昵称（1~12位字符）");
                } else {
                    String str = view.nickName_field.getText();
                    if (str.matches(".+")) {
                        for (User_message user_message : User.user_message_list) {
                            if (!str.equals(user_message.user_nickName))
                                view.isFormat_nickName = true;
                            else {
                                JOptionPane.showMessageDialog(view,
                                        "抱歉，该昵称已被使用请更换昵称",
                                        "昵称被使用", JOptionPane.WARNING_MESSAGE);
                                view.nickName_field.requestFocus();
                            }
                        }
                    }
                }
            } else if (event.getSource() == view.new_account_field) {
                if (view.new_account_field.getText().equals("")) {
                    view.new_account_field.setText(
                            "账号为8~20位的大、小写字母与数字组合");
                } else {
                    String str = view.new_account_field.getText();
                    view.isFormat_account = false;
                    if (str.matches(".{8,}")) {
                        if (pattern_lower.matcher(str).find()) {
                            if (pattern_upper.matcher(str).find()) {
                                if (pattern_digit.matcher(str).find()) {
                                    for (User_message user_message : User.user_message_list) {
                                        if (! str.equals(user_message.user_account))
                                            view.isFormat_account = true;
                                        else {
                                            JOptionPane.showMessageDialog(
                                                    view, "该账号已被注册",
                                                    "账号已被注册", JOptionPane.WARNING_MESSAGE);
                                            view.new_account_field.requestFocus();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!view.isFormat_account) {
                        JOptionPane.showMessageDialog(
                                view, "请输入正确格式的账号",
                                "账号格式错误", JOptionPane.WARNING_MESSAGE);
                        view.new_account_field.requestFocus();
                    }
                }
            } else if (event.getSource() == view.new_password_field) {
                if (new String(view.new_password_field.getPassword()).equals("")) {
                    view.new_password_field.setEchoChar((char) 0);
                    Font font = new Font("宋体", Font.BOLD, 13);
                    view.new_password_field.setFont(font);
                    view.new_password_field.setText(
                            "密码格式为8~20位的字母与数字组合");
                } else {
                    String str = new String(view.new_password_field.getPassword());
                    view.isFormat_password = false;
                    if (str.matches(".{8,}")) {
                        if (pattern_alpha.matcher(str).find()) {
                            if (pattern_digit.matcher(str).find()) {
                                view.isFormat_password = true;
                            }
                        }
                    }
                    if (!view.isFormat_password) {
                        JOptionPane.showMessageDialog(
                                view, "请输入正确格式的密码",
                                "密码格式错误", JOptionPane.WARNING_MESSAGE);
                        view.new_password_field.requestFocus();
                    }
                }
            }
        }
    }
}
