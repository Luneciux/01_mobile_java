package br.ufc.quixada.agendacontatosandroid.dao;

import android.content.Context;

import java.util.ArrayList;

import br.ufc.quixada.agendacontatosandroid.model.Contato;

public interface ContatosDAOInterface {

    static ContatosDAOInterface getInstance(Context context) {
        return null;
    }

    boolean addContato( Contato c );
    boolean editContato( Contato c );
    boolean deleteContato( int contatoId );
    Contato getContato( int contatoId );
    ArrayList<Contato> getListaContatos();
    boolean deleteAll();

    boolean init();
    boolean close();
    boolean isStarted();


}
