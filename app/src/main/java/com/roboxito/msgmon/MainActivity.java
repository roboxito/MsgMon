package com.roboxito.msgmon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    Button btnEnviar;
    TextView lblIP;
    int serverPort=8099;
    String localIP;
    private ServerThread serverThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblIP=findViewById(R.id.lblIP);
        btnEnviar=findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String cadnumeros="<cel>,<cel>,<cel>,";
//                SmsManager sms= SmsManager.getDefault();
//                String numeros[]=cadnumeros.split(",");
//                for(String number:numeros) {
//                    sms.sendTextMessage(number, null, "Hey, Arnold!", null, null);
//                    Log.d("EnviaSMS local: ",number);
//                }

                Toast.makeText(MainActivity.this,"Mensaje de prueba",Toast.LENGTH_LONG).show();
            }
        });
        localIP=getIpAddress();
        lblIP.setText(localIP);
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                serverThread=new ServerThread();
                serverThread.startServer();
            }
        });
        t.start();

    }//Fin OnCreate


    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress() ) {
                        if(inetAddress.getHostAddress().contains(".") && ip.isEmpty())
                            ip =  inetAddress.getHostAddress() ;
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }

    class ServerThread extends Thread implements Runnable {
        private boolean serverRunning;
        private ServerSocket serverSocket;
        private int count=0;

        public void startServer(){
            Log.d("ServerThread","inicio servidor");
            serverRunning=true;
            start();
        }

        //https://stackoverflow.com/questions/1239026/how-to-create-a-file-in-android

        @Override
        public void run() {
            Log.d("ServerThread","inicio run");
            try {
                serverSocket = new ServerSocket(serverPort);
                while (serverRunning) {
                    Socket socket=serverSocket.accept();
                    String cad="";

                    //Recepcion del mensaje del socket en texto
                    String str="";
                    byte[] bytes = new byte[1024];
                    BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
                    for (int read = is.read(bytes,0,1024); read >= 0; read = is.read(bytes,0,1024)) {
                        str=new String(bytes,0,read);
                        Log.d("ServerRespuesta:",str);
                        cad=cad+str;
                    }
                    try {
                        is.close();
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Envia mensajes y continua, si hay datos
                    SmsManager sms= SmsManager.getDefault();
                    String numeros[]=cad.split(",");
                    Log.d("Numeros[0]:","["+numeros[0]+"]");
                    if(numeros[0].equals("hey_arnold$")) {
                        Log.d("Mensaje:",numeros[1]);
                        for (String number : numeros) {
                            if(number.length()==10) {
                                sms.sendTextMessage(number, null, numeros[1], null, null);
                                Log.d("EnviaSMS a: ", number);
                            }
                        }
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        public void stopServer(){
            serverRunning=false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(serverSocket!=null){
                        try {
                            serverSocket.close();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }//Fin ServerThread

    @Override
    protected void onDestroy() {
        if(serverThread!=null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }

}


// https://stackoverflow.com/questions/77271961/dependency-androidx-activityactivity1-8-0-requires-libraries-or-apps-that-de
//4423216950,Hola Arnold!,4423439781,
//4423216950,Hola Mundo,4423439781,

//https://stackoverflow.com/questions/21233340/sending-string-via-socket-python
//https://realpython.com/python-sockets/#echo-client
//# eco.py

//        import sys
//        import socket

//        HOST="192.168.1.99"
//        PORT = 8099


//        n=len(sys.argv)
//        print('Argumentos: ',n)
//        if (n!=3):
//        print('Syntaxis: eco <IP> "<Msg>"')
//        exit()
//        HOST=sys.argv[1]
//        cad=b""
//        cad=sys.argv[2]

//        with socket.socket(socket.AF_INET,socket.SOCK_STREAM) as s:
//        s.connect((HOST,PORT))
//        s.sendall(cad.encode("ascii"))
//        s.close()

//$ python eco.py 192.168.1.99 "4423216950,Hola Mundo,4423439781,"