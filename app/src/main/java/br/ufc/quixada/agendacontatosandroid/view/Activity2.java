package br.ufc.quixada.agendacontatosandroid.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.ufc.quixada.agendacontatosandroid.controller.Codes;
import br.ufc.quixada.agendacontatosandroid.R;

public class Activity2 extends AppCompatActivity {

    EditText edtNome;
    EditText edtTelefone;
    EditText edtEndereco;

    int id;
    String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        id = -1;

        edtNome = findViewById( R.id.edtNomeTela02 );
        edtTelefone = findViewById( R.id.edtTelefoneTela02 );
        edtEndereco = findViewById( R.id.edtEnderecoTela02 );

        if( getIntent().getExtras() != null ){

            String nome = getIntent().getExtras().getString( Codes.CHAVE_NOME );
            String telefone = getIntent().getExtras().getString( Codes.CHAVE_TELEFONE );
            String endereco = getIntent().getExtras().getString( Codes.CHAVE_ENDERECO );
            String idString = getIntent().getExtras().getString( Codes.CHAVE_ID );
            documentId = getIntent().getExtras().getString( Codes.DOCUMENT_ID );

            if( idString != null ) {
                id = Integer.parseInt( idString );
                Toast.makeText( this, "Id: " + documentId, Toast.LENGTH_LONG ).show();
            }

            edtNome.setText( nome );
            edtTelefone.setText( telefone );
            edtEndereco.setText( endereco );

        }

//        String valor = getIntent().getExtras().getString( "chave" );
//        Toast.makeText( this, valor, Toast.LENGTH_LONG ).show();

    }

    public void voltar( View view ){
        finish();
    }

    public void adicionar( View view ){

        String nome = edtNome.getText().toString();
        String telefone = edtTelefone.getText().toString();
        String endereco = edtEndereco.getText().toString();

        Intent intent = new Intent();
        intent.putExtra( Codes.CHAVE_NOME, nome );
        intent.putExtra( Codes.CHAVE_TELEFONE, telefone );
        intent.putExtra( Codes.CHAVE_ENDERECO, endereco );


        if( id >= 0 ) {
            intent.putExtra( Codes.CHAVE_ID, ""+id );
            intent.putExtra( Codes.DOCUMENT_ID, documentId );
        }

        setResult( Codes.RESPONSE_OK, intent );
        finish();

    }
}