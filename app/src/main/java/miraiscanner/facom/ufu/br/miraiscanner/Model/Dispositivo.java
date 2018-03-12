package miraiscanner.facom.ufu.br.miraiscanner.Model;

/**
 * Created by MarceloPrado on 12/03/2018.
 */

public class Dispositivo {
    private String nome;
    private String ip;
    private String tipo;

    public Dispositivo(String ip){
        this.nome = "Genérico";
        this.ip = ip;
        this.tipo = "Genérico";
    }

    public Dispositivo(String nome, String ip, String tipo){
        this.nome = nome;
        this.ip = ip;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
