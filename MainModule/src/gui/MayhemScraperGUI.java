package gui;

import logic.MayhemScraperLogic;

import javax.swing.*;

public class MayhemScraperGUI extends JFrame {
    private JPanel mainPanel;
    private JButton runButton;
    private JLabel labelStatus;
    private JLabel labelStatusData;
    private JButton stopButton;

    public MayhemScraperGUI() {
        stopButton.setEnabled(false);
        this.setContentPane(mainPanel);
        MayhemScraperLogic logicObj = new MayhemScraperLogic(labelStatusData, runButton, stopButton);
        logicObj.restoreProperties();
        runButton.addActionListener(e -> {
            logicObj.Run();
        });
        stopButton.addActionListener(e -> {
            logicObj.Stop();
        });
        mainPanel.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
