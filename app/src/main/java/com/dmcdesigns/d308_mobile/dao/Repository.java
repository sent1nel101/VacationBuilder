package com.dmcdesigns.d308_mobile.dao;

import android.app.Application;

import com.dmcdesigns.d308_mobile.entities.Excursion;
import com.dmcdesigns.d308_mobile.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final VacationDAO mVacationDAO;
    private final ExcursionDAO mExcursionDAO;

    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
    }

    public List<Vacation> getAllVacations() {
        return mVacationDAO.getAllVacations();
    }

    public long insert(Vacation vacation) {
        return mVacationDAO.insert(vacation);
    }

    public long insert(Excursion excursion) {
        return mExcursionDAO.insert(excursion);
    }


    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.update(vacation));
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> mVacationDAO.delete(vacation));
    }

    public List<Excursion> getAllExcursions() {
        return mExcursionDAO.getAllExcursions();
    }

    public List<Excursion> getAssociatedExcursions(int vacationID) {
        return mExcursionDAO.getExcursionsByVacationId(vacationID);
    }


    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> mExcursionDAO.update(excursion));
    }

    public Vacation getVacationById(int id) {
        return mVacationDAO.getVacationById(id);
    }

    public void deleteVacationById(int id) {
        databaseExecutor.execute(() -> mVacationDAO.deleteVacationById(id));
    }

    public void deleteExcursionById(int id) {
        databaseExecutor.execute(() -> mExcursionDAO.deleteExcursionById(id));
    }
}
