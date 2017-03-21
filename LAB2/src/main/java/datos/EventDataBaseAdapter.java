package datos;

/**
 * Created by CJMO on 12/03/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class EventDataBaseAdapter
{
    static final String DATABASE_NAME = "datos.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"EVENTOS"+
            "( " +"ID"+" integer primary key autoincrement,"+ "NOMBRE text,ENCARGADO text, PUNTUACION text, FOTO text, FECHA text, UBICACION text, INFORMACION text, COORDENADAS text); ";
            // Variable to hold the database instance
            public  SQLiteDatabase db;
            // Context of the application using the database.
            private final Context context;
            // Database open/upgrade helper
            private DataBaseHelper dbHelper;

    public EventDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public EventDataBaseAdapter open() throws SQLException
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

    public void agregar(String nombre,String encargado, String puntuacion, String foto, String fecha, String ubicacion, String info, String coordenadas)
    {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("NOMBRE", nombre);
        newValues.put("ENCARGADO",encargado);
        newValues.put("PUNTUACION",puntuacion);
        newValues.put("FOTO",foto);
        newValues.put("FECHA",fecha);
        newValues.put("UBICACION",ubicacion);
        newValues.put("INFORMACION",info);
        newValues.put("COORDENADAS",coordenadas);

        // Insert the row into your table
        db.insert("EVENTOS", null, newValues);
        //Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }
    public int eliminar(String nombre)
    {
        //String id=String.valueOf(ID);
        String where="nombre=?";
        int numberOFEntriesDeleted= db.delete("EVENTOS", where, new String[]{nombre}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
    /*public String getSinlgeEntry(String nombre)
    {
        Cursor cursor=db.query("EVENTOS", null, " NOMBRE=?", new String[]{nombre}, null, null, null);
        if(cursor.getCount()<1) // NOMBRE No Existe
        {
            cursor.close();
            return "NO EXISTE";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("CLAVE"));
        cursor.close();
        return password;
    }*/
    public void actualizar(int ID, String nombre,String encargado, String puntuacion, String foto, String fecha, String ubicacion, String info, String coordenadas)
    {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Asignar datos ingresados
        updatedValues.put("NOMBRE", nombre);
        updatedValues.put("ENCARGADO",encargado);
        updatedValues.put("PUNTUACION",puntuacion);
        updatedValues.put("FOTO",foto);
        updatedValues.put("FECHA",fecha);
        updatedValues.put("UBICACION",ubicacion);
        updatedValues.put("INFORMACION",info);
        updatedValues.put("COORDENADAS",coordenadas);

        String where="ID = ?";
        db.update("EVENTOS",updatedValues, where, new String[]{ String.valueOf(ID)});
    }

    public String[] datos_evento(int ID)
    {
        Cursor cursor=db.query("EVENTOS", null, " ID=?", new String[]{String.valueOf(ID)}, null, null, null);
        if(cursor.getCount()<1) // Evento No Existe
        {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        String nombre = cursor.getString(cursor.getColumnIndex("NOMBRE"));
        String encargado = cursor.getString(cursor.getColumnIndex("ENCARGADO"));
        String puntuacion = cursor.getString(cursor.getColumnIndex("PUNTUACION"));
        String foto = cursor.getString(cursor.getColumnIndex("FOTO"));
        String fecha = cursor.getString(cursor.getColumnIndex("FECHA"));
        String ubicacion = cursor.getString(cursor.getColumnIndex("UBICACION"));
        String informacion = cursor.getString(cursor.getColumnIndex("INFORMACION"));
        String coordenadas = cursor.getString(cursor.getColumnIndex("COORDENADAS"));
        cursor.close();

        String[] datos = new String[]{nombre, encargado, puntuacion, foto, fecha, ubicacion, informacion, coordenadas};

        return datos;
    }

    public ArrayList<Evento> lista_eventos(){
        String query = "SELECT * FROM EVENTOS ";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Evento> lista=new ArrayList<Evento>();

        while(cursor.moveToNext()){
            Evento evento =new Evento();
            evento.setID(cursor.getInt(0));
            evento.setNombre(cursor.getString(1));
            evento.setEncargado(cursor.getString(2));
            evento.setPuntuacion(cursor.getString(3));
            evento.setFoto(cursor.getString(4));
            evento.setFecha(cursor.getString(5));
            evento.setUbicacion(cursor.getString(6));
            evento.setInformacion(cursor.getString(7));
            evento.setCoordenadas(cursor.getString(8));
            lista.add(evento);
        }

        cursor.close();
        //db.close();
        return lista;
    }

}