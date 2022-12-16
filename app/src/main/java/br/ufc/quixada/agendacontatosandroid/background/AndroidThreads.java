package br.ufc.quixada.agendacontatosandroid.background;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import br.ufc.quixada.agendacontatosandroid.model.Contato;
import br.ufc.quixada.agendacontatosandroid.view.MainActivity;

public class AndroidThreads extends Thread {

    boolean running;

    int id;
    static int contador = 0;
    Handler handler;

    public AndroidThreads( Handler handler ){
        this.running = true;
        this.id = contador++;
        this.handler = handler;
    }

    public void run(){

        while ( running ){

            Log.d( "Android Threads", "Codigo executando na thread secundaria: " + id );


            try{
                Thread.sleep( 2000 );
            } catch( InterruptedException e ){
                e.printStackTrace();
            }

            Message message = new Message();

            Contato c = new Contato( "nome", "telefone", "endereco" );
            Gson gson = new Gson();
            String contatoString = gson.toJson( c );

            message.obj = contatoString;
            handler.sendMessage( message );

        }

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning( boolean running ) {
        this.running = running;
    }
}
