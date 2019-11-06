package com.example.pedometer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private ArrayList<HistoryItems>  mHistoryList;

    public static class HistoryViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_Date;
        public TextView textView_Time;
        public TextView textView_Steps;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_Date = itemView.findViewById(R.id.tv_date);
            textView_Time = itemView.findViewById(R.id.tv_time);
            textView_Steps = itemView.findViewById(R.id.tv_steps);
        }
    }

    public HistoryAdapter(ArrayList<HistoryItems> historyList){
        mHistoryList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        HistoryViewHolder hvh = new HistoryViewHolder(v);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItems currentItem = mHistoryList.get(position);

        holder.textView_Date.setText(currentItem.getTextDate());
        holder.textView_Time.setText(currentItem.getTextTime());
        holder.textView_Steps.setText(currentItem.getTextSteps());
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }
}
