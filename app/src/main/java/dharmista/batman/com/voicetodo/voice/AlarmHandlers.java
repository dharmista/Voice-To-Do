package dharmista.batman.com.voicetodo.voice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

import dharmista.batman.com.voicetodo.services.AlarmServicer;
import dharmista.batman.com.voicetodo.controller.PopupActivity;
import dharmista.batman.com.voicetodo.database.entity.VoiceData;
import dharmista.batman.com.voicetodo.database.handler.DBHandler;
import dharmista.batman.com.voicetodo.util.DateUtil;
import dharmista.batman.com.voicetodo.util.Toaster;
import dharmista.batman.com.voicetodo.util.TokenProvider;
import lombok.Setter;
import lombok.experimental.Accessors;

import static android.content.Context.ALARM_SERVICE;

/**
 * {@link AlarmHandlers}
 * Created by Dharmista on 7/23/2017.
 */
@Accessors(chain = true)
@Setter
public class AlarmHandlers {
    PopupActivity instance;
    private String id;
    int[] alarmTime;

    public void createAlarm(Context context, int id, String message, String name) {
        Date date = new Date(alarmTime[0] - 1900,alarmTime[1],alarmTime[2],alarmTime[3],alarmTime[4],0);
        if(date.before(new Date())){
            new Toaster().alert(context, "Please select a upcoming date");
            return;
        }
        long i = date.getTime() - new Date().getTime();
        this.id = saveToDB(message, name, id);
        Intent intent = new Intent(context, AlarmServicer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("id", this.id);
        PendingIntent pendingIntent = PendingIntent.getService(
                context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+i, pendingIntent);
        new Toaster().alert(context, "Alarm set in " + DateUtil.getFormattedPathway(i));
    }

    private String saveToDB(String message, String name, int id) {
        VoiceData voiceData = VoiceData.builder()
                .fileName(instance.getFilename())
                .isTaskCompleted(0)
                .year(alarmTime[0]+"")
                .month(alarmTime[1]+"")
                .date(alarmTime[2]+"")
                .hours(alarmTime[3]+"")
                .minutes(alarmTime[4]+"")
                .serviceId(id+"")
                .id(TokenProvider.getUUID())
                .message(message)
                .name(name)
                .build();
        new DBHandler(instance.getApplicationContext()).addToDb(
                voiceData
        );
        return voiceData.getId();
    }

    public void updateAlarm(Context context, VoiceData updatedVoiceData) {
        Date date = new Date(alarmTime[0] - 1900,alarmTime[1],alarmTime[2],alarmTime[3],alarmTime[4],0);
        if(date.before(new Date())){
            new Toaster().alert(context, "Please select a upcoming date");
            return;
        }
        long i = date.getTime() - new Date().getTime();
        Intent intent = new Intent(context, AlarmServicer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("id", updatedVoiceData.getId());
        PendingIntent pendingIntent = PendingIntent.getService(
                context, Integer.parseInt(updatedVoiceData.getServiceId()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+i, pendingIntent);
        new Toaster().alert(context, "Alarm updated to " + DateUtil.getFormattedPathway(i));
    }
}
