package dharmista.batman.com.voicetodo.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dharmista.batman.com.voicetodo.database.handler.DBHandler;
import dharmista.batman.com.voicetodo.util.Toaster;
import dharmista.batman.com.voicetodo.voice.VoiceFileHandler;

import static dharmista.batman.com.voicetodo.util.Constants.DIRECTORY;

/**
 * Created by Dharmista on 7/29/2017.
 */

public class CacheRemover extends Service{
    private DBHandler dbHandler;
    private VoiceFileHandler voiceFileHandler;

    @Override
    public void onCreate() {
        dbHandler = new DBHandler(this);
        voiceFileHandler = new VoiceFileHandler();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<String> fileNames = dbHandler.getListOfFiles();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY;
        File folder = new File(path);
        List<String> filesInPath = Arrays.asList(folder.list());
        List<String> tempo1 = new ArrayList<>(filesInPath);
        tempo1.removeAll(fileNames);
        voiceFileHandler.deleteFiles(this, tempo1);
        new Toaster().alert(this, "Deleted "+tempo1.size()+" files.");
        return START_STICKY;
    }
}
