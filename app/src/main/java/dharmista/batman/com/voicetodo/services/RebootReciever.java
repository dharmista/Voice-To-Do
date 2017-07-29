package dharmista.batman.com.voicetodo.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dharmista on 7/26/2017.
 */

public class RebootReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, AlarmServicer.class);
            context.startService(pushIntent);
        }
    }
}