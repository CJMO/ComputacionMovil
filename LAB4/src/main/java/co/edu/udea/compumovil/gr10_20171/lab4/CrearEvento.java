package co.edu.udea.compumovil.gr10_20171.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import datos.Evento;
import datos.ImageFilePath;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrearEvento.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrearEvento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearEvento extends Fragment implements View.OnClickListener{

    View vista;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
            startActivityForResult( Intent.createChooser(intent, "Selecciona la imagen del evento"), FILE_SELECT_CODE);

        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "Por favor instala un gestor de archivos.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (resultCode == RESULT_OK) {
            if (requestCode == FILE_SELECT_CODE) {
                if (null == data) return;
                String selectedImagePath;
                Uri selectedImageUri = data.getData();
                selectedImagePath = ImageFilePath.getPath(getContext(), selectedImageUri);
                // asignar ruta de imagen
                this.ruta_imagen = selectedImagePath;
                Log.i("Ruta de la imagen", ""+selectedImagePath);
                //Toast.makeText(this, "Image File Path "+selectedImagePath, Toast.LENGTH_SHORT).show();

                //subir_imagen();
            //}
        }
    }

    public CrearEvento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearEvento.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearEvento newInstance(String param1, String param2) {
        CrearEvento fragment = new CrearEvento();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.crear_evento, container, false);

        Spinner edades = (Spinner) vista.findViewById(R.id.spinner_puntuacion);
        List<Integer> lista = new ArrayList<Integer>();

        for(int puntuacion=1; puntuacion<=5; ++puntuacion){
            lista.add(puntuacion);
        }

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(getContext(),android.R.layout.simple_spinner_item, lista);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edades.setAdapter(dataAdapter);

        Button btn_agregar_evento = (Button)vista.findViewById(R.id.btn_agregar_evento);
        // Inicializar aplicación y obtener instancia
        FirebaseApp.initializeApp(getActivity().getApplicationContext());

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

        btn_agregar_evento.setOnClickListener(this);

        Button btn_imagen = (Button)vista.findViewById(R.id.btn_imagen_evento);

        btn_imagen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                elegirImagen();
            }
        });

        return vista;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        // obtener datos de evento
        EditText edit_nombre = (EditText)getView().findViewById(R.id.input_nombre_evento);
        String nombre = edit_nombre.getText().toString();

        EditText edit_ubicacion = (EditText)getView().findViewById(R.id.input_ubicacion_evento);
        String ubicacion = edit_ubicacion.getText().toString();

        EditText edit_encargado = (EditText)getView().findViewById(R.id.input_encargado_evento);
        String encargado = edit_encargado.getText().toString();

        EditText edit_informacion = (EditText)getView().findViewById(R.id.input_informacion_evento);
        String informacion = edit_informacion.getText().toString();

        EditText edit_fecha = (EditText)getView().findViewById(R.id.input_fecha_evento);
        String fecha = edit_fecha.getText().toString();

        EditText edit_coordenadas = (EditText)getView().findViewById(R.id.input_coordenadas_evento);
        String coordenadas = edit_coordenadas.getText().toString();

        Spinner campo_puntuacion = (Spinner)getView().findViewById(R.id.spinner_puntuacion);
        String puntuacion = campo_puntuacion.getSelectedItem().toString();

        Integer id = 5;

        if (i == R.id.btn_agregar_evento) {

            // guardar foto de evento
            String nombre_foto = subir_imagen();
            if(nombre_foto == null){
                nombre_foto = "evento_4.png";
            }

            Evento nuevo = new Evento(nombre, informacion, ubicacion, fecha, puntuacion, encargado, nombre_foto, coordenadas);
            nuevo.setID(id);
            crear_evento(nuevo);

        }
    }

    public void crear_evento(Evento evento){
        //guardar datos de ecento
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("eventos");

        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(evento);

        Toast.makeText(getActivity().getApplicationContext(), "Evento creado correctamente", Toast.LENGTH_SHORT).show();
        showNotification("Nuevo evento creado!",evento.getNombre());

        Intent intent = new Intent(getActivity(),MenuLateral.class);
    }

    public String subir_imagen(){
        // File or Blob
        if(!this.ruta_imagen.equals("")){
            Uri file = Uri.fromFile(new File(this.ruta_imagen));

            // Create the file metadata
            StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg").build();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://lab-4-f76ec.appspot.com/");

            UploadTask uploadTask = storageRef.child("eventos/"+file.getLastPathSegment()).putFile(file, metadata);

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

    private void showNotification(String title, String text) {

        String titulo = (title == null || title.isEmpty()) ? "Notificación importante" : title;

        Notification.Builder notificationBuilder = new Notification.Builder(getActivity().getApplicationContext())
                .setSmallIcon(R.drawable.evento_1)
                .setContentTitle(titulo)
                .setContentText(text)
                .setAutoCancel(true);

        Intent notIntent = new Intent(getActivity().getApplicationContext(), MainActivity.class);

        PendingIntent contIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, notIntent, 0);

        notificationBuilder.setContentIntent(contIntent);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);

        NotificationManager notificationManager = (NotificationManager) getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationManager.notify(0, notificationBuilder.build());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}