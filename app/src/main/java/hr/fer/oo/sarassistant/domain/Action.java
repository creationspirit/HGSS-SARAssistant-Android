package hr.fer.oo.sarassistant.domain;

import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import hr.fer.oo.sarassistant.utils.NetworkUtils;

/**
 * Created by nameless on 4.2.2018..
 */

public class Action {

    private String title;
    private String description;
    private String LeaderName;
    private Long LeaderID;
    private boolean active;

    public Action (String title, String description, Long id, String LeaderName, boolean active) {
        this.title = title;
        this.description = description;
        this.LeaderID = id;
        this.LeaderName = LeaderName;
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeaderName() {
        return LeaderName;
    }

    public void setLeaderName(String leaderName) {
        LeaderName = leaderName;
    }

    public Long getLeaderID() {
        return LeaderID;
    }

    public void setLeaderID(Long leaderID) {
        LeaderID = leaderID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Action{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", LeaderName='" + LeaderName + '\'' +
                ", LeaderID=" + LeaderID +
                '}';
    }
}
