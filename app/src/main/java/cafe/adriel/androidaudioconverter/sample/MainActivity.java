package cafe.adriel.androidaudioconverter.sample;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Giving time to lib load
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                convertAudio();
            }
        }, 2000);
    }

    private void convertAudio(){
        /**
         *  Update with a valid audio file!
         *  Supported formats: {@link AndroidAudioConverter.AudioFormat}
         */
        File wavFile = new File(Environment.getExternalStorageDirectory(), "recorded_audio.wav");
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {
                Log.d("SUCCESS", convertedFile.getPath());
            }
            @Override
            public void onFailure(Exception error) {
                error.printStackTrace();
            }
        };
        AndroidAudioConverter.with(this)
                .setFile(wavFile)
                .setFormat(AndroidAudioConverter.AudioFormat.MP3)
                .setCallback(callback)
                .convert();
    }

}