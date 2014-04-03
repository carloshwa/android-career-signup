package com.doubleencore.careersignup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by carlos on 4/2/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "signup.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<User, Integer> mUserDao;

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase database, final ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to create tables", e);
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqliteDatabase, final ConnectionSource connectionSource,
                          final int oldVer, final int newVer) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to drop tables", e);
        }
    }

    @Override
    public void close() {
        super.close();
        mUserDao = null;
    }

    public Dao<User, Integer> getUserDao() throws SQLException {
        return (null == mUserDao) ? (mUserDao = getDao(User.class)) : mUserDao;
    }

}
