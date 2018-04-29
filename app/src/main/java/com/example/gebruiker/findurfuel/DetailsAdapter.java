package com.example.gebruiker.findurfuel;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gebruiker.findurfuel.utilities.LogoUtils;

/**
 * Created by Wout Briels on 4/03/2018.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.GasStationDetailsAdapterViewHolder> {

    private final Context gContext;
    private Cursor gCursor;

    private final GasStationDetailsOnClickHandler gasStationDetailsClickHandler;

    public interface GasStationDetailsOnClickHandler {
        void onClick(String address);
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

        // *** Gas station name *** //
        String name = gCursor.getString(MainActivity.INDEX_GASSTATION_NAME);
        gasStationDetailsAdapterViewHolder.nameTextView.setText(name);

        // *** Gas station open *** //
        String open = gCursor.getString(MainActivity.INDEX_GASSTATION_OPEN);
        gasStationDetailsAdapterViewHolder.openTextView.setText("OPEN");

        // *** Gas station rating *** //
        String rating = gCursor.getString(MainActivity.INDEX_GASSTATION_RATING);
        gasStationDetailsAdapterViewHolder.ratingTextView.setText(rating);

        // *** Gas station icon *** //
        int logoId = LogoUtils.getLogoForGasStation(name);
        gasStationDetailsAdapterViewHolder.iconImageView.setImageResource(logoId);
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

        private final TextView nameTextView;
        private final TextView openTextView;
        private final TextView ratingTextView;
        private final ImageView iconImageView;


        public GasStationDetailsAdapterViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.name);
            openTextView = (TextView) view.findViewById(R.id.open_now);
            ratingTextView = (TextView) view.findViewById(R.id.rating);
            iconImageView = (ImageView) view.findViewById(R.id.gas_station_icon);
            view.setOnClickListener(this);
        }

        // For the details on the detail activity !!
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            gCursor.moveToPosition(adapterPosition);
            String address = gCursor.getString(MainActivity.INDEX_GASSTATION_ADDRESS);
            gasStationDetailsClickHandler.onClick(address);
        }
    }
}