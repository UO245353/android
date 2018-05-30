package es.uniovi.uo245353.museosdeastudias.mda;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Interface que define las consultas , inserciones ... a BBDD
 */
@Dao
public interface MuseoDAO {
    @Insert
    public void insertMuseo(Museo museo);

    @Query("DELETE from museo_table")
    public void deleteAll();

    @Query("SELECT * FROM museo_table WHERE nombre LIKE :nombre")
    public LiveData<Museo> getMuseoByName(String nombre);

    @Query("SELECT nombre FROM museo_table")
    public LiveData<List<String>> getNames();

    @Query("SELECT nMuseosInJSON FROM museo_table ORDER BY RANDOM() LIMIT 1")
    public int getNMuseosInJSON();
}
