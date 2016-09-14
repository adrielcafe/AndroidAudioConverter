package cafe.adriel.androidaudiorecorder.callback;

import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public interface IInitCallback {
    
    void onSuccess();
    
    void onFailure(FFmpegNotSupportedException error);
    
}