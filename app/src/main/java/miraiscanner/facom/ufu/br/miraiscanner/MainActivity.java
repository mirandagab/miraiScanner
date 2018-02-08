package miraiscanner.facom.ufu.br.miraiscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button botaoScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //criação da variável de botão para escaneamento
        botaoScan = (Button) findViewById(R.id.botaoScanID);

        //muda para a activity de escaneamento da rede Wi-Fi ao clicar no botão SCAN
        botaoScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ScannerActivity.class));
            }
        });
    }
}
