package org.mifos.mobilewallet.core.data.fineract.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class FinancialApiInterceptor implements Interceptor {

    public static final String HEADER_TENANT = "Platform-TenantId";
    private String headerTenant;

    public FinancialApiInterceptor(String headerTenant) {
        this.headerTenant = headerTenant;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request chainRequest = chain.request();
        Request.Builder builder = chainRequest.newBuilder()
                .header(HEADER_TENANT, headerTenant);

        Request request = builder.build();
        return chain.proceed(request);
    }
}
