package org.mifos.mobilewallet.mifospay.createuser.usecase;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateIdentifierRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateIdentifierResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.IdentifierTemplateResponseBody;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateIdentifier extends UseCase<CreateIdentifier.RequestValues,
        CreateIdentifier.ResponseValue> {

    private final FineractRepository fineractRepository;

    @Inject
    public CreateIdentifier(FineractRepository fineractRepository) {
        this.fineractRepository = fineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        fineractRepository.createIdentifier(requestValues.createIdentifierRequestBody, requestValues.clientId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CreateIdentifierResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(CreateIdentifierResponseBody createIdentifierResponseBody) {
                        getUseCaseCallback().onSuccess(new CreateIdentifier.ResponseValue(createIdentifierResponseBody));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private CreateIdentifierRequestBody createIdentifierRequestBody;
        private int clientId;

        public RequestValues(CreateIdentifierRequestBody createIdentifierRequestBody, int clientId) {
            this.createIdentifierRequestBody = createIdentifierRequestBody;
            this.clientId = clientId;
        }

        public void setClientId(CreateIdentifierRequestBody createIdentifierRequestBody, int clientId) {
            this.createIdentifierRequestBody = createIdentifierRequestBody;
            this.clientId = clientId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private CreateIdentifierResponseBody createIdentifierResponseBody;

        public ResponseValue(CreateIdentifierResponseBody createIdentifierResponseBody) {
            this.createIdentifierResponseBody = createIdentifierResponseBody;
        }

        public CreateIdentifierResponseBody getCreateIdentifierResponseBody() {
            return createIdentifierResponseBody;
        }
    }
}
