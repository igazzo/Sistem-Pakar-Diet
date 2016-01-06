package com.example.ilhamsabar.cobadiet;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ilham sabar on 11/20/2015.
 */
public class SqlLiteDbHelper extends SQLiteOpenHelper {

// All Static variables
// Database Version
    private static final int DATABASE_VERSION = 1;
// Database Name
    private static final String DATABASE_NAME = "diet2.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context ctx;
    public SqlLiteDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }
// Getting single contact

    public PeraturanOCD Get_peraturan() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM dietocd", null);
        if (cursor != null && cursor.moveToFirst()){
            PeraturanOCD cont = new PeraturanOCD(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
// return contact
            cursor.close();
            db.close();
            return cont;
        }
        return null;
    }


    public void CopyDataBaseFromAsset() throws IOException{
        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);
// Path to the just created empty db
        String outFileName = getDatabasePath();
// if the path doesn't exist first, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
        f.mkdir();
// Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
// transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);

        }
// Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }


    public SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {

                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);

    }

    @Override

    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub
    }
}


