package co.edu.udea.compumovil.gr10_20171.lab3;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.StringTokenizer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        String coordenadas = intent.getStringExtra("coordenadas");
        String[] tokens = coordenadas.split(",");
        int zoom = 17;

        // Add a marker in Sydney and move the camera
        LatLng posicion = new LatLng(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]));
        mMap.addMarker(new MarkerOptions().position(posicion).title("Marcador evento"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
        //mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15));
    }
}
