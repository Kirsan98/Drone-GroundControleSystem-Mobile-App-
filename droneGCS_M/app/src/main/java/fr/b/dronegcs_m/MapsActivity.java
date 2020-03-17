package fr.b.dronegcs_m;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnTouchListener,JoystickView.JoystickListener {

    private GoogleMap mMap;
    private Button flyTo, takeOff, landing;
    private JoystickView leftJoystick, rightJoystick;
    private TextView x, y, z;
    private final String HOST = ""; //petitbonum
    private Socket socket;
    private DataOutputStream output;
    private Instruction lastInstruction;
    private int nbMessages = 0;
    private thread2 monThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        JoystickView joystick = new JoystickView(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        monThread = new thread2();
        monThread.start();
        linkViewObjects();
        /*
        try {
            connection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
    private void connection() throws IOException {
        socket= new Socket(HOST, 7778);
        output = new DataOutputStream((socket.getOutputStream()));
        DataInputStream input = new DataInputStream((socket.getInputStream()));
        output.writeBytes("GCS");
    }


    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        switch (id) {
            case R.id.joystickLeft:
                Log.d("Left Joystick", "X percent: " + xPercent + " Y percent: " + yPercent);
                break;
            case R.id.joystickRight:
                Log.d("Right Joystick", "X percent: " + xPercent + "Y percent: " + yPercent);
                break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void linkViewObjects() {
        // Position
        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        z = findViewById(R.id.z);

        flyTo = findViewById(R.id.autopilote);
        flyTo.setOnTouchListener(this);
        takeOff = findViewById(R.id.take_off);
        landing = findViewById(R.id.landing);
        leftJoystick = findViewById(R.id.joystickLeft);
        rightJoystick = findViewById(R.id.joystickRight);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng homePosition = new LatLng(44.804, -0.606);
        LatLng dronePosition = new LatLng(44.8037, -0.6057);
        LatLng flyAreaTopLeft = new LatLng(44.8045, -0.6065);
        LatLng flyAreaTopRight = new LatLng(44.8045, -0.6055);
        LatLng flyAreaBotLeft = new LatLng(44.8035, -0.6065);
        LatLng flyAreaBotRight = new LatLng(44.8035, -0.6055);



        MarkerOptions moHOME = new MarkerOptions();
        moHOME.position(homePosition).title("Home");
        mMap.addMarker(moHOME);

        MarkerOptions moDRONE = new MarkerOptions();
        moDRONE.position(dronePosition).title("Drone");
        moDRONE.icon(BitmapDescriptorFactory.fromResource(R.drawable.drone));
        mMap.addMarker(moDRONE);

        PolygonOptions rectOptions = new PolygonOptions();
        rectOptions.fillColor(Color.argb(80,0,200,100));
        rectOptions.strokeWidth(0);
        rectOptions.add(flyAreaTopLeft);
        rectOptions.add(flyAreaTopRight);
        rectOptions.add(flyAreaBotRight);
        rectOptions.add(flyAreaBotLeft);

        googleMap.addPolygon(rectOptions);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(homePosition));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(homePosition, 18.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {

            String message ="";
            switch (v.getId()) {
                case R.id.autopilote:
                    if (x.getText()!=null && y.getText()!=null && z.getText()!=null && !x.getText().toString().isEmpty() && !y.getText().toString().isEmpty() && !z.getText().toString().isEmpty()){

                        int valX = Integer.parseInt(x.getText().toString());
                        int valY = Integer.parseInt(y.getText().toString());
                        int valZ = Integer.parseInt(z.getText().toString());

                        lastInstruction = new Instruction(valX,valY,valZ,false,false,false,false, true);
                    }
                    break;
            }

            if (lastInstruction!=null)
                Toast.makeText(this,lastInstruction.getJSON().toString(), Toast.LENGTH_LONG).show();

            if (!message.isEmpty() && output!=null){
                message = lastInstruction.getJSON().toString();
                output.writeBytes(message);
                Toast.makeText(this,"Coordonnées envoyées", Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(this,"connexion impossible", Toast.LENGTH_SHORT).show();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
