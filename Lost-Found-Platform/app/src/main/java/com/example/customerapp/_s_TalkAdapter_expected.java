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

public class _s_TalkAdapter_expected extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<_s_TalkItem_lostlist> talkItems;
    ArrayList<HashMap<String, String>> lostList;


    public _s_TalkAdapter_expected(LayoutInflater inflater, ArrayList<_s_TalkItem_lostlist> talkItems, ArrayList<HashMap<String, String>> list) {
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

        view = inflater.inflate(R.layout._s_expected_list_item, viewGroup, false);

        //while(position!=lostList.size()) {
        if(position<talkItems.size()) {
            ImageView lost_photo = view.findViewById(R.id.lost_photo);
            _s_TalkItem_lostlist sTalkItemLostlist = talkItems.get(position);
            //네트워크에 있는 이미지 읽어오기.
            Glide.with(view).load(sTalkItemLostlist.getImgPath()).into(lost_photo);
        }

        //R.id.lost_num, R.id.mark,R.id.customer_name, R.id.phone_num, R.id.lost_object, R.id.lost_object_detail, R.id.receiving_date, R.id.receiving_time}
        TextView lost_num = view.findViewById(R.id.lost_num);
        TextView mark = view.findViewById(R.id.mark);
        TextView customer_name = view.findViewById(R.id.customer_name);
        TextView phone_num = view.findViewById(R.id.phone_num);
        TextView lost_object = view.findViewById(R.id.lost_object);
        TextView receiving_date = view.findViewById(R.id.receiving_date);
        TextView receiving_time = view.findViewById(R.id.receiving_time);


        lost_num.setText(lostList.get(position).get("lost_num"));
        mark.setText(lostList.get(position).get("mark"));
        customer_name.setText(lostList.get(position).get("customer_name"));
        phone_num.setText(lostList.get(position).get("phone_num"));
        lost_object.setText(lostList.get(position).get("lost_object"));
        receiving_date.setText(lostList.get(position).get("receiving_date"));
        receiving_time.setText(lostList.get(position).get("receiving_time"));


        return view;
    }
}