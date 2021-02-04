package org.mifos.mobilewallet.mifospay.deposit.presenter;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.domain.model.DepositRequestBody;
import org.mifos.mobilewallet.core.domain.model.DepositResponseBody;
import org.mifos.mobilewallet.core.domain.usecase.account.DepositMoney;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.data.local.LocalRepository;
import org.mifos.mobilewallet.mifospay.deposit.DepositContract;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import javax.inject.Inject;

public class DepositPresenter implements DepositContract.DepositPresenter {

    private final UseCaseHandler mUsecaseHandler;
    private final LocalRepository localRepository;
    @Inject
    DepositMoney depositMoney;
    DepositResponseBody depositResponseBody;

    private DepositContract.DepositView mDepositView;

    @Inject
    public DepositPresenter(UseCaseHandler mUsecaseHandler, LocalRepository localRepository) {
        this.mUsecaseHandler = mUsecaseHandler;
        this.localRepository = localRepository;
    }

    @Override
    public void attachView(BaseView baseView) {
        mDepositView = (DepositContract.DepositView) baseView;
        mDepositView.setPresenter(this);
    }

    @Override
    public void depositMoney(DepositRequestBody depositRequestBody) {
        mUsecaseHandler.execute(depositMoney,
                new DepositMoney.RequestValues(depositRequestBody),
                new UseCase.UseCaseCallback<DepositMoney.ResponseValue>() {
                    @Override
                    public void onSuccess(DepositMoney.ResponseValue response) {
                        depositResponseBody = response.getDepositResponseBody();
                        mDepositView.showDepositMoneyResult(depositResponseBody);
                    }

                    @Override
                    public void onError(String message) {
                        mDepositView.showToast(Constants.ERROR_OCCURRED);
                    }
                });
    }
}
