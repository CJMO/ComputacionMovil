package datos;


import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

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

public class ServicioUsuario {

    String url = "http://api.openweathermap.org/data/2.5";
    String url_servicio = "http://contenedor-cristianjmunoz988129.codeanyapp.com:3000/api";


    public ServicioUsuario(){

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    public String consultar_datos_acceso(String usuario) throws JSONException {

        //making object of RestAdapter
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url_servicio).build();

        //Creating Rest Services
        RestInterface restInterface = adapter.create(RestInterface.class);

        // realizar petición de comprobación de datos de usuario
        Object json_respuesta = restInterface.datosUsuario("{\"where\":{\"usuario\":\""+usuario+"\" }}");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(json_respuesta);

        String clave = "NO EXISTE";

        JSONArray jsonArray = new JSONArray(jsonInString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Iterator<String> keys = json.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                if(key.equals("clave")) clave = json.get(key).toString();
                System.out.println("Key :" + key + "  Value :" + json.get(key));
            }

        }
        //System.out.println(jsonArray);

        return clave;

    }

    public String[] consultar_datos_usuario(String usuario) throws JSONException {

        //making object of RestAdapter
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url_servicio).build();

        //Creating Rest Services
        RestInterface restInterface = adapter.create(RestInterface.class);

        // realizar petición de comprobación de datos de usuario
        Object json_respuesta = restInterface.datosUsuario("{\"where\":{\"usuario\":\""+usuario+"\" }}");

        Gson gson = new Gson();
        String jsonInString = gson.toJson(json_respuesta);

        String nombre_usuario = "";
        String correo = "";
        String edad = "";
        String foto = "";

        JSONArray jsonArray = new JSONArray(jsonInString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Iterator<String> keys = json.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                if(key.equals("usuario")) nombre_usuario = json.get(key).toString();
                else if(key.equals("correo")) correo = json.get(key).toString();
                else if(key.equals("edad")) edad = json.get(key).toString();
                else if(key.equals("foto")) foto = json.get(key).toString();
                //System.out.println("Key :" + key + "  Value :" + json.get(key));
            }

        }
        System.out.println(jsonArray);

        String[] datos = new String[]{nombre_usuario, correo, foto, edad};
        return datos;

    }

    public void registrar_usuario(String usuario,String clave, String correo, String foto, String edad){

        //making object of RestAdapter
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url_servicio).build();

        //Creating Rest Services
        RestInterface restInterface = adapter.create(RestInterface.class);

        /*String datos = "{\n" +
                "    \"usuario\": \"invitado\",\n" +
                "    \"clave\": \"invitado\",\n" +
                "    \"correo\": \"invitado@udea.edu.co\",\n" +
                "    \"foto\": \"img4\",\n" +
                "    \"edad\": \"18\"\n" +
                "  }";*/

        String datos = "{\n" +
                "    \"usuario\": \""+usuario+"\",\n" +
                "    \"clave\": \""+clave+"\",\n" +
                "    \"correo\": \""+correo+"\",\n" +
                "    \"foto\": \""+foto+"\",\n" +
                "    \"edad\": \""+edad+"\"\n" +
                "  }";
        restInterface.registroUsuario(datos);
    }

    public void actualizarDatos(String usuario_anterior, String indice, String dato){

        //making object of RestAdapter
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url_servicio)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        //Creating Rest Services
        RestInterface restInterface = adapter.create(RestInterface.class);

        String where = "{\"usuario\":\""+usuario_anterior+"\"}";
        //String where = "{\"usuario\":\"cjmo\" }";

        String datos = "{\""+indice+"\": \""+dato+"\" }";
        /*"{\n" +
            "    \""+indice+"\": \""+dato+"\"\n" +
            "  }";*/
        //String datos = "{\"correo\": \"cristian.j@hotmail.com\" }";
        restInterface.actualizarUsuario(where, datos);

    }
}
