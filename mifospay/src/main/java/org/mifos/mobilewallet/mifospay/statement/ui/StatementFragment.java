package org.mifos.mobilewallet.mifospay.statement.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mifos.mobilewallet.core.domain.model.Statement;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.base.BaseFragment;
import org.mifos.mobilewallet.mifospay.statement.StatementContract;
import org.mifos.mobilewallet.mifospay.statement.presenter.StatementPresenter;
import org.mifos.mobilewallet.mifospay.statement.ui.adapter.StatementAdapter;
import org.mifos.mobilewallet.mifospay.utils.Constants;
import org.mifos.mobilewallet.mifospay.utils.Toaster;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatementFragment extends BaseFragment implements StatementContract.StatementView {

    @Inject
    StatementAdapter mStatementAdapter;

    @Inject
    StatementPresenter mPresenter;
    StatementContract.StatementPresenter mStatementPresenter;

    @BindView(R.id.cc_statement_container)
    ViewGroup statementContainer;

    @BindView(R.id.inc_state_view)
    View vStateView;

    @BindView(R.id.iv_empty_no_transaction_history)
    ImageView ivTransactionsStateIcon;

    @BindView(R.id.tv_empty_no_transaction_history_title)
    TextView tvTransactionsStateTitle;

    @BindView(R.id.tv_empty_no_transaction_history_subtitle)
    TextView tvTransactionsStateSubtitle;

    @BindView(R.id.rv_statement)
    RecyclerView rvStatement;

    @BindView(R.id.pb_statement)
    ProgressBar pbStatement;

    @Override
    public void setPresenter(StatementContract.StatementPresenter presenter) {
        mStatementPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statement, container, false);
        ButterKnife.bind(this, root);
        mPresenter.attachView(this);

        setupSwipeRefreshLayout();
        setupRecyclerView();
        showSwipeProgress();
        mPresenter.fetchStatement();

        return root;
    }

    private void setupRecyclerView() {
        if (getActivity() != null) {
            mStatementAdapter.setContext(getActivity());
        }
        rvStatement.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStatement.setAdapter(mStatementAdapter);
    }

    private void setupSwipeRefreshLayout() {
        setSwipeEnabled(true);
        getSwipeRefreshLayout().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSwipeRefreshLayout().setRefreshing(false);
                mPresenter.fetchStatement();
            }
        });
    }

    @Override
    public void showStateView(int drawable, int title, int subtitle) {
        setSwipeEnabled(false);
        hideSwipeProgress();

        TransitionManager.beginDelayedTransition(statementContainer);
        rvStatement.setVisibility(View.GONE);
        pbStatement.setVisibility(View.GONE);
        vStateView.setVisibility(View.VISIBLE);
        if (getActivity() != null) {
            Resources res = getResources();
            ivTransactionsStateIcon
                    .setImageDrawable(res.getDrawable(drawable));
            tvTransactionsStateTitle
                    .setText(res.getString(title));
            tvTransactionsStateSubtitle
                    .setText(res.getString(subtitle));
        }
    }

    @Override
    public void showStatements(List<Statement> statements) {
        setSwipeEnabled(false);
        hideSwipeProgress();

        if (statements == null) {
            Toaster.showToast(getContext(), Constants.ERROR_FETCHING_TRANSACTION_DETAILS);
        } else {
            int statementSize = statements.size();
            if (statementSize > 0) {
                showRecyclerView();
                mStatementAdapter.setData(statements);
            } else {
                Toaster.showToast(getContext(), Constants.ERROR_FETCHING_TRANSACTION_DETAILS);
            }
        }

    }

    @Override
    public void showRecyclerView() {
        TransitionManager.beginDelayedTransition(statementContainer);
        vStateView.setVisibility(View.GONE);
        pbStatement.setVisibility(View.GONE);
        rvStatement.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStatementFetchingProgress() {
        TransitionManager.beginDelayedTransition(statementContainer);
        vStateView.setVisibility(View.GONE);
        rvStatement.setVisibility(View.GONE);
        pbStatement.setVisibility(View.VISIBLE);
    }


}

