package datos;

/**
 * Created by CJMO on 12/03/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class LoginDataBaseAdapter
{
    static final String DATABASE_NAME = "datos.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"LOGIN"+
            "( " +"ID"+" integer primary key autoincrement,"+ "USUARIO text,CLAVE text, CORREO text, FOTO text, EDAD text); ";
            // Variable to hold the database instance
            public  SQLiteDatabase db;
            // Context of the application using the database.
            private final Context context;
            // Database open/upgrade helper
            private DataBaseHelper dbHelper;

    public LoginDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  LoginDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public void insertEntry(String userName,String password, String correo, String foto, String edad)
    {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("USUARIO", userName);
        newValues.put("CLAVE",password);
        newValues.put("CORREO",correo);
        newValues.put("FOTO",foto);
        newValues.put("EDAD",edad);

        // Insert the row into your table
        db.insert("LOGIN", null, newValues);
        //Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }
    public int deleteEntry(String UserName)
    {
        //String id=String.valueOf(ID);
        String where="USUARIO=?";
        int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
    public String getSinlgeEntry(String userName)
    {
        Cursor cursor=db.query("LOGIN", null, " USUARIO=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NO EXISTE";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("CLAVE"));
        cursor.close();
        return password;
    }
    public void actualizar_datos(String usuario_anterior, String userName,String password, String correo, String edad)
    {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        if( !userName.equals("") ) updatedValues.put("USUARIO", userName);
        if( !password.equals("") ) updatedValues.put("CLAVE",password);
        if( !correo.equals("") ) updatedValues.put("CORREO",correo);
        if( !edad.equals("") ) updatedValues.put("EDAD",edad);

        String where="USUARIO = ?";
        db.update("LOGIN",updatedValues, where, new String[]{usuario_anterior});
    }

    public String[] datos_usuario(String userName)
    {
        Cursor cursor=db.query("LOGIN", null, " USUARIO=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        String foto = cursor.getString(cursor.getColumnIndex("FOTO"));
        String edad = cursor.getString(cursor.getColumnIndex("EDAD"));
        String correo = cursor.getString(cursor.getColumnIndex("CORREO"));
        cursor.close();

        String[] datos = new String[]{foto, edad, correo};

        return datos;
    }

    public void lista_usuarios(){
       /* Cursor c = db.rawQuery("SELECT * FROM LOGIN WHERE 0", null);
        try {
            String[] columnNames = c.columnNames();
        } finally {
            c.close();
        }*/
    }

}