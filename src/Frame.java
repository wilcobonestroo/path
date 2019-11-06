import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame implements ActionListener {

    //Create a frame with a button.
    public Frame() {
        super("A window");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    //Make the button do the same thing as the default close operation
    //(DISPOSE_ON_CLOSE).
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }
}
