package org.mifos.mobilewallet.mifospay.createuser.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.mifos.mobile.passcode.utils.PassCodeConstants;

import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.auth.ui.LoginActivity;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.passcode.ui.PassCodeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginCompleteActivity extends BaseActivity {

    @BindView(R.id.btn_start)
    Button btnStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_login_complete);
        ButterKnife.bind(this);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginCompleteActivity.this, LoginActivity.class);
//                intent.putExtra(PassCodeConstants.PASSCODE_INITIAL_LOGIN, true);
                startActivity(intent);
                finish();
            }
        });
    }
}
