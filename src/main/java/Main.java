import javax.swing.*;

public class Main {

    public Main(){
        startGUI();
    }
    private void startGUI(){
        JFrame frame = new JFrame("My First Gui");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        JButton button = new JButton("Press");
        frame.getContentPane().add(button);
        frame.setVisible(true);
    }
}
