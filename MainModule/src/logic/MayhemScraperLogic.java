package logic;

import logic.Models.ScrapedModelItem;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MayhemScraperLogic {

    private static List<ScrapedModelItem> resultItems;
    //private final int maximumIndex = 9999999;
    private final int maximumIndex = 500;
    private int currentIndex = 1;
    File propertiesFile;
    Boolean IsWorked;

    public Properties properties = new Properties();

    public void Run() {
        resultItems = new ArrayList();
        while (currentIndex < maximumIndex) {
            IsWorked = true;
            String URL = prepareURL();
            Element body = scrapeCurrentPage(URL);
            resultItems.add(new ScrapedModelItem(body, currentIndex));
            saveProperties();
            currentIndex++;
        }
        currentIndex = 1;
        IsWorked = false;
        saveProperties();
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
            properties.setProperty("IsWorked", String.valueOf(IsWorked));
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
            String propPath = FilenameUtils.getFullPathNoEndSeparator(propertiesFileTemp.getAbsolutePath()) + File.separator + "configMS.properties";
            File f = new File(propPath);
            if (f.exists() && !f.isDirectory()) {
                propertiesFile = f;
            } else {
                propertiesFile = f;
                f.createNewFile();
                output = new FileOutputStream(propertiesFile.getAbsoluteFile());
                properties.setProperty("index", "1");
                properties.setProperty("IsWorked", "false");
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

            if (properties.get("IsWorked") != null) {
                String isWorkStr = properties.get("IsWorked").toString();
                IsWorked = Boolean.parseBoolean(isWorkStr);
            }

            if (IsWorked) {
                Run();
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
