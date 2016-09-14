package cafe.adriel.androidaudioconverter.sample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import cafe.adriel.androidaudiorecorder.AndroidAudioConverter;
import cafe.adriel.androidaudiorecorder.callback.IConvertCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *  Update with a valid audio file!
         *  Supported formats: {@link AndroidAudioConverter.AudioFormat}
         */
        File wavFile = new File(Environment.getExternalStorageDirectory(), "my_audio.wav");

        AndroidAudioConverter.convert(this, wavFile, AndroidAudioConverter.AudioFormat.AAC, new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {
                Log.d("SUCCESS", convertedFile.getPath());
            }

            @Override
            public void onFailure(Exception error) {
                error.printStackTrace();
            }
        });
    }

}