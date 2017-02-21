package co.edu.udea.compumovil.gr10_20171.lab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Resumen extends AppCompatActivity {

    String nombre, apellido, escolaridad, sexo;
    String telefono, correo, pais, ciudad, direccion;
    String pasatiempos;
    String fecha_nacimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        // obtener datos de actividad anterior
        Intent intent = getIntent();
        nombre = intent.getStringExtra("nombre");
        apellido = intent.getStringExtra("apellido");
        escolaridad = intent.getStringExtra("escolaridad");
        sexo = intent.getStringExtra("sexo");
        fecha_nacimiento = intent.getStringExtra("fecha_nacimiento");

        telefono = intent.getStringExtra("telefono");
        correo = intent.getStringExtra("correo");
        pais = intent.getStringExtra("pais");
        ciudad = intent.getStringExtra("ciudad");
        direccion = intent.getStringExtra("direccion");

        pasatiempos = intent.getStringExtra("pasatiempos");

        TextView datos_usuario = (TextView)findViewById(R.id.textViewResumen);

        String resumen = String.format("Nombres: %s Apellidos: %sFecha Nacimiento: %sCorreo: %s Teléfono: %sPaís: %s Ciudad: %sEscolaridad: %s Género: %sPasatiempos %s",
                nombre, apellido+System.lineSeparator(), fecha_nacimiento+System.lineSeparator(), correo, telefono+System.lineSeparator(), pais, ciudad+System.lineSeparator(), escolaridad, sexo+System.lineSeparator(), pasatiempos);
        datos_usuario.setText(resumen);

        //EditText editNombre = (EditText)findViewById(R.id.editTextNombre);
        //String nombre = PersonalInfo.nombre;
        //String nombre = editNombre.getText().toString();
        /*String apellido = findViewById(R.id.editTextApellido).toString();
        String correo = findViewById(R.id.editTextCorreo).toString();
        String telefono = findViewById(R.id.editTextTelefono).toString();
        String pais = findViewById(R.id.autoCompleteTextViewPais).toString();
        String ciudad = findViewById(R.id.autoCompleteTextViewCiudad).toString();*/

        /*String resumen = String.format("Nombres: %s Apellidos: %s Correo: %s Teléfono: %s País: %s Ciudad: %s", nombre, apellido, correo, telefono, pais, ciudad);
        datos_usuario.setText( resumen );*/
        //datos_usuario.setText( nombre );
    }
}
