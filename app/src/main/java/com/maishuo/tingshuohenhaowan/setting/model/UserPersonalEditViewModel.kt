package com.maishuo.tingshuohenhaowan.setting.model

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.text.TextUtils
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.citywheel.CityConfig
import com.lljjcoder.style.citypickerview.CityPickerView
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.api.param.EditorialParam
import com.maishuo.tingshuohenhaowan.api.response.LoginBean
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.bean.UserInfoEvent
import com.maishuo.tingshuohenhaowan.databinding.ActivityUserPersonalEditLayoutBinding
import com.maishuo.tingshuohenhaowan.personal.ui.UserLookBigPicActivity
import com.maishuo.tingshuohenhaowan.setting.ui.HeadImageMakingActivity
import com.maishuo.tingshuohenhaowan.setting.ui.UserPersonalEditActivity
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.maishuo.tingshuohenhaowan.utils.TimeUtils
import com.qichuang.commonlibs.basic.BaseViewModel
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.GlideUtils
import com.qichuang.commonlibs.utils.PreferencesUtils
import com.qichuang.commonlibs.utils.ToastUtil
import com.qichuang.retrofit.CommonObserver
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * author : xpSun
 * date : 9/14/21
 * description :
 */
class UserPersonalEditViewModel constructor(val activity: UserPersonalEditActivity,
                                            val binding: ActivityUserPersonalEditLayoutBinding?) : BaseViewModel() {
    var newAuator: String? = null
    private var newNickname: String? = null
    private var newProvince: String? = null
    private var newCity: String? = null
    private var newbirth: String? = null
    private var newSex: String? = null
    private var cityPickerView: CityPickerView? = null

    fun initPreferences() {
        val oldNickname = PreferencesUtils.getString(PreferencesKey.USER_NAME)
        val oldAuator = PreferencesUtils.getString(PreferencesKey.USER_AVATOR)
        val oldbirth = PreferencesUtils.getString(PreferencesKey.BIRTH_DAY)
        val oldSex = PreferencesUtils.getString(PreferencesKey.SEX, "保密")
        val oldCity = PreferencesUtils.getString(PreferencesKey.CITY)
        val oldProvince = PreferencesUtils.getString(PreferencesKey.PROVINCE)

        PreferencesUtils.putString(PreferencesKey.USER_TEMP_AVATOR, "")

        newNickname = oldNickname
        newAuator = oldAuator
        newbirth = oldbirth
        newSex = oldSex
        newCity = oldCity
        newProvince = oldProvince

        initWidgets()

        cityPickerView = CityPickerView()
        cityPickerView?.init(activity)

        val cityConfig = CityConfig.Builder().build()
        cityPickerView?.setConfig(cityConfig)

        cityPickerView?.setOnCityItemClickListener(object : OnCityItemClickListener() {
            override fun onSelected(province: ProvinceBean?, city: CityBean?, district: DistrictBean?) {
                super.onSelected(province, city, district)

                newProvince = province?.name
                newCity = city?.name
                binding?.userPersonalEditAddress?.text = newProvince + "\t" + newCity
            }
        })
    }

    private fun initWidgets() {
        binding?.let {
            if (!TextUtils.isEmpty(newAuator)) {
                GlideUtils.initImageForHead(
                        activity,
                        newAuator,
                        R.mipmap.home_head_pic_default,
                        it.userPersonalEditHeader
                )
            }

            it.userPersonalEditNick.setText(newNickname ?: "")
            it.userPersonalEditNick.setSelection((newNickname ?: "").length)

            it.userPersonalEditSex.text = newSex ?: ""
            it.userPersonalEditAge.text = newbirth
            it.userPersonalEditAddress.text = String.format("%s\t%s", newCity, newProvince)
        }
    }

    //判断当前是否有修改过
    fun isChanged(): Boolean {
        val oldNickname = PreferencesUtils.getString(PreferencesKey.USER_NAME)
        val oldAuator = PreferencesUtils.getString(PreferencesKey.USER_AVATOR)
        val oldbirth = PreferencesUtils.getString(PreferencesKey.BIRTH_DAY)
        val oldSex = PreferencesUtils.getString(PreferencesKey.SEX, "保密")
        val oldCity = PreferencesUtils.getString(PreferencesKey.CITY)
        val oldProvince = PreferencesUtils.getString(PreferencesKey.PROVINCE)

        return newAuator != oldAuator
                || newNickname != oldNickname
                || newbirth != oldbirth
                || newSex != oldSex
                || newCity != oldCity
                || newProvince != oldProvince
    }


    fun openSelectorPic() {
        val intent = Intent(activity, HeadImageMakingActivity::class.java)
        activity.startActivityForResult(intent, 1)
    }

    fun openBigPic() {
        val oldAuator = PreferencesUtils.getString(PreferencesKey.USER_AVATOR, "")

        showLargePicture(oldAuator)
    }

    fun openSelectorSex() {
        DialogUtils.showSexSelectDialog(activity, newSex) {
            if (it != null) {
                newSex = it.text
                binding?.userPersonalEditSex?.text = newSex
            }
        }
    }

    fun openSelectorAge() {
        initBirthday()
    }

    fun openSelectorAddress() {
        cityPickerView?.showCityPicker()
    }

    fun commonPersonalInfo() {
        if (TextUtils.isEmpty(binding?.userPersonalEditNick?.text)) {
            ToastUtil.showToast("昵称不可为空")
            return
        }

        if (TextUtils.isEmpty(binding?.userPersonalEditSex?.text)) {
            ToastUtil.showToast("性别不可为空")
            return
        }

        if (TextUtils.isEmpty(binding?.userPersonalEditAge?.text)) {
            ToastUtil.showToast("生日不可为空")
            return
        }

        newNickname = binding?.userPersonalEditNick?.text.toString()

        val param = EditorialParam()
        param.avatar = newAuator
        param.name = newNickname
        param.sex = newSex
        param.birth = newbirth
        param.province = newProvince
        param.city = newCity

        ApiService.instance
                .editorial(param)
                .subscribe(object : CommonObserver<LoginBean>() {
                    override fun onResponseSuccess(response: LoginBean?) {
                        //提交成功后保存当前更新信息
                        PreferencesUtils.putString(PreferencesKey.USER_NAME, newNickname)
                        PreferencesUtils.putString(PreferencesKey.USER_AVATOR, newAuator)
                        PreferencesUtils.putString(PreferencesKey.USER_TEMP_AVATOR, newAuator)
                        PreferencesUtils.putString(PreferencesKey.SEX, newSex)
                        PreferencesUtils.putString(PreferencesKey.BIRTH_DAY, newbirth)
                        PreferencesUtils.putString(PreferencesKey.CITY, newCity)
                        PreferencesUtils.putString(PreferencesKey.PROVINCE, newProvince)

                        EventBus.getDefault().post(UserInfoEvent(false, newNickname, newAuator))
                        ToastUtil.showToast(activity.getString(R.string.saved_uccessfully))
                        //保存成功之后退出当前页面
                        activity.finish()
                    }
                })
    }

    /**
     * 展示有保存按钮的大图
     */
    private fun showLargePicture(imagePath: String?) {
        UserLookBigPicActivity.start(activity, imagePath)
    }

    /**
     * 初始化时间选择器
     */
    @SuppressLint("SimpleDateFormat")
    private fun initBirthday() {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(activity,
                { _, year, month, dayOfMonth ->
                    val timer = Calendar.getInstance()
                    timer.set(Calendar.YEAR, year)
                    timer.set(Calendar.MONTH, month)
                    timer.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val nowTime = System.currentTimeMillis()
                    val selectTime = timer.time.time
                    if (selectTime - nowTime > 0) {
                        ToastUtil.showToast(activity.getString(R.string.this_time_cannot_be_greater_than_the_current_time))
                        return@DatePickerDialog
                    }

                    newbirth = TimeUtils.dateFormat(timer.time, TimeUtils.DATE_PATTERN_YYYY_MM_DD)
                    binding?.userPersonalEditAge?.text = newbirth
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
        )
        dialog.show()
    }

}