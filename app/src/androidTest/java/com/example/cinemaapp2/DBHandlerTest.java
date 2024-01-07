package com.example.cinemaapp2;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cinemaapp2.connectwithsql.DBHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DBHandlerTest {

    private DBHandler db;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        db = new DBHandler(context);
    }

    @Test
    public void testAddMovieToList() {
        db.addMovieToList(123456, "Movie Name", "This is my cool movie!", "07-01-2024");
        SQLiteDatabase dbWritable = db.getWritableDatabase();
        Cursor cursor = dbWritable.rawQuery("SELECT * FROM watchList", null);

        assertEquals(1, cursor.getCount());
        cursor.close();
    }

    @After
    public void closeDb() {
        db.close();
    }
}
