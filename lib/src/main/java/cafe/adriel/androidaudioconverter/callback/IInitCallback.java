package cafe.adriel.androidaudioconverter.callback;

import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public interface IInitCallback {
    
    void onSuccess();
    
    void onFailure(FFmpegNotSupportedException error);
    
}