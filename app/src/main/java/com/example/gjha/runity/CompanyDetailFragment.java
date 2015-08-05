package com.example.gjha.runity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gjha.runity.database.COMPANY;
import com.example.gjha.runity.database.DatabaseHandlerC;

/**
 * A fragment representing a single ItemCompany detail screen.
 * This fragment is either contained in a {@link CompanyListActivity}
 * in two-pane mode (on tablets) or a {@link CompanyDetailActivity}
 * on handsets.
 */
public class CompanyDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private COMPANY mCompany;
    private TextView textcompanyname;
    private TextView textBioc;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CompanyDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mCompany = DatabaseHandlerC.getInstance(getActivity()).getcompany(getArguments().getLong(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_company_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mCompany != null) {
            textcompanyname = ((TextView) rootView.findViewById(R.id.textCompanyName));
            textcompanyname.setText(mCompany.companyname);

            textBioc = ((TextView) rootView.findViewById(R.id.textBioc));
            textBioc.setText(mCompany.bioc);

        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        updatePersonFromUI();
    }

    private void updatePersonFromUI() {
        if (mCompany != null) {
            mCompany.companyname = textcompanyname.getText().toString();
            mCompany.bioc = textBioc.getText().toString();

            DatabaseHandlerC.getInstance(getActivity()).putcompany(mCompany);
        }
    }
}
