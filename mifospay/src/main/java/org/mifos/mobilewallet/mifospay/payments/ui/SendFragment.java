package org.mifos.mobilewallet.mifospay.payments.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.widget.TextInputLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import org.mifos.mobilewallet.core.domain.model.AccountNameDetails;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionRequestBody;
import org.mifos.mobilewallet.core.domain.model.CurrencyConversionResponseBody;
import org.mifos.mobilewallet.core.domain.model.gsma.CreditParty;
import org.mifos.mobilewallet.core.domain.model.gsma.DebitParty;
import org.mifos.mobilewallet.core.domain.model.gsma.IntTransferRequestBody;
import org.mifos.mobilewallet.core.domain.model.gsma.InternationalTransferInformation;
import org.mifos.mobilewallet.mifospay.MoneyTransfer;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.base.BaseFragment;
import org.mifos.mobilewallet.mifospay.common.ui.MakeTransferFragment;
import org.mifos.mobilewallet.mifospay.home.BaseHomeContract;
import org.mifos.mobilewallet.mifospay.payments.presenter.TransferPresenter;
import org.mifos.mobilewallet.mifospay.qr.ui.ReadQrActivity;
import org.mifos.mobilewallet.mifospay.utils.Constants;
import org.mifos.mobilewallet.mifospay.utils.Toaster;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by naman on 30/8/17.
 */

public class SendFragment extends BaseFragment implements BaseHomeContract.TransferView {

    public static final int REQUEST_SHOW_DETAILS = 3;
    private static final int REQUEST_CAMERA = 0;
    private static final int SCAN_QR_REQUEST_CODE = 666;
    private static final int PICK_CONTACT = 1;
    private static final int REQUEST_READ_CONTACTS = 2;

