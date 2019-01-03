package gui;

import javax.swing.*;

public class MayhemScraperGUI extends JFrame {
    private JPanel mainPanel;
    private JButton runButton;
    private JLabel labelStatus;
    private JLabel labelStatusValue;

    public MayhemScraperGUI() {
        this.setContentPane(mainPanel);
        runButton.addActionListener(e -> System.out.println("Button clicked"));
        mainPanel.setVisible(true);
    }
}
