package es.uniovi.uo245353.museosdeastudias.mda;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

public class MuseoDetailFragment extends Fragment {

    Museo museoAMostrar;
    int imagenMostrando;
    ImageView imagen;

    LayoutInflater inflater;
    ViewGroup container;
    Bundle savedInstanceState;
    Activity that;

    public MuseoDetailFragment() {
    }
    public static MuseoDetailFragment newInstance(Museo museo) {

        MuseoDetailFragment fragment = new MuseoDetailFragment();

        Bundle args = new Bundle();
        //Nombre
        String nombre = museo.getNombre();
        args.putString("nombre", nombre);

        //Ubicacion
        String concejo = museo.getConcejo();
        args.putString("concejo", concejo);

        String zona = museo.getZona();
        args.putString("zona", zona);

        String direccion = museo.getDireccion();
        args.putString("direccion", direccion);

        double latitud = museo.getLatitud();
        args.putDouble("latitud", latitud);

        double longitud = museo.getLongitud();
        args.putDouble("longitud", longitud);

        //Contacto

        String telefono = museo.getTelefono();
        args.putString("telefono", telefono);

        String email = museo.getEmail();
        args.putString("email", email);

        String web = museo.getWeb();
        args.putString("web", web);

        String masInfo = museo.getMasInfo();
        args.putString("masInfo", masInfo);

        //Articulo
        String tituloArticulo = museo.getTituloArticulo();
        args.putString("tituloArticulo", tituloArticulo);

        String textoArticulo = museo.getTextoArticulo();
        args.putString("textoArticulo", textoArticulo);

        //Redes Sociales
        String facebook = museo.getFacebook();
        args.putString("facebook", facebook);

        String twitter = museo.getTwitter();
        args.putString("twitter", twitter);

        String instagram = museo.getInstagram();
        args.putString("instagram", instagram);

        //Pracio y hora
        String horario = museo.getHorario();
        args.putString("horario", horario);

        String tarifas = museo.getTarifas();
        args.putString("tarifas", tarifas);

        //Imagenes
        String imagenes = museo.getImagenes();
        args.putString("imagenes", imagenes);

        //tam
        int tam = museo.getNMuseosInJSON();
        args.putInt("tam", tam);

        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(this.museoAMostrar == null) onCreateView(inflater,container,savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.inflater=inflater;
        this.container=container;
        this.savedInstanceState=savedInstanceState;
        View viewRoot;
        Bundle args = getArguments();
        if (args != null) {
            museoAMostrar = new Museo(
                    args.getString("nombre"),
                    args.getString("concejo"),
                    args.getString("zona"),
                    args.getString("direccion"),
                    args.getDouble("latitud"),
                    args.getDouble("longitud"),
                    args.getString("telefono"),
                    args.getString("email"),
                    args.getString("web"),
                    args.getString("masInfo"),
                    args.getString("tituloArticulo"),
                    args.getString("textoArticulo"),
                    args.getString("facebook"),
                    args.getString("twitter"),
                    args.getString("instagram"),
                    args.getString("horario"),
                    args.getString("tarifas"),
                    args.getString("imagenes"),
                    args.getInt("tam")
            );
        }
        viewRoot = inflater.inflate(R.layout.fragment_museo_detail, container,false);
        if (museoAMostrar != null) {
            that=this.getActivity();

            //Text views
            TextView nombreMuseoTextView = (TextView) viewRoot.findViewById(R.id.nombreMuseoTextView);
            TextView tituloArticuloMuseoTextView = (TextView) viewRoot.findViewById(R.id.tituloArticuloMuseoTextView);
            TextView textoArticuloTextView = (TextView) viewRoot.findViewById(R.id.textoArticuloTextView);
            TextView observacionesTextView = (TextView) viewRoot.findViewById(R.id.observacionesTextView);
            TextView redesTextView = (TextView) viewRoot.findViewById(R.id.redesTextView);
            TextView informacionContactoTextView = (TextView) viewRoot.findViewById(R.id.informacionContactoTextView);
            TextView horarioTarifasObsTextView = (TextView) viewRoot.findViewById(R.id.horarioTarifasObsTextView);
            imagen = (ImageView) viewRoot.findViewById(R.id.imgenImageView);
            ImageButton bPrevious = (ImageButton) viewRoot.findViewById(R.id.buttonPreviousImg);
            ImageButton bNext = (ImageButton) viewRoot.findViewById(R.id.buttonNextImg);
            ImageButton bgoToMaps = (ImageButton) viewRoot.findViewById(R.id.goToMapsButton);


            //Cambiando textos :)
            String DATO_FALTANTE="Dato Faltante";
            nombreMuseoTextView.setText(museoAMostrar.getNombre());
            tituloArticuloMuseoTextView.setText(museoAMostrar.getTituloArticulo());
            textoArticuloTextView.setText(museoAMostrar.getTextoArticulo());
            if(museoAMostrar.getMasInfo()!= null)
                observacionesTextView.setText(museoAMostrar.getMasInfo());
            String f,t,i;
            if(museoAMostrar.getFacebook() == null){
                f = DATO_FALTANTE;
            }
            else{
                f = museoAMostrar.getFacebook();
            }
            if(museoAMostrar.getTwitter()== null){
                t = DATO_FALTANTE;
            }
            else{
                t = museoAMostrar.getTwitter();
            }
            if(museoAMostrar.getInstagram()== null){
                i = DATO_FALTANTE;
            }
            else{
                i = museoAMostrar.getInstagram();
            }
            redesTextView.setText(
                    "Redes Sociales\n\n"+
                            "Facebook : " + f + "\n" +
                            "Twitter : " + t + "\n" +
                            "Instagram : " + i);
            String c,z,e,w;
            if(museoAMostrar.getConcejo()== null){
                c = DATO_FALTANTE;
            }
            else{
                c = museoAMostrar.getConcejo();
            }
            if(museoAMostrar.getZona()== null){
                z = DATO_FALTANTE;
            }
            else{
                z = museoAMostrar.getZona();
            }
            if(museoAMostrar.getEmail()== null){
                e = DATO_FALTANTE;
            }
            else{
                e = museoAMostrar.getEmail();
            }
            if(museoAMostrar.getWeb()== null){
                w = DATO_FALTANTE;
            }
            else{
                w = museoAMostrar.getWeb();
            }
            informacionContactoTextView.setText("Ubicación\n\n" +
                    "Concejo :" + c + "\n" +
                    "Zona :" + z + "\n" +
                    "Direccion :" + museoAMostrar.getDireccion() + "\n" +
                    "Coordenadas :" + museoAMostrar.getLatitud() + "," + museoAMostrar.getLongitud() + "\n" +
                    "Contacto\n" +
                    "Telefono :" + museoAMostrar.getTelefono() + "\n" +
                    "Email :" + e + "\n" +
                    "Web :" + w + "\n"
            );
            horarioTarifasObsTextView.setText("Horarios\n" + museoAMostrar.getHorario() + "\n" +
                    "Tarifas\n" + museoAMostrar.getTarifas());
            Picasso.get().load(museoAMostrar.getImagenes().split("\t\t")[0]).into(imagen);
            imagenMostrando = 0;
            bNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(imagenMostrando+1 > museoAMostrar.getImagenes().split("\t\t").length-1){
                        imagenMostrando=0;
                    }
                    else{
                        imagenMostrando++;
                    }
                    Picasso.get().load(museoAMostrar.getImagenes().split("\t\t")[imagenMostrando]).into(imagen);
                }
            });
            bPrevious.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(imagenMostrando-1 < 0){
                        imagenMostrando=museoAMostrar.getImagenes().split("\t\t").length-1;
                    }
                    else{
                        imagenMostrando--;
                    }
                    Picasso.get().load(museoAMostrar.getImagenes().split("\t\t")[imagenMostrando]).into(imagen);
                }
            });
            bgoToMaps.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(that, MapsActivity.class);

                    Bundle args = new Bundle();
                    double latitud = museoAMostrar.getLatitud();
                    args.putDouble("latitud", latitud);

                    double longitud = museoAMostrar.getLongitud();
                    args.putDouble("longitud", longitud);

                    String nombre = museoAMostrar.getNombre();
                    args.putString("nombre", nombre);

                    intent.putExtras(args);
                    startActivity(intent);

                }
            });
        }
        return viewRoot;
    }

}
