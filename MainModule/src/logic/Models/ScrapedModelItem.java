package logic.Models;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrapedModelItem {
    public final int Id;
    public String ModelName;
    public String Location;
    public String WebsiteURL;
    public boolean isObjectCorrect = true;
    public String Description;

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
            String gender = body.select("#user_type").text();
            if (gender.contains("Female")){
                Location = body.select("div.location").text();
                ModelName = body.select("#user_name").text();
                Description = body.select("#about-me-container").text();
                Pattern igPattern = Pattern.compile("(((instagram\\.com\\/)|(ig\\ ?\\-\\ ?))([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?))|(@([a-z0-9_]{1,255}))");
                Pattern fbPattern = Pattern.compile("(?:https?:\\/\\/)?(?:www\\.)?(?:facebook|fb|m\\.facebook)\\.(?:com|me)\\/(?:(?:\\w)*#!\\/)?(?:pages\\/)?(?:[\\w\\-]*\\/)*([\\w\\-\\.]+)(?:\\/)?");
                Matcher igMatcher = igPattern.matcher(Description.toLowerCase());
                Matcher fbMatcher = fbPattern.matcher(Description.toLowerCase());
                if (igMatcher.find()) {
                    WebsiteURL = igMatcher.group(0);
                } else if (fbMatcher.find()) {
                    WebsiteURL = fbMatcher.group(0);
                }
                if (StringUtils.isEmpty(WebsiteURL)) {
                    WebsiteURL = body.select("a.website_url").text();
                }
                return;
            }
            else {
                isObjectCorrect = false;
            }
        }
        isObjectCorrect = false;
    }
}
