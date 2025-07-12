package com.dmcdesigns.d308_mobile.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dmcdesigns.d308_mobile.R;
import com.dmcdesigns.d308_mobile.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;

    public VacationAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;
        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textView11);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Vacation current = mVacations.get(position);
                    Intent i = new Intent(context, VacationDetails.class);
                    i.putExtra("vacationID", current.getVacationID());
                    i.putExtra("title", current.getTitle());
                    i.putExtra("hotelName", current.getHotelName());
                    i.putExtra("startDate", current.getStartDate());
                    i.putExtra("endDate", current.getEndDate());
                    context.startActivity(i);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if (mVacations != null){
            Vacation current = mVacations.get(position);
            String title = current.getTitle();
            holder.vacationItemView.setText(title);
        }
        else holder.vacationItemView.setText("No product name");
    }

    @Override
    public int getItemCount() {
        if (mVacations != null) {
            return mVacations.size();
        }
        else return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setVacations(List<Vacation> vacations){
        mVacations = vacations;
        notifyDataSetChanged();
    }
}
