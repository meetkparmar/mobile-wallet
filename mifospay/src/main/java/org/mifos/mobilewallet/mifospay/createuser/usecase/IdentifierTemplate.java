package org.mifos.mobilewallet.mifospay.createuser.usecase;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.uspf.IdentifierTemplateResponseBody;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IdentifierTemplate extends UseCase<IdentifierTemplate.RequestValues,
        IdentifierTemplate.ResponseValue> {

    private final FineractRepository fineractRepository;

    @Inject
    public IdentifierTemplate(FineractRepository fineractRepository) {
        this.fineractRepository = fineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        fineractRepository.fetchIdentifierTemplate(requestValues.clientId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<IdentifierTemplateResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(IdentifierTemplateResponseBody identifierTemplateResponseBody) {
                        getUseCaseCallback().onSuccess(new IdentifierTemplate.ResponseValue(identifierTemplateResponseBody));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private int clientId;

        public RequestValues(int clientId) {
            this.clientId = clientId;
        }

        public void setClientId(int clientId) {
            this.clientId = clientId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private IdentifierTemplateResponseBody identifierTemplateResponseBody;

        public ResponseValue(IdentifierTemplateResponseBody identifierTemplateResponseBody) {
            this.identifierTemplateResponseBody = identifierTemplateResponseBody;
        }

        public IdentifierTemplateResponseBody getIdentifierTemplateResponseBody() {
            return identifierTemplateResponseBody;
        }
    }

}
