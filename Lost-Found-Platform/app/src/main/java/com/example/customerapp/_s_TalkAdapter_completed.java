package com.example.customerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class _s_TalkAdapter_completed extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<_s_TalkItem_lostlist> talkItems;
    ArrayList<HashMap<String, String>> lostList;


    public _s_TalkAdapter_completed(LayoutInflater inflater, ArrayList<_s_TalkItem_lostlist> talkItems, ArrayList<HashMap<String, String>> list) {
        this.inflater = inflater;
        this.talkItems = talkItems;
        this.lostList = list;
    }

    @Override
    public int getCount() {
        return lostList.size();
    }

    @Override
    public Object getItem(int position) {
        return talkItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout._s_completed_list_item, viewGroup, false);

        //while(position!=lostList.size()) {
        if(position<talkItems.size()) {
            ImageView lost_photo = view.findViewById(R.id.lost_photo);
            _s_TalkItem_lostlist sTalkItemLostlist = talkItems.get(position);
            //네트워크에 있는 이미지 읽어오기.
            Glide.with(view).load(sTalkItemLostlist.getImgPath()).into(lost_photo);
        }

        TextView city = view.findViewById(R.id.branch);
        TextView object = view.findViewById(R.id.importance);
        TextView detail = view.findViewById(R.id.lost_object);
        TextView state = view.findViewById(R.id.state);
        TextView registertime = view.findViewById(R.id.registertime);
        TextView staffno = view.findViewById(R.id.staffno);

        city.setText(lostList.get(position).get("branch"));
        object.setText(lostList.get(position).get("lost_object"));
        detail.setText(lostList.get(position).get("lost_object_detail"));
        state.setText(lostList.get(position).get("processing_state"));
        registertime.setText(lostList.get(position).get("registered_time"));
        staffno.setText(lostList.get(position).get("staff_num"));

        return view;
    }
}