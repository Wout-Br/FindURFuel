package com.example.gebruiker.findurfuel;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
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
        void onClick(String name);
    }

    public DetailsAdapter(@NonNull Context context, GasStationDetailsOnClickHandler clickHandler) {
        gContext = context;
        gasStationDetailsClickHandler = clickHandler;
    }

    @Override
    public GasStationDetailsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.gasstation_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(gContext);
        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, false);
        view.setFocusable(true);

        return new GasStationDetailsAdapterViewHolder(view);
    }

    // For the details on the main activity !!
    @Override
    public void onBindViewHolder(GasStationDetailsAdapterViewHolder gasStationDetailsAdapterViewHolder, int position) {
        gCursor.moveToPosition(position);

        /*******************
         * Gas Station Summary *
         *******************/

        String name = gCursor.getString(MainActivity.INDEX_GASSTATION_NAME);
        String address = gCursor.getString(MainActivity.INDEX_GASSTATION_ADDRESS);
        String open = gCursor.getString(MainActivity.INDEX_GASSTATION_OPEN);

        String gasStationSummary = name + "   -   " + open + "\n" + address;
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

    public class GasStationDetailsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView gasStationTextView;

        public GasStationDetailsAdapterViewHolder(View view) {
            super(view);
            gasStationTextView = (TextView) view.findViewById(R.id.gasStationData);
            view.setOnClickListener(this);
        }

        // For the details on the detail activity !!
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            gCursor.moveToPosition(adapterPosition);
            String name = gCursor.getString(MainActivity.INDEX_GASSTATION_NAME);
            gasStationDetailsClickHandler.onClick(name);
        }
    }
}