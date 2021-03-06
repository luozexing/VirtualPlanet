package com.virtualplanet.virtualplanet;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.TextureMapView;

public class ContentFragment extends Fragment {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.content_main, container, false);
        TextureMapView mMapView = (TextureMapView) view.findViewById(R.id.bmapView);

        Log.d(TAG, "onCreate: done.");
        return view;
    }
}