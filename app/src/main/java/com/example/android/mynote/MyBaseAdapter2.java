package com.example.android.mynote;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyBaseAdapter2 extends BaseAdapter {

    private List<String> inf;
    private Context mContext;

    public MyBaseAdapter2(List<String> inf,Context mContext) {
        this.inf=inf;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {     //表示列表中的个数 ,即inf中的个数
        return inf.size();
    }

    @Override
    public Object getItem(int position) {        //获取指定位置的东西
        return inf.get(position);
    }

    @Override
    public long getItemId(int position) {        //获取指定位置物品的id,此处原样返回即可
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {    //所有的逻辑书写都在这里

        //将lista与该view绑定,之后只要return 该view即可
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menulistlayout,null);


//        //表示状态为已登录
//        if(position==0&&MainActivity.ifLogin==1){
//            return null;
//        }

        TextView textView2=view.findViewById(R.id.textView8);

        textView2.setText(inf.get(position));

        //TextView textView=view.findViewById(R.id.view5);
       // textView.setText((CharSequence) inf.get(position).get("name"));

        ImageView imageView=view.findViewById(R.id.imageView2);


        Resources resources = mContext.getResources();
        if(inf.get(position)=="登录") {
            Drawable drawable = resources.getDrawable(R.drawable.login_black_24dp);
            imageView.setImageDrawable(drawable);
        }
        else if(inf.get(position)=="同步"){
            Drawable drawable = resources.getDrawable(R.drawable.ic_sync_black_24dp);
            imageView.setImageDrawable(drawable);
        }


        // constraintLayout.getBackground()

        return view;
    }



}
