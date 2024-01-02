package connectwithsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Movie4.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "watchList";
    private static final String TABLE_NAME2 = "watchLater";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MOVIE_ID= "movieId";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_OVERVIEW = "overview";
    private static final String COLUMN_RELEASE_DATE = "releaseDate";

    public DBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_MOVIE_ID + " INTEGER, "
                        + COLUMN_TITLE + " TEXT, "
                        + COLUMN_OVERVIEW + " TEXT, "
                        + COLUMN_RELEASE_DATE + " TEXT)";
        String query2 =
                "CREATE TABLE " + TABLE_NAME2 +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_MOVIE_ID + " INTEGER, "
                        + COLUMN_TITLE + " TEXT, "
                        + COLUMN_OVERVIEW + " TEXT, "
                        + COLUMN_RELEASE_DATE + " TEXT)";
        Log.d("SQL", "QUERY 1: "+ query);  // Add this line to log your SQL queries
        Log.d("SQL", "QUERY 2: "+ query2);  // Add this line to log your SQL queries

        db.execSQL(query);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static boolean checkMovieExists(int movieId, SQLiteDatabase db, String tableName){
        String query = "SELECT " + COLUMN_MOVIE_ID + " FROM " + tableName + " WHERE " + COLUMN_MOVIE_ID + " =  " + movieId;
        Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() <= 0){
                cursor.close();
                return true;
            }
        cursor.close();
        return false;
    }

    public void addMovieToList(int movieId, String title, String overview, String releaseDate){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("SQL", TABLE_NAME);

        if (checkMovieExists(movieId, db, TABLE_NAME) == true){
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_MOVIE_ID, movieId);
            cv.put(COLUMN_TITLE, title);
            cv.put(COLUMN_OVERVIEW, overview);
            cv.put(COLUMN_RELEASE_DATE, releaseDate);

            long result = db.insert(TABLE_NAME,null, cv);
            if (result == -1){
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Movie already exists in list", Toast.LENGTH_SHORT).show();
        }

    }
    public void addMovieToLater(int movieId, String title, String overview, String releaseDate){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("SQL", TABLE_NAME2);

        if (checkMovieExists(movieId, db, TABLE_NAME2) == true){
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_MOVIE_ID, movieId);
            cv.put(COLUMN_TITLE, title);
            cv.put(COLUMN_OVERVIEW, overview);
            cv.put(COLUMN_RELEASE_DATE, releaseDate);

            long result = db.insert(TABLE_NAME2,null, cv);
            if (result == -1){
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("DB", String.valueOf(result));
                Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Movie already exists in list", Toast.LENGTH_SHORT).show();
        }

    }


    public Cursor getAllWatchListIds(){
        String query = "SELECT " + COLUMN_MOVIE_ID + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getAllWatchLaterIds(){
        String query = "SELECT " + COLUMN_MOVIE_ID + " FROM " + TABLE_NAME2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void removeMovie(String table, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + table + " WHERE " + COLUMN_MOVIE_ID + " = " + id;
        db.execSQL(query);
        Toast.makeText(context, "Movie removed successfully", Toast.LENGTH_SHORT).show();
    }
}
