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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import miraiscanner.facom.ufu.br.miraiscanner.Adapter.AdapterDispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.Model.Dispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.R;

/**
 * Created by mirandagab and MarceloPrado on 08/02/2018.
 */

public class ScannerWiFi extends AsyncTask<Void, Void, String>{

    private Context contexto;

    private WeakReference<Context> mContextRef;

    private static final String TAG = "ScannerWiFi";

    private String ips = "";

    private ArrayAdapter<String> adapterList;

    private AdapterDispositivo adapterDispositivo;

    private List<String> listaIpsString;

    private List<Dispositivo> listaDispositivos;

    private ProgressDialog carregamento;

    private Activity activity;

    private String redeWifi;

    public ScannerWiFi(Context contexto, ArrayAdapter adapterList,
                       AdapterDispositivo adapterDispositivo, Activity activity){
        this.contexto = contexto;
        this.mContextRef = new WeakReference<Context>(contexto);
        this.adapterList = adapterList;
        this.adapterDispositivo = adapterDispositivo;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        carregamento = ProgressDialog.show(contexto, "Por favor aguarde",
                "Escaneando a rede WiFi");
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
                this.redeWifi = connectionInfo.getSSID().substring(1, connectionInfo.getSSID().length() - 1);
                String ipString = Formatter.formatIpAddress(ipAddress);

                Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                Log.d(TAG, "ipString: " + String.valueOf(ipString));
                ips += "activeNetwork: " + String.valueOf(activeNetwork) + "ipString: "
                        + String.valueOf(ipString);

                String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                Log.d(TAG, "prefix: " + prefix);

                ExecutorService threadPool = Executors.newFixedThreadPool(85);

                for (int i = 0; i < 255; i++) {
                    final String prefixo = prefix;
                    final int j = i;
                    threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            String verificandoIP = prefixo + String.valueOf(j);
                            InetAddress enderecoInet = null;
                            try {
                                enderecoInet = InetAddress.getByName(verificandoIP);

                                if(enderecoInet != null) {
                                    boolean reachable = false;

//                                    esta parte do código foi retirada e trocada pelo
//                                    ScannerTCP devido a maior estabilidade nos resultados
//                                    reachable = enderecoInet.isReachable(500);

                                    ScannerTCP escanerTCP = new ScannerTCP(verificandoIP);
                                    reachable = escanerTCP.executarPing();

                                    String hostName = enderecoInet.getCanonicalHostName();
                                    NetworkInterface niMac = NetworkInterface.getByInetAddress(enderecoInet);

                                    if (reachable) {
                                        final String ip = verificandoIP;
                                        String endereco = "";
                                        if(niMac != null) {
                                            byte[] enderecoMAC = niMac.getHardwareAddress();
                                            String mac;
                                            if (enderecoMAC != null) {
                                                for (int i = 0; i < enderecoMAC.length; i++)
                                                    endereco += (String.format("%02X:", enderecoMAC[i]));
                                                endereco = endereco.substring(0, endereco.length() - 1);
                                            }
                                        }
                                        else
                                            endereco = MacAddress.getByIpLinux(verificandoIP);

                                        final String mac = endereco;
                                        final String hname = hostName;

//                                        String fabricante = MacVendorLookup.get(mac);
                                        listaIpsString.add(String.valueOf(ip));
                                        listaDispositivos.add(new Dispositivo(ip, mac));
                                        Log.i(TAG, "Host: " + String.valueOf(hname) +
                                                "(" + String.valueOf(ip) + ") está acessível!");
                                        ips += "Host: " + String.valueOf(hname) +
                                                "(" + String.valueOf(ip) + ") está acessível!";
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Algo deu errado.", e);
                            }
                        }
                    });
                } // Fim do FOR que percorre os 255 IPs

                threadPool.shutdown();
                while (!threadPool.isTerminated()) {
                    //Colocar uma barra de progresso
                }

                //chamar a API de verificar Fabricante por MAC
                //e também verificar as portas 23 e 48101
                for(Dispositivo dispositivo : listaDispositivos){
                    String fabricante = MacVendorLookup.get(dispositivo.getMac());
                    dispositivo.setFabricante(fabricante);
                    ScannerPortas escanerPortas = new ScannerPortas(dispositivo.getIp());
                    if(escanerPortas.porta23EstaAberta()) {
                        dispositivo.setPorta23Aberta("Aberta");
                        System.out.println("Dispositivo [" + dispositivo.getIp() +
                                "] está com a porta 23 aberta.");
                    }else{
                        System.out.println("Dispositivo [" + dispositivo.getIp() +
                                "] está com a porta 23 fechada.");
                    }
                    if(escanerPortas.porta48101EstaAberta()) {
                        dispositivo.setPorta48101Aberta("Aberta");
                        System.out.println("Dispositivo [" + dispositivo.getIp() +
                                "] está com a porta 48101 aberta.");
                    }else{
                        System.out.println("Dispositivo [" + dispositivo.getIp() +
                                "] está com a porta 48101 fechada.");
                    }
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
            adapterList.clear();
            adapterList.addAll(listaIpsString);
            //adapterList.notifyDataSetChanged();

            adapterDispositivo.setDispositivos(listaDispositivos);
            TextView nome_rede = (TextView) activity.findViewById(R.id.rede_wifi);
            if(!this.redeWifi.equals("unknown ssid"))
                nome_rede.setText(this.redeWifi);
            else
                nome_rede.setText("Rede desconhecida");

            TextView qtdDispositivos = (TextView) activity.findViewById(R.id.qtd_dispositivos);
            qtdDispositivos.setText(listaIpsString.size() + "");

            Log.i(TAG, "Setando resultados na tela via Async.");
        }
        else {
            Log.e(TAG, "Erro ao setar o texto na tela via Async.");
        }
        //Tirando o ProgressDialog da tela
        carregamento.dismiss();
    }
}