package hr.fer.oo.sarassistant.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hr.fer.oo.sarassistant.R;
import hr.fer.oo.sarassistant.adapter.ActionsAdapter;
import hr.fer.oo.sarassistant.domain.Action;
import hr.fer.oo.sarassistant.utils.NetworkUtils;

public class ActionListActivity extends AppCompatActivity implements ActionsAdapter.ActionsAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private ActionsAdapter mActionsAdapter;

    private ArrayList<Action> mReceivedActions;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_list);

        queue = Volley.newRequestQueue(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_actions);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mActionsAdapter = new ActionsAdapter(this);
        mRecyclerView.setAdapter(mActionsAdapter);
        fetchActionDataAndAddToAdapter();


    }

    @Override
    public void onClick(Action action) {
        Intent intent = new Intent(this, ActionDetailActivity.class);
        intent.putExtra("title", action.getTitle());
        intent.putExtra("leader", action.getLeaderName());
        intent.putExtra("description", action.getDescription());
        startActivity(intent);
    }

    public void fetchActionDataAndAddToAdapter() {

        mReceivedActions = new ArrayList<>();
        JsonArrayRequest jsArrRequest = new JsonArrayRequest
                (Request.Method.GET, NetworkUtils.ACTIONS_LIST_URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonAction = response.getJSONObject(i);
                                String title = jsonAction.getString("name");
                                String description = jsonAction.getString("description").trim();
                                boolean active = jsonAction.getBoolean("active");

                                JSONObject leader = jsonAction.getJSONObject("leader");
                                String leaderName = leader.getString("name");
                                Long leaderID = leader.getLong("id");

                                Action action = new Action(title, description, leaderID, leaderName, active);
                                mReceivedActions.add(action);
                                Log.d("Action", action.toString());
                            }
                            Log.d("ActionList", mReceivedActions.toString());
                            mActionsAdapter.setActionData(mReceivedActions);

                        } catch(JSONException e) {
                            Log.e("JSON Action", e.getMessage());
                            }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsArrRequest);
    }
}
