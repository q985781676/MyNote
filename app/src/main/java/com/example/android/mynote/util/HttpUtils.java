package com.example.android.mynote.util;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    public static final String BASE_URL = "http://192.168.1.5:8080";
    //存储cookie
    private static Map<String, List<Cookie>> cookieStore = new HashMap<>();

    //创建自定义线程池
    private static ExecutorService threadPool = Executors.newFixedThreadPool(30);
    /*private static ExecutorService threadPool = new ThreadPoolExecutor(
            3,                              //线程池中常驻线程数
            10,                        //允许容纳最大线程数
            2,                            //多余空闲线程的存活时间
            TimeUnit.SECONDS,                           //keepAliveTime 单位
            new LinkedBlockingDeque<>(5),      //任务队列
            Executors.defaultThreadFactory(),           //默认线程工厂
            new ThreadPoolExecutor.CallerRunsPolicy()   //不会抛弃任务也不会抛出异常，而是回退给调用者
    );*/

    //创建okhttp对象并让他管理cookie状态
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies == null ? new ArrayList<>() : cookies;
                }
            }).build();

    /**
     * 异步GET
     * @param url 发送请求的url
     * @return 服务器返回的json
     * @throws Exception 抛出异常，引用时根据需要处理异常逻辑
     */
    public static String getRequest(String url) throws Exception {
        //创建异步任务
        FutureTask<String> task = new FutureTask<>(() -> {
            //创建请求对象
            Request request = new Request.Builder().url(url).build();
            //加入发送GET请求
            Call call = okHttpClient.newCall(request);
            //执行请求
            Response response = call.execute();
            //服务器响应
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string().trim();
            } else {
                return null;
            }

        });
        //将异步任务交由线程池执行
        threadPool.submit(task);
        //返回异步任务结果结果
        return task.get();
    }


    /**
     * 异步POST提交表单
     * @param url
     * @param rawParams
     * @return
     * @throws Exception
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String postRequestForm(String url, Map<String, String> rawParams) throws Exception {
        FutureTask<String> task = new FutureTask<>(() -> {
            //构建包含请求参数的表单体
            FormBody.Builder builder = new FormBody.Builder();
            //将传入参数赋给表单构造器 （方法引用写法）
            rawParams.forEach(builder::add);
            FormBody body = builder.build();
            //创建请求对象
            Request request = new Request.Builder().url(url).post(body).build();
            //加入POST请求
            Call call = okHttpClient.newCall(request);
            //执行请求 - 异步回调
            Response response = call.execute();
            //服务器响应
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string().trim();
            } else {
                Log.d("http", "postRequestForm: "+response.isSuccessful());
                Log.d("http", "postRequestForm: "+response.body().string().trim());
                return null;
            }
        });

        //将异步任务交由线程池执行
        threadPool.submit(task);
        //返回异步任务结果结果
        return task.get();
    }

    //json格式
    private static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 异步POST提交表单
     * @param url
     * @param target
     * @return
     * @throws Exception
     */
    public static String postRequestJson(String url, Object target) throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(target);
        FutureTask<String> task = new FutureTask<>(() -> {
            RequestBody body = RequestBody.create(JSON,json);
            //创建请求对象
            Request request = new Request.Builder().url(url).post(body).build();
            //加入POST请求
            Call call = okHttpClient.newCall(request);
            //执行请求 - 异步回调
            Response response = call.execute();
            //服务器响应
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string().trim();
            } else {
                Log.d("http", "postRequestForm: "+response.isSuccessful());
                Log.d("http", "postRequestForm: "+response.body().string().trim());
                return null;
            }
        });

        //将异步任务交由线程池执行
        threadPool.submit(task);
        //返回异步任务结果结果
        return task.get();
    }

}
