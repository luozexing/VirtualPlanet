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
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    PomeloClient pomeloClient;
    String gateRoute = "gate.gateHandler.queryEntry";
    String connectorRoute = "connector.entryHandler.entry";
    String areaRoute = "area.areaHandler.map";
    String gateHost = "192.168.1.101";
    int gatePort = 2015;

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

                queryArea("uid","test",new HashMap<String, String>());

                Snackbar.make(view,"test done!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void queryConnector(String uid,String name, final HashMap<String,String> hp){
        pomeloClient = new PomeloClient(gateHost,gatePort);
        pomeloClient.init();

        JSONObject gateMsg = new JSONObject();
        try {
            gateMsg.put(uid, name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
            pomeloClient.request(gateRoute, gateMsg, new DataCallBack() {
                @Override
                public void responseData(JSONObject res) {
                    pomeloClient.disconnect();
                    try {
                        connectorEnter(res.getString("host"),res.getInt("port"),hp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    public void queryArea(String uid,String name, final HashMap<String,String> hp){
        pomeloClient = new PomeloClient(gateHost,gatePort);
        pomeloClient.init();

        JSONObject gateMsg = new JSONObject();
        try {
            gateMsg.put(uid, name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pomeloClient.request(gateRoute, gateMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject res) {
                pomeloClient.disconnect();
                try {
                    areaEnter(res.getString("host"),res.getInt("port"),hp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void connectorEnter(String host, int port, HashMap<String,String> hp){
        JSONObject msg = new JSONObject();
        Iterator iterator = hp.keySet().iterator();
        try {
            while (iterator.hasNext())
            msg.put(iterator.next().toString(),hp.get(iterator.next()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pomeloClient.request(connectorRoute, msg, new DataCallBack() {
            @Override
            public void responseData(JSONObject jsonObject) {
                pomeloClient.disconnect();
                //do something...
            }
        });
    }

    public void areaEnter(String host, int port, HashMap<String,String> hp){
        JSONObject msg = new JSONObject();
        Iterator iterator = hp.keySet().iterator();
        try {
            while (iterator.hasNext())
                msg.put(iterator.next().toString(),hp.get(iterator.next()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pomeloClient.request(connectorRoute, msg, new DataCallBack() {
            @Override
            public void responseData(JSONObject jsonObject) {
                pomeloClient.disconnect();
                //do something...
                TextView tv = (TextView)findViewById(R.id.content);
                try {
                    tv.setText(jsonObject.getString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
