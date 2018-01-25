package hr.fer.oo.sarassistant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hr.fer.oo.sarassistant.domain.Rescuer;

/**
 * Created by nameless on 24.1.2018..
 */

public class RescuersAdapter extends RecyclerView.Adapter<RescuersAdapter.RescuersAdapterViewHolder> {

    private ArrayList<Rescuer> mRescuersData;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final RescuersAdapterOnClickHandler mClickHandler;
    /**
     * The interface that receives onClick messages.
     */
    public interface RescuersAdapterOnClickHandler {
        void onClick(Rescuer rescuer);
    }

    public RescuersAdapter(RescuersAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public RescuersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.rescuers_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new RescuersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RescuersAdapterViewHolder holder, int position) {
        Rescuer rescuer = mRescuersData.get(position);
        holder.mRescuerName.setText(rescuer.getFullName());
        holder.mRescuerDistance.setText("2.5km");
    }

    @Override
    public int getItemCount() {
        if (null == mRescuersData) return 0;
        return mRescuersData.size();
    }

    public void setRescuerData(ArrayList<Rescuer> rescuersData) {
        mRescuersData = rescuersData;
        notifyDataSetChanged();
    }

    public class RescuersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mRescuerName;
        public final TextView mRescuerDistance;

        public RescuersAdapterViewHolder(View view) {
            super(view);
            mRescuerName = (TextView) view.findViewById(R.id.rescuer_name);
            mRescuerDistance = (TextView) view.findViewById(R.id.rescuer_distance);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Rescuer rescuer = mRescuersData.get(adapterPosition);
            mClickHandler.onClick(rescuer);
        }
    }
}
