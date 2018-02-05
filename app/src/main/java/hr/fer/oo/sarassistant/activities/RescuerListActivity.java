package hr.fer.oo.sarassistant.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hr.fer.oo.sarassistant.R;
import hr.fer.oo.sarassistant.adapter.RescuersAdapter;
import hr.fer.oo.sarassistant.domain.Rescuer;

public class RescuerListActivity extends AppCompatActivity implements RescuersAdapter.RescuersAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private RescuersAdapter mRescuersAdapter;
    private double mUserLatitude;
    private double mUserLongitude;

    private ArrayList<Rescuer> mReceivedRescuers;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescuer_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_rescuers);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRescuersAdapter = new RescuersAdapter(this);
        mRecyclerView.setAdapter(mRescuersAdapter);

        //Intent receivedIntent = getIntent();
        //mUserLatitude = receivedIntent.getDoubleExtra("my_lat", Double.NaN);
        //mUserLongitude = receivedIntent.getDoubleExtra("my_lon", Double.NaN);

        //loadRescuerData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        mLastKnownLocation = task.getResult();
                        //mUserLatitude = mLastKnownLocation.getLatitude();
                        //mUserLongitude = mLastKnownLocation.getLongitude();
                    }
                    loadRescuerData();
                }
            });
        }
    }

    private void loadRescuerData() {

        mReceivedRescuers = (ArrayList) getIntent().getParcelableArrayListExtra("rescuers");
        for(Rescuer r : mReceivedRescuers) {
            r.setDistanceFrom(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
        }

        Collections.sort(mReceivedRescuers, new RescuerComparator());

       /* Log.d("lat", mUserLatitude+"");
        Log.d("lat", mUserLongitude+"");
        for(Rescuer r : mReceivedRescuers) {
            Log.d("spasavatelji_test", r.toString());
        } */

        mRescuersAdapter.setRescuerData(mReceivedRescuers);
    }

    @Override
    public void onClick(Rescuer rescuerData) {
        Intent intent = new Intent(this, RescuerDetailActivity.class);
        intent.putExtra("id", rescuerData.getId());
        intent.putExtra("fullName", rescuerData.getFullName());
        intent.putExtra("distance", rescuerData.getCachedDistance());

        startActivity(intent);
    }

    public class RescuerComparator implements Comparator<Rescuer>
    {
        public int compare(Rescuer first, Rescuer second) {
            float first_dist = first.getCachedDistance();
            float second_dist = second.getCachedDistance();
            return (int) (first_dist - second_dist);
        }
    }
}
