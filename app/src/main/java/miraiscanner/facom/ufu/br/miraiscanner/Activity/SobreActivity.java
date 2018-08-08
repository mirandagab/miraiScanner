package miraiscanner.facom.ufu.br.miraiscanner.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import miraiscanner.facom.ufu.br.miraiscanner.Model.DispositivoResponse;
import miraiscanner.facom.ufu.br.miraiscanner.R;

/**
 * Created by mirandagab and MarceloPrado on 11/03/2018.
 */

public class SobreActivity extends AppCompatActivity {
    private DispositivoResponse dispositivos;
    private String nomeRede;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent it = this.getIntent();

        if(it.getSerializableExtra("dispositivos") != null) {
            DispositivoResponse disp = (DispositivoResponse) it.getSerializableExtra("dispositivos");
            this.dispositivos = disp;
        }
        if(it.getStringExtra("nome_rede") != null) {
            this.nomeRede = it.getStringExtra("nome_rede");
        }
        setContentView(R.layout.activity_sobre);
        ActionBar ts = getSupportActionBar();
        if(ts != null)
            ts.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(SobreActivity.this, MainActivity.class);
            intent.putExtra("dispositivos", this.dispositivos);
            intent.putExtra("nome_rede", this.nomeRede);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
