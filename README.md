[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidAudioConverter-green.svg?style=true)](https://android-arsenal.com/details/1/4341) [![Release](https://jitpack.io/v/adrielcafe/AndroidAudioConverter.svg)](https://jitpack.io/#adrielcafe/AndroidAudioConverter)

# AndroidAudioConverter

> Convert audio files inside your Android app easily. This is a wrapper of [FFmpeg-Android-Java](https://github.com/WritingMinds/ffmpeg-android-java) lib.

Supported formats:
* AAC
* MP3
* M4A
* WMA
* WAV
* FLAC

Lib size: ~9mb

## How To Use

1 - Add this permission into your `AndroidManifest.xml` and [request in Android 6.0+](https://developer.android.com/training/permissions/requesting.html)
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

2 - Load the lib inside your `Application` class
```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
                // Great!
            }
            @Override
            public void onFailure(Exception error) {
                // FFmpeg is not supported by device
            }
        });
    }
}
```

3 - Convert audio files async
```java
File flacFile = new File(Environment.getExternalStorageDirectory(), "my_audio.flac");
IConvertCallback callback = new IConvertCallback() {
    @Override
    public void onSuccess(File convertedFile) {
        // So fast? Love it!
    }
    @Override
    public void onFailure(Exception error) {
        // Oops! Something went wrong
    }
};
AndroidAudioConverter.with(this)
    // Your current audio file
    .setFile(flacFile)  
    
    // Your desired audio format 
    .setFormat(AudioFormat.MP3)
    
    // An callback to know when conversion is finished
    .setCallback(callback)
    
    // Start conversion
    .convert();
```

## Import to your project
Put this into your `app/build.gradle`:
```
repositories {
  maven {
    url "https://jitpack.io"
  }
}

dependencies {
  compile 'com.github.adrielcafe:AndroidAudioConverter:0.0.8'
}
```

## Dependencies
* [FFmpeg-Android-Java](https://github.com/WritingMinds/ffmpeg-android-java)

## Want to RECORD AUDIO into your app?
**Take a look at [AndroidAudioRecorder](https://github.com/adrielcafe/AndroidAudioRecorder)! Example of usage [here](https://github.com/adrielcafe/AndroidAudioRecorder/issues/8#issuecomment-247311572).**

## License
```
The MIT License (MIT)

Copyright (c) 2016 Adriel Café

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
