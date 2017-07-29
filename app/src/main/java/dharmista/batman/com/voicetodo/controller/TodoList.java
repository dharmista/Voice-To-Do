package dharmista.batman.com.voicetodo.controller;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dharmista.batman.com.voicetodo.R;
import dharmista.batman.com.voicetodo.database.entity.VoiceData;
import dharmista.batman.com.voicetodo.database.handler.DBHandler;
import dharmista.batman.com.voicetodo.interfaceH.listOfTodos.BodyProvider;
import dharmista.batman.com.voicetodo.interfaceH.mainActivity.BodyGenerator;
import dharmista.batman.com.voicetodo.util.AndroidUtils;
import dharmista.batman.com.voicetodo.util.DateUtil;
import dharmista.batman.com.voicetodo.voice.AlarmHandlers;

import static dharmista.batman.com.voicetodo.util.Constants.DIRECTORY;

public class TodoList extends AppCompatActivity implements DateTime<String> {

    private WebView webView;
    private AndroidUtils androidUtils;
    private boolean popupOpen = false;
    MediaPlayer mediaPlayer;
    private DBHandler dbHandler;
    TodoList todoList = this;
    Boolean isDateSetForSnooze = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        initHandlers();
        dbHandler = new DBHandler(this);
        List<VoiceData> voices;

        if(this.getIntent().getExtras().getString("show").equals("pending"))
            voices = dbHandler.getAllPendingTodos();
        else
            voices = dbHandler.getAllAlarms();

        androidUtils.setUpWebView(webView, new BodyProvider().getBody(voices));
        webView.addJavascriptInterface(this, "Android");
    }

    private void initHandlers() {
        webView = (WebView)findViewById(R.id.webViewList);
        androidUtils = new AndroidUtils();
    }

    @JavascriptInterface
    public void handlePopup() {
        if (popupOpen) {
            if(mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
        }
        popupOpen = !popupOpen;
    }

    int[] dates = new int[5];
    String settedTime = "", idToSnooze = "";

    @JavascriptInterface
    public String snoozeTheInstance(String id){
        idToSnooze = id;
        androidUtils.askDate(this, dates, 0, todoList);
        return settedTime;
    }


    @JavascriptInterface
    public void updateStatus(String id) {
        new DBHandler(this).setTaskStatus(id);
    }

    @JavascriptInterface
    public void playMediaVoiceFile(String filename) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filename);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    @JavascriptInterface
    public void stopPlaying(){
        if(mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
    }

    @JavascriptInterface
    public void deleteItem(String id) {
        String filename = dbHandler.deleteItem(id);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY +
                filename;
        Boolean isDeleted = new File(path).delete();
    }

    @Override
    public void onBackPressed() {
        isDateSetForSnooze = false;
        if(popupOpen) {
            webView.loadUrl("javascript:hideTheAlert();");
            popupOpen = false;
            if(mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public String afterSet(String id) {
        if(!isDateSetForSnooze) {
            androidUtils.askTime(this, dates, 3, todoList);
            isDateSetForSnooze = true;
        }
        else {
            settedTime = new DateUtil(dates[0]+"",dates[1]+"",dates[2]+"",dates[3]+"",dates[4]+"").getFormattedDate();
            VoiceData updatedVoiceData = dbHandler.snoozeTodo(idToSnooze, dates);
            new AlarmHandlers()
                    .setAlarmTime(dates)
                    .updateAlarm(TodoList.this, updatedVoiceData);
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:changeSnooze('"+settedTime+"');");
                }
            });
            isDateSetForSnooze = false;
        }
        return null;
    }
}
