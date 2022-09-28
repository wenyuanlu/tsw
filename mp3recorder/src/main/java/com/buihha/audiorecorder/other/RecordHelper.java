package com.buihha.audiorecorder.other;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.buihha.audiorecorder.other.fftlib.FftFactory;
import com.buihha.audiorecorder.other.listener.RecordDataListener;
import com.buihha.audiorecorder.other.listener.RecordFftDataListener;
import com.buihha.audiorecorder.other.listener.RecordResultListener;
import com.buihha.audiorecorder.other.listener.RecordSoundSizeListener;
import com.buihha.audiorecorder.other.listener.RecordStateListener;
import com.buihha.audiorecorder.other.utils.ByteUtils;
import com.buihha.audiorecorder.other.utils.FileUtils;
import com.buihha.audiorecorder.other.utils.LoggerUtils;
import com.buihha.audiorecorder.other.wav.WavUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * author ：yh
 * date : 2021/1/27 16:23
 * description :
 */
public class RecordHelper {
    private static final    String       TAG                       = RecordHelper.class.getSimpleName();
    private volatile static RecordHelper instance;
    private volatile        RecordState  state                     = RecordState.IDLE;
    private static final    int          RECORD_AUDIO_BUFFER_TIMES = 1;

    private       RecordStateListener     recordStateListener;
    private       RecordDataListener      recordDataListener;
    private       RecordSoundSizeListener recordSoundSizeListener;
    private       RecordResultListener    recordResultListener;
    private       RecordFftDataListener   recordFftDataListener;
    private       RecordConfig            currentConfig;
    private       AudioRecordThread       audioRecordThread;
    private final Handler                 mainHandler = new Handler(Looper.getMainLooper());

    private       File            resultFile = null;
    private       File            tmpFile    = null;
    private final List<File>      files      = new ArrayList<>();
    private       Mp3EncodeThread mp3EncodeThread;

    public RecordConfig getCurrentConfig () {
        return currentConfig;
    }

    private RecordHelper () {
    }

    public static RecordHelper getInstance () {
        if (instance == null) {
            synchronized (RecordHelper.class) {
                if (instance == null) {
                    instance = new RecordHelper();
                }
            }
        }
        return instance;
    }

    public void setRecordStateListener (RecordStateListener recordStateListener) {
        this.recordStateListener = recordStateListener;
    }

    public void setRecordSoundSizeListener (RecordSoundSizeListener recordSoundSizeListener) {
        this.recordSoundSizeListener = recordSoundSizeListener;
    }

    public void setRecordResultListener (RecordResultListener recordResultListener) {
        this.recordResultListener = recordResultListener;
    }

    public void start (String filePath, RecordConfig config) {
        if (config == null) {
            config = new RecordConfig();
        }
        this.currentConfig = config;
        if (state != RecordState.IDLE && state != RecordState.STOP) {
            LoggerUtils.e(TAG, "状态异常当前状态： %s", state.name());
            return;
        }
        resultFile = new File(filePath);
        String tempFilePath = getTempFilePath();

        LoggerUtils.d(TAG, "----------------开始录制 %s------------------------", currentConfig.getRecordFormat().name());
        LoggerUtils.d(TAG, "参数： %s", currentConfig.toString());
        LoggerUtils.i(TAG, "pcm缓存 tmpFile: %s", tempFilePath);
        LoggerUtils.i(TAG, "录音文件 resultFile: %s", filePath);

        tmpFile = new File(tempFilePath);
        audioRecordThread = new AudioRecordThread();
        audioRecordThread.start();
    }

    public void stop () {
        if (state == RecordState.IDLE) {
            LoggerUtils.e(TAG, "状态异常当前状态： %s", state.name());
            return;
        }

        if (state == RecordState.PAUSE) {
            makeFile();
            state = RecordState.IDLE;
            notifyState();
            stopMp3Encoded();
        } else {
            state = RecordState.STOP;
            notifyState();
        }
    }

    /**
     * 是否正在录音
     *
     * @return
     */
    public boolean isRecording () {
        return state == RecordState.RECORDING;
    }

    void pause () {
        if (state != RecordState.RECORDING) {
            LoggerUtils.e(TAG, "状态异常当前状态： %s", state.name());
            return;
        }
        state = RecordState.PAUSE;
        notifyState();
    }

    void resume () {
        if (state != RecordState.PAUSE) {
            LoggerUtils.e(TAG, "状态异常当前状态： %s", state.name());
            return;
        }
        String tempFilePath = getTempFilePath();
        LoggerUtils.i(TAG, "tmpPCM File: %s", tempFilePath);
        tmpFile = new File(tempFilePath);
        audioRecordThread = new AudioRecordThread();
        audioRecordThread.start();
    }

