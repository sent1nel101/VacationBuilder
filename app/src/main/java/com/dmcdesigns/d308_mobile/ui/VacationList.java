package com.dmcdesigns.d308_mobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter vacationAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list); //

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new Repository(getApplication());

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recViewVacList);
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Floating action button handles navigation to Vacation Details screen for a NEW vacation
        FloatingActionButton fab = findViewById(R.id.fab_vacDetails);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VacationList.this, VacationDetails.class);
                i.putExtra("vacationID", -1);
                startActivity(i);
            }
        });

        loadVacationsAndUpdateUI();
    }

    private void loadVacationsAndUpdateUI() {
        new Thread(() -> {

            final List<Vacation> allVacations = repository.getAllVacations();

            runOnUiThread(() -> {
                if (vacationAdapter != null) {
                    vacationAdapter.setVacations(allVacations);
                }
            });
        }).start();
    }

    // Handles the expansion of the menu to show menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.item1) {
            addSampleDataAndRefresh();
            return true;
        } else if (itemId == android.R.id.home) {
            finish(); // Or onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addSampleDataAndRefresh() {
        new Thread(() -> {

            // Sample Vacation 1
            Vacation vacation1 = new Vacation(0, "Italy Adventure", "Grand Hotel Rome", "01/01/2025", "01/15/2025");
            long v1Id = repository.insert(vacation1);

            // Sample Vacation 2
            Vacation vacation2 = new Vacation(0, "Brazilian Beaches", "Copacabana Palace", "03/05/2025", "03/15/2025");
            long v2Id = repository.insert(vacation2);

            // Sample Excursion for Vacation 1
            if (v1Id > 0) {
                Excursion exc1 = new Excursion(0, "Colosseum Tour", "01/05/2025", (int) v1Id);
                long e1Id = repository.insert(exc1); // Now correctly calls the overloaded insert(Excursion)
                // You can use e1Id if needed
            }

            // Sample Excursion for Vacation 2
            if (v2Id > 0) {
                Excursion exc2 = new Excursion(0, "Cable Car", "03/10/2025", (int) v2Id);
                long e1Id = repository.insert(exc2);
            }

            final List<Vacation> updatedVacations = repository.getAllVacations();
            runOnUiThread(() -> {
                if (vacationAdapter != null) {
                    vacationAdapter.setVacations(updatedVacations);
                }
                Toast.makeText(VacationList.this, "Sample data added.", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVacationsAndUpdateUI();
    }
}
