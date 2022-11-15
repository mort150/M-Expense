package com.example.assignment_demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_demo.Database.Entity.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder> {
    public interface OnClickListener {
        public void OnClick(View view, int position);
    }

    public TripListAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    private List<Trip> trips;

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tripName;
        public TextView tripTime;
        public FloatingActionButton iconTravel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tripName);
            tripTime = itemView.findViewById(R.id.tripTime);
            iconTravel = itemView.findViewById(R.id.tripItemFab);
            itemView.setOnClickListener(this);
            iconTravel.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.OnClick(view, getAbsoluteAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tripName.setText(trips.get(position).getName());
        holder.tripTime.setText(trips.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
}
