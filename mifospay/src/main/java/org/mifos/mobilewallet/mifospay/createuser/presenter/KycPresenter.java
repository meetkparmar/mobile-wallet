package org.mifos.mobilewallet.mifospay.createuser.presenter;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.base.UseCaseHandler;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateIdentifierRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateIdentifierResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.IdentifierTemplateResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.UploadDocumentResponseBody;
import org.mifos.mobilewallet.mifospay.base.BaseView;
import org.mifos.mobilewallet.mifospay.createuser.contract.KycContract;
import org.mifos.mobilewallet.mifospay.createuser.usecase.CreateIdentifier;
import org.mifos.mobilewallet.mifospay.createuser.usecase.IdentifierTemplate;
import org.mifos.mobilewallet.mifospay.createuser.usecase.UploadDocument;
import org.mifos.mobilewallet.mifospay.data.local.PreferencesHelper;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import java.io.File;
import java.util.Map;

import javax.inject.Inject;

public class KycPresenter implements KycContract.KycPresenter {

    @Inject
    IdentifierTemplate identifierTemplate;

    @Inject
    CreateIdentifier createIdentifier;

    @Inject
    UploadDocument uploadDocument;

    private final UseCaseHandler mUseCaseHandler;
    private final PreferencesHelper mPreferencesHelper;
    private KycContract.KycView kycView;
    IdentifierTemplateResponseBody identifierTemplateResponseBody;
    CreateIdentifierResponseBody createIdentifierResponseBody;
    UploadDocumentResponseBody uploadDocumentResponseBody;

    @Inject
    public KycPresenter(UseCaseHandler mUseCaseHandler, PreferencesHelper mPreferencesHelper) {
        this.mUseCaseHandler = mUseCaseHandler;
        this.mPreferencesHelper = mPreferencesHelper;
    }

    @Override
    public void fetchIdentifierTemplate(int clientId) {
        mUseCaseHandler.execute(identifierTemplate,
                new IdentifierTemplate.RequestValues(clientId),
                new UseCase.UseCaseCallback<IdentifierTemplate.ResponseValue>() {
                    @Override
                    public void onSuccess(IdentifierTemplate.ResponseValue response) {
                        identifierTemplateResponseBody = response.getIdentifierTemplateResponseBody();
                        kycView.showKycTemplateResult(identifierTemplateResponseBody);
                    }

                    @Override
                    public void onError(String message) {
                        kycView.showToast(Constants.ERROR_OCCURRED);
                    }
                });
    }

    @Override
    public void createIdentifier(CreateIdentifierRequestBody createIdentifierRequestBody, int clientId) {
        mUseCaseHandler.execute(createIdentifier,
                new CreateIdentifier.RequestValues(createIdentifierRequestBody, clientId),
                new UseCase.UseCaseCallback<CreateIdentifier.ResponseValue>() {
                    @Override
                    public void onSuccess(CreateIdentifier.ResponseValue response) {
                        createIdentifierResponseBody = response.getCreateIdentifierResponseBody();
                        kycView.createIdentifierResult(createIdentifierResponseBody);
                    }

                    @Override
                    public void onError(String message) {
                        kycView.showToast(Constants.ERROR_OCCURRED);
                    }
                });
    }

    @Override
    public void uploadDocument(Map<String, String> partMap, File file, int clientId) {
        mUseCaseHandler.execute(uploadDocument,
                new UploadDocument.RequestValues(partMap, file, clientId),
                new UseCase.UseCaseCallback<UploadDocument.ResponseValue>() {
                    @Override
                    public void onSuccess(UploadDocument.ResponseValue response) {
                        uploadDocumentResponseBody = response.getUploadDocumentResponseBody();
                        kycView.uploadDocumentResult(uploadDocumentResponseBody);
                    }

                    @Override
                    public void onError(String message) {
                        kycView.showToast(Constants.ERROR_OCCURRED);
                    }
                });
    }

    @Override
    public void attachView(BaseView baseView) {
        kycView = (KycContract.KycView) baseView;
        kycView.setPresenter(this);
    }


}
