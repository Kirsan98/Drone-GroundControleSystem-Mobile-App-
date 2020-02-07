package bordeaux.gcs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button leftButton, bottomButton, rightButton, topButton, flyToButton;
    private TextView x, y, z, altitudeIndicator;
    private SeekBar altitudeController;
    private final String HOST = "";
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkViewObjects();

        try {
            connection();
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

    private void connection() throws IOException {
        socket= new Socket(HOST, 7778);
        DataOutputStream output = new DataOutputStream((socket.getOutputStream()));
        DataInputStream input = new DataInputStream((socket.getInputStream()));



    }

    private void linkViewObjects() {
        // Position
        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        z = findViewById(R.id.z);

        altitudeIndicator = findViewById(R.id.altitudeIndicator);
        leftButton = findViewById(R.id.leftButton);
        leftButton.setOnClickListener(this);
        bottomButton = findViewById(R.id.bottomButton);
        bottomButton.setOnClickListener(this);
        rightButton = findViewById(R.id.rightButton);
        rightButton.setOnClickListener(this);
        topButton = findViewById(R.id.topButton);
        topButton.setOnClickListener(this);
        flyToButton = findViewById(R.id.flyToButton);
        flyToButton.setOnClickListener(this);
        altitudeController = findViewById(R.id.altitudeController);
        altitudeController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Envoyer Altitude", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                altitudeIndicator.setText(String.valueOf(progress) + "m");

            }

        });
    }

    @Override
    public void onClick(View v) {
        String buttonPressed = "";
        switch (v.getId()) {
            case R.id.leftButton:
                buttonPressed = "Gauche";

                break;
            case R.id.bottomButton:
                buttonPressed = "Arrière";
                break;
            case R.id.rightButton:
                buttonPressed = "Droite";
                break;
            case R.id.topButton:
                buttonPressed = "Avant";
                break;
            case R.id.flyToButton:
                buttonPressed = "Voler vers";
                break;
        }
        Toast.makeText(this, "Bouton " + buttonPressed, Toast.LENGTH_SHORT).show();
    }
}
