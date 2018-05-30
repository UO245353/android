package es.uniovi.uo245353.museosdeastudias.mda;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MuseoDetailSingleViewActivity extends AppCompatActivity {
    Museo museoAMostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        setContentView(R.layout.museo_details_actitity);

        // Existe el contenedor del fragmento?
        if (findViewById(R.id.fragment_container) != null) {

            // Si estamos restaurando desde un estado previo no hacemos nada
            if (savedInstanceState != null) {
                return;
            }
            museoAMostrar = new Museo(
                    extras.getString("nombre"),
                    extras.getString("concejo"),
                    extras.getString("zona"),
                    extras.getString("direccion"),
                    extras.getDouble("latitud"),
                    extras.getDouble("longitud"),
                    extras.getString("telefono"),
                    extras.getString("email"),
                    extras.getString("web"),
                    extras.getString("masInfo"),
                    extras.getString("tituloArticulo"),
                    extras.getString("textoArticulo"),
                    extras.getString("facebook"),
                    extras.getString("twitter"),
                    extras.getString("instagram"),
                    extras.getString("horario"),
                    extras.getString("tarifas"),
                    extras.getString("imagenes"),
                    extras.getInt("tam")
            );
            // Crear el fragmento pasándole el parámetro
            MuseoDetailFragment fragment = MuseoDetailFragment.newInstance(museoAMostrar);

            // Añadir el fragmento al contenedor 'fragment_container'
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();

        }
    }
}

