package WeChat;

import javax.swing.*;
import java.awt.*;

public class Friend_Window extends JFrame {
    ChatBox_Panel chatBox_panel;
    public JPanel panel;
    public CardLayout card;
    public void setChatBox_panel(ChatBox_Panel chatBox_panel) {
        this.chatBox_panel = chatBox_panel;
    }
    public Friend_Window() {
        panel = new JPanel();
        card = new CardLayout();
        panel.setLayout(card);
        add(panel);
        setBounds(300, 100, 745, 620);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }
}
