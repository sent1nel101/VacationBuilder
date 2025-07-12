package com.dmcdesigns.d308_mobile.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.dmcdesigns.d308_mobile.entities.Vacation;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class VacationDAO_Impl implements VacationDAO {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Vacation> __insertAdapterOfVacation;

  private final EntityDeleteOrUpdateAdapter<Vacation> __deleteAdapterOfVacation;

  private final EntityDeleteOrUpdateAdapter<Vacation> __updateAdapterOfVacation;

  public VacationDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfVacation = new EntityInsertAdapter<Vacation>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `vacations` (`vacationID`,`title`,`hotelName`,`startDate`,`endDate`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Vacation entity) {
        statement.bindLong(1, entity.getVacationID());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getTitle());
        }
        if (entity.getHotelName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getHotelName());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getEndDate());
        }
      }
    };
    this.__deleteAdapterOfVacation = new EntityDeleteOrUpdateAdapter<Vacation>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `vacations` WHERE `vacationID` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Vacation entity) {
        statement.bindLong(1, entity.getVacationID());
      }
    };
    this.__updateAdapterOfVacation = new EntityDeleteOrUpdateAdapter<Vacation>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `vacations` SET `vacationID` = ?,`title` = ?,`hotelName` = ?,`startDate` = ?,`endDate` = ? WHERE `vacationID` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Vacation entity) {
        statement.bindLong(1, entity.getVacationID());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getTitle());
        }
        if (entity.getHotelName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getHotelName());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getEndDate());
        }
        statement.bindLong(6, entity.getVacationID());
      }
    };
  }

  @Override
  public long insert(final Vacation vacation) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfVacation.insertAndReturnId(_connection, vacation);
    });
  }

  @Override
  public void delete(final Vacation vacation) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfVacation.handle(_connection, vacation);
      return null;
    });
  }

  @Override
  public void update(final Vacation vacation) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfVacation.handle(_connection, vacation);
      return null;
    });
  }

  @Override
  public List<Vacation> getAllVacations() {
    final String _sql = "SELECT * FROM vacations ORDER BY vacationID ASC;";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfVacationID = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "vacationID");
        final int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
        final int _columnIndexOfHotelName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hotelName");
        final int _columnIndexOfStartDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startDate");
        final int _columnIndexOfEndDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endDate");
        final List<Vacation> _result = new ArrayList<Vacation>();
        while (_stmt.step()) {
          final Vacation _item;
          final int _tmpVacationID;
          _tmpVacationID = (int) (_stmt.getLong(_columnIndexOfVacationID));
          final String _tmpTitle;
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _tmpTitle = null;
          } else {
            _tmpTitle = _stmt.getText(_columnIndexOfTitle);
          }
          final String _tmpHotelName;
          if (_stmt.isNull(_columnIndexOfHotelName)) {
            _tmpHotelName = null;
          } else {
            _tmpHotelName = _stmt.getText(_columnIndexOfHotelName);
          }
          final String _tmpStartDate;
          if (_stmt.isNull(_columnIndexOfStartDate)) {
            _tmpStartDate = null;
          } else {
            _tmpStartDate = _stmt.getText(_columnIndexOfStartDate);
          }
          final String _tmpEndDate;
          if (_stmt.isNull(_columnIndexOfEndDate)) {
            _tmpEndDate = null;
          } else {
            _tmpEndDate = _stmt.getText(_columnIndexOfEndDate);
          }
          _item = new Vacation(_tmpVacationID,_tmpTitle,_tmpHotelName,_tmpStartDate,_tmpEndDate);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Vacation getVacationById(final int id) {
    final String _sql = "SELECT * FROM vacations WHERE vacationID = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfVacationID = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "vacationID");
        final int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
        final int _columnIndexOfHotelName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hotelName");
        final int _columnIndexOfStartDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startDate");
        final int _columnIndexOfEndDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endDate");
        final Vacation _result;
        if (_stmt.step()) {
          final int _tmpVacationID;
          _tmpVacationID = (int) (_stmt.getLong(_columnIndexOfVacationID));
          final String _tmpTitle;
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _tmpTitle = null;
          } else {
            _tmpTitle = _stmt.getText(_columnIndexOfTitle);
          }
          final String _tmpHotelName;
          if (_stmt.isNull(_columnIndexOfHotelName)) {
            _tmpHotelName = null;
          } else {
            _tmpHotelName = _stmt.getText(_columnIndexOfHotelName);
          }
          final String _tmpStartDate;
          if (_stmt.isNull(_columnIndexOfStartDate)) {
            _tmpStartDate = null;
          } else {
            _tmpStartDate = _stmt.getText(_columnIndexOfStartDate);
          }
          final String _tmpEndDate;
          if (_stmt.isNull(_columnIndexOfEndDate)) {
            _tmpEndDate = null;
          } else {
            _tmpEndDate = _stmt.getText(_columnIndexOfEndDate);
          }
          _result = new Vacation(_tmpVacationID,_tmpTitle,_tmpHotelName,_tmpStartDate,_tmpEndDate);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteVacationById(final int id) {
    final String _sql = "DELETE FROM vacations WHERE vacationID = ?";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
