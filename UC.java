import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Scanner;
import java.util.ArrayList;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.json.Json;
import javax.json.JsonReaderFactory;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class UC {
    public static final Scanner sc = new Scanner(System.in);
    private static ArrayList<JsonObject> commands = new ArrayList<JsonObject>();
    private static String status = "send";
    private static JsonObject instruction;

    public static void main(String[] args) throws UnknownHostException, IOException {

        try {
            /**
                Connection au BUS et envoie des instructions recupéré.
             */
            Socket bus = new Socket("localhost", 7777);
            DataOutputStream toBus = new DataOutputStream(bus.getOutputStream());
            DataInputStream fromBus = new DataInputStream(bus.getInputStream());

            if (bus.isConnected()) {
                if (instruction!=null){
                    JsonObject message = Json.createObjectBuilder()
                        .add("Type", "UC")
                        .add("TTL", 0)
                        .add("metadata", instruction);
                        .build();
                    toBus.writeBytes(message.toString() + "\n");
                }
                while (bus.isConnected()) {
                    System.out.println(fromBus.readLine());
                }
            }

        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException : ");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException : ");
            System.out.println(e.getMessage());
        }
    }

    /**
        Serveur reception des instructions de la GCS
     */
    class UcThread extends Thread {
        ServerSocket gcsSocket ;
        String id;
        String status;

        public UcThread(Socket s, String id) throws IOException {
            this.gcsSocket = new ServerSocket(7778);
            this.id = id;
        }

        public void run() {
            if (this.id == "GCS") {
                try {
                    while(true) {
                        this.gcsSocket.accept();
                        DataOutputStream toGCS = new DataOutputStream(gcsSocket.getOutputStream());
                        DataInputStream fromGCS = new DataInputStream(gcsSocket.getInputStream());
                        String instructionString;
                        while ((instructionString = input.readLine()) != null){
                            JSONParser parser = new JSONParser();
                            try {
                                JsonObject json = (JSONObject) (parser.parse(instructionString));
                            }
                            JsonObject instructionFromGCS = Json.createObjectBuilder()
                                .add("id", "UC")
                                .add("TTL", 0)
                                .add("metadata", json)
                                .build();
                            instruction = instructionFromGCS;
                        }
                } catch (UnknownHostException e) {
                    System.out.println("UnknownHostException : ");
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println("IOException : ");
                    System.out.println(e.getMessage());
                }
            }

        }
    }

}
