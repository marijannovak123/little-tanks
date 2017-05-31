package com.marijannovak.littletanks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import static java.security.AccessController.getContext;

/**
 * Created by marij on 31.5.2017..
 */

public class SettingsAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

/*
        int viewType = this.getItemViewType(position);

        switch(viewType)
        {
            case TYPE1:

                Type1Holder holder1;

                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.layout_mylistlist_item_type_1, parent, false);

                    holder1 = new Type1Holder ();
                    holder1.text = (TextView) v.findViewById(R.id.mylist_itemname);
                    v.setTag(holder1);
                }
                else {
                    holder1 = (Type1Holder)v.getTag();
                }

                MyListItem myItem = m_items.get(position);

                // set up the list item
                if (myItem != null) {
                    // set item text
                    if (holder1.text != null) {
                        holder1.text.setText(myItem.getItemName());
                    }
                }

                // return the created view
                return v;


            case TYPE2:
                Type2Holder holder2;

                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.layout_mylistlist_item_type_2, parent, false);

                    holder2 = new Type2Holder ();
                    holder2.text = (TextView) v.findViewById(R.id.mylist_itemname);
                    holder2.icon = (ImageView) v.findViewById(R.id.mylist_itemicon);
                    v.setTag(holder1);
                }
                else {
                    holder2 = (Type2Holder)v.getTag();
                }

                MyListItem myItem = m_items.get(position);

                // set up the list item
                if (myItem != null) {
                    // set item text
                    if (holder2.text != null) {
                        holder2.text.setText(myItem.getItemName());
                    }

                    if(holder2.icon != null)
                        holder2.icon.setDrawable(R.drawable.icon1);
                }


                // return the created view
                return v;


            default:
                //Throw exception, unknown data type
        }*/

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }
}
