package test.minipadmin;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class TabbedActivity2 extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;

    String user;
    String selectedunit;
    String selectedsubject;
    String uora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        user = getIntent().getStringExtra("user");
        selectedunit = getIntent().getStringExtra("selectedunit");
        selectedsubject = getIntent().getStringExtra("selectedsubject");
        uora = getIntent().getStringExtra("uora");

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //display back button on title bar
        }

        //------------------------------------------------------------------------------- To change status bar and action bar color (Remaining color is changed in xml file)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#961b1b"));
            window.setNavigationBarColor(Color.parseColor("#961b1b"));


            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription("Mr Professor",null,Color.parseColor("#961b1b"));
            this.setTaskDescription(taskDescription);
        }

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#961b1b")));
        }
        //------------------------------------------------------------------------------------------------



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);



        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //Refresh and back arrow(actionbar) buttons are handled in the respective fragments

        int id = item.getItemId();

        if(id==R.id.about){
            Intent i = new Intent(TabbedActivity2.this,AboutActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId()==R.id.help){
            Intent i = new Intent(TabbedActivity2.this,HelpActivity.class);
            startActivity(i);
            return true;
        }
        else  if(item.getItemId()==R.id.logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(TabbedActivity2.this);
            builder.setMessage("Are you sure you want to logout?");
            builder.setNegativeButton("No",null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TabbedActivity2.this);
                    preferences.edit().remove("user").remove("uora").putBoolean("loggedin",false).apply();
                    Intent intent = new Intent(TabbedActivity2.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.create().show();
            return  true;
        }


        return super.onOptionsItemSelected(item);

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){

                case 0 :
                    return new DoubtsFragment();
                case 1 :
                    return new LinksFragment();
                case 2:
                    return new TipsFragment();

                default:  return null;

            }


        }

        @Override
        public int getCount() {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Doubts";
                case 1:
                    return "Links";
                case 2:
                    return "Tips";
            }
            return null;
        }
    }
}
