package miraiscanner.facom.ufu.br.miraiscanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.net.InetAddress;

/**
 * Created by mirandagab on 08/02/2018.
 */

class ScannerWiFi extends AsyncTask<Void, Void, Void>{

    private Context contexto;

    private WeakReference<Context> mContextRef;


    public ScannerWiFi(Context contexto){
        this.contexto = contexto;
        mContextRef = new WeakReference<Context>(contexto);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressDialog carregamento = ProgressDialog.show(contexto, "Por favor aguarde", "Escaneando a rede WiFi");
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            Context context = mContextRef.get();

            if (context != null) {

                ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);


                WifiInfo connectionInfo = wm.getConnectionInfo();
                int ipAddress = connectionInfo.getIpAddress();
                String ipString = Formatter.formatIpAddress(ipAddress);

                //Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                //Log.d(TAG, "ipString: " + String.valueOf(ipString));
                System.out.println("activeNetwork: " + String.valueOf(activeNetwork));
                System.out.println("ipString: " + String.valueOf(ipString));

                String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                //Log.d(TAG, "prefix: " + prefix);
                System.out.println("prefix: " + prefix);

                for (int i = 0; i < 255; i++) {
                    String testIp = prefix + String.valueOf(i);

                    InetAddress address = InetAddress.getByName(testIp);
                    boolean reachable = address.isReachable(1000);
                    String hostName = address.getCanonicalHostName();

                    if (reachable)
                        //Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                        System.out.println("Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                }
            }
        } catch (Throwable t) {
            //Log.e(TAG, "Well that's not good.", t);
            System.out.println("Well that's not good.");
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);
    }



    //TODO
        /*
         COLOCAR AQUI O MÉTODO scan() QUE BUSCA OS IPs
         E NOMES DOS DISPOSITIVOS CONECTADOS NA REDE WIFI
        */

}
