package com.example.gebruiker.findurfuel;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Wout Briels on 4/03/2018.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.GasStationDetailsAdapterViewHolder> {

    private final Context gContext;
    private Cursor gCursor;

    private final GasStationDetailsOnClickHandler gasStationDetailsClickHandler;

    public interface GasStationDetailsOnClickHandler {
        void onClick(String detailsPerStation);
    }

    public DetailsAdapter(Context context, GasStationDetailsOnClickHandler clickHandler) {
        gContext = context;
        gasStationDetailsClickHandler = clickHandler;
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
        gCursor.moveToPosition(position);

        /*******************
         * Gas Station Summary *
         *******************/

        String name = gCursor.getString(MainActivity.INDEX_GASSTATION_NAME);
        String address = gCursor.getString(MainActivity.INDEX_GASSTATION_ADDRESS);

        String gasStationSummary = name + "\n" + address;
        gasStationDetailsAdapterViewHolder.gasStationTextView.setText(gasStationSummary);
    }

    @Override
    public int getItemCount() {
        if (gCursor == null) {
            return 0;
        }
        return gCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        gCursor = newCursor;
        notifyDataSetChanged();
    }

    /*public void setGasStationData(String[] gasStationDataArray) {
        gasStationData = gasStationDataArray;
        notifyDataSetChanged();
    }*/

    public class GasStationDetailsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView gasStationTextView;

        public GasStationDetailsAdapterViewHolder(View view) {
            super(view);
            gasStationTextView = (TextView) view.findViewById(R.id.gasStationData);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String detailsPerStation = gasStationTextView.getText().toString();
            gasStationDetailsClickHandler.onClick(detailsPerStation);
        }
    }
}