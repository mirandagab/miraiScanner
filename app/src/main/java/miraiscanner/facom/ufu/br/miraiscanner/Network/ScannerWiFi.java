package miraiscanner.facom.ufu.br.miraiscanner.Network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ArrayAdapter;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import miraiscanner.facom.ufu.br.miraiscanner.Adapter.AdapterDispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.Model.Dispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.Network.MacAddress;

/**
 * Created by mirandagab and MarceloPrado on 08/02/2018.
 */

public class ScannerWiFi extends AsyncTask<Void, Void, String>{

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

                ExecutorService executor = Executors.newFixedThreadPool(85);

                for (int i = 0; i < 255; i++) {
                    final String prefixo = prefix;
                    final int j = i;
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            String testIp = prefixo + String.valueOf(j);
                            InetAddress address = null;
                            try {
                                address = InetAddress.getByName(testIp);

                                if(address != null) {
                                    boolean reachable = false;
                                    reachable = address.isReachable(500);
                                    String hostName = address.getCanonicalHostName();
                                    NetworkInterface niMac = NetworkInterface.getByInetAddress(address);

                                    if (reachable) {
                                        final String ip = testIp;
                                        String endereco = "";
                                        if(niMac != null) {
                                            byte[] endMac = niMac.getHardwareAddress();
                                            String mac;
                                            if (endMac != null) {
                                                for (int i = 0; i < endMac.length; i++)
                                                    endereco += (String.format("%02X:", endMac[i]));
                                                endereco = endereco.substring(0, endereco.length() - 1);
                                            }
                                        }
                                        else
                                            endereco = MacAddress.getByIpLinux(testIp);

                                        final String mac = endereco;
                                        final String hname = hostName;

                                        String fabricante = MacVendorLookup.get(mac);
                                        listaIpsString.add(String.valueOf(ip));
                                        listaDispositivos.add(new Dispositivo("Genérico", ip, mac, "Genérico", fabricante));
                                        Log.i(TAG, "Host: " + String.valueOf(hname) + "(" + String.valueOf(ip) + ") está acessível!");
                                        ips += "Host: " + String.valueOf(hname) + "(" + String.valueOf(ip) + ") está acessível!";


                                        //Corrigir essa parte do código
//                                        try {
//                                            TimeLimitedCodeBlock.runWithTimeout(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    //As vezes retorna o resultado, as vezes não... Obrigar a esperar o retorno ou dar um certo timeout
//                                                    String fabricante = MacVendorLookup.get(mac);
//                                                    listaIpsString.add(String.valueOf(ip));
//                                                    listaDispositivos.add(new Dispositivo("Genérico", ip, mac, "Genérico", fabricante));
//                                                    Log.i(TAG, "Host: " + String.valueOf(hname) + "(" + String.valueOf(ip) + ") está acessível!");
//                                                    ips += "Host: " + String.valueOf(hname) + "(" + String.valueOf(ip) + ") está acessível!";
//                                                }
//                                            }, 5, TimeUnit.SECONDS);
//                                        }
//                                        catch (TimeoutException e) {
//                                            System.out.println("Erro na chamada do fabricante");
//                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Algo deu errado.", e);
                            }
                        }
                    });
                }

                executor.shutdown();
                while (!executor.isTerminated()) {
                    //Colocar uma barra de progresso
                }
                System.out.println("Escaneamento da rede WiFi concluído!");
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
