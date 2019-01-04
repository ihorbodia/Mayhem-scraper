package logic;

import logic.Models.ScrapedModelItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MayhemScraperLogic {

    private static List<ScrapedModelItem> resultItems;
    //private final int maximumIndex = 9999999;
    private final int maximumIndex = 500;
    private int currentIndex = 1;

    public void Run() {
        resultItems = new ArrayList();
        while (currentIndex < maximumIndex) {
            String URL = prepareURL();
            Element body = scrapeCurrentPage(URL);
            resultItems.add(new ScrapedModelItem(body, currentIndex));
            currentIndex++;
        }
        currentIndex = 1;
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
}
