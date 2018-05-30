package es.uniovi.uo245353.museosdeastudias.mda;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


/**
 * Clase (anotada) que describe la BBDD
 */
@Entity(tableName = "museo_table")
public class Museo {

    //Variables

    //Nombre
    @PrimaryKey
    @NonNull
    private String nombre;

    //Ubicacion
    private String concejo;
    private String zona;
    @NonNull
    private String direccion;
    @NonNull
    private double latitud;
    @NonNull
    private double longitud;

    //Contacto
    @NonNull
    private String telefono;
    private String email;
    private String web;
    private String masInfo;

    //Articulo
    @NonNull
    private String tituloArticulo;
    @NonNull
    private String textoArticulo;

    //Redes Sociales
    private String facebook;
    private String twitter;
    private String instagram;

    //Pracio y hora
    @NonNull
    private String horario;
    @NonNull
    private String tarifas;

    //Imagenes
    @NonNull
    private String imagenes;

    //no todos valen, sino tendria que pedirle al usuario permiso para almacenar un dato, de esta manera cuargo una contante en la bbdd que me indica el total(para no regenerar la BBDD)
    @NonNull
    private int nMuseosInJSON;

    //Constructor

    public Museo(@NonNull String nombre, String concejo, String zona, @NonNull String direccion, @NonNull double latitud, @NonNull double longitud, @NonNull String telefono, String email, String web, String masInfo, @NonNull String tituloArticulo, @NonNull String textoArticulo, String facebook, String twitter, String instagram, @NonNull String horario, @NonNull String tarifas, @NonNull String imagenes, @NonNull int nMuseosInJSON) {
        this.nombre = nombre;
        this.concejo = concejo;
        this.zona = zona;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.telefono = telefono;
        this.email = email;
        this.web = web;
        this.masInfo = masInfo;
        this.tituloArticulo = tituloArticulo;
        this.textoArticulo = textoArticulo;
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.horario = horario;
        this.tarifas = tarifas;
        this.imagenes = imagenes;
        this.nMuseosInJSON = nMuseosInJSON;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    public String getConcejo() {
        return concejo;
    }

    public void setConcejo(String concejo) {
        this.concejo = concejo;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    @NonNull
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(@NonNull String direccion) {
        this.direccion = direccion;
    }

    @NonNull
    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(@NonNull double latitud) {
        this.latitud = latitud;
    }

    @NonNull
    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(@NonNull double longitud) {
        this.longitud = longitud;
    }

    @NonNull
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NonNull String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getMasInfo() {
        return masInfo;
    }

    public void setMasInfo(String masInfo) {
        this.masInfo = masInfo;
    }

    @NonNull
    public String getTituloArticulo() {
        return tituloArticulo;
    }

    public void setTituloArticulo(@NonNull String tituloArticulo) {
        this.tituloArticulo = tituloArticulo;
    }

    @NonNull
    public String getTextoArticulo() {
        return textoArticulo;
    }

    public void setTextoArticulo(@NonNull String textoArticulo) {
        this.textoArticulo = textoArticulo;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    @NonNull
    public String getHorario() {
        return horario;
    }

    public void setHorario(@NonNull String horario) {
        this.horario = horario;
    }

    @NonNull
    public String getTarifas() {
        return tarifas;
    }

    public void setTarifas(@NonNull String tarifas) {
        this.tarifas = tarifas;
    }

    @NonNull
    public String getImagenes() {
        return imagenes;
    }

    public void setImagenes(@NonNull String imagenes) {
        this.imagenes = imagenes;
    }

    @NonNull
    public int getNMuseosInJSON() {
        return nMuseosInJSON;
    }

    public void setNMuseosInJSON(@NonNull int nMuseosInJSON) {
        this.nMuseosInJSON = nMuseosInJSON;
    }
    @Override
    public String toString() {
        return "Museo{" +
                "nombre='" + nombre + '\'' +
                ", concejo='" + concejo + '\'' +
                ", zona='" + zona + '\'' +
                ", direccion='" + direccion + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", web='" + web + '\'' +
                ", masInfo='" + masInfo + '\'' +
                ", tituloArticulo='" + tituloArticulo + '\'' +
                ", textoArticulo='" + textoArticulo + '\'' +
                ", facebook='" + facebook + '\'' +
                ", twitter='" + twitter + '\'' +
                ", instagram='" + instagram + '\'' +
                ", horario='" + horario + '\'' +
                ", tarifas='" + tarifas + '\'' +
                ", imagenes='" + imagenes + '\'' +
                ", nMuseosInJSON=" + nMuseosInJSON +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Museo))
            return false;
        Museo museo = (Museo) o;
        return getNombre() == museo.getNombre();
    }

}