package hr.fer.oo.sarassistant.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import hr.fer.oo.sarassistant.R;
import hr.fer.oo.sarassistant.utils.NetworkUtils;

public class RescuerDetailActivity extends AppCompatActivity {

    private FloatingActionButton mFabLocate;
    private FloatingActionButton mFabCall;
    private FloatingActionButton mFabSms;

    private TextView mPhoneNumber1;
    private TextView mPhoneNumber2;
    private TextView mPhoneNumber2Label;

    private TextView mDistance;
    private TextView mCategory;
    private TextView mAddress;


    private AlertDialog.Builder mPhoneNumberDialogBuilder;

    private TextView mFullName;

    private boolean mCallPermissionGranted;
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 1;
    private String selectedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescuer_detail);

        RequestQueue queue = Volley.newRequestQueue(this);

        mFullName = (TextView) findViewById(R.id.detail_fullname);
        mAddress = (TextView) findViewById(R.id.detail_address_value);
        mCategory = (TextView) findViewById(R.id.detail_category);
        mDistance = (TextView) findViewById(R.id.detail_distance);

        mPhoneNumber1 = (TextView) findViewById(R.id.detail_mobile1_value);
        mPhoneNumber2 = (TextView) findViewById(R.id.detail_mobile2_value);
        mPhoneNumber2Label = (TextView) findViewById(R.id.detail_mobile2_label);

        mFabLocate = (FloatingActionButton) findViewById(R.id.fab_locate);
        mFabCall = (FloatingActionButton) findViewById(R.id.fab_call);
        mFabSms = (FloatingActionButton) findViewById(R.id.fab_sms);


        mFabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhoneNumber2.getText().toString() == "") {
                    dialPhoneNumber(mPhoneNumber1.getText().toString());
                } else {
                    getPhoneNumberDialogBuilder(new CharSequence[]{mPhoneNumber1.getText().toString(), mPhoneNumber2.getText().toString()}, false).show();
                }
            }
        });

        mFabLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RescuerDetailActivity.this, MainActivity.class);
                intent.putExtra("focus_rescuer_lat", 45.797382);
                intent.putExtra("focus_rescuer_lon", 15.885526);
                startActivity(intent);
            }
        });

        mFabSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhoneNumber2.getText().toString() == "") {
                    getSmsTextDialogBuilder(mPhoneNumber1.getText().toString());
                } else {
                    getPhoneNumberDialogBuilder(new CharSequence[]{mPhoneNumber1.getText().toString(), mPhoneNumber2.getText().toString()}, true).show();
                }
            }
        });

        long id = getIntent().getLongExtra("id", -1);
        mFullName.setText(getIntent().getStringExtra("fullName"));
        mDistance.setText(Float.toString(getIntent().getFloatExtra("distance", -1)));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, NetworkUtils.RESCUERS_LIST_URL+"/"+id, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mAddress.setText(response.getString("address"));
                            mPhoneNumber1.setText(response.getString("contactNumber"));
                            String secondaryNumber = response.getString("secondaryContactNumber");
                            if (secondaryNumber != "null") {
                            mPhoneNumber2.setText(secondaryNumber);
                            } else {
                                mPhoneNumber2.setVisibility(View.INVISIBLE);
                                mPhoneNumber2Label.setVisibility(View.INVISIBLE);
                            }
                            mCategory.setText(response.getString("category"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsObjRequest);
    }

    private AlertDialog.Builder getPhoneNumberDialogBuilder(CharSequence[] phoneNumbers, final boolean showSmsDialogAfter) {

        final CharSequence[] numbers = phoneNumbers;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Odaberi broj telefona");
        builder.setItems(phoneNumbers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedNumber = numbers[which].toString();
                if(showSmsDialogAfter) {
                    getSmsTextDialogBuilder(numbers[which].toString()).show();
                } else {
                    dialPhoneNumber(numbers[which].toString());
                }
            }
        }).create();

        return builder;
    }

    private AlertDialog.Builder getSmsTextDialogBuilder(String phoneNumber) {
        final String number = phoneNumber;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tekst poruke");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                composeSmsMessage(input.getText().toString(), number);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create();

        return builder;
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                getCallPermission();
                return;
            }
            startActivity(intent);
        }
    }

    public void composeSmsMessage(String message, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"+phoneNumber));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void getCallPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            mCallPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_PHONE_CALL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mCallPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_PHONE_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCallPermissionGranted = true;
                    dialPhoneNumber(selectedNumber);
                }
            }
        }
    }


}
