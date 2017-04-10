package datos;

/**
 * Created by CJMO on 12/03/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper
{
    public DataBaseHelper(Context context, String name,CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        //context.deleteDatabase("LOGIN");
    }
    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase _db)
    {
        // base de datos de usuarios
        _db.execSQL(LoginDataBaseAdapter.DATABASE_CREATE);
        // insertar datos de prueba
        _db.execSQL("INSERT INTO LOGIN(USUARIO,CLAVE,CORREO,FOTO,EDAD) VALUES ('cjmo','12345','cjmo@cjmo.com','img1','25')");
        _db.execSQL("INSERT INTO LOGIN(USUARIO,CLAVE,CORREO,FOTO,EDAD) VALUES ('admin','admin-91','admin@admin.com','img2','45')");
        _db.execSQL("INSERT INTO LOGIN(USUARIO,CLAVE,CORREO,FOTO,EDAD) VALUES ('usuario','usuario','usuario@usuario.com','img3','20')");
        _db.execSQL("INSERT INTO LOGIN(USUARIO,CLAVE,CORREO,FOTO,EDAD) VALUES ('invitado','invitado','invitado@udea.edu.co','img4','20')");

        // base de datos de eventos
        _db.execSQL(EventDataBaseAdapter.DATABASE_CREATE);
        _db.execSQL("INSERT INTO EVENTOS(NOMBRE,ENCARGADO,PUNTUACION,FOTO,FECHA,UBICACION,INFORMACION,COORDENADAS) " +
                "VALUES ('Carrera Atlética Facultad de Ciencias Farmacéuticas y Alimentarias','Joselito Carnaval','3/5','evento_carrera','Octubre 30 2017','Circunvalar UdeA','6ta Carrera Atlética Facultad de Ciencias Farmacéuticas y Alimentarias UdeA','6.2692227,-75.5701272' )");
        /*_db.execSQL("INSERT INTO EVENTOS(NOMBRE,ENCARGADO,PUNTUACION,FOTO,FECHA,UBICACION,INFORMACION,COORDENADAS) " +
                "VALUES ('Evento de caridad','Melquiades','4.5/5','evento_caridad','Octubre 30 2017','Circunvalar UdeA','6ta Carrera Atlética Facultad de Ciencias Farmacéuticas y Alimentarias UdeA','6.2692227,-75.5701272' )");
        _db.execSQL("INSERT INTO EVENTOS(NOMBRE,ENCARGADO,PUNTUACION,FOTO,FECHA,UBICACION,INFORMACION,COORDENADAS) " +
                "VALUES ('Campeonato Nacional de Patinaje Artístico sobre hielo','Federación Argentina de Patinaje Sobre Hielo (FAPH)','4.7/5','evento_patinaje','Marzo 25 2017 A las 20:30 hs','Circunvalar UdeA','La Federación Argentina de Patinaje Sobre Hielo (FAPH) realizará una nueva edición de \"Argentina Patina Sobre Hielo\" que contará con la producción integral de Teammedia Eventos conjuntamente con Imagen Deportiva.','-34.613535,-58.4535854' )");
        */
    }
    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion)
    {
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");

        // Upgrade the existing database to conform to the new version. Multiple
        // previous versions can be handled by comparing _oldVersion and _newVersion
        // values.
        // The simplest case is to drop the old table and create a new one.
        _db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
        // Create a new one.
        onCreate(_db);
    }

}