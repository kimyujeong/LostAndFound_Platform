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

public class _s_TalkAdapter_expire extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<_s_TalkItem_lostlist> talkItems;
    ArrayList<HashMap<String, String>> lostList;


    public _s_TalkAdapter_expire(LayoutInflater inflater, ArrayList<_s_TalkItem_lostlist> talkItems, ArrayList<HashMap<String, String>> list) {
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

        view = inflater.inflate(R.layout._s_expire_list_item, viewGroup, false);

        //while(position!=lostList.size()) {
        if(position<talkItems.size()) {
        //if (!talkItems.isEmpty()) {

        ImageView lost_photo = view.findViewById(R.id.lost_photo);
        _s_TalkItem_lostlist sTalkItemLostlist = talkItems.get(position);
        //네트워크에 있는 이미지 읽어오기.
        Glide.with(view).load(sTalkItemLostlist.getImgPath()).into(lost_photo);
        }

        //{R.id.lno, R.id.object, R.id.site, R.id.state, R.id.staffno, R.id.detail,R.id.expired}
        TextView lno = view.findViewById(R.id.lno);
        TextView object = view.findViewById(R.id.object);
        TextView site = view.findViewById(R.id.site);
        TextView state = view.findViewById(R.id.state);
        TextView staffno = view.findViewById(R.id.staffno);
        TextView detail = view.findViewById(R.id.detail);
        TextView expired = view.findViewById(R.id.expired);


        lno.setText(lostList.get(position).get("lost_num"));
        object.setText(lostList.get(position).get("lost_object"));
        site.setText(lostList.get(position).get("cinema_num"));
        state.setText(lostList.get(position).get("processing_state"));
        staffno.setText(lostList.get(position).get("staff_num"));
        detail.setText(lostList.get(position).get("lost_object_detail"));
        expired.setText(lostList.get(position).get("expired_date"));


        return view;
    }

}