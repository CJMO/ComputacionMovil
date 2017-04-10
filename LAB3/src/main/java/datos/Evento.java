package datos;

import android.graphics.drawable.Drawable;

/**
 * Created by CJMO on 19/03/2017.
 */

public class Evento {

    private int ID;
    private String nombre;
    private String encargado;
    private String puntuacion;
    private String foto;
    private String fecha;
    private String ubicacion;
    private String informacion;
    private String coordenadas;

    public Evento(){

    }

    public Evento(String nombre, String informacion, String puntuacion, String foto ) {
        this.informacion = informacion;
        this.puntuacion = puntuacion;
        this.nombre = nombre;
        this.foto = foto;
    }

    public Evento(String nombre, String informacion, String ubicacion, String fecha, String puntuacion, String encargado, String foto, String coordenadas ) {
        this.informacion = informacion;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.puntuacion = puntuacion;
        this.encargado = encargado;
        this.nombre = nombre;
        this.foto = foto;
        this.coordenadas = coordenadas;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }


}
