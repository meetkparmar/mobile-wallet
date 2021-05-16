package org.mifos.mobilewallet.mifospay.createuser.contract;

import org.mifos.mobilewallet.core.domain.model.uspf.CreateIdentifierRequestBody;
import org.mifos.mobilewallet.core.domain.model.uspf.CreateIdentifierResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.IdentifierTemplateResponseBody;
import org.mifos.mobilewallet.core.domain.model.uspf.UploadDocumentResponseBody;
import org.mifos.mobilewallet.mifospay.base.BasePresenter;
import org.mifos.mobilewallet.mifospay.base.BaseView;

import java.io.File;
import java.util.Map;

public class KycContract {

    public interface KycView extends BaseView<KycPresenter> {

        void showToast(String message);

        void showKycTemplateResult(IdentifierTemplateResponseBody identifierTemplateResponseBody);

        void createIdentifierResult(CreateIdentifierResponseBody createIdentifierResponseBody);

        void uploadDocumentResult(UploadDocumentResponseBody uploadDocumentResponseBody);
    }

    public interface KycPresenter extends BasePresenter {

        void fetchIdentifierTemplate(int clientId);

        void createIdentifier(CreateIdentifierRequestBody createIdentifierRequestBody, int clientId);

        void uploadDocument(Map<String, String> partMap, File file, int clientId);
    }
}
