package com.example.gsc_30286.todoapp.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gsc_30286.todoapp.Adapter.CustomAdapter;
import com.example.gsc_30286.todoapp.Database.Database;
import com.example.gsc_30286.todoapp.R;
import com.example.gsc_30286.todoapp.Tasks.Tasks;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CompletedTask extends Fragment {
    private ArrayList<Tasks> taskDetailArrayList;
    private View view ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser) {
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            Cursor res = new Database(getContext()).getCompletedTask();
            taskDetailArrayList = new ArrayList<>();
            while (res.moveToNext()) {
                Tasks rowDetail = new Tasks();
                rowDetail.setDatetime(res.getString(0));
                rowDetail.setName(res.getString(1));
                rowDetail.setDescription(res.getString(2));
                rowDetail.setStatus(res.getString(3));
                taskDetailArrayList.add(rowDetail);
            }
            res.close();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new CustomAdapter(taskDetailArrayList));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_completed_task, container, false);
        return view;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
