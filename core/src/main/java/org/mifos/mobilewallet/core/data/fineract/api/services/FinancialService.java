package org.mifos.mobilewallet.core.data.fineract.api.services;

import org.mifos.mobilewallet.core.domain.model.AccountBalance;
import org.mifos.mobilewallet.core.domain.model.AccountNameDetails;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionRequestBody;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionResponseBody;
import org.mifos.mobilewallet.core.domain.model.DepositRequestBody;
import org.mifos.mobilewallet.core.domain.model.DepositResponseBody;
import org.mifos.mobilewallet.core.domain.model.Statement;
import org.mifos.mobilewallet.core.domain.model.gsma.GsmaRequestStateResponseBody;
import org.mifos.mobilewallet.core.domain.model.gsma.IntTransferRequestBody;
import org.mifos.mobilewallet.core.domain.model.gsma.IntTransferResponseBody;

import java.util.List;

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

    @GET("accounts/MSISDN/{mobileNo}/balance")
    Observable<AccountBalance> getAccountBalance(
            @Path("mobileNo") String mobileNo
    );

    @GET("gsma/accounts/MSISDN/{mobileNo}/statemententries")
    Observable<List<Statement>> getStatements(
            @Path("mobileNo") String mobileNo
    );

    @POST("deposit")
    Observable<DepositResponseBody> depositMoney(
            @Body DepositRequestBody depositRequestBody
    );

    @POST("imuConversion/preview")
    Observable<CurrencyConversionResponseBody> currencyConvert(
            @Body CurrencyConversionRequestBody currencyConversionRequestBody
    );

    @POST("gsma/inttransfer")
    Observable<IntTransferResponseBody> gsmaTransfer(
            @Body IntTransferRequestBody intTransferRequestBody
    );

    @GET("requeststates/{key}")
    Observable<GsmaRequestStateResponseBody> gsmaRequestState(
            @Path("key") String key
    );
}
