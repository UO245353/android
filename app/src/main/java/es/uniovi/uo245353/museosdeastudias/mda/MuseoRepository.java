package es.uniovi.uo245353.museosdeastudias.mda;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Repositorio que servira los datos necesitados al modelo
 */
public class MuseoRepository {

    //Variables a utilizar por el repositorio
    private MuseoDAO museoDAO;
    private LiveData<Museo> mMuseo;
    private LiveData<List<String>> mNombres;

    /**
     * Constructor del repositorio
     * @param application
     */
    public MuseoRepository(Application application) {
        MuseoDatabase museoDatabase = MuseoDatabase.getDatabase(application);
        this.museoDAO = museoDatabase.museoDAO();
        this.mNombres = this.museoDAO.getNames();
    }

    /**
     *
     * @param name Nombre del museo pedido
     * @return
     */
    public LiveData<Museo> getMuseoByName(String name) {
        this.mMuseo = this.museoDAO.getMuseoByName(name);
        return this.mMuseo;
    }

    /**
     *
     * @return Live data de la lista de nombres museos
     */
    public LiveData<List<String>> getMuseoNames() {
        return mNombres;
    }

}
