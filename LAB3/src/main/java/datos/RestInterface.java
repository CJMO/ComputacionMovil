package datos;

/**
 * Created by CJMO on 2/04/2017.
 */

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface RestInterface {

    @GET("/weather")
    Object getWheatherReport(@Query("q") String place, @Query("appid") String appId);

    // usuarios

    @GET("/usuarios")
    Object datosUsuario(@Query("filter") String nombre_usuario );

    @POST("/usuarios")
    Response registroUsuario(@Body String nombre_usuario );

    @POST("/usuarios/update")
    Response actualizarUsuario(@Query("where") String nombre_usuario_actualziar ,@Body String datos_usuario );

    // eventos

    @GET("/eventos")
    Object listaEventos( );

    @GET("/eventos")
    Object datosEvento(@Query("filter") String id_evento );

}