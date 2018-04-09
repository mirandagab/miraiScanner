package miraiscanner.facom.ufu.br.miraiscanner.Network;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by mirandagab and MarceloPrado on 09/04/2018.
 */
public class ScannerPortas {
    private String endereco;

    public ScannerPortas(String ip){
        this.endereco = ip;
    }

    public boolean porta23EstaAberta() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(this.endereco, 23), 500);
            socket.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[porta23] Erro: " + e);
            return false;
        }
    }

    public boolean porta48101EstaAberta() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(this.endereco, 48101), 500);
            socket.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[porta48101] Erro: " + e);
            return false;
        }
    }

    public String getEndereco() { return endereco; }

    public void setEndereco(String endereco) { this.endereco = endereco; }
}
