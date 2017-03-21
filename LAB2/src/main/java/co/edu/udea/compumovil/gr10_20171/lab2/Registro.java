package co.edu.udea.compumovil.gr10_20171.lab2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import datos.LoginDataBaseAdapter;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        Spinner edades = (Spinner) findViewById(R.id.spinner_edad);
        List<Integer> lista = new ArrayList<Integer>();

        for(int edad=15; edad<=90; ++edad){
            lista.add(edad);
        }


        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, lista);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edades.setAdapter(dataAdapter);

        Button btn_registro = (Button)findViewById(R.id.btn_registro);

        btn_registro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // obtener datos de registro
                EditText edit_correo = (EditText)findViewById(R.id.input_email);
                String correo = edit_correo.getText().toString();

                EditText edit_usuario = (EditText)findViewById(R.id.input_usuario);
                String usuario = edit_usuario.getText().toString();

                EditText edit_clave = (EditText)findViewById(R.id.input_password);
                String clave = edit_clave.getText().toString();

                Spinner campo_edad = (Spinner)findViewById(R.id.spinner_edad);
                String edad = campo_edad.getSelectedItem().toString();

                String foto = "img_perfil";

                // registrar usuario
                // adaptador de base de datos
                LoginDataBaseAdapter adaptador;
                Context contexto = getApplicationContext();
                // conectar a base de datos
                adaptador = new LoginDataBaseAdapter(contexto);
                adaptador = adaptador.open();

                // guardar datos en BD
                adaptador.insertEntry(usuario,clave, correo, foto, edad);

                Toast.makeText(contexto, "Usuario " + usuario + " registrado satisfactoriamente", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Registro.this,LoginActivity.class);

                /*intent.putExtra("correo", correo);
                intent.putExtra("usuario", usuario);
                intent.putExtra("clave", clave);
                intent.putExtra("edad", edad);
                intent.putExtra("foto", foto);*/

                startActivity(intent);
            }
        });

    }
}
