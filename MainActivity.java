package lage.oros.mx.mqttapptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    static String MQTTHOST ="tcp://192.168.1.70:1883";
    static String USERNAME ="user";
    static String PASSWORD ="pass";
    //String topicStr="hab1/luz";//outTopic
    String topicStr="inTopic";
    MqttAndroidClient client;

    TextView subText;
    Button pubBtn;

    MqttConnectOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subText = (TextView) findViewById(R.id.subText);
        pubBtn = (Button) findViewById(R.id.button);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);

        options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected ///Aqui se puede agregar un toast //
                   // Log.d(TAG, "onSuccess");
                    Toast.makeText(MainActivity.this,"Conexión correcta",Toast.LENGTH_SHORT).show();
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems // Aqui se puede agregar un toast //
                    //Log.d(TAG, "onFailure");
                    Toast.makeText(MainActivity.this,"Error en la conexión",Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {



            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Toast.makeText(MainActivity.this,(new String(message.getPayload())),Toast.LENGTH_SHORT).show();
                subText.setText(new String(message.getPayload()));
                pubBtn.setText(new String (message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    public void pub(View v){
        String topic="inTopic";
        String outTopic="test/one";
        String message="Hello World";
        //String estado=pubBtn.getText().toString();

        try {
            client.publish(outTopic,message.getBytes(),0,false);

            /*if(estado.equals("off")){
                message="on";
                client.publish(topic,message.getBytes(),0,false);
                //client.publish(outTopic,message.getBytes(),0,false);
                //pubBtn.setText("on");
            }else if(estado.equals("on")){
                message="off";
                client.publish(topic,message.getBytes(),0,false);
                //client.publish(outTopic,message.getBytes(),0,false);
                //pubBtn.setText("off");
            }*/
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void setSubscription(){

        try{
            client.subscribe(topicStr,0);
        }catch (MqttException e){
            e.printStackTrace();
        }

    }

    public void conn(View v){

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected ///Aqui se puede agregar un toast //
                    // Log.d(TAG, "onSuccess");
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems // Aqui se puede agregar un toast //
                    //Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    public void disconn(View v){

        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected ///Aqui se puede agregar un toast //
                    // Log.d(TAG, "onSuccess");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems // Aqui se puede agregar un toast //
                    //Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}
