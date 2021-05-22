package org.mifos.mobilewallet.mifospay.createuser.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.hbb20.CountryCodePicker;

import org.mifos.mobilewallet.core.domain.model.client.Client;
import org.mifos.mobilewallet.core.domain.model.uspf.ClientAddress;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateClientResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateUserResponseBody;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.createuser.contract.CreateClientContract;
import org.mifos.mobilewallet.mifospay.createuser.presenter.CreateClientPresenter;
import org.mifos.mobilewallet.mifospay.utils.Constants;
import org.mifos.mobilewallet.mifospay.utils.Toaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoLoginActivity extends BaseActivity implements CreateClientContract.CreateClientView {

    @Inject
    CreateClientPresenter mPresenter;

    CreateClientContract.CreateClientPresenter mCreateClientPresenter;

    @BindView(R.id.iv_back_arrow)
    ImageView ivBackArrow;

    @BindView(R.id.et_first_name)
    EditText etFirstName;

    @BindView(R.id.et_last_name)
    EditText etLastName;

    @BindView(R.id.ccp_code)
    CountryCodePicker ccpPhonecode;

    @BindView(R.id.et_mobile_number)
    EditText etMobileNumber;

    @BindView(R.id.et_date_of_birth)
    EditText etDateOfBirth;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.btn_login)
    Button btnLogin;

    final Calendar myCalendar = Calendar.getInstance();
    private ProgressDialog progressDialog;
    private CreateClientRequestBody createClientRequestBody;
    private CreateUserRequestBody createUserRequestBody;
    private String dateFormat = "dd MMMM yyyy";
    private String locale = "en";
    private Boolean active = true;
    private String formattedDate = "";
    private int clientId = -1;
    private ClientAddress clientAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.demo_activity_login);
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        progressDialog = new ProgressDialog(this);

        clientAddress = (ClientAddress) getIntent().getSerializableExtra(Constants.ADDRESS);

        etFirstName.addTextChangedListener(textWatcher);
        etLastName.addTextChangedListener(textWatcher);
        etMobileNumber.addTextChangedListener(textWatcher);
        etDateOfBirth.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etDateOfBirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(DemoLoginActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDetails();
            }
        });
    }

    private void submitDetails() {
        List<ClientAddress> addresses = new ArrayList<>();
        addresses.add(clientAddress);
        createClientRequestBody = new CreateClientRequestBody(
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                ccpPhonecode.getFullNumber() + etMobileNumber.getText().toString(),
                formattedDate,
                currentDate(),
                currentDate(),
                dateFormat,
                locale,
                active,
                1,
                1,
                1,
                addresses,
                new ArrayList<String>()
        );
        showLoadingDialog("Loading...");
        mCreateClientPresenter.createClient(createClientRequestBody, etFirstName.getText().toString(), ccpPhonecode.getFullNumber() + etMobileNumber.getText().toString());
    }

    private String currentDate() {
        return new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!etFirstName.getText().toString().equals("") && !etLastName.getText().toString().equals("") && !etMobileNumber.getText().toString().equals("")
                    && !etDateOfBirth.getText().toString().equals("") && !etEmail.getText().toString().equals("")) {
                btnLogin.setEnabled(true);
                btnLogin.setClickable(true);
            } else {
                btnLogin.setEnabled(false);
                btnLogin.setClickable(false);
            }
        }
    };

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat, Locale.US);
        formattedDate = sdf2.format(myCalendar.getTime());
        etDateOfBirth.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void showToast(String message) {
        hideLoadingDialog();
        Toaster.showToast(this, message);
    }

    @Override
    public void showCreateClientResult(CreateClientResponseBody createClientResponseBody) {
        hideLoadingDialog();
        clientId = createClientResponseBody.getClientId();
        createUserRequestBody = new CreateUserRequestBody(
                etEmail.getText().toString(),
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                etEmail.getText().toString(),
                false,
                1,
                Arrays.asList(1),
                "password",
                "password"
        );

        Client client = new Client();
        client.setClientId(clientId);
        client.setDisplayName(etFirstName.getText().toString() + etLastName.getText().toString());
        client.setMobileNo(etMobileNumber.getText().toString());
        client.setName(etFirstName.getText().toString());
        showLoadingDialog("Loading...");
        mCreateClientPresenter.createUser(createUserRequestBody);
    }

    @Override
    public void showCreateUserResult(CreateUserResponseBody createUserResponseBody) {
        hideLoadingDialog();
        Intent intent = new Intent(this, KycActivity.class);
        intent.putExtra(KycActivity.CLIENT_ID, clientId);
        startActivity(intent);
        finish();
    }

    @Override
    public void setPresenter(CreateClientContract.CreateClientPresenter presenter) {
        mCreateClientPresenter = presenter;
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
}
