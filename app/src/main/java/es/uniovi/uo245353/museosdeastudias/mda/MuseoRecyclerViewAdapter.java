package es.uniovi.uo245353.museosdeastudias.mda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MuseoRecyclerViewAdapter extends RecyclerView.Adapter <MuseoRecyclerViewAdapter.ViewHolder>{

    //variables
    public LayoutInflater inflater = null;
    private List<String> museos = new ArrayList<String>();
    CustomOnClick customOnClick;

    //implementacion del view holder
    public final class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
        //variable del holder
        TextView museoName;

        //constructor
        public ViewHolder(View itemView) {
            super(itemView);
            //Se inicia museo name
            museoName= (TextView) itemView.findViewById(R.id.museoNameTextView);
            itemView.setOnClickListener(this);
        }

        /**
         * Implementacion del evento onClick del view holder
         * @param v
         */
        @Override
        public void onClick(View v) {
            //Por si museos es 0
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            if(museos.size()> 0)
                customOnClick.onClickEvent(museos.get(getAdapterPosition()));
        }
    }

    /**
     * Cuando se creo la vista del vH hay que inflar la vista de la fila
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = inflater.inflate(R.layout.fragment_list_row_layout, parent, false);
        return new ViewHolder(rowView);
    }

    /**
     * Cuando se fija la vista de vH hay que poner el texto en su sitio
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.museoName.setText(museos.get(position));
    }

    /**
     * Cuenta de items
     * @return
     */
    @Override
    public int getItemCount() {
        return museos.size();
    }
    //constructor
    public MuseoRecyclerViewAdapter(Context context, List<String> museos, CustomOnClick customOnClick) {

        if (context == null) {
            throw new IllegalArgumentException();
        }
        this.museos = museos;
        if(museos == null){
            this.museos = new ArrayList<String>();
            this.museos.add("Cargando Museos");
        }
        this.inflater = LayoutInflater.from(context);
        this.customOnClick = customOnClick;
    }

    /**
     * Metodo para añadir los museos segun vengan llegando
     * @param museos
     */
    public void addMuseos(List<String> museos) {

        if (museos == null) {
            throw new IllegalArgumentException();
        }
        //Metemos solo los que no esten, se nos pasan todos
        for(String museo : museos){
            if(this.museos.indexOf(museo) == -1)
                this.museos.add(museo);
        }

        //notificar que ha cambiado el dataset
        notifyDataSetChanged();
    }

}
