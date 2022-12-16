package br.ufc.quixada.agendacontatosandroid.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import br.ufc.quixada.agendacontatosandroid.controller.Codes;
import br.ufc.quixada.agendacontatosandroid.model.Contato;

public class ContatosDAOSharedPreferences implements ContatosDAOInterface {

    private static ArrayList<Contato> lista;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Context context;
    private static boolean initialized = false;

    private static ContatosDAOSharedPreferences contatosDAOSharedPreferences = null;

    private ContatosDAOSharedPreferences( Context c ){
        ContatosDAOSharedPreferences.context = c;
    }

    public static ContatosDAOInterface getInstance(Context context) {

        if( contatosDAOSharedPreferences == null ){
            contatosDAOSharedPreferences = new ContatosDAOSharedPreferences( context );
        }
        return contatosDAOSharedPreferences;
    }

    @Override
    public boolean addContato(Contato c) {
        lista.add( c );
        return true;
    }

    @Override
    public boolean editContato( Contato c ) {

        boolean edited = false;

        for( Contato contato : lista ){

            if( contato.getId() == c.getId() ){
                contato.setNome( c.getNome() );
                contato.setTelefone( c.getTelefone() );
                contato.setEndereco( c.getEndereco() );

                edited = true;
                break;

            }
        }
        return edited;
    }

    @Override
    public boolean deleteContato(int contatoId) {

        boolean deleted = false;
        Contato contatoDeletar = null;

        for( Contato contato : lista ){
            if( contato.getId() == contatoId ){

               contatoDeletar = contato;
                deleted = true;
                break;

            }
        }

        if( contatoDeletar != null ) lista.remove( contatoDeletar );
        return deleted;

    }

    @Override
    public Contato getContato( int contatoId ) {

        Contato contatoBuscar = null;
        for( Contato contato : lista ){
            if( contato.getId() == contatoId ){
                contatoBuscar = contato;
                break;
            }
        }

        return contatoBuscar;
    }

    @Override
    public boolean init() {

        sharedPreferences = context.getSharedPreferences(Codes.SHAREDPREFERENCES_FILE,
                Context.MODE_PRIVATE );
        editor = sharedPreferences.edit();

        String listaContatosString = sharedPreferences.getString( Codes.SHAREDPREFERENCES_FILE_KEY, "" );

        if( listaContatosString.equals( "" ) ) lista = new ArrayList<Contato>();
        else{

            Type listType = TypeToken.getParameterized(ArrayList.class, Contato.class).getType();
            Gson gson = new Gson();
            lista = gson.fromJson( listaContatosString, listType );

        }

        initialized = true;

        return true;
    }

    public ArrayList<Contato> getListaContatos(){
        return lista;
    }

    public boolean deleteAll(){
        lista.clear();
        return true;
    }

    public boolean isStarted(){
        return initialized;
    }

    @Override
    public boolean close() {

        Gson gson = new Gson();
        String listaContatosString = gson.toJson( lista );
        editor.putString( Codes.SHAREDPREFERENCES_FILE_KEY, listaContatosString );
        editor.apply();

        return true;
    }
}
