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
//        TextView fabricante = (TextView)
//                view.findViewById(R.id.lista_dispositivo_fabricante);
        TextView mac = (TextView)
                view.findViewById(R.id.lista_dispositivo_mac);
        ImageView imagem = (ImageView)
                view.findViewById(R.id.lista_dispositivo_imagem);

        //populando as Views
        nome.setText(dispositivo.getNome());
        ip.setText(dispositivo.getIp());
//        fabricante.setText(dispositivo.getFabricante());
        mac.setText(dispositivo.getMac());

        switch(dispositivo.getTipo()){
            case "Celular":
                imagem.setImageResource(R.drawable.disp_celular);
                break;
            case "Computador":
                imagem.setImageResource(R.drawable.disp_computador);
                break;
            case "Roteador":
                imagem.setImageResource(R.drawable.disp_roteador);
                break;
            case "IoT":
                imagem.setImageResource(R.drawable.disp_camera);
                break;
            default:
                imagem.setImageResource(R.drawable.disp_generico);
                break;
        }

        return view;
    }

    public void setDispositivos(List<Dispositivo> dispositivos){
        this.dispositivos.clear();
        this.dispositivos.addAll(dispositivos);
        this.notifyDataSetChanged();
    }
}
