package test.minip;

import android.app.ActivityManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle("About");

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //display back button on title bar
        }

        //------------------------------------------------------------------------------- To change status bar and action bar color
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
//-------------------------------------------------------------------------------------------------------------------------------



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return false;
    }
}
