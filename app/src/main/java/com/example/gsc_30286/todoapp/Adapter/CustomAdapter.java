package com.example.gsc_30286.todoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gsc_30286.todoapp.Database.Database;
import com.example.gsc_30286.todoapp.Activity.EditTask;
import com.example.gsc_30286.todoapp.Activity.MainActivity;
import com.example.gsc_30286.todoapp.Fragment.RemainingTask;
import com.example.gsc_30286.todoapp.R;
import com.example.gsc_30286.todoapp.Tasks.Tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private ArrayList<Tasks> rowDetails;
    private Tasks singleTask;
    private Context context;
    private Bundle bundle;
    private int xA = 0;
    private int[] place = new int[4];
    private SimpleDateFormat sdf;
    private AppCompatActivity activity = null;

    public CustomAdapter() {
    }

    public CustomAdapter(ArrayList<Tasks> rowDetails) {
        this.rowDetails = rowDetails;

    }

    public void xyz(int xA, int place[]) {
        this.xA = xA;
        this.place = place;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row, viewGroup, false);
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        return new CustomViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        singleTask = rowDetails.get(position);
        Log.d("ValueOfX:", "" + xA);
        xA++;
        if ((singleTask.getName() == "RECENT") | (singleTask.getName() == "TODAY") | (singleTask.getName() == "TOMMORROW") | (singleTask.getName() == "THIS WEEK")) {
            holder.nameText.setText(singleTask.getName());
            holder.nameText.setTag(position);
        } else {
            String statusA = singleTask.getStatus().toString();
            holder.nameText.setText(singleTask.getName());
            holder.nameText.setTag(position);
            holder.dateText.setVisibility(View.VISIBLE);
            holder.descText.setVisibility(View.VISIBLE);
            String xzx = String.valueOf(singleTask.getDatetime());
            long miliSeconds = Long.parseLong(xzx);
            Date res = new Date(miliSeconds);
            String timeStamp = sdf.format(res);
            holder.dateText.setText(timeStamp);
            holder.dateText.setTag(position);
            holder.descText.setText(singleTask.getDescription());
            holder.descText.setTag(position);
            if (statusA.compareTo("DONE") == 0) {
                holder.delete.setVisibility(View.GONE);
            } else {
                holder.edit.setVisibility(View.VISIBLE);
                holder.done.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.edit.setTag(position);
                holder.delete.setTag(position);
                holder.done.setTag(position);
                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity = (AppCompatActivity) v.getContext();
                        Intent intent = new Intent(activity, EditTask.class);
                        intent.putExtra("type", "Edit");
                        intent.putExtra("name", String.valueOf(rowDetails.get((Integer) v.getTag()).getName()));
                        intent.putExtra("description", String.valueOf(rowDetails.get((Integer) v.getTag()).getDescription()));
                        long miliSeconds = Long.parseLong(rowDetails.get((Integer) v.getTag()).getDatetime());
                        Date date = new Date(miliSeconds);
                        String previousTimeStamp = sdf.format(date);
                        intent.putExtra("previousStamp", previousTimeStamp);
                        activity.startActivity(intent);
                    }
                });
                holder.done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity = (AppCompatActivity) v.getContext();
                        boolean doneCheck =  new Database(activity).doneStatus(String.valueOf(rowDetails.get((Integer) v.getTag()).getDatetime()));
                        if (doneCheck){
                            rowDetails.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, rowDetails.size());
                        }
                        else {
                            printToast(activity, "Data not updated");
                        }
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity = (AppCompatActivity) v.getContext();
                        boolean deleted = new Database(activity).deleteRow(String.valueOf(rowDetails.get((Integer) v.getTag()).getDatetime()));
                        if (deleted){
                            rowDetails.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, rowDetails.size());
                        }
                        else {
                            printToast(activity, "Data not deleted");
                        }
                    }
                });
            }
        }
    }

    public void printToast(AppCompatActivity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return rowDetails.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout, buttonLayout;
        TextView nameText, dateText, descText;
        ImageButton edit, done, delete;

        public CustomViewHolder(View view) {
            super(view);
            linearLayout = view.findViewById(R.id.linearLayout);
            buttonLayout = view.findViewById(R.id.buttonLayout);
            nameText = view.findViewById(R.id.name);
            dateText = view.findViewById(R.id.timestamp);
            descText = view.findViewById(R.id.description);
            edit = view.findViewById(R.id.edit);
            done = view.findViewById(R.id.complete);
            delete = view.findViewById(R.id.delete);
        }
    }
}
