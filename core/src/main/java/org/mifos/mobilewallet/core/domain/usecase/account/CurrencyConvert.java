package org.mifos.mobilewallet.core.domain.usecase.account;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionRequestBody;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionResponseBody;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CurrencyConvert extends UseCase<CurrencyConvert.RequestValues,
        CurrencyConvert.ResponseValue> {

    private final FineractRepository mFineractRepository;

    @Inject
    public CurrencyConvert(FineractRepository fineractRepository) {
        this.mFineractRepository = fineractRepository;
    }


    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mFineractRepository.currencyConvert(requestValues.currencyConversionRequestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CurrencyConversionResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(CurrencyConversionResponseBody currencyConversionResponseBody) {
                        getUseCaseCallback().onSuccess(new CurrencyConvert.ResponseValue(currencyConversionResponseBody));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private CurrencyConversionRequestBody currencyConversionRequestBody;

        public RequestValues(CurrencyConversionRequestBody currencyConversionRequestBody) {
            this.currencyConversionRequestBody = currencyConversionRequestBody;
        }

        public void setCurrencyConversionRequestBody(CurrencyConversionRequestBody currencyConversionRequestBody) {
            this.currencyConversionRequestBody = currencyConversionRequestBody;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private CurrencyConversionResponseBody currencyConversionResponseBody;

        public ResponseValue(CurrencyConversionResponseBody currencyConversionResponseBody) {
            this.currencyConversionResponseBody = currencyConversionResponseBody;
        }

        public CurrencyConversionResponseBody getCurrencyConversionResponseBody() {
            return currencyConversionResponseBody;
        }
    }
}
