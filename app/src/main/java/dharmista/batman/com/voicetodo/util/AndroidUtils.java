package dharmista.batman.com.voicetodo.util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import dharmista.batman.com.voicetodo.controller.DateTime;
import dharmista.batman.com.voicetodo.controller.PopupActivity;
import dharmista.batman.com.voicetodo.interfaceH.mainActivity.BodyGenerator;

/**
 * Created by Dharmista on 7/24/2017.
 */

public class AndroidUtils {

    public void askDate(Context context, final int[] perfectDateAndTimeInstance, final int startIndex, final DateTime inst) {
        final Calendar myCal = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMinDate(System.currentTimeMillis() - 1000);
                perfectDateAndTimeInstance[startIndex] = datePicker.getYear();
                perfectDateAndTimeInstance[startIndex + 1] = datePicker.getMonth();
                perfectDateAndTimeInstance[startIndex + 2] = datePicker.getDayOfMonth();

                inst.afterSet(null);
            }
        };

        new DatePickerDialog(context, date, myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), myCal.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void askTime(final Context context, final int[] perfectDateAndTimeInstance, final int startIndex,final DateTime inst) {
        final Calendar myCal = Calendar.getInstance();
        final TimePickerDialog.OnTimeSetListener time =  new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                perfectDateAndTimeInstance[startIndex] = i;
                perfectDateAndTimeInstance[startIndex + 1] = i1;

                inst.afterSet(null);
            }
        };

        new TimePickerDialog(context, time, myCal.get(Calendar.HOUR), myCal.get(Calendar.MINUTE), false).show();
    }

    public void setUpWebView(WebView webView, String body){
        webView.getSettings().setAppCacheEnabled(false);
        webView.loadDataWithBaseURL( "file:///android_asset/", body, "text/html",
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
    }
}
