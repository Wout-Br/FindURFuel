package com.example.gebruiker.findurfuel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by gebruiker on 4/03/2018.
 */

public class GasStationDetailsAdapter extends RecyclerView.Adapter<GasStationDetailsAdapter.GasStationDetailsAdapterViewHolder> {

    private String[] gasStationData;

    private final GasStationDetailsOnClickHandler gasStationDetailsClickHandler;

    public interface GasStationDetailsOnClickHandler {
        void onClick(String detailsPerStation);
    }

    public GasStationDetailsAdapter(GasStationDetailsOnClickHandler clickHandler) {
        gasStationDetailsClickHandler = clickHandler;
    }

    public class GasStationDetailsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView gasStationTextView;

        public GasStationDetailsAdapterViewHolder(View view) {
            super(view);
            gasStationTextView = (TextView) view.findViewById(R.id.gasStationData);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String detailsPerStation = gasStationData[adapterPosition];
            gasStationDetailsClickHandler.onClick(detailsPerStation);
        }
    }

    @Override
    public GasStationDetailsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.gasstation_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new GasStationDetailsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GasStationDetailsAdapterViewHolder gasStationDetailsAdapterViewHolder, int position) {
        String singleGasStaion = gasStationData[position];
        gasStationDetailsAdapterViewHolder.gasStationTextView.setText(singleGasStaion);
    }

    @Override
    public int getItemCount() {
        if (null == gasStationData) return 0;
        return gasStationData.length;
    }

    public void setGasStationData(String[] gasStationDataArray) {
        gasStationData = gasStationDataArray;
        notifyDataSetChanged();
    }
}
