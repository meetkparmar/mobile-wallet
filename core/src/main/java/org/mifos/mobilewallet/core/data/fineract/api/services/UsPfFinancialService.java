package org.mifos.mobilewallet.core.data.fineract.api.services;

import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientResponseBody;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface UsPfFinancialService {

    @POST("clients")
    Observable<CreateClientResponseBody> createClient(
            @Body CreateClientRequestBody createClientRequestBody
    );
}
