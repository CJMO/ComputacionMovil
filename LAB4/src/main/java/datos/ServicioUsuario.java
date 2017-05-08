package datos;


import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Semaphore;

import co.edu.udea.compumovil.gr10_20171.lab4.LoginActivity;
import co.edu.udea.compumovil.gr10_20171.lab4.MenuLateral;
import co.edu.udea.compumovil.gr10_20171.lab4.R;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CJMO on 4/04/2017.
 */

public class ServicioUsuario extends AppCompatActivity {

    private String nombre_usuario = "";
    private String correo = "";
    private String edad = "";
    private String foto = "";
    private static String[] datos = new String[4];

    Intent intent;

    String respuesta = "";

    String url_servicio = "http://lab-4-f76ec.firebaseio.com";

    // Declare the FirebaseAuth and AuthStateListener objects.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializar aplicación y obtener instancia
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        // declarar listener para las opciones a realizar
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    public ServicioUsuario(){

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    public String login(String correo, String clave){

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(correo, clave)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                    try {
                        AuthResult resultado = task.getResult();
                        //actualizarVista( resultado.getUser() );

                        // obtener excepción
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        // mostrar excepción
                        respuesta = getResources().getString(R.string.datos_incompletos);

                    } catch (RuntimeExecutionException e) {
                        e.printStackTrace();
                        if( e.getMessage().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.") ){
                            // mostrar excepción
                            respuesta = getResources().getString(R.string.correo_formato_incorrecto);
                        }
                        else if( e.getMessage().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.") ){
                            // mostrar excepción
                            respuesta = getResources().getString(R.string.datos_incorrectos);
                        }
                        else{
                            // mostrar excepción
                            respuesta = e.getMessage().toString();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        // mostrar excepción
                        respuesta = e.getMessage().toString();
                    }

                    // Si falla el registro, mostrar mensaje al usuario.
                    // Si es correcto, el listener para el estado de la autenticación será notificado
                    // y la lógica para manejar el usuario se puede realizar mediante el listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        respuesta = getResources().getString(R.string.auth_failed);
                    }

                    // [START_EXCLUDE]
                    if (!task.isSuccessful()) {
                        respuesta = getResources().getString(R.string.auth_failed);
                    }
                    //hideProgressDialog();
                    // [END_EXCLUDE]
                }
            });
        // [END sign_in_with_email]

        // retornar respuesta
        return respuesta;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        //FirebaseUser usuarioActual = mAuth.getCurrentUser();
        //actualizarVista(usuarioActual);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /*public String consultar_datos_acceso(String usuario) throws JSONException {

        //making object of RestAdapter
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url_servicio).build();

        //Creating Rest Services
        RestInterface restInterface = adapter.create(RestInterface.class);

        // realizar petición de comprobación de datos de usuario
        Object json_respuesta = restInterface.datosUsuario("{\"where\":{\"usuario\":\""+usuario+"\" }}");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(json_respuesta);

        String clave = "NO EXISTE";

        JSONArray jsonArray = new JSONArray(jsonInString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Iterator<String> keys = json.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                if(key.equals("clave")) clave = json.get(key).toString();
                System.out.println("Key :" + key + "  Value :" + json.get(key));
            }

        }
        //System.out.println(jsonArray);

        return clave;

    }*/

    public String[] consultar_datos_usuario(final String usuario) throws JSONException{

        // DatabaseReference datos_usuario =  FirebaseDatabase.getInstance().getReference().child("usuarios");

        Query datos_usuario = FirebaseDatabase.getInstance().getReference().child("usuarios").orderByChild("correo").equalTo(usuario);

        // Attach a listener to read the data at our posts reference
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Datos usuario "+dataSnapshot.getValue().toString());

                Gson gson = new Gson();
                //String jsonInString = gson.toJson(dataSnapshot.getValue());
                String jsonInString = gson.toJson(dataSnapshot.getValue());

                JSONArray jsonArray = null;

                try {
                    jsonArray = new JSONArray(jsonInString);
                    System.out.println(jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        Iterator<String> keys = json.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                            if(key.equals("usuario")) nombre_usuario = json.get(key).toString();
                            else if(key.equals("correo")) correo = json.get(key).toString();
                            else if(key.equals("edad")) edad = json.get(key).toString();
                            else if(key.equals("foto")) foto = json.get(key).toString();
                            //System.out.println("Key :" + key + "  Value :" + json.get(key));
                        }
                    }

                    datos = new String[]{nombre_usuario, correo, foto, edad};

                    getUser(datos);

                    // iniciar navigation
                    /*intent = new Intent(ServicioUsuario.this,MenuLateral.class);
                    intent.putExtra("nombre_usuario",nombre_usuario);
                    intent.putExtra("correo",correo);
                    intent.putExtra("foto",foto);
                    intent.putExtra("edad",edad);
                    startActivity(intent);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };

        datos_usuario.addValueEventListener(listener);

        // Remove listener
        //datos_usuario.removeEventListener(listener);

        /*//making object of RestAdapter
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url_servicio).build();

        //Creating Rest Services
        RestInterface restInterface = adapter.create(RestInterface.class);

        // realizar petición de comprobación de datos de usuario
        Object json_respuesta = restInterface.listaUsuarios();

        System.out.println(jsonArray);*/

        //String[] datos = new String[]{nombre_usuario, correo, foto, edad};

        return datos;
    }

    public void getUser(String[] datos_usuario) {
        setDatos(datos_usuario);
        System.out.println("Temp " + datos[0]);
    }

    public String[] getDatos() {
       return this.datos;
    }
    public void setDatos(String[] valores) {
        this.datos = valores;
    }

    public void registrar_usuario(String usuario,String clave, String correo, String foto, String edad){

        //making object of RestAdapter
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url_servicio).build();

        //Creating Rest Services
        RestInterface restInterface = adapter.create(RestInterface.class);

        String datos = "{\n" +
                "    \"usuario\": \""+usuario+"\",\n" +
                "    \"clave\": \""+clave+"\",\n" +
                "    \"correo\": \""+correo+"\",\n" +
                "    \"foto\": \""+foto+"\",\n" +
                "    \"edad\": \""+edad+"\"\n" +
                "  }";
        restInterface.registroUsuario(datos);
    }

    public void actualizarDatos(String usuario_anterior, String indice, String dato){

        //making object of RestAdapter
        /*RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url_servicio)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        //Creating Rest Services
        RestInterface restInterface = adapter.create(RestInterface.class);

        String where = "{\"usuario\":\""+usuario_anterior+"\"}";
        //String where = "{\"usuario\":\"cjmo\" }";

        String datos = "{\""+indice+"\": \""+dato+"\" }";

        restInterface.actualizarUsuario(where, datos);*/

    }
}
