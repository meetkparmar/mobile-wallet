package org.mifos.mobilewallet.core.data.fineract.api;

import android.util.Base64;

import org.mifos.mobilewallet.core.data.fineract.api.services.AccountTransfersService;
import org.mifos.mobilewallet.core.data.fineract.api.services.AuthenticationService;
import org.mifos.mobilewallet.core.data.fineract.api.services.ClientService;
import org.mifos.mobilewallet.core.data.fineract.api.services.DocumentService;
import org.mifos.mobilewallet.core.data.fineract.api.services.InvoiceService;
import org.mifos.mobilewallet.core.data.fineract.api.services.KYCLevel1Service;
import org.mifos.mobilewallet.core.data.fineract.api.services.NotificationService;
import org.mifos.mobilewallet.core.data.fineract.api.services.RegistrationService;
import org.mifos.mobilewallet.core.data.fineract.api.services.RunReportService;
import org.mifos.mobilewallet.core.data.fineract.api.services.SavedCardService;
import org.mifos.mobilewallet.core.data.fineract.api.services.SavingsAccountsService;
import org.mifos.mobilewallet.core.data.fineract.api.services.SearchService;
import org.mifos.mobilewallet.core.data.fineract.api.services.ThirdPartyTransferService;
import org.mifos.mobilewallet.core.data.fineract.api.services.TwoFactorAuthService;
import org.mifos.mobilewallet.core.data.fineract.api.services.UserService;
import org.mifos.mobilewallet.core.utils.Constants;

import java.io.IOException;
import java.nio.charset.Charset;
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

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by naman on 17/6/17.
 */

public class FineractApiManager {

    public static final String DEFAULT = "default";
    public static final String BASIC = "Basic ";
    public static final String TENANT_ID = "ibank-usa";
    public static final String CONTENT_TYPE_ID = "application/json";
    private static BaseURL baseUrl = new BaseURL();
    private static final String BASE_URL = baseUrl.getUrl();

    private static Retrofit retrofit;
    private static AuthenticationService authenticationApi;
    private static ClientService clientsApi;
    private static SavingsAccountsService savingsAccountsApi;
    private static RegistrationService registrationAPi;
    private static SearchService searchApi;
    private static SavedCardService savedCardApi;
    private static DocumentService documentApi;
    private static TwoFactorAuthService twoFactorAuthApi;
    private static AccountTransfersService accountTransfersApi;
    private static RunReportService runReportApi;
    private static KYCLevel1Service kycLevel1Api;
    private static InvoiceService invoiceApi;
    private static UserService userApi;
    private static ThirdPartyTransferService thirdPartyTransferApi;
    private static NotificationService notificationApi;

    private static SelfServiceApiManager sSelfInstance;
    private static IBankPaymentHubApiManager sFinanceInstance;
    private static IBankAMSApiManager IBankAMSApiManager;

    public FineractApiManager() {
        String authToken = BASIC + Base64.encodeToString(Constants.MIFOS_PASSWORD
                        .getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP);
        createService(authToken);

        if (sSelfInstance == null) {
            sSelfInstance = new SelfServiceApiManager();
        }

        if (sFinanceInstance == null) {
            sFinanceInstance = new IBankPaymentHubApiManager();
        }

        if (IBankAMSApiManager == null) {
            IBankAMSApiManager = new IBankAMSApiManager();
        }
    }

    private static void init() {
        authenticationApi = createApi(AuthenticationService.class);
        clientsApi = createApi(ClientService.class);
        savingsAccountsApi = createApi(SavingsAccountsService.class);
        registrationAPi = createApi(RegistrationService.class);
        searchApi = createApi(SearchService.class);
        savedCardApi = createApi(SavedCardService.class);
        documentApi = createApi(DocumentService.class);
        twoFactorAuthApi = createApi(TwoFactorAuthService.class);
        accountTransfersApi = createApi(AccountTransfersService.class);
        runReportApi = createApi(RunReportService.class);
        kycLevel1Api = createApi(KYCLevel1Service.class);
        invoiceApi = createApi(InvoiceService.class);
        userApi = createApi(UserService.class);
        thirdPartyTransferApi = createApi(ThirdPartyTransferService.class);
        notificationApi = createApi(NotificationService.class);
    }

    private static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    public static void createService(String authToken) {
        OkHttpClient okHttpClient = getOkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        init();
    }

    private static OkHttpClient getOkHttpClient() {
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
                    .addInterceptor(new UsPfFinancialApiInterceptor(TENANT_ID, CONTENT_TYPE_ID))
                    .authenticator(new Authenticator() {
                        @Override
                        public Request authenticate(Route route, Response response) throws IOException {
                            String credential = Credentials.basic("mifos", "password");
                            return response.request().newBuilder().header("Authorization", credential).build();
                        }
                    })
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

    public static void createSelfService(String authToken) {
        SelfServiceApiManager.createService(authToken);
    }

    public static SelfServiceApiManager getSelfApiManager() {
        return sSelfInstance;
    }

    public static IBankPaymentHubApiManager getsFinanceInstance() {
        return sFinanceInstance;
    }

    public static IBankAMSApiManager getUsPfFinanceInstance() {
        return IBankAMSApiManager;
    }

    public AuthenticationService getAuthenticationApi() {
        return authenticationApi;
    }

    public ClientService getClientsApi() {
        return clientsApi;
    }

    public SavingsAccountsService getSavingAccountsListApi() {
        return savingsAccountsApi;
    }

    public RegistrationService getRegistrationAPi() {
        return registrationAPi;
    }

    public SearchService getSearchApi() {
        return searchApi;
    }

    public DocumentService getDocumentApi() {
        return documentApi;
    }

    public RunReportService getRunReportApi() {
        return runReportApi;
    }

    public TwoFactorAuthService getTwoFactorAuthApi() {
        return twoFactorAuthApi;
    }

    public AccountTransfersService getAccountTransfersApi() {
        return accountTransfersApi;
    }

    public SavedCardService getSavedCardApi() {
        return savedCardApi;
    }

    public KYCLevel1Service getKycLevel1Api() {
        return kycLevel1Api;
    }

    public InvoiceService getInvoiceApi() {
        return invoiceApi;
    }

    public UserService getUserApi() {
        return userApi;
    }

    public ThirdPartyTransferService getThirdPartyTransferApi() {
        return thirdPartyTransferApi;
    }

    public NotificationService getNotificationApi() {
        return notificationApi;
    }
}
