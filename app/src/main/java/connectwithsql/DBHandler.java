package connectwithsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import retrofit2.http.Query;

public class DBHandler extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Movie.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "My_movies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MOVIE_ID= "movieId";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_OVERVIEW = "overview";
    private static final String COLUMN_RELEASE_DATE = "releaseDate";

    public DBHandler(@Nullable Context context) {
        super(context, "movie.db", null, 1);
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
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static boolean checkMovieExists(int movieId, SQLiteDatabase db){
        String query = "SELECT " + COLUMN_MOVIE_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_MOVIE_ID + " =  " + movieId;
        Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() <= 0){
                cursor.close();
                return true;
            }
        cursor.close();
        return false;
    }

    public void addMovie(int movieId, String title, String overview, String releaseDate){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("SQL", TABLE_NAME);

        if (checkMovieExists(movieId, db) == true){
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

    public Cursor getAllMovieIds(){
        String query = "SELECT " + COLUMN_MOVIE_ID + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
