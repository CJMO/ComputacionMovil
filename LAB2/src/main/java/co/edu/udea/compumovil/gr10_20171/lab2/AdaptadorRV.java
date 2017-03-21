package co.edu.udea.compumovil.gr10_20171.lab2;

/**
 * Created by CJMO on 19/03/2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import datos.EventDataBaseAdapter;
import datos.Evento;

public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ViewHolderEvento> {

    public static class ViewHolderEvento extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView nombre_evento;
        TextView descripcion_evento;
        TextView puntuacion_evento;
        ImageView foto_evento;

        int parametro = 0;
        FragmentManager fragment_manager = null;

        ViewHolderEvento(View itemView, FragmentManager fg) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

            nombre_evento = (TextView)itemView.findViewById(R.id.evento_nombre);
            descripcion_evento = (TextView)itemView.findViewById(R.id.evento_descripcion);
            puntuacion_evento = (TextView)itemView.findViewById(R.id.evento_puntuacion);
            foto_evento = (ImageView)itemView.findViewById(R.id.evento_foto);

            itemView.setOnClickListener(this);

            this.fragment_manager = fg;
        }

        @Override
        public void onClick(View v) {
            Context contexto = v.getContext();
            //Toast.makeText(contexto, "ITEM "+this.parametro, Toast.LENGTH_LONG).show();

            Fragment fragmento = new Fragment_eventos();
            //replacing the fragment
            if (fragmento != null) {

               /* EventDataBaseAdapter gestor_eventos = new EventDataBaseAdapter(getActivity().getApplicationContext());
                gestor_eventos = gestor_eventos.open();

                // consultar lista de eventos en base de datos
                List<Evento> lista = gestor_eventos.lista_eventos();*/

                Bundle argumentos = new Bundle();
                argumentos.putString("individual", "SI");
                argumentos.putInt("identificador", this.parametro);
                fragmento.setArguments(argumentos);

                android.support.v4.app.FragmentTransaction ft = this.fragment_manager.beginTransaction();
                ft.replace(R.id.contenido_fragment, fragmento);
                // retornar a pantalla previa
                ft.addToBackStack(null);
                ft.commit();
            }

        }
    }

    List<Evento> eventos;

    FragmentManager fragment_manager;


    AdaptadorRV(List<Evento> eventos, FragmentManager fg){
        this.eventos = eventos;
        fragment_manager = fg;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolderEvento onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.eventos_item, viewGroup, false);
        ViewHolderEvento pvh = new ViewHolderEvento(v, this.fragment_manager);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolderEvento eventoViewHolder, int i) {

        // asignar datos de cada evento al cardview
        eventoViewHolder.nombre_evento.setText(eventos.get(i).getNombre());
        eventoViewHolder.descripcion_evento.setText(eventos.get(i).getInformacion());
        eventoViewHolder.puntuacion_evento.setText(eventos.get(i).getPuntuacion());
        //eventoViewHolder.foto_evento.setImageResource(eventos.get(i).getFoto());

        eventoViewHolder.parametro = eventos.get(i).getID();

        Context contexto =  eventoViewHolder.itemView.getContext();

        Resources res = contexto.getResources();
        int resID = res.getIdentifier(eventos.get(i).getFoto(), "drawable", contexto.getPackageName());
        Drawable drawable = res.getDrawable(resID );
        eventoViewHolder.foto_evento.setImageDrawable(drawable);
    }

    /*public void detalles(){
        Fragment fragmento = new Fragment_perfil();
        //replacing the fragment
        if (fragmento != null) {
            android.support.v4.app.FragmentTransaction ft = this.fragment_manager.beginTransaction();
            ft.replace(R.id.contenido_fragment, fragmento);
            ft.commit();
        }
    }*/

    @Override
    public int getItemCount() {
        return eventos.size();
    }
}
