package org.mifos.mobilewallet.mifospay.statement;

import org.mifos.mobilewallet.core.domain.model.Statement;
import org.mifos.mobilewallet.mifospay.base.BasePresenter;
import org.mifos.mobilewallet.mifospay.base.BaseView;

import java.util.List;

public interface StatementContract {

    interface StatementView extends BaseView<StatementContract.StatementPresenter> {

        void showRecyclerView();

        void showStateView(int drawable, int title, int subtitle);

        void showStatements(List<Statement> statements);

        void showStatementFetchingProgress();
    }

    interface StatementPresenter extends BasePresenter {

        void fetchStatement();
    }
}
