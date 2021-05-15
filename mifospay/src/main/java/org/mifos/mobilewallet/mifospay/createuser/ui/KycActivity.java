package org.mifos.mobilewallet.mifospay.createuser.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.mobilewallet.core.domain.model.uspf.DocumentTypes;
import org.mifos.mobilewallet.core.domain.model.uspf.IdentifierTemplateResponseBody;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.createuser.contract.KycContract;
import org.mifos.mobilewallet.mifospay.createuser.presenter.KycPresenter;
import org.mifos.mobilewallet.mifospay.utils.Toaster;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KycActivity extends BaseActivity implements KycContract.KycView {

    private static final int REQUEST_READ_IMAGE_FOR_1 = 1;
    private static final int REQUEST_READ_IMAGE_FOR_2 = 2;
    private static final int REQUEST_READ_IMAGE_FOR_3 = 3;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 7;

    @Inject
    KycPresenter mPresenter;

    KycContract.KycPresenter mKycPresenter;

    @BindView(R.id.et_upload_doc1)
    EditText etUploadDoc1;

    @BindView(R.id.et_upload_doc2)
    EditText etUploadDoc2;

    @BindView(R.id.et_doc_number_1)
    EditText etDocNumber1;

    @BindView(R.id.et_doc_number_2)
    EditText etDocNumber2;

    @BindView(R.id.et_social_security_number)
    EditText etSSNumber;

    @BindView(R.id.et_social_security_document)
    EditText etSSDocument;

    @BindView(R.id.tv_click_here)
    TextView tvClickHere;

    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @BindView(R.id.other_document_layout)
    ConstraintLayout otherDocumentLayout;

    @BindView(R.id.social_security_document_layout)
    ConstraintLayout ssDocumentLayout;

    String fileName;
    int clientId;
    int layoutState = 1;
    private ProgressDialog progressDialog;
    private List<DocumentTypes> documentTypes;

    @Override
    public void setPresenter(KycContract.KycPresenter presenter) {
        mKycPresenter = presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_kyc);
        ButterKnife.bind(this);

        mPresenter.attachView(this);
        progressDialog = new ProgressDialog(this);

        showLoadingDialog("Loading...");
        mKycPresenter.fetchIdentifierTemplate(23);

        tvClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssDocumentLayout.setVisibility(View.GONE);
                otherDocumentLayout.setVisibility(View.VISIBLE);
                layoutState = 2;
            }
        });

        etSSNumber.addTextChangedListener(textWatcher);
        etSSDocument.addTextChangedListener(textWatcher);
        etDocNumber1.addTextChangedListener(textWatcher);
        etDocNumber2.addTextChangedListener(textWatcher);
        etUploadDoc1.addTextChangedListener(textWatcher);
        etUploadDoc2.addTextChangedListener(textWatcher);

        etSSDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery(1);
            }
        });

        etUploadDoc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery(2);
            }
        });

        etUploadDoc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery(3);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutState == 1) {
                    if(etSSNumber.getText().toString().isEmpty() || etSSDocument.getText().toString().isEmpty()) {
                        showToast("Please fill all the details");
                    } else {

                    }
                } else {
                    if(etDocNumber1.getText().toString().isEmpty() || etDocNumber2.getText().toString().isEmpty() ||
                       etUploadDoc1.getText().toString().isEmpty() || etUploadDoc2.getText().toString().isEmpty()) {
                        showToast("Please fill all the details");
                    } else {

                    }
                }
            }
        });

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
            if (layoutState == 1) {
                if (!etSSNumber.getText().toString().equals("") && !etSSDocument.getText().toString().equals("")) {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setClickable(true);
                } else {
                    btnSubmit.setEnabled(false);
                    btnSubmit.setClickable(false);
                }
            } else {
                if (!etDocNumber1.getText().toString().equals("") && !etDocNumber2.getText().toString().equals("")
                        && etUploadDoc1.getText().toString().equals("") && !etUploadDoc2.getText().toString().equals("")) {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setClickable(true);
                } else {
                    btnSubmit.setEnabled(false);
                    btnSubmit.setClickable(false);
                }
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_READ_IMAGE_FOR_1 && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    setFileName(selectedImageUri, 1);
                }
            } else if (requestCode == REQUEST_READ_IMAGE_FOR_2 && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    setFileName(selectedImageUri, 2);
                }
            } else if (requestCode == REQUEST_READ_IMAGE_FOR_3 && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    setFileName(selectedImageUri, 3);
                }
            }
        }
    }

    private void setFileName(Uri selectedImageUri, int docNumber) {
        String scheme = selectedImageUri.getScheme();
        if (scheme.equals("file")) {
            fileName = selectedImageUri.getLastPathSegment();
        } else if (scheme.equals("content")) {
            String[] proj = {MediaStore.Images.Media.TITLE};
            Cursor cursor = this.getContentResolver().query(selectedImageUri, proj, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                cursor.moveToFirst();
                fileName = cursor.getString(columnIndex);
                if (docNumber == 1) {
                    etSSDocument.setText(fileName + ".jpg");
                } else if (docNumber == 2) {
                    etUploadDoc1.setText(fileName + ".jpg");
                } else {
                    etUploadDoc2.setText(fileName + ".jpg");
                }
                etSSDocument.setText(fileName + ".jpg");
            }
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void pickImageFromGallery(int docNumber) {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String[] mimeTypes = {"image/jpeg", "image/png"};
                galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
            if (docNumber == 1) {
                startActivityForResult(galleryIntent, REQUEST_READ_IMAGE_FOR_1);
            } else if (docNumber == 2) {
                startActivityForResult(galleryIntent, REQUEST_READ_IMAGE_FOR_2);
            } else {
                startActivityForResult(galleryIntent, REQUEST_READ_IMAGE_FOR_3);
            }
        }
    }

    @Override
    public void showToast(String message) {
        hideLoadingDialog();
        Toaster.showToast(this, message);
    }

    @Override
    public void showKycTemplateResult(IdentifierTemplateResponseBody identifierTemplateResponseBody) {
        showToast("Success");

        documentTypes = identifierTemplateResponseBody.getAllowedDocumentTypes();
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
