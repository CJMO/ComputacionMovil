package co.edu.udea.compumovil.gr10_20171.lab3;

//import android.app.Fragment;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MenuLateral extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Bundle argumentos = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lateral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //crear fragment por defecto (eventos)
        Fragment fragment = null;

        // mostrar página de eventos
        fragment = new Fragment_eventos();

        argumentos.putString("individual", "NO");
        argumentos.putString("modo-eventos", "local");
        fragment.setArguments(argumentos);

        //replacing the fragment
        if (fragment != null) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenido_fragment, fragment);
            ft.commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //creating fragment object
        Fragment fragment = null;

        if (id == R.id.menu_eventos) {

            // mostrar página de eventos
            fragment = new Fragment_eventos();

            argumentos.putString("modo-eventos", "local");
            fragment.setArguments(argumentos);


        }
        if (id == R.id.menu_eventos_actualizar) {

            // mostrar página de eventos
            fragment = new Fragment_eventos();

            argumentos.putString("modo-eventos", "servicio");
            fragment.setArguments(argumentos);

        }
        else if (id == R.id.menu_perfil) {
            //Toast.makeText(getBaseContext(), "PERFIL", Toast.LENGTH_LONG).show();

            // mostrar página de perfil
            fragment = new Fragment_perfil();

        } else if (id == R.id.menu_config) {

            //fragment = new FragmentPreferencias();

            /*Intent preferencias = new Intent(MenuLateral.this,FragmentPreferencias.class);
            startActivity(preferencias);*/

            Intent preferencias = new Intent(MenuLateral.this,ActivityPreferencias.class);
            startActivity(preferencias);


        } else if (id == R.id.menu_logout) {
            // gestionar datos de sesión
            SharedPreferences sharedpreferences;
            String MyPREFERENCES = "Mis datos" ;
            String Name = "nombre_sesion";

            // inicializar gestor de sesión
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            // realizar cierre de sesión
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();

            // redireccionar a actividad de inicio de sesión
            Intent intent = new Intent(MenuLateral.this,LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.menu_about) {
            fragment = new Fragment_info();

        }

        //replacing the fragment
        if (fragment != null) {
            //android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contenido_fragment, fragment);
            // retornar a pantalla previa
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }
}
