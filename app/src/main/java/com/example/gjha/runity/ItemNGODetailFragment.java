package com.example.gjha.runity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gjha.runity.database.DatabaseHandler;
import com.example.gjha.runity.database.NGO;

/**
 * A fragment representing a single ItemNGO detail screen.
 * This fragment is either contained in a {@link ItemNGOListActivity}
 * in two-pane mode (on tablets) or a {@link ItemNGODetailActivity}
 * on handsets.
 */
public class ItemNGODetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private NGO mItem;
private TextView textngoname;
    private TextView textBio;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemNGODetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DatabaseHandler.getInstance(getActivity()).getngo(getArguments().getLong(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_itemngo_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            textngoname = ((TextView) rootView.findViewById(R.id.textNGOName));
            textngoname.setText(mItem.ngoname);

            textBio = ((TextView) rootView.findViewById(R.id.textBio));
            textBio.setText(mItem.bio);

        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        updatePersonFromUI();
    }

    private void updatePersonFromUI() {
        if (mItem != null) {
            mItem.ngoname = textngoname.getText().toString();
            mItem.bio = textBio.getText().toString();

            DatabaseHandler.getInstance(getActivity()).putngo(mItem);
        }
    }
}
