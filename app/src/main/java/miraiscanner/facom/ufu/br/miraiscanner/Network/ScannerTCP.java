package miraiscanner.facom.ufu.br.miraiscanner.Network;

import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * Created by mirandagab and MarceloPrado on 09/04/2018.
 */

public class ScannerTCP {
    Runtime runtime;

    private String ip;

    public ScannerTCP(String ip){
        this.ip = ip;
        this.runtime = Runtime.getRuntime();
    }

    private String getIP(){
        return this.ip;
    }

    private void setIP(String ip){
        this.ip = ip;
    }

    public boolean executarPing(){
        try{
            Process processo = runtime.exec("/system/bin/ping -c 1 " + ip);
            int valorDeSaida = processo.waitFor();

            System.out.println("Valor de saída: " + valorDeSaida);

            return (valorDeSaida == 0);

        } catch (IOException e){
            e.printStackTrace();
            System.out.println("[ScannerTCP] executarPing: " + e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("[ScannerTCP] Interrução: " + e);
        }

        return false;
    }
}
