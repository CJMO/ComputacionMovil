package co.edu.udea.compumovil.gr10_20171.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static co.edu.udea.compumovil.gr10_20171.lab1.R.id.datePicker;

public class Calendario extends AppCompatActivity {

    String nombre, apellido, escolaridad, sexo;
    String fecha_nacimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        // obtener datos de actividad anterior
        Intent intent = getIntent();
        nombre = intent.getStringExtra("nombre");
        apellido = intent.getStringExtra("apellido");
        escolaridad = intent.getStringExtra("escolaridad");
        sexo = intent.getStringExtra("sexo");

        System.out.println("PARAM "+nombre);

        Button button= (Button) findViewById(R.id.boton_volver);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year =  datePicker.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = sdf.format(calendar.getTime());

                System.out.println("FECHA "+fecha);

                Intent myIntent = new Intent(Calendario.this,PersonalInfo.class);

                myIntent.putExtra("fecha_nacimiento", fecha);

                myIntent.putExtra("nombre", nombre);
                myIntent.putExtra("apellido", apellido);
                myIntent.putExtra("escolaridad", escolaridad);
                myIntent.putExtra("sexo", sexo);

                startActivity(myIntent);
                //finish();
            }
        });

    }

}
