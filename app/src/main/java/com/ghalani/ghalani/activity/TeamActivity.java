package com.ghalani.ghalani.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ghalani.ghalani.R;
import com.ghalani.ghalani.app.Config;
import com.ghalani.ghalani.app.MyApplication;
import com.ghalani.ghalani.helper.PrefManager;
import com.ghalani.ghalani.helper.TextLogHelper;
import com.ghalani.ghalani.item.task.TeamTask;
import com.ghalani.ghalani.item.task.TeamTaskListAdapter;
import com.ghalani.ghalani.item.team.Team;
import com.ghalani.ghalani.item.team.TeamListAdapter;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeamActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    TextView teamNameTv;
    private PrefManager pref;
    Team tm;
    private ArrayList<TeamTask> ttList;
    private TeamTaskListAdapter adapter;
    private ListView ttListView;
    public static ProgressBar progressBar;
    FloatingActionButton reloadBut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        getSupportActionBar().setTitle("Activity list");  // provide compatibility to all the versions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = new PrefManager(getApplicationContext());
        Bundle b = getIntent().getExtras();
        tm = (Team) b.getSerializable("team_detail");
        teamNameTv = (TextView)findViewById(R.id.team_name_tv);
        teamNameTv.setText(tm.getName());

        reloadBut = (FloatingActionButton) findViewById(R.id.reload_but);

        ttList = new ArrayList<TeamTask>();
        adapter = new TeamTaskListAdapter(this, ttList);
        ttListView = (ListView) findViewById(R.id.team_task_list);
        ttListView.setAdapter(adapter);
        ttListView.setOnItemClickListener(this);

        progressBar = (ProgressBar)findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        TextLogHelper.log("Activity: TeamActivity");
        getTeamDetails();
    }

    private void getTeamDetails(){
        showDialog();
        String url = Config.URL_ROOT+"/teams/"+tm.getId()+"?access_token="+pref.getAccessToken();
        TextLogHelper.log("REQUEST: " + url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                TextLogHelper.log(response.toString());
                hideDialog();
                showReload(false);
                try {
                    JSONObject responseObj = new JSONObject(response);
                    JSONArray tasks = responseObj.getJSONObject("team").getJSONArray("activities");
                    //teamList.clear();
                    int i = 0;
                    while (i < tasks.length()){
                        JSONObject t = tasks.getJSONObject(i);
                        TeamTask tt = new TeamTask(t.getString("id"),
                            t.getJSONObject("activity").getString("name"),
                            t.getJSONObject("activity").getString("description"),
                            t.getString("comment"),
                            t.getString("start_date"),
                            t.getString("end_date"),
                            t.getJSONObject("farm").toString(),
                            t.getJSONObject("activity").getJSONArray("fields").toString());
                        ttList.add(tt);
                        i++;
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    TextLogHelper.log(e.getMessage());
                    //TextLogHelper.toast(getApplicationContext(), "Reload: Unable to connect to server", true);
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                TextLogHelper.log("Error: " + error.getMessage());
                TextLogHelper.toast(TeamActivity.this, "Reload: Unable to connect to server", false);
                showReload(true);
                hideDialog();
            }
        });

        int socketTimeout = 15000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Intent intent = new Intent(this, TeamTaskActivity.class);
        TeamTask tt = ttList.get(position);
        Bundle b = new Bundle();
        b.putSerializable("team_task_detail", tt);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void showDialog(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void hideDialog(){
        progressBar.setVisibility(View.GONE);
    }

    public void showReload(Boolean show){
        if (show){
            reloadBut.setVisibility(View.VISIBLE);
        } else {
            reloadBut.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reload_but:
                getTeamDetails();
                break;
        }
    }
}
