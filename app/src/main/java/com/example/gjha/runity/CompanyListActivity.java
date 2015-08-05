package com.example.gjha.runity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.gjha.runity.database.COMPANY;
import com.example.gjha.runity.database.DatabaseHandlerC;


/**
 * An activity representing a list of ItemsCompany. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CompanyDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link CompanyListFragment} and the item details
 * (if present) is a {@link CompanyDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link CompanyListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class CompanyListActivity extends FragmentActivity
        implements CompanyListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);


       if (findViewById(R.id.company_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((CompanyListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.company_list))
                    .setActivateOnItemClick(true);
       }


        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link CompanyListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     * @param id
     */
    @Override
    public void onItemSelected(long id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(CompanyDetailFragment.ARG_ITEM_ID, id);
            CompanyDetailFragment fragment = new CompanyDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.company_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, CompanyDetailActivity.class);
            detailIntent.putExtra(CompanyDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_c, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        if (R.id.newCompany == item.getItemId()) {
            result = true;
            // Create a new person.
            COMPANY c = new COMPANY();
            DatabaseHandlerC.getInstance(this).putcompany(c);
            // Open a new fragment with the new id
            onItemSelected(c.id);
        }

        return result;
    };
}
