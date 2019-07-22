package site.nihaoa.smarttrash.rep;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpHelper {
    private static OkHttpClient okHttpClient;


    public static OkHttpClient getOkHttpClient(){
        if (okHttpClient == null){
            okHttpClient = new OkHttpClient.Builder().build();
        }
        return okHttpClient;
    }

    public static Request getMusicSearchRequst(Double lat,Double lng){
//        String url = "http://192.168.43.98:8080/jsp1_4_war_exploded/map";
        String url = "http://192.168.43.173:8080/jsp1_4_war_exploded/map";
        RequestBody body = new FormBody.Builder()
                .add("lon",lat.toString())
                .add("lat",lng.toString()).build();
        Request request = new Request.Builder().url(url).post(body).build();
        return request;
    }
}
