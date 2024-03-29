package site.nihaoa.smarttrash.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import site.nihaoa.smarttrash.R;
import site.nihaoa.smarttrash.fragment.AppViewPage;
import site.nihaoa.smarttrash.fragment.MapFragment;
import site.nihaoa.smarttrash.fragment.MyFragmentPageAdapter;
import site.nihaoa.smarttrash.fragment.NewsFragment;
import site.nihaoa.smarttrash.fragment.WeatherFragment;
import site.nihaoa.smarttrash.tools.ViewBean;
import site.nihaoa.smarttrash.tools.ViewTools;

public class MainActivity extends AppCompatActivity {
    @ViewBean(id = R.id.viewPage)
    private AppViewPage viewPager;

    @ViewBean(id = R.id.navigation)
    private BottomNavigationView bottomNavigationView;

    private MyFragmentPageAdapter myFragmentPageAdapter;

    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    private void settingConpment(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_map:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_weather:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_news:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewTools.register(MainActivity.this,this);
        getPersimmions();
        myFragmentPageAdapter = new MyFragmentPageAdapter(getSupportFragmentManager());
        myFragmentPageAdapter.addFragment(new MapFragment());
        myFragmentPageAdapter.addFragment(new WeatherFragment());
        myFragmentPageAdapter.addFragment(new NewsFragment());
        viewPager.setAdapter(myFragmentPageAdapter);
        settingConpment();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("提示");
        dialogBuilder.setMessage("确定退出程序?");
        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                System.exit(0);
            }
        });
        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogBuilder.create().show();
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}
