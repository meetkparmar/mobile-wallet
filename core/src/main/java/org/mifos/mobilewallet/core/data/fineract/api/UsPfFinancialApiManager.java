package org.mifos.mobilewallet.core.data.fineract.api;

import org.mifos.mobilewallet.core.data.fineract.api.services.UsPfFinancialService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsPfFinancialApiManager {

    public static final String TENANT_ID = "ibank-usa";
    public static final String CONTENT_TYPE_ID = "application/json";
    private static BaseURL baseUrl = new BaseURL();
    private static final String BASE_URL = baseUrl.getUsPfFinancialUrl();

    private static Retrofit retrofit;
    private static UsPfFinancialService usPfFinancialService;

    public UsPfFinancialApiManager() {
        createService();
    }

    public static void createService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new UsPfFinancialApiInterceptor(TENANT_ID, CONTENT_TYPE_ID))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        init();
    }

    private static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    private static void init() {

        usPfFinancialService = createApi(UsPfFinancialService.class);
    }

    public UsPfFinancialService getUsPfFinancialServiceApi() {
        return usPfFinancialService;
    }
}
