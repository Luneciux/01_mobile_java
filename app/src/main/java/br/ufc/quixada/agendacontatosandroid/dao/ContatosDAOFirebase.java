package br.ufc.quixada.agendacontatosandroid.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.ufc.quixada.agendacontatosandroid.model.Contato;
import br.ufc.quixada.agendacontatosandroid.view.MainActivity;

public class ContatosDAOFirebase implements ContatosDAOInterface {

    private static ContatosDAOFirebase contatosDAOFirebase = null;
    private static MainActivity mainActivity;
    private FirebaseFirestore db;

    ArrayList<Contato> lista;

    private ContatosDAOFirebase( MainActivity mainActivity ){
        ContatosDAOFirebase.mainActivity = mainActivity;
        lista = new ArrayList<>();
    }

    public static ContatosDAOInterface getInstance( MainActivity mainActivity ) {

        if( contatosDAOFirebase == null ){
            contatosDAOFirebase = new ContatosDAOFirebase( mainActivity );
        }
        return contatosDAOFirebase;
    }

    @Override
    public boolean addContato(Contato c) {

        mainActivity.controlProgressBar( true );

        Map<String, Object> user = new HashMap<>();
        user.put("nome", c.getNome() );
        user.put("telefone", c.getTelefone() );
        user.put("endereco", c.getEndereco() );

        // Add a new document with a generated ID
        db.collection("contatos")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    public void onSuccess(DocumentReference documentReference) {
                        Log.d( "Sucess", "DocumentSnapshot added with ID: " + documentReference.getId());

                        c.setDocumentId( documentReference.getId() );
                        lista.add( c );
                        mainActivity.notifyAdapter();

                        Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    public void onFailure( Exception e) {
                        Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();
                    }
                });

        mainActivity.controlProgressBar( false );

        return true;

    }

    @Override
    public boolean editContato( Contato c ) {

        DocumentReference newContato = db.collection("contatos").document( c.getDocumentId() );
        newContato.update("nome", c.getNome(),
                        "telefone", c.getTelefone(),
                        "endereco", c.getEndereco() )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();

                        for( Contato contato : lista ){

                            if( contato.getDocumentId().equals( c.getDocumentId() ) ){
                                contato.setNome( c.getNome() );
                                contato.setTelefone( c.getTelefone() );
                                contato.setEndereco( c.getEndereco() );

                                mainActivity.notifyAdapter();

                                break;

                            }
                        }

                        mainActivity.notifyAdapter();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();
                        mainActivity.notifyAdapter();
                    }
                });

        return false;
    }

    @Override
    public boolean deleteContato(int contatoId) {

        Contato c = null;

        for( Contato contato : lista ){
            if( contato.getId() == contatoId ) {
                c = contato;
                break;
            }
        }

        if( c != null ){

            final Contato apagar = c;

            DocumentReference deleteContato = db.collection("contatos").document( c.getDocumentId() );
            deleteContato.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();

                    Contato contatoApagar = null;
                    for( Contato contato : lista ){

                        if( contato.getDocumentId().equals( apagar.getDocumentId() ) ){
                            contatoApagar = contato;
                            break;
                        }

                    }

                    if( contatoApagar != null ) lista.remove( contatoApagar );
                    mainActivity.notifyAdapter();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure( Exception e) {
                            Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();

                        }
                    });

        }

        return false;
    }

    @Override
    public Contato getContato(int contatoId) {
        return null;
    }

    @Override
    public ArrayList<Contato> getListaContatos() {

        mainActivity.controlProgressBar( true );

        lista = new ArrayList<Contato>();

        db.collection("contatos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    public void onComplete( Task<QuerySnapshot> task ) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String nome = document.getString( "nome");
                                String telefone = document.getString( "telefone" );
                                String endereco = document.getString( "endereco" );

                                Contato c = new Contato( nome, telefone, endereco );
                                c.setDocumentId( document.getId() );

                                lista.add( c );

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }

                        mainActivity.notifyAdapter();
                    }
                });

        mainActivity.controlProgressBar( false );

        return lista;

    }

    @Override
    public boolean deleteAll() {
        return false;
    }

    @Override
    public boolean init() {
        db = FirebaseFirestore.getInstance();
        return true;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public boolean isStarted() {
        return false;
    }
}
