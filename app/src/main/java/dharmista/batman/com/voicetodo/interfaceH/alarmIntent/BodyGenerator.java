package dharmista.batman.com.voicetodo.interfaceH.alarmIntent;

import java.util.Date;

/**
 * Created by Dharmista on 7/23/2017.
 */

public class BodyGenerator {

    private final String CSS = "<style type=\"text/css\">\n" +
            "    body {\n" +
            "        font-family: helvetica;\n" +
            "        background: black;\n" +
            "    }\n" +
            "    html {\n" +
            "        -webkit-touch-callout: inherit;\n" +
            "        -webkit-user-select: none;\n" +
            "        -khtml-user-select: none;\n" +
            "        -moz-user-select: none;\n" +
            "        -ms-user-select: none;\n" +
            "        user-select: none;\n" +
            "        outline: none;\n" +
            "        -webkit-tap-highlight-color: transparent;\n" +
            "    }\n" +
            "    .decorator{\n" +
            "        background: black;\n" +
            "        color: white;\n" +
            "        display: inline-block;\n" +
            "        margin: 13px;\n" +
            "        width: 40px;\n" +
            "        height: 40px;\n" +
            "        text-align: center;\n" +
            "        border-radius: 40px;\n" +
            "        line-height: 40px;\n" +
            "        font-size: 22px;\n" +
            "        border: 2px solid white;\n" +
            "        cursor: pointer;\n" +
            "        margin-top: 56px;\n" +
            "        margin-bottom: 23px;\n" +
            "        box-shadow: 0 0 20px 5px rgb(226, 73, 222);\n" +
            "    }\n" +
            "    .time{\n" +
            "        font-size: 63px;\n" +
            "        font-weight: 100;\n" +
            "        color: white;\n" +
            "        text-align: center;;\n" +
            "    }\n" +
            "    .message {\n" +
            "        color: white;\n" +
            "        padding: 10px;\n" +
            "        line-height: 11px;\n" +
            "        height: 200px;\n" +
            "        max-height: 200px;\n" +
            "    }\n" +
            "    .status {\n" +
            "        color: black;\n" +
            "        background: rgb(226, 73, 222);\n" +
            "        padding: 10px;\n" +
            "        width: fit-content;\n" +
            "        margin: 9px;\n" +
            "        border-radius: 4px;\n" +
            "        border: 2px double white;\n" +
            "    }\n" +
            "    </style>";

    private final String SCRIPT = "<script>\n" +
            "    colorCodeSpreadAmount = 5;\n" +
            "    const a = function ripple() {\n" +
            "        if (colorCodeSpreadAmount >= 15) {\n" +
            "            colorCodeSpreadAmount = 5;\n" +
            "        }\n" +
            "        let div = document.getElementById(\"decorator\").style;\n" +
            "        div.boxShadow = \"0 0 20px \"+colorCodeSpreadAmount+\"px rgb(226, 73, 222)\";\n" +
            "        colorCodeSpreadAmount += 5;\n" +
            "    };\n" +
            "    setInterval(a, 100);" +
            "    document.getElementById(\"taskCompleted\").onclick = function () {\n" +
            "        Android.setTaskCompleted(document.getElementById(\"id\").innerText);\n" +
            "    };\n" +
            "</script>";

    public String getBody(String message, String id) {
        Date date = new Date();
        String body = "<!DOCTYPE html><html><head><meta charset=\"utf-8\" name=\"viewport\" content=\"width=device-width\">"
                + CSS + "<body><div id='id' style='display:none;'>"+id+"</div>\n" +
                "    <center><div class=\"decorator\" id=\"decorator\"></div></center>\n" +
                "    <div class=\"time\">"+pad(date.getHours() + "")+":"+pad(date.getMinutes() + "")+"</div>\n" +
                "<div class=\"message\">\""+message+"\"</div>\n" +
                "    <center>\n" +
                "<div class=\"status\" id=\"taskCompleted\">Task Completed</div>\n" +
                "<div class=\"status\" id=\"snoozeTo\" onclick='javascript:Android.snoozer();'>Snooze to</div>\n" +
                "    </center>\n" +
                "</body>" + SCRIPT + "</html>";
        return body;
    }

    private String pad(String number) {
        return (number.length() == 1 ? "0" : "") + number;
    }
}
