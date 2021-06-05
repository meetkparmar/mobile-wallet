package org.mifos.mobilewallet.mifospay.home.presenter;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.domain.usecase.client.FetchClientData;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.data.local.LocalRepository;
import org.mifos.mobilewallet.mifospay.data.local.PreferencesHelper;
import org.mifos.mobilewallet.mifospay.home.BaseHomeContract;

import javax.inject.Inject;

/**
 * Created by naman on 17/6/17.
 */

public class MainPresenter implements BaseHomeContract.BaseHomePresenter {

    private final UseCaseHandler mUsecaseHandler;
    private final LocalRepository localRepository;
    private final PreferencesHelper preferencesHelper;
    @Inject
    FetchClientData fetchClientData;
    private BaseHomeContract.BaseHomeView mHomeView;

    @Inject
    public MainPresenter(UseCaseHandler useCaseHandler, LocalRepository localRepository, PreferencesHelper preferencesHelper) {
        this.mUsecaseHandler = useCaseHandler;
        this.localRepository = localRepository;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void attachView(BaseView baseView) {
        mHomeView = (BaseHomeContract.BaseHomeView) baseView;
        mHomeView.setPresenter(this);
    }

    @Override
    public void fetchClientDetails() {
        mUsecaseHandler.execute(fetchClientData,
                new FetchClientData.RequestValues(localRepository.getClientDetails().getClientId()),
                new UseCase.UseCaseCallback<FetchClientData.ResponseValue>() {
                    @Override
                    public void onSuccess(FetchClientData.ResponseValue response) {
                        localRepository.saveClientData(response.getUserDetails());
                        if (!response.getUserDetails().getName().equals("")) {
                            mHomeView.showClientDetails(response.getUserDetails(), preferencesHelper.getLocation());
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
    }

}
