package com.maishuo.tingshuohenhaowan.setting.ui

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.api.param.SaveContentHobbiesParam
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityInterestLayoutBinding
import com.maishuo.tingshuohenhaowan.api.response.InterestBean
import com.qichuang.commonlibs.utils.ToastUtil
import com.qichuang.retrofit.CommonObserver
import java.util.*

/**
 * author : xpSun
 * date : 9/13/21
 * description : 兴趣设置界面
 */
class InterestActivity : CustomBaseActivity<ActivityInterestLayoutBinding>() {

    private var mDataList: MutableList<InterestBean> = ArrayList()

    override fun initView() {
        setTitle("兴趣设置")

        vb?.let {
            it.tvInterestOne.visibility = View.INVISIBLE
            it.tvInterestTwo.visibility = View.INVISIBLE
            it.tvInterestThree.visibility = View.INVISIBLE
            it.tvInterestFour.visibility = View.INVISIBLE
            it.tvInterestFive.visibility = View.INVISIBLE
            it.tvInterestSix.visibility = View.INVISIBLE
            it.tvInterestSeven.visibility = View.INVISIBLE

            it.tvInterestOne.setOnClickListener(this)
            it.tvInterestTwo.setOnClickListener(this)
            it.tvInterestThree.setOnClickListener(this)
            it.tvInterestFour.setOnClickListener(this)
            it.tvInterestFive.setOnClickListener(this)
            it.tvInterestSix.setOnClickListener(this)
            it.tvInterestSeven.setOnClickListener(this)
            it.btInterestUp.setOnClickListener(this)
        }
    }

    override fun initData() {
        ApiService.instance
                .hobbiesList()
                .subscribe(object : CommonObserver<MutableList<InterestBean>>() {
                    override fun onResponseSuccess(response: MutableList<InterestBean>?) {
                        if (response != null) {
                            mDataList = response
                            if (!mDataList.isNullOrEmpty()) {
                                setData()
                            }
                        }
                    }
                })
    }

    /**
     * 界面展示,设置数据
     */
    private fun setData() {
        if (mDataList.isNullOrEmpty()) {
            return
        }

        vb?.let {
            for (i in mDataList.indices) {
                val bean = mDataList[i]
                val name = bean.name
                val choice = bean.isChoice
                when (i) {
                    0 -> {
                        it.tvInterestOne.text = name
                        it.tvInterestOne.isSelected = choice
                        it.tvInterestOne.visibility = View.VISIBLE
                    }
                    1 -> {
                        it.tvInterestTwo.text = name
                        it.tvInterestTwo.isSelected = choice
                        it.tvInterestTwo.visibility = View.VISIBLE
                    }
                    2 -> {
                        it.tvInterestThree.text = name
                        it.tvInterestThree.isSelected = choice
                        it.tvInterestThree.visibility = View.VISIBLE
                    }
                    3 -> {
                        it.tvInterestFour.text = name
                        it.tvInterestFour.isSelected = choice
                        it.tvInterestFour.visibility = View.VISIBLE
                    }
                    4 -> {
                        it.tvInterestFive.text = name
                        it.tvInterestFive.isSelected = choice
                        it.tvInterestFive.visibility = View.VISIBLE
                    }
                    5 -> {
                        it.tvInterestSix.text = name
                        it.tvInterestSix.isSelected = choice
                        it.tvInterestSix.visibility = View.VISIBLE
                    }
                    6 -> {
                        it.tvInterestSeven.text = name
                        it.tvInterestSeven.isSelected = choice
                        it.tvInterestSeven.visibility = View.VISIBLE
                    }
                    else -> {

                    }
                }
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        if (mDataList.isNullOrEmpty()) {
            return
        }

        when (v.id) {
            R.id.tv_interest_one -> {
                val bean1 = mDataList[0]
                val choice1 = bean1.isChoice
                vb!!.tvInterestOne.isSelected = !choice1
                bean1.isChoice = !choice1
                mDataList[0] = bean1
            }
            R.id.tv_interest_two -> {
                val bean2 = mDataList[1]
                val choice2 = bean2.isChoice
                vb!!.tvInterestTwo.isSelected = !choice2
                bean2.isChoice = !choice2
                mDataList[1] = bean2
            }
            R.id.tv_interest_three -> {
                val bean3 = mDataList[2]
                val choice3 = bean3.isChoice
                vb!!.tvInterestThree.isSelected = !choice3
                bean3.isChoice = !choice3
                mDataList[2] = bean3
            }
            R.id.tv_interest_four -> {
                val bean4 = mDataList[3]
                val choice4 = bean4.isChoice
                vb!!.tvInterestFour.isSelected = !choice4
                bean4.isChoice = !choice4
                mDataList[3] = bean4
            }
            R.id.tv_interest_five -> {
                val bean5 = mDataList[4]
                val choice5 = bean5.isChoice
                vb!!.tvInterestFive.isSelected = !choice5
                bean5.isChoice = !choice5
                mDataList[4] = bean5
            }
            R.id.tv_interest_six -> {
                val bean6 = mDataList[5]
                val choice6 = bean6.isChoice
                vb!!.tvInterestSix.isSelected = !choice6
                bean6.isChoice = !choice6
                mDataList[5] = bean6
            }
            R.id.tv_interest_seven -> {
                val bean7 = mDataList[6]
                val choice7 = bean7.isChoice
                vb!!.tvInterestSeven.isSelected = !choice7
                bean7.isChoice = !choice7
                mDataList[6] = bean7
            }
            R.id.bt_interest_up -> {
                if (!mDataList.isNullOrEmpty()) {
                    var typeIds = ""
                    for (bean in mDataList.iterator()) {
                        val choice = bean.isChoice
                        val typeId = bean.typeId
                        if (choice) {
                            typeIds = if (TextUtils.isEmpty(typeIds)) {
                                typeId.toString() + ""
                            } else {
                                "$typeIds,$typeId"
                            }
                        }
                    }
                    if (TextUtils.isEmpty(typeIds)) {
                        ToastUtil.showToast("请选择感兴趣的内容")
                        return
                    }

                    //网络保存
                    saveInterest(typeIds)
                }
            }
            else -> {

            }
        }
    }

    /**
     * 保存兴趣
     */
    private fun saveInterest(typeIds: String) {
        val param = SaveContentHobbiesParam()
        param.typeId = typeIds
        ApiService.instance
                .saveContentHobbies(param)
                .subscribe(object : CommonObserver<Any>() {
                    override fun onResponseSuccess(response: Any?) {
                        ToastUtil.showToast("设置成功")
                        finish()
                    }
                })
    }
}