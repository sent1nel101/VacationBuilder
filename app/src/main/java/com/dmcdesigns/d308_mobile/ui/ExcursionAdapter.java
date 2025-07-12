package com.dmcdesigns.d308_mobile.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.dmcdesigns.d308_mobile.R;
import com.dmcdesigns.d308_mobile.entities.Excursion;
import com.dmcdesigns.d308_mobile.entities.Vacation;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> mExcursions;
    private final Context c;
    private final LayoutInflater mInflater;
    public ExcursionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.c = context;
    }

    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView;
        private final TextView excursionItemView2;


        private ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView12);
            excursionItemView2 = itemView.findViewById(R.id.textView13);
            itemView.setOnClickListener(new View.OnClickListener() {
                // In ExcursionAdapter.java
                @OptIn(markerClass = UnstableApi.class)
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent i = new Intent(c, ExcursionDetails.class);
                    i.putExtra("excID", current.getExcursionID());
                    i.putExtra("excName", current.getExcursionName());
                    i.putExtra("excDate", current.getExcDate());
                    i.putExtra("vacationID", current.getVacationID());

                    if (c instanceof VacationDetails) {
                        VacationDetails activity = (VacationDetails) c;
                        String vacStartDate = activity.getVacationStartDateString();
                        String vacEndDate = activity.getVacationEndDateString();

                        if (vacStartDate != null && vacEndDate != null) {
                            i.putExtra("vacationStartDate", vacStartDate);
                            i.putExtra("vacationEndDate", vacEndDate);
                        } else {
                            Log.e("ExcursionAdapter", "Could not retrieve vacation start/end dates from VacationDetails context.");
                            Toast.makeText(c, "Error: Parent vacation dates not found.", Toast.LENGTH_SHORT).show();
                            return; // Don't start activity if critical info is missing
                        }
                    } else {
                        Log.w("ExcursionAdapter", "Context is not VacationDetails. Vacation dates might be missing in intent.");
                    }
                    c.startActivity(i);
                }

            });
        }
    }
    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionAdapter.ExcursionViewHolder holder, int position) {
    if (mExcursions != null){
        Excursion current = mExcursions.get(position);
        String name = current.getExcursionName();
        String excDate = current.getExcDate();;
        holder.excursionItemView.setText(name);
        holder.excursionItemView2.setText(excDate);
        } else{
            holder.excursionItemView.setText("No excursion name");
            holder.excursionItemView2.setText("No excursion date");
        }
    }

    public void setmExcursions(List<Excursion> excursions){
        mExcursions = excursions;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (mExcursions != null) {
            return mExcursions.size();
        } else return 0;
    }
}
