package co.edu.udea.compumovil.gr10_20171.lab4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import datos.EventDataBaseAdapter;
import datos.Evento;
import datos.ServicioEventos;

/**
 * Created by CJMO on 4/04/2017.
 */

public class ReceiverActualizacion extends BroadcastReceiver {

    public ReceiverActualizacion(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        List<Evento> eventos = new ArrayList<>();
        ServicioEventos servicio_eventos = new ServicioEventos();

        EventDataBaseAdapter gestor_eventos = new EventDataBaseAdapter(context);
        gestor_eventos = gestor_eventos.open();

        try {
            eventos = servicio_eventos.lista_eventos();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

            Toast.makeText(context, "Datos de eventos actualizados", Toast.LENGTH_LONG).show();
        }

        /*Toast.makeText(context, "time is up!!!!.",
                Toast.LENGTH_LONG).show();
        // Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);*/
    }
}
