package es.uniovi.uo245353.museosdeastudias.mda;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lati = -1000;
    double longi = -1000;
    String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();


        // Existe el contenedor del fragmento?
        if (findViewById(R.id.map) != null) {

            // Si estamos restaurando desde un estado previo no hacemos nada
            if (savedInstanceState != null) {
                return;
            }
            nombre = extras.getString("nombre");
            lati = extras.getDouble("latitud");
            longi = extras.getDouble("longitud");

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
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
        if(nombre != null && lati != -1000 && longi != -1000) {
            // Add a marker in Sydney and move the camera
            LatLng museo = new LatLng(lati, longi);
            mMap.setMinZoomPreference(12);
            mMap.addMarker(new MarkerOptions().position(museo).title(nombre));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(museo));
        }
    }
}
