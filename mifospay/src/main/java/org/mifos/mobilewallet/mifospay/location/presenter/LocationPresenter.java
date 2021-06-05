package org.mifos.mobilewallet.mifospay.location.presenter;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.domain.model.uspf.AddAddressResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.ClientAddress;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.data.local.LocalRepository;
import org.mifos.mobilewallet.mifospay.data.local.PreferencesHelper;
import org.mifos.mobilewallet.mifospay.location.contract.LocationContract;
import org.mifos.mobilewallet.mifospay.location.usecase.AddAddress;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import javax.inject.Inject;

public class LocationPresenter implements LocationContract.locationPresenter {

    @Inject
    AddAddress addAddress;

    private final UseCaseHandler mUseCaseHandler;
    private final PreferencesHelper preferencesHelper;
    private LocalRepository localRepository;
    private LocationContract.LocationView locationView;
    AddAddressResponseBody addAddressResponseBody;

    @Inject
    public LocationPresenter(UseCaseHandler mUseCaseHandler, PreferencesHelper preferencesHelper, LocalRepository localRepository) {
        this.mUseCaseHandler = mUseCaseHandler;
        this.preferencesHelper = preferencesHelper;
        this.localRepository = localRepository;
    }

    @Override
    public void addAddress(ClientAddress clientAddress, int clientId, int addressType) {
        clientId = (int) localRepository.getClientDetails().getClientId();
        mUseCaseHandler.execute(addAddress,
                new AddAddress.RequestValues(clientAddress, clientId, addressType),
                new UseCase.UseCaseCallback<AddAddress.ResponseValue>() {
                    @Override
                    public void onSuccess(AddAddress.ResponseValue response) {
                        preferencesHelper.setLocation(true);
                        addAddressResponseBody = response.getAddAddressResponseBody();
                        locationView.showAddAddressResult(addAddressResponseBody);
                    }
                    @Override
                    public void onError(String message) {
                        locationView.showToast(Constants.ERROR_OCCURRED);
                    }
                });
    }

    @Override
    public void attachView(BaseView baseView) {
        locationView = (LocationContract.LocationView) baseView;
        locationView.setPresenter(this);
    }
}
