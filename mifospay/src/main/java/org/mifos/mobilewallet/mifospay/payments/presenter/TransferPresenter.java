package org.mifos.mobilewallet.mifospay.payments.presenter;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.domain.model.AccountNameDetails;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionRequestBody;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionResponseBody;
import org.mifos.mobilewallet.core.domain.usecase.account.CurrencyConvert;
import org.mifos.mobilewallet.core.domain.usecase.account.FetchAccount;
import org.mifos.mobilewallet.core.domain.usecase.account.FetchAccountName;
import org.mifos.mobilewallet.core.domain.usecase.client.FetchClientData;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.data.local.LocalRepository;
import org.mifos.mobilewallet.mifospay.home.BaseHomeContract;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import javax.inject.Inject;

/**
 * Created by naman on 30/8/17.
 */

public class TransferPresenter implements BaseHomeContract.TransferPresenter {

    private final UseCaseHandler mUsecaseHandler;
    private final LocalRepository localRepository;
    @Inject
    FetchClientData fetchClientData;
    @Inject
    FetchAccount mFetchAccount;
    @Inject
    FetchAccountName mFetchAccountName;
    AccountNameDetails accountNameDetails;

    @Inject
    CurrencyConvert currencyConvert;
    CurrencyConversionResponseBody currencyConversionResponseBody;

    private BaseHomeContract.TransferView mTransferView;

    @Inject
    public TransferPresenter(UseCaseHandler useCaseHandler, LocalRepository localRepository) {
        this.mUsecaseHandler = useCaseHandler;
        this.localRepository = localRepository;
    }

    @Override
    public void attachView(BaseView baseView) {
        mTransferView = (BaseHomeContract.TransferView) baseView;
        mTransferView.setPresenter(this);
    }

    @Override
    public void fetchVpa() {
        mTransferView.showVpa(localRepository.getClientDetails().getExternalId());
    }

    @Override
    public void fetchMobile() {
        mTransferView.showMobile(localRepository.getPreferencesHelper().getMobile());
    }

    @Override
    public boolean checkSelfTransfer(String externalId) {
        return (externalId.equals(localRepository.getClientDetails().getExternalId()));
    }

    @Override
    public void checkBalanceAvailability(final String externalId, final double transferAmount) {
        mUsecaseHandler.execute(mFetchAccount,
                new FetchAccount.RequestValues(localRepository.getClientDetails().getClientId()),
                new UseCase.UseCaseCallback<FetchAccount.ResponseValue>() {
                    @Override
                    public void onSuccess(FetchAccount.ResponseValue response) {
                        mTransferView.hideSwipeProgress();
                        if (transferAmount > response.getAccount().getBalance()) {
                            mTransferView.showSnackbar(Constants.INSUFFICIENT_BALANCE);
                        } else {
                            mTransferView.showClientDetails(externalId, transferAmount);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        mTransferView.hideSwipeProgress();
                        mTransferView.showToast(Constants.ERROR_FETCHING_BALANCE);
                    }
                });
    }

    @Override
    public void currencyConvert(CurrencyConversionRequestBody currencyConversionRequestBody) {
        mUsecaseHandler.execute(currencyConvert,
                new CurrencyConvert.RequestValues(currencyConversionRequestBody),
                new UseCase.UseCaseCallback<CurrencyConvert.ResponseValue>() {
                    @Override
                    public void onSuccess(CurrencyConvert.ResponseValue response) {
                        currencyConversionResponseBody = response.getCurrencyConversionResponseBody();
                        mTransferView.showCurrencyConversionDetails(currencyConversionResponseBody);
                    }

                    @Override
                    public void onError(String message) {
                        mTransferView.showToast(Constants.UNABLE_TO_CONVERT_CURRENCY);
                    }
                });
    }

    @Override
    public void getAccountName(String identifierType, String identifier) {
        mUsecaseHandler.execute(mFetchAccountName,
                new FetchAccountName.RequestValues(identifierType, identifier),
                new UseCase.UseCaseCallback<FetchAccountName.ResponseValue>() {
                    @Override
                    public void onSuccess(FetchAccountName.ResponseValue response) {
                        accountNameDetails = response.getAccountNameDetails();
                        mTransferView.showAccountName(accountNameDetails);
                    }

                    @Override
                    public void onError(String message) {
                        mTransferView.showToast(Constants.ERROR_FETCHING_ACCOUNT_NAME);
                    }
                });
    }
}
