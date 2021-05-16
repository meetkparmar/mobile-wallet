package org.mifos.mobilewallet.mifospay.createuser.usecase;

import org.mifos.mobilewallet.core.base.UseCase;
import org.mifos.mobilewallet.core.data.fineract.repository.FineractRepository;
import org.mifos.mobilewallet.core.domain.model.uspf.UploadDocumentResponseBody;

import java.io.File;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UploadDocument extends UseCase<UploadDocument.RequestValues,
        UploadDocument.ResponseValue> {

    private final FineractRepository fineractRepository;

    @Inject
    public UploadDocument(FineractRepository fineractRepository) {
        this.fineractRepository = fineractRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        fineractRepository.uploadDocument(requestValues.partMap, requestValues.file, requestValues.clientId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UploadDocumentResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getUseCaseCallback().onError(e.toString());
                    }

                    @Override
                    public void onNext(UploadDocumentResponseBody uploadDocumentResponseBody) {
                        getUseCaseCallback().onSuccess(new UploadDocument.ResponseValue(uploadDocumentResponseBody));
                    }
                });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private Map<String, String> partMap;
        private File file;
        private int clientId;

        public void setPartMap(Map<String, String> partMap) {
            this.partMap = partMap;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public void setClientId(int clientId) {
            this.clientId = clientId;
        }

        public RequestValues(Map<String, String> partMap, File file, int clientId) {
            this.partMap = partMap;
            this.file = file;
            this.clientId = clientId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private UploadDocumentResponseBody uploadDocumentResponseBody;

        public ResponseValue(UploadDocumentResponseBody uploadDocumentResponseBody) {
            this.uploadDocumentResponseBody = uploadDocumentResponseBody;
        }

        public UploadDocumentResponseBody getUploadDocumentResponseBody() {
            return uploadDocumentResponseBody;
        }
    }
}
