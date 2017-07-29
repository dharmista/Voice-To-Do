package dharmista.batman.com.voicetodo.interfaceH.listOfTodos;

import java.util.List;

import dharmista.batman.com.voicetodo.database.entity.VoiceData;
import dharmista.batman.com.voicetodo.util.DateUtil;

import static dharmista.batman.com.voicetodo.util.Constants.DUMMY;

/**
 * Created by Dharmista on 7/24/2017.
 */

public class BodyProvider {


    private static final String LIGHT_GREEN = "#6aff6a";

    private static final String LIGHT_RED = "#ff3c3c";

    private static final String CSS = "<style type=\"text/css\">\n" +
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
            "        -webkit-tap-highlight-color: transparent;" +
            "        word-wrap:break-word;\n" +
            "    }\n" +
            "    .item {\n" +
            "        color: white;\n" +
            "        padding: 10px;\n" +
            "        line-height: 30px;\n" +
            "        border-top: 1px solid rgb(226, 73, 222);\n" +
            "    }\n" +
            "    .status {\n" +
            "        float: right;\n" +
            "        height: 60px;\n" +
            "        width: 60px;\n" +
            "        margin-top: 0px;\n" +
            "    }.loader {\n" +
            "         display : none;    position: fixed;\n" +
            "         z-index: 10000;\n" +
            "         top: 0;\n" +
            "         right: 0;\n" +
            "         bottom: 0;\n" +
            "         left: 0;\n" +
            "     }.blank {\n" +
            "          opacity: 0.8;\n" +
            "          top: 0;\n" +
            "          right: 0;\n" +
            "          bottom: 0;\n" +
            "          left: 0;\n" +
            "          background-color: black;\n" +
            "          position: absolute;\n" +
            "          z-index: -1;\n" +
            "      }.alert {\n" +
            "           padding: 10px;\n" +
            "           line-height: 34px;\n" +
            "           box-shadow: 0 0 6px black;\n" +
            "           left: 14%;\n" +
            "           right: 14%;\n" +
            "           display: none;\n" +
            "           position: fixed;\n" +
            "           background-color: white;\n" +
            "           border-radius: 3px;\n" +
            "       }.id{display:none;}\n" +
            "    .nextO {\n" +
            "        float: right;\n" +
            "        color: #9C27B0;\n" +
            "        font-weight: 700;\n" +
            "        margin-right: 9px;\n" +
            "    }\n" +
            "</style>";

    private static final String SCRIPT = "<script>    let clickedRed = true, lastAccessBox = null;\n" +
            "function showalert(ins) {        if(clickedRed) {lastAccessBox = ins; if(ins.children[6].innerText == '1'){document.getElementById('snoozer').style.display='none';document.getElementById('player').style.marginTop='9px';document.getElementById('deleter').style.marginTop='-17px';}else{document.getElementById('snoozer').style.display='block';document.getElementById('player').style.marginTop='0px';document.getElementById('deleter').style.marginTop='0px';}\n" +
            "    let name = ins.children[3].innerText, message = ins.children[4].innerText, date = ins.children[2].innerText, id = ins.children[0].innerText;\n" +
            "    Android.handlePopup();        document.getElementById(\"recordId\").innerHTML=ins.children[0].innerText; name = name.replace('<','&lt;').replace('>','&gt;');\n" +
            "    document.getElementById(\"message\").innerHTML=\"Name : <b>\"+name+\"</b><br>Message : <b>\"+message.replace('<','&lt;').replace('>','&rt;')+\"</b><br>Date : <b><span id='dateInPopup'>\"+date.replace('<','&lt;').replace('>','&rt;')+\"</span></b>\";\n" +
            "    a = document.getElementById(\"alert\");\n" +
            "    document.getElementById(\"voiceFile\").innerHTML = ins.children[5].innerHTML;\n" +
            "    a.style.display = \"block\";\n" +
            "    document.getElementsByClassName(\"loader\")[0].style.display=\"block\";\n" +
            "    document.getElementsByClassName(\"blank\")[0].style.display=\"block\";\n" +
            "    a=document.getElementById(\"alert\");\n" +
            "    a.style.marginTop = ((window.outerHeight/2)-(a.clientHeight/2)) + \"px\";\n" +
            "} else {clickedRed = true;}    }\n" +
            "function hideTheAlert(){\n" +
            "    Android.stopPlaying();\n" +
            "    a = document.getElementById(\"alert\");a.style.display = \"none\";\n" +
            "    document.getElementsByClassName(\"blank\")[0].style.display=\"none\";\n" +
            "    document.getElementsByClassName(\"loader\")[0].style.display=\"none\";\n" +
            "}\n" +
            "function changeStatus(ins) {        \n" +
            "    Android.updateStatus(ins.parentElement.children[0].innerText);\n" +
            "    clickedRed = false;let tempo = ins.parentElement.children[6].innerText;" +
            "    ins.parentElement.children[6].innerText = (parseInt(tempo)+1)%2;" +
            "    if(tempo == '0'){\n" +
            "    ins.style.background = \"#6aff6a\";} else {ins.style.background = \"#ff3c3c\";}\n" +
            "    return false;\n" +
            "}\nfunction playVoice(ins) {\n" +
            "    Android.playMediaVoiceFile(ins.parentElement.children[2].innerText);\n" +
            "}\n" +
            "function deleteItem(ins){" +
            "     Android.deleteItem(ins.parentElement.children[1].innerText);\n" +
            "     lastAccessBox.style.display='none';hideTheAlert();Android.handlePopup();" +
            "}function snooze(ins){let snoozedTime = Android.snoozeTheInstance(ins.parentElement.children[1].innerText);} function changeSnooze(snoozedTime){ document.getElementById(\"dateInPopup\").innerHTML\n" +
            " = snoozedTime; lastAccessBox.children[2].innerText = snoozedTime;}</script>";

