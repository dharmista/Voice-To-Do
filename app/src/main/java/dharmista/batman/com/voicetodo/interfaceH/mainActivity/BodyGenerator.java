package dharmista.batman.com.voicetodo.interfaceH.mainActivity;

/**
 * Created by Dharmista on 7/20/2017.
 */

public class BodyGenerator {
    private String META_MOBILE_FIT = "<meta charset=\"utf-8\" name=\"viewport\" content=\"width=device-width\">";

    public String getHtmlBody() {
        String htmlData = "<!DOCTYPE html><html><head>"
                + META_MOBILE_FIT
                + new CssProvider().getCss()
                + "</head>";

        htmlData += "<body>";
        htmlData += getBody();
        htmlData += "</body>";
        htmlData += new ScriptProvider().getFrontScript();
        htmlData += "</html>";

        return htmlData;
    }

    private String getBody() {
        return "<div class=\"recorder\">\n" +
                "    <div class=\"wrapperR\">\n" +
                "        <center><div class=\"stopwatch\"></div></center>\n" +
                "        <div class=\"record\">Press the button to record the voice.</div>\n" +
                "    </div>\n" +
                "    <div class=\"recordWrapper\"><center><div class=\"play_button\" id=\"recordButton\">" +
                "<img class='icon' src='mic.png'/>" +
                "</div></center></div>\n" +
                "    <div class=\"pause_button\" id=\"reset_button\"><img class='icon' src='reset.png'/></div>\n" +
                "</div>\n" +
                "<center><div class=\"timeAndDate\" id=\"timeAndDate\">Add to list</div></center>";
    }
}
