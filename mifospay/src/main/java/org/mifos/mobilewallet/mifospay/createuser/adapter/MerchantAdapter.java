package org.mifos.mobilewallet.mifospay.createuser.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobilewallet.mifospay.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MerchantAdapter extends RecyclerView.Adapter<MerchantAdapter.ViewHolder> {

    private List<String> addresses;
    private Context context;

    @Inject
    public MerchantAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_merchant_address, parent, false);
        return new MerchantAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String value = addresses.get(position);

        holder.tvLine1.setText(value);
    }

    @Override
    public int getItemCount() {
        if (addresses != null) {
            return addresses.size();
        } else {
            return 0;
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(List<String> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    public ArrayList<String> getMerchantList() {
        return (ArrayList<String>) addresses;
    }

    public String getAddress(int position) {
        return addresses.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_line_1)
        TextView tvLine1;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
