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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.concurrent.atomic.AtomicLong; // For safely updating vacationID from background thread

public class VacationDetails extends AppCompatActivity {
    EditText editTitle;
    EditText editHotelName;
    EditText editStartDate;
    EditText editEndDate;
    int vacationID;
    Repository repo;
    DatePickerDialog.OnDateSetListener startDatePicker;
    DatePickerDialog.OnDateSetListener endDatePicker;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();

    private static final String KEY_VACATION_ID = "vacation_id";
    private static final String KEY_TITLE_TEXT = "title_text";
    private static final String KEY_HOTEL_NAME_TEXT = "hotel_name_text";
    private static final String KEY_START_DATE_TEXT = "start_date_text";
    private static final String KEY_END_DATE_TEXT = "end_date_text";
    private static final String KEY_CALENDAR_START_MILLIS = "calendar_start_millis";
    private static final String KEY_CALENDAR_END_MILLIS = "calendar_end_millis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab = findViewById(R.id.fab_excDetails);

        editTitle = findViewById(R.id.titleText);
        editHotelName = findViewById(R.id.hotelName);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        repo = new Repository(getApplication());

        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (savedInstanceState != null) {
            vacationID = savedInstanceState.getInt(KEY_VACATION_ID, -1);
            editTitle.setText(savedInstanceState.getString(KEY_TITLE_TEXT));
            editHotelName.setText(savedInstanceState.getString(KEY_HOTEL_NAME_TEXT));
            editStartDate.setText(savedInstanceState.getString(KEY_START_DATE_TEXT));
            editEndDate.setText(savedInstanceState.getString(KEY_END_DATE_TEXT));

            if (savedInstanceState.containsKey(KEY_CALENDAR_START_MILLIS)) {
                myCalendarStart.setTimeInMillis(savedInstanceState.getLong(KEY_CALENDAR_START_MILLIS));
            }
            if (savedInstanceState.containsKey(KEY_CALENDAR_END_MILLIS)) {
                myCalendarEnd.setTimeInMillis(savedInstanceState.getLong(KEY_CALENDAR_END_MILLIS));
            }
        } else {
            vacationID = getIntent().getIntExtra("vacationID", -1);
            String titleFromIntent = getIntent().getStringExtra("title");
            String hotelNameFromIntent = getIntent().getStringExtra("hotelName");
            String startDateFromIntent = getIntent().getStringExtra("startDate");
            String endDateFromIntent = getIntent().getStringExtra("endDate");

            editTitle.setText(titleFromIntent);
            editHotelName.setText(hotelNameFromIntent);
            editStartDate.setText(startDateFromIntent);
            editEndDate.setText(endDateFromIntent);

            try {
                if (startDateFromIntent != null && !startDateFromIntent.isEmpty()) {
                    myCalendarStart.setTime(sdf.parse(startDateFromIntent));
                }
                if (endDateFromIntent != null && !endDateFromIntent.isEmpty()) {
                    myCalendarEnd.setTime(sdf.parse(endDateFromIntent));
                }
            } catch (ParseException e) {
                Log.e("VacationDetails", "Error parsing dates from intent in onCreate", e);
                Toast.makeText(this, "Error parsing dates from intent.", Toast.LENGTH_SHORT).show();
            }
        }

        startDatePicker = (view, year, month, dayOfMonth) -> {
            myCalendarStart.set(Calendar.YEAR, year);
            myCalendarStart.set(Calendar.MONTH, month);
            myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelStart();
        };

        endDatePicker = (view, year, month, dayOfMonth) -> {
            myCalendarEnd.set(Calendar.YEAR, year);
            myCalendarEnd.set(Calendar.MONTH, month);
            myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelEnd();
        };

