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

public class MainActivity extends AppCompatActivity {

    private String gateRoute = "gate.gateHandler.queryEntry";
    private String connectorRoute = "connector.entryHandler.entry";
    private String areaRoute = "area.areaHandler.map";
    private String gateHost = "192.168.1.101";
    private int gatePort = 2015;

    private String TAG = this.getClass().getSimpleName();
    public PomeloClient pomeloClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate: done.");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: clicked.");
                JSONObject reqMsg = new JSONObject();
                try {
                    reqMsg.put("coordinate","1234");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                queryArea("uid","test",reqMsg);
                //areaEnter(gateHost,3010,reqMsg);

                Snackbar.make(view,"test done!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void queryConnector(String uid,String name, final JSONObject reqMsg){
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
                        connectorEnter(res.getString("host"),res.getInt("port"),reqMsg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    private void queryArea(String uid,String name, final JSONObject reqMsg){
        pomeloClient = new PomeloClient(gateHost,gatePort);
        pomeloClient.init();

        JSONObject gateMsg = new JSONObject();
        try {
            gateMsg.put(uid, name);
            Log.d(TAG, "queryArea: put uid into gateMsg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pomeloClient.request(gateRoute, gateMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject res) {
                pomeloClient.disconnect();
                Log.d(TAG, "responseData: try to call areaEnter");
                try {
                    areaEnter(res.getString("host"),res.getInt("port"),reqMsg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void connectorEnter(String host, int port, JSONObject reqMsg){
        pomeloClient = new PomeloClient(host,port);
        pomeloClient.init();

        pomeloClient.request(connectorRoute, reqMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject jsonObject) {
                pomeloClient.disconnect();
                //do something...
            }
        });
    }

    private void areaEnter(String host, int port, JSONObject reqMsg){
        Log.d(TAG, "areaEnter: host="+host+"; port="+port);
        //if host is 127.0.0.1, it should be replaced as local IP, eg:192.168.1.101
        pomeloClient = new PomeloClient(host,port);
        pomeloClient.init();

        Log.d(TAG, "areaEnter: "+reqMsg.toString());
        pomeloClient.request(areaRoute, reqMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject jsonObject) {
                pomeloClient.disconnect();
                Log.d(TAG, "responseData: areaServer deal.");
                String text= null;
                try {
                    text = jsonObject.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TextView tv = (TextView)findViewById(R.id.content);
                tv.setText(text);

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
