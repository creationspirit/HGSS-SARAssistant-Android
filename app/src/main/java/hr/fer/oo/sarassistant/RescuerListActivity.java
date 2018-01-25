package hr.fer.oo.sarassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hr.fer.oo.sarassistant.domain.Rescuer;
import hr.fer.oo.sarassistant.utils.MockData;

public class RescuerListActivity extends AppCompatActivity implements RescuersAdapter.RescuersAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private RescuersAdapter mRescuersAdapter;

    private ArrayList<Rescuer> mReceivedRescuers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescuer_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_rescuers);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRescuersAdapter = new RescuersAdapter(this);
        mRecyclerView.setAdapter(mRescuersAdapter);

        loadRescuerData();
    }

    private void loadRescuerData() {

        mReceivedRescuers = (ArrayList) getIntent().getParcelableArrayListExtra("rescuers");
        mRescuersAdapter.setRescuerData(mReceivedRescuers);
    }

    @Override
    public void onClick(Rescuer rescuerData) {

    }
}
