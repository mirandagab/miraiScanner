package miraiscanner.facom.ufu.br.miraiscanner.Network;

/**
 * Created by MarceloPrado on 02/04/2018.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MacVendorLookup {
    /** URL Base para API dos fornecedores: www.macvendors.com */
    private static final String baseURL = "https://api.macvendors.com/";
    /*
    /** Recebe os endereços MAC passados como argumento.
     * @param args endereço MAC
    public static void main(String[] args) {
        for (String arguments : args)
            System.out.println(arguments + ": " + get(arguments));
    }*/

    /** Realiza a busca pelo fornecedor de acordo com o endereço MAC.
     * @param macAddress endereço MAC.
     * @return Dados do fabricante do endereço MAC. */
    public static String get(String macAddress) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(baseURL + macAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            conn.disconnect();
            return result.toString();
        } catch (FileNotFoundException e) {
            // MAC não encontrado
            return "N/A";
        } catch (IOException e) {
            // Qualquer erro durante a busca
            return "N/A";
        }
    }
}