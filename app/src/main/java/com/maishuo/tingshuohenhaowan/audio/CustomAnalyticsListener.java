package com.maishuo.tingshuohenhaowan.audio;

import android.view.Surface;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.qichuang.commonlibs.utils.LogUtils;

/**
 * author : xpSun
 * date : 2021/3/16
 * description :
 */
public class CustomAnalyticsListener implements AnalyticsListener {

    private static final String TAG = "CustomAnalyticsListener";

    @Override
    public void onTimelineChanged (EventTime eventTime, int reason) {
        LogUtils.LOGE(TAG, "onTimelineChanged: 已更改的时间");
    }

    @Override
    public void onPositionDiscontinuity (EventTime eventTime, int reason) {
        LogUtils.LOGE(TAG, "onPositionDiscontinuity:非定位连续性");
    }

    @Override
    public void onSeekStarted (EventTime eventTime) {
        LogUtils.LOGE(TAG, "onSeekStarted:开始seek");
    }

    @Override
    public void onSeekProcessed (EventTime eventTime) {
        LogUtils.LOGE(TAG, "onSeekProcessed: seek 处理");
    }

    @Override
    public void onPlaybackParametersChanged (EventTime eventTime, PlaybackParameters playbackParameters) {
        LogUtils.LOGE(TAG, "onPlaybackParametersChanged: 播放参数更改时");
    }

    @Override
    public void onRepeatModeChanged (EventTime eventTime, int repeatMode) {
        LogUtils.LOGE(TAG, "onRepeatModeChanged: 重复模式更改时");
    }

    @Override
    public void onShuffleModeChanged (EventTime eventTime, boolean shuffleModeEnabled) {
        LogUtils.LOGE(TAG, "onShuffleModeChanged:论分流模式的改变 ");
    }

    @Override
    public void onLoadingChanged (EventTime eventTime, boolean isLoading) {
        LogUtils.LOGE(TAG, "onLoadingChanged: 加载时更改");
    }

    @Override
    public void onPlayerError (EventTime eventTime, ExoPlaybackException error) {
        LogUtils.LOGE(TAG, "onPlayerError: ");
    }

    @Override
    public void onTracksChanged (EventTime eventTime, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        LogUtils.LOGE(TAG, "onTracksChanged: 在更改的轨道上");
    }

    @Override
    public void onBandwidthEstimate (EventTime eventTime, int totalLoadTimeMs, long totalBytesLoaded, long bitrateEstimate) {
        LogUtils.LOGE(TAG, "onBandwidthEstimate: 带宽估计 总加载时间" + totalLoadTimeMs + "   已加载的字节总数" + totalBytesLoaded + "应该是网速" + bitrateEstimate / (1024 * 1024));
    }

    @Override
    public void onSurfaceSizeChanged (EventTime eventTime, int width, int height) {

        LogUtils.LOGE(TAG, "onSurfaceSizeChanged: " + width + "------" + height);
    }

    @Override
    public void onMetadata (EventTime eventTime, Metadata metadata) {
        LogUtils.LOGE(TAG, "onMetadata: 元数据");
    }

    @Override
    public void onDecoderEnabled (EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
        LogUtils.LOGE(TAG, "onDecoderEnabled:启用解码器时 ");
    }

    @Override
    public void onDecoderInitialized (EventTime eventTime, int trackType, String decoderName, long initializationDurationMs) {
        LogUtils.LOGE(TAG, "onDecoderInitialized: 解码器初始化时");
    }

    @Override
    public void onDecoderInputFormatChanged (EventTime eventTime, int trackType, Format format) {
        LogUtils.LOGE(TAG, "onDecoderInputFormatChanged:解码器输入格式更改时 ");
    }

    @Override
    public void onDecoderDisabled (EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
        LogUtils.LOGE(TAG, "onDecoderDisabled:已禁用解码器 ");
    }

    @Override
    public void onAudioAttributesChanged (EventTime eventTime, AudioAttributes audioAttributes) {
        LogUtils.LOGE(TAG, "onAudioAttributesChanged: 音频属性改变");
    }

    @Override
    public void onVolumeChanged (EventTime eventTime, float volume) {
        LogUtils.LOGE(TAG, "onVolumeChanged: 音量改变时");
    }

    @Override
    public void onAudioUnderrun (EventTime eventTime, int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        LogUtils.LOGE(TAG, "onAudioUnderrun: 在音频欠载运行时");
    }

    @Override
    public void onDroppedVideoFrames (EventTime eventTime, int droppedFrames, long elapsedMs) {
        LogUtils.LOGE(TAG, "onDroppedVideoFrames:在丢弃的视频帧上 ");
    }

    @Override
    public void onVideoSizeChanged (EventTime eventTime, int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        LogUtils.LOGE(TAG, "onVideoSizeChanged: ");
    }

    @Override
    public void onRenderedFirstFrame (EventTime eventTime, @Nullable Surface surface) {
        LogUtils.LOGE(TAG, "onRenderedFirstFrame: 在渲染的第一帧上");
    }

    @Override
    public void onDrmSessionAcquired (EventTime eventTime) {
        LogUtils.LOGE(TAG, "onDrmSessionAcquired:在获得DRM会话时 ");
    }

    @Override
    public void onDrmKeysLoaded (EventTime eventTime) {
        LogUtils.LOGE(TAG, "onDrmKeysLoaded: 在已加载的DRM密钥上");
    }

    @Override
    public void onDrmSessionManagerError (EventTime eventTime, Exception error) {
        LogUtils.LOGE(TAG, "onDrmSessionManagerError: 在DRM会话管理器错误时");
    }

    @Override
    public void onDrmKeysRestored (EventTime eventTime) {
        LogUtils.LOGE(TAG, "onDrmKeysRestored: 在DRM密钥恢复时");
    }

    @Override
    public void onDrmKeysRemoved (EventTime eventTime) {
        LogUtils.LOGE(TAG, "onDrmKeysRemoved:在已删除的DRM密钥上");
    }

    @Override
    public void onDrmSessionReleased (EventTime eventTime) {
        LogUtils.LOGE(TAG, "onDrmSessionReleased: 会话已释放");
    }
}
