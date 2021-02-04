package org.mifos.mobilewallet.mifospay.deposit.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import org.mifos.mobilewallet.core.domain.model.DepositRequestBody;
import org.mifos.mobilewallet.core.domain.model.DepositResponseBody;
import org.mifos.mobilewallet.core.domain.model.gsma.CreditParty;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.base.BaseFragment;
import org.mifos.mobilewallet.mifospay.deposit.DepositContract;
import org.mifos.mobilewallet.mifospay.deposit.presenter.DepositPresenter;
import org.mifos.mobilewallet.mifospay.utils.Constants;
import org.mifos.mobilewallet.mifospay.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DepositFragment extends BaseFragment implements DepositContract.DepositView {

    @Inject
    DepositPresenter mPresenter;
    DepositContract.DepositPresenter mDepositPresenter;

    @BindView(R.id.deposit_money_layout)
    ConstraintLayout mDepositMoneyLayout;
    @BindView(R.id.result_layout)
    ConstraintLayout mResultLayout;
    @BindView(R.id.country_code_picker)
    CountryCodePicker mCountryCodePicker;
    @BindView(R.id.et_mobile_number)
    EditText mEtMobileNumber;
    @BindView(R.id.amount_currency_code)
    TextView mAmountCurrencyCode;
    @BindView(R.id.et_amount)
    TextView mEtAmount;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    @BindView(R.id.view_transfer_success)
    View mViewTransferSuccess;
    @BindView(R.id.view_transfer_failure)
    View mViewTransferFailer;
    @BindView(R.id.txn_id)
    TextView mTxnId;
    @BindView(R.id.tv_txn_id)
    TextView mTvTxnId;

    private ProgressDialog progressDialog;
    private String eamount;
    private String mobileNumber;
    private String currency;
    private String countryFrom = "USD";
    private DepositRequestBody depositRequestBody;
    private CreditParty creditParty;

    @Override
    public void setPresenter(DepositContract.DepositPresenter presenter) {
        mDepositPresenter = presenter;
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
        View root = inflater.inflate(R.layout.fragment_deposit, container, false);
        ButterKnife.bind(this, root);
        mPresenter.attachView(this);
        setSwipeEnabled(false);
        hideSwipeProgress();
        progressDialog = new ProgressDialog(getContext());
        mAmountCurrencyCode.setText(countryFrom);
        return root;
    }

    @OnClick(R.id.btn_deposit)
    public void transferClicked() {
        mEtDescription.onEditorAction(EditorInfo.IME_ACTION_DONE);
        mEtAmount.onEditorAction(EditorInfo.IME_ACTION_DONE);
        eamount = mEtAmount.getText().toString().trim();
        mobileNumber = "+" + mCountryCodePicker.getFullNumber() + mEtMobileNumber.getText().toString().trim().replaceAll("\\s+", "");
        currency = mAmountCurrencyCode.getText().toString();
        if (eamount.equals("")) {
            Toast.makeText(getActivity(), Constants.PLEASE_ENTER_ALL_THE_FIELDS, Toast.LENGTH_SHORT).show();
        } else {
            double amount = Double.parseDouble(eamount);
            if (amount <= 0) {
                showSnackbar(Constants.PLEASE_ENTER_VALID_AMOUNT);
                return;
            }
            creditParty = new CreditParty("msisdn", mobileNumber);
            depositRequestBody = new DepositRequestBody(eamount, "transfer", currency, creditParty);
            showLoadingDialog("Loading...");
            mDepositPresenter.depositMoney(depositRequestBody);
        }
    }

    @OnClick(R.id.amount_currency_layout)
    public void amountCurrencyCodeClicked() {
        final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                mAmountCurrencyCode.setText(code);
                countryFrom = code;
                picker.dismiss();
            }
        });
        picker.show(getFragmentManager(), "CURRENCY_PICKER");
    }

    @OnClick(R.id.btn_back)
    public void backButtonClicked(){
        mEtAmount.setText("");
        mEtDescription.setText("");
        mEtMobileNumber.setText("");
        mDepositMoneyLayout.setVisibility(View.VISIBLE);
        mResultLayout.setVisibility(View.GONE);
    }

    @Override
    public void showSnackbar(String message) {
        Toaster.show(getView(), message);
    }

    @Override
    public void showToast(String message) {
        hideLoadingDialog();
        Toaster.showToast(getContext(), message);
    }

    private void showLoadingDialog(String message) {
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    private void hideLoadingDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    @Override
    public void showDepositMoneyResult(DepositResponseBody depositResponseBody) {
        hideLoadingDialog();
        mDepositMoneyLayout.setVisibility(View.GONE);
        mResultLayout.setVisibility(View.VISIBLE);
        if (depositResponseBody.getStatus().equals("ACCEPTED")){
            mViewTransferSuccess.setVisibility(View.VISIBLE);
            mViewTransferFailer.setVisibility(View.GONE);
            mTvTxnId.setVisibility(View.VISIBLE);
            mTxnId.setVisibility(View.VISIBLE);
            mTvTxnId.setText(depositResponseBody.getServerCorrelationId());

        } else {
            showToast(Constants.ERROR_OCCURRED);
            mTvTxnId.setVisibility(View.GONE);
            mTxnId.setVisibility(View.GONE);
            mViewTransferSuccess.setVisibility(View.GONE);
            mViewTransferFailer.setVisibility(View.VISIBLE);
        }
    }

}
