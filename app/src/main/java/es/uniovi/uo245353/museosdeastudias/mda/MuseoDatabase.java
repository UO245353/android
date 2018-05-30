package es.uniovi.uo245353.museosdeastudias.mda;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

@Database(entities = {Museo.class}, version = 1)
public abstract class MuseoDatabase extends RoomDatabase {

    //variables

    public abstract MuseoDAO museoDAO();

    private static String URL_JSON= "https://www.turismoasturias.es/open-data/catalogo-de-datos?p_p_id=opendata_WAR_importportlet&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&p_p_resource_id=export&p_p_cacheability=cacheLevelPage&p_p_col_id=column-1&p_p_col_count=1&_opendata_WAR_importportlet_structure=27552&_opendata_WAR_importportlet_type=json&_opendata_WAR_importportlet_robots=nofollow";

    private static MuseoDatabase INSTANCE;

    private static Context context;

    //Creacion de la BBDD si no esta creada
    public static MuseoDatabase getDatabase(final Context context) {
        MuseoDatabase.context=context;
        if (INSTANCE == null) {
            synchronized (MuseoDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MuseoDatabase.class,
                            "museo_database")
                            .addCallback(sRoomDatabaseCallbackCreate)
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    //Primera vez que se crea (extraña la forma de crearse, parece JS)
    /**
     * CB para crear la BBDD
     */
    private static RoomDatabase.Callback sRoomDatabaseCallbackCreate = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //Creo y relleno la BBDD
            new populateDBAsync(INSTANCE,true).execute();
        }
    };

    //Clase que se ejecutara si la BBDD ya esta creada , tengo que comprobar que no haya actualizaciones, si las hay descargo
    /**
     * CB que se ejecuta cuando se abre la BBDD
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //Abro la BBDD y compruebo que los datos esten actualizados
            new populateDBAsync(INSTANCE,false).execute();
        }
    };

    /**
     * Clase que sirve para "poblar" la BBDD
     */
    private static class populateDBAsync extends AsyncTask<Void, Void, Void> {

        private final MuseoDAO mDAO;
        private final boolean primeraVez;
        private int lastContentLenght;

        populateDBAsync(MuseoDatabase db,boolean primeraVez) {
            this.mDAO = db.museoDAO();
            this.primeraVez = primeraVez;
        }
        boolean isOnline() {
            ConnectivityManager connMgr = (ConnectivityManager)MuseoDatabase.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }

         private InputStream openHttpInputStream(String myUrl) throws MalformedURLException, IOException, ProtocolException {
            InputStream is;
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Aquí se hace realmente la petición
            conn.connect();

            lastContentLenght = Integer.parseInt(conn.getHeaderField("Content-Length"));

            is = conn.getInputStream();
            return is;
        }

        private boolean hayDatosNuevos(String myUrl) throws MalformedURLException, IOException, ProtocolException {

            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //ESTO DEBERIA DE HACERSE CON HEAD PERO EL SERVIDOR DA UN DATO INCORRECTO EN CONTENT LENGHT
            conn.setRequestMethod("GET");

            // Aquí se hace realmente la petición
            conn.connect();

            int res = Integer.parseInt(conn.getHeaderField("Content-Length"));

            if(res != mDAO.getNMuseosInJSON())
                return true;
            else
                return false;
        }

        private String downloadUrl(String myUrl) throws IOException {
            InputStream is = null;

            try {
                is = openHttpInputStream(myUrl);

                return streamToString(is);
            } finally {
                // Asegurarse de que el InputStream se cierra
                if (is != null) {
                    is.close();
                }

            }
        }

        public String streamToString(InputStream stream) throws IOException,UnsupportedEncodingException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            do {
                length = stream.read(buffer);
                if (length != -1) {
                    baos.write(buffer, 0, length);
                }
            } while (length != -1);
            return baos.toString("UTF-8");
        }

