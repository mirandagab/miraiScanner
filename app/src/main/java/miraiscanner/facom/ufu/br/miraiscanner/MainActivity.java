package miraiscanner.facom.ufu.br.miraiscanner;

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

        botaoScan = (Button) findViewById(R.id.botaoScanID);

        botaoScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TO DO
                // colocar o método para escanear a rede Wi-Fi
                // chamar próxima tela com informações e ações disponíveis
            }
        });
    }
}
