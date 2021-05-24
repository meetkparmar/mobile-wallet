package org.mifos.mobilewallet.core.data.fineract.api;

import org.mifos.mobilewallet.core.data.fineract.api.services.AuthenticationService;
import org.mifos.mobilewallet.core.data.fineract.api.services.BeneficiaryService;
import org.mifos.mobilewallet.core.data.fineract.api.services.ClientService;
import org.mifos.mobilewallet.core.data.fineract.api.services.RegistrationService;
import org.mifos.mobilewallet.core.data.fineract.api.services.SavingsAccountsService;
import org.mifos.mobilewallet.core.data.fineract.api.services.ThirdPartyTransferService;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by naman on 20/8/17.
 */

public class SelfServiceApiManager {

    public static final String DEFAULT = "default";
    public static final String TENANT_ID = "ibank-usa";
    public static final String CONTENT_TYPE_ID = "application/json";
    private static BaseURL baseUrl = new BaseURL();
    private static final String BASE_URL = baseUrl.getSelfServiceUrl();

    private static Retrofit retrofit;
    private static AuthenticationService authenticationApi;
    private static ClientService clientsApi;
    private static SavingsAccountsService savingAccountsListApi;
    private static RegistrationService registrationAPi;
    private static BeneficiaryService beneficiaryApi;
    private static ThirdPartyTransferService thirdPartyTransferApi;

    public SelfServiceApiManager() {
        String authToken = "";
        createService(authToken);
    }

    public static void createService(String authToken) {
        OkHttpClient okHttpClient = getOkHttpClient(authToken);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        init();
    }

    private static OkHttpClient getOkHttpClient(String authToken) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            return new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(new ApiInterceptor(authToken, TENANT_ID, CONTENT_TYPE_ID))
                    .addInterceptor(interceptor)
                    .sslSocketFactory(sslSocketFactory , (X509TrustManager)trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    private static void init() {
        authenticationApi = createApi(AuthenticationService.class);
        clientsApi = createApi(ClientService.class);
        savingAccountsListApi = createApi(SavingsAccountsService.class);
        registrationAPi = createApi(RegistrationService.class);
        beneficiaryApi = createApi(BeneficiaryService.class);
        thirdPartyTransferApi = createApi(ThirdPartyTransferService.class);
    }

    public AuthenticationService getAuthenticationApi() {
        return authenticationApi;
    }

    public ClientService getClientsApi() {
        return clientsApi;
    }

    public SavingsAccountsService getSavingAccountsListApi() {
        return savingAccountsListApi;
    }

    public RegistrationService getRegistrationAPi() {
        return registrationAPi;
    }

    public BeneficiaryService getBeneficiaryApi() {
        return beneficiaryApi;
    }

    public ThirdPartyTransferService getThirdPartyTransferApi() {
        return thirdPartyTransferApi;
    }

}
