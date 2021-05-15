package org.mifos.mobilewallet.mifospay.createuser.usecase;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserResponseBody;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateUser extends UseCase<CreateUser.RequestValues,
        CreateUser.ResponseValue> {

    private final FineractRepository fineractRepository;

    @Inject
    public CreateUser(FineractRepository fineractRepository) {
        this.fineractRepository = fineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        fineractRepository.createUserForUsPf(requestValues.createUserRequestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CreateUserResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(CreateUserResponseBody createUserResponseBody) {
                        getUseCaseCallback().onSuccess(new CreateUser.ResponseValue(createUserResponseBody));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private CreateUserRequestBody createUserRequestBody;

        public RequestValues(CreateUserRequestBody createUserRequestBody) {
            this.createUserRequestBody = createUserRequestBody;
        }

        public void setDepositRequestBody(CreateUserRequestBody createUserRequestBody) {
            this.createUserRequestBody = createUserRequestBody;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private CreateUserResponseBody createUserResponseBody;

        public ResponseValue(CreateUserResponseBody createUserResponseBody) {
            this.createUserResponseBody = createUserResponseBody;
        }

        public CreateUserResponseBody getCreateUserResponseBody() {
            return createUserResponseBody;
        }
    }
}
