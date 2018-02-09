package miraiscanner.facom.ufu.br.miraiscanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mirandagab on 08/02/2018.
 */

public class ScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        fazerEscaneamentoAsync();
    }

    private void fazerEscaneamentoAsync() {
        ScannerWiFi scannerWiFi = new ScannerWiFi(ScannerActivity.this);
        scannerWiFi.execute();
    }
}