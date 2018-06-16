package miraiscanner.facom.ufu.br.miraiscanner.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

public class ScannerInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token da App", token);

        try {
            String auth2 = ServidorFCM.getAccessToken();

            if(auth2 != null && !auth2.isEmpty()){
                System.out.println(auth2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
