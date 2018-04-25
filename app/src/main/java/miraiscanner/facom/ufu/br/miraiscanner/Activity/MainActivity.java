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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import miraiscanner.facom.ufu.br.miraiscanner.Adapter.AdapterDispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.Model.Dispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.R;
import miraiscanner.facom.ufu.br.miraiscanner.Network.ScannerWiFi;

/**
 * Created by mirandagab and MarceloPrado on 08/02/2018.
 */

public class MainActivity extends AppCompatActivity {

    //private Button botaoScan;
    private android.support.v7.widget.Toolbar toolbar;
    private ListView listaDeIpsConectados;
    private ArrayAdapter<String> adapter;
    private AdapterDispositivo adapterTeste;
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

        listaDeIpsConectados = (ListView) findViewById(R.id.listaDeIPsConectadosID);
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        adapterTeste = new AdapterDispositivo(new ArrayList<Dispositivo>(), MainActivity.this);
        listaDeIpsConectados.setAdapter(adapterTeste);
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
                startActivity(new Intent(MainActivity.this, SobreActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fazerEscaneamentoAsync() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo tipoRede = cm.getActiveNetworkInfo();
        if(tipoRede.getType() == ConnectivityManager.TYPE_WIFI) {
            ScannerWiFi scannerWiFi = new ScannerWiFi(MainActivity.this, adapter, adapterTeste,
                    this, progressBar, textoProgresso);
            scannerWiFi.execute();
        }else{
            Toast.makeText(this, "Sem conexão WiFi", Toast.LENGTH_LONG).show();
        }

    }
}
