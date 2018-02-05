package hr.fer.oo.sarassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hr.fer.oo.sarassistant.R;
import hr.fer.oo.sarassistant.domain.Action;

/**
 * Created by nameless on 4.2.2018..
 */

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionsAdapterViewHolder> {

    private ArrayList<Action> mActionsData;
    private ActionsAdapterOnClickHandler mClickHandler;

    public ActionsAdapter(ActionsAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    public interface ActionsAdapterOnClickHandler {
        void onClick(Action action);
    }

    @Override
    public ActionsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.actions_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ActionsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActionsAdapterViewHolder holder, int position) {
        Action action = mActionsData.get(position);
        holder.mActionTitle.setText(action.getTitle());
        holder.mActionDescription.setText(action.getDescription());
        holder.mActionLeader.setText("Vođa akcije: " + action.getLeaderName());
        if(action.isActive()) holder.mActionStatus.setText("Active");
        else holder.mActionStatus.setText("Not Active");
    }

    @Override
    public int getItemCount() {
        if (null == mActionsData) return 0;
        return mActionsData.size();
    }

    public void setActionData(ArrayList<Action> actionsData) {
        mActionsData = actionsData;
        notifyDataSetChanged();
    }

    public class ActionsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mActionTitle;
        public final TextView mActionLeader;
        public final TextView mActionDescription;
        public final TextView mActionStatus;

        public ActionsAdapterViewHolder(View view) {
            super(view);
            mActionTitle = (TextView) view.findViewById(R.id.action_title);
            mActionDescription = (TextView) view.findViewById(R.id.action_short_description);
            mActionLeader = (TextView) view.findViewById(R.id.action_leader_name);
            mActionStatus = (TextView) view.findViewById(R.id.action_status);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Action action = mActionsData.get(adapterPosition);
            Log.d("Click", "Am i clicked?");
            mClickHandler.onClick(action);
        }
    }
}
