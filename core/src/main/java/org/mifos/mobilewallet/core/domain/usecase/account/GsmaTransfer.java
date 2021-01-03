package org.mifos.mobilewallet.core.domain.usecase.account;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.gsma.IntTransferRequestBody;
import org.mifos.mobilewallet.core.domain.model.gsma.IntTransferResponseBody;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GsmaTransfer extends UseCase<GsmaTransfer.RequestValues,
        GsmaTransfer.ResponseValue> {

    private final FineractRepository mFineractRepository;

    @Inject
    public GsmaTransfer(FineractRepository mFineractRepository) {
        this.mFineractRepository = mFineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mFineractRepository.gsmaTransfer(requestValues.intTransferRequestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<IntTransferResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(IntTransferResponseBody intTransferResponseBody) {
                        getUseCaseCallback().onSuccess(new GsmaTransfer.ResponseValue(intTransferResponseBody));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private IntTransferRequestBody intTransferRequestBody;

        public RequestValues(IntTransferRequestBody intTransferRequestBody) {
            this.intTransferRequestBody = intTransferRequestBody;
        }

        public void setIntTransferRequestBody(IntTransferRequestBody intTransferRequestBody) {
            this.intTransferRequestBody = intTransferRequestBody;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private IntTransferResponseBody intTransferResponseBody;

        public ResponseValue(IntTransferResponseBody intTransferResponseBody) {
            this.intTransferResponseBody = intTransferResponseBody;
        }

        public IntTransferResponseBody getIntTransferResponseBody() {
            return intTransferResponseBody;
        }
    }
}
