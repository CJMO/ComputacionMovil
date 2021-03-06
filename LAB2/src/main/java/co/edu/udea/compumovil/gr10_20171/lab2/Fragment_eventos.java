package co.edu.udea.compumovil.gr10_20171.lab2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import datos.EventDataBaseAdapter;
import datos.Evento;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_eventos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_eventos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_eventos extends Fragment{

    TextView nombre_evento;
    TextView descripcion_evento;
    TextView puntuacion_evento;

    TextView encargado_evento;
    TextView ubicacion_evento;

    ImageView foto_evento;

    private List<Evento> eventos;
    private RecyclerView rv;

    private String evento_individual = "NO";
    private int identificador_evento = 0;

    private static String coordenadas;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context contexto = getActivity().getApplicationContext();

        System.out.println("MODO "+ this.evento_individual);

        if(this.evento_individual.equals("NO")){
            View v = inflater.inflate(R.layout.eventos_recyvlerview, container, false);

            rv=(RecyclerView)v.findViewById(R.id.rv);

            LinearLayoutManager llm = new LinearLayoutManager( getActivity().getApplicationContext() );
            rv.setLayoutManager(llm);
            rv.setHasFixedSize(true);

            initializeData();
            initializeAdapter();

            return v;
        }
        else{

            EventDataBaseAdapter gestor_eventos = new EventDataBaseAdapter(getActivity().getApplicationContext());
            gestor_eventos = gestor_eventos.open();

            //Evento evento = new Evento();

            String datos[] = gestor_eventos.datos_evento(identificador_evento);

            gestor_eventos.close();

            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.eventos_cardview, container, false);

            nombre_evento = (TextView)v.findViewById(R.id.evento_nombre);
            descripcion_evento = (TextView)v.findViewById(R.id.evento_descripcion);
            puntuacion_evento = (TextView)v.findViewById(R.id.evento_puntuacion_label);
            encargado_evento = (TextView)v.findViewById(R.id.evento_encargado);
            ubicacion_evento = (TextView)v.findViewById(R.id.evento_ubicacion);

            foto_evento = (ImageView)v.findViewById(R.id.evento_foto);

            nombre_evento.setText( datos[0] );
            descripcion_evento.setText(datos[6]);
            puntuacion_evento.setText( puntuacion_evento.getText()+ datos[2]);
            ubicacion_evento.setText(datos[5]);

            encargado_evento.setText(datos[1]);

            Resources res = contexto.getResources();
            int resID = res.getIdentifier(datos[3], "drawable", contexto.getPackageName());
            Drawable drawable = res.getDrawable(resID );
            foto_evento.setImageDrawable(drawable);

            this.coordenadas = datos[7];

            Button btn_mapa = (Button)v.findViewById(R.id.evento_mapa_boton);
            btn_mapa.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),MapsActivity.class);
                    intent.putExtra("coordenadas", Fragment_eventos.coordenadas);
                    startActivity(intent);
                }
            });

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

    private void initializeData(){
        eventos = new ArrayList<>();
        /*eventos.add(new Evento("Evento 1", "Info", "4", "evento_1" ));
        eventos.add(new Evento("Evento 2", "Info", "4", "evento_3" ));
        eventos.add(new Evento("Evento 3", "Info", "4", "evento_5" ));*/

        EventDataBaseAdapter gestor_eventos = new EventDataBaseAdapter(getActivity().getApplicationContext());
        gestor_eventos = gestor_eventos.open();

        // consultar lista de eventos en base de datos
        List<Evento> lista = gestor_eventos.lista_eventos();
        eventos = lista;

        //System.out.println("EVENTOS "+ lista.size());
        //System.out.println("EVENTOS "+ lista.get(0).getFoto() +"--"+eventos.get(0).getFoto() );

        gestor_eventos.close();

    }

    private void initializeAdapter(){
        AdaptadorRV adapter = new AdaptadorRV(eventos, getFragmentManager());
        rv.setAdapter(adapter);
    }


}
