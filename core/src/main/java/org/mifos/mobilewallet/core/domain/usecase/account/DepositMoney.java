package org.mifos.mobilewallet.core.domain.usecase.account;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionRequestBody;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionResponseBody;
import org.mifos.mobilewallet.core.domain.model.DepositRequestBody;
import org.mifos.mobilewallet.core.domain.model.DepositResponseBody;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DepositMoney extends UseCase<DepositMoney.RequestValues,
        DepositMoney.ResponseValue> {

    private final FineractRepository mFineractRepository;

    @Inject
    public DepositMoney(FineractRepository fineractRepository) {
        this.mFineractRepository = fineractRepository;
    }


    @Override
    protected void executeUseCase(DepositMoney.RequestValues requestValues) {
        mFineractRepository.depositMoney(requestValues.depositRequestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DepositResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(DepositResponseBody depositResponseBody) {
                        getUseCaseCallback().onSuccess(new DepositMoney.ResponseValue(depositResponseBody));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private DepositRequestBody depositRequestBody;

        public RequestValues(DepositRequestBody depositRequestBody) {
            this.depositRequestBody = depositRequestBody;
        }

        public void setDepositRequestBody(DepositRequestBody depositRequestBody) {
            this.depositRequestBody = depositRequestBody;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private DepositResponseBody depositResponseBody;

        public ResponseValue(DepositResponseBody depositResponseBody) {
            this.depositResponseBody = depositResponseBody;
        }

        public DepositResponseBody getDepositResponseBody() {
            return depositResponseBody;
        }
    }
}
