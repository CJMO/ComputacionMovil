package co.edu.udea.compumovil.gr10_20171.lab2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import datos.LoginDataBaseAdapter;

public class LoginActivity extends AppCompatActivity {

    // adaptador de base de datos
    LoginDataBaseAdapter adaptador;

    // gestión de sesión
    public static final String MyPREFERENCES = "Mis datos" ;
    public static final String Name = "nombre_sesion";
    SharedPreferences sharedpreferences;

    String usuario_sesion;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // conectar a base de datos
        adaptador = new LoginDataBaseAdapter(this);
        adaptador = adaptador.open();

        //logout();

        // inicializar gestor de sesión
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // verificar si hay sesión iniciada
        usuario_sesion = sharedpreferences.getString(Name, null);

        if(usuario_sesion != null){
            System.out.println("SESIÓN LEÍDA");
            intent= new Intent(LoginActivity.this,MenuLateral.class);
            startActivity(intent);
        }

        Button boton_ingresar = (Button)findViewById(R.id.btn_login);

        boton_ingresar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            EditText edit_usuario = (EditText)findViewById(R.id.input_email);
            String usuario = edit_usuario.getText().toString();

            EditText edit_clave = (EditText)findViewById(R.id.input_password);
            String clave = edit_clave.getText().toString();

            String respuesta = String.format("Nombre de usuario: %s - Clave %s", usuario, clave);

            System.out.println(respuesta);

            /*adaptador.insertEntry("cjmo","12345","cjmo@cjmo.com");
            adaptador.insertEntry("admin","12345","admin@admin.com");
            adaptador.insertEntry("usuario","usuario","usuario@usuario.com");*/

            String clave_ingreso = adaptador.getSinlgeEntry(usuario);

            // verificar que usuario existe
            if(!clave_ingreso.equals("NO EXISTE")){
                if( clave_ingreso.equals(clave) ){
                    //System.out.println("El usuario ha ingresado correctamente");
                    Toast.makeText(getBaseContext(), "El usuario ha ingresado correctamente", Toast.LENGTH_LONG).show();

                    // guardar sesión
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(Name, usuario);
                    editor.commit();


                    System.out.println("SESIÓN INICIADA " +usuario_sesion);

                    // iniciar navigation
                    //intent= new Intent(LoginActivity.this,Navigation.class);
                    intent= new Intent(LoginActivity.this,MenuLateral.class);
                    startActivity(intent);

                }
                else Toast.makeText(getBaseContext(), "Datos de ingreso incorrectos", Toast.LENGTH_LONG).show();
                //System.out.println("Datos incorrectos");
            }
            else Toast.makeText(getBaseContext(), "El usuario especificado no se encuentra registrado", Toast.LENGTH_LONG).show();


            //System.out.println("El usuario no existe");

            //adaptador.insertEntry(usuario, clave);
            }
        });


        TextView link_registro = (TextView)findViewById(R.id.link_signup);

        link_registro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Registro.class);
                startActivity(intent);
            }
        });


    }

    //@Override
    /*protected void onResume(Bundle savedInstanceState){

        super.onResume();


        //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        //String usuario_sesion = pref.getString("Name", null);

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String usuario_sesion = sharedpreferences.getString(Name, null);

        System.out.println("SESIÓN "+usuario_sesion);

        if(usuario_sesion != null){
            System.out.println("SESIÓN LEÍDA");
            //intent= new Intent(LoginActivity.this,Navigation.class);
            //startActivity(intent);
        }
        else{
           super.onCreate(savedInstanceState);
        }

    }*/

    public void logout(){
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
}
