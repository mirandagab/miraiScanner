package miraiscanner.facom.ufu.br.miraiscanner.Network;

/**
 * Created by mirandagab and MarceloPrado on 03/04/2018.
 *
 * créditos: evercam (https://github.com/evercam)
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacAddress {

    /**
     * Read hardware address from ARP table by specified IP address(work for
     * Android only). If no MAC address associated, return 00:00:00:00:00:00.
     *
     * @param ip
     *            host IP address
     */
    public static String getByIpLinux(String ip) {
        final String MAC_RE = "^%s\\s+0x1\\s+0x2\\s+([:0-9a-fA-F]+)\\s+\\*\\s+\\w+$";
        final int BUF = 8 * 1024;
        String hw = "00:00:00:00:00:00";
        try {
            if (ip != null) {
                String ptrn = String.format(MAC_RE, ip.replace(".", "\\."));
                Pattern pattern = Pattern.compile(ptrn);
                BufferedReader bufferedReader = new BufferedReader(
                        new FileReader("/proc/net/arp"), BUF);
                String line;

                Matcher matcher;
                while ((line = bufferedReader.readLine()) != null) {
                    matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        hw = matcher.group(1);
                        break;
                    }
                }
                bufferedReader.close();
            }
        } catch (IOException e) {
            return hw;
        }
        return hw;
    }
}