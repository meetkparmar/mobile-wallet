package org.mifos.mobilewallet.mifospay.deposit;

import org.mifos.mobilewallet.core.domain.model.DepositRequestBody;
import org.mifos.mobilewallet.core.domain.model.DepositResponseBody;
import org.mifos.mobilewallet.mifospay.base.BasePresenter;
import org.mifos.mobilewallet.mifospay.base.BaseView;

public interface DepositContract {

    interface DepositView extends BaseView<DepositContract.DepositPresenter> {

        void showSnackbar(String message);

        void showToast(String message);

        void showDepositMoneyResult(DepositResponseBody depositResponseBody);
    }

    interface DepositPresenter extends BasePresenter {

        void depositMoney(DepositRequestBody depositRequestBody);
    }
}
