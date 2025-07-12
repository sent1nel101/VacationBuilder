package com.dmcdesigns.d308_mobile.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dmcdesigns.d308_mobile.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Vacation vacation);

//    Update using built-in features
    @Update
    void update(Vacation vacation);

//    Delete using built-in vacation
    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacations ORDER BY vacationID ASC;")
    List<Vacation> getAllVacations();

//    get a vacation by ID
    @Query("SELECT * FROM vacations WHERE vacationID = :id LIMIT 1")
    Vacation getVacationById(int id);

//    delete a  vacation by ID
    @Query("DELETE FROM vacations WHERE vacationID = :id")
    void deleteVacationById(int id);
}
