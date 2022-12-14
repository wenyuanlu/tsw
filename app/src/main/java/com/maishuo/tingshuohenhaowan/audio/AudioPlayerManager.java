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
        //????????????????????????
        createPlayer();
        //??????
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

    //??????????????????player
    private void createPlayer () {
        // ????????????
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // ????????????????????????
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        // ???????????????????????????
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        //step2.???????????????
        mMediaPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        //?????????????????????????????????????????????MediaSource
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

    //???????????? ??????url
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

    //???????????? ??????file
    public void setAudioFile (File file) {
        try {
            //?????????????????????????????????????????????MediaSource
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(file));
            mMediaPlayer.prepare(mediaSource);
            mMediaPlayer.setPlayWhenReady(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????
     *
     * @param volume 0-100
     */
    public void setVolume (float volume) {
        float value = Math.abs(volume / 100);
        mMediaPlayer.setVolume(value);
    }

    //??????
    public void start () {
        if (!isPlaying()) {
            mMediaPlayer.setPlayWhenReady(true);
        }
    }

    //??????
    public void pause () {
        if (isPlaying()) {
            mMediaPlayer.setPlayWhenReady(false);
        }
    }

    //??????or??????,??????????????????
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

    //???????????????????????????
    public boolean isPlaying () {
        int playbackState = mMediaPlayer.getPlaybackState();
        if (playbackState == SimpleExoPlayer.STATE_READY && mMediaPlayer.getPlayWhenReady()) {
            return true;
        }
        return false;
    }

    //??????????????????
    public void switchSpeed (int speedIndex) {
        // ????????????Speed???????????????????????????
        if (isPlaying()) {
            // ??????????????????????????????????????????????????????Speed????????????????????????
            mMediaPlayer.setPlaybackParameters(new PlaybackParameters(mSpeedArray[speedIndex]));
        } else {
            mMediaPlayer.setPlaybackParameters(new PlaybackParameters(mSpeedArray[speedIndex]));
            mMediaPlayer.setPlayWhenReady(false);
        }
    }

    //????????????
    public void stop (boolean reset) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop(reset);
        }
    }

    //????????????
    public void release () {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            if (null != eventListener) {
                mMediaPlayer.removeListener(eventListener);
            }
        }
    }

    //????????????
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
                    //????????????????????????????????????
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

    //??????
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

    //??????
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
