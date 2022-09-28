package com.maishuo.tingshuohenhaowan.audio;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.maishuo.tingshuohenhaowan.common.CustomApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AudioPlayerManager {

    private static final Float[]         mSpeedArray = new Float[]{0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f};
    private static       Context         mContext;
    private              SimpleExoPlayer mMediaPlayer;

    private DefaultDataSourceFactory dataSourceFactory;
    private Player.EventListener     eventListener;

    public AudioPlayerManager (Context context) {
        mContext = context;
        //初始化创建的参数
        createPlayer();
        //监听
        initListener();
    }

    public static AudioPlayerManager getInstance (Context context) {
        Map<Context, AudioPlayerManager> audioPlayerManagerMap = CustomApplication.getApp().getAudioPlayerManagerMap();
        AudioPlayerManager               mInstance;
        if (!audioPlayerManagerMap.containsKey(context)) {
            mInstance = new AudioPlayerManager(context);
            audioPlayerManagerMap.put(context, mInstance);
        } else {
            mInstance = audioPlayerManagerMap.get(context);
        }

        if (null == mInstance) {
            mInstance = new AudioPlayerManager(context);
            audioPlayerManagerMap.put(context, mInstance);
        }

        return mInstance;
    }

    //创建一个新的player
    private void createPlayer () {
        // 创建带宽
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // 创建轨道选择工厂
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        // 创建轨道选择器实例
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        //step2.创建播放器
        mMediaPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        //这是一个代表将要被播放的媒体的MediaSource
        String userAgent = Util.getUserAgent(mContext, "loader");
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent,
                null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true
        );

        dataSourceFactory = new DefaultDataSourceFactory(
                mContext,
                null,
                httpDataSourceFactory
        );
    }

    public ExoPlayer getMediaPlayer () {
        return mMediaPlayer;
    }

    private String audioUrl;

    public String getAudioUrl () {
        return audioUrl;
    }

    //设置播放 网络url
    public void setAudioUrl (String audioUrl) {
        if (TextUtils.isEmpty(audioUrl)) return;
        try {
            this.audioUrl = audioUrl;

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(audioUrl));
            mMediaPlayer.prepare(mediaSource);
            mMediaPlayer.setPlayWhenReady(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置播放 本地file
    public void setAudioFile (File file) {
        try {
            //这是一个代表将要被播放的媒体的MediaSource
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(file));
            mMediaPlayer.prepare(mediaSource);
            mMediaPlayer.setPlayWhenReady(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调整音量
     *
     * @param volume 0-100
     */
    public void setVolume (float volume) {
        float value = Math.abs(volume / 100);
        mMediaPlayer.setVolume(value);
    }

    //播放
    public void start () {
        if (!isPlaying()) {
            mMediaPlayer.setPlayWhenReady(true);
        }
    }

    //暂停
    public void pause () {
        if (isPlaying()) {
            mMediaPlayer.setPlayWhenReady(false);
        }
    }

    //播放or暂停,返回是否暂停
    public void playOrPause () {
        mMediaPlayer.setPlayWhenReady(!isPlaying());
    }

    public long getDuration () {
        if (mMediaPlayer == null) {
            return 0;
        }
        return mMediaPlayer.getDuration();
    }

    public long getCurrentPosition () {
        if (mMediaPlayer == null) {
            return 0;
        }
        return mMediaPlayer.getCurrentPosition();
    }

    //判断是否是播放状态
    public boolean isPlaying () {
        int playbackState = mMediaPlayer.getPlaybackState();
        if (playbackState == SimpleExoPlayer.STATE_READY && mMediaPlayer.getPlayWhenReady()) {
            return true;
        }
        return false;
    }

    //切换播放速率
    public void switchSpeed (int speedIndex) {
        // 通过设置Speed改变音乐的播放速率
        if (isPlaying()) {
            // 判断是否正在播放，未播放时，要在设置Speed后，暂停音乐播放
            mMediaPlayer.setPlaybackParameters(new PlaybackParameters(mSpeedArray[speedIndex]));
        } else {
            mMediaPlayer.setPlaybackParameters(new PlaybackParameters(mSpeedArray[speedIndex]));
            mMediaPlayer.setPlayWhenReady(false);
        }
    }

    //停止播放
    public void stop (boolean reset) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop(reset);
        }
    }

    //释放资源
    public void release () {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            if (null != eventListener) {
                mMediaPlayer.removeListener(eventListener);
            }
        }
    }

    //监听回调
    private void initListener () {
        eventListener = new Player.EventListener() {
            @Override
            public void onTimelineChanged (Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged (TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged (boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged (boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    //玩家没有任何媒体可以播放
                    case Player.STATE_IDLE:
                    case Player.STATE_BUFFERING:
                        break;
                    case Player.STATE_READY:
                        if (null != playerEventListenerList && !playerEventListenerList.isEmpty()) {
                            for (OnPlayerEventListener listener : playerEventListenerList) {
                                if (null != listener) {
                                    listener.onReady();
                                }
                            }
                        }

                        if (defaultEventListener != null) {
                            defaultEventListener.onReady();
                        }
                        break;
                    case Player.STATE_ENDED:
                        if (null != playerEventListenerList && !playerEventListenerList.isEmpty()) {
                            for (OnPlayerEventListener listener : playerEventListenerList) {
                                if (null != listener) {
                                    listener.onEnd();
                                }
                            }
                        }

                        if (defaultEventListener != null) {
                            defaultEventListener.onEnd();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged (int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged (boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError (ExoPlaybackException error) {
                if (null != playerEventListenerList && !playerEventListenerList.isEmpty()) {
                    for (OnPlayerEventListener listener : playerEventListenerList) {
                        if (null != listener) {
                            listener.onError(error.getMessage());
                        }
                    }
                }

                if (defaultEventListener != null) {
                    defaultEventListener.onError(error.getMessage());
                }
            }

            @Override
            public void onPositionDiscontinuity (int reason) {

            }

            @Override
            public void onPlaybackParametersChanged (PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed () {
                if (defaultEventListener != null) {
                    defaultEventListener.isSeek();
                }
            }
        };
        mMediaPlayer.addListener(eventListener);
    }

    public void seekTo (long positionMs) {
        mMediaPlayer.seekTo(positionMs);
    }

    //回调
    public interface OnPlayerEventListener {

        void onReady ();

        void onEnd ();

        void onError (String msg);

    }

    private final List<OnPlayerEventListener> playerEventListenerList = new ArrayList<>();

    public void addPlayerEventListenerList (OnPlayerEventListener defaultEventListener) {
        this.playerEventListenerList.add(defaultEventListener);
    }

    public void removePlayerEventListener (OnPlayerEventListener onPlayerEventListener) {
        try {
            if (null != playerEventListenerList && !playerEventListenerList.isEmpty()) {
                if (playerEventListenerList.contains(onPlayerEventListener)) {
                    playerEventListenerList.remove(onPlayerEventListener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanDefaultEventListenerList () {
        if (null != playerEventListenerList) {
            playerEventListenerList.clear();
        }
    }

    //回调
    private DefaultEventListener defaultEventListener;

    public interface DefaultEventListener {

        void onReady ();

        void onEnd ();

        void onError (String msg);

        void isSeek ();
    }

    public void setOnDefaultEventListener (DefaultEventListener defaultEventListener) {
        this.defaultEventListener = defaultEventListener;
    }
}
