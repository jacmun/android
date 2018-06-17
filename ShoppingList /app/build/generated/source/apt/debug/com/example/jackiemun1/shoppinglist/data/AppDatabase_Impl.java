package com.example.jackiemun1.shoppinglist.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;

public class AppDatabase_Impl extends AppDatabase {
  private volatile ItemDAO _itemDAO;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Item` (`itemID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `itemName` TEXT, `description` TEXT, `itemPrice` REAL NOT NULL, `itemStatus` INTEGER NOT NULL, `itemType` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"4b1334e522c7fce887b882d107514a6b\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Item`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsItem = new HashMap<String, TableInfo.Column>(6);
        _columnsItem.put("itemID", new TableInfo.Column("itemID", "INTEGER", true, 1));
        _columnsItem.put("itemName", new TableInfo.Column("itemName", "TEXT", false, 0));
        _columnsItem.put("description", new TableInfo.Column("description", "TEXT", false, 0));
        _columnsItem.put("itemPrice", new TableInfo.Column("itemPrice", "REAL", true, 0));
        _columnsItem.put("itemStatus", new TableInfo.Column("itemStatus", "INTEGER", true, 0));
        _columnsItem.put("itemType", new TableInfo.Column("itemType", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysItem = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesItem = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoItem = new TableInfo("Item", _columnsItem, _foreignKeysItem, _indicesItem);
        final TableInfo _existingItem = TableInfo.read(_db, "Item");
        if (! _infoItem.equals(_existingItem)) {
          throw new IllegalStateException("Migration didn't properly handle Item(com.example.jackiemun1.shoppinglist.data.Item).\n"
                  + " Expected:\n" + _infoItem + "\n"
                  + " Found:\n" + _existingItem);
        }
      }
    }, "4b1334e522c7fce887b882d107514a6b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Item");
  }

  @Override
  public ItemDAO itemDao() {
    if (_itemDAO != null) {
      return _itemDAO;
    } else {
      synchronized(this) {
        if(_itemDAO == null) {
          _itemDAO = new ItemDAO_Impl(this);
        }
        return _itemDAO;
      }
    }
  }
}
