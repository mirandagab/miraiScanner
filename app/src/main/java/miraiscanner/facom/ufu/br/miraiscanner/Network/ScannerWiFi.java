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
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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

    private AdapterDispositivo adapterDispositivo;

    private List<String> listaIpsString;

    private List<Dispositivo> listaDispositivos;

    private Activity activity;

    private String redeWifi;

    private ProgressBar progressBar;

    private TextView textoProgresso;

    private static int PROGRESSO = 39;

    private double total, totalAux = 0;

    public ScannerWiFi(Context contexto, AdapterDispositivo adapterDispositivo, Activity activity,
                       ProgressBar progressBar, TextView textoProgresso){
        this.contexto = contexto;
        this.mContextRef = new WeakReference<Context>(contexto);
        this.adapterDispositivo = adapterDispositivo;
        this.activity = activity;
        this.textoProgresso = textoProgresso;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        total = 0;
        progressBar.setMax(10000);
        progressBar.setProgress(0);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        textoProgresso.setGravity(Gravity.CENTER_HORIZONTAL);
        textoProgresso.setVisibility(TextView.VISIBLE);
        textoProgresso.setText("0%");
        super.onPreExecute();
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

                                    ScannerTCP scannerTCP = new ScannerTCP(verificandoIP);
                                    reachable = scannerTCP.executarPing();

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
                            publishProgress();
                        }
                    });
                } // Fim do FOR que percorre os 255 IPs

                threadPool.shutdown();
                while (!threadPool.isTerminated()) {
                    //Espera o término da pool antes de continuar
                }

                //chamar a API de verificar Fabricante por MAC
                //e também verificar as portas 23 e 48101
                for(Dispositivo dispositivo : listaDispositivos){
                    String fabricante = MacVendorLookup.get(dispositivo.getMac());
                    dispositivo.setFabricante(fabricante);
                    ScannerPortas scannerPortas = new ScannerPortas(dispositivo);
                    scannerPortas.statusPorta23();
                    scannerPortas.statusPorta48101();
                }

                System.out.println("Escaneamento da rede WiFi concluído!");
            }
        } catch (Throwable t) {
            Log.e(TAG, "Algo deu errado.", t);
        }

        return ips;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        total += (PROGRESSO / 100d);
        totalAux = Math.ceil(total);
        progressBar.incrementProgressBy(PROGRESSO);
        String texto = String.valueOf(totalAux);
        texto += " %";
        textoProgresso.setText(texto);
    }

    //Retorna resultados para a thread de exibição ao usuário
    //e seta os valores dos IPs encontrados em um ListView, com
    //um IP por linha. Também fecha o ProgressDialog
    @Override
    protected void onPostExecute(String textoExibicao) {
        super.onPostExecute(textoExibicao);

        if(textoExibicao != "" && textoExibicao != null) {
            adapterDispositivo.setDispositivos(listaDispositivos);
            TextView nome_rede = (TextView) activity.findViewById(R.id.rede_wifi);
            if(!this.redeWifi.equals("unknown ssid"))
                nome_rede.setText(this.redeWifi);
            else
                nome_rede.setText("Rede desconhecida");

            TextView qtdDispositivos = (TextView) activity.findViewById(R.id.qtd_dispositivos);
            qtdDispositivos.setText(listaDispositivos.size() + "");

            Log.i(TAG, "Setando resultados na tela via Async.");
        }
        else {
            Log.e(TAG, "Erro ao setar o texto na tela via Async.");
        }

        progressBar.setVisibility(ProgressBar.GONE);
        textoProgresso.setText("Rede escaneada");
        textoProgresso.setVisibility(TextView.GONE);

        //Enviando notificação teste avisando sobre a quantidade de dispositivos
        /*FirebaseMessaging fm = FirebaseMessaging.getInstance();
        AtomicInteger msgID = new AtomicInteger();
        fm.send(new RemoteMessage.Builder("notificacaomirai" + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgID.incrementAndGet()))
                .addData("my_message", "Hello World")
                .addData("my_action","SAY_HELLO")
                .build());
        */
        //Tirando o ProgressDialog da tela
        //carregamento.dismiss();
    }
}