package dharmista.batman.com.voicetodo.voice;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import dharmista.batman.com.voicetodo.controller.MainActivity;
import lombok.Getter;
import lombok.Setter;

import static dharmista.batman.com.voicetodo.util.Constants.DIRECTORY;

/**
 * Created by Dharmista on 7/20/2017.
 */

public class VoiceControllers {
    public String AudioSavePathInDevice = null;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    @Getter
    @Setter
    private Boolean isRecording = false;
    public long startTime, endTime;
    Random random = new Random();
    @Getter
    private MediaPlayer mediaPlayer;
    @Getter
    private MediaRecorder mediaRecorder;

    public void recordVoice(Context context) {
        AudioSavePathInDevice =
                Environment.getExternalStorageDirectory().getAbsolutePath() + DIRECTORY +
                        CreateRandomAudioFileName(5) + "AudioRecording.3gp";

        MediaRecorderReady();

        try {
            mediaRecorder.prepare();
            startTime = System.currentTimeMillis();
            mediaRecorder.start();
            isRecording = true;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopRecordingVoice(Context context) {
        endTime = System.currentTimeMillis();
        mediaRecorder.stop();
        isRecording = false;
    }

    public void recordLastPlayed(Context context) {

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(AudioSavePathInDevice);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    public void stopPlaying(MediaPlayer mediaPlayer) {
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                MediaRecorderReady();
            }
        }
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public void checkExistance(String fileName) {
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                fileName
                        );

        if(!path.exists()) {
            path.mkdirs();
        }
    }
}
