package es.uniovi.uo245353.museosdeastudias.mda;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

public class MdAActivity extends AppCompatActivity implements CustomOnClick {


    FragmentManager fragmentManager;
    MuseoListViewModel mlvm;
    MuseoListFragment mlf;
    MuseoDetailViewModel mdvm;
    MuseoDetailFragment mdf;
    List<String> museosToSave;
    Activity that;
    boolean mDualPane = true;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        String save="";
        if(museosToSave!=null) {
            for (String museoToSave : museosToSave) {
                save += museoToSave;
                if (museoToSave.indexOf(save) < museoToSave.lastIndexOf(-1)) save += "\t\t";
            }
            outState.putString("lm", save);
        }
        if(mlf !=null)
            getSupportFragmentManager().putFragment(outState, "mlf", mlf);
        if(mdf != null)
            getSupportFragmentManager().putFragment(outState, "mdf", mdf);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDualPane = false;
        museosToSave = new ArrayList<String>();
        fragmentManager = getSupportFragmentManager();
        for (String museo : savedInstanceState.getString("lm").split("\t\t")) {
            museosToSave.add(museo);
        }
        if(mDualPane){
            if((MuseoListFragment)fragmentManager.findFragmentById(R.id.museo_list_frag) != null )
                mlf = (MuseoListFragment)fragmentManager.findFragmentById(R.id.museo_list_frag);
            mlf = MuseoListFragment.newInstance(museosToSave);
            if((MuseoDetailFragment)fragmentManager.findFragmentById(R.id.museo_details_container) != null)
                mdf =  (MuseoDetailFragment)fragmentManager.findFragmentById(R.id.museo_details_container);

            setContentView(new LinearLayout(getApplicationContext()));
        }
        else {
            if((MuseoListFragment)fragmentManager.findFragmentById(R.id.museo_list_frag) != null )
                mlf = (MuseoListFragment)fragmentManager.findFragmentById(R.id.museo_list_frag);
            mlf = MuseoListFragment.newInstance(museosToSave);
            if((MuseoDetailFragment)fragmentManager.findFragmentById(R.id.museo_details_frag) != null)
                mdf =  (MuseoDetailFragment)fragmentManager.findFragmentById(R.id.museo_details_frag);
            setContentView(R.layout.single_fragment_museo_layout);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ponemos el layout main
        mDualPane = getResources().getBoolean(R.bool.doble);
        //inicializamos el manager
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            //Restore the fragment's instance

            return;

        }
        else{
            setContentView(R.layout.single_fragment_museo_layout);
        }



        that=this;

        //Cogemos el fragmento
        mlf = (MuseoListFragment)fragmentManager.findFragmentById(R.id.museo_list_frag);

        fragmentManager.beginTransaction().hide(mdf);

        mdvm = ViewModelProviders.of(this).get(MuseoDetailViewModel.class);
        //inicializamos el viewmodel y nos ponemos a observar, teniendo en cuenta el estado de creacion del fragmento
        mlvm = ViewModelProviders.of(this).get(MuseoListViewModel.class);
        mlvm.getMuseoNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> museos) {
                museosToSave=museos;
                if(mlf == null){
                    mlf = MuseoListFragment.newInstance(museos);
                    fragmentManager.beginTransaction().add(R.id.museo_list_frag, mlf).commit();
                }
                else {
                    mlf = MuseoListFragment.newInstance(museos);
                    fragmentManager.beginTransaction().replace(R.id.museo_list_frag, mlf).commit();
                }
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .show(mlf)
                        .commit();

            }
        });
        mdvm.getmMuseo().observe(this, new Observer<Museo>() {
            @Override
            public void onChanged(@Nullable Museo museo) {
                if ( !mDualPane ) {
                    Intent intent = new Intent(that, MuseoDetailSingleViewActivity.class);

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

                    intent.putExtras(args);
                    startActivity(intent);
                }
                else {
                    MuseoDetailFragment fragment =
                            MuseoDetailFragment.newInstance(museo);

                    // Añadir el fragmento al contenedor 'fragment_container'
                    fragmentManager.beginTransaction()
                            .replace(R.id.museo_details_container, fragment).commit();

                }
            }
        });
    }


    @Override
    public void onClickEvent(String museo) {

        mdvm.setName(museo);
    }


}
