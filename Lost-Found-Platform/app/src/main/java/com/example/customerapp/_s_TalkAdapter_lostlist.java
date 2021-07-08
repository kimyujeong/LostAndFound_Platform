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

public class _s_TalkAdapter_lostlist extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<_s_TalkItem_lostlist> sTalkItemLostlists;
    ArrayList<HashMap<String, String>> lostList;


    public _s_TalkAdapter_lostlist(LayoutInflater inflater, ArrayList<_s_TalkItem_lostlist> sTalkItemLostlists, ArrayList<HashMap<String, String>> list) {
        this.inflater = inflater;
        this.sTalkItemLostlists = sTalkItemLostlists;
        this.lostList = list;
    }

    @Override
    public int getCount() {
        return lostList.size();
    }

    @Override
    public Object getItem(int position) {
        return sTalkItemLostlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout._s_lost_list_item, viewGroup, false);

        //while(position!=lostList.size()) {
        if(position<sTalkItemLostlists.size()) {
            ImageView lost_photo = view.findViewById(R.id.lost_photo);
            _s_TalkItem_lostlist sTalkItemLostlist = sTalkItemLostlists.get(position);
            //네트워크에 있는 이미지 읽어오기.
            Glide.with(view).load(sTalkItemLostlist.getImgPath()).into(lost_photo);
        }

        TextView branch = view.findViewById(R.id.branch);
        TextView imp = view.findViewById(R.id.importance);
        TextView object = view.findViewById(R.id.lost_object);
        TextView detail = view.findViewById(R.id.lost_object_detail);
        TextView state = view.findViewById(R.id.processing_state);
        TextView time = view.findViewById(R.id.registered_time);
        TextView date = view.findViewById(R.id.expired_date);
        TextView staff_num = view.findViewById(R.id.staff_num);



        branch.setText(lostList.get(position).get("branch"));
        imp.setText(lostList.get(position).get("importance"));
        object.setText(lostList.get(position).get("lost_object"));
        detail.setText(lostList.get(position).get("lost_object_detail"));
        state.setText(lostList.get(position).get("processing_state"));
        time.setText(lostList.get(position).get("registered_time"));
        date.setText(lostList.get(position).get("expired_date"));
        staff_num.setText(lostList.get(position).get("staff_name"));

        return view;
    }
}