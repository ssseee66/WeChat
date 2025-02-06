package WeChat;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;

public class Document_Police implements DocumentListener {
    public AddFriend_View addFriend_view;
    public void setAddFriend_view(AddFriend_View addFriend_view) {
        this.addFriend_view = addFriend_view;
    }
    public void changedUpdate(DocumentEvent event) {
        if (addFriend_view != null) {
            if (event.getDocument() == addFriend_view.field.getDocument()) {
                Document document = event.getDocument();
                String text = null;
                try {
                    text = document.getText(0, document.getLength());
                } catch (BadLocationException ignored) {
                }
                if (text != null) {
                    if (text.length() <= 12)
                        text = "搜索：" + text;
                    else
                        text = "搜索：" + text.substring(0, 12) + "...";
                }
                if (addFriend_view.field.getText().equals("")) {
                    addFriend_view.panel.remove(addFriend_view.panel.box);
                    for (Component com : addFriend_view.panel.getComponents()) {
                        if (com instanceof User_Panel) {
                            addFriend_view.panel.remove(com);
                        }
                    }
                    addFriend_view.panel.input_count = -1;
                    addFriend_view.panel.updateUI();
                }
                if (addFriend_view.panel.input_count == 0) {
                    addFriend_view.panel.label.setText(text);
                    addFriend_view.panel.add(addFriend_view.panel.box);
                    addFriend_view.panel.box.setBounds(0, 45, 310, 40);
                    addFriend_view.panel.updateUI();
                } else {
                    addFriend_view.panel.label.setText(text);
                    addFriend_view.panel.updateUI();
                }
                addFriend_view.panel.input_count++;
            }
        }
    }
    public void removeUpdate(DocumentEvent event) {
        changedUpdate(event);
    }
    public void insertUpdate(DocumentEvent event) {
        changedUpdate(event);
    }
}
