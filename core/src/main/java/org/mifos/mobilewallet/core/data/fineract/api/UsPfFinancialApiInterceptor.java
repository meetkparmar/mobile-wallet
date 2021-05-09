package org.mifos.mobilewallet.core.data.fineract.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UsPfFinancialApiInterceptor implements Interceptor {

    public static final String HEADER_TENANT = "Fineract-Platform-TenantId";
    public static final String CONTENT_TYPE = "Content-Type";
    private String headerTenant;
    private String contentType;

    public UsPfFinancialApiInterceptor(String headerTenant, String contentType) {
        this.headerTenant = headerTenant;
        this.contentType = contentType;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request chainRequest = chain.request();
        Request.Builder builder = chainRequest.newBuilder()
                .header(HEADER_TENANT, headerTenant)
                .header(CONTENT_TYPE, contentType);

        Request request = builder.build();
        return chain.proceed(request);
    }
}
