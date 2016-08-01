package com.ghalani.ghalani.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.ghalani.ghalani.item.team.Team;
import com.ghalani.ghalani.item.team.TeamListAdapter;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Toolbar toolbar;
    private PrefManager pref;
    private TextView name, email, mobile;
    private TeamListAdapter adapter;
    ListView teamListView;
    private List<Team> teamList;
    ProgressBar progressBar;
    FloatingActionButton reloadBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //getActionBar().setTitle("Hello world App");
        getSupportActionBar().setTitle("Home");  // provide compatibility to all the versions

        name = (TextView) findViewById(R.id.name);
        mobile = (TextView) findViewById(R.id.mobile);
        reloadBut = (FloatingActionButton) findViewById(R.id.reload_but);

        // enabling toolbar
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        pref = new PrefManager(getApplicationContext());

        // Checking if user session
        // if not logged in, take user to sms screen
        if (!pref.isLoggedIn()) {
            logout();
        }
        progressBar = (ProgressBar)findViewById(R.id.progress);
        ThreeBounce tb = new ThreeBounce(); // https://android-arsenal.com/details/1/3305
        progressBar.setIndeterminateDrawable(tb);
        initTeams();

        // Displaying user information from shared preferences
        HashMap<String, String> profile = pref.getUserDetails();
        name.setText("Name: " + profile.get("name"));
        mobile.setText("Mobile: " + profile.get("mobile"));
        teamList = new ArrayList<Team>();
        adapter = new TeamListAdapter(this, teamList);

        teamListView = (ListView) findViewById(R.id.team_list_main);
        teamListView.setAdapter(adapter);
        teamListView.setOnItemClickListener(this);


    }

    private void initTeams() {
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Config.URL_ROOT+"/service_providers/"+pref.getSPId()+"?access_token="+pref.getAccessToken(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                TextLogHelper.log(response.toString());
                showReload(false);

                try {
                    JSONObject responseObj = new JSONObject(response);
                    JSONArray teams = responseObj.getJSONObject("service_provider").getJSONArray("teams");
                    teamList.clear();
                    int i = 0;
                    while (i < teams.length()){
                        JSONObject t = teams.getJSONObject(i);
                        Team tm = new Team(t.getString("id"), t.getString("name"), t.getJSONObject("manager").getString("id"));
                        teamList.add(tm);
                        i++;
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    //hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                TextLogHelper.log("Error: " + error.getMessage());
                TextLogHelper.toast(MainActivity.this, "Reload: Unable to connect to server", true);
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

    /**
     * Logging out user
     * will clear all user shared preferences and navigate to
     * sms activation screen
     */
    private void logout() {
        pref.clearSession();
        Intent intent = new Intent(MainActivity.this, SmsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextLogHelper.log("Clicked: 1");
        final Intent intent = new Intent(this, TeamActivity.class);
        Team hm = teamList.get(position);
        Bundle b = new Bundle();
        b.putSerializable("team_detail", hm);
        TextLogHelper.log("Clicked: After serialize");
        intent.putExtras(b);
        TextLogHelper.log("Clicked: "+ hm.getName());
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
                initTeams();
                break;
        }
    }
}
