package com.dmcdesigns.d308_mobile.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dmcdesigns.d308_mobile.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Excursion excursion);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Excursion> excursions);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursions ORDER BY excursionID ASC;")
    List<Excursion> getAllExcursions();

    //    delete an excursion by ID
    @Query("DELETE FROM excursions WHERE excursionID = :id")
    void deleteExcursionById(int id);

    @Query("SELECT * FROM excursions WHERE vacationID = :vacationId ORDER BY excDate ASC")
    List<Excursion> getExcursionsByVacationId(int vacationId);

    @Query("SELECT * FROM excursions WHERE vacationID = :vacationId ORDER BY excDate ASC")
    List<Excursion> getExcursionsForVacation(int vacationId);
}

