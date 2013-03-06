package android.otpc.know;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class UnitListActivity extends FragmentActivity
        implements UnitListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_list);

        if (findViewById(R.id.unit_detail_container) != null) {
            mTwoPane = true;
            ((UnitListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.unit_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(UnitDetailFragment.ARG_ITEM_ID, id);
            UnitDetailFragment fragment = new UnitDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.unit_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, UnitDetailActivity.class);
            detailIntent.putExtra(UnitDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
