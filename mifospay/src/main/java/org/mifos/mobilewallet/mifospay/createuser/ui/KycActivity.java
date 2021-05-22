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
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.mifos.mobilewallet.core.domain.model.uspf.CreateIdentifierRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateIdentifierResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.DocumentTypes;
import org.mifos.mobilewallet.core.domain.model.uspf.IdentifierTemplateResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.UploadDocumentResponseBody;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseActivity;
import org.mifos.mobilewallet.mifospay.createuser.FilePath;
import org.mifos.mobilewallet.mifospay.createuser.adapter.IdentifierAdapter;
import org.mifos.mobilewallet.mifospay.createuser.adapter.ItemClicked;
import org.mifos.mobilewallet.mifospay.createuser.contract.KycContract;
import org.mifos.mobilewallet.mifospay.createuser.presenter.KycPresenter;
import org.mifos.mobilewallet.mifospay.utils.Toaster;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KycActivity extends BaseActivity implements KycContract.KycView {

    private static final int REQUEST_READ_IMAGE = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 7;
    public static final String CLIENT_ID = "client_id";

    @Inject
    KycPresenter mPresenter;

    KycContract.KycPresenter mKycPresenter;

    @BindView(R.id.iv_back_arrow)
    ImageView ivBackArrow;

    @BindView(R.id.iv_card)
    ImageView ivCard;

    @BindView(R.id.et_social_security_number)
    EditText etSSNumber;

    @BindView(R.id.et_social_security_document)
    EditText etSSDocument;

    @BindView(R.id.tv_click_here)
    TextView tvClickHere;

    @BindView(R.id.sub_text)
    TextView tvSubText;

    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @BindView(R.id.btn_submit2)
    Button btnSubmit2;

    @BindView(R.id.other_document_layout)
    ConstraintLayout otherDocumentLayout;

    @BindView(R.id.social_security_document_layout)
    ConstraintLayout ssDocumentLayout;

    @BindView(R.id.rv_identifier)
    RecyclerView rvIdentifier;

    IdentifierAdapter identifierAdapter;

    private String fileName;
    int clientId = -1;
    int layoutState = 1;
    private ProgressDialog progressDialog;
    private List<DocumentTypes> documentTypes = new ArrayList<>();
    private List<DocumentTypes> listDocumentOption = new ArrayList<>();
    private int itemPosition = -1;
    private File firstFile;
    private int count = 0;
    private ArrayList<Integer> positionList = new ArrayList<>();

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

        clientId = getIntent().getIntExtra(CLIENT_ID, clientId);

        mPresenter.attachView(this);
        progressDialog = new ProgressDialog(this);
        setupRecyclerView();
        showLoadingDialog("Loading...");
        mKycPresenter.fetchIdentifierTemplate(clientId);

        tvClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssDocumentLayout.setVisibility(View.GONE);
                otherDocumentLayout.setVisibility(View.VISIBLE);
                layoutState = 2;
            }
        });

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssDocumentLayout.setVisibility(View.VISIBLE);
                otherDocumentLayout.setVisibility(View.GONE);
                layoutState = 1;
            }
        });

        ivCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });


        etSSNumber.addTextChangedListener(textWatcher);
        etSSDocument.addTextChangedListener(textWatcher);

        etSSDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSSNumber.getText().toString().isEmpty() || etSSDocument.getText().toString().isEmpty()) {
                    showToast("Please fill all the details");
                } else {
                    CreateIdentifierRequestBody createIdentifierRequestBody = new CreateIdentifierRequestBody(
                            Integer.toString(documentTypes.get(0).getId()),
                        etSSNumber.getText().toString(),
                        "Active",
                        ""
                    );
                    showLoadingDialog("Loading ..");
                    mKycPresenter.createIdentifier(createIdentifierRequestBody, clientId);
                }
            }
        });

        btnSubmit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int index=0; index<listDocumentOption.size(); index++) {
                    int c = 0;
                    if (listDocumentOption.get(index).getDocumentImage() != null && listDocumentOption.get(index).getDocumentNumber() != null) {
                        positionList.add(index);
                        c++;
                    }
                    if (c==2) {
                        break;
                    }
                }
                CreateIdentifierRequestBody createIdentifierRequestBody = new CreateIdentifierRequestBody(
                        Integer.toString(listDocumentOption.get(positionList.get(0)).getId()),
                        documentTypes.get(positionList.get(0)).getName(),
                        "Active",
                        ""
                );
                showLoadingDialog("Loading ..");
                mKycPresenter.createIdentifier(createIdentifierRequestBody, clientId);

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
            if (!etSSNumber.getText().toString().equals("") && !etSSDocument.getText().toString().equals("")) {
                btnSubmit.setEnabled(true);
                btnSubmit.setClickable(true);
            } else {
                btnSubmit.setEnabled(false);
                btnSubmit.setClickable(false);
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_READ_IMAGE && data != null) {
                Uri firstImageUri = data.getData();
                if (firstImageUri != null) {
                    try {
                        String selectedFilePath = FilePath.getPath(this, firstImageUri);
                        firstFile = new File(selectedFilePath);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    if (itemPosition >= 0) {
                        listDocumentOption.get(itemPosition).setDocumentImage(setFileName(firstImageUri));
                        listDocumentOption.get(itemPosition).setFile(firstImageUri.toString());
                        identifierAdapter.setIdentifier(itemPosition, listDocumentOption.get(itemPosition));
                    } else {
                        etSSDocument.setText(setFileName(firstImageUri));
                    }
                }
            }
        }
    }

    private void setupRecyclerView() {
        identifierAdapter = new IdentifierAdapter(new ItemClicked() {
            @Override
            public void onClickedListener(int position) {
                pickImageFromGallery();
                itemPosition = position;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvIdentifier.setLayoutManager(layoutManager);
        rvIdentifier.setHasFixedSize(true);
        rvIdentifier.setAdapter(identifierAdapter);
    }

    private void openBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = this.getLayoutInflater().inflate(R.layout.card_info_bottom_sheet, null);
        dialog.setContentView(view);
        dialog.show();
    }

    private String setFileName(Uri selectedImageUri) {
        String scheme = selectedImageUri.getScheme();
        if (scheme.equals("file")) {
            fileName = selectedImageUri.getLastPathSegment();
        } else if (scheme.equals("content")) {
            String[] proj = {MediaStore.Images.Media.TITLE};
            Cursor cursor = this.getContentResolver().query(selectedImageUri, proj, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                cursor.moveToFirst();
                return cursor.getString(columnIndex);
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private void pickImageFromGallery() {
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
            startActivityForResult(galleryIntent, REQUEST_READ_IMAGE);
        }
    }

    @Override
    public void showToast(String message) {
        hideLoadingDialog();
        Toaster.showToast(this, message);
    }

    @Override
    public void showKycTemplateResult(IdentifierTemplateResponseBody identifierTemplateResponseBody) {
        hideLoadingDialog();
        documentTypes = identifierTemplateResponseBody.getAllowedDocumentTypes();
        setFirstDocument();
        setIdentifierTemplate();
    }

    private void setFirstDocument() {
        etSSNumber.setHint(documentTypes.get(0).getName() + " Number");
    }

    private void setIdentifierTemplate() {
        listDocumentOption.clear();
        StringBuilder sub_text = new StringBuilder();
        for (int index=1; index < documentTypes.size(); index++){
            sub_text.append(documentTypes.get(index).getName()).append(" |");
            listDocumentOption.add(documentTypes.get(index));
        }
        tvSubText.setText(sub_text);
        identifierAdapter.setData(listDocumentOption);
    }

    @Override
    public void createIdentifierResult(CreateIdentifierResponseBody createIdentifierResponseBody) {
        hideLoadingDialog();
        HashMap<String, String> partMap = new HashMap<>();
        partMap.put("name", documentTypes.get(0).getName());
        partMap.put("description", " ");
        showLoadingDialog("Loading ..");
        mKycPresenter.uploadDocument(partMap, firstFile, clientId);
    }

    @Override
    public void uploadDocumentResult(UploadDocumentResponseBody uploadDocumentResponseBody) {
        hideLoadingDialog();
        count++;
        if (layoutState == 2 && count == 1) {
            CreateIdentifierRequestBody createIdentifierRequestBody = new CreateIdentifierRequestBody(
                    Integer.toString(listDocumentOption.get(positionList.get(1)).getId()),
                    documentTypes.get(positionList.get(0)).getName(),
                    "Active",
                    ""
            );
            showLoadingDialog("Loading ..");
            mKycPresenter.createIdentifier(createIdentifierRequestBody, clientId);
        } else {
            Intent intent = new Intent(this, LoginCompleteActivity.class);
            startActivity(intent);
            finish();
        }
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
