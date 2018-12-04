package com.example.gsc_30286.todoapp.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gsc_30286.todoapp.Activity.MainActivity;
import com.example.gsc_30286.todoapp.Database.Database;
import com.example.gsc_30286.todoapp.R;
import com.example.gsc_30286.todoapp.Tasks.Tasks;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTask extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.nameText)
    TextView nameText;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.descText)
    TextView descText;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.dateBtn)
    Button dateBtn;
    @BindView(R.id.dateText)
    TextView dateText;
    @BindView(R.id.timeBtn)
    Button timeBtn;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.submit)
    Button submit;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private String previousTimeStamp, previousName, previousDescription;
    private String type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.compareTo("Edit") == 0) {
            previousTimeStamp = intent.getStringExtra("previousStamp");
            previousName = intent.getStringExtra("name");
            previousDescription = intent.getStringExtra("description");
            name.setText(previousName);
            description.setText(previousDescription);
            dateText.setText(previousTimeStamp.substring(0, 10));
            timeText.setText(previousTimeStamp.substring(11, previousTimeStamp.length()));
        } else {
            title.setText("CREATE TASK");
        }
        dateBtn.setOnClickListener(this);
        timeBtn.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dateBtn) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            dateText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        } else if (v.getId() == R.id.timeBtn) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (hourOfDay < 12) {
                                timeText.setText(hourOfDay + ":" + minute + ":00 AM");
                            } else {
                                hourOfDay = hourOfDay - 12;
                                timeText.setText(hourOfDay + ":" + minute + ":00 PM");
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        } else if (v.getId() == R.id.submit) {
            String s_date = dateText.getText().toString();
            String s_time = timeText.getText().toString();
            String s_name = name.getText().toString();
            String s_desc = description.getText().toString();
            String myDate = s_date + " " + s_time;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
            Date date = null;
            try {
                date = sdf.parse(myDate);
            } catch (Exception e) {
                Log.e("EditTask", e.getMessage());
            }
            long timeInMillis = date.getTime();
            if ((s_date.length() != 0) && (s_time.length() != 0) && (s_name.length() != 0)) {
                Tasks tasks = new Tasks();
                tasks.setName(s_name);
                tasks.setDescription(s_desc);
                tasks.setStatus("");
                tasks.setDatetime(String.valueOf(timeInMillis));
                boolean status;
                if (type.compareTo("Edit") == 0) {
                    status = new Database(this).updateData(tasks, previousTimeStamp);
                } else {
                    status = new Database(this).addTasks(tasks);
                }

                if (status == true) {
                    finish();
                } else {
                    Toast.makeText(this, " Error, data not inserted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "All field are mandatory", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
