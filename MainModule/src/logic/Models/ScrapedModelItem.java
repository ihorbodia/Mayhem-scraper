package logic.Models;

import org.jsoup.nodes.Element;

public class ScrapedModelItem {
    public final int Id;
    private String ModelName;
    private String Location;
    private String WebsiteURL;
    private boolean isObjectCorrect = true;

    public ScrapedModelItem(Element body, int id) {
        Id = id;
        if (body == null) {
            System.out.println("Page is null");
            isObjectCorrect = false;
            return;
        }
        fillModelWithDocument(body);
    }

    private void fillModelWithDocument(Element body) {
        Element errorInfoMessage = body.select("#not-approved-title").first();
        if (errorInfoMessage == null){
            System.out.println("User exists");
            String gender = body.select("#user_type").text();
            if (gender.contains("Female")){
                Location = body.select("div.location").text();
                ModelName = body.select("#user_name").text();
                WebsiteURL = body.select("div.website_url").text();
            }
            else {
                isObjectCorrect = false;
            }
        }
        isObjectCorrect = true;
    }
}
