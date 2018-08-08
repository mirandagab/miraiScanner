package miraiscanner.facom.ufu.br.miraiscanner.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import miraiscanner.facom.ufu.br.miraiscanner.Model.Dispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.Model.DispositivoResponse;
import miraiscanner.facom.ufu.br.miraiscanner.R;

public class DispositivoActivity extends AppCompatActivity {
    private Dispositivo dispositivo;
    private DispositivoResponse dispositivos;
    private String nomeRede;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivo);
        Intent it = this.getIntent();

        if(it.getSerializableExtra("dispositivos") != null) {
            DispositivoResponse dispositivoResponse = (DispositivoResponse) it.getSerializableExtra("dispositivos");
            this.dispositivos = dispositivoResponse;
        }
        if(it.getStringExtra("nome_rede") != null) {
            this.nomeRede = it.getStringExtra("nome_rede");
        }
        if(it.getSerializableExtra("dispositivo") != null) {
            Dispositivo disp = (Dispositivo) it.getSerializableExtra("dispositivo");
            this.dispositivo = disp;
        }
        else{
            dispositivo = new Dispositivo("0.0.0.0", "00:00:00:00:00:00");
        }

        //Painel de informações
        TextView txt_nome = (TextView) findViewById(R.id.txt_nome);
        TextView txt_ip = (TextView) findViewById(R.id.txt_ip);
        TextView txt_fabricante = (TextView) findViewById(R.id.txt_fabricante);
        TextView txt_mac = (TextView) findViewById(R.id.txt_mac);

        txt_nome.setText(dispositivo.getNome());
        txt_ip.setText(dispositivo.getIp());
        txt_fabricante.setText(dispositivo.getFabricante());
        txt_mac.setText(dispositivo.getMac());

        //Painel de ameaças
        TextView txt_23 = (TextView) findViewById(R.id.txt_23);
        TextView txt_48101 = (TextView) findViewById(R.id.txt_48101);

        boolean vulneravel = false;

        if(dispositivo.getPorta23Aberta()){
            txt_23.setText("Aberta");
            txt_23.setTextColor(Color.parseColor("#ffbb33"));
            vulneravel = true;
        }
        else{
            txt_23.setText("Fechada");
        }

        if(dispositivo.getPorta48101Aberta()){
            txt_48101.setText("Aberta");
            txt_48101.setTextColor(Color.parseColor("#ffbb33"));
            vulneravel = true;
        }
        else{
            txt_48101.setText("Fechada");
        }

        TextView txt_ameacas = (TextView) findViewById(R.id.txt_ameacas);
        ImageView img_ameacas = (ImageView) findViewById(R.id.img_ameacas);

        if(vulneravel){
            txt_ameacas.setText("Foram encontradas vulnerabilidades no dispositivo");
            img_ameacas.setImageResource(R.drawable.twotone_warning_24);
        }
        else{
            txt_ameacas.setText("O dispositivo não possui nenhuma vulnerabilidade");
            img_ameacas.setImageResource(R.drawable.twotone_check_circle_24);
            ImageViewCompat.setImageTintList(img_ameacas, ColorStateList.valueOf(ContextCompat.getColor(DispositivoActivity.this, R.color.checked)));
        }

        //Botão voltar da barra superior
        ActionBar ts = getSupportActionBar();
        if(ts != null)
            ts.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(DispositivoActivity.this, MainActivity.class);
            intent.putExtra("dispositivos", this.dispositivos);
            intent.putExtra("nome_rede", this.nomeRede);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
