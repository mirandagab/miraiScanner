package miraiscanner.facom.ufu.br.miraiscanner.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import miraiscanner.facom.ufu.br.miraiscanner.Model.Dispositivo;
import miraiscanner.facom.ufu.br.miraiscanner.R;

/**
 * Created by MarceloPrado on 12/03/2018.
 */

public class AdapterDispositivo extends BaseAdapter {
    private List<Dispositivo> dispositivos;
    private Activity atv;

    public AdapterDispositivo(List<Dispositivo> dispositivos, Activity atv){
        this.dispositivos = dispositivos;
        this.atv = atv;
    }

    @Override
    public int getCount() {
        return dispositivos.size();
    }

    @Override
    public Object getItem(int position) {
        return dispositivos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = atv.getLayoutInflater().inflate(R.layout.lista_dispositivos, parent, false);
        Dispositivo dispositivo = dispositivos.get(position);

        TextView nome = (TextView)
                view.findViewById(R.id.lista_dispositivo_nome);
        TextView ip = (TextView)
                view.findViewById(R.id.lista_dispositivo_descricao);
        ImageView imagem = (ImageView)
                view.findViewById(R.id.lista_dispositivo_imagem);

        //populando as Views
        nome.setText(dispositivo.getNome());
        ip.setText(dispositivo.getIp());
        imagem.setImageResource(R.mipmap.icone_app);

        return view;
    }

    public void setDispositivos(List<Dispositivo> dispositivos){
        this.dispositivos.clear();
        this.dispositivos.addAll(dispositivos);
        this.notifyDataSetChanged();
    }
}
