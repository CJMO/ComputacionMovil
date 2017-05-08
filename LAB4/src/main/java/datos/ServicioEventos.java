package datos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

import android.content.Context;
import android.os.StrictMode;

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

public class ServicioEventos {

    String url_servicio = "http://contenedor-cristianjmunoz988129.codeanyapp.com:3000/api";

    //making object of RestAdapter
    RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url_servicio).build();

    //Creating Rest Services
    RestInterface restInterface = adapter.create(RestInterface.class);

    public ServicioEventos(){
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public ArrayList<Evento> lista_eventos() throws JSONException{

        ArrayList<Evento> lista = new ArrayList<Evento>();

        // realizar petición de comprobación de datos de eventos
        Object json_respuesta = restInterface.listaEventos();

        Gson gson = new Gson();
        String jsonInString = gson.toJson(json_respuesta);

        JSONArray jsonArray = new JSONArray(jsonInString);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Iterator<String> keys = json.keys();

            Evento evento = new Evento();

            while (keys.hasNext()) {
                String key = keys.next();
                //if(key.equals("id")) evento.setID( Integer.parseInt(json.get(key).toString()) );
                if(key.equals("id")) evento.setID( Double.valueOf(json.get(key).toString()).intValue() );
                else if(key.equals("nombre")) evento.setNombre(json.get(key).toString());
                else if(key.equals("puntuacion")) evento.setPuntuacion(json.get(key).toString());
                else if(key.equals("fecha")) evento.setFecha(json.get(key).toString());
                else if(key.equals("coordenadas")) evento.setCoordenadas(json.get(key).toString());
                else if(key.equals("informacion")) evento.setInformacion(json.get(key).toString());
                else if(key.equals("foto")) evento.setFoto(json.get(key).toString());
                else if(key.equals("encargado")) evento.setEncargado(json.get(key).toString());
                else if(key.equals("ubicacion")) evento.setUbicacion(json.get(key).toString());

                //System.out.println("Key :" + key + "  Value :" + json.get(key));

            }
            // añadir evento a la lista
            lista.add(evento);

        }

        return lista;

    }

    public Evento consultar_datos_evento(Integer id, String modo, Context contexto) throws JSONException{

        Evento evento = null;

        if( modo.equals("servicio") ){
            // realizar petición de comprobación de datos de evento
            Object json_respuesta = restInterface.datosEvento("{\"where\":{\"id\":"+id+" }}");

            Gson gson = new Gson();
            String jsonInString = gson.toJson(json_respuesta);

            JSONArray jsonArray = new JSONArray(jsonInString);

            evento = new Evento();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Iterator<String> keys = json.keys();

                while (keys.hasNext()) {
                    String key = keys.next();

                    if(key.equals("id")) evento.setID( Double.valueOf(json.get(key).toString()).intValue() );
                    else if(key.equals("nombre")) evento.setNombre(json.get(key).toString());
                    else if(key.equals("puntuacion")) evento.setPuntuacion(json.get(key).toString());
                    else if(key.equals("fecha")) evento.setFecha(json.get(key).toString());
                    else if(key.equals("coordenadas")) evento.setCoordenadas(json.get(key).toString());
                    else if(key.equals("informacion")) evento.setInformacion(json.get(key).toString());
                    else if(key.equals("foto")) evento.setFoto(json.get(key).toString());
                    else if(key.equals("encargado")) evento.setEncargado(json.get(key).toString());
                    else if(key.equals("ubicacion")) evento.setUbicacion(json.get(key).toString());

                }

            }

            //return evento;
        }
        else if(modo.equals("local")){

            EventDataBaseAdapter gestor_eventos = new EventDataBaseAdapter(contexto);
            gestor_eventos = gestor_eventos.open();

            // obtener datos de base de datos local
            String datos[] = gestor_eventos.datos_evento(id);

            evento = new Evento();
            evento.setID(id );
            evento.setNombre(datos[0]);
            evento.setEncargado(datos[1]);
            evento.setPuntuacion(datos[2]);
            evento.setFoto(datos[3]);
            evento.setFecha(datos[4]);
            evento.setUbicacion(datos[5]);
            evento.setInformacion(datos[6]);
            evento.setCoordenadas(datos[7]);

            gestor_eventos.close();
        }

        return evento;
    }

}
