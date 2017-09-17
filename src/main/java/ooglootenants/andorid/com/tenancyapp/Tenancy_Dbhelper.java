package ooglootenants.andorid.com.tenancyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Tenancy_Dbhelper extends SQLiteOpenHelper {

    SQLiteDatabase db = this.getWritableDatabase();
    private static final String DATABSE_NAME = "TENANCY.DB";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_LANDLORD =
            "CREATE TABLE " + Tenancycontract.newlandlord.LTABLE_NAME + " ( " +
                    Tenancycontract.newlandlord.LANDLORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    Tenancycontract.newlandlord.LANDLORD_NAME + " TEXT," +
                    Tenancycontract.newlandlord.LANDLORD_PHONE + " TEXT," +
                    Tenancycontract.newlandlord.LANDLORD_EMAIL + " TEXT," +
                    Tenancycontract.newlandlord.LANDLORD_CNIC + " TEXT);";

    private static final String CREATE_RENTER =
            "CREATE TABLE " + Tenancycontract.newrenter.RTABLE_NAME + " ( " +
                    Tenancycontract.newrenter.RENTER_ID + " INTEGER," +
                    Tenancycontract.newrenter.LANDLORD_ID + " TEXT," +
                    Tenancycontract.newrenter.RENTER_NAME + " TEXT," +
                    Tenancycontract.newrenter.RENTER_PHONE + " TEXT," +
                    Tenancycontract.newrenter.RENTER_EMAIL + " TEXT," +
                    Tenancycontract.newrenter.RENTER_CNIC + " TEXT," +
                    Tenancycontract.newrenter.RENTER_PAYMENTDATE + " TEXT," +
                    Tenancycontract.newrenter.RENT_AMOUNT + " TEXT," +
                    Tenancycontract.newrenter.RENT_SECURITY + " TEXT," +
                    Tenancycontract.newrenter.PAYMENT_RECEIVED + " TEXT," +
                    Tenancycontract.newrenter.NOTIFICATIONS + " TEXT," +
                    Tenancycontract.newrenter.RENTER_RENT_NOTIFICATION_DATE + " TEXT);";


    public Tenancy_Dbhelper(Context context) {
        super(context, DATABSE_NAME, null, DATABASE_VERSION);
        Log.e("DATABSE OPERATIONS", "DATABASE CREATED/ OPENED");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_LANDLORD);
        db.execSQL(CREATE_RENTER);
        Log.e("DATABASE OPERATIONS", "TABLES CREATED...");

    }

    public void deletetenentcolumns() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(" DELETE FROM  " + Tenancycontract.newrenter.RTABLE_NAME);
        db.close();
    }

    public void synchronize(String rid, String lid, String name, String phone, String email, String cnic, String paymentdate, String rentamount, String rentsecurity, String paymentreceived, String notifications, String rentnotificationdate) {
        try {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Tenancycontract.newrenter.RENTER_ID, rid);
            contentValues.put(Tenancycontract.newrenter.LANDLORD_ID, lid);
            contentValues.put(Tenancycontract.newrenter.RENTER_NAME, name);
            contentValues.put(Tenancycontract.newrenter.RENTER_PHONE, phone);
            contentValues.put(Tenancycontract.newrenter.RENTER_EMAIL, email);
            contentValues.put(Tenancycontract.newrenter.RENTER_CNIC, cnic);
            contentValues.put(Tenancycontract.newrenter.RENTER_PAYMENTDATE, paymentdate);
            contentValues.put(Tenancycontract.newrenter.RENT_AMOUNT, rentamount);
            contentValues.put(Tenancycontract.newrenter.RENT_SECURITY, rentsecurity);
            contentValues.put(Tenancycontract.newrenter.PAYMENT_RECEIVED, paymentreceived);
            contentValues.put(Tenancycontract.newrenter.NOTIFICATIONS, notifications);
            contentValues.put(Tenancycontract.newrenter.RENTER_RENT_NOTIFICATION_DATE, rentnotificationdate);
            db.insert(Tenancycontract.newrenter.RTABLE_NAME, null, contentValues);
            db.close();
            Log.e("DB OPREATIONS", "ROW INSERTED.....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor gettimedrenters(String date) {
        db = this.getReadableDatabase();
        Cursor cursor;
        cursor = db.query(Tenancycontract.newrenter.RTABLE_NAME, null, Tenancycontract.newrenter.RENTER_RENT_NOTIFICATION_DATE + " =? AND " + Tenancycontract.newrenter.NOTIFICATIONS + " =? ", new String[]{date, "Yes"}, null, null, null);
        return cursor;
    }

    public Cursor getallrenters(String id) {
        SQLiteDatabase db;
        db = this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(" SELECT * FROM " + Tenancycontract.newrenter.RTABLE_NAME + " WHERE " + Tenancycontract.newrenter.LANDLORD_ID + " = '" + id + "'", null);
        Log.e("DB OPREATIONS", "GET ALL ROWS.....");
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
