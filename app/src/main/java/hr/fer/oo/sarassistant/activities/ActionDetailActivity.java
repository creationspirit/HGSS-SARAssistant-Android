package hr.fer.oo.sarassistant.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hr.fer.oo.sarassistant.R;
import hr.fer.oo.sarassistant.domain.Action;
import hr.fer.oo.sarassistant.utils.NetworkUtils;

public class ActionDetailActivity extends AppCompatActivity {

    private FloatingActionButton mFabLocate;
    private FloatingActionButton mFabCallLeader;
    private FloatingActionButton mFabShowResucerList;
    private FloatingActionButton mFabFinish;

    private TextView mTitle;
    private TextView mDescription;
    private TextView mLeader;

    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_detail);

        queue = Volley.newRequestQueue(this);

        mTitle = (TextView) findViewById(R.id.action_detail_title);
        mDescription = (TextView) findViewById(R.id.action_detail_description);
        mLeader = (TextView) findViewById(R.id.action_detail_leader_value);

        mFabLocate = (FloatingActionButton) findViewById(R.id.fab_action_location);
        mFabCallLeader = (FloatingActionButton) findViewById(R.id.fab_call_leader);
        mFabShowResucerList = (FloatingActionButton) findViewById(R.id.fab_view_participants);
        mFabFinish = (FloatingActionButton) findViewById(R.id.fab_make_action_unactive);

        Intent intent = getIntent();
        mTitle.setText(intent.getStringExtra("title"));
        mDescription.setText(intent.getStringExtra("description"));
        mLeader.setText(intent.getStringExtra("leader"));

        mFabLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mFabCallLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mFabShowResucerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mFabFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void fetchActionDataAndInflate() {



        JsonArrayRequest jsArrRequest = new JsonArrayRequest
                (Request.Method.GET, NetworkUtils.ACTIONS_LIST_URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsArrRequest);
    }
}
