package org.mifos.mobilewallet.mifospay.createuser.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.mifos.mobilewallet.core.domain.model.uspf.DocumentTypes;
import org.mifos.mobilewallet.mifospay.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentifierAdapter extends RecyclerView.Adapter<IdentifierAdapter.ViewHolder> {

    private List<DocumentTypes> documentTypesList;
    private ItemClicked itemClicked;

    public IdentifierAdapter(ItemClicked itemClicked) {
        documentTypesList = new ArrayList<>();
        this.itemClicked = itemClicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_identifier_list,
                parent, false);
        return new IdentifierAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setView(position);
        final DocumentTypes documentTypes = documentTypesList.get(position);
        holder.etDocNumber.setHint(documentTypes.getName() + " Number");
        holder.etUploadDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked.onClickedListener(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (documentTypesList != null) {
            return documentTypesList.size();
        } else {
            return 0;
        }
    }

    public void setData(List<DocumentTypes> documentTypesList) {
        this.documentTypesList = documentTypesList;
        notifyDataSetChanged();
    }

    public void setIdentifier(int index, DocumentTypes documentTypes) {
        documentTypesList.set(index, documentTypes);
        notifyDataSetChanged();
    }

    public DocumentTypes getIdentifierDetails(int position) {
        return documentTypesList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.et_doc_number)
        EditText etDocNumber;
        @BindView(R.id.et_upload_doc)
        EditText etUploadDoc;
        int itemPosition;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void setView(int position) {
            itemPosition = position;
            if (documentTypesList.get(itemPosition).getDocumentNumber() != null) {
                etDocNumber.setText(documentTypesList.get(itemPosition).getDocumentNumber());
            } else {
                etDocNumber.setText(null);
            }
            if (documentTypesList.get(itemPosition).getDocumentImage() != null) {
                etUploadDoc.setText(documentTypesList.get(itemPosition).getDocumentImage());
            } else {
                etUploadDoc.setText(null);
            }
            etDocNumber.addTextChangedListener(textWatcher);
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                documentTypesList.get(itemPosition).setDocumentNumber(etDocNumber.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

    }
}
