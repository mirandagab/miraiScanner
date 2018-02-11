package miraiscanner.facom.ufu.br.miraiscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by mirandagab on 08/02/2018.
 */

public class ScannerActivity extends AppCompatActivity {

    private EditText listaIPs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        listaIPs = (EditText)findViewById(R.id.stringIPsID);

        fazerEscaneamentoAsync();
    }

    private void fazerEscaneamentoAsync() {
        ScannerWiFi scannerWiFi = new ScannerWiFi(ScannerActivity.this, listaIPs);
        scannerWiFi.execute();
    }
}