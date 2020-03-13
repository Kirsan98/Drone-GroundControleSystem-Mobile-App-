package fr.b.dronegcs_m;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;





public class thread2 extends Thread{

    private Socket socket;
    private InetSocketAddress addr;
    private String ipServeur;
    private int portServeur;
    private DataInputStream in;
    DataOutputStream out;



    public void run() {
        try {
            Socket gcs = new Socket("2001:660:6101:800:103::5", 7777);
            DataOutputStream gcs_out = new DataOutputStream(gcs.getOutputStream());
            DataInputStream gcs_in = new DataInputStream(gcs.getInputStream());

            if (gcs.isConnected()){
                Log.d("TAG", "SOCKET CONNECTE!");
                gcs_out.writeChars("GCS");
            }
            else{
                Log.d("TAG", "SOCKET NON CONNECTE!");
            }

            if (gcs != null){
                gcs.close();
            }
        } catch (IOException e) {
            Log.d("TAG", "ERREUR SOCKET : "+e.getMessage());
        }


    }

}
