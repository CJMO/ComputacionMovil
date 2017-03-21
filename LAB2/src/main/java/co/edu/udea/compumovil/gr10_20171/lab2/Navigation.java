package co.edu.udea.compumovil.gr10_20171.lab2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import datos.LoginDataBaseAdapter;

public class Navigation extends AppCompatActivity { //FragmentActivity
    private boolean isDrawerLocked = false;
    private android.support.v4.widget.DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String[] drawerItems;

    // adaptador de base de datos
    LoginDataBaseAdapter adaptador;

    public Navigation() {

    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.navigation);

        // conectar a base de datos
        adaptador = new LoginDataBaseAdapter(this);
        adaptador = adaptador.open();

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        if(((ViewGroup.MarginLayoutParams)frameLayout.getLayoutParams()).leftMargin == (int)getResources().getDimension(R.dimen.drawer_size)) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, drawerList);
            drawerLayout.setScrimColor(Color.TRANSPARENT);
            isDrawerLocked = true;
        }

        // Set the adapter for the list view
        drawerItems = getResources().getStringArray(R.array.lista_items_menu);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.actionSort,
                R.string.actionSort
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //    getActionBar().setTitle(title);
                //   ((FragmentInterface)fragment).showMenuActions();
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle("Select Option");
                //  ((FragmentInterface)fragment).hideMenuActions();
                invalidateOptionsMenu();
            }
        };

        if(!isDrawerLocked) {
            drawerLayout.setDrawerListener(drawerToggle);
        }

        // Set the drawer toggle as the DrawerListener
        DrawerItemClickListener drawerItemClickListener = new DrawerItemClickListener();
        drawerList.setOnItemClickListener(drawerItemClickListener);

        if(!isDrawerLocked) {
            //  getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //drawerLayout.openDrawer(Gravity.LEFT);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        MenuItem item = null;
        if(item != null) {
            item.setVisible(!drawerOpen);
        }
        item = null;
        if(item != null) {
            item.setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * The drawer item click listener
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }

        /** Swaps fragments in the main content view */
        private void selectItem(int position) {
            Bundle bundle;
            switch(position) {
                default:
                    break;
            }
           /* FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();*/

            // Highlight the selected item, update the title, and close the drawer
            drawerList.setItemChecked(position, true);

            String elemento_menu = drawerList.getItemAtPosition(position).toString();

            switch (elemento_menu){
                case "Cerrar sesión":

                    // gestionar datos de sesión
                    SharedPreferences sharedpreferences;
                    String MyPREFERENCES = "Mis datos" ;
                    String Name = "nombre_sesion";

                    // inicializar gestor de sesión
                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();

                    Intent intent = new Intent(Navigation.this,LoginActivity.class);
                    startActivity(intent);

                    //Toast.makeText(getBaseContext(), "Cerrando sesión", Toast.LENGTH_LONG).show();
                    break;
                case "Perfil":
                    vista_perfil();
                    break;
                case "Eventos":
                    Intent intent_menu = new Intent(Navigation.this,MenuLateral.class);
                    startActivity(intent_menu);
                default:
                    Toast.makeText(getBaseContext(), "Setting " + elemento_menu, Toast.LENGTH_LONG).show();
                    break;
            }

            setTitle(Navigation.this.drawerItems[position]);
            if(!isDrawerLocked) {
                drawerLayout.closeDrawer(drawerList);
            }
        }
    }

    public void vista_perfil(){

        setContentView(R.layout.fragment_perfil);

        // gestionar datos de sesión
        SharedPreferences sharedpreferences;
        String MyPREFERENCES = "Mis datos" ;
        String Name = "nombre_sesion";

        // inicializar gestor de sesión
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // verificar si hay sesión iniciada
        String usuario_sesion = sharedpreferences.getString(Name, null);
        if(usuario_sesion != null){
            Toast.makeText(getBaseContext(), "Conectado " + usuario_sesion, Toast.LENGTH_LONG).show();

            String[] datos_usuario = adaptador.datos_usuario(usuario_sesion);

            String foto = datos_usuario[0];
            String edad = datos_usuario[1];
            String correo = datos_usuario[2];

            //System.out.println("PERFIL "+clave_ingreso);
            //Toast.makeText(getBaseContext(), "PERFIL "+clave_ingreso, Toast.LENGTH_LONG).show();

            TextView texto_nombre_usuario = (TextView)findViewById(R.id.perfil_nombre_usuario);
            texto_nombre_usuario.setText(usuario_sesion);

            TextView texto_edad_usuario = (TextView)findViewById(R.id.perfil_edad);
            texto_edad_usuario.setText(edad);

            TextView texto_correo_usuario = (TextView)findViewById(R.id.perfil_correo_usuario);
            texto_correo_usuario.setText(correo);

        }

        // navigation drawer
        //gestionar_navigation_drawer();
        //drawerLayout.openDrawer(GravityCompat.START);
        drawerList.refreshDrawableState();
        drawerList.invalidate();


    }

    public void gestionar_navigation_drawer(){

        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        if(((ViewGroup.MarginLayoutParams)frameLayout.getLayoutParams()).leftMargin == (int)getResources().getDimension(R.dimen.drawer_size)) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, drawerList);
            drawerLayout.setScrimColor(Color.TRANSPARENT);
            isDrawerLocked = true;
        }

        // Set the adapter for the list view
        drawerItems = getResources().getStringArray(R.array.lista_items_menu);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.actionSort,
                R.string.actionSort
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //    getActionBar().setTitle(title);
                //   ((FragmentInterface)fragment).showMenuActions();
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle("Select Option");
                //  ((FragmentInterface)fragment).hideMenuActions();
                invalidateOptionsMenu();
            }
        };

        if(!isDrawerLocked) {
            drawerLayout.setDrawerListener(drawerToggle);
        }

        // Set the drawer toggle as the DrawerListener
        DrawerItemClickListener drawerItemClickListener = new DrawerItemClickListener();
        drawerList.setOnItemClickListener(drawerItemClickListener);

        if(!isDrawerLocked) {
            //  getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //drawerLayout.openDrawer(Gravity.LEFT);
        drawerLayout.openDrawer(GravityCompat.START);
    }
}