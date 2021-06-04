package org.mifos.mobilewallet.mifospay.location.contract;

import org.mifos.mobilewallet.core.domain.model.uspf.AddAddressResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.ClientAddress;
import org.mifos.mobilewallet.mifospay.base.BasePresenter;
import org.mifos.mobilewallet.mifospay.base.BaseView;

public interface LocationContract {

    interface LocationView extends BaseView<LocationContract.locationPresenter> {

        void showToast(String message);

        void showAddAddressResult(AddAddressResponseBody addAddressResponseBody);
    }

    interface locationPresenter extends BasePresenter {

        void addAddress(ClientAddress clientAddress, int clientId, int addressType);
    }
}
