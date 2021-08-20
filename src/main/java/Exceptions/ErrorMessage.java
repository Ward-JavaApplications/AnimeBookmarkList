package Exceptions;

import Managers.MyLogger;

import javax.swing.*;

public class ErrorMessage {
    public ErrorMessage(String msg){

        MyLogger.log(msg);

        JFrame ErrorFrame = new JFrame("Error");
        ErrorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ErrorFrame.setSize(300,200);
        ErrorFrame.setLocationRelativeTo(null);
        ErrorFrame.setVisible(true);
        JLabel errorMsg = new JLabel(msg);
        //errorMsg.setFont(new Font(Font.BOLD));
        ErrorFrame.getContentPane().add(errorMsg);
        SwingUtilities.updateComponentTreeUI(ErrorFrame);
    }

}
