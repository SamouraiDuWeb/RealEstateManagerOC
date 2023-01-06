package com.openclassrooms.realestatemanager.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.models.InterestPoints;
import com.openclassrooms.realestatemanager.models.PropertyAddress;
import com.openclassrooms.realestatemanager.models.PropertyPhotos;
import com.openclassrooms.realestatemanager.models.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private String name;
    private Context context;
    public static final String DATABASE_NAME = "Properties.db";
    public static final int DATABASE_VERSION = 1;

    // id , type ,prix ,surface, nbDePieces, description, photos, adresse, InterestPoints, status, dateEntry, dateSold, agent
    public static final String TABLE_NAME = "Properties";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final float COLUMN_PRIX = 0;
    public static final int COLUMN_SURFACE = 0;
    public static final int COLUMN_NBDEPIECES = 0;
    public static final String COLUMN_DESCRIPTION = "description";
    public static final PropertyPhotos COLUMN_PHOTOS = null;
    public static final PropertyAddress COLUMN_ADDRESS = null;
    public static final InterestPoints COLUMN_INTERESTPOINTS = null;
    public static final String COLUMN_STATUS = "status";
    public static final Date COLUMN_DATEENTRY = null;
    public static final Date COLUMN_DATESOLD = null;
    public static final User COLUMN_USER = null;



    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_TYPE + " TEXT NOT NULL, " +
                COLUMN_PRIX + " FLOAT, " +
                COLUMN_SURFACE + " INTEGER, " +
                COLUMN_NBDEPIECES + " INTEGER, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PHOTOS + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_INTERESTPOINTS + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_DATEENTRY + " TEXT, " +
                COLUMN_DATESOLD + " TEXT, " +
                COLUMN_USER + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addProperty(String id, String name, String type, float prix, int surface, int nbDePieces, String description, List<PropertyPhotos> Photos, PropertyAddress address, List<InterestPoints> interestPoints, String status, Date dateEntry, Date dateSold, User user) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("COLUMN_ID", id);
        cv.put("COLUMN_NAME", name);
        cv.put("COLUMN_TYPE", type);
        cv.put("COLUMN_PRIX", prix);
        cv.put("COLUMN_SURFACE", surface);
        cv.put("COLUMN_NBDEPIECES", nbDePieces);
        cv.put("COLUMN_DESCRIPTION", description);
        cv.put("COLUMN_PHOTOS", serializePhotos(Photos));
        cv.put("COLUMN_ADDRESS", serializeAddress(address));
        cv.put("COLUMN_INTERESTPOINTS", serializeInterestPoints(interestPoints));
        cv.put("COLUMN_STATUS", status);
        cv.put("COLUMN_DATEENTRY", serializeDateEntry(dateEntry));
        cv.put("COLUMN_DATESOLD", serializeDateSold(dateSold));
        cv.put("COLUMN_USER", serializeUser(user));

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast toast = Toast.makeText(context, "error", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(context, "success", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private byte[] serializeUser(User user) {

        if (user == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(user);
            oos.flush();
            oos.close();
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    private byte[] serializeDateSold(Date dateSold) {

        if (dateSold == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateSold);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dateSold).getBytes();
    }

    private byte[] serializeDateEntry(Date dateEntry) {

        if (dateEntry == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateEntry);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dateEntry).getBytes();
    }

    private byte[] serializeInterestPoints(List<InterestPoints> interestPoints) {

        if (interestPoints == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = null;
        try {
            dos.writeInt(interestPoints.size());
            for (InterestPoints interestPoint : interestPoints) {

                dos.writeUTF(interestPoint.getName());
                dos.writeUTF(interestPoint.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    private byte[] serializeAddress(PropertyAddress address) {

        if (address == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(address);
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private byte[] serializePhotos(List<PropertyPhotos> photos) throws IOException {

        if (photos == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeInt(photos.size());
            for (PropertyPhotos photo : photos) {
                //to be continued when PropertyPhotos is done
                dos.writeUTF(photo.getPhotoUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public PropertyPhotos readPhotos(byte[] data) {
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            PropertyPhotos dataobj = (PropertyPhotos) ois.readObject();
            return dataobj ;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PropertyAddress readAdress(byte[] data) {
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            PropertyAddress dataobj = (PropertyAddress) ois.readObject();
            return dataobj ;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InterestPoints readInterestPoints(byte[] data) {
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            InterestPoints dataobj = (InterestPoints) ois.readObject();
            return dataobj ;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date readDateEntry(byte[] data) {

        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            Date dataobj = (Date) ois.readObject();
            return dataobj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date readDateSold(byte[] data) {

        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            Date dataobj = (Date) ois.readObject();
            return dataobj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
