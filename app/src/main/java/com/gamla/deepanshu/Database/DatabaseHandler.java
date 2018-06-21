package com.gamla.deepanshu.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.DeliveryAdress.AdressBean;

import java.util.ArrayList;

/**
 * Created by Deepanshu on 04-04-2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSON = 4;
    public static final String DATABASE_NAME = "Gamla3.db";
    public static String TABLE_CART = "Cart";
    public static String TABLE_USER = "User";
    public static String TABLE_ADDRESS = "Address";
    public Context _context;

    public static final String KEY_ID = "id";
    public static final String KEY_PRODUCT_ID = "ProductID";
    public static final String KEY_SKU_ID = "SkuId";
    public static final String KEY_PRODUCTCATEGORY = "ProductCategory";
    public static final String KEY_PRODUCT_NAME = "ProductName";
    public static final String KEY_MRP = "Mrp";
    public static final String KEY_SELLING_PRICE = "SellingPrice";
    public static final String KEY_WEIGHT = "Weight";
    public static final String KEY_SHIPIN_DAYS = "ShipInDays";
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_IMAGE_NAME = "ImageName";
    public static final String KEY_QUANTATITY = "Quantatity";
    public static final String KEY_IMAGE_PATH = "ImagePath";
    public static final String KEY_STATUS = "Status";
    public static final String KEY_MOBILE_NUMBER = "MobileNo";
    public static final String KEY_BUISNESS_ID = "BuisnessID";
    public static final String KEY_BUISNESS_TYPE = "BuisnessType";
    public static final String KEY_DISPLAY_NAME = "DisplayName";
    public static final String KEY_REGISTERE_DISPLAY_NAME = "RegisteredDisplayName";
    public static final String KEY_REGISTERED_DISPLAY_ADRESS = "RegistredDisplayAdress";
    public static final String KEY_PIN_CODE = "PinCode";
    public static final String KEY_STATE = "State";
    public static final String KEY_CITY = "City";
    public static final String KEY_PRODUCT_PURCHASE_QUANTATITY = "ProductPurchaseQuantatity";

    public static final String KEY_USER_NAME = "UserName";
    public static final String KEY_USER_PASSWORD = "Password";

    public static final String KEY_FULL_NAME = "FullName";
    public static final String KEY_ADRESS_LINE1 ="AdressLine1";
    public static final String KEY_ADRESS_LINE2 = "AdressLine2";



    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSON);
        _context = context;
        // ac = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE "
                + TABLE_CART + "(" + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SKU_ID
                + " TEXT," + KEY_PRODUCTCATEGORY + " TEXT, " + KEY_PRODUCT_ID
                + " TEXT UNIQUE, " + KEY_PRODUCT_NAME + " TEXT, " + KEY_MRP
                + " TEXT, " + KEY_SELLING_PRICE + " TEXT, "
                + KEY_WEIGHT + " TEXT ," + KEY_SHIPIN_DAYS
                + " TEXT ," + KEY_IMAGE_NAME + " TEXT, "
                + KEY_QUANTATITY + " TEXT, " + KEY_IMAGE_PATH
                + " TEXT, " + KEY_STATUS + " TEXT, " + KEY_MOBILE_NUMBER
                + " TEXT ," + KEY_BUISNESS_ID + " TEXT, "
                + KEY_BUISNESS_TYPE + " TEXT, " + KEY_DESCRIPTION + " TEXT, "
                + KEY_DISPLAY_NAME + " TEXT, "
                + KEY_REGISTERE_DISPLAY_NAME + " TEXT, " + KEY_REGISTERED_DISPLAY_ADRESS + " TEXT," + KEY_PIN_CODE + " TEXT, " + KEY_STATE + " TEXT, " + KEY_CITY + " TEXT, " + KEY_PRODUCT_PURCHASE_QUANTATITY+" TEXT "+")";
        db.execSQL(CREATE_CART_TABLE);

        String CREATE_USER_TABLE = "CREATE TABLE "+ TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +KEY_USER_NAME+" TEXT,"+KEY_USER_PASSWORD+" TEXT"+")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_USER_ADRESS = "CREATE TABLE "+ TABLE_ADDRESS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +KEY_FULL_NAME+" TEXT,"+KEY_MOBILE_NUMBER+" TEXT,"+KEY_PIN_CODE+" TEXT,"+KEY_ADRESS_LINE1+" TEXT,"+KEY_ADRESS_LINE2+" TEXT,"+KEY_CITY+" TEXT,"+KEY_STATE+" TEXT, "+KEY_STATUS+" TEXT "+")";
        db.execSQL(CREATE_USER_ADRESS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);

    }
    public void savecartRecord(ProductlistBean obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;

            values = new ContentValues();
            values.put(KEY_PRODUCT_ID, obj.get_id());
            values.put(KEY_SKU_ID, obj.get_skuid());
            values.put(KEY_PRODUCTCATEGORY, obj.get_productCategory());
            values.put(KEY_PRODUCT_NAME, obj.get_productName());
            values.put(KEY_MRP, obj.get_mrp());
            values.put(KEY_SELLING_PRICE, obj.get_sellingPrice());
            values.put(KEY_WEIGHT, obj.get_weight());
            values.put(KEY_SHIPIN_DAYS, obj.get_shipinDays());
            values.put(KEY_DESCRIPTION, obj.get_desceription());
            values.put(KEY_IMAGE_NAME, obj.get_imageName());
            values.put(KEY_QUANTATITY, obj.get_quantatity());
            values.put(KEY_IMAGE_PATH, obj.get_imagepath());
            values.put(KEY_STATUS, obj.get_status());
            values.put(KEY_MOBILE_NUMBER, obj.get_mobilenumber());
            values.put(KEY_BUISNESS_ID, obj.get_buisnessid());
            values.put(KEY_BUISNESS_TYPE, obj.get_buisnessType());
            values.put(KEY_DISPLAY_NAME, obj.get_displayName());
            values.put(KEY_REGISTERE_DISPLAY_NAME, obj.get_RegisteredDisplayName());
            values.put(KEY_REGISTERED_DISPLAY_ADRESS, obj.get_RegisteredDisplayAdress());
            values.put(KEY_PIN_CODE, obj.get_pincode());
            values.put(KEY_STATE, obj.get_state());
            values.put(KEY_CITY, obj.get_city());
            values.put(KEY_PRODUCT_PURCHASE_QUANTATITY, obj.get_productPurchaseQuantatity());
            db.insertOrThrow(TABLE_CART, null, values);



        db.close();

    }
    public void SaveUserRecord(String Username,String Password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;

            values = new ContentValues();
            values.put(KEY_USER_NAME,Username);
            values.put(KEY_USER_PASSWORD,Password);
            db.insert(TABLE_USER,null,values);

        db.close();
    }

    public ArrayList<String> getUserRecord()
    {
        ArrayList<String> objArraylist = new ArrayList<String>();
        Cursor c = null;
        SQLiteDatabase db = null;
        try {

            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_USER;
            System.out.println("query=========>" + query);
            c = db.rawQuery(query, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                      objArraylist.add(c.getString(c.getColumnIndex(KEY_USER_NAME)));

                      objArraylist.add(c.getString(c.getColumnIndex(KEY_USER_PASSWORD)));

                    } while (c.moveToNext());
                }
            }

            return objArraylist;
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (c != null) {
                c.close();

            }
            db.close();
        }
        return objArraylist;

    }
    public void deleteTable(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
        db.close();

    }
    public ArrayList<ProductlistBean> getCartRecord() {
        ArrayList<ProductlistBean> objArraylist = new ArrayList<ProductlistBean>();
        Cursor c = null;
        SQLiteDatabase db = null;
        try {

            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_CART;
            System.out.println("query=========>" + query);
            c = db.rawQuery(query, null);
            ProductlistBean obj;
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        obj = new ProductlistBean();
                        obj.set_state(c.getString(c.getColumnIndex(KEY_STATE)));
                        obj.set_RegisteredDisplayName(c.getString(c.getColumnIndex(KEY_REGISTERE_DISPLAY_NAME)));
                        obj.set_RegisteredDisplayAdress(c.getString(c.getColumnIndex(KEY_REGISTERED_DISPLAY_ADRESS)));
                        obj.set_pincode(c.getString(c.getColumnIndex(KEY_PIN_CODE)));
                        obj.set_displayName(c.getString(c.getColumnIndex(KEY_DISPLAY_NAME)));
                        obj.set_city(c.getString(c.getColumnIndex(KEY_CITY)));
                        obj.set_buisnessType(c.getString(c.getColumnIndex(KEY_BUISNESS_TYPE)));
                        obj.set_buisnessid(c.getString(c.getColumnIndex(KEY_BUISNESS_ID)));
                        obj.set_desceription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                        obj.set_id(c.getString(c.getColumnIndex(KEY_PRODUCT_ID)));
                        obj.set_imageName(c.getString(c.getColumnIndex(KEY_IMAGE_NAME)));
                        obj.set_imagepath(c.getString(c.getColumnIndex(KEY_IMAGE_PATH)));
                        obj.set_mobilenumber(c.getString(c.getColumnIndex(KEY_MOBILE_NUMBER)));
                        obj.set_mrp(c.getString(c.getColumnIndex(KEY_MRP)));
                        obj.set_productCategory(c.getString(c.getColumnIndex(KEY_PRODUCTCATEGORY)));
                        obj.set_productName(c.getString(c.getColumnIndex(KEY_PRODUCT_NAME)));
                        obj.set_quantatity(c.getString(c.getColumnIndex(KEY_QUANTATITY)));
                        obj.set_sellingPrice(c.getString(c.getColumnIndex(KEY_SELLING_PRICE)));
                        obj.set_shipinDays(c.getString(c.getColumnIndex(KEY_SHIPIN_DAYS)));
                        obj.set_skuid(c.getString(c.getColumnIndex(KEY_SKU_ID)));
                        obj.set_status(c.getString(c.getColumnIndex(KEY_STATUS)));
                        obj.set_weight(c.getString(c.getColumnIndex(KEY_WEIGHT)));
                        obj.set_productPurchaseQuantatity(c.getString(c.getColumnIndex(KEY_PRODUCT_PURCHASE_QUANTATITY)));


                        objArraylist.add(obj);
                    } while (c.moveToNext());
                }
            }

            return objArraylist;
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (c != null) {
                c.close();

            }
            db.close();
        }
        return objArraylist;

    }
    public void updateQunatatity(String productid,String purchaseQuantatity,String updatePrice)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_PURCHASE_QUANTATITY, purchaseQuantatity);
        values.put(KEY_SELLING_PRICE,updatePrice);
        db.update(TABLE_CART, values, KEY_PRODUCT_ID + " = '" + productid+"'", null);
        db.close();

    }
    public void deleteRecordFromCart(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_PRODUCT_ID + " = '" + id+"'", null);
        db.close();

    }
    public String getToatalAmmount() {
        Cursor c = null;
        String totalamount=null;
        SQLiteDatabase db = null;
        try {

            db = this.getReadableDatabase();
            String query = "SELECT SUM(SellingPrice) AS TotalPrice  FROM " + TABLE_CART;
            c = db.rawQuery(query,null);
            if(c!=null){
               if(c.moveToFirst())
               {
                   do{
                        totalamount = c.getString(c.getColumnIndex("TotalPrice"));
                   }
                   while (c.moveToNext());
               }
            }
        }
        catch (Exception e)
        {

        }
        finally {
            if (c != null) {
                c.close();

            }
            db.close();
        }
        return totalamount;
    }
    public void SaveUserAdress(AdressBean objBean)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        System.out.println("adress status ----------------->"+objBean.get_status());
            values.put(KEY_FULL_NAME,objBean.get_fullname());
            values.put(KEY_MOBILE_NUMBER,objBean.get_mobilenumber());
            values.put(KEY_PIN_CODE,objBean.get_pincode());
            values.put(KEY_ADRESS_LINE1,objBean.get_adress1());
            values.put(KEY_ADRESS_LINE2,objBean.get_adress2());
            values.put(KEY_CITY,objBean.get_city());
            values.put(KEY_STATE,objBean.get_state());
            values.put(KEY_STATUS,objBean.get_status());
            db.insert(TABLE_ADDRESS,null,values);
        db.close();
    }
    public void UpdateAddressStatus(String id,String status)
    {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, "Not Default");
        ContentValues values1 = new ContentValues();
        values1.put(KEY_STATUS, "Default");
        db.update(TABLE_ADDRESS, values, KEY_STATUS + " = '" + status+"'", null);
        db.update(TABLE_ADDRESS, values1, KEY_ID + " = '" + id+"'", null);
        db.close();

    }
    public ArrayList<AdressBean> GetUserAdress(String status)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AdressBean> objArrayList = new ArrayList<>();
        AdressBean objBean;
        String sql;
        if(status.length()>0) {
             sql = "Select * from " + TABLE_ADDRESS + " Where " + KEY_STATUS + " = '" + status + "'";
        }
        else
        {
             sql = "Select * from " + TABLE_ADDRESS;
        }
        Cursor c = null;
        try{
            c= db.rawQuery(sql,null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        objBean = new AdressBean();
                        objBean.set_state(c.getString(c.getColumnIndex(KEY_STATE)));
                        objBean.set_pincode(c.getString(c.getColumnIndex(KEY_PIN_CODE)));
                        objBean.set_mobilenumber(c.getString(c.getColumnIndex(KEY_MOBILE_NUMBER)));
                        objBean.set_fullname(c.getString(c.getColumnIndex(KEY_FULL_NAME)));
                        objBean.set_city(c.getString(c.getColumnIndex(KEY_CITY)));
                        objBean.set_adress2(c.getString(c.getColumnIndex(KEY_ADRESS_LINE2)));
                        objBean.set_adress1(c.getString(c.getColumnIndex(KEY_ADRESS_LINE1)));
                        objBean.set_id(c.getString(c.getColumnIndex(KEY_ID)));
                        objArrayList.add(objBean);
                    } while (c.moveToNext());
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if(c!=null)
            {
                c.close();
            }
            db.close();
        }
        return objArrayList;

    }
    public int getRowCount(String Table) {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + Table;

        Cursor c = null;

        try {
            c = db.rawQuery(query, null);
            count = c.getCount();
        } catch (Exception exception) {
            exception.printStackTrace();

        } finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
        return count;
    }

}