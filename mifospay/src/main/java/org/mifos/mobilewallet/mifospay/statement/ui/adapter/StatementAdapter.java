package org.mifos.mobilewallet.mifospay.statement.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mifos.mobilewallet.core.domain.model.Statement;
import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.mifos.mobilewallet.mifospay.utils.Utils.getFormattedAccountBalance;

public class StatementAdapter extends RecyclerView.Adapter<StatementAdapter.ViewHolder> {

    private List<Statement> statements;
    private Context context;

    @Inject
    public StatementAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_statement_list, parent, false);
        return new StatementAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Statement statement = statements.get(position);

        Long amount = statement.getAmount();
        String currencyCode = statement.getCurrency();
        holder.tvAmount.setText(getFormattedAccountBalance((double)amount, currencyCode));
//        holder.tvSubtitle.setText(statement.getStatus());

        if (amount > 0 && context != null) {
            int color = ContextCompat.getColor(context, R.color.colorAccentBlue);
            holder.tvAmount.setTextColor(color);
        }

        String strDate = statement.getStartedAt();

        String[] date = strDate.split("\\.");
        String[] dateTime = date[0].split("T");
        holder.tvDate.setText(String.format("%s %s", dateTime[0], dateTime[1]));

        switch (statement.getDirection()) {
            case "OUTGOING":
                holder.tvTitle.setText(Constants.WITHDRAWAL);
                holder.ivIcon.setImageResource(R.drawable.cashout);
                break;
            case "INCOMING":
                holder.tvTitle.setText(Constants.DEPOSIT);
                holder.ivIcon.setImageResource(R.drawable.cashin);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (statements != null) {
            return statements.size();
        } else {
            return 0;
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(List<Statement> statements) {
        this.statements = statements;
        notifyDataSetChanged();
    }

    public ArrayList<Statement> getTransactions() {
        return (ArrayList<Statement>) statements;
    }

    public Statement getStatement(int position) {
        return statements.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;

        @BindView(R.id.tv_title)
        TextView tvTitle;

//        @BindView(R.id.tv_subtitle)
//        TextView tvSubtitle;

        @BindView(R.id.tv_amount)
        TextView tvAmount;

        @BindView(R.id.tv_formatted_date)
        TextView tvDate;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
