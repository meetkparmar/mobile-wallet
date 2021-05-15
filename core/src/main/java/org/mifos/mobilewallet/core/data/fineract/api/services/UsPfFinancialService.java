package org.mifos.mobilewallet.core.data.fineract.api.services;

import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.IdentifierTemplateResponseBody;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface UsPfFinancialService {

    @POST("clients")
    Observable<CreateClientResponseBody> createClient(
            @Body CreateClientRequestBody createClientRequestBody
    );

    @POST("users")
    Observable<CreateUserResponseBody> createUser(
            @Body CreateUserRequestBody createUserRequestBody
    );

    @GET("clients/{clientId}/identifiers/template")
    Observable<IdentifierTemplateResponseBody> fetchIdentifierTemplate(
            @Path("clientId") int clientId
    );
}
