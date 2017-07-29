package dharmista.batman.com.voicetodo.controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dharmista.batman.com.voicetodo.R;
import dharmista.batman.com.voicetodo.interfaceH.mainActivity.BodyGenerator;
import dharmista.batman.com.voicetodo.security.PermissionHandler;
import dharmista.batman.com.voicetodo.services.CacheRemover;
import dharmista.batman.com.voicetodo.util.AndroidUtils;
import dharmista.batman.com.voicetodo.util.Toaster;
import dharmista.batman.com.voicetodo.voice.VoiceControllers;
import dharmista.batman.com.voicetodo.voice.VoiceFileHandler;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static dharmista.batman.com.voicetodo.util.Constants.DIRECTORY;
import static dharmista.batman.com.voicetodo.util.Constants.FILENAME;

public class MainActivity extends AppCompatActivity {

    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    MediaPlayer mediaPlayer ;
    VoiceControllers voiceControllers;
    final int[] perfectDateAndTimeInstance = new int[5];
    private RelativeLayout mRelativeLayout;
    private List<String> allTheTrails;
    private String fileName = null;
    WebView webView;private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webView = (WebView)findViewById(R.id.webView);
        voiceControllers = new VoiceControllers();
        mRelativeLayout = (RelativeLayout)findViewById(R.id.content_main);
        allTheTrails = new ArrayList<>();
        voiceControllers.checkExistance(DIRECTORY);

        List<String> p = new PermissionHandler().askPermissions(this.getApplicationContext(), this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO});

        if(p.size() != 0) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    p.toArray(new String[]{}), 1);
        }

        new AndroidUtils().setUpWebView(webView, new BodyGenerator().getHtmlBody());
        webView.addJavascriptInterface(this, "Android");
    }

    private void initiatePopupWindow(Context mContext) {
        Intent intent = new Intent(MainActivity.this, PopupActivity.class);
        intent.putExtra(FILENAME, fileName);
        startActivity(intent);
    }

    @JavascriptInterface
    public void reset_refresh() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadDataWithBaseURL( "file:///android_asset/", new BodyGenerator().getHtmlBody(), "text/html",
                        "utf-8", null );
            }
        });
        if(voiceControllers.getIsRecording())
            mediaRecorder.stop();
        fileName = null;
    }

    @JavascriptInterface
    public void setAlarm(){
        deleteAllTheTrails();
        initiatePopupWindow(this);
    }

    private void deleteAllTheTrails() {
        new VoiceFileHandler().deleteFiles(this, allTheTrails);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_pending) {
            Intent intent = new Intent(this, TodoList.class);
            intent.putExtra("show","pending");
            startActivity(intent);
        }
        else if(id == R.id.action_all) {
            Intent intent = new Intent(this, TodoList.class);
            intent.putExtra("show","all");
            startActivity(intent);
        }
        else if(id == R.id.action_cache) {
            new Toaster().alert(this, "Started removing cache");
            startService(new Intent(this, CacheRemover.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int i = 0;
        for(String permission : permissions) {
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                finish();
                startActivity(getIntent());
            }
            else {
                Toast.makeText(this, "Require all permissions to run", Toast.LENGTH_SHORT).show();
                finish();
            }
            i++;
        }
    }

    @JavascriptInterface
    public void recordVoice() {
        voiceControllers.recordVoice(this);
        fileName = voiceControllers.AudioSavePathInDevice;
        allTheTrails.add(fileName);
    }

    @JavascriptInterface
    public String stopRecordingVoice(){
        voiceControllers.stopRecordingVoice(this);
        return process();
    }

    private String process() {
        long duration = voiceControllers.endTime - voiceControllers.startTime;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long milliseconds = duration - (60000 * minutes + seconds * 1000);

        return String.format("%d|%d|%s",minutes,seconds,(milliseconds+"").substring(0,2));
    }

    @JavascriptInterface
    public void playLastRecorded() {
        voiceControllers.recordLastPlayed(this);
    }

    @JavascriptInterface
    public void stopPlaying() {
        voiceControllers.stopPlaying(voiceControllers.getMediaPlayer());
    }
}
