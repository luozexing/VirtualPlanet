package com.virtualplanet.virtualplanet;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by suyhuai on 2015/12/10.
 */
public class MyLocationListener implements BDLocationListener
{
    @Override
    public void onReceiveLocation(BDLocation location)
    {

        // map view 销毁后不在处理新接收的位置
        if (location == null || mMapView == null)
            return;
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mXDirection).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mCurrentAccracy = location.getRadius();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        mCurrentLantitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();
        // 设置自定义图标
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.navi_map_gps_locked);
        MyLocationConfigeration config = new MyLocationConfigeration(
                mCurrentMode, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfigeration(config);
        // 第一次定位时，将地图位置移动到当前位置
        if (isFristLocation)
        {
            isFristLocation = false;
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
        }
    }

}