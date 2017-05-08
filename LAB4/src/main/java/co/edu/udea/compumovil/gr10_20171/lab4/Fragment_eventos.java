package co.edu.udea.compumovil.gr10_20171.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import datos.EventDataBaseAdapter;
import datos.Evento;
import datos.ServicioEventos;

import com.firebase.ui.database.FirebaseRecyclerAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_eventos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_eventos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_eventos extends Fragment{

    View v;
    RecyclerView rv2;
    private RecyclerView mRecyclerView;

    // Declare the FirebaseAuth and AuthStateListener objects.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG;

    TextView nombre_evento;
    TextView descripcion_evento;
    TextView puntuacion_evento;

    TextView encargado_evento;
    TextView ubicacion_evento;
    TextView fecha_evento;

    ImageView foto_evento;

    private List<Evento> eventos;
    private RecyclerView rv;

    private String evento_individual = "NO";
    private int identificador_evento = 0;

    private static String coordenadas;

    ServicioEventos servicio_eventos = new ServicioEventos();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_eventos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_eventos.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_eventos newInstance(String param1, String param2) {
        Fragment_eventos fragment = new Fragment_eventos();
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
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);

            // obtener parámetros
            if(getArguments().get("individual").equals("SI")){
                this.evento_individual = "SI";
            }

            if(getArguments().getInt("identificador") != 0){
                this.identificador_evento = getArguments().getInt("identificador");
            }
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
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.eventos_recyvlerview, container, false);

        final Context contexto = getActivity().getApplicationContext();

        System.out.println("MODO "+ this.evento_individual);

        if(this.evento_individual.equals("NO")){
            v = inflater.inflate(R.layout.eventos_recyvlerview, container, false);

            rv=(RecyclerView)v.findViewById(R.id.rv);

            LinearLayoutManager llm = new LinearLayoutManager( getActivity().getApplicationContext() );
            rv.setLayoutManager(llm);
            rv.setHasFixedSize(true);

            String modo_lectura_eventos = "";

            if (getArguments() != null){
                // obtener parámetros
                if(getArguments().get("modo-eventos").equals("local")){
                    modo_lectura_eventos = "local";
                }
                else if(getArguments().get("modo-eventos").equals("servicio")){
                    modo_lectura_eventos = "servicio";
                }
            }

            initializeAdapter();
            return v;
        }
        else{
            // evento individual

            //Initializing our Recyclerview
            mRecyclerView = (RecyclerView) v.findViewById(R.id.rv);
            //using staggered grid pattern in recyclerview
            StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);

            if (mRecyclerView != null) {
                //to enable optimization of recyclerview
                mRecyclerView.setHasFixedSize(true);
            }

            // Inflate the layout for this fragment
            v = inflater.inflate(R.layout.eventos_cardview, container, false);

            nombre_evento = (TextView)v.findViewById(R.id.evento_nombre);
            descripcion_evento = (TextView)v.findViewById(R.id.evento_descripcion);
            puntuacion_evento = (TextView)v.findViewById(R.id.evento_puntuacion_label);
            encargado_evento = (TextView)v.findViewById(R.id.evento_encargado);
            ubicacion_evento = (TextView)v.findViewById(R.id.evento_ubicacion);
            fecha_evento = (TextView)v.findViewById(R.id.evento_fecha);

            foto_evento = (ImageView)v.findViewById(R.id.evento_foto);

            System.out.println("ID "+this.identificador_evento);

            try {
                Query datos_eventos = FirebaseDatabase.getInstance().getReference().child("eventos").orderByChild("id").equalTo(this.identificador_evento);
                // Attach a listener to read the data at our posts reference
                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //System.out.println("Datos eventos "+dataSnapshot.getValue().toString());

                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(dataSnapshot.getValue()).replace("null,","");
                        jsonInString = jsonInString.replace("[","");
                        jsonInString = jsonInString.replace("]","");
                        System.out.println("Datos eventos "+jsonInString);

                        try {
                            JSONObject json = new JSONObject(jsonInString);
                            Iterator<String> keys = json.keys();

                            while (keys.hasNext()) {
                                String key = keys.next();
                                if( !json.isNull(key) ){

                                    JSONObject json_ev = json.getJSONObject(key);
                                    Iterator<String> keys_ev = json_ev.keys();

                                    Evento evento = new Evento();
                                    while (keys_ev.hasNext()) {
                                        key = keys_ev.next();
                                        if (key.equals("id"))
                                            evento.setID(Double.valueOf(json_ev.get(key).toString()).intValue());
                                        else if (key.equals("nombre"))
                                            evento.setNombre(json_ev.get(key).toString());
                                        else if (key.equals("puntuacion"))
                                            evento.setPuntuacion(json_ev.get(key).toString());
                                        else if (key.equals("fecha"))
                                            evento.setFecha(json_ev.get(key).toString());
                                        else if (key.equals("coordenadas"))
                                            evento.setCoordenadas(json_ev.get(key).toString());
                                        else if (key.equals("informacion"))
                                            evento.setInformacion(json_ev.get(key).toString());
                                        else if (key.equals("foto"))
                                            evento.setFoto(json_ev.get(key).toString());
                                        else if (key.equals("encargado"))
                                            evento.setEncargado(json_ev.get(key).toString());
                                        else if (key.equals("ubicacion"))
                                            evento.setUbicacion(json_ev.get(key).toString());
                                    }

                                    nombre_evento.setText(evento.getNombre());
                                    descripcion_evento.setText(evento.getInformacion());
                                    puntuacion_evento.setText(puntuacion_evento.getText() + evento.getPuntuacion());
                                    ubicacion_evento.setText(evento.getUbicacion());
                                    encargado_evento.setText(evento.getEncargado());
                                    fecha_evento.setText(evento.getFecha());
                                    System.out.println("Evento " + evento.getNombre());

                                    // descargar imagen evento
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReferenceFromUrl("gs://lab-4-f76ec.appspot.com/eventos/" + evento.getFoto()); //vento_caridad.jpg
                                    // Create a reference with an initial file path and name
                                    //StorageReference pathReference = storageRef.child("images/evento_patinaje.jpg");
                                    ////StorageReference storageRef = storage.getReference().child("images/evento_patinaje.jpg");

                                    final long ONE_MEGABYTE = 1024 * 1024;
                                    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            Drawable imagen = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                            foto_evento.setImageDrawable(imagen);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });

                                    coordenadas = evento.getCoordenadas();

                                    Button btn_mapa = (Button) v.findViewById(R.id.evento_mapa_boton);
                                    btn_mapa.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), MapsActivity.class);
                                            intent.putExtra("coordenadas", Fragment_eventos.coordenadas);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                };

                datos_eventos.addValueEventListener(listener);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return v;
        }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context contexto = getActivity().getApplicationContext();
        Toast.makeText(contexto, "ITEM " + position, Toast.LENGTH_LONG).show();
        System.out.println("ITEM ");
    }*/

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

    private void initializeData(String modo) throws JSONException {
        eventos = new ArrayList<>();

        EventDataBaseAdapter gestor_eventos = new EventDataBaseAdapter(getActivity().getApplicationContext());
        gestor_eventos = gestor_eventos.open();

        if(modo.equals("local")){

            // consultar lista de eventos en base de datos
            //List<Evento> lista = gestor_eventos.lista_eventos();
            eventos = gestor_eventos.lista_eventos();

            gestor_eventos.close();
        }
        else if(modo.equals("servicio")){
            // obtener eventos por medio de servicio web
            //ArrayList<Evento> lista = servicio_eventos.lista_eventos();
            eventos = servicio_eventos.lista_eventos();

            for(Evento e: eventos){
                String[] nuevo = gestor_eventos.datos_evento(e.getID());

                if(nuevo == null){
                    // agregar evento, ya que no existe en bd local
                    gestor_eventos.agregar(e.getNombre(), e.getEncargado(), e.getPuntuacion(), e.getFoto(), e.getFecha(), e.getUbicacion(), e.getInformacion(), e.getCoordenadas());
                }
                else{
                    // actualizar evento, ya que no existe en bd local
                    gestor_eventos.actualizar(e.getID(), e.getNombre(), e.getEncargado(), e.getPuntuacion(), e.getFoto(), e.getFecha(), e.getUbicacion(), e.getInformacion(), e.getCoordenadas());
                }

                Toast.makeText(getContext(), "Datos de eventos actualizados", Toast.LENGTH_LONG).show();
            }

        }

        //eventos = lista;

    }

    private void initializeAdapter(){

        eventos = new ArrayList<>();

        //AdaptadorRV adapter = new AdaptadorRV(eventos, getFragmentManager());
        //rv.setAdapter(adapter);

        Query datos_eventos = FirebaseDatabase.getInstance().getReference().child("eventos").orderByChild("id");
        // Attach a listener to read the data at our posts reference
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Datos eventos "+dataSnapshot.getValue().toString());

                Gson gson = new Gson();
                String jsonInString = gson.toJson(dataSnapshot.getValue());

                //JSONArray jsonArray = null;
                try {
                    JSONObject json = new JSONObject(jsonInString);
                    Iterator<String> keys = json.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        System.out.println("key "+key);

                        JSONObject json_ev = json.getJSONObject(key);
                        /*keys = null;
                        key = null;
                        keys = json.keys();*/
                        Iterator<String> keys_ev = json_ev.keys();

                        Evento evento = new Evento();
                        while (keys_ev.hasNext()){
                            key = keys_ev.next();
                            System.out.println("key "+key);
                            if(key.equals("id")) evento.setID( Double.valueOf(json_ev.get(key).toString()).intValue() );
                            else if(key.equals("nombre")) evento.setNombre(json_ev.get(key).toString());
                            else if(key.equals("puntuacion")) evento.setPuntuacion(json_ev.get(key).toString());
                            else if(key.equals("fecha")) evento.setFecha(json_ev.get(key).toString());
                            else if(key.equals("coordenadas")) evento.setCoordenadas(json_ev.get(key).toString());
                            else if(key.equals("informacion")) evento.setInformacion(json_ev.get(key).toString());
                            else if(key.equals("foto")) evento.setFoto(json_ev.get(key).toString());
                            else if(key.equals("encargado")) evento.setEncargado(json_ev.get(key).toString());
                            else if(key.equals("ubicacion")) evento.setUbicacion(json_ev.get(key).toString());
                        }
                        // añadir evento a la lista
                        eventos.add(evento);

                        //showNotification("Nuevo evento creado!",evento.getNombre());
                    }

                    AdaptadorRV adapter = new AdaptadorRV(eventos, getFragmentManager());
                    rv.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };

        datos_eventos.addValueEventListener(listener);
    }

    //ViewHolder for our Firebase UI
    public static class EventoViewHolder extends RecyclerView.ViewHolder{

        TextView nombre_evento;
        TextView descripcion_evento;
        TextView puntuacion_evento;
        TextView fecha_evento;
        TextView coordenadas_evento;

        TextView encargado_evento;
        TextView ubicacion_evento;

        ImageView foto_evento;

        public EventoViewHolder(View v) {
            super(v);
            nombre_evento = (TextView)v.findViewById(R.id.evento_nombre);
            descripcion_evento = (TextView)v.findViewById(R.id.evento_descripcion);
            puntuacion_evento = (TextView)v.findViewById(R.id.evento_puntuacion_label);
            encargado_evento = (TextView)v.findViewById(R.id.evento_encargado);
            ubicacion_evento = (TextView)v.findViewById(R.id.evento_ubicacion);

            foto_evento = (ImageView)v.findViewById(R.id.evento_foto);
        }
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
}