package de.qabel.qabelbox.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import de.qabel.core.config.Identities;
import de.qabel.core.config.Identity;
import de.qabel.desktop.repository.IdentityRepository;
import de.qabel.desktop.repository.exception.PersistenceException;
import de.qabel.qabelbox.QabelBoxApplication;
import de.qabel.qabelbox.R;

public class CreateAccountFinalFragment extends BaseIdentityFragment {

    private TextView tvSuccess, tvMessage;

    @Inject
    IdentityRepository identityRepository;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        QabelBoxApplication.getApplicationComponent(getActivity().getApplicationContext()).inject(this);
        Identities identities = null;
        try {
            identities = identityRepository.findAll();
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        if (identities == null || identities.getIdentities().size() == 0) {
            //no identities available.
            tvMessage.setText(R.string.create_identity_final_create_identity);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_identity_final, container, false);

        tvSuccess = (TextView) v.findViewById(R.id.tv_success);
        tvMessage = (TextView) v.findViewById(R.id.tv_message);
        //override identity messages
        tvMessage.setText(R.string.create_account_final);
        tvSuccess.setText(R.string.create_account_final_headline);
        return v;
    }

    @Override
    public String check() {

        return null;
    }
}
