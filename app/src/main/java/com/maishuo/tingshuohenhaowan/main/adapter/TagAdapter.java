package com.maishuo.tingshuohenhaowan.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.PhonicTagBean;
import com.qichuang.commonlibs.utils.DeviceUtil;

import java.util.List;

public class TagAdapter extends BaseAdapter {
    private int                 w = 0;
    private List<PhonicTagBean> list;
    private Context             context;

    public TagAdapter (List<PhonicTagBean> list, Context context1) {
        this.list = list;
        this.context = context1;
        int width = DeviceUtil.getDeviceWidth();
        int space = DeviceUtil.dip2px(context, 50);
        w = (width - space) / 4;
    }

    @Override
    public int getCount () {
        return this.list.size();
    }

    @Override
    public Object getItem (int position) {
        return position;
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_dialog_phonic_tag_item_layout,
                    null);
            holder.iv_phonic_tag = convertView.findViewById(R.id.iv_phonic_tag);
            holder.layout_rl = convertView.findViewById(R.id.layout_rl);
            holder.tv_phonic_tag_name = convertView.findViewById(R.id.tv_phonic_tag_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_phonic_tag_name.setText(list.get(position).getName());
        holder.layout_rl.setLayoutParams(new LinearLayout.LayoutParams(w, w / 3));
        if (list.get(position).isSelect()) {
            holder.iv_phonic_tag.setImageResource(R.mipmap.classification_bounced_icon_note);
            holder.tv_phonic_tag_name.setPadding(0, 0, DeviceUtil.dip2px(context, 10), 0);
        } else {
            holder.iv_phonic_tag.setImageResource(R.mipmap.classification_bounced_button_black);
            holder.tv_phonic_tag_name.setPadding(0, 0, 0, 0);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView      iv_phonic_tag;
        TextView       tv_phonic_tag_name;
        RelativeLayout layout_rl;
    }
}