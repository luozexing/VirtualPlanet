package com.virtualplanet.virtualplanet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.netease.pomelo.DataCallBack;
import com.netease.pomelo.PomeloClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

public class MainActivity extends AppCompatActivity {

    PomeloClient pomeloClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pomeloClient = new PomeloClient("192.168.0.68",3010);
                pomeloClient.init();

                String route = "area.areaHandler.map";
                //String route = "connector.entryHandler.entry";

                JSONObject msg = new JSONObject();
                try {
                    //msg.put("username","TestUser");
                    msg.put("coordinate", 1234);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                pomeloClient.request(route, msg, new DataCallBack() {
                    @Override
                    public void responseData(JSONObject jsonObject) {
                        pomeloClient.disconnect();
                        Log.d(getClass().getSimpleName(),"------------------");//jsonObject.toString()
                        TextView tv = (TextView)findViewById(R.id.content);
                        tv.setText("hahaha111");
                    }
                });
//==================================SocketIO-java==============================
                /*SocketIO socket = null;
                try {
                    socket = new SocketIO("http://127.0.0.1:6050/");
                }catch (Exception e){
                    e.printStackTrace();
                }

                socket.connect(new IOCallback() {
                    @Override
                    public void onDisconnect() {

                    }

                    @Override
                    public void onConnect() {
                        System.out.println("=======================");
                    }

                    @Override
                    public void onMessage(String s, IOAcknowledge ioAcknowledge) {

                    }

                    @Override
                    public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge) {
                        try {
                            System.out.println("Server said:" + jsonObject.toString(2));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {

                    }

                    @Override
                    public void onError(SocketIOException e) {

                    }
                });*/

               Snackbar.make(view,"test1", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
