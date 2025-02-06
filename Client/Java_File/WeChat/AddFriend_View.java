package WeChat;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.LinkedList;

public class AddFriend_View extends JSplitPane {
    public JSplitPane splitPane_function_add;
    public Function_Panel panel_function;
    public panel_add panel;
    public JTextField field;
    public LinkedList<Mouse_Police> mouse_police_list;
    public JButton button_cancel;
    public Mouse_Police mouse_police;
    static class panel_add extends JPanel {
        public Box box;
        public JLabel label;
        public int input_count;

        public panel_add() {
            input_count = 0;
            box = Box.createHorizontalBox();
            JButton find_button = new JButton();
            find_button.setContentAreaFilled(false);
            find_button.setBorder(null);
            find_button.setBorderPainted(false);
            String path = "image/function/";
            SetImageIcon.SetIcon(path, find_button, 40, 40, "搜索");
            label = new JLabel();
            label.setFont(new Font("宋体", Font.BOLD, 13));
            JButton come_button = new JButton();
            come_button.setContentAreaFilled(false);
            come_button.setBorder(null);
            come_button.setBorderPainted(false);
            SetImageIcon.SetIcon(path, come_button, 20, 40, "come");
            box.add(find_button);
            box.add(Box.createHorizontalStrut(10));
            box.add(label);
            box.add(Box.createHorizontalGlue());
            box.add(come_button);
        }
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g_2d = (Graphics2D)g;
            g_2d.setColor(Color.GRAY);
            BasicStroke bs = new BasicStroke
                    (2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
            g_2d.setStroke(bs);
            //在对应位置绘制一条直线
            Line2D line = new Line2D.Double(0, 40, 310, 40);
            g_2d.draw(line);
        }
    }
    public AddFriend_View() {
        mouse_police_list = new LinkedList<>();
        panel_function = new Function_Panel();
        panel = new panel_add();
        panel.setLayout(null);
        mouse_police = new Mouse_Police();
        panel.box.addMouseListener(mouse_police);
        mouse_police.setAddFriend_view(this);
        JButton user = new JButton();
        user.setEnabled(false);
        user.setBorderPainted(false);
        user.setContentAreaFilled(false);
        String path = "image/function/";
        SetImageIcon.SetIcon(path, user, 30, 30, "用户");
        field = new JTextField();
        field.setName("添加好友");
        field.setFont(new Font("宋体", Font.BOLD, 13));
        Document_Police document_police = new Document_Police();
        field.getDocument().addDocumentListener(document_police);
        document_police.setAddFriend_view(this);
        button_cancel = new JButton("取消");
        button_cancel.setName("addFriend_view:取消");
        Mouse_Police mouse_police = new Mouse_Police();
        mouse_police.setButton(button_cancel);
        mouse_police.setAddFriend_view(this);
        mouse_police_list.add(mouse_police);
        button_cancel.addMouseListener(mouse_police);
        button_cancel.setBackground(Color.WHITE);
        panel.add(user);
        user.setBounds(5, 5, 30, 30);
        panel.add(field);
        field.setBounds(35, 5, 200, 30);
        panel.add(button_cancel);
        button_cancel.setBounds(245, 5, 60, 30);
        splitPane_function_add = new JSplitPane
                (JSplitPane.HORIZONTAL_SPLIT, panel_function, panel);
        splitPane_function_add.setDividerSize(1);
        splitPane_function_add.setSize(400, 600);
        splitPane_function_add.setEnabled(false);
        splitPane_function_add.setDividerLocation(0.15);
        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setLeftComponent(splitPane_function_add);
        JPanel panel_free = new JPanel();
        panel_free.setBackground(Color.WHITE);
        panel_free.setLayout(null);
        setRightComponent(panel_free);
        setDividerSize(0);
        setEnabled(false);
        setSize(1200, 600);
        setDividerLocation(0.31);
    }
}
