package miraiscanner.facom.ufu.br.miraiscanner.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import miraiscanner.facom.ufu.br.miraiscanner.Adapter.AdapterDispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.Model.Dispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.Model.DispositivoResponse;
import miraiscanner.facom.ufu.br.miraiscanner.R;
import miraiscanner.facom.ufu.br.miraiscanner.Network.ScannerWiFi;

/**
 * Created by mirandagab and MarceloPrado on 08/02/2018.
 */

public class MainActivity extends AppCompatActivity {

    //private Button botaoScan;
    private android.support.v7.widget.Toolbar toolbar;
    private ListView listaDeIpsConectados;
    private AdapterDispositivo adapterDispositivo;
    private TextView textoProgresso;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //criação da progressBar
        progressBar = (ProgressBar) findViewById(R.id.barraProgresso);
        textoProgresso = (TextView) findViewById(R.id.textoProgresso);

        //criação da toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //criação da listview
        listaDeIpsConectados = (ListView) findViewById(R.id.listaDeIPsConectadosID);

        //seta a ação de clique no item
        listaDeIpsConectados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(MainActivity.this, DispositivoActivity.class);

                TextView nomeRede = (TextView) findViewById(R.id.rede_wifi);
                DispositivoResponse dispositivoResponse = new DispositivoResponse(adapterDispositivo.getDispositivos());
                Dispositivo disp = (Dispositivo) parent.getItemAtPosition(position);

                intent.putExtra("dispositivos", dispositivoResponse);
                intent.putExtra("nome_rede", nomeRede.getText());
                intent.putExtra("dispositivo", disp);
                startActivity(intent);
            }
        });
        adapterDispositivo = new AdapterDispositivo(new ArrayList<Dispositivo>(), MainActivity.this);
        Intent it = this.getIntent();

        if(it.getSerializableExtra("dispositivos") != null) {
            DispositivoResponse dispositivoResponse = (DispositivoResponse) it.getSerializableExtra("dispositivos");
            adapterDispositivo.setDispositivos(dispositivoResponse.getDispositivos());

            TextView nome_rede = (TextView) this.findViewById(R.id.rede_wifi);
            if(it.getStringExtra("nome_rede") != null)
                nome_rede.setText(it.getStringExtra("nome_rede"));
            else
                nome_rede.setText("Rede desconhecida");

            TextView qtdDispositivos = (TextView) this.findViewById(R.id.qtd_dispositivos);
            qtdDispositivos.setText(dispositivoResponse.size() + "");

            progressBar.setVisibility(ProgressBar.GONE);
            textoProgresso.setText("Rede escaneada");
            textoProgresso.setVisibility(TextView.GONE);
        }

        listaDeIpsConectados.setAdapter(adapterDispositivo);

        if(adapterDispositivo.isEmpty())
            fazerEscaneamentoAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_sair:
                finish();
                System.exit(1);
                return true;
            case R.id.item_configuracoes:
                return true;
            case R.id.item_atualizar:
                fazerEscaneamentoAsync();
                return true;
            case R.id.item_sobre:
                Intent intent = new Intent(MainActivity.this, SobreActivity.class);
                TextView nomeRede = (TextView) this.findViewById(R.id.rede_wifi);

                DispositivoResponse dispositivoResponse = new DispositivoResponse(this.adapterDispositivo.getDispositivos());
                intent.putExtra("dispositivos", dispositivoResponse);
                intent.putExtra("nome_rede", nomeRede.getText());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fazerEscaneamentoAsync() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo tipoRede = cm.getActiveNetworkInfo();
        if(tipoRede.getType() == ConnectivityManager.TYPE_WIFI) {
            ScannerWiFi scannerWiFi = new ScannerWiFi(MainActivity.this, adapterDispositivo,
                    this, progressBar, textoProgresso);
            scannerWiFi.execute();
        }else{
            Toast.makeText(this, "Sem conexão WiFi", Toast.LENGTH_LONG).show();
        }

    }
}
