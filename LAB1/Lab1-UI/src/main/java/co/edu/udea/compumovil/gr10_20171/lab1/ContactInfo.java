package co.edu.udea.compumovil.gr10_20171.lab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class ContactInfo extends AppCompatActivity {

    private static final String[] PAISES = new String[] {
            "Argentina", "Bolivia", "Brasil", "Chile", "Colombia", "Costa Rica", "Cuba",
            "Ecuador", "El Salvador", "Guayana Francesa", "Granada", "Guatemala", "Guayana",
            "Haití", "Honduras", "Jamaica", "México", "Nicaragua", "Paraguay", "Panamá", "Perú",
            "Puerto Rico", "República Dominicana", "Surinam", "Uruguay", "Venezuela"
    };

    private static final String[] CIUDADES = new String[] {
            "Leticia", "Medellín", "Arauca", "Barranquilla", "Cartagena", "Tunja", "Manizales",
            "Florencia", "Yopal", "Popayán", "Valledupar", "Quibdó", "Montería", "Bogotá", "Puerto Inírida",
            "San José del Guaviare", "Neiva", "Riohacha", "Santa Marta", "Villavicencio", "Pasto", "Cúcuta",
            "Mocoa", "Armenia", "Pereira", "San Andrés", "Bucaramanga", "Sincelejo", "Ibagué", "Cali" , "Mitú" , "Puerto Carreño"
    };

    String nombre, apellido, escolaridad, sexo;
    String telefono, correo, pais, ciudad, direccion;
    String fecha_nacimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        /* Cambiar título de actividad */
        this.setTitle(R.string.app_name2);
        getSupportActionBar().setTitle(R.string.app_name2);

        /* Opciones autocompletado paises */
        AutoCompleteTextView texto_paises = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewPais);

        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1,PAISES);

        texto_paises.setAdapter(adaptador);

        /* Opciones autocompletado ciudades */
        AutoCompleteTextView texto_ciudades = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewCiudad);

        ArrayAdapter adaptador_ciudades = new ArrayAdapter(this,android.R.layout.simple_list_item_1,CIUDADES);

        texto_ciudades.setAdapter(adaptador_ciudades);

        // obtener datos de actividad anterior
        Intent intent = getIntent();
        nombre = intent.getStringExtra("nombre");
        apellido = intent.getStringExtra("apellido");
        escolaridad = intent.getStringExtra("escolaridad");
        sexo = intent.getStringExtra("sexo");
        fecha_nacimiento = intent.getStringExtra("fecha_nacimiento");

        // obtener datos de la actividad actual
        final EditText editTelefono = (EditText)findViewById(R.id.editTextTelefono);
        final EditText editCorreo = (EditText)findViewById(R.id.editTextCorreo);
        final EditText editPais = (EditText)findViewById(R.id.autoCompleteTextViewPais);
        final EditText editCiudad = (EditText)findViewById(R.id.autoCompleteTextViewCiudad);
        final EditText editDireccion = (EditText)findViewById(R.id.editTextDireccion);


        System.out.println("DATOS "+nombre);

        Button boton_sigte = (Button) findViewById(R.id.botonSiguienteInformacion);
        boton_sigte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(ContactInfo.this,OtherInfo.class);

                myIntent.putExtra("nombre", nombre);
                myIntent.putExtra("apellido", apellido);
                myIntent.putExtra("escolaridad", escolaridad);
                myIntent.putExtra("sexo", sexo);
                myIntent.putExtra("fecha_nacimiento", fecha_nacimiento);

                // obtener datos del layout
                telefono = editTelefono.getText().toString();
                correo = editCorreo.getText().toString();
                pais = editPais.getText().toString();
                ciudad = editCiudad.getText().toString();
                direccion = editDireccion.getText().toString();


                myIntent.putExtra("telefono", telefono);
                myIntent.putExtra("correo", correo);
                myIntent.putExtra("pais", pais);
                myIntent.putExtra("ciudad", ciudad);
                myIntent.putExtra("direccion", direccion);

                startActivity(myIntent);
                //startActivity(new Intent(ContactInfo.this,OtherInfo.class));
                // obtener datos de actividad anterior
                //String text = myIntent.getStringExtra("nombre");
                //System.out.println("DATOS "+text);
            }
        });

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PAISES);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.editTextPais);
        textView.setAdapter(adapter);*/

    }
}
