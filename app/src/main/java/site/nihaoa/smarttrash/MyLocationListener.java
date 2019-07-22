package site.nihaoa.smarttrash;

import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import site.nihaoa.smarttrash.rep.JdbcHelper;
import site.nihaoa.smarttrash.rep.Lese;
import site.nihaoa.smarttrash.rep.OkhttpHelper;

public class MyLocationListener extends BDAbstractLocationListener {
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean first = true;
    private RoutePlanSearch routePlanSearch;
    private LatLng persent;

    public MyLocationListener(final MapView mapView, BaiduMap baiduMap){
        this.mapView = mapView;
        this.baiduMap = baiduMap;
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getTitle().equals("trash")){
                    if(persent == null){
                        Toast.makeText(BMapApplication.getContext(),"当前定位未成功",Toast.LENGTH_LONG).show();
                    }
                    routePlanSearch = RoutePlanSearch.newInstance();
                    routePlanSearch.setOnGetRoutePlanResultListener(new ResultRoutePlan());
                    PlanNode stNode = PlanNode.withLocation(persent);
                    PlanNode enNode = PlanNode.withLocation(marker.getPosition());
                    routePlanSearch.walkingSearch((new WalkingRoutePlanOption())
                            .from(stNode)
                            .to(enNode));
                    return true;
                }else
                    return false;
            }
        });
    }


    private List<OverlayOptions> addMark(){
        try {
            List<Lese> leseList = JdbcHelper.getLeseList();
            BitmapDescriptor bitmap;
            if(null == leseList)
                return null;
            List<OverlayOptions> overlayOptionsList = new ArrayList<>();
            for (Lese lese:leseList){
                LatLng point = new LatLng(lese.getBaidu_lat(),lese.getBaidu_lon());
                if(lese.getFull() >= 1){
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.red_mark);
                }else {
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.blue_mark);
                }
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().title("trash")
                        .position(point)
                        .icon(bitmap);
                overlayOptionsList.add(option);
            }
            return overlayOptionsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null || mapView == null){
            return;
        }
        Log.d("main","位置信息："+location.getLongitude()+"  -  "+location.getLatitude()+"--"+location.getLocationDescribe());
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.getDirection()).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        baiduMap.setMyLocationData(locData);
        final LatLng latLng = new LatLng(location.getLongitude(),location.getLatitude());

        persent = latLng;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = OkhttpHelper.getOkHttpClient();
                try {
                    client.newCall(OkhttpHelper.getMusicSearchRequst(latLng.longitude,latLng.latitude)).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if(true)
                    return;
                List<OverlayOptions> optionsList = addMark();
                baiduMap.clear();
                for (OverlayOptions options:optionsList)
                    baiduMap.addOverlay(options);
            }
        }).start();
        if(first){
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15);
            baiduMap.animateMapStatus(mapStatusUpdate);
            Log.d("main","位置信息："+location.getLongitude()+"  -  "+location.getLatitude()+"--"+location.getLocationDescribe());
            Log.d("main","返回码："+location.getLocType());
            first = false;
        }
    }


    public class ResultRoutePlan implements OnGetRoutePlanResultListener {

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            WalkingRouteOverlay overlay = new WalkingRouteOverlay(baiduMap);
            if (walkingRouteResult.getRouteLines() != null && walkingRouteResult.getRouteLines().size() > 0) {
                //获取路径规划数据,(以返回的第一条数据为例)
                //为WalkingRouteOverlay实例设置路径数据
                overlay.setData(walkingRouteResult.getRouteLines().get(0));
                //在地图上绘制WalkingRouteOverlay
                overlay.addToMap();
            }else {
                Toast.makeText(BMapApplication.getContext(),"导航路线规划失败",Toast.LENGTH_LONG).show();
            }
            routePlanSearch.destroy();
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    }
}
