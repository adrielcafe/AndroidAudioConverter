package cafe.adriel.androidaudiorecorder;

import android.content.Context;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.IOException;

import cafe.adriel.androidaudiorecorder.callback.IConvertCallback;
import cafe.adriel.androidaudiorecorder.callback.IInitCallback;

public class AndroidAudioConverter {

    public enum AudioFormat {
        WAV,
        AAC,
        MP3,
        M4A,
        WMA,
        FLAC;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    private Context context;
    private File audioFile;
    private AudioFormat format;
    private IConvertCallback callback;

    private static boolean load;

    private AndroidAudioConverter(Context context){
        this.context = context;
    }

    public static boolean isLoad(){
        return load;
    }

    public static void load(Context context, final IInitCallback callback){
        try {
            FFmpeg.getInstance(context)
                    .loadBinary(new FFmpegLoadBinaryResponseHandler() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess() {
                            load = true;
                            callback.onSuccess();
                        }

                        @Override
                        public void onFailure() {
                            load = false;
                            callback.onFailure(new FFmpegNotSupportedException("Failed to load FFmpeg lib"));
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
        } catch (FFmpegNotSupportedException e){
            load = false;
            callback.onFailure(e);
        }
    }

    public static AndroidAudioConverter with(Context context) {
        return new AndroidAudioConverter(context);
    }

    public AndroidAudioConverter setFile(File originalFile) {
        this.audioFile = originalFile;
        return this;
    }

    public AndroidAudioConverter setFormat(AudioFormat format) {
        this.format = format;
        return this;
    }

    public AndroidAudioConverter setCallback(IConvertCallback callback) {
        this.callback = callback;
        return this;
    }

    public void convert() {
        if(!isLoad()){
            callback.onFailure(new Exception("FFmpeg not loaded"));
            return;
        }
        if(audioFile == null || !audioFile.exists()){
            callback.onFailure(new IOException("File not exists"));
            return;
        }
        if(!audioFile.canRead()){
            callback.onFailure(new IOException("Can't read the file. Missing permission?"));
            return;
        }
        final File convertedFile = getConvertedFile(audioFile, format);
        String[] cmd = new String[]{"-y", "-i", audioFile.getPath(), convertedFile.getPath()};
        try {
            FFmpeg.getInstance(context)
                    .execute(cmd, new FFmpegExecuteResponseHandler() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onProgress(String message) {

                        }

                        @Override
                        public void onSuccess(String message) {
                            callback.onSuccess(convertedFile);
                        }

                        @Override
                        public void onFailure(String message) {
                            callback.onFailure(new IOException(message));
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
        } catch (FFmpegCommandAlreadyRunningException e){
            callback.onFailure(e);
        }
    }

    private static File getConvertedFile(File originalFile, AudioFormat format){
        String[] f = originalFile.getPath().split("\\.");
        String filePath = originalFile.getPath().replace("." + f[f.length - 1], "." + format);
        return new File(filePath);
    }
}