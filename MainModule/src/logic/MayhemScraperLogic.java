package logic;

public class MayhemScraperLogic {
    private final int maximumIndex = 9999999;
    private int currentIndex = 0;

    public void Run() {
        boolean continueWork = true;
        while (continueWork) {
            String URL = prepareURL();


            currentIndex++;
        }
        currentIndex = 0;
    }

    private String prepareURL() {
        String URL = "";
        URL += "www.modelmayhem.com/" + currentIndex;
        return URL;
    }
}
