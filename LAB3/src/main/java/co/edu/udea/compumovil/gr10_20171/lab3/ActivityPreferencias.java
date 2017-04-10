package co.edu.udea.compumovil.gr10_20171.lab3;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import datos.LoginDataBaseAdapter;
import datos.ServicioUsuario;

/**
 * Created by CJMO on 1/04/2017.
 */

public class ActivityPreferencias extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String usuario_sesion;

    ServicioUsuario servicio = new ServicioUsuario();

    // gestión de sesión
    public static final String MyPREFERENCES = "Mis datos" ;
    public static final String Name = "nombre_sesion";

    SharedPreferences sharedpreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);


        // inicializar gestor de sesión
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // verificar si hay sesión iniciada
        this.usuario_sesion = sharedpreferences.getString("nombre_sesion", null);
        System.out.println("Iniciando preferencias "+this.usuario_sesion);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // verificar si hay sesión iniciada
        this.usuario_sesion = sharedpreferences.getString("nombre_sesion", null);
        System.out.println("Retomando preferencias "+this.usuario_sesion);

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key)  {
        // datos para actualizar nuevos valores
        String nuevo_usuario = "";
        String nuevo_correo = "";
        String nueva_edad = "";
        String nueva_clave = "";

        //datos para leer preferencias
        String preferencia = "";
        Boolean preferencia_boolean;

        System.out.println("Cambio: "+key );

        if( key.equals("nombre_usuario") ) {
            preferencia = sharedPreferences.getString(key,null);
            if( !preferencia.equals("") && preferencia != null ){
                nuevo_usuario = preferencia;
                servicio.actualizarDatos(this.usuario_sesion, "usuario", nuevo_usuario);

                //actualizar nombre de usuario
                this.usuario_sesion = nuevo_usuario;
                // editar datos de sesión
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Name, nuevo_usuario);
                editor.commit();

                Toast.makeText(getBaseContext(), "Nombre de usuario actualizado correctamente", Toast.LENGTH_LONG).show();

            }

            System.out.println("Cambio nombre usuario "+key +" "+preferencia);
        }
        else if( key.equals("clave") ) {
            preferencia = sharedPreferences.getString(key,null);

            if( !preferencia.equals("") && preferencia != null ){
                nueva_clave = preferencia;
                servicio.actualizarDatos(this.usuario_sesion, "clave", nueva_clave);

                Toast.makeText(getBaseContext(), "Clave de acceso actualizada correctamente", Toast.LENGTH_LONG).show();
            }
            System.out.println("Cambio clave "+key +" "+preferencia);
        }
        else if( key.equals("correo") ){
            preferencia = sharedPreferences.getString(key,null);

            if( !preferencia.equals("") && preferencia != null ){
                nuevo_correo = preferencia;
                servicio.actualizarDatos(this.usuario_sesion, "correo", nuevo_correo);

                Toast.makeText(getBaseContext(), "Correo electrónico actualizado correctamente", Toast.LENGTH_LONG).show();
            }
            System.out.println("Cambio correo "+key +" "+preferencia);
        }
        else if( key.equals("edad") ) {
            preferencia = sharedPreferences.getString(key,null);

            if( !preferencia.equals("") && preferencia != null ){
                nueva_edad = preferencia;
                servicio.actualizarDatos(this.usuario_sesion, "edad", nueva_edad);

                Toast.makeText(getBaseContext(), "Edad actualizada correctamente", Toast.LENGTH_LONG).show();
            }
            System.out.println("Cambio edad "+key +" "+preferencia);
        }
        else if( key.equals("preferencia_notificaciones") ) {
            preferencia_boolean = sharedPreferences.getBoolean(key, true);
            System.out.println("Cambio notificaciones "+key +" "+preferencia);
        }
        else if( key.equals("preferencia_notificaciones_sonido") ) {
            preferencia_boolean = sharedPreferences.getBoolean(key, true);
            System.out.println("Cambio notificaciones sonido "+key +" "+preferencia);
        }

    }

}