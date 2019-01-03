package logic;

import gui.MayhemScraperGUI;

import javax.swing.*;

public class MayhemScraper {

    public static void main(String[] args) {
        MayhemScraperGUI gui = new MayhemScraperGUI();
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        gui.setTitle("Mayhem scraper v 1.0");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setSize(400, 150);
    }
}
