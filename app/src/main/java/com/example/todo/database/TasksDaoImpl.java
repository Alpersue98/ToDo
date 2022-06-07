package com.example.todo.database;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.todo.Task;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
@SuppressWarnings({"unchecked", "deprecation"})
final class TasksDao_Impl implements TasksDao {
    private final RoomDatabase __db;

    private final EntityInsertionAdapter<Task> __insertionAdapterOfTask;

    private final SharedSQLiteStatement __preparedStmtOfDeleteAllTasks;

    public TasksDao_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfTask = new EntityInsertionAdapter<Task>(__db) {
            @Override
            public String createQuery() {
                return "INSERT OR ABORT INTO `tasks` (`id`,`name`) VALUES (nullif(?, 0),?)";
            }

            @Override
            public void bind(SupportSQLiteStatement stmt, Task value) {
                stmt.bindLong(1, value.getId());
                if (value.getShortName() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getShortName());
                }
            }
        };
        this.__preparedStmtOfDeleteAllTasks = new SharedSQLiteStatement(__db) {
            @Override
            public String createQuery() {
                final String _query = "delete from tasks";
                return _query;
            }
        };
    }

    @Override
    public void addTask(final Task task) {
        __db.assertNotSuspendingTransaction();
        __db.beginTransaction();
        try {
            __insertionAdapterOfTask.insert(task);
            __db.setTransactionSuccessful();
        } finally {
            __db.endTransaction();
        }
    }

    @Override
    public void insertTasks(final List<Task> tasks) {
        __db.assertNotSuspendingTransaction();
        __db.beginTransaction();
        try {
            __insertionAdapterOfTask.insert(tasks);
            __db.setTransactionSuccessful();
        } finally {
            __db.endTransaction();
        }
    }

    @Override
    public void deleteAllTasks() {
        __db.assertNotSuspendingTransaction();
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllTasks.acquire();
        __db.beginTransaction();
        try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
        } finally {
            __db.endTransaction();
            __preparedStmtOfDeleteAllTasks.release(_stmt);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        final String _sql = "select * from tasks";
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
        __db.assertNotSuspendingTransaction();
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final List<Task> _result = new ArrayList<Task>(_cursor.getCount());
            while(_cursor.moveToNext()) {
                final Task _item;
                final String _tmpName;
                if (_cursor.isNull(_cursorIndexOfName)) {
                    _tmpName = null;
                } else {
                    _tmpName = _cursor.getString(_cursorIndexOfName);
                }
                _item = new Task(_tmpName);
                final long _tmpUid;
                _tmpUid = _cursor.getLong(_cursorIndexOfId);
                _item.setId( (int) _tmpUid);
                _result.add(_item);
            }
            return _result;
        } finally {
            _cursor.close();
            _statement.release();
        }
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}

 */
