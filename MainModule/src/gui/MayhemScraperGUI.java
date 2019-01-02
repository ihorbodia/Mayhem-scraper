package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MayhemScraperGUI extends JFrame {
    private JPanel mainPanel;
    private JButton runButton;

    public MayhemScraperGUI() {
        this.setContentPane(mainPanel);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        mainPanel.setVisible(true);
    }
}
