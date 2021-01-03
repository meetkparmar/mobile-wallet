package org.mifos.mobilewallet.core.domain.usecase.account;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.gsma.GsmaRequestStateResponseBody;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GsmaRequestState extends UseCase<GsmaRequestState.RequestValues,
        GsmaRequestState.ResponseValue> {

    private final FineractRepository mFineractRepository;

    @Inject
    public GsmaRequestState(FineractRepository mFineractRepository) {
        this.mFineractRepository = mFineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mFineractRepository.gsmaRequestState(requestValues.key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GsmaRequestStateResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(GsmaRequestStateResponseBody gsmaRequestStateResponseBody) {
                        getUseCaseCallback().onSuccess(new GsmaRequestState.ResponseValue(gsmaRequestStateResponseBody));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private String key;

        public RequestValues(String key) {
            this.key = key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private GsmaRequestStateResponseBody gsmaRequestStateResponseBody;

        public ResponseValue(GsmaRequestStateResponseBody gsmaRequestStateResponseBody) {
            this.gsmaRequestStateResponseBody = gsmaRequestStateResponseBody;
        }

        public GsmaRequestStateResponseBody getGsmaRequestStateResponseBody() {
            return gsmaRequestStateResponseBody;
        }
    }
}
