package com.example.gsc_30286.todoapp.Fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gsc_30286.todoapp.Adapter.CustomAdapter;
import com.example.gsc_30286.todoapp.Database.Database;
import com.example.gsc_30286.todoapp.R;
import com.example.gsc_30286.todoapp.Tasks.Tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RemainingTask extends Fragment {


    private ArrayList<Tasks> taskDetailArrayList = null;
    private View view = null;
    private CustomAdapter mAdapter = new CustomAdapter();
    private RecyclerView recyclerView;
    private int recentCount = 0, todayCount = 0, tommorrowCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView = view.findViewById(R.id.recyclerView);
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayAsString = dateFormat.format(today);
        String tomorrowAsString = dateFormat.format(tomorrow);
        Cursor res = new Database(getContext()).getRemainingTask();
        taskDetailArrayList = new ArrayList<>();
        while (res.moveToNext()) {
            Tasks rowDetail = new Tasks();
            long miliSeconds = Long.parseLong(res.getString(0));
            Date date = new Date(miliSeconds);
            String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(date);
            String dateCheck = dateTime.substring(0, 10);
            if (dateCheck.compareTo(todayAsString) < 0) {
                recentCount++;
            } else if (dateCheck.compareTo(todayAsString) == 0) {
                todayCount++;
            } else if (dateCheck.compareTo(tomorrowAsString) == 0) {
                tommorrowCount++;
            } else if (dateCheck.compareTo(tomorrowAsString) > 0) {
            }
            rowDetail.setDatetime(res.getString(0));
            rowDetail.setName(res.getString(1));
            rowDetail.setDescription(res.getString(2));
            rowDetail.setStatus(res.getString(3));
            taskDetailArrayList.add(rowDetail);
        }
        res.close();
        for (int i = 0; i < 4; i++) {
            Tasks tasks = new Tasks();
            tasks.setStatus("");
            tasks.setDatetime("");
            tasks.setDescription("");
            if (i == 0) {
                tasks.setName("RECENT");
                taskDetailArrayList.add(0, tasks);
            } else if (i == 1) {
                tasks.setName("TODAY");
                taskDetailArrayList.add(recentCount + 1, tasks);
            } else if (i == 2) {
                tasks.setName("TOMMORROW");
                taskDetailArrayList.add(recentCount + todayCount + 2, tasks);
            } else if (i == 3) {
                tasks.setName("THIS WEEK");
                taskDetailArrayList.add(recentCount + todayCount + tommorrowCount + 3, tasks);
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CustomAdapter(taskDetailArrayList));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_remaining_task, container, false);
        return view;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
