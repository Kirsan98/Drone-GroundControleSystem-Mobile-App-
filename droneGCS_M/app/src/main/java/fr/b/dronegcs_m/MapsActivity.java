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
    private float xSpeed=0;
    private float ySpeed=0;
    private float zSpeed=0;
    private float xDrone =0;
    private float yDrone =0;
    private final LatLng flyAreaTopLeft = new LatLng(44.8045, -0.6065);
    private final LatLng flyAreaTopRight = new LatLng(44.8045, -0.6055);
    private final LatLng flyAreaBotLeft = new LatLng(44.8035, -0.6065);
    private final LatLng flyAreaBotRight = new LatLng(44.8035, -0.6055);
    private final double DRONE_X_MAX = 100;
    private final double DRONE_X_MIN = -100;
    private final double DRONE_Y_MAX = 100;
    private final double DRONE_Y_MIN = -100;
    private MarkerOptions moDRONE;

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
        try {
            String message ="";
            switch (id) {
                case R.id.joystickLeft:
                    zSpeed = round(-yPercent,3);

                    break;
                case R.id.joystickRight:
                    xSpeed = round(xPercent,3);
                    ySpeed = round(-yPercent,3);
                    break;
            }
            lastInstruction = new Instruction(0,0,0, false,xSpeed, ySpeed, zSpeed, false, false);
            if (lastInstruction!=null)
                Toast.makeText(this,lastInstruction.getJSON().toString(), Toast.LENGTH_LONG).show();
            if (!message.isEmpty() && output!=null){
                message = lastInstruction.getJSON().toString();
                output.writeBytes(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
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
     * Convertis des coordonnées x et y cartésiennes en coordonnées sphériques au format LatLng.
     */
    private LatLng cartesianToSpherical(int x, int y){
        double deltaLong = Math.abs(flyAreaTopLeft.longitude - flyAreaBotRight.longitude);
        double deltaLat = Math.abs(flyAreaTopLeft.latitude - flyAreaBotRight.latitude);
        double deltaX = Math.abs(DRONE_X_MAX - DRONE_X_MIN);
        double deltaY = Math.abs(DRONE_Y_MAX -DRONE_Y_MIN);

        double percentXpos = (x - DRONE_X_MIN)/deltaX;
        double percentYpos = (y - DRONE_Y_MIN)/deltaY;

        double xInLong = flyAreaTopLeft.longitude + percentXpos * deltaLong;
        double yInLat = flyAreaTopLeft.latitude - percentYpos * deltaLong;

        System.out.println("x=" + x + " percentXpos=" + percentXpos + " yInLat=" + yInLat + " ");
        return new LatLng(yInLat, xInLong);
    }

    /**
     * Charge et dessine la google Map et les différents objets qui y sont représentés.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng homePosition = cartesianToSpherical(0,0);
        LatLng dronePosition = cartesianToSpherical(-50,0);//new LatLng(44.8037, -0.6057);

        MarkerOptions moHOME = new MarkerOptions();
        moHOME.position(homePosition).title("Home");
        mMap.addMarker(moHOME);

        moDRONE = new MarkerOptions();
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
                case R.id.landing:
                    lastInstruction = new Instruction(0,0,0, false, 0, 0, 0, true, false);
                    if (lastInstruction!=null)
                        Toast.makeText(this,lastInstruction.getJSON().toString(), Toast.LENGTH_LONG).show();
                case R.id.take_off:
                    lastInstruction = new Instruction(0,0,0, false, 0, 0, 0, false, true);
                case R.id.autopilote:
                    if (x.getText()!=null && y.getText()!=null && z.getText()!=null && !x.getText().toString().isEmpty() && !y.getText().toString().isEmpty() && !z.getText().toString().isEmpty()){
                        int valX = Integer.parseInt(x.getText().toString());
                        int valY = Integer.parseInt(y.getText().toString());
                        int valZ = Integer.parseInt(z.getText().toString());

                        lastInstruction = new Instruction(valX,valY,valZ,true,0,0,0, false, false);
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

    public float round(float n, int nbSignificatif) {
        String s = String.valueOf(n);
        int charSignificatif=nbSignificatif+1;
        if (s.charAt(0)=='-') charSignificatif++;
        if (s.length()>charSignificatif+1)
            return Float.parseFloat(s.substring(0,charSignificatif));
        return n;
    }

}
