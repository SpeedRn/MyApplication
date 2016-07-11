package com.stv.launcher.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.stv.launcher.db.AppBadge;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table APP_BADGE.
*/
public class AppBadgeDao extends AbstractDao<AppBadge, Long> {

    public static final String TABLENAME = "APP_BADGE";

    /**
     * Properties of entity AppBadge.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PackageName = new Property(1, String.class, "packageName", false, "PACKAGE_NAME");
        public final static Property ClassName = new Property(2, String.class, "className", false, "CLASS_NAME");
        public final static Property MsgType = new Property(3, String.class, "msgType", false, "MSG_TYPE");
        public final static Property MsgCount = new Property(4, String.class, "msgCount", false, "MSG_COUNT");
    };


    public AppBadgeDao(DaoConfig config) {
        super(config);
    }
    
    public AppBadgeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'APP_BADGE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'PACKAGE_NAME' TEXT," + // 1: packageName
                "'CLASS_NAME' TEXT," + // 2: className
                "'MSG_TYPE' TEXT," + // 3: msgType
                "'MSG_COUNT' TEXT);"); // 4: msgCount
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'APP_BADGE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AppBadge entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(2, packageName);
        }
 
        String className = entity.getClassName();
        if (className != null) {
            stmt.bindString(3, className);
        }
 
        String msgType = entity.getMsgType();
        if (msgType != null) {
            stmt.bindString(4, msgType);
        }
 
        String msgCount = entity.getMsgCount();
        if (msgCount != null) {
            stmt.bindString(5, msgCount);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AppBadge readEntity(Cursor cursor, int offset) {
        AppBadge entity = new AppBadge( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // packageName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // className
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // msgType
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // msgCount
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AppBadge entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPackageName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setClassName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMsgType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMsgCount(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(AppBadge entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(AppBadge entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
