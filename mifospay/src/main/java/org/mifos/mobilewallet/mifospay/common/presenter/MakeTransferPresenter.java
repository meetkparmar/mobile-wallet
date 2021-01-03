package org.mifos.mobilewallet.mifospay.common.presenter;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.domain.model.SearchResult;
import org.mifos.mobilewallet.core.domain.model.gsma.GsmaRequestStateResponseBody;
import org.mifos.mobilewallet.core.domain.model.gsma.IntTransferRequestBody;
import org.mifos.mobilewallet.core.domain.model.gsma.IntTransferResponseBody;
import org.mifos.mobilewallet.core.domain.usecase.account.GsmaRequestState;
import org.mifos.mobilewallet.core.domain.usecase.account.GsmaTransfer;
import org.mifos.mobilewallet.core.domain.usecase.account.TransferFunds;
import org.mifos.mobilewallet.core.domain.usecase.client.SearchClient;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.common.TransferContract;
import org.mifos.mobilewallet.mifospay.data.local.LocalRepository;

import javax.inject.Inject;

/**
 * Created by naman on 30/8/17.
 */

public class MakeTransferPresenter implements TransferContract.TransferPresenter {

    private final UseCaseHandler mUsecaseHandler;
    private final LocalRepository localRepository;
    @Inject
    TransferFunds transferFunds;
    @Inject
    SearchClient searchClient;
    @Inject
    GsmaTransfer gsmaTransfer;
    @Inject
    GsmaRequestState gsmaRequestState;
    private TransferContract.TransferView mTransferView;
    IntTransferResponseBody intTransferResponseBody;
    GsmaRequestStateResponseBody gsmaRequestStateResponseBody;

    @Inject
    public MakeTransferPresenter(UseCaseHandler useCaseHandler, LocalRepository localRepository) {
        this.mUsecaseHandler = useCaseHandler;
        this.localRepository = localRepository;
    }

    @Override
    public void attachView(BaseView baseView) {
        mTransferView = (TransferContract.TransferView) baseView;
        mTransferView.setPresenter(this);
    }

    @Override
    public void fetchClient(final String externalId) {
        mUsecaseHandler.execute(searchClient, new SearchClient.RequestValues(externalId),
                new UseCase.UseCaseCallback<SearchClient.ResponseValue>() {
                    @Override
                    public void onSuccess(SearchClient.ResponseValue response) {
                        SearchResult searchResult = response.getResults().get(0);
                        mTransferView.showToClientDetails(searchResult.getResultId(),
                                searchResult.getResultName(), externalId);
                    }

                    @Override
                    public void onError(String message) {
                        mTransferView.showVpaNotFoundSnackbar();
                    }
                });
    }

    @Override
    public void makeTransfer(long fromClientId, long toClientId, double amount) {
        mTransferView.enableDragging(false);
        mUsecaseHandler.execute(transferFunds,
                new TransferFunds.RequestValues(fromClientId, toClientId, amount),
                new UseCase.UseCaseCallback<TransferFunds.ResponseValue>() {
                    @Override
                    public void onSuccess(TransferFunds.ResponseValue response) {
                        mTransferView.enableDragging(true);
                        mTransferView.transferSuccess();
                    }

                    @Override
                    public void onError(String message) {
                        mTransferView.enableDragging(true);
                        mTransferView.transferFailure();
                    }
                });
    }

    @Override
    public void intTransfer(IntTransferRequestBody intTransferRequestBody) {

        mUsecaseHandler.execute(gsmaTransfer,
                new GsmaTransfer.RequestValues(intTransferRequestBody),
                new UseCase.UseCaseCallback<GsmaTransfer.ResponseValue>() {
                    @Override
                    public void onSuccess(GsmaTransfer.ResponseValue response) {
                        intTransferResponseBody = response.getIntTransferResponseBody();
                        mTransferView.gsmaTransferResponse(intTransferResponseBody);
                    }

                    @Override
                    public void onError(String message) {
                        mTransferView.transferFailure();
                    }
                });
    }

    @Override
    public void getRequestState(String key) {
        mUsecaseHandler.execute(gsmaRequestState,
                new GsmaRequestState.RequestValues(key),
                new UseCase.UseCaseCallback<GsmaRequestState.ResponseValue>() {
                    @Override
                    public void onSuccess(GsmaRequestState.ResponseValue response) {
                        mTransferView.enableDragging(true);
                        mTransferView.transferSuccess();
                    }

                    @Override
                    public void onError(String message) {
                        mTransferView.transferFailure();
                    }
                });
    }
}

