package com.dmcdesigns.d308_mobile.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.dmcdesigns.d308_mobile.entities.Excursion;
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
public final class ExcursionDAO_Impl implements ExcursionDAO {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Excursion> __insertAdapterOfExcursion;

  private final EntityDeleteOrUpdateAdapter<Excursion> __deleteAdapterOfExcursion;

  private final EntityDeleteOrUpdateAdapter<Excursion> __updateAdapterOfExcursion;

  public ExcursionDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExcursion = new EntityInsertAdapter<Excursion>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `excursions` (`excursionID`,`excursionName`,`excDate`,`vacationID`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Excursion entity) {
        statement.bindLong(1, entity.getExcursionID());
        if (entity.getExcursionName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getExcursionName());
        }
        if (entity.getExcDate() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getExcDate());
        }
        statement.bindLong(4, entity.getVacationID());
      }
    };
    this.__deleteAdapterOfExcursion = new EntityDeleteOrUpdateAdapter<Excursion>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `excursions` WHERE `excursionID` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Excursion entity) {
        statement.bindLong(1, entity.getExcursionID());
      }
    };
    this.__updateAdapterOfExcursion = new EntityDeleteOrUpdateAdapter<Excursion>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `excursions` SET `excursionID` = ?,`excursionName` = ?,`excDate` = ?,`vacationID` = ? WHERE `excursionID` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Excursion entity) {
        statement.bindLong(1, entity.getExcursionID());
        if (entity.getExcursionName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getExcursionName());
        }
        if (entity.getExcDate() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getExcDate());
        }
        statement.bindLong(4, entity.getVacationID());
        statement.bindLong(5, entity.getExcursionID());
      }
    };
  }

  @Override
  public long insert(final Excursion excursion) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfExcursion.insertAndReturnId(_connection, excursion);
    });
  }

  @Override
  public void insertAll(final List<Excursion> excursions) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExcursion.insert(_connection, excursions);
      return null;
    });
  }

  @Override
  public void delete(final Excursion excursion) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfExcursion.handle(_connection, excursion);
      return null;
    });
  }

  @Override
  public void update(final Excursion excursion) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfExcursion.handle(_connection, excursion);
      return null;
    });
  }

  @Override
  public List<Excursion> getAllExcursions() {
    final String _sql = "SELECT * FROM excursions ORDER BY excursionID ASC;";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfExcursionID = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "excursionID");
        final int _columnIndexOfExcursionName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "excursionName");
        final int _columnIndexOfExcDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "excDate");
        final int _columnIndexOfVacationID = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "vacationID");
        final List<Excursion> _result = new ArrayList<Excursion>();
        while (_stmt.step()) {
          final Excursion _item;
          final int _tmpExcursionID;
          _tmpExcursionID = (int) (_stmt.getLong(_columnIndexOfExcursionID));
          final String _tmpExcursionName;
          if (_stmt.isNull(_columnIndexOfExcursionName)) {
            _tmpExcursionName = null;
          } else {
            _tmpExcursionName = _stmt.getText(_columnIndexOfExcursionName);
          }
          final String _tmpExcDate;
          if (_stmt.isNull(_columnIndexOfExcDate)) {
            _tmpExcDate = null;
          } else {
            _tmpExcDate = _stmt.getText(_columnIndexOfExcDate);
          }
          final int _tmpVacationID;
          _tmpVacationID = (int) (_stmt.getLong(_columnIndexOfVacationID));
          _item = new Excursion(_tmpExcursionID,_tmpExcursionName,_tmpExcDate,_tmpVacationID);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Excursion> getExcursionsByVacationId(final int vacationId) {
    final String _sql = "SELECT * FROM excursions WHERE vacationID = ? ORDER BY excDate ASC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, vacationId);
        final int _columnIndexOfExcursionID = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "excursionID");
        final int _columnIndexOfExcursionName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "excursionName");
        final int _columnIndexOfExcDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "excDate");
        final int _columnIndexOfVacationID = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "vacationID");
        final List<Excursion> _result = new ArrayList<Excursion>();
        while (_stmt.step()) {
          final Excursion _item;
          final int _tmpExcursionID;
          _tmpExcursionID = (int) (_stmt.getLong(_columnIndexOfExcursionID));
          final String _tmpExcursionName;
          if (_stmt.isNull(_columnIndexOfExcursionName)) {
            _tmpExcursionName = null;
          } else {
            _tmpExcursionName = _stmt.getText(_columnIndexOfExcursionName);
          }
          final String _tmpExcDate;
          if (_stmt.isNull(_columnIndexOfExcDate)) {
            _tmpExcDate = null;
          } else {
            _tmpExcDate = _stmt.getText(_columnIndexOfExcDate);
          }
          final int _tmpVacationID;
          _tmpVacationID = (int) (_stmt.getLong(_columnIndexOfVacationID));
          _item = new Excursion(_tmpExcursionID,_tmpExcursionName,_tmpExcDate,_tmpVacationID);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Excursion> getExcursionsForVacation(final int vacationId) {
    final String _sql = "SELECT * FROM excursions WHERE vacationID = ? ORDER BY excDate ASC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, vacationId);
        final int _columnIndexOfExcursionID = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "excursionID");
        final int _columnIndexOfExcursionName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "excursionName");
        final int _columnIndexOfExcDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "excDate");
        final int _columnIndexOfVacationID = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "vacationID");
        final List<Excursion> _result = new ArrayList<Excursion>();
        while (_stmt.step()) {
          final Excursion _item;
          final int _tmpExcursionID;
          _tmpExcursionID = (int) (_stmt.getLong(_columnIndexOfExcursionID));
          final String _tmpExcursionName;
          if (_stmt.isNull(_columnIndexOfExcursionName)) {
            _tmpExcursionName = null;
          } else {
            _tmpExcursionName = _stmt.getText(_columnIndexOfExcursionName);
          }
          final String _tmpExcDate;
          if (_stmt.isNull(_columnIndexOfExcDate)) {
            _tmpExcDate = null;
          } else {
            _tmpExcDate = _stmt.getText(_columnIndexOfExcDate);
          }
          final int _tmpVacationID;
          _tmpVacationID = (int) (_stmt.getLong(_columnIndexOfVacationID));
          _item = new Excursion(_tmpExcursionID,_tmpExcursionName,_tmpExcDate,_tmpVacationID);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteExcursionById(final int id) {
    final String _sql = "DELETE FROM excursions WHERE excursionID = ?";
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
