public class MainWindow {
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //createAndShowGUI();
                Frame f = new Frame();
                f.add(new Drawing());

                f.setSize(300, 200);
                f.setVisible(true);
            }
        });
    }
}