    private void notifyState () {
        if (recordStateListener == null) {
            return;
        }
        mainHandler.post(() -> recordStateListener.onStateChange(state));

        if (state == RecordState.STOP || state == RecordState.PAUSE) {
            if (recordSoundSizeListener != null) {
                recordSoundSizeListener.onSoundSize(0);
            }
        }
    }

    private void notifyFinish () {
        LoggerUtils.d(TAG, "录音结束 file: %s", resultFile.getAbsolutePath());

        mainHandler.post(() -> {
            if (recordStateListener != null) {
                recordStateListener.onStateChange(RecordState.FINISH);
            }
            if (recordResultListener != null) {
                recordResultListener.onResult(resultFile);
            }
        });
    }

    private void notifyError (final String error) {
        if (recordStateListener == null) {
            return;
        }
        mainHandler.post(() -> recordStateListener.onError(error));
    }

    private final FftFactory fftFactory = new FftFactory();

    private void notifyData (final byte[] data) {
        if (recordDataListener == null && recordSoundSizeListener == null && recordFftDataListener == null) {
            return;
        }
        mainHandler.post(() -> {
            if (recordDataListener != null) {
                recordDataListener.onData(data);
            }

            if (recordFftDataListener != null || recordSoundSizeListener != null) {
                byte[] fftData = fftFactory.makeFftData(data);
                if (fftData != null) {
                    if (recordSoundSizeListener != null) {
                        recordSoundSizeListener.onSoundSize(getDb(fftData));
                    }
                    if (recordFftDataListener != null) {
                        recordFftDataListener.onFftData(fftData);
                    }
                }
            }
        });
    }

    private int getDb (byte[] data) {
        double sum         = 0;
        double ave;
        int    length      = Math.min(data.length, 128);
        int    offsetStart = 8;
        for (int i = offsetStart; i < length; i++) {
            sum += data[i];
        }
        ave = (sum / (length - offsetStart)) * 65536 / 128f;
        int i = (int) (Math.log10(ave) * 20);
        return i < 0 ? 27 : i;
    }

    private void initMp3EncoderThread (int bufferSize) {
        try {
            mp3EncodeThread = new Mp3EncodeThread(resultFile, bufferSize);
            mp3EncodeThread.start();
        } catch (Exception e) {
            LoggerUtils.e(e, TAG, e.getMessage());
        }
    }

    /**
     * 录音子线程
     */
    private class AudioRecordThread extends Thread {
        private final AudioRecord mAudioRecord;
        private final int         mBufferSize;

        AudioRecordThread () {
            mBufferSize = AudioRecord.getMinBufferSize(currentConfig.getSampleRate(),
                    currentConfig.getChannel(), currentConfig.getEncodingConfig()) * RECORD_AUDIO_BUFFER_TIMES;
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, currentConfig.getSampleRate(),
                    currentConfig.getChannel(), currentConfig.getEncodingConfig(), mBufferSize);
            if (currentConfig.getRecordFormat() == RecordConfig.RecordFormat.MP3) {
                if (mp3EncodeThread == null) {
                    initMp3EncoderThread(mBufferSize);
                } else {
                    LoggerUtils.e(TAG, "mp3EncodeThread != null, 请检查代码");
                }
            }
        }

        @Override
        public void run () {
            super.run();
            if (currentConfig.getRecordFormat() == RecordConfig.RecordFormat.MP3) {
                startMp3Recorder();
            } else {
                startPcmRecorder();
            }
        }

        private void startPcmRecorder () {
            state = RecordState.RECORDING;
            notifyState();
            LoggerUtils.d(TAG, "开始录制 Pcm");
            try (FileOutputStream fos = new FileOutputStream(tmpFile)) {
                mAudioRecord.startRecording();
                byte[] byteBuffer = new byte[mBufferSize];

                while (state == RecordState.RECORDING) {
                    int end = mAudioRecord.read(byteBuffer, 0, byteBuffer.length);
                    notifyData(byteBuffer);
                    fos.write(byteBuffer, 0, end);
                    fos.flush();
                }
                mAudioRecord.stop();
                files.add(tmpFile);
                if (state == RecordState.STOP) {
                    makeFile();
                } else {
                    LoggerUtils.i(TAG, "暂停！");
                }
            } catch (Exception e) {
                LoggerUtils.e(e, TAG, e.getMessage());
                notifyError("录音失败");
            }

            if (state != RecordState.PAUSE) {
                state = RecordState.IDLE;
                notifyState();
                LoggerUtils.d(TAG, "录音结束");
            }
        }

