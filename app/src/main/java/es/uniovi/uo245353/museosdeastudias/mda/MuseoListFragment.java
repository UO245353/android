package es.uniovi.uo245353.museosdeastudias.mda;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MuseoListFragment extends Fragment {

    //variables
    private MuseoRecyclerViewAdapter adapter = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;
    private CustomOnClick customOnClick;
    List<String> museos;

    LayoutInflater inflater;
    ViewGroup container;
    Bundle savedInstanceState;

    private static final String MUSEO_NAMES_ARG = "museoNames";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static MuseoListFragment newInstance(List<String> museos) {

        MuseoListFragment fragment = new MuseoListFragment();
        String museosConcat ="";
        for(int i = 0 ; i<museos.size() ; i++){
            museosConcat += museos.get(i);
            if(i < museos.size()-1){
                museosConcat += "\t\n\t";
            }
        }
        Bundle args = new Bundle();
        args.putString(MUSEO_NAMES_ARG, museosConcat);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String save="";
        if(museos!=null) {
            for (String museoToSave : museos) {
                save += museoToSave;
                if (museoToSave.indexOf(save) < museoToSave.lastIndexOf(-1)) save += "\t\t";
            }
            outState.putString("lm", save);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if(this.museos == null) onCreateView(inflater,container,savedInstanceState);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            museos = new ArrayList<String>();
            for (String museo : savedInstanceState.getString("lm").split("\t\t")) {
                museos.add(museo);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();

        if (activity != null) {
            customOnClick = (CustomOnClick) activity;
            museos = new ArrayList<String>();
            Bundle args = getArguments();
            if (args != null) {
                museos.clear();
                for(String str :args.getString(MUSEO_NAMES_ARG).split("\t\n\t")){
                    museos.add(str);
                }
            }
            adapter = new MuseoRecyclerViewAdapter(getContext(), museos , customOnClick);

        }
        else {
            throw new ClassCastException(context.toString() +
                    " must implement Callbacks");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.inflater=inflater;
        this.container=container;
        this.savedInstanceState=savedInstanceState;
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_museo_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return  rootView;
    }

    /**
     * Añadir los museos
     * @param museos
     */
    public void addMuseos(List<String> museos) {
        adapter.addMuseos(museos);
    }
}
