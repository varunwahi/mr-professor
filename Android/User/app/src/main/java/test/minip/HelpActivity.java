package test.minip;

import android.app.ActivityManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {

    ArrayList<String> arrayList;
    ArrayList<String> arrayList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        setTitle("Help");

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


        arrayList = new ArrayList<>();
        arrayList1 = new ArrayList<>();
        arrayList.add("How to upvote/downvote on doubts/links/tips?");
        arrayList1.add("Either use the indicated buttons or long hold to display the popup menu.");
        arrayList.add("How to logout?");
        arrayList1.add("Use the options menu on the top right.");
        arrayList.add("How to view answers of doubts?");
        arrayList1.add("Tap on the doubts");
        arrayList.add("How to open links?");
        arrayList1.add("Tap on the link and choose open url option or long hold the url to display the popup menu.");
        arrayList.add("Can I copy a link's url instead of opening it?");
        arrayList1.add("Yes. Tap on the link and choose copy to clipboard option or long hold the url to display the popup menu.");
        arrayList.add("How can I delete my own content?");
        arrayList1.add("Simply use the delete button or long hold the item and choose the delete option.");

        ViewGroup headerview = (ViewGroup)getLayoutInflater().inflate(R.layout.helplistheader,null);
        headerview.setOnClickListener(null);

        ListView listView = (ListView)findViewById(R.id.helplv);
        listView.addHeaderView(headerview);


        CustomAdapter customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView imageView = (ImageView)view.findViewById(R.id.expandhelpiv);
                TextView answer = (TextView)view.findViewById(R.id.answerhelptv);

                if(answer.getVisibility()==View.GONE){

                    imageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    answer.setVisibility(View.VISIBLE);

                }
                else{

                    imageView.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    answer.setVisibility(View.GONE);

                }
            }
        });





    }

    public class CustomAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            final ViewHolder holder;


            if(view==null){

                holder = new ViewHolder();
                view = LayoutInflater.from(HelpActivity.this).inflate(R.layout.helplist,null);
                holder.question = (TextView)view.findViewById(R.id.questionhelptv);
                holder.answer = (TextView)view.findViewById(R.id.answerhelptv);

                view.setTag(holder);
            }
            else{
                holder = (ViewHolder)view.getTag();
            }


            holder.question.setText(arrayList.get(i));
            holder.answer.setText(arrayList1.get(i));





            return view;
        }

        class ViewHolder{

            TextView question;
            TextView answer;

        }
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
