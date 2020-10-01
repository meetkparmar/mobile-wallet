package org.mifos.mobilewallet.core.domain.usecase.account;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.AccountNameDetails;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FetchAccountName extends UseCase<FetchAccountName.RequestValues,
        FetchAccountName.ResponseValue> {

    private final FineractRepository mFineractRepository;

    @Inject
    public FetchAccountName(FineractRepository fineractRepository) {
        mFineractRepository = fineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        mFineractRepository.getAccountName(requestValues.identifierType, requestValues.identifier)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AccountNameDetails>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(AccountNameDetails accountNameDetails) {
                        getUseCaseCallback().onSuccess(new FetchAccountName.ResponseValue(accountNameDetails));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private String identifierType;
        private String identifier;

        public RequestValues(String identifierType, String identifier) {
            this.identifierType = identifierType;
            this.identifier = identifier;
        }

        public void setIdentifierType(String identifierType) {
            this.identifierType = identifierType;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private AccountNameDetails accountNameDetails;

        public ResponseValue(AccountNameDetails accountNameDetails) {
            this.accountNameDetails = accountNameDetails;
        }

        public AccountNameDetails getAccountNameDetails() {
            return accountNameDetails;
        }
    }

}
