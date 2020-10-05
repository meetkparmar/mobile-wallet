package org.mifos.mobilewallet.core.data.fineract.api.services;

import org.mifos.mobilewallet.core.domain.model.AccountNameDetails;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionRequestBody;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionResponseBody;
import org.mifos.mobilewallet.core.domain.usecase.client.CreateClient;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface FinancialService {

    @GET("accounts/{identifierType}/{identifier}/accountname")
    Observable<AccountNameDetails> getAccountName(
            @Path("identifierType") String identifierType,
            @Path("identifier") String identifier
    );

    @POST("imuConversion/preview")
    Observable<CurrencyConversionResponseBody> currencyConvert(
            @Body CurrencyConversionRequestBody currencyConversionRequestBody
    );
}
