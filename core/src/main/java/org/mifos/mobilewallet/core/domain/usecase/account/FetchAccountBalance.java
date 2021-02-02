package org.mifos.mobilewallet.core.domain.usecase.account;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.AccountBalance;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FetchAccountBalance extends UseCase<FetchAccountBalance.RequestValues,
        FetchAccountBalance.ResponseValue> {

    private final FineractRepository fineractRepository;

    @Inject
    public FetchAccountBalance(FineractRepository fineractRepository) {
        this.fineractRepository = fineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        fineractRepository.getAccountBalance(requestValues.mobileNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AccountBalance>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(AccountBalance accountBalance) {
                        getUseCaseCallback().onSuccess(new FetchAccountBalance.ResponseValue(accountBalance));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private String mobileNo;

        public RequestValues(String mobileNo) {
            this.mobileNo = mobileNo;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final AccountBalance accountBalance;

        public ResponseValue(AccountBalance accountBalance) {
            this.accountBalance = accountBalance;
        }

        public AccountBalance getAccountBalance() {
            return accountBalance;
        }
    }
}