        public ArrayList<Museo> JSONToMuseo(String json) {
            //Variables
            ArrayList<Museo> ret = new ArrayList<Museo>();
            //Nombre
            JSONObject root = null;
            try {
                root = new JSONObject(json);

                JSONObject museoJson = root.getJSONObject("articles");
                JSONArray museoArray = museoJson.getJSONArray("article");


                for (int i = 0; i < museoArray.length(); i++) {
                    JSONObject museo = museoArray.getJSONObject(i);
                    JSONArray museoContent = museo.getJSONArray("dynamic-element");
                    //Variables

                    //Nombre
                    String nombre = null;

                    //Ubicacion
                    String concejo = null;
                    String zona = null;
                    String direccion = null;
                    double latitud = -1000;
                    double longitud = -1000;
                    //Contacto
                    String telefono = null;
                    String email = null;
                    String web = null;
                    String masInfo = null;
                    //Articulo
                    String tituloArticulo = null;
                    String textoArticulo = null;
                    //Redes Sociales
                    String facebook = null;
                    String twitter = null;
                    String instagram = null;
                    //Pracio y hora
                    String horario = null;
                    String tarifas = null;
                    //Imagenes
                    String imagenes = null;

                    for (int j = 0; j < museoContent.length(); j++) {
                        JSONArray caracteristica = museoArray.getJSONObject(i).getJSONArray("dynamic-element");
                        for(int w = 0 ; w<caracteristica.length();w++) {
                            String nombreCaracteristica = caracteristica.getJSONObject(w).getString("name");
                            switch (nombreCaracteristica) {
                                case "Nombre":
                                    //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                    try {
                                        if (!caracteristica.getJSONObject(w).getJSONObject("dynamic-content").isNull("content"))
                                            nombre = caracteristica.getJSONObject(w).getJSONObject("dynamic-content").getString("content");
                                    }
                                    catch (JSONException e){
                                        if (!caracteristica.getJSONObject(w).isNull("dynamic-content"))
                                            nombre = caracteristica.getJSONObject(w).getString("dynamic-content");
                                    }
                                    break;
                                case "Contacto":
                                    JSONArray museoContacto = caracteristica.getJSONObject(w).getJSONArray("dynamic-element");
                                    for (int x = 0; x < museoContacto.length(); x++) {
                                        String nombreMuseoContacto = museoContacto.getJSONObject(x).getString("name");
                                        switch (nombreMuseoContacto) {
                                            case "Concejo":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoContacto.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        concejo = museoContacto.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                }
                                                catch (JSONException e){
                                                    if (!museoContacto.getJSONObject(x).isNull("dynamic-content"))
                                                        concejo = museoContacto.getJSONObject(x).getString("dynamic-content");
                                                }
                                                break;
                                            case "Zona":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoContacto.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        zona = museoContacto.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                }
                                                catch (JSONException e){
                                                    if (!museoContacto.getJSONObject(x).isNull("dynamic-content"))
                                                        zona = museoContacto.getJSONObject(x).getString("dynamic-content");
                                                }
                                                break;
                                            case "Direccion":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoContacto.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        direccion = museoContacto.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                }
                                                catch (JSONException e){
                                                    if (!museoContacto.getJSONObject(x).isNull("dynamic-content"))
                                                        direccion = museoContacto.getJSONObject(x).getString("dynamic-content");
                                                }
                                                break;
                                            case "Telefono":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoContacto.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        telefono = museoContacto.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                }
                                                catch (JSONException e){
                                                    if (!museoContacto.getJSONObject(x).isNull("dynamic-content"))
                                                        telefono = museoContacto.getJSONObject(x).getString("dynamic-content");
                                                }
                                                break;
                                            case "Email":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoContacto.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        email = museoContacto.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                }
                                                catch (JSONException e){
                                                    if (!museoContacto.getJSONObject(x).isNull("dynamic-content"))
                                                        email = museoContacto.getJSONObject(x).getString("dynamic-content");
                                                }
                                                break;
                                            case "Web":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoContacto.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        web = museoContacto.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                }
                                                catch (JSONException e){
                                                    if (!museoContacto.getJSONObject(x).isNull("dynamic-content"))
                                                        web = museoContacto.getJSONObject(x).getString("dynamic-content");
                                                }
                                                break;
                                            case "MasInformacion":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoContacto.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        masInfo = museoContacto.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                }
                                                catch (JSONException e){
                                                    if (!museoContacto.getJSONObject(x).isNull("dynamic-content"))
                                                        masInfo = museoContacto.getJSONObject(x).getString("dynamic-content");
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    break;
                                case "Descargas":
                                    break;
                                case "RedesSociales":
                                    JSONArray museoRedesSociales = caracteristica.getJSONObject(w).getJSONArray("dynamic-element");
                                    for (int x = 0; x < museoRedesSociales.length(); x++) {
                                        String nombreMuseoRedesSociales = museoRedesSociales.getJSONObject(x).getString("name");
                                        switch (nombreMuseoRedesSociales) {
                                            case "Facebook":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoRedesSociales.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        facebook = museoRedesSociales.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                }
                                                catch (JSONException e){
                                                    if (!museoRedesSociales.getJSONObject(x).isNull("dynamic-content"))
                                                        facebook = museoRedesSociales.getJSONObject(x).getString("dynamic-content");
                                                }
                                                break;
                                            case "Twitter":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoRedesSociales.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        twitter = museoRedesSociales.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                }
                                                catch (JSONException e){
                                                    if (!museoRedesSociales.getJSONObject(x).isNull("dynamic-content"))
                                                        twitter = museoRedesSociales.getJSONObject(x).getString("dynamic-content");
                                                }
                                                break;
                                            case "Instagram":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoRedesSociales.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        instagram = museoRedesSociales.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                }
                                                catch (JSONException e){
                                                    if (!museoRedesSociales.getJSONObject(x).isNull("dynamic-content"))
                                                        instagram = museoRedesSociales.getJSONObject(x).getString("dynamic-content");
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    break;
                                case "Informacion":
                                    JSONArray museoInformacion = caracteristica.getJSONObject(w).getJSONArray("dynamic-element");
                                    for (int x = 0; x < museoInformacion.length(); x++) {
                                        String nombreMuseoInformacion = museoInformacion.getJSONObject(x).getString("name");
                                        switch (nombreMuseoInformacion) {
                                            case "Titulo":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoInformacion.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        tituloArticulo = Html.fromHtml(museoInformacion.getJSONObject(x).getJSONObject("dynamic-content").getString("content")).toString();
                                                }
                                                catch (JSONException e){
                                                    if (!museoInformacion.getJSONObject(x).isNull("dynamic-content"))
                                                        tituloArticulo = Html.fromHtml(museoInformacion.getJSONObject(x).getString("dynamic-content")).toString();
                                                }
                                                break;
                                            case "Texto":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoInformacion.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        textoArticulo = Html.fromHtml(museoInformacion.getJSONObject(x).getJSONObject("dynamic-content").getString("content")).toString();
                                                }
                                                catch (JSONException e){
                                                    if (!museoInformacion.getJSONObject(x).isNull("dynamic-content"))
                                                        textoArticulo = Html.fromHtml(museoInformacion.getJSONObject(x).getString("dynamic-content")).toString();
                                                }
                                                break;
                                            case "Horario":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try{
                                                    if(!museoInformacion.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        horario = Html.fromHtml(museoInformacion.getJSONObject(x).getJSONObject("dynamic-content").getString("content")).toString();
                                                }
                                                catch (JSONException e){
                                                    if(!museoInformacion.getJSONObject(x).isNull("dynamic-content"))
                                                        horario = Html.fromHtml(museoInformacion.getJSONObject(x).getString("dynamic-content")).toString();
                                                }
                                                break;
                                            case "Tarifas":
                                                //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                                try {
                                                    if (!museoInformacion.getJSONObject(x).getJSONObject("dynamic-content").isNull("content"))
                                                        tarifas = Html.fromHtml(museoInformacion.getJSONObject(x).getJSONObject("dynamic-content").getString("content")).toString();
                                                }
                                                catch (JSONException e){
                                                    if (!museoInformacion.getJSONObject(x).isNull("dynamic-content"))
                                                        tarifas = Html.fromHtml(museoInformacion.getJSONObject(x).getString("dynamic-content")).toString();
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    break;
                                case "Geolocalizacion":
                                    //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                    try {
                                        String aux="";
                                        if (!caracteristica.getJSONObject(w).getJSONObject("dynamic-element").getJSONObject("dynamic-content").isNull("content")) {
                                            aux = caracteristica.getJSONObject(w).getJSONObject("dynamic-element").getJSONObject("dynamic-content").getString("content").replace(" ","").replace(" ","");
                                            if(!aux.split(",")[0].isEmpty() && !aux.split(",")[1].isEmpty()) {
                                                latitud = Double.parseDouble(aux.split(",")[0].trim());
                                                longitud = Double.parseDouble(aux.split(",")[1].trim());
                                            }
                                        }
                                    }
                                    catch (JSONException e){
                                        String aux="";
                                        if (!caracteristica.getJSONObject(w).getJSONObject("dynamic-element").isNull("dynamic-content")) {
                                            aux = caracteristica.getJSONObject(w).getJSONObject("dynamic-element").getString("dynamic-content").replace(" ","").replace(" ","");
                                            if(!aux.split(",")[0].isEmpty() && !aux.split(",")[1].isEmpty()) {
                                                latitud = Double.parseDouble(aux.split(",")[0].trim());
                                                longitud = Double.parseDouble(aux.split(",")[1].trim());
                                            }
                                        }
                                    }
                                    break;
                                case "Visualizador":
                                    //Esto hay que hacelo gracias al que hizo el JSON, que no se puso de acuerdo consigo mismo
                                    try {
                                        JSONArray museoVisualizador = caracteristica.getJSONObject(w).getJSONArray("dynamic-element");
                                        imagenes="";
                                        for (int x = 0; x < museoVisualizador.length(); x++) {
                                            try {
                                                imagenes += MuseoDatabase.context.getString(R.string.URL_BASE) + museoVisualizador.getJSONObject(x).getJSONObject("dynamic-content").getString("content");
                                                if (x < museoVisualizador.length() - 1) {
                                                    imagenes += "\t\t";
                                                }
                                            }
                                            catch (JSONException e){
                                                imagenes += MuseoDatabase.context.getString(R.string.URL_BASE) + museoVisualizador.getJSONObject(x).getString("dynamic-content");
                                                if (x < museoVisualizador.length() - 1) {
                                                    imagenes += "\t\t";
                                                }
                                            }
                                        }
                                    }
                                    catch (JSONException e){
                                        try {
                                            if (!caracteristica.getJSONObject(w).getJSONObject("dynamic-element").getJSONObject("dynamic-content").getString("content").isEmpty()) {
                                                imagenes = MuseoDatabase.context.getString(R.string.URL_BASE) + caracteristica.getJSONObject(w).getJSONObject("dynamic-element").getJSONObject("dynamic-content").getString("content");
                                            }
                                        }
                                        catch (JSONException el) {
                                            if (!caracteristica.getJSONObject(w).getJSONObject("dynamic-element").getString("dynamic-content").isEmpty()) {
                                                imagenes = MuseoDatabase.context.getString(R.string.URL_BASE) + caracteristica.getJSONObject(w).getJSONObject("dynamic-element").getString("dynamic-content");
                                            }
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }

                        }
                        //no todos valen, sino tendria que pedirle al usuario permiso para almacenar un dato, de esta manera cuargo una contante en la bbdd que me indica el total(para no regenerar la BBDD)
                    }
                    int nMuseosInJSON = lastContentLenght;
                    Museo aux = new Museo(nombre, concejo, zona, direccion, latitud, longitud, telefono, email, web, masInfo, tituloArticulo, textoArticulo, facebook, twitter, instagram, horario, tarifas, imagenes, nMuseosInJSON);
                    if(ret.indexOf(aux)==-1 && nombre != null && direccion != null && latitud != -1000 && longitud !=- 1000 && telefono != null && tituloArticulo != null && textoArticulo != null && horario != null && tarifas != null && imagenes != null)
                        ret.add(aux);
                }
                return ret;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return ret;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (isOnline()) {
                    if (primeraVez) {
                        String json = downloadUrl(URL_JSON);
                        ArrayList<Museo> museos = JSONToMuseo(json);

                        for (Museo m : museos) {
                            mDAO.insertMuseo(m);
                        }

                    }
                    else {
                        if (hayDatosNuevos(URL_JSON)) {
                            String json = downloadUrl(URL_JSON);
                            ArrayList<Museo> museos = JSONToMuseo(json);
                            for (Museo m : museos) {
                                mDAO.insertMuseo(m);
                            }
                        }

                    }
                }
                else{
                    Log.e("NONET","Error de red");
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
