package miraiscanner.facom.ufu.br.miraiscanner.Model;

/**
 * Created by mirandagab and MarceloPrado on 12/03/2018.
 */

public class Dispositivo {
    private String nome;
    private String ip;
    private String mac;
    private String tipo;
    private String fabricante;

    public Dispositivo(String ip, String mac){
        this.nome = "Genérico";
        this.ip = ip;
        this.mac = mac.toUpperCase();
        this.tipo = "Genérico";
    }

    public Dispositivo(String nome, String ip, String mac, String tipo, String fabricante){
        this.nome = nome;
        this.ip = ip;
        this.tipo = tipo;
        this.mac = mac.toUpperCase();
        this.fabricante = fabricante;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIp() { return ip; }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() { return mac; }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
}
