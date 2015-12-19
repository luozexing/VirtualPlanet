package com.virtualplanet.virtualplanet;

import android.util.Log;

import com.netease.pomelo.DataCallBack;
import com.netease.pomelo.PomeloClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by suyhuai on 2015/12/19.
 */
public class Message {

    private String gateRoute = "gate.gateHandler.queryEntry";
    private String connectorRoute = "connector.entryHandler.entry";
    private String areaRoute = "area.areaHandler.map";
    private String gateHost = "192.168.1.101";
    private int gatePort = 3222;

    public PomeloClient pomeloClient;
    private String TAG = "Message.class";

    public void queryConnector(String username, final JSONObject reqMsg, final CallBack callBack){
        pomeloClient = new PomeloClient(gateHost,gatePort);
        pomeloClient.init();

        JSONObject gateMsg = new JSONObject();
        try {
            gateMsg.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pomeloClient.request(gateRoute, gateMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject res) {
                pomeloClient.disconnect();
                try {
                    Log.d(TAG, "to connectorEnter");
                    connectorEnter(res.getString("host"),res.getInt("port"),reqMsg, callBack);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void connectorEnter(String host, int port, JSONObject reqMsg, final CallBack callBack){
        pomeloClient = new PomeloClient(gateHost,port);
        pomeloClient.init();
        Log.d(TAG, "in connectorEnter");
        pomeloClient.request(connectorRoute, reqMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject res) {
                pomeloClient.disconnect();
                callBack.execute(res);
            }
        });
    }

    public void queryArea(String username, final JSONObject reqMsg, final CallBack callBack){
        pomeloClient = new PomeloClient(gateHost,gatePort);
        pomeloClient.init();

        JSONObject gateMsg = new JSONObject();
        try {
            gateMsg.put("username", username);
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
                    areaEnter(res.getString("host"),res.getInt("port"),reqMsg, callBack);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void areaEnter(String host, int port, JSONObject reqMsg, final CallBack callBack){
        pomeloClient = new PomeloClient(gateHost,port);
        pomeloClient.init();

        pomeloClient.request(areaRoute, reqMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject res) {
                pomeloClient.disconnect();
                callBack.execute(res);
            }
        });
    }
}
