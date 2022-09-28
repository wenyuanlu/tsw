package com.maishuo.tingshuohenhaowan.main.dialog

import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.maishuo.tingshuohenhaowan.audio.AudioPlayerManager
import com.maishuo.tingshuohenhaowan.audio.AudioPlayerManager.OnPlayerEventListener
import com.maishuo.tingshuohenhaowan.audio.BackgroundAudioPlayerManager
import com.maishuo.tingshuohenhaowan.databinding.ViewPhonicSendSelecotBgmLayoutBinding
import com.maishuo.tingshuohenhaowan.api.response.SoundeffectBean
import com.maishuo.tingshuohenhaowan.main.adapter.CustomBGMSelectorAdapter
import com.qichuang.commonlibs.basic.BaseActivity
import com.qichuang.commonlibs.basic.BaseDialog

/**
 * author : xpSun
 * date : 10/11/21
 * description :留声发布选择背景音乐界面
 */
class PhonicSendSelectorBGMDialog constructor(activity: BaseActivity) : BaseDialog(activity) {

    companion object {
        private const val MUSIC_DEFAULT_VOLUME = 50
    }

    private var binding: ViewPhonicSendSelecotBgmLayoutBinding? = null
    private var adapter: CustomBGMSelectorAdapter? = null
    private var selectorBGMusicId: Int? = null

    var soundeffects: MutableList<SoundeffectBean>? = null
        set(value) {
            if (field.isNullOrEmpty() || value.isNullOrEmpty()) {
                field = mutableListOf()
            }

            if (!field.isNullOrEmpty()) {
                field?.clear()
            }

            field?.add(SoundeffectBean())

            if (!value.isNullOrEmpty()) {
                field?.addAll(value)
            }

            adapter?.setNewInstance(field)
        }
    var mp3Path: String? = null

    interface OnBgMusicChangerListener {

        fun onChanger(recorderVolume: Int?, bgMusicVolume: Int?, bgMusicId: Int?)

    }

    var onBgMusicChangerListener: OnBgMusicChangerListener? = null

    override fun fetchRootView(): View? {
        binding = ViewPhonicSendSelecotBgmLayoutBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    override fun initWidgets() {
        setGravity(Gravity.BOTTOM)

        AudioPlayerManager.getInstance(activity).setVolume(MUSIC_DEFAULT_VOLUME.toFloat())
        BackgroundAudioPlayerManager.getInstance(activity).setVolume(MUSIC_DEFAULT_VOLUME.toFloat())

        adapter = CustomBGMSelectorAdapter()
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding?.recyclerviewBgMusic?.layoutManager = layoutManager
        binding?.recyclerviewBgMusic?.adapter = adapter

        initWidgetsEvent()
    }

    private fun initWidgetsEvent() {
        binding?.musicProgress1?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                AudioPlayerManager.getInstance(activity).setVolume(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        binding?.musicProgress2?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                BackgroundAudioPlayerManager.getInstance(activity).setVolume(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        adapter?.setOnItemClickListener { _, _, position ->
            playBGMusic(position)
        }
    }


    private fun playBGMusic(position: Int) {
        try {
            if (adapter?.data.isNullOrEmpty()) {
                return
            }

            val item: SoundeffectBean = adapter?.data!![position]
            adapter?.setSelectorPosition(position)
            val audioPlayerManager = AudioPlayerManager.getInstance(activity)
            val backgroundAudioPlayerManager = BackgroundAudioPlayerManager.getInstance(activity)
            audioPlayerManager.addPlayerEventListenerList(object : OnPlayerEventListener {
                override fun onReady() {}
                override fun onEnd() {
                    if (null != backgroundAudioPlayerManager && backgroundAudioPlayerManager.isPlaying) {
                        backgroundAudioPlayerManager.stop(false)
                    }
                }

                override fun onError(msg: String) {}
            })

            if (0 == position) {
                audioPlayerManager.stop(true)
                backgroundAudioPlayerManager!!.stop(true)
                audioPlayerManager.audioUrl = mp3Path
                audioPlayerManager.start()
            } else {
                if (TextUtils.isEmpty(item.sound_path)) {
                    return
                }
                audioPlayerManager.audioUrl = mp3Path
                audioPlayerManager.start()
                backgroundAudioPlayerManager!!.audioUrl = item.sound_path
                backgroundAudioPlayerManager.play()
            }

            selectorBGMusicId = if (0 == position) 0 else item.id

            onBgMusicChangerListener?.onChanger(
                    binding?.musicProgress1?.progress,
                    binding?.musicProgress2?.progress,
                    selectorBGMusicId
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDialogDismiss() {
        val audioPlayerManager = AudioPlayerManager.getInstance(activity)
        val backgroundAudioPlayerManager = BackgroundAudioPlayerManager.getInstance(activity)

        audioPlayerManager.stop(true)
        backgroundAudioPlayerManager.stop(true)
    }
}