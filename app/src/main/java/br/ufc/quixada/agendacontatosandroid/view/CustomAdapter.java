package br.ufc.quixada.agendacontatosandroid.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.ufc.quixada.agendacontatosandroid.model.Contato;
import br.ufc.quixada.agendacontatosandroid.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    ArrayList<Contato> dataSet;
    MainActivity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewNome;
        TextView textViewTelefone;
        TextView textViewEndereco;
        ImageView imageViewDelete;

        ImageView imageViewEdit;

        public ViewHolder( View itemView ) {

            super(itemView);

            textViewNome = itemView.findViewById( R.id.textViewNome );
            textViewTelefone = itemView.findViewById( R.id.textViewTelefone );
            textViewEndereco = itemView.findViewById( R.id.textViewEndereco );
            imageViewDelete = itemView.findViewById( R.id.imageViewDelete );
            imageViewEdit = itemView.findViewById( R.id.imageViewEdit );

        }

        public TextView getTextViewNome() {
            return textViewNome;
        }

        public TextView getTextViewTelefone() {
            return textViewTelefone;
        }

        public TextView getTextViewEndereco() {
            return textViewEndereco;
        }

        public ImageView getImageViewDelete() {
            return imageViewDelete;
        }

        public ImageView getImageViewEdit() {
            return imageViewEdit;
        }
    }

    public CustomAdapter( MainActivity activity, ArrayList<Contato> data ){
        this.dataSet = data;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate(
                R.layout.contatos_layout, parent, false
        );

        return new ViewHolder( view );
    }

    public void onBindViewHolder( ViewHolder holder, int position ) {

        Contato c = dataSet.get( position );

        holder.getTextViewNome().setText( c.getNome() );
        holder.getTextViewTelefone().setText( c.getTelefone() );
        holder.getTextViewEndereco().setText( c.getEndereco() );

        holder.getImageViewDelete().setOnClickListener(
                new View.OnClickListener(){

                    public void onClick(View view) {
                        Contato c = dataSet.get( holder.getAdapterPosition() );
                        activity.removerContato( c );

                    }
                }
        );

        holder.getImageViewEdit().setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View view) {
                        activity.editarContato( holder.getAdapterPosition() );
                    }

        });

    }

    public int getItemCount() {
        return dataSet.size();
    }
}
