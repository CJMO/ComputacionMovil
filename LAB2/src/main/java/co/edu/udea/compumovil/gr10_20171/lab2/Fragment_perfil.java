package co.edu.udea.compumovil.gr10_20171.lab2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import datos.LoginDataBaseAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_perfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_perfil extends Fragment {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        Context contexto = getActivity().getApplicationContext();
        // conectar a base de datos
        adaptador = new LoginDataBaseAdapter(contexto);
        adaptador = adaptador.open();

        // gestionar datos de sesión
        SharedPreferences sharedpreferences;
        String MyPREFERENCES = "Mis datos" ;
        String Name = "nombre_sesion";

        // inicializar gestor de sesión
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // verificar si hay sesión iniciada
        String usuario_sesion = sharedpreferences.getString(Name, null);
        if(usuario_sesion != null){
            //Toast.makeText(contexto, "Conectado " + usuario_sesion, Toast.LENGTH_LONG).show();

            // obtener datos de sesión del usuario actual
            String[] datos_usuario = adaptador.datos_usuario(usuario_sesion);

            String foto = datos_usuario[0];
            String edad = datos_usuario[1];
            String correo = datos_usuario[2];

            // asignar datos a la interfaz

            ImageView avatar = (ImageView)v.findViewById(R.id.avatar_usuario);

            Resources res = getResources();
            int resID = res.getIdentifier(foto , "drawable", contexto.getPackageName());
            Toast.makeText(contexto, "foto " + resID +foto, Toast.LENGTH_LONG).show();
            Drawable drawable = res.getDrawable(resID );
            avatar.setImageDrawable(drawable );

            TextView texto_nombre_usuario = (TextView)v.findViewById(R.id.perfil_nombre_usuario);
            texto_nombre_usuario.setText(usuario_sesion);

            TextView texto_edad_usuario = (TextView)v.findViewById(R.id.perfil_edad);
            texto_edad_usuario.setText(edad);

            TextView texto_correo_usuario = (TextView)v.findViewById(R.id.perfil_correo_usuario);
            texto_correo_usuario.setText(correo);

        }

        return v;
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
