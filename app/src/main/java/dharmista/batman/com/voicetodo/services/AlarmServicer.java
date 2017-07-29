package dharmista.batman.com.voicetodo.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import dharmista.batman.com.voicetodo.controller.AlarmIntent;

/**
 * Created by Dharmista on 7/23/2017.
 */

public class AlarmServicer extends Service {

    private BroadcastReceiver mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1 = new Intent(this, AlarmIntent.class);
        intent1.putExtra("id", intent.getExtras().getString("id"));
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent1);
        return START_STICKY;
    }
}
