package bordeaux.gcs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
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

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    private Button leftButton, bottomButton, rightButton, topButton, flyToButton;
    private TextView x, y, z, altitudeIndicator;
    private SeekBar altitudeController;
    private final String HOST = "";
    private Socket socket;
    private DataOutputStream output;
    private Instruction lastInstruction;
    private int nbMessages =0;
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
        output = new DataOutputStream((socket.getOutputStream()));
        DataInputStream input = new DataInputStream((socket.getInputStream()));
        output.writeBytes("GCS");
    }

    private void linkViewObjects() {
        // Position
        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        z = findViewById(R.id.z);

        altitudeIndicator = findViewById(R.id.altitudeIndicator);
        leftButton = findViewById(R.id.leftButton);
        leftButton.setOnTouchListener(this);
        bottomButton = findViewById(R.id.bottomButton);
        bottomButton.setOnTouchListener(this);
        rightButton = findViewById(R.id.rightButton);
        rightButton.setOnTouchListener(this);
        topButton = findViewById(R.id.topButton);
        topButton.setOnTouchListener(this);
        flyToButton = findViewById(R.id.flyToButton);
        flyToButton.setOnTouchListener(this);
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
    public boolean onTouch(View v, MotionEvent event) {
        try {

            String message ="";
            switch (v.getId()) {
                case R.id.leftButton:
                    if (event.getAction()==MotionEvent.ACTION_DOWN)
                        lastInstruction = new Instruction(0,0,0,false,true,false,false, false);
                    else
                        lastInstruction = new Instruction(0,0,0,false,false,false,false, false);
                    break;
                case R.id.bottomButton:
                    if (event.getAction()==MotionEvent.ACTION_DOWN)
                        lastInstruction = new Instruction(0,0,0,false,false,false,true, false);
                    else
                        lastInstruction = new Instruction(0,0,0,false,false,false,false, false);
                    break;
                case R.id.rightButton:
                    if (event.getAction()==MotionEvent.ACTION_DOWN)
                        lastInstruction = new Instruction(0,0,0,true,false,false,false, false);
                    else
                        lastInstruction = new Instruction(0,0,0,false,false,false,false, false);
                    break;
                case R.id.topButton:
                    if (event.getAction()==MotionEvent.ACTION_DOWN)
                        lastInstruction = new Instruction(0,0,0,false,false,true,false, false);
                    else
                        lastInstruction = new Instruction(0,0,0,false,false,false,false, false);
                    break;
                case R.id.flyToButton:
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
                Toast.makeText(this,"connexion impossible", Toast.LENGTH_SHORT).show();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
