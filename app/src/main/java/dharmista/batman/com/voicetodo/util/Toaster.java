package dharmista.batman.com.voicetodo.util;

import android.content.Context;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * Created by Dharmista on 7/25/2017.
 */

public class Toaster {

    public void alert(Context context, String message) {
        Toast.makeText(context, message,Toast.LENGTH_SHORT).show();
    }
}
