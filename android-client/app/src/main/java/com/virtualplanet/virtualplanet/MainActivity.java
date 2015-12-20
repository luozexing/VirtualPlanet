package com.virtualplanet.virtualplanet;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.mapapi.SDKInitializer;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    private LoginFragment loginFragment;
    private ContentFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        final SharedPreferences auth = getSharedPreferences("auth",0);
        Boolean autoLogin = auth.getBoolean("autoLogin",false);
        if(autoLogin){
            String username = auth.getString("username",null);
            String passwd = auth.getString("passwd",null);
            if(username != null && passwd != null){
                JSONObject gateMsg = new JSONObject();
                try {
                    gateMsg.put(username, passwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //JSONObject result = QueryPomelo.queryConnector("name",gateMsg);
            }

            contentFragment = new ContentFragment();
            transaction.replace(R.id.fragment_layout, contentFragment);
            transaction.commit();
        }else{
            loginFragment = new LoginFragment();
            transaction.replace(R.id.fragment_layout, loginFragment);
            transaction.commit();
        }
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
