package org.mifos.mobilewallet.mifospay.createuser.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.createuser.model.LoginDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoLoginActivity extends BaseActivity {

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.demo_activity_login);
        ButterKnife.bind(this);

        etFirstName.addTextChangedListener(textWatcher);
        etLastName.addTextChangedListener(textWatcher);
        etMobileNumber.addTextChangedListener(textWatcher);
        etDateOfBirth.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);

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
            LoginDetails loginDetails = new LoginDetails();
            loginDetails.setFirstName(etFirstName.getText().toString());
            loginDetails.setLastName(etLastName.getText().toString());
            loginDetails.setEmail(etEmail.getText().toString());
            loginDetails.setDateOfBirth(etDateOfBirth.getText().toString());
            loginDetails.setMobileNumber(ccpPhonecode.getFullNumber() + etMobileNumber.getText().toString());
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

        etDateOfBirth.setText(sdf.format(myCalendar.getTime()));
    }
}