        private void startMp3Recorder () {
            state = RecordState.RECORDING;
            notifyState();

            try {
                mAudioRecord.startRecording();
                short[] byteBuffer = new short[mBufferSize];

                while (state == RecordState.RECORDING) {
                    int end = mAudioRecord.read(byteBuffer, 0, byteBuffer.length);
                    if (mp3EncodeThread != null) {
                        mp3EncodeThread.addChangeBuffer(new Mp3EncodeThread.ChangeBuffer(byteBuffer, end));
                    }
                    notifyData(ByteUtils.toBytes(byteBuffer));
                }
                mAudioRecord.stop();
            } catch (Exception e) {
                LoggerUtils.e(e, TAG, e.getMessage());
                notifyError("录音失败");
            }
            if (state != RecordState.PAUSE) {
                state = RecordState.IDLE;
                notifyState();
                stopMp3Encoded();
            } else {
                LoggerUtils.d(TAG, "暂停");
            }
        }
    }

    private void stopMp3Encoded () {
        if (mp3EncodeThread != null) {
            mp3EncodeThread.stopSafe(() -> {
                notifyFinish();
                mp3EncodeThread = null;
            });
        } else {
            LoggerUtils.e(TAG, "mp3EncodeThread is null, 代码业务流程有误，请检查！！ ");
        }
    }

    private void makeFile () {
        switch (currentConfig.getRecordFormat()) {
            case MP3:
                return;
            case WAV:
                mergePcmFile();
                makeWav();
                break;
            case PCM:
                mergePcmFile();
                break;
            default:
                break;
        }
        notifyFinish();
        LoggerUtils.i(TAG, "录音完成！ path: %s ； 大小：%s", resultFile.getAbsoluteFile(), resultFile.length());
    }

    /**
     * 添加Wav头文件
     */
    private void makeWav () {
        if (!FileUtils.isFile(resultFile) || resultFile.length() == 0) {
            return;
        }
        byte[] header = WavUtils.generateWavFileHeader((int) resultFile.length(), currentConfig.getSampleRate(), currentConfig.getChannelCount(), currentConfig.getEncoding());
        WavUtils.writeHeader(resultFile, header);
    }

    /**
     * 合并文件
     */
    private void mergePcmFile () {
        boolean mergeSuccess = mergePcmFiles(resultFile, files);
        if (!mergeSuccess) {
            notifyError("合并失败");
        }
    }

    /**
     * 合并Pcm文件
     *
     * @param recordFile 输出文件
     * @param files      多个文件源
     * @return 是否成功
     */
    private boolean mergePcmFiles (File recordFile, List<File> files) {
        if (recordFile == null || files == null || files.size() <= 0) {
            return false;
        }

        byte[] buffer = new byte[1024];
        try (FileOutputStream fos = new FileOutputStream(recordFile);
             BufferedOutputStream outputStream = new BufferedOutputStream(fos)) {

            for (int i = 0; i < files.size(); i++) {
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(files.get(i)));
                int                 readCount;
                while ((readCount = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, readCount);
                }
                inputStream.close();
            }
        } catch (Exception e) {
            LoggerUtils.e(e, TAG, e.getMessage());
            return false;
        }

        try {
            if (null != files && !files.isEmpty()) {
                for (File file : files) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        files.clear();
        return true;
    }

    /**
     * 根据当前的时间生成相应的文件名
     * 实例 record_20160101_13_15_12
     */
    private String getTempFilePath () {
        String fileDir = this.currentConfig != null ? this.currentConfig.getRecordDir() : String.format(Locale.getDefault(), "%s/Record/", Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!FileUtils.createOrExistsDir(fileDir)) {
            LoggerUtils.e(TAG, "文件夹创建失败：%s", fileDir);
        }
        String fileName = String.format(Locale.getDefault(), "record_tmp_%s", FileUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.SIMPLIFIED_CHINESE)));
        return String.format(Locale.getDefault(), "%s%s.pcm", fileDir, fileName);
    }

    /**
     * 表示当前状态
     */
    public enum RecordState {
        /**
         * 空闲状态
         */
        IDLE,
        /**
         * 录音中
         */
        RECORDING,
        /**
         * 暂停中
         */
        PAUSE,
        /**
         * 正在停止
         */
        STOP,
        /**
         * 录音流程结束（转换结束）
         */
        FINISH
    }

}
