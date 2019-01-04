package gui;

import logic.MayhemScraperLogic;

import javax.swing.*;

public class MayhemScraperGUI extends JFrame {
    private JPanel mainPanel;
    private JButton runButton;
    private JLabel labelStatus;
    private JLabel labelStatusValue;

    public MayhemScraperGUI() {
        this.setContentPane(mainPanel);
        MayhemScraperLogic logicObj = new MayhemScraperLogic();
        runButton.addActionListener(e -> {
            System.out.println("Button clicked");
            logicObj.Run();
        });
        mainPanel.setVisible(true);
    }
}
