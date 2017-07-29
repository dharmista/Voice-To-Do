package dharmista.batman.com.voicetodo.controller;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Locale;

import dharmista.batman.com.voicetodo.R;
import dharmista.batman.com.voicetodo.database.entity.VoiceData;
import dharmista.batman.com.voicetodo.database.handler.DBHandler;
import dharmista.batman.com.voicetodo.interfaceH.alarmIntent.BodyGenerator;
import dharmista.batman.com.voicetodo.util.AndroidUtils;
import dharmista.batman.com.voicetodo.util.DateUtil;
import dharmista.batman.com.voicetodo.voice.AlarmHandlers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

public class AlarmIntent extends AppCompatActivity implements DateTime {

    private MediaPlayer mediaPlayer;
    WebView webView;
    private String message;
    private TextToSpeech t1;
    private VoiceInterface voiceInterface;
    private VoiceData voiceData;
    Boolean isDateSetForSnooze = false;
    private AndroidUtils androidUtils;
    private AlarmIntent alarmIntent = this;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();

        window.addFlags(FLAG_TURN_SCREEN_ON);
        window.addFlags(FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(FLAG_KEEP_SCREEN_ON);
        window.addFlags(FLAG_DISMISS_KEYGUARD);
        window.addFlags(FLAG_FULLSCREEN);

        setContentView(R.layout.activity_alarm_intent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Voice to-do");
        setSupportActionBar(toolbar);
        androidUtils = new AndroidUtils();
        webView = (WebView)findViewById(R.id.webView2);
        dbHandler = new DBHandler(this);
        voiceData = dbHandler.getVoiceRecordById(getIntent().getExtras().getString("id"));
        if(voiceData.getIsTaskCompleted() == 1)
            finish();
        renderInstance(voiceData);
        playAudioFile(voiceData.getFileName());
    }

    private void renderInstance(VoiceData voiceData) {
        message = voiceData.getMessage();
        webView.getSettings().setAppCacheEnabled(false);
        webView.loadDataWithBaseURL( "file:///android_asset/", new BodyGenerator().getBody(message, voiceData.getId()), "text/html",
                "utf-8", null );
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        webView.setHapticFeedbackEnabled(false);
        webView.addJavascriptInterface(this, "Android");
    }

    private void playAudioFile(String name) {
        voiceInterface = new VoiceInterface(name, true, mediaPlayer, t1);
        voiceInterface.loopOver();
    }

    private void speakMessage(final String speech) {
        t1=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
                t1.speak(speech ,TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    @JavascriptInterface
    public void setTaskCompleted(String id){
        new DBHandler(this).setTaskStatus(id);
        voiceInterface.stopLooping();
        Toast.makeText(this, "Set the task status to completed.",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {}

    @JavascriptInterface
    public void snoozer(){
        if(mediaPlayer != null) {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }
        androidUtils.askDate(this, dates, 0, alarmIntent);
    }

    @Override
    protected void onDestroy() {
        if(t1!= null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }

    int[] dates = new int[5];
    String settedTime = "";

    @Override
    public Object afterSet(String param) {
        if(!isDateSetForSnooze) {
            isDateSetForSnooze = true;
            androidUtils.askTime(this, dates, 3, alarmIntent);
        }
        else {
            settedTime = new DateUtil(dates[0]+"",dates[1]+"",dates[2]+"",dates[3]+"",dates[4]+"").getFormattedDate();
            VoiceData updatedVoiceData = dbHandler.snoozeTodo(voiceData.getId(), dates);
            new AlarmHandlers()
                    .setAlarmTime(dates)
                    .updateAlarm(AlarmIntent.this, updatedVoiceData);
            isDateSetForSnooze = false;
            voiceInterface.stopLooping();
        }
        return null;
    }

    class VoiceInterface{
        private String file;
        private boolean isLooping;
        private MediaPlayer mediaPlayer;
        private TextToSpeech t1;

        VoiceInterface(String file, boolean isLooping, MediaPlayer mediaPlayer, TextToSpeech t1){
            this.file = file;
            this.isLooping = isLooping;
            this.mediaPlayer = mediaPlayer;
            this.t1 = t1;
        }

        void loopOver(){
            if(isLooping) {
                if (file != null) {
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(file);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    speakMessage("You have a recording");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mediaPlayer.start();
                        }
                    }, 1000);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(final MediaPlayer mediaPlayer) {
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    loopOver();
                                }
                            }, 1000);
                        }
                    });
                } else if (message != null) {
                    speakMessage(message);
                }
            }
        }

        void stopLooping(){
            isLooping = false;
            if(mediaPlayer != null) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    if(t1 != null) {
                        if(t1.isSpeaking()) {
                            t1.stop();
                            t1.shutdown();
                        }
                    }
                }
            }
            finish();
        }
    }
}
