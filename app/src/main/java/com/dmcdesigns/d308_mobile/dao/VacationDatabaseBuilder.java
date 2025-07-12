package com.dmcdesigns.d308_mobile.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dmcdesigns.d308_mobile.entities.Excursion;
import com.dmcdesigns.d308_mobile.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 18, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (VacationDatabaseBuilder.class){
                if(INSTANCE==null){
                    INSTANCE=Room.databaseBuilder(context.getApplicationContext(),VacationDatabaseBuilder.class, "myVacationDatabase.db")
                            .fallbackToDestructiveMigrationFrom(false,12)
                            .fallbackToDestructiveMigrationOnDowngrade(false)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
