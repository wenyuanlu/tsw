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
import com.qichuang.commonlibs.utils.LogUtils;

import java.io.File;
import java.util.Map;

public class BackgroundAudioPlayerManager {

    private static final Float[]                      mSpeedArray = new Float[]{0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f};
    private static       BackgroundAudioPlayerManager mInstance;
    private static       Context                      mContext;
    private              SimpleExoPlayer              mMediaPlayer;
    private              DefaultDataSourceFactory     dataSourceFactory;

    public BackgroundAudioPlayerManager (Context context) {
        mContext = context;

        //????????????????????????
        createPlayer();
        //??????
        initListener();
    }

    public static BackgroundAudioPlayerManager getInstance (Context context) {
        Map<Context, BackgroundAudioPlayerManager> backgroundAudioPlayerManagerMap = CustomApplication.getApp().getBackgroundAudioPlayerManagerMap();
        if (!backgroundAudioPlayerManagerMap.containsKey(context)) {
            mInstance = new BackgroundAudioPlayerManager(context);
            backgroundAudioPlayerManagerMap.put(context, mInstance);
        } else {
            mInstance = backgroundAudioPlayerManagerMap.get(context);
        }

        if (null == mInstance) {
            mInstance = new BackgroundAudioPlayerManager(context);
            backgroundAudioPlayerManagerMap.put(context, mInstance);
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
                null /* listener */,
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

    //????????????
    public void start (Player.EventListener listener) {
        if (listener != null) {
            mMediaPlayer.addListener(listener);
        }
        mMediaPlayer.setPlayWhenReady(true);
    }

    public void setVolume (float volume) {
        float value = Math.abs(volume / 100);
        mMediaPlayer.setVolume(value);
    }


    //??????
    public void play () {
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
        if (isPlaying()) {
            mMediaPlayer.setPlayWhenReady(false);
        } else {
            mMediaPlayer.setPlayWhenReady(true);
        }
    }

    public long getDuration () {
        return mMediaPlayer.getDuration();
    }

    public long getCurrentPosition () {
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
        }
    }

    //????????????
    private void initListener () {
        mMediaPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged (Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged (TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged (boolean isLoading) {
                LogUtils.LOGE("ExoPlayer", "isLoading=" + isLoading);
            }

            @Override
            public void onPlayerStateChanged (boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged (int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged (boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError (ExoPlaybackException error) {
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
            }
        });

        mMediaPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged (boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    //????????????????????????????????????
                    case Player.STATE_IDLE:
                    case Player.STATE_BUFFERING:
                        break;
                    case Player.STATE_READY:
                        if (defaultEventListener != null) {
                            defaultEventListener.onReady();
                        }
                        break;
                    case Player.STATE_ENDED:
                        if (defaultEventListener != null) {
                            defaultEventListener.onEnd();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void seekTo (long positionMs) {
        mMediaPlayer.seekTo(positionMs);
    }

    //??????
    private DefaultEventListener defaultEventListener;

    public interface DefaultEventListener {
        void onReady ();

        void onEnd ();

        void onError (String msg);
    }

    public void setOnDefaultEventListener (DefaultEventListener defaultEventListener) {
        this.defaultEventListener = defaultEventListener;
    }
}
