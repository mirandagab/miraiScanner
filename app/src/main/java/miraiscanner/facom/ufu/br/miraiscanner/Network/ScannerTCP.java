package miraiscanner.facom.ufu.br.miraiscanner.Network;

import java.io.IOException;

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

    //método para executar o ping TCP via runtime
    //quando a varíavel valorDeSaida == 0, significa que o ping foi bem sucedido (true)
    //caso contrário, significa que o ping não foi bem sucedido (false)
    public boolean executarPing(){
        try{
            Process processo = runtime.exec("/system/bin/ping -c 1 " + ip);
            int valorDeSaida = processo.waitFor();

            System.out.println("Valor de saída: " + valorDeSaida);

            processo.getInputStream().close();
            processo.getOutputStream().close();
            processo.getErrorStream().close();

            return (valorDeSaida == 0);

        } catch (IOException e){
            e.printStackTrace();
            System.out.println("[ScannerTCP] Exceção de IO: " + e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("[ScannerTCP] Interrução: " + e);
        }
        return false;
    }

    private String getIP(){ return this.ip; }

    private void setIP(String ip){ this.ip = ip; }
}
