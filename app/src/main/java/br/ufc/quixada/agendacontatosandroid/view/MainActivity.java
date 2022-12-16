package br.ufc.quixada.agendacontatosandroid.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import br.ufc.quixada.agendacontatosandroid.background.AndroidThreads;
import br.ufc.quixada.agendacontatosandroid.controller.Codes;
import br.ufc.quixada.agendacontatosandroid.R;
import br.ufc.quixada.agendacontatosandroid.dao.ContatosDAOFirebase;
import br.ufc.quixada.agendacontatosandroid.dao.ContatosDAOInterface;
import br.ufc.quixada.agendacontatosandroid.dao.ContatosDAOSharedPreferences;
import br.ufc.quixada.agendacontatosandroid.model.Contato;

public class MainActivity extends AppCompatActivity {



    ListView listViewContatos;

    String apresentarListaContatos;
    static ArrayList<Contato> lista;
    Button btnAdd;
    Button btnLimpar;

    int posicao;

    static CustomAdapter adapter;
    RecyclerView recyclerView;

    ContatosDAOInterface contatosDAO;

    AndroidThreads threads;

    ProgressBar progressBarDownload;
    boolean progressBarVisibility;

    ExecutorService executorService;


    public static void addContato( Contato c ){
        lista.add( c );
        adapter.notifyDataSetChanged();
    }



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contatosDAO = ContatosDAOFirebase.getInstance( this );
        contatosDAO.init();
        posicao = -1;

        progressBarDownload = findViewById( R.id.progressBarDownload );
        progressBarDownload.setVisibility( ProgressBar.GONE );
        progressBarVisibility = false;

       //lista = new ArrayList<Contato>();
        lista = contatosDAO.getListaContatos();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );

        adapter = new CustomAdapter( this, lista );
        recyclerView = findViewById( R.id.recyclerViewContatos );
        recyclerView.setLayoutManager( linearLayoutManager );
        recyclerView.setAdapter( adapter );

        recyclerView.addItemDecoration( new DividerItemDecoration( this, DividerItemDecoration.VERTICAL ) );



    }


    public void controlProgressBar( boolean show ){

        if( show ){
            progressBarDownload.setVisibility( ProgressBar.VISIBLE );
            progressBarVisibility = true;

        } else {
           progressBarDownload.setVisibility( ProgressBar.GONE );
            progressBarVisibility = false;
        }

    }


    public void pararThread(){
        threads.setRunning( false );
    }

    protected void onStop() {
        super.onStop();
        contatosDAO.close();
    }

    public void adicionarContatos(View view ){

        Intent intent = new Intent( this, Activity2.class );
        startActivityForResult( intent, Codes.REQUEST_ADD );


    }

    public void removerContato( Contato c ){
        contatosDAO.deleteContato( c.getId() );

    }

    public void editarContato( int pos ){

        Contato c = lista.get( pos );

        String nome = c.getNome();
        String telefone = c.getTelefone();
        String endereco = c.getEndereco();
        int id = c.getId();

        Intent intent = new Intent( this, Activity2.class );
        intent.putExtra( Codes.CHAVE_NOME, nome );
        intent.putExtra( Codes.CHAVE_TELEFONE, telefone );
        intent.putExtra( Codes.CHAVE_ENDERECO, endereco );
        intent.putExtra( Codes.CHAVE_ID, ""+id );
        intent.putExtra( Codes.DOCUMENT_ID, c.getDocumentId() );

        startActivityForResult( intent, Codes.REQUEST_EDT );

    }

    public void notifyAdapter(){
        adapter.notifyDataSetChanged();
    }

    public void limpar( View view ){

       contatosDAO.deleteAll();
       adapter.notifyDataSetChanged();

        Toast.makeText( this, "Contatos Apagados", Toast.LENGTH_LONG ).show();

    }



    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == Codes.REQUEST_ADD && resultCode == Codes.RESPONSE_OK ){
            //Atualizar a lista de contatos

            String nome = data.getExtras().getString( Codes.CHAVE_NOME );
            String telefone = data.getExtras().getString( Codes.CHAVE_TELEFONE );
            String endereco = data.getExtras().getString( Codes.CHAVE_ENDERECO );

            Contato c = new Contato( nome, telefone, endereco );

            contatosDAO.addContato( c );
            adapter.notifyDataSetChanged();

        } else if( requestCode == Codes.REQUEST_EDT && resultCode == Codes.RESPONSE_OK ){

            String nome = data.getExtras().getString( Codes.CHAVE_NOME );
            String telefone = data.getExtras().getString( Codes.CHAVE_TELEFONE );
            String endereco = data.getExtras().getString( Codes.CHAVE_ENDERECO );
            String idString = data.getExtras().getString( Codes.CHAVE_ID );
            String documentId = data.getExtras().getString( Codes.DOCUMENT_ID );

            int id = -1;
            if( idString != null ){

                id = Integer.parseInt( idString );
                Toast.makeText( this, "ID: " + id, Toast.LENGTH_LONG ).show();

                Contato c = new Contato( nome, telefone, endereco );
                c.setId( id );
                c.setDocumentId( documentId );

                contatosDAO.editContato( c );


            }




            adapter.notifyDataSetChanged();
        }
    }
}