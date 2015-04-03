package com.infinitetech.expenses.ActivityClasses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.infinitetech.expenses.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends ActionBarActivity implements OverviewFragment.OnFragmentInteractionListener {

    private boolean wantsToExit;
    private Drawer.Result result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflateFragment(new OverviewFragment());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_overview);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        result = new Drawer().withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.navigationbar_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Overview"),
                        new PrimaryDrawerItem().withName("Add Transaction"),
                        new PrimaryDrawerItem().withName("View Expenses"),
                        new PrimaryDrawerItem().withName("View Income")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        switch (i){
                            case 0:
                                inflateFragment(new OverviewFragment());
                                break;
                            case 1:
                                startActivity(new Intent(MainActivity.this , InsertActivity.class));
                                break;
                            case 2:
                                inflateFragment(new ExpensesFragment());
                                break;
                            case 3:
                                inflateFragment(new IncomeFragment());
                                break;
                            default:
                                callSuperToast("Invalid");
                        }
                    }
                })
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private void inflateFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom , R.anim.abc_shrink_fade_out_from_bottom);
        ft.replace(R.id.frameLayout_main, fragment);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onFragmentInteraction() {
        callSuperToast("Inflated");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (result.isDrawerOpen()){
            result.closeDrawer();
        }else{
            if (!wantsToExit){
                wantsToExit = true ;
                callSuperToast("Press back again to Exit");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(SuperToast.Duration.SHORT);
                            wantsToExit = false ;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callSuperToast(String Text) {
        SuperToast.create(this, Text, SuperToast.Duration.SHORT, Style.getStyle(Style.BLUE, SuperToast.Animations.FADE)).show();
    }


}
