package com.example.gjha.runity;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;


/**
 * A placeholder fragment containing a simple view.
 */
public class listSelectionFragment extends DialogFragment {

    public listSelectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_selection, container, false);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_company:
                if (checked) {
                    Intent intent = new Intent(getActivity(), ItemNGOListActivity.class);
                    getActivity().startActivity(intent);
                }
                    break;
            case R.id.radio_ngos:
                if (checked){
                    Intent intent = new Intent(getActivity(), CompanyListActivity.class);
                    getActivity().startActivity(intent);
                }
                    // Ninjas rule
                    break;
        }
    }
}