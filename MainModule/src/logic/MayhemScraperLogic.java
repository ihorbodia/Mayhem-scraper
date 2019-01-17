package logic;

import logic.Models.ScrapedModelItem;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MayhemScraperLogic {

    private static List<ScrapedModelItem> resultItems;
    private final int maximumIndex = 9999999;
    private int currentIndex = 1;
    File propertiesFile;
    ExecutorService executorService;
    public Future<?> future;
    Boolean isWorked;
    JLabel labelStatusData;
    JButton runBtn;
    JButton stopBtn;
    Boolean stopedWork = false;

    public MayhemScraperLogic(JLabel labelStatusData, JButton runBtn, JButton stopButton) {
        this.labelStatusData = labelStatusData;
        this.runBtn = runBtn;
        this.stopBtn = stopButton;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveProperties();
            saveDataToFile();
        }));
    }

    public Properties properties = new Properties();

    public void Stop() {
        stopedWork = true;
    }

    private void setDefaultValues() {
        currentIndex = 1;
        isWorked = false;
        stopedWork = false;
    }

    public void Run() {
        resultItems = new ArrayList();
        executorService = Executors.newSingleThreadExecutor();
        future = executorService.submit(() -> {
            createNewOutputFile();
            while (currentIndex <= maximumIndex && !stopedWork) {
                isWorked = true;
                String URL = prepareURL();
                Element body = scrapeCurrentPage(URL);
                resultItems.add(new ScrapedModelItem(body, currentIndex));
                updateMultipleSearchGUI();
                saveProperties();
                currentIndex++;
                if (resultItems.size() > 10000){
                    saveDataToFile();
                }
            }
        });

        Thread thread = new Thread(() -> {
            runBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            while (true) {
                if (future.isDone()) {
                    labelStatusData.setText((currentIndex - 1) + "/" + maximumIndex + " accounts processed. Finished.");
                    break;
                }
            }
            setDefaultValues();
            saveProperties();
            saveDataToFile();
            runBtn.setEnabled(true);
            stopBtn.setEnabled(false);
        });
        thread.start();
    }

    public void updateMultipleSearchGUI() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> labelStatusData.setText((currentIndex - 1) + "/" + maximumIndex + " accounts processed. "));
            return;
        }
    }

    public void createNewOutputFile() {
        try {
            File path = new File(StringUtils.removeEnd(new File(".").getAbsolutePath(), "."));
            String outputPath = path.getAbsolutePath() + File.separator + "Mayhem models results.csv";
            StringBuilder sb = new StringBuilder();
            sb.append("\"ModelName\"").append(',').append("\"Location\"").append(',').append("\"WebsiteURL\"").append(',').append("\"MMId\"").append('\n');
            Files.write(Paths.get(outputPath), sb.toString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void saveDataToFile() {
        if (resultItems == null) {
            return;
        }
        try {
            File path = new File(StringUtils.removeEnd(new File(".").getAbsolutePath(), "."));
            String outputPath = path.getAbsolutePath() + File.separator + "Mayhem models results.csv";
            StringBuilder sb = new StringBuilder();
            for (ScrapedModelItem item : resultItems) {
                if (item.isObjectCorrect) {
                    sb.append("\"")
                            .append(item.ModelName)
                            .append("\"")
                            .append(',')
                            .append("\"")
                            .append(item.Location)
                            .append("\"")
                            .append(',')
                            .append("\"")
                            .append(item.WebsiteURL)
                            .append("\"")
                            .append(',')
                            .append("\"")
                            .append(item.Id)
                            .append("\"")
                            .append('\n');
                }
            }
            Files.createDirectories(Paths.get(path.getParent()));
            Files.write(Paths.get(outputPath), sb.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultItems.clear();
    }

    private String prepareURL() {
        return "https://www.modelmayhem.com/" + currentIndex;
    }

    private Element scrapeCurrentPage(String currentURL) {
        Element doc = null;
        try {
            doc = Jsoup.connect(currentURL).userAgent("Mozilla/5.0").get().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public void saveProperties() {
        OutputStream output = null;
        try {
            output = new FileOutputStream(propertiesFile.getAbsoluteFile());
            properties.setProperty("index", String.valueOf(currentIndex));
            properties.setProperty("isWorked", String.valueOf(isWorked));
            properties.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println(io.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }


    private void createNewFile() {
        OutputStream output = null;
        try {
            File propertiesFileTemp = File.createTempFile("configMS", ".properties");
            String propPath = propertiesFileTemp.getAbsolutePath().substring(0, propertiesFileTemp.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + "configMS.properties";
            File f = new File(propPath);
            if (f.exists() && !f.isDirectory()) {
                propertiesFile = f;
            } else {
                propertiesFile = f;
                f.createNewFile();
                output = new FileOutputStream(propertiesFile.getAbsoluteFile());
                properties.setProperty("index", "1");
                properties.setProperty("isWorked", "false");
                properties.store(output, null);
            }
            propertiesFileTemp.delete();
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println(io.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void restoreProperties() {
        InputStream input = null;
        try {
            createNewFile();
            input = new FileInputStream(propertiesFile.getAbsoluteFile());
            properties.load(input);

            if (properties.get("index") != null) {
                String currentIndexStr = properties.get("index").toString();
                currentIndex = Integer.parseInt(currentIndexStr);
            }

            if (properties.get("isWorked") != null) {
                String isWorkStr = properties.get("isWorked").toString();
                isWorked = Boolean.parseBoolean(isWorkStr);
            }

            if (isWorked) {
                Run();
            }
            else {
                labelStatusData.setText("press Run to start");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