        editStartDate.setOnClickListener(v -> {
            String info = editStartDate.getText().toString();
            try {
                if (info.isEmpty()) {
                    Calendar tempCal = Calendar.getInstance(); // Use current date if field is empty
                    myCalendarStart.set(Calendar.YEAR, tempCal.get(Calendar.YEAR));
                    myCalendarStart.set(Calendar.MONTH, tempCal.get(Calendar.MONTH));
                    myCalendarStart.set(Calendar.DAY_OF_MONTH, tempCal.get(Calendar.DAY_OF_MONTH));
                } else {
                    myCalendarStart.setTime(sdf.parse(info));
                }
            } catch (ParseException e) {
                Log.e("VacationDetails", "ParseException for start date picker init", e);
                Calendar tempCal = Calendar.getInstance(); // Fallback to current date
                myCalendarStart.set(Calendar.YEAR, tempCal.get(Calendar.YEAR));
                myCalendarStart.set(Calendar.MONTH, tempCal.get(Calendar.MONTH));
                myCalendarStart.set(Calendar.DAY_OF_MONTH, tempCal.get(Calendar.DAY_OF_MONTH));
            }
            new DatePickerDialog(VacationDetails.this, startDatePicker, myCalendarStart
                    .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                    myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
        });

        editEndDate.setOnClickListener(v -> {
            String info = editEndDate.getText().toString();
            try {
                if (info.isEmpty()) {
                    Calendar tempCal = Calendar.getInstance(); // Use current date if field is empty
                    myCalendarEnd.set(Calendar.YEAR, tempCal.get(Calendar.YEAR));
                    myCalendarEnd.set(Calendar.MONTH, tempCal.get(Calendar.MONTH));
                    myCalendarEnd.set(Calendar.DAY_OF_MONTH, tempCal.get(Calendar.DAY_OF_MONTH));
                } else {
                    myCalendarEnd.setTime(sdf.parse(info));
                }
            } catch (ParseException e) {
                Log.e("VacationDetails", "ParseException for end date picker init", e);
                Calendar tempCal = Calendar.getInstance(); // Fallback to current date
                myCalendarEnd.set(Calendar.YEAR, tempCal.get(Calendar.YEAR));
                myCalendarEnd.set(Calendar.MONTH, tempCal.get(Calendar.MONTH));
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, tempCal.get(Calendar.DAY_OF_MONTH));
            }
            new DatePickerDialog(VacationDetails.this, endDatePicker, myCalendarEnd
                    .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                    myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
        });

        fab.setOnClickListener(v -> {
            if (vacationID == -1) {
                Toast.makeText(VacationDetails.this, "Please save the vacation before adding excursions.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent i = new Intent(VacationDetails.this, ExcursionDetails.class);
            i.putExtra("excID", -1); // For a new excursion
            i.putExtra("vacationID", vacationID);
            String vacStartDateStr = editStartDate.getText().toString();
            String vacEndDateStr = editEndDate.getText().toString();
            if (!vacStartDateStr.isEmpty() && !vacEndDateStr.isEmpty()) {
                i.putExtra("vacationStartDate", vacStartDateStr);
                i.putExtra("vacationEndDate", vacEndDateStr);
                startActivity(i);
            } else {
                Toast.makeText(VacationDetails.this, "Vacation start and end dates must be set to add excursions.", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupRecyclerView();
        Log.d("VacationDetails_Life", "onCreate finished. Current this.vacationID: " + this.vacationID);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_VACATION_ID, vacationID);
        outState.putString(KEY_TITLE_TEXT, editTitle.getText().toString());
        outState.putString(KEY_HOTEL_NAME_TEXT, editHotelName.getText().toString());
        outState.putString(KEY_START_DATE_TEXT, editStartDate.getText().toString());
        outState.putString(KEY_END_DATE_TEXT, editEndDate.getText().toString());
        outState.putLong(KEY_CALENDAR_START_MILLIS, myCalendarStart.getTimeInMillis());
        outState.putLong(KEY_CALENDAR_END_MILLIS, myCalendarEnd.getTimeInMillis());
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recView);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadExcursions(excursionAdapter);
    }

    private void loadExcursions(ExcursionAdapter excursionAdapter) {
        if (repo == null) {
            repo = new Repository(getApplication());
        }
        final int currentVacationId = this.vacationID; // Capture for use in thread

        final int currentVacationIdToLoadFor = this.vacationID;
        Log.d("VacationDetails_LoadExc", "Attempting to load excursions for vacationID: " + currentVacationIdToLoadFor);
        if (currentVacationId != -1) {
            new Thread(() -> {
                // Use method to get excursions only for this vacation
                Log.d("VacationDetails_LoadExc", "Background thread: Calling repo.getAssociatedExcursions(" + currentVacationIdToLoadFor + ")");
                List<Excursion> filteredExcursions = repo.getAssociatedExcursions(currentVacationId);

                // Log the result from the repo
                if (filteredExcursions == null) {
                    Log.d("VacationDetails_LoadExc", "Background thread: repo.getAssociatedExcursions returned null");
                } else {
                    Log.d("VacationDetails_LoadExc", "Background thread: repo.getAssociatedExcursions returned " + filteredExcursions.size() + " excursions.");
                    for (Excursion exc : filteredExcursions) {
                        Log.d("VacationDetails_LoadExc_Data", "Excursion: " + exc.getExcursionName() + " (ID: " + exc.getExcursionID() + ", VacID: " + exc.getVacationID() + ")");
                    }
                }

                runOnUiThread(() -> {
                    if (excursionAdapter != null) {
                        excursionAdapter.setmExcursions(filteredExcursions);
                    }
                });
            }).start();
        } else {
            Log.d("VacationDetails_LoadExc", "currentVacationId was -1, clearing adapter.");
            if (excursionAdapter != null) {
                excursionAdapter.setmExcursions(new ArrayList<>());
            }
        }
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        final String titleValue = editTitle.getText().toString().trim();
        final String hotelNameValue = editHotelName.getText().toString().trim();
        final String startDateString = editStartDate.getText().toString().trim();
        final String endDateString = editEndDate.getText().toString().trim();
        final int currentVacationId = this.vacationID; // Capture current vacationID

        if ((itemId == R.id.notifyVacationStart || itemId == R.id.notifyVacationEnd) && currentVacationId == -1) {
            Toast.makeText(this, "Please save the vacation before setting alerts.", Toast.LENGTH_LONG).show();
            return true;
        }

        if (itemId == R.id.saveVacation) {
            if (titleValue.isEmpty() || hotelNameValue.isEmpty() || startDateString.isEmpty() || endDateString.isEmpty()) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_LONG).show();
                return true;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            try {
                Date vacayStart = sdf.parse(startDateString);
                Date vacayEnd = sdf.parse(endDateString);
                if (vacayEnd != null && vacayStart != null && vacayEnd.before(vacayStart)) {
                    Toast.makeText(VacationDetails.this, "Vacation end date must be on or after the start date.", Toast.LENGTH_LONG).show();
                    return true;
                }
            } catch (ParseException e) {
                Toast.makeText(VacationDetails.this, "Invalid date format. Please use MM/dd/yyyy.", Toast.LENGTH_LONG).show();
                return true;
            }

            new Thread(() -> {
                Vacation vacation;
                boolean isNewVacation = (currentVacationId == -1);

                if (isNewVacation) {
                    vacation = new Vacation(0, titleValue, hotelNameValue, startDateString, endDateString);
                    long newId = repo.insert(vacation); // Assuming repo.insert(Vacation) returns the new ID (long)
                    runOnUiThread(() -> {
                        Toast.makeText(VacationDetails.this, "Vacation Saved", Toast.LENGTH_SHORT).show();
                         this.vacationID = (int) newId;
                        finish();
                    });
                } else {
                    vacation = new Vacation(currentVacationId, titleValue, hotelNameValue, startDateString, endDateString);
                    repo.update(vacation);
                    runOnUiThread(() -> {
                        Toast.makeText(VacationDetails.this, "Vacation Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }).start();
            return true;

        } else if (itemId == R.id.notifyVacationStart) {
            if (titleValue.isEmpty() || startDateString.isEmpty()) {
                Toast.makeText(this, "Vacation title and start date must be set.", Toast.LENGTH_LONG).show();
                return true;
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date parsedStartDate = sdf.parse(startDateString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedStartDate);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long triggerAtMillis = calendar.getTimeInMillis();
                String message = "Your vacation '" + titleValue + "' is starting today!";
                int requestCode = currentVacationId * 10 + 1;
                scheduleVacationAlarm(getApplicationContext(), titleValue, message, triggerAtMillis, requestCode, "start");
            } catch (ParseException e) {
                Toast.makeText(this, "Invalid start date format for alert. Please use MM/dd/yyyy.", Toast.LENGTH_LONG).show();
                Log.e("VacationDetails", "ParseException for start date alert: " + startDateString, e);
            }
            return true;

        } else if (itemId == R.id.notifyVacationEnd) {
            if (titleValue.isEmpty() || endDateString.isEmpty()) {
                Toast.makeText(this, "Vacation title and end date must be set.", Toast.LENGTH_LONG).show();
                return true;
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                Date parsedEndDate = sdf.parse(endDateString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedEndDate);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long triggerAtMillis = calendar.getTimeInMillis();
                String message = "Your vacation '" + titleValue + "' is ending today!";
                int requestCode = currentVacationId * 10 + 2;
                scheduleVacationAlarm(getApplicationContext(), titleValue, message, triggerAtMillis, requestCode, "end");
            } catch (ParseException e) {
                Toast.makeText(this, "Invalid end date format for alert. Please use MM/dd/yyyy.", Toast.LENGTH_LONG).show();
                Log.e("VacationDetails", "ParseException for end date alert: " + endDateString, e);
            }
            return true;

        } else if (itemId == R.id.deleteVacation) {
            if (currentVacationId == -1) {
                Toast.makeText(VacationDetails.this, "Cannot delete an unsaved vacation.", Toast.LENGTH_LONG).show();
                return true;
            }
            new Thread(() -> {
                // Fetch associated excursions for the current vacation
                List<Excursion> associatedExcursions = repo.getAssociatedExcursions(currentVacationId);
                final int excursionsCount = associatedExcursions != null ? associatedExcursions.size() : 0;

                // Fetch the vacation to ensure it exists before trying to delete
                final Vacation vacationToDelete = repo.getVacationById(currentVacationId);

                runOnUiThread(() -> {
                    if (vacationToDelete == null) {
                        Toast.makeText(VacationDetails.this, "Vacation not found for deletion.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (excursionsCount == 0) {
                        new Thread(() -> {
                             repo.deleteVacationById(vacationToDelete.getVacationID());
                            runOnUiThread(() -> {
                                Toast.makeText(VacationDetails.this, vacationToDelete.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
                                finish();
                            });
                        }).start();
                    } else {
                        Toast.makeText(VacationDetails.this, "Can't delete a vacation with " + excursionsCount + " excursion(s).", Toast.LENGTH_LONG).show();
                    }
                });
            }).start();
            return true;

        } else if (itemId == R.id.shareVacation) {
            if (currentVacationId == -1) {
                Toast.makeText(this, "Please save the vacation before sharing.", Toast.LENGTH_LONG).show();
                return true;
            }
            if (titleValue.isEmpty() && hotelNameValue.isEmpty() && startDateString.isEmpty() && endDateString.isEmpty()) {
                Toast.makeText(this, "Nothing to share. Please enter vacation details.", Toast.LENGTH_LONG).show();
                return true;
            }

            new Thread(() -> {
                final StringBuilder shareTextBuilder = new StringBuilder();
                shareTextBuilder.append("Hello! Here are the details for your vacation:\n\n");
                shareTextBuilder.append("Destination: ").append(titleValue).append("\n");
                shareTextBuilder.append("Hotel name: ").append(hotelNameValue).append("\n");
                shareTextBuilder.append("Start date: ").append(startDateString).append("\n");
                shareTextBuilder.append("End date: ").append(endDateString).append("\n");

                List<Excursion> associatedExcursions = repo.getAssociatedExcursions(currentVacationId);

                if (associatedExcursions != null && !associatedExcursions.isEmpty()) {
                    shareTextBuilder.append("\nAssociated Excursions:\n");
                    for (Excursion excursion : associatedExcursions) {
                        shareTextBuilder.append("- ").append(excursion.getExcursionName());
                        if (excursion.getExcDate() != null && !excursion.getExcDate().isEmpty()) {
                            shareTextBuilder.append(" (Date: ").append(excursion.getExcDate()).append(")");
                        }
                        shareTextBuilder.append("\n");
                    }
                } else {
                    shareTextBuilder.append("\nNo excursions currently associated with this vacation.\n");
                }

                runOnUiThread(() -> {
                    Intent sentIntent = new Intent();
                    sentIntent.setAction(Intent.ACTION_SEND);
                    sentIntent.putExtra(Intent.EXTRA_TEXT, shareTextBuilder.toString());
                    sentIntent.putExtra(Intent.EXTRA_TITLE, "Vacation Details for " + titleValue);
                    sentIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sentIntent, "Share Vacation Details");
                    startActivity(shareIntent);
                });
            }).start();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void scheduleVacationAlarm(Context context, String vacationTitle, String message, long triggerAtMillis, int requestCode, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyReceiver.class);

        intent.putExtra("EXTRA_NOTIFICATION_TYPE", "VACATION_TYPE");
        intent.putExtra("EXTRA_TITLE", vacationTitle);
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
                        Log.d("VacationDetails", "Exact alarm scheduled for Toast.");
                    } else {
                        Toast.makeText(context, "Exact alarm permission needed for timely alerts. Please enable in settings.", Toast.LENGTH_LONG).show();
                        // try an inexact alarm as a fallback
                         alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                         Log.d("VacationDetails", "Exact alarm PERMISSION DENIED. Toast may be unreliable/not show.");
                         Toast.makeText(context, "Scheduling alert inexactly. Please grant exact alarm permission for best results.", Toast.LENGTH_LONG).show();

                    }
                }  else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                }
            } catch (SecurityException se) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("VacationDetails_Life", "onResume called. Current this.vacationID: " + this.vacationID); // Log ID at start of onResume
        RecyclerView recyclerView = findViewById(R.id.recView);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof ExcursionAdapter) {
            loadExcursions((ExcursionAdapter) adapter);
        } else {
            Log.w("VacationDetails", "RecyclerView adapter not ExcursionAdapter or null in onResume. Re-initializing.");
            setupRecyclerView();
        }
    }

    // Getter methods for ExcursionAdapter to access vacation dates
    public String getVacationStartDateString() {
        return editStartDate.getText().toString();
    }

    public String getVacationEndDateString() {
        return editEndDate.getText().toString();
    }
}
