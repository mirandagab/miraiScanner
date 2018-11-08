package miraiscanner.facom.ufu.br.miraiscanner.Network;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import miraiscanner.facom.ufu.br.miraiscanner.Model.Dispositivo;

/**
 * Created by mirandagab and MarceloPrado on 09/04/2018.
 */
public class ScannerPortas {
    private Dispositivo dispositivo;

    public ScannerPortas(Dispositivo dispositivo){
        this.dispositivo = dispositivo;
    }

    public boolean statusPorta23() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(this.dispositivo.getIp(), 23), 500);
            socket.close();
            dispositivo.setPorta23Aberta(true);
            System.out.println("[ConnectException]Dispositivo [" + dispositivo.getIp() +
                    "] está com a porta 23 aberta.");
            return true;
        } catch (ConnectException c) {
            System.out.println("[porta23] Não foi possível conectar. Erro: " + c);
            dispositivo.setPorta23Aberta(false);
            System.out.println("[ConnectException] Dispositivo [" + dispositivo.getIp() +
                    "] está com a porta 23 fechada.");
            return false;
        } catch (SocketTimeoutException t) {
            System.out.println("[porta23] Não foi possível conectar. Erro: " + t);
            dispositivo.setPorta23Aberta(false);
            System.out.println("[SocketTimeoutException] Dispositivo [" + dispositivo.getIp() +
                    "] está com a porta 23 fechada.");
            return false;
        } catch (IOException i) {
            System.out.println("[porta23] Não foi possível conectar. Erro: " + i);
            dispositivo.setPorta23Aberta(false);
            System.out.println("[IOException] Dispositivo [" + dispositivo.getIp() +
                    "] está com a porta 23 fechada.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[porta23] Erro: " + e);
            dispositivo.setPorta23Aberta(false);
            System.out.println("[Exception]Dispositivo [" + dispositivo.getIp() +
                    "] está com a porta 23 fechada.");
            return false;
        }
    }

    public boolean statusPorta48101() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(this.dispositivo.getIp(), 48101), 500);
            socket.close();
            dispositivo.setPorta48101Aberta(true);
            System.out.println("Dispositivo [" + dispositivo.getIp() +
                    "] está com a porta 48101 aberta.");
            return true;
        } catch(ConnectException c){
            System.out.println("[porta48101] Não foi possível conectar. Erro: " + c);
            dispositivo.setPorta48101Aberta(false);
            System.out.println("[ConnectException]Dispositivo [" + dispositivo.getIp() +
                    "] está com a porta 48101 fechada.");
            return false;
        } catch (SocketTimeoutException t) {
            System.out.println("[porta48101] Não foi possível conectar. Erro: " + t);
            dispositivo.setPorta48101Aberta(false);
            System.out.println("[SocketTimeoutException] Dispositivo [" + dispositivo.getIp() +
                    "] está com a porta 48101 fechada.");
            return false;
        } catch (IOException i) {
            System.out.println("[porta48101] Não foi possível conectar. Erro: " + i);
            dispositivo.setPorta48101Aberta(false);
            System.out.println("[IOException] Dispositivo [" + dispositivo.getIp() +
                    "] está com a porta 48101 fechada.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[porta48101] Erro: " + e);
            dispositivo.setPorta48101Aberta(false);
            System.out.println("[Exception]Dispositivo [" + dispositivo.getIp() +
                    "] está com a porta 48101 fechada.");
            return false;
        }
    }

    public Dispositivo getDispositivo() { return dispositivo; }

    public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }
}
