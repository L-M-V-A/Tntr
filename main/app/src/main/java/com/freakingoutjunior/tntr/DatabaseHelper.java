package com.freakingoutjunior.tntr;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Luis on 3/10/2016.
 
 Class to take the binary SQLite database created containing the necessary data to check
 legality of tint by state. 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "";	//variable to hold path of the R/W database
    private static String DB_NAME ="StateTintLaw.db"; //binary database name
	
    private SQLiteDatabase mDatabase;
    private final Context mContext;

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, 1);
		
		//generate the path for the database accounts for older versions since work on
		//this began on an earlier SDK version.
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }else{
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
		
        this.mContext = context;
    }

    public void createDatabase() throws IOException
    {
        //Check if database exists within the data section
        boolean mDatabaseExists = checkDatabase();
		//if no such database exists create it
        if(!mDatabaseExists)
        {
            this.getReadableDatabase();
            this.close();
			
            try{
                copyDatabase();	//call to generate the database
            }catch (IOException mIOException){
                throw new Error("ErrorCopyingDatabase");
            }
        }
    }

    //Check that the database exists within the data folder
    private boolean checkDatabase()
    {
        File db = new File(DB_PATH + DB_NAME);
        return db.exists();
    }

    //Copy the database from the static SQLite binary contained int eh assets folder,
	//done with usage of byte buffers to result on a system file that the app is granted
	//read and write priviledges. retireved from StackOverflow, before realising there
	//was an existing library SQLiteAssetHelper to handle this.
    private void copyDatabase() throws IOException
    {
		//create inputstream from sqlite binary
        InputStream inDB = mContext.getAssets().open(DB_NAME);
		//create outstream for resultant database
        OutputStream outDB = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] buff = new byte[1024];
        int length;
		//navigate through binary and place contents in accessible database
        while ((length = inDB.read(buff))>0)
        {
            outDB.write(buff, 0, length);
        }
        outDB.flush();
        outDB.close();
        inDB.close();
    }

    //Open database based on the location it is exported/copied to return if said DB exists
    public boolean openDatabase() throws SQLException
    {
        String path = DB_PATH + DB_NAME;
        mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDatabase != null;
    }

	
    @Override
    public synchronized void close()
    {
        if(mDatabase != null)
            mDatabase.close();
		mDatabase = null;
        super.close();
    }

	//inheritance constrctor, not really necessary or used
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    //included as required overrides
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
		//nothing to-do or to implement
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		//nothing to-do or to implement
    }
}
