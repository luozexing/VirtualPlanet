package com.virtualplanet.virtualplanet;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.ref.WeakReference;

public class RegisterFragment extends Fragment{
    private String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.register, container, false);

        final EditText userNameText = (EditText) view.findViewById(R.id.register_userName);
        final EditText passwdText = (EditText) view.findViewById(R.id.register_passwd);
        final EditText name = (EditText) view.findViewById(R.id.register_name);
        final Button register = (Button) view.findViewById(R.id.btnRegister);
        final Button back = (Button) view.findViewById(R.id.btnBack);

        Drawable accountImg = getResources().getDrawable(R.mipmap.login_icon_account, getActivity().getTheme());
        if (accountImg != null) {
            accountImg.setBounds(0, 0, 80, 80);
        }
        userNameText.setCompoundDrawables(accountImg, null, null, null);
        name.setCompoundDrawables(accountImg, null, null, null);

        Drawable passwordImg = getResources().getDrawable(R.mipmap.login_icon_password, getActivity().getTheme());
        if (passwordImg != null) {
            passwordImg.setBounds(0, 0, 80, 70);
        }
        passwdText.setCompoundDrawables(passwordImg, null, null, null);

        final MyHandler myHandler = new MyHandler(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = userNameText.getText().toString();
                String passwd = passwdText.getText().toString();
                JSONObject reqMsg = new JSONObject();
                try {
                    reqMsg.put("username",username);
                    reqMsg.put("passwd",passwd);
                    reqMsg.put("name",name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "login button is clicked");

                QueryPomelo queryPomelo = new QueryPomelo();

                queryPomelo.queryConnector(username,reqMsg, myHandler,"connector.entryHandler.signup");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment mLoginFragment = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout,mLoginFragment).commit();
            }
        });
        return view;
    }

    static class MyHandler extends Handler {
        private WeakReference<RegisterFragment> mRegFragER;

        MyHandler(RegisterFragment theRegisterFragment) {
            mRegFragER = new WeakReference<>(theRegisterFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            RegisterFragment mRegisterFragment = mRegFragER.get();
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
                Toast.makeText(mRegisterFragment.getActivity().getApplicationContext(),"服务器未响应",Toast.LENGTH_SHORT).show();
            }else if(code == 200){
                Toast.makeText(mRegisterFragment.getActivity().getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                LoginFragment mLoginFragment = new LoginFragment();
                FragmentTransaction transaction = mRegisterFragment.getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout,mLoginFragment);
                transaction.commit();
            }else if (code == 500){
                Toast.makeText(mRegisterFragment.getActivity().getApplicationContext(),info,Toast.LENGTH_SHORT).show();
            }
        }
    }

}
