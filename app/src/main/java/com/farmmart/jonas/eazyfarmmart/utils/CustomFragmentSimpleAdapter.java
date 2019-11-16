package com.farmmart.jonas.eazyfarmmart.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.farmmart.jonas.eazyfarmmart.R;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by JONAS on 2/25/2019.
 */

public class CustomFragmentSimpleAdapter extends SimpleAdapter {

    private List<Map<String, Object>> itemList;
    private Context mContext;
    private String[] mFrom;
    private int [] mTo;


    public CustomFragmentSimpleAdapter(Context context, List<? extends Map<String, ?>> data,
                                       int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

        this.itemList = (List<Map<String, Object>>) data;
        this.mContext = context;
        this.mFrom = from;
        this.mTo = to;
    }

    /* A Static class for holding the elements of each List View Item
     * This is created as per Google UI Guideline for faster performance */
    class ViewHolder {
        TextView textCat;
        TextView textName;
        TextView textPrice;
        TextView textDate;
        TextView textBotBuy;
//        ImageView imageArrow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_trans_sales, null);
            holder = new ViewHolder();

            // get the textview's from the convertView
            holder.textCat = (TextView)   convertView.findViewById(R.id.cat);
            holder.textName = (TextView) convertView.findViewById(R.id.name);
            holder.textPrice = (TextView) convertView.findViewById(R.id.price);
            holder.textDate = (TextView) convertView.findViewById(R.id.qty);
            holder.textBotBuy = (TextView) convertView.findViewById(R.id.botBuy);
            // store it in a Tag as its the first time this view is generated
            convertView.setTag(holder);
        } else {
            /* get the View from the existing Tag */
            holder = (ViewHolder) convertView.getTag();
        }
        System.out.println("itemList.size::"+ itemList.size());



        System.out.println("itemList.get(position).values.entrySet()::"+ itemList.get(position).entrySet());
        Set<Map.Entry<String, Object>> setOfEntries = itemList.get(position).entrySet();
        for(Map.Entry<String, Object> entry : setOfEntries){
            System.out.println("Key : "  + entry.getKey() + "\t\t" +
                    "Value : "  + entry.getValue());

            if (entry.getKey().contains("sellprice")) {
                System.out.println("entry for sellprice::"+ entry.getValue());
                String value = entry.getValue().toString();

                if(!value.contains("-")){
                    holder.textPrice.setText(entry.getValue().toString());
                    holder.textPrice.setTextColor(mContext.getResources().getColor(R.color.darkgreen));
                }
            }
            if (entry.getKey().contains("category")){
                System.out.println("entry for category::"+ entry.getValue());
                holder.textCat.setText(entry.getValue().toString());
            }
            if (entry.getKey().contains("name")){
                System.out.println("entry for name::"+ entry.getValue());
                holder.textName.setText(entry.getValue().toString());
            }
            if (entry.getKey().contains("dateSold")){
                System.out.println("entry for dateSold::"+ entry.getValue());

                holder.textDate.setText(entry.getValue().toString());
            }
            if (entry.getKey().contains("boughtBuy")){
                System.out.println("entry for boughtBuy::"+ entry.getValue());

                holder.textBotBuy.setText(entry.getValue().toString());
            }
        }


//        holder.textSummary.setText((CharSequence) itemList.get(0));
//        holder.textDate.setText((CharSequence) itemList.get(1));
//        holder.textAmt.setText((CharSequence) itemList.get(2));
//        holder.textBranch.setText((CharSequence) itemList.get(3));
//        /* update the textView's text and color of list item */
//
//        if(itemList.get(2).toString().contains("-")){
//            holder.textAmt.setTextColor(mContext.getResources().getColor(R.color.negative_bal));
//
//        }else{
//            holder.textAmt.setTextColor(mContext.getResources().getColor(R.color.darkgreen));
//
//        }



        return convertView;
    }

}
