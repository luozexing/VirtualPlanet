package com.virtualplanet.virtualplanet;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.netease.pomelo.DataCallBack;
import com.netease.pomelo.PomeloClient;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryPomelo {

    private String gateRoute = "gate.gateHandler.queryEntry";
    private String gateHost = "45.33.32.123";
    private int gatePort = 3222;

    public PomeloClient pomeloClient;
    private String TAG = "QueryPomelo.class";

    public void queryConnector(String username, final JSONObject reqMsg, final Handler handler, final String route){
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
                    connectorEnter(res.getString("host"),res.getInt("port"),reqMsg, handler, route);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void connectorEnter(String host, int port, JSONObject reqMsg, final Handler handler,final String route){
        pomeloClient = new PomeloClient(host,port);
        pomeloClient.init();
        Log.d(TAG, "in connectorEnter");
        pomeloClient.request(route, reqMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject res) {
                pomeloClient.disconnect();
                Message message = Message.obtain();
                message.obj = res;
                handler.sendMessage(message);
            }
        });
    }

    public void queryArea(String username, final JSONObject reqMsg, final Handler handler,final String route){
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
                    areaEnter(res.getString("host"),res.getInt("port"),reqMsg, handler, route);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void areaEnter(String host, int port, JSONObject reqMsg, final Handler handler,final String route){
        pomeloClient = new PomeloClient(gateHost,port);
        pomeloClient.init();

        pomeloClient.request(route, reqMsg, new DataCallBack() {
            @Override
            public void responseData(JSONObject res) {
                pomeloClient.disconnect();
                Message message = Message.obtain();
                message.obj = res;
                handler.sendMessage(message);
            }
        });
    }
}
