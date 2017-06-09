package com.marijannovak.littletanks;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by marij on 31.5.2017..
 */

public class SettingsAdapter extends BaseAdapter {

    private ArrayList<SettingItem> settings;

    public SettingsAdapter(ArrayList<SettingItem> settingItems) {

        this.settings = settingItems;

    }

    @Override
    public int getCount() {
        return this.settings.size();
    }

    @Override
    public Object getItem(int position) {
        return this.settings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (settings.get(position).getHasCheck()) {

            final CheckViewHolder checkViewHolder;

            if (convertView == null) {

                LayoutInflater vi = LayoutInflater.from(parent.getContext());
                convertView = vi.inflate(R.layout.check_setting, parent, false);

                checkViewHolder = new CheckViewHolder(convertView);
                checkViewHolder.settingName = (TextView) convertView.findViewById(R.id.settingName);
                checkViewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(checkViewHolder);

            } else
                checkViewHolder = (CheckViewHolder) convertView.getTag();


            SettingItem checkItem = this.settings.get(position);

            if (checkViewHolder.settingName != null) {
                checkViewHolder.settingName.setText(checkItem.getSettingName());
            }

            if (checkViewHolder.checkBox != null && checkItem.getHasCheck()) {
                checkViewHolder.checkBox.setChecked(checkItem.getChecked());
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    checkViewHolder.checkBox.setChecked(!checkViewHolder.checkBox.isChecked());
                    settings.get(position).setChecked(!settings.get(position).getChecked());

                }
            });


        } else if (!settings.get(position).getHasCheck()) {

            ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater vi = LayoutInflater.from(parent.getContext());
                convertView = vi.inflate(R.layout.normal_setting, parent, false);

                viewHolder = new ViewHolder(convertView);
                viewHolder.settingName = (TextView) convertView.findViewById(R.id.settingName2);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            SettingItem item = this.settings.get(position);

            if (viewHolder.settingName != null) {
                viewHolder.settingName.setText(item.getSettingName());
            }

        }


        return convertView;

    }

    public boolean getSoundStatus()
    {
       return settings.get(0).getChecked();
    }

    public boolean getSensorStatus()
    {
        return settings.get(1).getChecked();
    }

    public void updateDiff(int current) {

        if(current == 1)
            settings.get(2).setSettingName("Difficulty: Easy");
        else if(current == 2)
            settings.get(2).setSettingName("Difficulty: Medium");
        else if(current == 3)
            settings.get(2).setSettingName("Difficulty: Hard");

        this.notifyDataSetChanged();
    }

    private static class CheckViewHolder {

        TextView settingName;
        CheckBox checkBox;

        public CheckViewHolder(View checkView) {
            settingName = (TextView) checkView.findViewById(R.id.settingName);
            checkBox = (CheckBox) checkView.findViewById(R.id.checkBox);
        }
    }

    private static class ViewHolder {

        TextView settingName;

        public ViewHolder(View view) {
            settingName = (TextView) view.findViewById(R.id.settingName2);
        }
    }

}


