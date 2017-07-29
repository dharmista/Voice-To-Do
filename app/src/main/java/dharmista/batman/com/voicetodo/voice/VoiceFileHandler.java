package dharmista.batman.com.voicetodo.voice;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import dharmista.batman.com.voicetodo.services.CacheRemover;

import static dharmista.batman.com.voicetodo.util.Constants.DIRECTORY;

/**
 * Created by Dharmista on 7/23/2017.
 */

public class VoiceFileHandler {

    public boolean deleteFiles(Context context, List<String> fileNames) {
        Class name = context.getClass();
        Boolean aeiouVowel = true, isThisCacheRemover = false;
        if(name.equals(CacheRemover.class)){
         isThisCacheRemover = true;
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY;
        int i,fino = 1;
        if(isThisCacheRemover)
            fino = 0;
        for(i=0; i < fileNames.size() - fino; i++) {
            String filename = fileNames.get(i);

            if(isThisCacheRemover){
                filename = path + filename;
            }
            File file = new File(filename);
            aeiouVowel &= file.delete();
        }
        if(i!=0 && !(context.getClass().equals(CacheRemover.class))) {
            Toast.makeText(context, "Deleted all your dubsmash trails :P",Toast.LENGTH_SHORT).show();
        }
        return aeiouVowel;
    }
}
