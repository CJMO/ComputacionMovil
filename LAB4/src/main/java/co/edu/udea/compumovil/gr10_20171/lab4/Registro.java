package co.edu.udea.compumovil.gr10_20171.lab4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import datos.Evento;
import datos.ImageFilePath;
import datos.LoginDataBaseAdapter;
import datos.ServicioEventos;
import datos.ServicioUsuario;
import datos.Usuario;

public class Registro extends AppCompatActivity implements View.OnClickListener{

    //ServicioUsuario servicio = new ServicioUsuario();

    // Declare the FirebaseAuth and AuthStateListener objects.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG;

    private String ruta_imagen = "";

    private static final int FILE_SELECT_CODE = 0;

    private void elegirImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult( Intent.createChooser(intent, "Selecciona la imagen para usar en tu perfil"), FILE_SELECT_CODE);

        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Por favor instala un gestor de archivos.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == FILE_SELECT_CODE) {
                if (null == data) return;
                String selectedImagePath;
                Uri selectedImageUri = data.getData();
                selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
                // asignar ruta de imagen
                this.ruta_imagen = selectedImagePath;
                Log.i("Ruta de la imagen", ""+selectedImagePath);
                //Toast.makeText(this, "Image File Path "+selectedImagePath, Toast.LENGTH_SHORT).show();

                //subir_imagen();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        Spinner edades = (Spinner) findViewById(R.id.spinner_edad);
        List<Integer> lista = new ArrayList<Integer>();

        for(int edad=15; edad<=90; ++edad){
            lista.add(edad);
        }

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, lista);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edades.setAdapter(dataAdapter);

        Button btn_imagen = (Button)findViewById(R.id.btn_imagen);

        btn_imagen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                elegirImagen();
            }
        });

        Button btn_registro = (Button)findViewById(R.id.btn_registro);

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

        btn_registro.setOnClickListener(this);

    }

    public String subir_imagen(){
        // File or Blob
        if(!this.ruta_imagen.equals("")){
            Uri file = Uri.fromFile(new File(this.ruta_imagen));

            // Create the file metadata
            StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg").build();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://lab-4-f76ec.appspot.com/");

            UploadTask uploadTask = storageRef.child("usuarios/"+file.getLastPathSegment()).putFile(file, metadata);

            // Listen for state changes, errors, and completion of the upload.
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Handle successful uploads on complete
                    Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                    System.out.println("Ruta "+downloadUrl);
                }
            });

            return file.getLastPathSegment();
        }
        return null;

    }

    public void crearCuenta(final String usuario, final String correo, final String clave, final String edad, final String foto){

        if(correo.equals("") || clave.equals("")){
            Toast.makeText(Registro.this, R.string.datos_incompletos, Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, clave)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                //Toast.makeText(Registro.this, "Registro realizado correctamente", Toast.LENGTH_SHORT).show();

                // guardar foto de perfil
                String nombre_foto = subir_imagen();
                if(nombre_foto == null){
                    nombre_foto = foto;
                }

                //guardar datos de usuario
                DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("usuarios");

                DatabaseReference newPostRef = postsRef.push();
                newPostRef.setValue(new Usuario(usuario, correo, clave, edad, nombre_foto));

                // We can also chain the two calls together
                //postsRef.push().setValue(new Post("alanisawesome", "The Turing Machine"));
                // fin guardar

                Toast.makeText(Registro.this, "Usuario " + usuario + " registrado satisfactoriamente", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Registro.this,LoginActivity.class);
                startActivity(intent);

                // Si falla el registro, mostrar mensaje al usuario.
                // Si es correcto, el listener para el estado de la autenticación será notificado
                // y la lógica para manejar el usuario se puede realizar mediante el listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(Registro.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        // obtener datos de registro
        EditText edit_correo = (EditText)findViewById(R.id.input_email);
        String correo = edit_correo.getText().toString();

        EditText edit_usuario = (EditText)findViewById(R.id.input_usuario);
        String usuario = edit_usuario.getText().toString();

        EditText edit_clave = (EditText)findViewById(R.id.input_password);
        String clave = edit_clave.getText().toString();

        Spinner campo_edad = (Spinner)findViewById(R.id.spinner_edad);
        String edad = campo_edad.getSelectedItem().toString();

        String foto = "img_perfil";

        if (i == R.id.btn_registro) {
            crearCuenta(usuario, correo, clave, edad, foto);
        }
    }
}
