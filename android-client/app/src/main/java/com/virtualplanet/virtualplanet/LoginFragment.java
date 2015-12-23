package com.virtualplanet.virtualplanet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.lang.ref.WeakReference;

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

        Drawable accountImg = getResources().getDrawable(R.mipmap.login_icon_account, getActivity().getTheme());
        if (accountImg != null) {
            accountImg.setBounds(0, 0, 80, 80);
        }
        userNameText.setCompoundDrawables(accountImg, null, null, null);

        Drawable passwordImg = getResources().getDrawable(R.mipmap.login_icon_password, getActivity().getTheme());
        if (passwordImg != null) {
            passwordImg.setBounds(0, 0, 80, 70);
        }
        passwdText.setCompoundDrawables(passwordImg, null, null, null);

        final MyHandler myHandler = new MyHandler(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userNameText.getText().toString();
                String password = passwdText.getText().toString();
                JSONObject reqMsg = new JSONObject();
                try {
                    reqMsg.put("password",password).put("username",username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "login button is clicked");

                SharedPreferences.Editor authEditor = auth.edit();
                if(save.isChecked()){
                    authEditor.putString("userName",username);
                    authEditor.putString("password",password);
                    Log.d(TAG, "save username and password");
                }else {
                    authEditor.putString("userName",null);
                    authEditor.putString("password",null);

                    Log.d(TAG, "not save username and password.");
                }
                authEditor.apply();

                QueryPomelo queryPomelo = new QueryPomelo();

                queryPomelo.queryConnector(username,reqMsg, myHandler, "auth.authHandler.signin");
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment registerFragment = new RegisterFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout,registerFragment).commit();

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
                authEditor.apply();
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
                authEditor.apply();
            }
        });
        return view;
    }

    static class MyHandler extends Handler {
        private WeakReference<LoginFragment> mLoginFragmWR;

        MyHandler(LoginFragment theLoginFragment) {
            mLoginFragmWR = new WeakReference<>(theLoginFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginFragment mLoginFragment = mLoginFragmWR.get();
            JSONObject jsonObject = (JSONObject) msg.obj;
            int code = 0;
            String info = null;
            try {
                code = jsonObject.getInt("code");
                info = jsonObject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(code == 0){
                Toast.makeText(mLoginFragment.getActivity().getApplicationContext(),"服务器未响应",Toast.LENGTH_SHORT).show();
            }else if(code == 200){
                ContentFragment contentFragment = new ContentFragment();
                FragmentTransaction transaction = mLoginFragment.getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout,contentFragment);
                transaction.commit();
            }else if (code == 500){
                Toast.makeText(mLoginFragment.getActivity().getApplicationContext(),info,Toast.LENGTH_SHORT).show();
            }
        }
    }

}
