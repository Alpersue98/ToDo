package com.example.todo.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
//import androidx.room.RoomOpenHelper.ValidationResult;
//import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
    private volatile TasksDao _tasksDao;

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
        final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
            @Override
            public void createAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
                _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '77934c7868196355f04d7dee267d73f9')");
            }

            @Override
            public void dropAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("DROP TABLE IF EXISTS `tasks`");
                if (mCallbacks != null) {
                    for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
                        mCallbacks.get(_i).onDestructiveMigration(_db);
                    }
                }
            }

            @Override
            protected void onCreate(SupportSQLiteDatabase _db) {
                if (mCallbacks != null) {
                    for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
                        mCallbacks.get(_i).onCreate(_db);
                    }
                }
            }

            @Override
            public void onOpen(SupportSQLiteDatabase _db) {
                mDatabase = _db;
                internalInitInvalidationTracker(_db);
                if (mCallbacks != null) {
                    for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
                        mCallbacks.get(_i).onOpen(_db);
                    }
                }
            }

            @Override
            public void onPreMigrate(SupportSQLiteDatabase _db) {
                DBUtil.dropFtsSyncTriggers(_db);
            }

            @Override
            public void onPostMigrate(SupportSQLiteDatabase _db) {
            }

            @Override
            protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
                final HashMap<String, TableInfo.Column> _columnsPlaces = new HashMap<String, TableInfo.Column>(2);
                _columnsTasks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
                _columnsTasks.put("shortName", new TableInfo.Column("shortName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
                _columnsTasks.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
                _columnsTasks.put("creationDate", new TableInfo.Column("creationDate", "DATE", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
                final HashSet<TableInfo.ForeignKey> _foreignKeysPlaces = new HashSet<TableInfo.ForeignKey>(0);
                final HashSet<TableInfo.Index> _indicesPlaces = new HashSet<TableInfo.Index>(0);
                final TableInfo _infoPlaces = new TableInfo("tasks", _columnsTasks, _foreignKeysTasks, _indicesTasks);
                final TableInfo _existingPlaces = TableInfo.read(_db, "tasks");
                if (! _infoPlaces.equals(_existingPlaces)) {
                    return new RoomOpenHelper.ValidationResult(false, "places(de.hsbremen.mc.myplaces.model.Place).\n"
                            + " Expected:\n" + _infoPlaces + "\n"
                            + " Found:\n" + _existingPlaces);
                }
                return new RoomOpenHelper.ValidationResult(true, null);
            }
        }, "77934c7868196355f04d7dee267d73f9", "5967be0c09c5be3181196ef981005f02");
        final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
                .name(configuration.name)
                .callback(_openCallback)
                .build();
        final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
        return _helper;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
        HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
        return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "tasks");
    }

    @Override
    public void clearAllTables() {
        super.assertNotMainThread();
        final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            _db.execSQL("DELETE FROM `tasks`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            _db.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!_db.inTransaction()) {
                _db.execSQL("VACUUM");
            }
        }
    }

    @Override
    protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
        final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
        _typeConvertersMap.put(TasksDao.class, TasksDao_Impl.getRequiredConverters());
        return _typeConvertersMap;
    }

    @Override
    public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
        final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
        return _autoMigrationSpecsSet;
    }

    @Override
    public List<Migration> getAutoMigrations(
            @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
        return Arrays.asList();
    }

    @Override
    public TasksDao tasksDao() {
        if (_tasksDao != null) {
            return _tasksDao;
        } else {
            synchronized(this) {
                if(_tasksDao == null) {
                    _tasksDao = new TasksDao_Impl(this);
                }
                return _tasksDao;
            }
        }
    }
}
