package dharmista.batman.com.voicetodo.controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import dharmista.batman.com.voicetodo.R;
import dharmista.batman.com.voicetodo.database.entity.VoiceData;
import dharmista.batman.com.voicetodo.database.handler.DBHandler;
import dharmista.batman.com.voicetodo.util.AndroidUtils;
import dharmista.batman.com.voicetodo.util.Toaster;
import dharmista.batman.com.voicetodo.voice.AlarmHandlers;
import lombok.Getter;

import static dharmista.batman.com.voicetodo.util.Constants.DUMMY;
import static dharmista.batman.com.voicetodo.util.Constants.FILENAME;
import static dharmista.batman.com.voicetodo.util.DateUtil.months_;

public class PopupActivity extends AppCompatActivity implements DateTime{

    Button setDate, setTime, submit;
    EditText name, message;
    @Getter
    private String filename;
    private DBHandler dbHandler;
    private AndroidUtils androidUtils;
    final PopupActivity popupActivity = this;

    TextView dateMsg;
    final int[] perfectDateAndTimeInstance = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        initHandlers();
        androidUtils = new AndroidUtils();
        dbHandler = new DBHandler(this);
        name.requestFocus();
        filename = getIntent().getExtras().getString(FILENAME);

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidUtils.askDate(PopupActivity.this, perfectDateAndTimeInstance, 0, popupActivity);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(message.getText().toString().length() == 0 && filename == null){
                    new Toaster().alert(getApplicationContext(), "Either message or a recording is compulsory..");
                }
                else {
                    startAlert();
                }
            }
        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidUtils.askTime(PopupActivity.this, perfectDateAndTimeInstance, 3, popupActivity);
            }
        });
    }

    private void startAlert() {
        int id;
        VoiceData voiceData = dbHandler.getVoiceRecordById(DUMMY);
        if(voiceData == null) {
            dbHandler.addToDb(
                    VoiceData.builder()
                    .id(DUMMY)
                    .isTaskCompleted(1)
                    .name("0")
                    .build()
            );
            id = 0;
        }else {
            id = Integer.parseInt(voiceData.getName());
            dbHandler.updateAlarmInstanceId(DUMMY, id + 1);
        }
        new AlarmHandlers()
                .setAlarmTime(perfectDateAndTimeInstance)
                .setInstance(this)
                .createAlarm(this, id, message.getText().toString(), name.getText().toString());
    }

    private void initHandlers() {
        name = (EditText)findViewById(R.id.editText2);
        message = (EditText)findViewById(R.id.editText);

        setDate = (Button)findViewById(R.id.button);
        submit = (Button)findViewById(R.id.button2);
        setTime = (Button)findViewById(R.id.dateButton);

        dateMsg = (TextView)findViewById(R.id.setTime);
    }

    @Override
    public Object afterSet(String param) {
        String ampm = "AM";int timeSeperator = 0;
        if(perfectDateAndTimeInstance[3] > 12) {
            ampm = "PM";
            timeSeperator = 12;
        }
        dateMsg.setText(String.format("%d-%s-%d %d:%d %s",
                perfectDateAndTimeInstance[0],
                months_[perfectDateAndTimeInstance[1]],
                perfectDateAndTimeInstance[2],
                perfectDateAndTimeInstance[3] - timeSeperator,
                perfectDateAndTimeInstance[4],
                ampm
        ));
        dateMsg.setVisibility(View.VISIBLE);
        return null;
    }
}
