package miraiscanner.facom.ufu.br.miraiscanner.Activity;

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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import miraiscanner.facom.ufu.br.miraiscanner.Adapter.AdapterDispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.Model.Dispositivo;

/**
 * Created by mirandagab on 08/02/2018.
 */

class ScannerWiFi extends AsyncTask<Void, Void, String>{

    private Context contexto;

    private WeakReference<Context> mContextRef;

    private static final String TAG = "ScannerWiFi";

    private String ips = "";

    private ArrayAdapter<String> adapter;

    private AdapterDispositivo adapterTeste;

    private List<String> listaIpsString;

    private List<Dispositivo> listaDispositivos;

    private ProgressDialog carregamento;

    private Activity atv;

    public ScannerWiFi(Context contexto, ArrayAdapter adapter, AdapterDispositivo adapterTeste, Activity atv){
        this.contexto = contexto;
        this.mContextRef = new WeakReference<Context>(contexto);
        this.adapter = adapter;
        this.adapterTeste = adapterTeste;
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
            listaDispositivos = new ArrayList<>();

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
                ips += "activeNetwork: " + String.valueOf(activeNetwork) + "ipString: " + String.valueOf(ipString);

                String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                Log.d(TAG, "prefix: " + prefix);

                for (int i = 0; i < 255; i++) {
                    final String prefixo = prefix;
                    final int j = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String testIp = prefixo + String.valueOf(j);

                            InetAddress address = null;
                            try {
                                address = InetAddress.getByName(testIp);

                                if(address != null) {
                                    boolean reachable = false;
                                    reachable = address.isReachable(1000);

                                    String hostName = address.getCanonicalHostName();

                                    if (reachable) {
                                        listaIpsString.add(String.valueOf(testIp));
                                        listaDispositivos.add(new Dispositivo(testIp));
                                        Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") está acessível!");
                                        ips += "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") está acessível!";
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Algo deu errado.", e);
                            }
                        }
                    }).start();
                }
            }
        } catch (Throwable t) {
            Log.e(TAG, "Algo deu errado.", t);
        }

        return ips;
    }

    //Retorna resultados para a thread de exibição ao usuário
    //e seta os valores dos IPs encontrados em um ListView, com
    //um IP por linha. Também fecha o ProgressDialog
    @Override
    protected void onPostExecute(String textoExibicao) {
        super.onPostExecute(textoExibicao);

        if(textoExibicao != "" && textoExibicao != null) {
            adapter.clear();
            adapter.addAll(listaIpsString);
            //adapter.notifyDataSetChanged();

            adapterTeste.setDispositivos(listaDispositivos);

            Log.i(TAG, "Setando resultados na tela via Async.");
        }
        else {
            Log.e(TAG, "Erro ao setar o texto na tela via Async.");
        }
        //Tirando o ProgressDialog da tela
        carregamento.dismiss();
    }


}
