package org.mifos.mobilewallet.mifospay.location.usecase;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.uspf.AddAddressResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.ClientAddress;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddAddress extends UseCase<AddAddress.RequestValues,
        AddAddress.ResponseValue> {

    private final FineractRepository fineractRepository;

    @Inject
    public AddAddress(FineractRepository fineractRepository) {
        this.fineractRepository = fineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        fineractRepository.addAddress(requestValues.clientAddress, requestValues.clientId, requestValues.addressType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddAddressResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(AddAddressResponseBody addAddressResponseBody) {
                        getUseCaseCallback().onSuccess(new AddAddress.ResponseValue(addAddressResponseBody));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private ClientAddress clientAddress;
        private int clientId;
        private int addressType;

        public RequestValues(ClientAddress clientAddress, int clientId, int addressType) {
            this.clientAddress = clientAddress;
            this.clientId = clientId;
            this.addressType = addressType;
        }

        public void setClientAddress(ClientAddress clientAddress) {
            this.clientAddress = clientAddress;
        }

        public void setClientId(int clientId) {
            this.clientId = clientId;
        }

        public void setAddressType(int addressType) {
            this.addressType = addressType;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private AddAddressResponseBody addAddressResponseBody;

        public ResponseValue(AddAddressResponseBody addAddressResponseBody) {
            this.addAddressResponseBody = addAddressResponseBody;
        }

        public AddAddressResponseBody getAddAddressResponseBody() {
            return addAddressResponseBody;
        }
    }
}
