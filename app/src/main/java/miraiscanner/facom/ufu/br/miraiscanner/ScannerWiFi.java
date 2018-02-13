package miraiscanner.facom.ufu.br.miraiscanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mirandagab on 08/02/2018.
 */

class ScannerWiFi extends AsyncTask<Void, Void, String>{

    private Context contexto;

    private WeakReference<Context> mContextRef;

    private static final String TAG = "ntask";

    private String ips = "";

    private EditText listaIPs;

    private ListView listaDeIpsConectados;

    private List<String> listaIpsString;

    private ProgressDialog carregamento;

    private Activity atv;

    public ScannerWiFi(Context contexto, EditText listaIPs, ListView listaDeIpsConectados, Activity atv){
        this.contexto = contexto;
        this.mContextRef = new WeakReference<Context>(contexto);
        this.listaIPs = listaIPs;
        this.listaDeIpsConectados = listaDeIpsConectados;
        this.atv = atv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        carregamento = ProgressDialog.show(contexto, "Por favor aguarde", "Escaneando a rede WiFi");
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            listaIpsString = new ArrayList<>();

            Context context = mContextRef.get();

            if (context != null) {

                ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                WifiManager wm = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


                WifiInfo connectionInfo = wm.getConnectionInfo();
                int ipAddress = connectionInfo.getIpAddress();
                String ipString = Formatter.formatIpAddress(ipAddress);

                Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                Log.d(TAG, "ipString: " + String.valueOf(ipString));
                ips += "activeNetwork: " + String.valueOf(activeNetwork) + "\nipString: " + String.valueOf(ipString) + "\n";
                //System.out.println("activeNetwork: " + String.valueOf(activeNetwork));
                //System.out.println("ipString: " + String.valueOf(ipString));

                String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                Log.d(TAG, "prefix: " + prefix);
                //System.out.println("prefix: " + prefix);

                for (int i = 0; i < 255; i++) {
                    String testIp = prefix + String.valueOf(i);

                    InetAddress address = InetAddress.getByName(testIp);
                    boolean reachable = address.isReachable(1000);
                    String hostName = address.getCanonicalHostName();

                    if (reachable) {
                        listaIpsString.add(String.valueOf(testIp));
                        Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                        ips += "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!\n";
                        //System.out.println("Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                    }
                }
            }
        } catch (Throwable t) {
            Log.e(TAG, "Well that's not good.", t);
            //System.out.println("Well that's not good.");
        }

        return ips;
    }

    @Override
    protected void onPostExecute(String textoExibicao) {
        super.onPostExecute(textoExibicao);

        if(textoExibicao != "" && textoExibicao != null) {
            listaIPs.setText(ips);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.atv,
                    android.R.layout.simple_list_item_1, listaIpsString);

            listaDeIpsConectados.setAdapter(adapter);
            Log.i(TAG, "Setando resultados na tela via Async.");
        }
        else {
            Log.i(TAG, "Erro ao setar o texto na tela via Async.");
        }
        //Tirando o ProgressDialog da tela
        carregamento.dismiss();
    }

}
