package com.virtualplanet.virtualplanet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by suyhuai on 2015/12/18.
 */
public class LoginFragment extends Fragment{
    private String TAG = this.getClass().getSimpleName();
    SharedPreferences auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        auth = getActivity().getSharedPreferences("auth",0);
        View view = inflater.inflate(R.layout.login, container, false);

        final TextView log = (TextView) view.findViewById(R.id.logs);
        final EditText userNameText = (EditText) view.findViewById(R.id.userNameText);
        final EditText passwdText = (EditText) view.findViewById(R.id.passwdText);
        final Button login = (Button) view.findViewById(R.id.btnLogin);
        final CheckBox save = (CheckBox) view.findViewById(R.id.saveUser);
        final Button register = (Button) view.findViewById(R.id.btnRegister);
        final CheckBox auto = (CheckBox) view.findViewById(R.id.autoLogin);

        FragmentManager fm = getFragmentManager();
        final FragmentTransaction transaction = fm.beginTransaction();

        Drawable accountImg = getResources().getDrawable(R.mipmap.login_icon_account, null);
        accountImg.setBounds(0, 0, 80, 80);
        userNameText.setCompoundDrawables(accountImg, null, null, null);

        Drawable passwordImg = getResources().getDrawable(R.mipmap.login_icon_password, null);
        passwordImg.setBounds(0, 0, 80, 70);
        passwdText.setCompoundDrawables(passwordImg, null, null, null);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.setBackgroundColor(Color.parseColor("#042178"));
                String username = userNameText.getText().toString();
                String passwd = passwdText.getText().toString();
                JSONObject reqMsg = new JSONObject();
                try {
                    reqMsg.put(username,passwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                log.setText("login button is clicked");
                Log.d(TAG, "login button is clicked");

                SharedPreferences.Editor authEditor = auth.edit();
                if(save.isChecked()){
                    authEditor.putString("userName",username);
                    authEditor.putString("passwd",passwd);
                    log.setText("login button is clicked,and will save username and password");
                    Log.d(TAG, "save username and password");
                }else {
                    authEditor.putString("userName",null);
                    authEditor.putString("passwd",null);

                    log.setText("login button is clicked,and will not save username and password.");
                    Log.d(TAG, "not save username and password.");
                }
                authEditor.commit();

                JSONObject result = Message.queryConnector(username,reqMsg);
                String code = null;
                String msg = null;
                try {
                    code = result.getString("code");
                    msg = result.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code != null){
                    if(code.equals(200)){
                        ContentFragment contentFragment = new ContentFragment();
                        transaction.replace(R.id.fragment_layout,contentFragment);
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(), msg,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用segment
                view.setBackgroundColor(Color.parseColor("#042178"));

                log.setText("register button is clicked");
                Log.d(TAG, "register button is clicked");
            }
        });

        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor authEditor = auth.edit();
                if (isChecked){
                    authEditor.putBoolean("autoLogin",true);

                    log.setText("autoLogin");
                    Log.d(TAG, "autoLogin");
                }else {
                    authEditor.putBoolean("autoLogin",false);

                    log.setText("not autoLogin");
                    Log.d(TAG, "not autoLogin");
                }
                authEditor.commit();
            }
        });

        save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor authEditor = auth.edit();
                if (isChecked){

                    log.setText("saveUser");
                    Log.d(TAG, "saveUser");
                }else {

                    log.setText("not saveUser");
                    Log.d(TAG, "not saveUser");
                }
                authEditor.commit();
            }
        });
        return view;
    }

}
