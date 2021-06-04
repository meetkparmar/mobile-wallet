package org.mifos.mobilewallet.core.data.fineract.api.services;

import org.mifos.mobilewallet.core.domain.model.uspf.AddAddressResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.ClientAddress;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateIdentifierRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateIdentifierResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.IdentifierTemplateResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.UploadDocumentResponseBody;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
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

    @POST("clients/{clientId}/identifiers")
    Observable<CreateIdentifierResponseBody> createIdentifier(
            @Body CreateIdentifierRequestBody createIdentifierRequestBody,
            @Path("clientId") int clientId
    );

    @Multipart
    @POST("{entityType}/{entityId}/documents")
    Observable<UploadDocumentResponseBody> uploadDocument(
            @Path("entityType") String entityType,
            @Path("entityId") int clientId,
            @PartMap Map<String, String> map,
            @Part MultipartBody.Part file
            );

    @POST("client/{clientId}/addresses")
    Observable<AddAddressResponseBody> addAddress(
            @Body ClientAddress clientAddress,
            @Path("clientId") int clientId,
            @Query("type") int addressType
            );
}
