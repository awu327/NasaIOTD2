package com.example.myapplication.image;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.myapplication.image.Image;

import java.io.ByteArrayOutputStream;
import java.sql.Array;
import java.util.ArrayList;

// Database class
public class ImageHelper extends SQLiteOpenHelper {

    private static ImageHelper dbInstance;

    // Instantiate the variables to keep the db running.
    protected final static int VERSION_NUM = 1;
    protected final static String DATABASE_NAME = "ImageDB";
    protected final static String TABLE_NAME = "Images";

    protected final static String COL_TITLE = "Title";
    protected final static String COL_DATE = "Date";
    protected final static String COL_IMAGE_BLOB = "Blob";
    protected final static String COL_ID = "_id";

    protected final static int ID_POS = 0;
    protected final static int TITLE_POS = 1;
    protected final static int DATE_POS = 2;
    protected final static int BLOB_POS = 3;


    public ImageHelper(Context ctx) {super(ctx, DATABASE_NAME, null, VERSION_NUM);}

    public static ImageHelper instanceOfDatabase(Context context) {
        if(dbInstance == null) {
            dbInstance = new ImageHelper(context);
        }

        return dbInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_IMAGE_BLOB + " BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public ArrayList<Image> getImageDetailsList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Image> returnList = new ArrayList<Image>();

        try (Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(ID_POS);
                    String title = result.getString(TITLE_POS);
                    String date = result.getString(DATE_POS);
                    returnList.add(new Image(id, title, date));
                }
            }
        }

        return returnList;
    }

    public Image getImage(String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor result = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COL_DATE + " = '" + date + "'", null)) {
            if(result.getCount() == 0)
                return null;
            else {

                result.moveToNext();

                int _id = result.getInt(ID_POS);
                String title = result.getString(TITLE_POS);
                byte[] byteArray = result.getBlob(BLOB_POS);
                Bitmap data = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                return new Image(_id, title, date, data);
            }
        }
    }

    public boolean saveImage(Image image) {
        if(!image.validate() || checkIfExists(image)) {
            Log.d("fail", "fail");
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        image.getData().compress(Bitmap.CompressFormat.PNG, 100, byteArray);

        byte[] img = byteArray.toByteArray();

        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, image.getTitle());
        cv.put(COL_DATE, image.getDate());
        cv.put(COL_IMAGE_BLOB, img);

        Log.d("database object", cv.toString());

        db.insert(TABLE_NAME, null, cv);

        return true;
    }

    // A check to see if something exists or not.
    public boolean checkIfExists(Image image) {
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor result = db.rawQuery(
                "SELECT " + COL_IMAGE_BLOB + " FROM " + TABLE_NAME +
                        " WHERE " + COL_DATE + " = '" + image.getDate() + "'", null)) {
            if(result.getCount() != 0)
                return true;
            else
                return false;
        }
    }

    public boolean deleteImage(Image image) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_NAME, COL_DATE + "=?", new String[]{image.getDate()}) > 0;
    }
}
