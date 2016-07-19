package com.ghalani.ghalani.item.task;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ghalani.ghalani.R;
import com.ghalani.ghalani.item.team.Team;

import java.util.List;

/**
 * Created by Amanze on 7/18/2016.
 */
public class TeamTaskListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<TeamTask> teamTaskItems;

    public TeamTaskListAdapter(Activity activity, List<TeamTask> teamTaskItems) {
        this.activity = activity;
        this.teamTaskItems = teamTaskItems;
    }

    @Override
    public int getCount() {
        return teamTaskItems.size();
    }

    @Override
    public Object getItem(int location) {
        return teamTaskItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TeamTask t = teamTaskItems.get(position);
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.team_task_list_item, null);
        TextView name = (TextView) convertView.findViewById(R.id.team_task_name);
        TextView start = (TextView) convertView.findViewById(R.id.team_task_start);
        TextView end = (TextView) convertView.findViewById(R.id.team_task_end);

        name.setText(t.getName());
        start.setText(t.getStart());
        end.setText(t.getEnd());

        return convertView;
    }
}
