package org.mifos.mobilewallet.core.domain.usecase.account;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.Statement;
import org.mifos.mobilewallet.core.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FetchStatement extends UseCase<FetchStatement.RequestValues,
        FetchStatement.ResponseValue> {

    private final FineractRepository fineractRepository;

    @Inject
    public FetchStatement(FineractRepository fineractRepository) {
        this.fineractRepository = fineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        fineractRepository.getStatement(requestValues.mobileNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Statement>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(Constants.ERROR_FETCHING_ACCOUNTS);
                    }

                    @Override
                    public void onNext(List<Statement> statement) {
                        getUseCaseCallback().onSuccess(new FetchStatement.ResponseValue(statement));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final String mobileNo;

        public RequestValues(String mobileNo) {
            this.mobileNo = mobileNo;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private List<Statement> statement;

        public ResponseValue(List<Statement> statement) {
            this.statement = statement;
        }

        public List<Statement> getStatement() {
            return statement;
        }
    }
}
