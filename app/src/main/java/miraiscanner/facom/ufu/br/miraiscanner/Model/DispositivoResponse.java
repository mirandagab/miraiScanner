package miraiscanner.facom.ufu.br.miraiscanner.Model;

import java.io.Serializable;
import java.util.List;

public class DispositivoResponse implements Serializable{
    private List<Dispositivo> dispositivos;

    public DispositivoResponse(List<Dispositivo> dispositivos){
        this.dispositivos = dispositivos;
    }

    public List<Dispositivo> getDispositivos(){
        return this.dispositivos;
    }

    public int size(){
        return this.dispositivos.size();
    }
}
