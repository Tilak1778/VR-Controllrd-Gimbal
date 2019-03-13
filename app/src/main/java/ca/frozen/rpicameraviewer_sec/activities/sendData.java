package ca.frozen.rpicameraviewer_sec.activities;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ca.frozen.rpicameraviewer_sec.R;

public class sendData extends AppCompatActivity implements SensorEventListener {

    EditText ip,port;
    Button connect,stop;
    Sensor sensor;
    SensorManager SM;

    DatagramSocket socket;
    DatagramPacket packet;
    String ipaddr;
    int p;
    static float value0,value1,value2;
    Thread mthread;

    float aPitch = 0; //angle from Pitch
    float aRoll = 0; //angle from Roll
    float aYaw = 0;// angle from Yaw

    double dt= (float) 0.01;
    public float timestamp;
    private static final float NS2S = 1.0f / 1000000000.0f;

    Button Reset_button;


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);

        ip=findViewById(R.id.editText_IP);
        port=findViewById(R.id.editText_port);
        connect=findViewById(R.id.button_start);
        stop=findViewById(R.id.button_stop);




        SM= (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor=SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SM.registerListener(sendData.this,sensor,SensorManager.SENSOR_DELAY_FASTEST);


                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        ipaddr=ip.getText().toString();
                        p=Integer.parseInt(port.getText().toString());



                    }
                });

                thread.start();




            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SM.unregisterListener(sendData.this);
                aYaw=0;
                aRoll=0;
                aPitch=0;

               // socket.close();
            }
        });


    }


    public void send(final String ip, final int port, final String data){

        mthread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    byte[] msg=data.getBytes();
                    InetAddress inetAddress=InetAddress.getByName(ip);
                    socket=new DatagramSocket(port);
                    packet=new DatagramPacket(msg,msg.length,inetAddress,port);
                    socket.send(packet);
                    socket.close();


                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        mthread.start();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {


        if(timestamp!=0) {
            float dT=(event.timestamp - timestamp)*NS2S;


            float dPitch = (float) ((event.values[2]));// angular rate from Pitch
            float dRoll = (float) ((event.values[1]));// angular rate from Roll
            float dYaw = (float) ((event.values[0])); //angular rate from Yaw


            aPitch += dPitch * dT; // or aPitch = aPitch + dPitch*dt;
            aRoll += dRoll * dT; // or aRoll = aRoll + dRoll*dt;
            aYaw += dYaw * dT; // or aYaw = aYaw + dYaw*dt;


        }
        timestamp = event.timestamp;


        String ori="yaw:"+Math.round(aYaw*-57)+"    roll:"+Math.round(aPitch*-57)+"  pitch: "+Math.round(aRoll*-57);

        send(ipaddr,p,ori);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
