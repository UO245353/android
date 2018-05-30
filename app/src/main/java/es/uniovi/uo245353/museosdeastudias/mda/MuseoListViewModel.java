package es.uniovi.uo245353.museosdeastudias.mda;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MuseoListViewModel extends AndroidViewModel {
    //variables
    private MuseoRepository mRepository;
    private LiveData<List<String>> mNames;

    /**
     * Constructor
     * @param application
     */
    public MuseoListViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MuseoRepository(application);
        mNames = mRepository.getMuseoNames();
    }

    /**
     *
     * @return Listado de nombres
     */
    public LiveData<List<String>> getMuseoNames() {
        return mNames;
    }

}
