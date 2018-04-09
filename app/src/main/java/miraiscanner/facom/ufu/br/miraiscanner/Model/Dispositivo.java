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

    //portas 23 e 48101
    //esta variavel indica se a porta está aberta (true)
    //ou fechada (false)
    private boolean porta23Aberta;
    private boolean porta48101Aberta;

    public Dispositivo(String ip, String mac){
        this.nome = "Genérico";
        this.ip = ip;
        this.mac = mac.toUpperCase();
        this.tipo = "Genérico";
        this.fabricante = "Indisponível";
        this.porta23Aberta = false;
        this.porta48101Aberta = false;
    }

    public Dispositivo(String nome, String ip, String mac, String tipo, String fabricante, boolean porta23Aberta, boolean porta48101Aberta){
        this.nome = nome;
        this.ip = ip;
        this.tipo = tipo;
        this.mac = mac.toUpperCase();
        this.fabricante = fabricante;
        this.porta23Aberta = porta23Aberta;
        this.porta48101Aberta = porta48101Aberta;
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

    public boolean getPorta23Aberta(){ return this.porta23Aberta; }

    public void setPorta23Aberta(boolean porta23Aberta) { this.porta23Aberta = porta23Aberta; }

    public boolean getPorta48101Aberta(){ return this.porta48101Aberta; }

    public void setPorta48101Aberta(boolean porta48101Aberta) { this.porta48101Aberta = porta48101Aberta; }
}
