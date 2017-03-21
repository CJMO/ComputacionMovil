package co.edu.udea.compumovil.gr10_20171.lab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class PersonalInfo extends AppCompatActivity {

    String nombre, apellido, escolaridad, sexo;
    String fecha_nacimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacion_personal);

        final EditText editNombre = (EditText)findViewById(R.id.editTextNombre);
        final EditText editApellido = (EditText)findViewById(R.id.editTextApellido);
        final Spinner spinnerEscolaridad = (Spinner)findViewById(R.id.spinnerEscolaridad);
        final RadioButton radioButtonSexoH = (RadioButton)findViewById(R.id.radioButtonHombre);
        final RadioButton radioButtonSexoM = (RadioButton)findViewById(R.id.radioButtonMujer);

        final Intent myIntent = new Intent(PersonalInfo.this,ContactInfo.class);

        Intent intent = getIntent();
        if(intent.getStringExtra("nombre")!= null){
            nombre = intent.getStringExtra("nombre");
            editNombre.setText(nombre);
            //System.out.println("PARAM "+nombre);
        }
        if(intent.getStringExtra("apellido")!= null){
            apellido = intent.getStringExtra("apellido");
            editApellido.setText(apellido);
        }
        if(intent.getStringExtra("sexo")!= null){
            sexo = intent.getStringExtra("sexo");System.out.println("SX2 "+sexo);
            if(sexo.equals("Hombre"))radioButtonSexoH.setChecked(true);
            else if(sexo.equals("Mujer")) radioButtonSexoM.setChecked(true);
        }


        /* Cambiar título de actividad */
        this.setTitle(R.string.app_name);
        getSupportActionBar().setTitle(R.string.app_name);

        Button button= (Button) findViewById(R.id.boton_fecha);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //startActivity(new Intent(PersonalInfo.this,Calendario.class));
                myIntent.setClass(PersonalInfo.this,Calendario.class);

                // obtener radioButton seleccionado para género de la persona
                if(radioButtonSexoH.isChecked()){
                    sexo = radioButtonSexoH.getText().toString();
                }
                else if(radioButtonSexoM.isChecked()){
                    sexo = radioButtonSexoM.getText().toString();
                }else sexo = "N/A";

                // obtener datos del layout
                nombre = editNombre.getText().toString();
                apellido = editApellido.getText().toString();
                escolaridad = spinnerEscolaridad.getSelectedItem().toString();
                // enviar datos a otra actividad
                myIntent.putExtra("nombre", nombre);
                myIntent.putExtra("apellido", apellido);
                myIntent.putExtra("escolaridad", escolaridad);
                myIntent.putExtra("sexo", sexo);
                startActivity(myIntent);
            }
        });

        Button boton_sigte = (Button) findViewById(R.id.boton_sigte);
        boton_sigte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent myIntent = new Intent(PersonalInfo.this,ContactInfo.class);
                myIntent.setClass(PersonalInfo.this,ContactInfo.class);

                // obtener datos del layout
                nombre = editNombre.getText().toString();
                apellido = editApellido.getText().toString();
                escolaridad = spinnerEscolaridad.getSelectedItem().toString();

                // obtener datos de calendario
                Intent intent_cal = getIntent();
                fecha_nacimiento = intent_cal.getStringExtra("fecha_nacimiento");
                System.out.println("NACIMIENTO: "+fecha_nacimiento);

                // obtener radioButton seleccionado para género de la persona
                if(radioButtonSexoH.isChecked()){
                    sexo = radioButtonSexoH.getText().toString();
                }
                else if(radioButtonSexoM.isChecked()){
                    sexo = radioButtonSexoM.getText().toString();
                }else sexo = "N/A";

                // otra forma
                /*RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroupSexo);
                sexo = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();*/

                myIntent.putExtra("nombre", nombre);
                myIntent.putExtra("apellido", apellido);
                myIntent.putExtra("escolaridad", escolaridad);
                myIntent.putExtra("sexo", sexo);

                myIntent.putExtra("fecha_nacimiento", fecha_nacimiento);

                //System.out.println("PASANDO "+nombre);
                startActivity(myIntent);

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText editNombre = (EditText)findViewById(R.id.editTextNombre);
        String nombre = editNombre.getText().toString();
        outState.putString("nombre", nombre);
        outState.putString("apellido", apellido);

        System.out.println("GUARDADO "+nombre);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String nombre = savedInstanceState.getString("nombre");
        EditText editNombre = (EditText)findViewById(R.id.editTextNombre);
        editNombre.setText(nombre);
        String apellido = savedInstanceState.getString("apellido");
        System.out.println("LEÍDO "+nombre);
    }
}
