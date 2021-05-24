package org.mifos.mobilewallet.core.data.fineract.api;

import org.mifos.mobilewallet.core.data.fineract.api.services.FinancialService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class IBankPaymentHubApiManager {

    public static final String TENANT_ID = "ibank-usa";
    private static BaseURL baseUrl = new BaseURL();
    private static final String BASE_URL = baseUrl.getFinancialUrl();

    private static Retrofit retrofit;
    private static FinancialService financialServiceApi;

    public IBankPaymentHubApiManager() {
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
                .addInterceptor(new FinancialApiInterceptor(TENANT_ID))
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

        financialServiceApi = createApi(FinancialService.class);
    }

    public FinancialService getFinancialServiceApi() {
        return financialServiceApi;
    }
}
