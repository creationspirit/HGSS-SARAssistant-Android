package hr.fer.oo.sarassistant.activities;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import hr.fer.oo.sarassistant.R;
import hr.fer.oo.sarassistant.utils.NetworkUtils;

public class NewActionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private Spinner mDropdownLeader;

    private Spinner mTemplateMessages;
    private EditText mAlertMessage;

    private RequestQueue queue;

    private ArrayList<String> rescuerNames;
    private ArrayList<Long> rescuerIds;
    private long selectedLeaderId;
    private ArrayList<String> templates;

    private EditText mDateTime;
    private Calendar calendar;

    private FloatingActionButton mFabCreateAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_action);

        rescuerNames = new ArrayList<>();
        rescuerIds = new ArrayList<>();
        templates = new ArrayList<>();

        queue = Volley.newRequestQueue(this);

        mTitleEditText = (EditText) findViewById(R.id.action_title_edit_text);
        mDescriptionEditText = (EditText) findViewById(R.id.action_description_edit_text);

        mTemplateMessages = findViewById(R.id.message_template_spinner);
        mAlertMessage = findViewById(R.id.action_alert_message_edit_text);

        mDropdownLeader =  findViewById(R.id.leader_spinner);

        mDateTime = findViewById(R.id.action_datetime);

        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mDateTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewActionActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fetchRescuersAndInflateSpinner();
        fetchMsgTemplatesAndInflateSpinner();

        mFabCreateAction = (FloatingActionButton) findViewById(R.id.fab_create_action);
        mFabCreateAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void fetchRescuersAndInflateSpinner() {
        JsonArrayRequest jsArrRequest = new JsonArrayRequest
                (Request.Method.GET, NetworkUtils.RESCUERS_LIST_URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject rescuer = response.getJSONObject(i);
                                rescuerNames.add(rescuer.getString("name"));
                                rescuerIds.add(rescuer.getLong("id"));
                            }

                            setLeaderSpinner();
                        } catch(JSONException e){
                            Log.e("JSON", e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsArrRequest);
    }

    private void setLeaderSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, rescuerNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mDropdownLeader.setAdapter(adapter);
        mDropdownLeader.setOnItemSelectedListener(this);
    }

    private void fetchMsgTemplatesAndInflateSpinner() {
        JsonArrayRequest jsArrRequest = new JsonArrayRequest
                (Request.Method.GET, NetworkUtils.MSG_TEMPLATES_LIST_URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject template = response.getJSONObject(i);
                                templates.add(template.getString("message"));
                            }

                            setTemplateSpinner();
                        } catch(JSONException e){
                            Log.e("JSON", e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsArrRequest);
    }

    private void setTemplateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, templates);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mTemplateMessages.setAdapter(adapter);
        mTemplateMessages.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();
        if(id == R.id.leader_spinner) {
            selectedLeaderId = rescuerIds.get(i);
        } else if(id == R.id.message_template_spinner) {
            String s = (String) adapterView.getItemAtPosition(i);
            mAlertMessage.setText(s);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }



    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDateTime.setText(sdf.format(calendar.getTime()));
    }
}
