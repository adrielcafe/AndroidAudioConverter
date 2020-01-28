package cafe.adriel.androidaudioconverter;

import android.content.Context;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;
import cafe.adriel.androidaudioconverter.model.BitRate;

public class AndroidAudioConverter {

    private static boolean loaded;

    private Context context;
    private File audioFile;
    private AudioFormat format;
    private BitRate bitRate = BitRate.def;
    private IConvertCallback callback;
    private boolean mono;
    private Integer sampleRate;

    private AndroidAudioConverter(Context context){
        this.context = context;
    }

    public static boolean isLoaded(){
        return loaded;
    }

    public static void load(Context context, final ILoadCallback callback){
        try {
            FFmpeg.getInstance(context).loadBinary(new FFmpegLoadBinaryResponseHandler() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess() {
                            loaded = true;
                            callback.onSuccess();
                        }

                        @Override
                        public void onFailure() {
                            loaded = false;
                            callback.onFailure(new Exception("Failed to loaded FFmpeg lib"));
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
        } catch (Exception e){
            loaded = false;
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

    public AndroidAudioConverter setBitRate(BitRate bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    public AndroidAudioConverter setSampleRate(Integer sampleRate) {
        if(Arrays.asList(new Integer[]{8000, 11025, 16000, 22050, 32000, 44100, 48000, 88200, 96000,76400, 192000, 352800,384000}).contains(sampleRate)){
                this.sampleRate = sampleRate;
        } else{
            Log.e("error", "Indicated sample rate is not supported, audio is converting with original sample rate");
            this.sampleRate = 0;
        }
        return this;
    }

    public AndroidAudioConverter setMono(boolean mono) {
        this.mono = mono;
        return this;
    }

    public AndroidAudioConverter setCallback(IConvertCallback callback) {
        this.callback = callback;
        return this;
    }

    public void convert() {
        if(!isLoaded()){
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

        ArrayList<String> commandBuilder = new ArrayList<String>();

        commandBuilder.addAll(Arrays.asList("-y", "-i", audioFile.getPath()));

        if(sampleRate != null && sampleRate != 0){
            commandBuilder.addAll(Arrays.asList("-ar", sampleRate.toString()));
        }

        if(bitRate != BitRate.def){
            commandBuilder.addAll(Arrays.asList("-sample_fmt", bitRate.getBitRate()));
        }

        if(mono){
            commandBuilder.addAll(Arrays.asList("-ac", "1"));
        }

        commandBuilder.add(convertedFile.getPath());

        String[] cmd = commandBuilder.toArray(new String[commandBuilder.size()]);

        try {
            FFmpeg.getInstance(context).execute(cmd, new FFmpegExecuteResponseHandler() {
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
        } catch (Exception e){
            callback.onFailure(e);
        }
    }

    private static File getConvertedFile(File originalFile, AudioFormat format){
        String[] f = originalFile.getPath().split("\\.");
        String filePath = originalFile.getPath().replace(f[f.length - 1], format.getFormat());
        return new File(filePath);
    }
}