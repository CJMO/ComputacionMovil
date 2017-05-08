package co.edu.udea.compumovil.gr10_20171.lab4;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import datos.LoginDataBaseAdapter;
import datos.ServicioUsuario;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_perfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_perfil extends Fragment {

    // Declare the FirebaseAuth and AuthStateListener objects.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG;

    private String nombre_usuario = "";
    private String correo = "";
    private String edad = "";
    private String foto = "";

    View v;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // adaptador de base de datos
    LoginDataBaseAdapter adaptador;

    // gestionar datos de sesión
    SharedPreferences sharedpreferences;
    String MyPREFERENCES = "Mis datos" ;
    String Name = "nombre_sesion";

    public Fragment_perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_perfil newInstance(String param1, String param2) {
        Fragment_perfil fragment = new Fragment_perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Inicializar aplicación y obtener instancia
        FirebaseApp.initializeApp(getContext());

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_perfil, container, false);

        Context contexto = getActivity().getApplicationContext();
        // conectar a base de datos
        adaptador = new LoginDataBaseAdapter(contexto);
        adaptador = adaptador.open();

        // inicializar gestor de sesión
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // verificar si hay sesión iniciada
        String usuario_sesion = sharedpreferences.getString(Name, null);
        if(usuario_sesion != null){

            obtener_datos(usuario_sesion);

            //Toast.makeText(contexto, "Conectado " + usuario_sesion, Toast.LENGTH_LONG).show();

            /*String[] datos_usuario = new String[4];

            // obtener datos de sesión del usuario actual
            ServicioUsuario servicio = new ServicioUsuario();
            try {
                datos_usuario = servicio.consultar_datos_usuario(usuario_sesion);
                //System.out.println("SERVICIO: "+datos_usuario[0] +datos_usuario[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println("preferencias "+usuario_sesion);

            String nombre_usuario = datos_usuario[0];
            String correo = datos_usuario[1];
            String foto = datos_usuario[2];
            String edad = datos_usuario[3];

            // asignar datos a la interfaz

            ImageView avatar = (ImageView)v.findViewById(R.id.avatar_usuario);

            Resources res = getResources();
            int resID = res.getIdentifier(foto , "drawable", contexto.getPackageName());
            //Toast.makeText(contexto, "foto " + resID +foto, Toast.LENGTH_LONG).show();
            Drawable drawable = res.getDrawable(resID );
            avatar.setImageDrawable(drawable );

            TextView texto_nombre_usuario = (TextView)v.findViewById(R.id.perfil_nombre_usuario);
            texto_nombre_usuario.setText(nombre_usuario);

            TextView texto_edad_usuario = (TextView)v.findViewById(R.id.perfil_edad);
            texto_edad_usuario.setText(edad);

            TextView texto_correo_usuario = (TextView)v.findViewById(R.id.perfil_correo_usuario);
            texto_correo_usuario.setText(correo);*/

        }

        return v;
    }

    public void obtener_datos(String usuario){

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
                    JSONObject json = new JSONObject(jsonInString);
                    Iterator<String> keys = json.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        System.out.println("key "+key);

                        json = json.getJSONObject(key);
                        keys = null;
                        key = null;
                        keys = json.keys();
                        while (keys.hasNext()){
                            key = keys.next();
                            System.out.println("key "+key);
                            if(key.equals("usuario")) nombre_usuario = json.get(key).toString();
                            else if(key.equals("correo")) correo = json.get(key).toString();
                            else if(key.equals("edad")) edad = json.get(key).toString();
                            else if(key.equals("foto")) foto = json.get(key).toString();
                        }
                    }

                    // asignar datos a la interfaz
                    Context contexto = getActivity().getApplicationContext();
                    final ImageView avatar = (ImageView)v.findViewById(R.id.avatar_usuario);

                    // descargar imagen evento
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://lab-4-f76ec.appspot.com/usuarios/"+foto);
                    //System.out.println("gs://lab-4-f76ec.appspot.com/"+foto+".jpg");

                    final long ONE_MEGABYTE = 1024 * 1024;
                    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Data for "images/island.jpg" is returns, use this as needed
                            Drawable imagen = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            avatar.setImageDrawable(imagen);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                    TextView texto_nombre_usuario = (TextView)v.findViewById(R.id.perfil_nombre_usuario);
                    texto_nombre_usuario.setText(nombre_usuario);

                    TextView texto_edad_usuario = (TextView)v.findViewById(R.id.perfil_edad);
                    texto_edad_usuario.setText(edad);

                    TextView texto_correo_usuario = (TextView)v.findViewById(R.id.perfil_correo_usuario);
                    texto_correo_usuario.setText(correo);

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

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
