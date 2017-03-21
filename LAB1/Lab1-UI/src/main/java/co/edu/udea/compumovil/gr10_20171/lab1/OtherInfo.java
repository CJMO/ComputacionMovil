package co.edu.udea.compumovil.gr10_20171.lab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;

public class OtherInfo extends AppCompatActivity {

    String nombre, apellido, escolaridad, sexo;
    String telefono, correo, pais, ciudad, direccion;
    String fecha_nacimiento;
    String pasatiempos = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);

        /* Cambiar título de actividad */
        this.setTitle(R.string.app_name3);
        getSupportActionBar().setTitle(R.string.app_name3);

        // obtener datos de actividad actual
        final CheckBox opcion_leer = (CheckBox)findViewById(R.id.checkBoxLeer);
        final CheckBox opcion_tv = (CheckBox)findViewById(R.id.checkBoxVerTv);
        final CheckBox opcion_bailar= (CheckBox)findViewById(R.id.checkBoxBailar);
        final CheckBox opcion_cantar = (CheckBox)findViewById(R.id.checkBoxCantar);
        final CheckBox opcion_nadar = (CheckBox)findViewById(R.id.checkBoxNadar);

        final RatingBar puntaje_leer = (RatingBar)findViewById(R.id.ratingBarLeer);
        final RatingBar puntaje_tv = (RatingBar)findViewById(R.id.ratingBarVerTV);
        final RatingBar puntaje_bailar= (RatingBar)findViewById(R.id.ratingBarBailar);
        final RatingBar puntaje_cantar = (RatingBar)findViewById(R.id.ratingBarCantar);
        final RatingBar puntaje_nadar = (RatingBar)findViewById(R.id.ratingBarNadar);

        // obtener datos de actividades anteriores
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

        Button boton_sigte = (Button) findViewById(R.id.botonMostrar);
        boton_sigte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(OtherInfo.this,Resumen.class);
                myIntent.putExtra("nombre", nombre);
                myIntent.putExtra("apellido", apellido);
                myIntent.putExtra("escolaridad", escolaridad);
                myIntent.putExtra("sexo", sexo);

                myIntent.putExtra("telefono", telefono);
                myIntent.putExtra("correo", correo);
                myIntent.putExtra("pais", pais);
                myIntent.putExtra("ciudad", ciudad);
                myIntent.putExtra("direccion", direccion);
                myIntent.putExtra("fecha_nacimiento", fecha_nacimiento);

                // Generar lista de pasatiempos
                if(opcion_leer.isChecked()){
                    pasatiempos += "Leer: Calificación "+puntaje_leer.getRating()+ " de 5"+System.lineSeparator();
                }
                if(opcion_tv.isChecked()){
                    pasatiempos += "Ver TV: Calificación "+puntaje_tv.getRating()+" de 5"+System.lineSeparator();
                }
                if(opcion_bailar.isChecked()){
                    pasatiempos += "Bailar: Calificación "+puntaje_bailar.getRating()+" de 5"+System.lineSeparator();
                }
                if(opcion_cantar.isChecked()){
                    pasatiempos += "Cantar: Calificación "+puntaje_cantar.getRating()+" de 5"+System.lineSeparator();
                }
                if(opcion_nadar.isChecked()){
                    pasatiempos += "Nadar: Calificación "+puntaje_nadar.getRating()+" de 5"+System.lineSeparator();
                }

                myIntent.putExtra("pasatiempos", pasatiempos);

                startActivity(myIntent);
            }
        });
    }
}
