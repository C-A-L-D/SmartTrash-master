package site.nihaoa.smarttrash.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import site.nihaoa.smarttrash.BMapApplication;
import site.nihaoa.smarttrash.MyLocationListener;
import site.nihaoa.smarttrash.R;
import site.nihaoa.smarttrash.tools.ViewBean;
import site.nihaoa.smarttrash.tools.ViewTools;

public class MapFragment extends Fragment {
    private View rootView;

    @ViewBean(id = R.id.bmapView,parentViewName = "rootView")
    private MapView mMapView;

    private BaiduMap baiduMap;
    private Context context;
    private LocationClient mLocationClient;

    public MapFragment() {
        context = BMapApplication.getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.map_layout, container, false);
            ViewTools.register(MapFragment.this,rootView);

            baiduMap = mMapView.getMap();
            baiduMap.setMyLocationEnabled(true);
            initMyLocation();
        }
        return rootView;
    }


    private void initMyLocation(){
        //定位初始化
        mLocationClient = new LocationClient(context);
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(false);
        option.setIsNeedLocationDescribe(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new MyLocationListener(mMapView,baiduMap));
        mLocationClient.start();
    }


    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if(mLocationClient != null){
            mLocationClient.stop();
        }
    }
}
