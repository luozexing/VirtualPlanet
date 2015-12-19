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

    private static String gateRoute = "gate.gateHandler.queryEntry";
    private static String connectorRoute = "connector.entryHandler.entry";
    private static String areaRoute = "area.areaHandler.map";
    private static String gateHost = "192.168.1.101";
    private static int gatePort = 3222;

    public static PomeloClient pomeloClient;
    private static String TAG = "Message.class";

    public static JSONObject queryConnector(String username, final JSONObject reqMsg){
        final JSONObject[] list = new JSONObject[1];
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
                    JSONObject result = connectorEnter(res.getString("host"),res.getInt("port"),reqMsg);
                    list[0] = result;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return list[0];
    }

    public static JSONObject queryArea(String username, final JSONObject reqMsg){
        final JSONObject[] list = new JSONObject[1];
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
                    JSONObject result = areaEnter(res.getString("host"),res.getInt("port"),reqMsg);

                    list[0] = result;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return list[0];
    }

    private static JSONObject connectorEnter(String host, int port, JSONObject reqMsg){
        final ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        final JSONObject result;
        pomeloClient = new PomeloClient(gateHost,port);
        pomeloClient.init();

        pomeloClient.request(connectorRoute, reqMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject jsonObject) {
                pomeloClient.disconnect();
                list.add(jsonObject);
            }
        });
        return list.get(0);
    }

    private static JSONObject areaEnter(String host, int port, JSONObject reqMsg){
        final ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        pomeloClient = new PomeloClient(host,port);
        pomeloClient.init();

        Log.d(TAG, "areaEnter: "+reqMsg.toString());
        pomeloClient.request(areaRoute, reqMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject jsonObject) {
                pomeloClient.disconnect();
                list.add(jsonObject);
            }
        });
        return list.get(0);
    }
}
