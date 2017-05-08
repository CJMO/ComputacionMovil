package co.edu.udea.compumovil.gr10_20171.lab4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import datos.ServicioUsuario;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Declare the FirebaseAuth and AuthStateListener objects.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG;

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

        // inicializar gestor de sesión
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // verificar si hay sesión iniciada
        usuario_sesion = sharedpreferences.getString(Name, null);

        /*// tiempo de actualización por defecto: 30 segundos
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("tiempo_actualizacion", "30");
        editor.commit();*/

        if(usuario_sesion != null){

            // lanzar servicio

            /*startService(new Intent(this, ServicioBOOT.class));
            System.out.println("Servicio registrado");*/

            //System.out.println("SESIÓN LEÍDA");
            intent = new Intent(LoginActivity.this,MenuLateral.class);
            startActivity(intent);
        }

        Button boton_ingresar = (Button)findViewById(R.id.btn_login);

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

        boton_ingresar.setOnClickListener(this);

        /*boton_ingresar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            ServicioUsuario servicio = new ServicioUsuario();

            EditText edit_usuario = (EditText)findViewById(R.id.input_email);
            String usuario = edit_usuario.getText().toString();

            EditText edit_clave = (EditText)findViewById(R.id.input_password);
            String clave = edit_clave.getText().toString();

            String respuesta = String.format("Nombre de usuario: %s - Clave %s", usuario, clave);
            System.out.println(respuesta);

            if(usuario.equals("") || clave.equals("")){
                Toast.makeText(getBaseContext(), R.string.datos_incompletos, Toast.LENGTH_SHORT).show();
                return;
            }

            String respuesta_login = "";

            try {
                respuesta_login = servicio.login(usuario, clave);
            } catch (Exception e) {
                System.out.println("Error login "+e.getMessage().toString());
                e.printStackTrace();
            }

            if(respuesta_login.equals("OK")){

                //System.out.println("El usuario ha ingresado correctamente");
                Toast.makeText(getBaseContext(), "El usuario ha ingresado correctamente", Toast.LENGTH_LONG).show();

                // guardar sesión
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Name, usuario);
                editor.commit();

                //System.out.println("SESIÓN INICIADA " +usuario_sesion);

                // iniciar navigation
                //intent= new Intent(LoginActivity.this,Navigation.class);
                intent = new Intent(LoginActivity.this,MenuLateral.class);
                startActivity(intent);

            }
            else Toast.makeText(getBaseContext(), respuesta_login, Toast.LENGTH_LONG).show();

            }
        });*/


        TextView link_registro = (TextView)findViewById(R.id.link_signup);

        link_registro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this,Registro.class);
            startActivity(intent);
            }
        });

    }

    private void ingresar(String email, String password) {

        if(email.equals("") || password.equals("")){
            Toast.makeText(LoginActivity.this, R.string.datos_incompletos, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "signIn:" + email);

        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                AuthResult resultado = null;

                try {
                    resultado = task.getResult();

                    //actualizarVista( resultado.getUser() );
                    Toast.makeText(LoginActivity.this, "Ingreso correcto "+resultado.getUser().getEmail(), Toast.LENGTH_SHORT).show();

                    /////
                    /*DatabaseReference datos_usuario = FirebaseDatabase.getInstance().getReference().child("usuarios");

                    // Attach a listener to read the data at our posts reference
                    datos_usuario.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("Forma 2 "+dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });*/

                    /////

                    // guardar sesión
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(Name, resultado.getUser().getEmail());
                    editor.commit();

                    //System.out.println("SESIÓN INICIADA " +usuario_sesion);

                    // iniciar navigation
                    intent = new Intent(LoginActivity.this,MenuLateral.class);
                    startActivity(intent);

                    // obtener excepción
                    throw task.getException();
                }
                catch (FirebaseAuthInvalidCredentialsException e){
                    // mostrar excepción
                    Toast.makeText(LoginActivity.this, R.string.datos_incompletos, Toast.LENGTH_SHORT).show();
                    return;
                } catch (RuntimeExecutionException e) {
                    e.printStackTrace();
                    if( e.getMessage().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.") ){
                        // mostrar excepción
                        Toast.makeText(LoginActivity.this, R.string.correo_formato_incorrecto, Toast.LENGTH_SHORT).show();
                        //return;
                    }
                    else if( e.getMessage().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.") ){
                        // mostrar excepción
                        Toast.makeText(LoginActivity.this, R.string.datos_incorrectos, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // mostrar excepción
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // mostrar excepción
                    Toast.makeText(LoginActivity.this, R.string.auth_failed,Toast.LENGTH_SHORT).show();
                    //return;
                }

                /*ServicioUsuario servicio = new ServicioUsuario();
                String[] resp = new String[4];
                try {
                    resp = servicio.consultar_datos_usuario( resultado.getUser().getEmail() );
                    System.out.println("Usuario "+resp[0]+ " - "+servicio.getDatos()[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                    //Toast.makeText(ActivityFirebase.this, R.string.auth_failed,Toast.LENGTH_SHORT).show();
                }

                // [START_EXCLUDE]
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, R.string.auth_failed,Toast.LENGTH_SHORT).show();
                    //mStatusTextView.setText(R.string.auth_failed);
                }
                //hideProgressDialog();
                // [END_EXCLUDE]
                }
            });
        // [END sign_in_with_email]
    }

    public void crearCuenta(String correo, String clave){

        if(correo.equals("") || clave.equals("")){
            Toast.makeText(LoginActivity.this, R.string.datos_incompletos, Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, clave)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // Si falla el registro, mostrar mensaje al usuario.
                    // Si es correcto, el listener para el estado de la autenticación será notificado
                    // y la lógica para manejar el usuario se puede realizar mediante el listener.
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                    }

                }
            });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        System.out.println("Click");

        EditText edit_usuario = (EditText)findViewById(R.id.input_email);
        String usuario = edit_usuario.getText().toString();

        EditText edit_clave = (EditText)findViewById(R.id.input_password);
        String clave = edit_clave.getText().toString();

        if (i == R.id.btn_login) {
            ingresar(usuario, clave);
        }

        /*if (i == R.id.email_create_account_button) {
            crearCuenta(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }*/
    }

    public void logout(){
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
}