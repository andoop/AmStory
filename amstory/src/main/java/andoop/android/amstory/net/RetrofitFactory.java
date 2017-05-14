package andoop.android.amstory.net;

import android.util.Log;

import java.util.HashMap;

import andoop.android.amstory.BuildConfig;
import lombok.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by wsy on 17/2/12.
 */

public class RetrofitFactory {

    /**
     * @param url      需要斜杠结尾
     * @param headers
     * @return
     */
    public static Retrofit createRetrofit(@NonNull String url, HashMap<String, String> headers) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Log.i("RetrofitLog", "retrofitBack = " + message));
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
        }
        if (headers != null && headers.size() > 0) {
            Interceptor interceptor = chain -> {
                Request.Builder builder1 = chain.request().newBuilder();
                for (String key : headers.keySet()) {
                    builder1.addHeader(key, headers.get(key));
                }
                return chain.proceed(builder1.build());
            };
            builder.addInterceptor(interceptor);
        }
        return new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(url.startsWith("http") ? url : "http://" + url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
}
