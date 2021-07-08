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

public class _s_TalkAdapter_approve extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<_s_TalkItem_lostlist> talkItems;
    ArrayList<HashMap<String, String>> lostList;


    public _s_TalkAdapter_approve(LayoutInflater inflater, ArrayList<_s_TalkItem_lostlist> talkItems, ArrayList<HashMap<String, String>> list) {
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

        view = inflater.inflate(R.layout._s_approve_list_item, viewGroup, false);

        if(position<talkItems.size()) {
            ImageView lost_photo = view.findViewById(R.id.lost_photo);
            _s_TalkItem_lostlist sTalkItemLostlist = talkItems.get(position);
            //네트워크에 있는 이미지 읽어오기.
            Glide.with(view).load(sTalkItemLostlist.getImgPath()).into(lost_photo);
            /*String path=talkItems.get(position).getImgPath();
            Glide.with(view).load(path).into(lost_photo);*/
        }
        TextView lost_num = view.findViewById(R.id.lost_num);
        TextView lost_object = view.findViewById(R.id.lost_object);
        TextView registered_date = view.findViewById(R.id.registered_date);

        lost_num.setText(lostList.get(position).get("lost_num"));
        lost_object.setText(lostList.get(position).get("lost_object"));
        registered_date.setText(lostList.get(position).get("registered_date"));

        return view;
    }
}