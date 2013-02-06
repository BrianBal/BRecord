package com.brecord;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BDatabase extends SQLiteOpenHelper
{

	final public static String LOCK = "brecord_lock";

	private static BDatabase sharedDatabase;

	private static ReadWriteLock rwLock;

	public static void setup(Context context, String name, CursorFactory factory, int version)
	{
		// rwLock = new ReentrantReadWriteLock(true);
		sharedDatabase = new BDatabase(context, name, factory, version);
	}

	private static void beginReadLock()
	{
		rwLock.readLock().lock();
	}

	private static void endReadLock()
	{
		rwLock.readLock().unlock();
	}

	private static void beginWriteLock()
	{
		rwLock.writeLock().lock();
	}

	private static void endWriteLock()
	{
		rwLock.writeLock().unlock();
	}

	public static SQLiteDatabase getReadDatabase()
	{
		// beginReadLock();
		if (sharedDatabase == null)
		{
			sharedDatabase = new BDatabase(BConfig.CONTEXT, BConfig.DATABASE_NAME, null, BConfig.DATABASE_VERSION);
		}
		
		return sharedDatabase.getReadableDatabase();
	}

	public static SQLiteDatabase getDatabase()
	{
		// beginWriteLock();
		if (sharedDatabase == null)
		{
			sharedDatabase = new BDatabase(BConfig.CONTEXT, BConfig.DATABASE_NAME, null, BConfig.DATABASE_VERSION);
		}
		
		return sharedDatabase.getWritableDatabase();
	}

	public BDatabase(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			migrate(db, 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		try
		{
			migrate(db, oldVersion);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public SQLiteDatabase getWritableDatabase()
	{
		return super.getWritableDatabase();
	}

	@Override
	public SQLiteDatabase getReadableDatabase()
	{
		return super.getReadableDatabase();
	}

	public void migrate(SQLiteDatabase db, int currentVersion)
	{
		BMigration.Migrate(db, currentVersion);
	}

	public static void closeDatabase()
	{
		// LOCKED = false;
		// endWriteLock();
		// Log.d("Test", "Thread " + Thread.currentThread().getName() +
		// " released lock");
	}

	public static void closeReadDatabase()
	{
		// LOCKED = false;
		// endReadLock();
		// Log.d("Test", "Thread " + Thread.currentThread().getName() +
		// " released lock");
	}

	public void dropDataBase()
	{
		SQLiteDatabase db = getDatabase();

		// Drop tables
		Cursor c = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'", null);
		if (c.getCount() > 0)
		{
			// App.printCursorContents(c);
			while (c.moveToNext())
			{
				String tableName = c.getString(1);
				if (!(tableName.equals("android_metadata") || tableName.equals("sqlite_sequence")))
				{
					try
					{
						db.execSQL("DROP TABLE " + tableName + ";");
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		c.close();
		closeDatabase();
	}

}
