package org.mifos.mobilewallet.mifospay.createuser.presenter;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientResponseBody;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.createuser.contract.CreateClientContract;
import org.mifos.mobilewallet.mifospay.createuser.usecase.CreateClient;
import org.mifos.mobilewallet.mifospay.data.local.PreferencesHelper;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import javax.inject.Inject;

public class CreateClientPresenter implements CreateClientContract.CreateClientPresenter {

    @Inject
    CreateClient createClient;
    private final UseCaseHandler mUseCaseHandler;
    private final PreferencesHelper mPreferencesHelper;
    private CreateClientContract.CreateClientView mCreateClientView;
    CreateClientResponseBody createClientResponseBody;

    @Inject
    public CreateClientPresenter(UseCaseHandler mUseCaseHandler, PreferencesHelper mPreferencesHelper) {
        this.mUseCaseHandler = mUseCaseHandler;
        this.mPreferencesHelper = mPreferencesHelper;
    }

    @Override
    public void createClient(CreateClientRequestBody createClientRequestBody) {
        mUseCaseHandler.execute(createClient,
                new CreateClient.RequestValues(createClientRequestBody),
                new UseCase.UseCaseCallback<CreateClient.ResponseValue>() {
                    @Override
                    public void onSuccess(CreateClient.ResponseValue response) {
                        createClientResponseBody = response.getCreateClientResponseBody();
                        mCreateClientView.showCreateClientResult(createClientResponseBody);
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
