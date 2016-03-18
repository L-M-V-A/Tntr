package com.freakingoutjunior.tntr;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Luis on 3/10/2016.
 
 Class to serve as an API to the database used by the app.
 */
 
 
public class DatabaseAdapter {

    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

	//create n instance of the new database while constructing
    public DatabaseAdapter(Context context)
    {
        this.mContext = context;
        mDbHelper = new DatabaseHelper(mContext);
    }

	//Method that calls on the creation of the database from the assets folder accessible to
	//the app.
    public DatabaseAdapter createDatabase() throws SQLException
    {
        try{
            mDbHelper.createDataBase();
        }catch (IOException exception){
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DatabaseAdapter open() throws SQLException
    {
        try{
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }catch (SQLException exception){
            throw exception;
        }
        return this;
    }

	//close the database safely
    public void close()
    {	mDbHelper.close();	}

	//default getter that retrieves all tuples from the database
    public Cursor getData()
    {
        try{
            String sql ="SELECT * FROM TintTable";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null){
                mCur.moveToNext();
            }
			
            return mCur;
        }catch (SQLException mSQLException){
            throw mSQLException;
        }
    }

    /*retrieves the data by respective state inputed this one should be the one used to compare
    legality of tint.

    The database tuple is organized as:
        TEXT STATE PRIMARY KEY  (States name)
        INT WINDSHIELD
        INT FRONTSIDE
        INT BACKSIDE
        INT REAR
    The int values represent the minimum percentage light the tint must allow. If the value returned
    is 0 it means that there is no restriction on the tint and -1 means that it is prohibited to
    have any tint.
    */
    public Cursor getStateData(String state){
        try{
            String sql = "SELECT * FROM TintTable WHERE STATE = '" + state + "'";
            Cursor mCur = mDb.rawQuery(sql, null);
            if(mCur != null){
                mCur.moveToNext();
            }
            return mCur;
        }catch (SQLException mSQLException){
            throw mSQLException;
        }
    }
}
