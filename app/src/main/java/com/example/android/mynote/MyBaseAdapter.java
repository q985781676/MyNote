package com.example.android.mynote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyBaseAdapter extends BaseAdapter {

    private List<Map<String,?>> inf;

    public MyBaseAdapter(List<Map<String,?>> inf) {
        this.inf=inf;
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listlayout,null);


        TextView textView=view.findViewById(R.id.textView4);
        textView.setText((CharSequence) inf.get(position).get("name"));
        TextView textView2=view.findViewById(R.id.textView5);
        textView2.setText((CharSequence) inf.get(position).get("date"));
        TextView textView3=view.findViewById(R.id.textView6);
        textView3.setText((CharSequence) inf.get(position).get("information"));


      //  ConstraintLayout constraintLayout=view.findViewById(R.id.list_constraints);

//        int color1=Color.rgb(227, 231, 218);
//        int color2=Color.rgb(219, 227, 240);
//
//
//        if(position%2==0)
//             constraintLayout.setBackgroundColor(color1);
//        else if(position%2!=0){
//            constraintLayout.setBackgroundColor(color2);
//        }

       // constraintLayout.getBackground()

        return view;
    }



}
