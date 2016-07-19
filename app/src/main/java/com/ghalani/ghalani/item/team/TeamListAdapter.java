package com.ghalani.ghalani.item.team;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ghalani.ghalani.R;

import java.util.List;

/**
 * Created by Amanze on 7/18/2016.
 */
public class TeamListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Team> teamItems;

    public TeamListAdapter(Activity activity, List<Team> teamItems) {
        this.activity = activity;
        this.teamItems = teamItems;
    }

    @Override
    public int getCount() {
        return teamItems.size();
    }

    @Override
    public Object getItem(int location) {
        return teamItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Team t = teamItems.get(position);
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.team_list_item, null);
        TextView name = (TextView) convertView.findViewById(R.id.team_list_name);

        name.setText(t.getName());

        return convertView;
    }
}
