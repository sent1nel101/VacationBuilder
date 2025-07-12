package com.dmcdesigns.d308_mobile.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.dmcdesigns.d308_mobile.R;
import com.dmcdesigns.d308_mobile.dao.Repository;
import com.dmcdesigns.d308_mobile.entities.Excursion;
import com.dmcdesigns.d308_mobile.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ExcursionDetails extends AppCompatActivity {
    private String excName;
    private String excDate;
    private String excDateString;
    private int vacationID;
    private int excID;
    Repository repo;

    EditText editExcName;
    EditText editExcDate;
    DatePickerDialog.OnDateSetListener excursionDatePickerListener;
    final Calendar myCalendarExcursion = Calendar.getInstance();
    private String vacationStartDateString;
    private String vacationEndDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.repo = new Repository(getApplication());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);

        editExcName = findViewById(R.id.excName);
        editExcDate = findViewById(R.id.excDate);

        excName = getIntent().getStringExtra("excName");
        excDate = getIntent().getStringExtra("excDate");
        vacationID = getIntent().getIntExtra("vacationID", -1);
        excID = getIntent().getIntExtra("excID", -1);

        vacationStartDateString = getIntent().getStringExtra("vacationStartDate");
        vacationEndDateString = getIntent().getStringExtra("vacationEndDate");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editExcName.setText(excName);
        editExcDate.setText(excDate);

        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        excursionDatePickerListener = (view, year, month, dayOfMonth) -> {
            myCalendarExcursion.set(Calendar.YEAR, year);
            myCalendarExcursion.set(Calendar.MONTH, month);
            myCalendarExcursion.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateExcursionDateLabel();
        };


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editExcDate.setOnClickListener(v -> {
            Date date;
            String info = editExcDate.getText().toString();
            if (info.isEmpty() && this.excDate != null && !this.excDate.isEmpty()) {
                info = this.excDate;
            } else if (info.isEmpty() && excDateString != null && !excDateString.isEmpty()) {
                info = excDateString;
            } else if (info.isEmpty()) {
                info = sdf.format(new Date()); // Default to today if still empty
            }
            try {
                myCalendarExcursion.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
                myCalendarExcursion.setTime(new Date()); // Default to today on parse error
            }
            new DatePickerDialog(ExcursionDetails.this, excursionDatePickerListener, myCalendarExcursion
                    .get(Calendar.YEAR), myCalendarExcursion.get(Calendar.MONTH),
                    myCalendarExcursion.get(Calendar.DAY_OF_MONTH)).show();
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (item.getItemId() == R.id.saveExc) {
            String titleValue = editExcName.getText().toString().trim();
            String excursionDateValue = editExcDate.getText().toString().trim();

            if (titleValue.isEmpty() || excursionDateValue.isEmpty()) {
                Toast.makeText(this, "Please enter both name and date.", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (this.vacationID == -1) {
                Toast.makeText(this, "Cannot save: Critical error - No associated vacation ID.", Toast.LENGTH_LONG).show();
                finish();
                return true;
            }

            try {
                if (excursionDateValue == null || vacationStartDateString == null || vacationEndDateString == null) {
                    Toast.makeText(this, "Error: Date information is missing.", Toast.LENGTH_LONG).show();
                    Log.e("ExcursionDetails", "Date string is null. EDV: " + excursionDateValue +
                            ", VSDS: " + vacationStartDateString + ", VEDS: " + vacationEndDateString);
                    return true; // Don't proceed
                }

                Date excDateParsed = sdf.parse(excursionDateValue);
                Date vacStartDate = sdf.parse(vacationStartDateString);
                Date vacEndDate = sdf.parse(vacationEndDateString);

                if (excDateParsed.before(vacStartDate) || excDateParsed.after(vacEndDate)) {
                    Toast.makeText(this, "Excursion date must be within the vacation period (" +
                            vacationStartDateString + " - " + vacationEndDateString + ").", Toast.LENGTH_LONG).show();
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error parsing dates. Please check date formats.", Toast.LENGTH_LONG).show();
                return true;
            }

            new Thread(() -> {
                Excursion excursion;
                boolean isNewExcursion = (this.excID == -1);

                if (isNewExcursion) { // Logic for a NEW excursion
                    excursion = new Excursion(0, titleValue, excursionDateValue, this.vacationID);
                    try {
                        if (repo == null) {
                            runOnUiThread(() -> Toast.makeText(ExcursionDetails.this, "Error: Repository not initialized.", Toast.LENGTH_LONG).show());
                            return; // Exit this background thread
                        }
                        long newExcursionId = repo.insert(excursion); // Call insert(Excursion), returns long
                        runOnUiThread(() -> {
                            if (newExcursionId > 0) {
                                Toast.makeText(ExcursionDetails.this, "Excursion Saved", Toast.LENGTH_SHORT).show();
                                 this.excID = (int) newExcursionId;
                            } else {
                                Toast.makeText(ExcursionDetails.this, "Failed to save excursion.", Toast.LENGTH_SHORT).show();
                            }
                            finish(); // Go back after saving
                        });
                    } catch (Exception e) {
                        Log.e("ExcursionDetails", "Error saving new excursion: " + e.getMessage(), e);
                        runOnUiThread(() -> Toast.makeText(ExcursionDetails.this, "Error saving excursion: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    }
                } else { // Logic for UPDATING an EXISTING excursion
                    excursion = new Excursion(this.excID, titleValue, excursionDateValue, this.vacationID);
                    try {
                        if (repo == null) {
                            Log.e("ExcursionDetails", "Repository not initialized during update.");
                            runOnUiThread(() -> Toast.makeText(ExcursionDetails.this, "Error: Repository not initialized.", Toast.LENGTH_LONG).show());
                            return; // Exit background thread
                        }
                        repo.update(excursion);
                        runOnUiThread(() -> {
                            Toast.makeText(ExcursionDetails.this, "Excursion Updated", Toast.LENGTH_SHORT).show();
                            finish(); // Go back after updating
                        });
                    } catch (Exception e) {
                        Log.e("ExcursionDetails", "Error updating excursion: " + e.getMessage(), e);
                        runOnUiThread(() -> Toast.makeText(ExcursionDetails.this, "Error updating excursion: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    }
                }
            }).start();
            return true;
        }
        else if (item.getItemId() == R.id.deleteExc) {
            if (this.excID == -1) {
                Toast.makeText(this, "Cannot delete an unsaved excursion.", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(() -> { // Background thread for delete
                    try {
                        if (repo == null) {
                            Log.e("ExcursionDetails", "Repository not initialized during delete.");
                            runOnUiThread(() -> Toast.makeText(ExcursionDetails.this, "Error: Repository not initialized.", Toast.LENGTH_LONG).show());
                            return;
                        }
                        repo.deleteExcursionById(this.excID);
                        runOnUiThread(() -> {
                            Toast.makeText(ExcursionDetails.this, "Excursion deleted.", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    } catch (Exception e) {
                        Log.e("ExcursionDetails", "Error deleting excursion: " + e.getMessage(), e);
                        runOnUiThread(() -> Toast.makeText(ExcursionDetails.this, "Error deleting excursion: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    }
                }).start();
            }
            return true;
        } else if (item.getItemId() == R.id.notifyExc) {
            myFormat = "MM/dd/yyyy";
            String dateFromScreen = editExcDate.getText().toString();
            sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate;

            if (dateFromScreen.isEmpty()) {
                Toast.makeText(this, "Please set an excursion date first.", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (editExcName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please set an excursion name first.", Toast.LENGTH_SHORT).show();
                return true;
            }

            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Invalid date format. Please use " + myFormat, Toast.LENGTH_LONG).show();
                return true;
            }

            long triggerAtMillis = myDate.getTime();
            String excursionName = editExcName.getText().toString();
            String excursionDate = editExcDate.getText().toString();

            // Construct the message for the Toast
            String message = "Reminder for Excursion: " + excursionName + " on " + excursionDate;

            int requestCode = ++MainActivity.numAlert;

            // Call the helper method
            scheduleExcursionAlarm(this, excursionName, message, triggerAtMillis, requestCode);

             Toast.makeText(this, "Notification scheduling initiated.", Toast.LENGTH_SHORT).show();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private void scheduleExcursionAlarm(Context context, String excursionTitle, String message, long triggerAtMillis, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyReceiver.class);

        intent.putExtra("EXTRA_NOTIFICATION_TYPE", "EXCURSION_TYPE");
        intent.putExtra("EXTRA_TITLE", excursionTitle);
        intent.putExtra("EXTRA_MESSAGE", message);
        intent.putExtra("EXTRA_REQUEST_CODE", requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                        Log.d("ExcursionDetails", "Exact alarm scheduled for Excursion Toast.");
                        Toast.makeText(context, "Excursion notification scheduled!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Exact alarm permission needed for timely excursion alerts. Please enable in app settings.", Toast.LENGTH_LONG).show();
                        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                        Log.d("ExcursionDetails", "Exact alarm PERMISSION DENIED for Excursion. Scheduled inexactly.");
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    Log.d("ExcursionDetails", "Exact (allow while idle) alarm scheduled for Excursion Toast on pre-S device.");
                    Toast.makeText(context, "Excursion notification scheduled!", Toast.LENGTH_SHORT).show();
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    Log.d("ExcursionDetails", "Basic Exact alarm scheduled for Excursion Toast on very old pre-M device.");
                    Toast.makeText(context, "Excursion notification scheduled!", Toast.LENGTH_SHORT).show();
                }
            } catch (SecurityException se) {
                Log.e("ExcursionDetails", "SecurityException scheduling excursion alarm: " + se.getMessage());
                // Fallback to inexact
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                Toast.makeText(context, "Excursion notification scheduled (inexact fallback due to error).", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e("ExcursionDetails", "AlarmManager was null. Excursion alarm not scheduled.");
            Toast.makeText(context, "Could not schedule excursion notification: Alarm service not available.", Toast.LENGTH_LONG).show();
        }
    }


    private void updateExcursionDateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editExcDate.setText(sdf.format(myCalendarExcursion.getTime()));
    }

}