package br.ufc.quixada.agendacontatosandroid.model;

public class Contato {

    static int geradorIds = -1;

    int id;
    String nome;
    String telefone;
    String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ){
        this.id = id;
    }

    String endereco;

    public Contato(String nome, String telefone, String endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;

        geradorIds++;
        this.id = geradorIds;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {

       return "Nome: " + nome;

//        return "Contato{" +
//                "id=" + id +
//                ", nome='" + nome + '\'' +
//                ", telefone='" + telefone + '\'' +
//                ", endereco='" + endereco + '\'' +
//                '}';
    }

}
