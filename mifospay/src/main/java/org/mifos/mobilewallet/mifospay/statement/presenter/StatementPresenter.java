package org.mifos.mobilewallet.mifospay.statement.presenter;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.domain.model.Statement;
import org.mifos.mobilewallet.core.domain.usecase.account.FetchStatement;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.data.local.PreferencesHelper;
import org.mifos.mobilewallet.mifospay.statement.StatementContract;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import java.util.List;

import javax.inject.Inject;

public class StatementPresenter implements StatementContract.StatementPresenter {

    @Inject
    protected FetchStatement mFetchAccountUseCase;
    private final UseCaseHandler mUseCaseHandler;
    private final PreferencesHelper mPreferencesHelper;
    private StatementContract.StatementView mStatementView;
    private List<Statement> statements;

    @Inject
    public StatementPresenter(UseCaseHandler mUseCaseHandler, PreferencesHelper mPreferencesHelper) {
        this.mUseCaseHandler = mUseCaseHandler;
        this.mPreferencesHelper = mPreferencesHelper;
    }

    @Override
    public void attachView(BaseView baseView) {
        mStatementView = (StatementContract.StatementView) baseView;
        mStatementView.setPresenter(this);
    }

    @Override
    public void fetchStatement() {
        mStatementView.showStatementFetchingProgress();
        mUseCaseHandler.execute(mFetchAccountUseCase,
                new FetchStatement.RequestValues(mPreferencesHelper.getMobile()),
                new UseCase.UseCaseCallback<FetchStatement.ResponseValue>() {
                    @Override
                    public void onSuccess(FetchStatement.ResponseValue response) {
                        statements = response.getStatement();
                        mStatementView.showStatements(statements);
                    }

                    @Override
                    public void onError(String message) {
                        showErrorStateView();
                    }
                });
    }

    private void showErrorStateView() {
        mStatementView.showStateView(R.drawable.ic_error_state, R.string.error_oops,
                R.string.error_no_statement_subtitle);
    }
}
