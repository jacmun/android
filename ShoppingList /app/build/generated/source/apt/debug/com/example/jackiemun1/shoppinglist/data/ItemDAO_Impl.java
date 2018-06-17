package com.example.jackiemun1.shoppinglist.data;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO_Impl implements ItemDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfItem;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfItem;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfItem;

  public ItemDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfItem = new EntityInsertionAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Item`(`itemID`,`itemName`,`description`,`itemPrice`,`itemStatus`,`itemType`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        stmt.bindLong(1, value.getItemID());
        if (value.getItemName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getItemName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        stmt.bindDouble(4, value.getItemPrice());
        final int _tmp;
        _tmp = value.getItemStatus() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        stmt.bindLong(6, value.getItemType());
      }
    };
    this.__deletionAdapterOfItem = new EntityDeletionOrUpdateAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Item` WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        stmt.bindLong(1, value.getItemID());
      }
    };
    this.__updateAdapterOfItem = new EntityDeletionOrUpdateAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Item` SET `itemID` = ?,`itemName` = ?,`description` = ?,`itemPrice` = ?,`itemStatus` = ?,`itemType` = ? WHERE `itemID` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        stmt.bindLong(1, value.getItemID());
        if (value.getItemName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getItemName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        stmt.bindDouble(4, value.getItemPrice());
        final int _tmp;
        _tmp = value.getItemStatus() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        stmt.bindLong(6, value.getItemType());
        stmt.bindLong(7, value.getItemID());
      }
    };
  }

  @Override
  public long insertItem(Item item) {
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfItem.insertAndReturnId(item);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Item item) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteUsers(Item... items) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfItem.handleMultiple(items);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(Item item) {
    __db.beginTransaction();
    try {
      __updateAdapterOfItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Item> getAll() {
    final String _sql = "SELECT * FROM item";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfItemID = _cursor.getColumnIndexOrThrow("itemID");
      final int _cursorIndexOfItemName = _cursor.getColumnIndexOrThrow("itemName");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfItemPrice = _cursor.getColumnIndexOrThrow("itemPrice");
      final int _cursorIndexOfItemStatus = _cursor.getColumnIndexOrThrow("itemStatus");
      final int _cursorIndexOfItemType = _cursor.getColumnIndexOrThrow("itemType");
      final List<Item> _result = new ArrayList<Item>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Item _item;
        final String _tmpItemName;
        _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final double _tmpItemPrice;
        _tmpItemPrice = _cursor.getDouble(_cursorIndexOfItemPrice);
        final boolean _tmpItemStatus;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfItemStatus);
        _tmpItemStatus = _tmp != 0;
        final int _tmpItemType;
        _tmpItemType = _cursor.getInt(_cursorIndexOfItemType);
        _item = new Item(_tmpItemName,_tmpDescription,_tmpItemPrice,_tmpItemStatus,_tmpItemType);
        final long _tmpItemID;
        _tmpItemID = _cursor.getLong(_cursorIndexOfItemID);
        _item.setItemID(_tmpItemID);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
