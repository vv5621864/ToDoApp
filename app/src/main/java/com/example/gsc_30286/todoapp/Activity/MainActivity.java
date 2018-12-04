package com.example.gsc_30286.todoapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gsc_30286.todoapp.Adapter.CustomAdapter;
import com.example.gsc_30286.todoapp.Adapter.PageAdapter;
import com.example.gsc_30286.todoapp.Database.Database;
import com.example.gsc_30286.todoapp.Fragment.CompletedTask;
import com.example.gsc_30286.todoapp.Fragment.RemainingTask;
import com.example.gsc_30286.todoapp.R;
import com.example.gsc_30286.todoapp.Tasks.Tasks;

import java.sql.Timestamp;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RemainingTask.OnFragmentInteractionListener, CompletedTask.OnFragmentInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    boolean doubleBackToExitPressedOnce = false;
    private String todayAsString = null;
    private String tomorrowAsString = null;
    private int recent = 0, today = 0, tommorrow = 0, week = 0;
    private ArrayList<Tasks> taskDetailArrayList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        tabLayout.addTab(tabLayout.newTab().setText("Remaining Task"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("Completed Task"), 1);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditTask.class);
                intent.putExtra("type", "Create");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