    @Inject
    TransferPresenter mPresenter;
    BaseHomeContract.TransferPresenter mTransferPresenter;
    @BindView(R.id.rl_send_container)
    ViewGroup sendContainer;
    @BindView(R.id.et_vpa)
    EditText etVpa;
    @BindView(R.id.btn_submit)
    Button btnTransfer;
    @BindView(R.id.btn_scan_qr)
    TextView btnScanQr;
    @BindView(R.id.btn_vpa)
    Chip mBtnVpa;
    @BindView(R.id.btn_mobile)
    Chip mBtnMobile;
    @BindView(R.id.country_code_picker)
    CountryCodePicker mCountryCodePicker;
    @BindView(R.id.et_mobile_number)
    EditText mEtMobileNumber;
    @BindView(R.id.btn_search_contact)
    TextView mBtnSearchContact;
    @BindView(R.id.vpa_layout)
    LinearLayout mVpaLayout;
    @BindView(R.id.rl_mobile)
    RelativeLayout mRlMobile;
    @BindView(R.id.rl_user_details)
    RelativeLayout mRlUserDetails;
    @BindView(R.id.iv_user_image)
    ImageView ivUserImage;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.til_vpa)
    TextInputLayout mTilVpa;
    @BindView(R.id.amount_from_currency_code)
    TextView mAmountFromCurrencyCode;
    @BindView(R.id.amount_to_currency_code)
    TextView mAmountToCurrencyCode;
    @BindView(R.id.et_amount_from)
    TextView mEtAmountFrom;
    @BindView(R.id.et_amount_to)
    TextView mEtAmountTo;

    private String vpa;
    private ProgressDialog progressDialog;
    private CurrencyConversionRequestBody currencyConversionRequestBody;
    private IntTransferRequestBody intTransferRequestBody;
    private String countryFrom = "USD";
    private String countryTo = "INR";
    private double currencyRate;
    private String requestingLei;
    private String receivingLei = "ibank-india";
    private MoneyTransfer moneyTransfer;
    private String userMobileNumber;
    private String eamount;
    private String mobileNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send, container,
                false);
        ButterKnife.bind(this, rootView);
        setSwipeEnabled(false);
        mPresenter.attachView(this);
        mBtnMobile.setSelected(true);
        progressDialog = new ProgressDialog(getContext());

        mAmountFromCurrencyCode.setText(countryFrom);
        mAmountToCurrencyCode.setText(countryTo);
        mEtAmountFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtAmountTo.setText("0");
            }

        });
        mEtMobileNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>7) {
                    if (mCountryCodePicker.isValidFullNumber() || s.toString().equals("9738700217")) {
                        showLoadingDialog("Loading...");
                        mTransferPresenter.getAccountName("MSISDN", "+" + mCountryCodePicker.getFullNumber() + s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
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

    @OnClick(R.id.btn_vpa)
    public void onVPASelected() {
        TransitionManager.beginDelayedTransition(sendContainer);
        mBtnVpa.setSelected(true);
        mBtnVpa.setFocusable(true);
        mBtnVpa.setChipBackgroundColorResource(R.color.clickedblue);
        mBtnMobile.setSelected(false);
        mBtnMobile.setChipBackgroundColorResource(R.color.changedBackgroundColour);
        mVpaLayout.setVisibility(View.VISIBLE);
        btnScanQr.setVisibility(View.VISIBLE);
        mRlMobile.setVisibility(View.GONE);
        mRlUserDetails.setVisibility(View.GONE);
        mTilVpa.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_mobile)
    public void onMobileSelected() {
        TransitionManager.beginDelayedTransition(sendContainer);
        mBtnMobile.setSelected(true);
        mBtnMobile.setFocusable(true);
        mBtnMobile.setChipBackgroundColorResource(R.color.clickedblue);
        mBtnVpa.setSelected(false);
        mBtnVpa.setChipBackgroundColorResource(R.color.changedBackgroundColour);
        mVpaLayout.setVisibility(View.GONE);
        mTilVpa.setVisibility(View.GONE);
        btnScanQr.setVisibility(View.GONE);
        mRlMobile.setVisibility(View.VISIBLE);
        mCountryCodePicker.registerCarrierNumberEditText(mEtMobileNumber);
    }

    @OnClick(R.id.btn_submit)
    public void transferClicked() {
        String externalId = etVpa.getText().toString().trim();
        eamount = mEtAmountFrom.getText().toString().trim();
        mobileNumber = mEtMobileNumber.getText()
                .toString().trim().replaceAll("\\s+", "");
        if (eamount.equals("") || (mBtnVpa.isSelected() && externalId.equals("")) ||
                (mBtnMobile.isSelected() && mobileNumber.equals(""))) {
            Toast.makeText(getActivity(),
                    Constants.PLEASE_ENTER_ALL_THE_FIELDS, Toast.LENGTH_SHORT).show();
        } else {
            double amount = Double.parseDouble(eamount);
            if (amount <= 0) {
                showSnackbar(Constants.PLEASE_ENTER_VALID_AMOUNT);
                return;
            }
            if (!mTransferPresenter.checkSelfTransfer(externalId)) {
                mTransferPresenter.checkBalanceAvailability(externalId, amount);
            } else {
                showSnackbar(Constants.SELF_ACCOUNT_ERROR);
            }
        }
    }

    @OnClick(R.id.btn_scan_qr)
    public void scanQrClicked() {

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {

            // Permission has already been granted
            Intent i = new Intent(getActivity(), ReadQrActivity.class);
            startActivityForResult(i, SCAN_QR_REQUEST_CODE);
        }
    }

    @OnClick(R.id.amount_from_currency_layout)
    public void amountFromCurrencyCodeClicked() {
        final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                mAmountFromCurrencyCode.setText(code);
                countryFrom = code;
                picker.dismiss();
            }
        });
        picker.show(getFragmentManager(), "CURRENCY_PICKER");
    }

    @OnClick(R.id.amount_to_currency_layout)
    public void amountToCurrencyCodeClicked() {
        final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                mAmountToCurrencyCode.setText(code);
                countryTo = code;
                picker.dismiss();
            }
        });
        picker.show(getFragmentManager(), "CURRENCY_PICKER");
    }

    @OnClick(R.id.btn_convert_currency)
    public void btnConvertCurrency() {
        String lockKey = UUID.randomUUID().toString();
        countryFrom = mAmountFromCurrencyCode.getText().toString();
        countryTo = mAmountToCurrencyCode.getText().toString();
        currencyConversionRequestBody = new CurrencyConversionRequestBody(lockKey, mEtAmountFrom.getText().toString(), countryFrom, countryTo, false);
        showLoadingDialog("Loading...");
        mTransferPresenter.currencyConvert(currencyConversionRequestBody);
    }

    @Override
    public void showVpa(String vpa) {
        this.vpa = vpa;
    }

    @Override
    public void setPresenter(BaseHomeContract.TransferPresenter presenter) {
        this.mTransferPresenter = presenter;
    }

    @OnClick(R.id.btn_search_contact)
    public void searchContactClicked() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        } else {

            // Permission has already been granted
            Intent intent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SCAN_QR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String qrData = data.getStringExtra(Constants.QR_DATA);
            final String[] qrDataArray = qrData.split(", ");
            if (qrDataArray.length == 1) {
                etVpa.setText(qrDataArray[0]);
            } else {
                etVpa.setText(qrDataArray[0]);
                mEtAmountFrom.setText(qrDataArray[1]);
            }
            String externalId = etVpa.getText().toString();
            if (mEtAmountFrom.getText().toString().isEmpty()) {
                showSnackbar(Constants.PLEASE_ENTER_AMOUNT);
                return;
            }
            double amount = Double.parseDouble(mEtAmountFrom.getText().toString());
            if (!mTransferPresenter.checkSelfTransfer(externalId)) {
                mTransferPresenter.checkBalanceAvailability(externalId, amount);
            } else {
                showSnackbar(Constants.SELF_ACCOUNT_ERROR);
            }

        } else if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            Cursor cursor = null;
            try {
                String phoneNo = null;
                String name = null;
                // getData() method will have the Content Uri of the selected contact
                Uri uri = data.getData();
                //Query the content uri
                cursor = getContext().getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                // column index of the phone number
                int phoneIndex = cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER);
                // column index of the contact name
                int nameIndex = cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                phoneNo = cursor.getString(phoneIndex);
                name = cursor.getString(nameIndex);
                phoneNo = phoneNo.replace(" ", "");
                mEtMobileNumber.setText(phoneNo);

            } catch (Exception e) {
                showToast(Constants.ERROR_CHOOSING_CONTACT);
            }
        } else if (requestCode == REQUEST_SHOW_DETAILS && resultCode == Activity.RESULT_CANCELED) {
            if (mBtnMobile.isSelected()) {
                showSnackbar(Constants.ERROR_FINDING_MOBILE_NUMBER);
            } else {
                showSnackbar(Constants.ERROR_FINDING_VPA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // camera-related task you need to do.
                    Intent i = new Intent(getActivity(), ReadQrActivity.class);
                    startActivityForResult(i, SCAN_QR_REQUEST_CODE);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toaster.show(getView(), Constants.NEED_CAMERA_PERMISSION_TO_SCAN_QR_CODE);
                }
                return;
            }
            case REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toaster.show(getView(), Constants.NEED_READ_CONTACTS_PERMISSION);
                }
                return;
            }
        }
    }

    @Override
    public void showToast(String message) {
        mRlUserDetails.setVisibility(View.GONE);
        hideLoadingDialog();
        Toaster.showToast(getContext(), message);
    }

    @Override
    public void showSnackbar(String message) {
        Toaster.show(getView(), message);
    }

    @Override
    public void showMobile(String mobileNo) {
        userMobileNumber = mobileNo;
    }

    @Override
    public void showAccountName(AccountNameDetails accountNameDetails) {
        mRlUserDetails.setVisibility(View.VISIBLE);
        hideLoadingDialog();
        tvName.setText(accountNameDetails.getName().getFullName());
        if (accountNameDetails.getImage() != null){
            byte[] decodedString = Base64.decode(accountNameDetails.getImage().split(",")[1], Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivUserImage.setImageBitmap(decodedByte);
        }
        requestingLei = accountNameDetails.getLei();
    }

    @Override
    public void showCurrencyConversionDetails(CurrencyConversionResponseBody currencyConversionResponseBody) {
        hideLoadingDialog();
        String convertedAmount = currencyConversionResponseBody.getConvertedAmount().toString();
        currencyRate = currencyConversionResponseBody.getRate();
        mEtAmountTo.setText(convertedAmount);
    }

    @Override
    public void showClientDetails(String externalId, double amount, String mobileNumber) {

        if (mAmountFromCurrencyCode.getText() == mAmountToCurrencyCode.getText()){
            moneyTransfer = MoneyTransfer.DOMESTIC_MONEY_TRANSFER;
        } else {
            moneyTransfer = MoneyTransfer.INTERNATIONAL_MONEY_TRANSFER;
            CreditParty creditParty = new CreditParty("msisdn", mCountryCodePicker.getFullNumber() + this.mobileNumber);
            ArrayList<CreditParty> creditParties = new ArrayList<>();
            creditParties.add(creditParty);// check mobile number
            DebitParty debitParty = new DebitParty("msisdn", mobileNumber); // check mobile number
            ArrayList<DebitParty> debitParties = new ArrayList<>();
            debitParties.add(debitParty);// check mobile number
            String currencyPair = mAmountFromCurrencyCode.getText().toString() + "/" + mAmountToCurrencyCode.getText().toString();
            InternationalTransferInformation internationalTransferInformation = new InternationalTransferInformation(countryFrom, countryTo, countryTo,
                    currencyPair, 0.013); // change currencyPairRate
            intTransferRequestBody = new IntTransferRequestBody(eamount, "transfer", requestingLei, receivingLei,
                    countryFrom, creditParties, debitParties, internationalTransferInformation);
        }

        MakeTransferFragment fragment = MakeTransferFragment.newInstance(externalId, amount, intTransferRequestBody, moneyTransfer);
        fragment.setTargetFragment(this, REQUEST_SHOW_DETAILS);
        if (getParentFragment() != null) {
            fragment.show(getParentFragment().getChildFragmentManager(),
                    Constants.MAKE_TRANSFER_FRAGMENT);
        }
    }
}