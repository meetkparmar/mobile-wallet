package org.mifos.mobilewallet.mifospay.createuser.presenter;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.domain.model.client.Client;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserResponseBody;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.createuser.contract.CreateClientContract;
import org.mifos.mobilewallet.mifospay.createuser.usecase.CreateClient;
import org.mifos.mobilewallet.mifospay.createuser.usecase.CreateUser;
import org.mifos.mobilewallet.mifospay.data.local.LocalRepository;
import org.mifos.mobilewallet.mifospay.data.local.PreferencesHelper;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import javax.inject.Inject;

public class CreateClientPresenter implements CreateClientContract.CreateClientPresenter {

    @Inject
    CreateClient createClient;

    @Inject
    CreateUser createUser;

    private final UseCaseHandler mUseCaseHandler;
    private final PreferencesHelper mPreferencesHelper;
    private LocalRepository localRepository;
    private CreateClientContract.CreateClientView mCreateClientView;
    CreateClientResponseBody createClientResponseBody;
    CreateUserResponseBody createUserResponseBody;

    @Inject
    public CreateClientPresenter(UseCaseHandler mUseCaseHandler, PreferencesHelper mPreferencesHelper, LocalRepository localRepository) {
        this.mUseCaseHandler = mUseCaseHandler;
        this.mPreferencesHelper = mPreferencesHelper;
        this.localRepository = localRepository;
    }

    @Override
    public void createClient(final CreateClientRequestBody createClientRequestBody, final String name, final String mobileNo) {
        mUseCaseHandler.execute(createClient,
                new CreateClient.RequestValues(createClientRequestBody),
                new UseCase.UseCaseCallback<CreateClient.ResponseValue>() {
                    @Override
                    public void onSuccess(CreateClient.ResponseValue response) {
                        createClientResponseBody = response.getCreateClientResponseBody();

                        Client client = new Client();
                        client.setClientId(createClientResponseBody.getClientId());
                        client.setName(name);
                        client.setDisplayName(name);
                        client.setMobileNo(mobileNo);
                        localRepository.saveClientData(client);
                        mCreateClientView.showCreateClientResult(createClientResponseBody);
                    }

                    @Override
                    public void onError(String message) {
                        mCreateClientView.showToast(Constants.ERROR_OCCURRED);
                    }
                });
    }

    @Override
    public void createUser(CreateUserRequestBody createUserRequestBody) {
        mUseCaseHandler.execute(createUser,
                new CreateUser.RequestValues(createUserRequestBody),
                new UseCase.UseCaseCallback<CreateUser.ResponseValue>() {
                    @Override
                    public void onSuccess(CreateUser.ResponseValue response) {
                        createUserResponseBody = response.getCreateUserResponseBody();
                        mCreateClientView.showCreateUserResult(createUserResponseBody);
                    }

                    @Override
                    public void onError(String message) {
                        mCreateClientView.showToast(Constants.ERROR_OCCURRED);
                    }
                });
    }

    @Override
    public void attachView(BaseView baseView) {
        mCreateClientView = (CreateClientContract.CreateClientView) baseView;
        mCreateClientView.setPresenter(this);
    }

}
