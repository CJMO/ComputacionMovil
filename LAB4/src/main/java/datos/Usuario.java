package datos;

/**
 * Created by CJMO on 6/05/2017.
 */

public class Usuario {

    private String usuario;
    private String correo;
    private String clave;
    private String edad;
    private String foto;

    public Usuario(){

    }
    public Usuario(String usuario, String correo, String clave, String edad){
        this.usuario = usuario;
        this.correo = correo;
        this.clave = clave;
        this.edad = edad;
    }

    public Usuario(String usuario, String correo, String clave, String edad, String foto){
        this.usuario = usuario;
        this.correo = correo;
        this.clave = clave;
        this.edad = edad;
        this.foto = foto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
