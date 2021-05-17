package org.mifos.mobilewallet.mifospay.createuser.contract;

import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserResponseBody;
import org.mifos.mobilewallet.mifospay.base.BasePresenter;
import org.mifos.mobilewallet.mifospay.base.BaseView;

public interface CreateClientContract {

    interface CreateClientView extends BaseView<CreateClientContract.CreateClientPresenter> {

        void showToast(String message);

        void showCreateClientResult(CreateClientResponseBody createClientResponseBody);

        void showCreateUserResult(CreateUserResponseBody createUserResponseBody);
    }

    interface CreateClientPresenter extends BasePresenter {

        void createClient(CreateClientRequestBody createClientRequestBody, String name, String mobileNo);

        void createUser(CreateUserRequestBody createUserRequestBody);
    }
}
