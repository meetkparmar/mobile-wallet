package org.mifos.mobilewallet.mifospay.home.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.mobilewallet.mifospay.R;
import org.mifos.mobilewallet.mifospay.base.BaseFragment;
import org.mifos.mobilewallet.mifospay.bank.ui.AccountsFragment;
import org.mifos.mobilewallet.mifospay.home.adapter.TabLayoutAdapter;
import org.mifos.mobilewallet.mifospay.kyc.ui.KYCDescriptionFragment;
import org.mifos.mobilewallet.mifospay.merchants.ui.MerchantsFragment;
import org.mifos.mobilewallet.mifospay.savedcards.ui.CardsFragment;
import org.mifos.mobilewallet.mifospay.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinanceFragment extends BaseFragment {

    @BindView(R.id.vp_tab_layout)
    ViewPager vpTabLayout;

    @BindView(R.id.tl_tab_layout)
    TabLayout tilTabLayout;

    private static boolean isAddressAdded;

    public static FinanceFragment newInstance(boolean isLocationAdded) {
        Bundle args = new Bundle();
        args.putBoolean(Constants.LOCATION, isLocationAdded);
        isAddressAdded = args.getBoolean(Constants.LOCATION);
        FinanceFragment fragment = new FinanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finance, container, false);
        ButterKnife.bind(this, rootView);

        if (!isAddressAdded) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.address_title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        setupUi();
        setupViewPager();
        tilTabLayout.setupWithViewPager(vpTabLayout);

        return rootView;
    }

    private void setupUi() {
        setSwipeEnabled(false);
        setToolbarTitle(getString(R.string.finance));
    }

    private void setupViewPager() {
        vpTabLayout.setOffscreenPageLimit(1);
        TabLayoutAdapter tabLayoutAdapter
                = new TabLayoutAdapter(getChildFragmentManager());
        tabLayoutAdapter.addFragment(new AccountsFragment(), getString(R.string.accounts));
        tabLayoutAdapter.addFragment(new CardsFragment(), getString(R.string.cards));
        tabLayoutAdapter.addFragment(new MerchantsFragment(), getString(R.string.merchants));
        tabLayoutAdapter.addFragment(new KYCDescriptionFragment(), getString(R.string.kyc));
        vpTabLayout.setAdapter(tabLayoutAdapter);
    }
}
