    package com.example.nike;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    import com.example.nike.Model.AdminCategory;
    import com.example.nike.Model.ProductAdmin;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    public class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "ecommerce.db";
        private static final int DATABASE_VERSION = 2;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "email TEXT UNIQUE, " +  // Thêm cột email
                    "name TEXT, " +
                    "dob TEXT, " +
                    "password TEXT)");
            String createCategoryTable = "CREATE TABLE categories (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "image TEXT)";

            String createProductTable = "CREATE TABLE products (" +
                    "idProduct INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nameProduct TEXT, " +
                    "priceProduct TEXT, " +
                    "sizeProduct TEXT, " +
                    "idCategory INTEGER, " +
                    "imageUrlProduct TEXT, " +  // 🆕 Thêm cột này để lưu URL ảnh
                    "FOREIGN KEY(idCategory) REFERENCES categories(id))";


            db.execSQL(createCategoryTable);
            db.execSQL(createProductTable);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS categories");
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS product");
            onCreate(db);
        }



        public Boolean insertData(String email,String name,String dob ,String password){
            SQLiteDatabase Mydatabase =this.getWritableDatabase();
            ContentValues contentValues =new ContentValues();
            contentValues.put("email", email);
            contentValues.put("name", name);
            contentValues.put("dob", dob);
            contentValues.put("password", password);
            long result=Mydatabase.insert("users",null, contentValues);
            if (result == -1) {
                System.out.println("Lỗi: Không thể chèn dữ liệu vào database!");
                return false;
            } else {
                System.out.println("Thêm dữ liệu thành công!");
                return true;
            }
        }
        public Boolean checkEmail(String email){
            SQLiteDatabase MyDatabase=this.getWritableDatabase();
            Cursor cursor=MyDatabase.rawQuery("Select * from users where email= ?",new String[]{email});
            if (cursor.getCount()>0){
                return  true;
            }else {
                return false;
            }
        }
        public Boolean CheckEmailPassword(String email,String password){
            SQLiteDatabase MyDatabase=this.getWritableDatabase();
            Cursor cursor=MyDatabase.rawQuery("Select * from users where email =? and password=?",new String[]{email,password});
            if (cursor.getCount()>0){
                return  true;
            }else {
                return false;
            }
        }
        public int updatepass(String email, String password) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("password", password);

            int result = db.update("users", values, "email=?", new String[]{email});
            db.close();
            return result;
        }

    }