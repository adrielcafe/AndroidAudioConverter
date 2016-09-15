package cafe.adriel.androidaudioconverter.sample;

import android.app.Application;

import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IInitCallback;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidAudioConverter.load(this, new IInitCallback() {
            @Override
            public void onSuccess() {
                // Great!
            }
            @Override
            public void onFailure(FFmpegNotSupportedException error) {
                // FFmpeg is not supported by device
                error.printStackTrace();
            }
        });
    }
}