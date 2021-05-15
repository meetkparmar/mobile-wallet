package org.mifos.mobilewallet.mifospay.createuser.contract;

import org.mifos.mobilewallet.core.domain.model.uspf.IdentifierTemplateResponseBody;
import org.mifos.mobilewallet.mifospay.base.BasePresenter;
import org.mifos.mobilewallet.mifospay.base.BaseView;

public class KycContract {

    public interface KycView extends BaseView<KycPresenter> {

        void showToast(String message);

        void showKycTemplateResult(IdentifierTemplateResponseBody identifierTemplateResponseBody);
    }

    public interface KycPresenter extends BasePresenter {

        void fetchIdentifierTemplate(int clientId);
    }
}