    public String getBody(List<VoiceData> voiceDatas) {
        String itemsCode = "";
        for(VoiceData voiceData : voiceDatas) {
            if(!voiceData.getId().equals(DUMMY))
                itemsCode += generateItem(voiceData);
        }
        String body = "<!DOCTYPE html><html><head><meta charset=\"utf-8\" name=\"viewport\" content=\"width=device-width\">"+
                CSS + "<body>" + getNotDonePopup() + "<div class=\"list\">" + itemsCode + "</div></body>" + SCRIPT + "</html>";
        return body;
    }

    private String generateItem(VoiceData voiceData) {
        String name = voiceData.getName(), id = voiceData.getId();
        if (name == null || name.equals("")) {
            name = "&lt;No Name&gt;";
        }
        return "<div class=\"item\" onclick=\"showalert(this);\">\n" +
                "        <div class='id'>"+id+"</div>" +
                "        <div class=\"status\" onclick=\"changeStatus(this);\" style='background-color:"+(voiceData.getIsTaskCompleted()==0?LIGHT_RED:LIGHT_GREEN)+"'></div>\n" +
                "        <div class=\"date\">"+new DateUtil(voiceData.getYear(),voiceData.getMonth(),voiceData.getDate(),voiceData.getHours(),voiceData.getMinutes()).getFormattedDate()+"</div>\n" +
                "        <div class=\"name\" style='display:none'>"+name+"</div>\n" +
                "        <div class=\"message\">"+voiceData.getMessage()+"</div>\n" +
                "        <div class=\"voice\" style=\"display: none;\">"+voiceData.getFileName()+"</div>" +
                "        <div style='display:none'>"+voiceData.getIsTaskCompleted()+"</div>" +
                "    </div>";
    }

    private String getNotDonePopup(){
        return "<div class=\"loader\"><div class=\"blank\" onclick=\"hideTheAlert(); Android.handlePopup();\"></div>\n" +
                "    <div class=\"alert\" id=\"alert\">\n" +
                "        <div class=\"message\" id=\"message\"></div>\n" +
                "        <div class=\"message_id\" id=\"recordId\" style=\"display: none;\"></div>\n" +
                "        <div class=\"message_id\" id=\"voiceFile\" style=\"display: none;\"></div>\n" +
                "        <div class=\"nextO\" id='player' onclick=\"playVoice(this)\">Play recording</div><br>\n" +
                "        <div class=\"nextO\" id='snoozer' onclick=\"snooze(this);\">Snooze</div><br>\n" +
                "        <div class=\"nextO\" id='deleter' onclick=\"deleteItem(this);\">Delete this</div><br>\n" +
                "    </div>\n" +
                "</div>";
    }
}
