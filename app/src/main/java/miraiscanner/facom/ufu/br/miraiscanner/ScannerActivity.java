package miraiscanner.facom.ufu.br.miraiscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by mirandagab on 08/02/2018.
 */

public class ScannerActivity extends AppCompatActivity {

    private EditText listaIPs;
    private ListView listaDeIpsConectados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        listaIPs = (EditText)findViewById(R.id.stringIPsID);
        listaDeIpsConectados = (ListView) findViewById(R.id.listaDeIPsConectadosID);

        fazerEscaneamentoAsync();
    }

    private void fazerEscaneamentoAsync() {
        ScannerWiFi scannerWiFi = new ScannerWiFi(ScannerActivity.this, listaIPs, listaDeIpsConectados, this);
        scannerWiFi.execute();
    }
}