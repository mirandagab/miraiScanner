package miraiscanner.facom.ufu.br.miraiscanner.Network;

/**
 * Created by mirandagab and MarceloPrado on 02/04/2018.
 *
 * créditos: www.macvendors.com
 */

// TESTAR COM http://www.macvendorlookup.com/api/v2/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MacVendorLookup {
    /** URL Base para API dos fornecedores: www.macvendors.com */
    private static final String baseURL = "https://api.macvendors.com/";

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
            // Fabricante não encontrado pelo MAC
            return "Desconhecido";
        } catch (IOException e) {
            // Qualquer erro durante a busca
            return "Erro durante a busca";
        }
    }
}