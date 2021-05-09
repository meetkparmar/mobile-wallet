package org.mifos.mobilewallet.mifospay.createuser.usecase;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientResponseBody;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateClient extends UseCase<CreateClient.RequestValues,
        CreateClient.ResponseValue> {

    private final FineractRepository fineractRepository;

    @Inject
    public CreateClient(FineractRepository fineractRepository) {
        this.fineractRepository = fineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        fineractRepository.createClient(requestValues.createClientRequestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CreateClientResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(CreateClientResponseBody createClientResponseBody) {
                        getUseCaseCallback().onSuccess(new CreateClient.ResponseValue(createClientResponseBody));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private CreateClientRequestBody createClientRequestBody;

        public RequestValues(CreateClientRequestBody createClientRequestBody) {
            this.createClientRequestBody = createClientRequestBody;
        }

        public void setDepositRequestBody(CreateClientRequestBody createClientRequestBody) {
            this.createClientRequestBody = createClientRequestBody;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private CreateClientResponseBody createClientResponseBody;

        public ResponseValue(CreateClientResponseBody createClientResponseBody) {
            this.createClientResponseBody = createClientResponseBody;
        }

        public CreateClientResponseBody getCreateClientResponseBody() {
            return createClientResponseBody;
        }
    }
}
