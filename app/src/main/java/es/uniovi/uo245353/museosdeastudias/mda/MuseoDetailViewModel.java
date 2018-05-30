package es.uniovi.uo245353.museosdeastudias.mda;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

public class MuseoDetailViewModel extends AndroidViewModel {

    private MuseoRepository mRepository;
    private LiveData<Museo> mMuseo;
    private MutableLiveData<String> mName;

    /**
     * Selecciona un museo
     * @param mName nombre del museo
     */
    public void setName(String mName) {
        this.mName.setValue(mName);
    }

    /**
     * Devuelve el museo seleccionado
     * @return
     */
    public LiveData<Museo> getmMuseo() {
        return this.mMuseo;
    }

    /**
     * Constructor
     * @param application
     */
    public MuseoDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MuseoRepository(application);
        mName = new MutableLiveData<>();
        mMuseo = Transformations.switchMap(mName, new Function<String, LiveData<Museo>>() {
            @Override
            public LiveData<Museo> apply(String input) {
                return mRepository.getMuseoByName(input);
            }
        });

    }
}
