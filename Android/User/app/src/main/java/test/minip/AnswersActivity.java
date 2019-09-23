package test.minip;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AnswersActivity extends AppCompatActivity {

    String doubtid;
    String selectedsubject;
    ListView listView;
    String user;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);


        ViewGroup headerview = (ViewGroup)getLayoutInflater().inflate(R.layout.answerheader,null);
        headerview.setOnClickListener(null);

        ViewGroup footerview = (ViewGroup)getLayoutInflater().inflate(R.layout.listfooter2,null);
        footerview.setOnClickListener(null);

        TextView title = (TextView)headerview.findViewById(R.id.titletv);
        TextView content = (TextView)headerview.findViewById(R.id.contenttv);
        TextView nov = (TextView)headerview.findViewById(R.id.novtv);
        TextView by = (TextView)headerview.findViewById(R.id.bytv);



        String titleofselected = getIntent().getStringExtra("titleofselected");
        String contentofselected = getIntent().getStringExtra("contentofselected");
        String byofselected = getIntent().getStringExtra("byofselected");
        String votesofselected = getIntent().getStringExtra("votesofselected");
        doubtid = getIntent().getStringExtra("doubtid");
        selectedsubject = getIntent().getStringExtra("selectedsubject");
        user = getIntent().getStringExtra("user");

        setTitle(titleofselected);

        title.setText(titleofselected);
        content.setText(contentofselected);
        nov.setText("Votes : "+votesofselected);
        by.setText(byofselected);



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


        listView = (ListView)findViewById(R.id.answerslv);

        listView.addHeaderView(headerview);
        listView.addFooterView(footerview);


        fab = (FloatingActionButton)findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(AnswersActivity.this);

                dialog.setTitle("Answer!");
                dialog.setView(R.layout.postanswer);
                dialog.setPositiveButton("Post Answer!", null);
                dialog.setNegativeButton("Cancel", null);
                dialog.setCancelable(false);


                final AlertDialog alertDialog = dialog.create();
                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final EditText answerdialog = (EditText)alertDialog.findViewById(R.id.answerdialog);

                        if(answerdialog.getText().toString().trim().equals("")){

                            answerdialog.requestFocus();
                            answerdialog.setError("Answer cannot be empty!");
                        }

                        else {


                            AlertDialog.Builder dialog2 = new AlertDialog.Builder(AnswersActivity.this);
                            dialog2.setMessage("Are you sure you want to post this answer?");
                            dialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                    new postanswersat().execute(doubtid,selectedsubject,user,answerdialog.getText().toString().trim());
                                }
                            });
                            dialog2.setNegativeButton("No", null);

                            dialog2.show();
                        }

                    }
                });
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(AnswersActivity.this);
                        dialog2.setMessage("Are you sure you want to cancel?");
                        dialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        });
                        dialog2.setNegativeButton("No",null);

                        dialog2.show();

                    }
                });


            }
        });
        fab.hide();





        new loadanswerssat().execute(selectedsubject);





    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.refresh)
        {
            new loadanswerssat().execute(selectedsubject);
            return true;
        }
        else if(item.getItemId()==R.id.about){
            Intent i = new Intent(AnswersActivity.this,AboutActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId()==R.id.help){
            Intent i = new Intent(AnswersActivity.this,HelpActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId()==android.R.id.home){

            finish();
            return true;

        }
        else  if(item.getItemId()==R.id.logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(AnswersActivity.this);
            builder.setMessage("Are you sure you want to logout?");
            builder.setNegativeButton("No",null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AnswersActivity.this);
                    preferences.edit().remove("user").remove("pass").putBoolean("loggedin",false).apply();
                    Intent intent = new Intent(AnswersActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
            builder.create().show();
            return  true;
        }
        return false;
    }



    public class loadanswerssat extends AsyncTask<String,Void,JSONArray>{


        ArrayList<String> answerids = new ArrayList<>();
        ArrayList<String> byuser = new ArrayList<>();
        ArrayList<String> content = new ArrayList<>();
        ArrayList<String> votes = new ArrayList<>();
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONArray doInBackground(String... strings) {

            String selectedsubject = strings[0];

            String loginurl = "http://192.168.43.227/minip/answers.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("doubtid", doubtid).appendQueryParameter("subject", selectedsubject)
                        .build().getEncodedQuery();


                // String postdata = "unit" + "=" + selectedunit+"&subject="+selectedsubject;


                bufferedWriter.write(postdata);


                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();




                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String response = "";
                response = bufferedReader.readLine();





                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                if(response==null){
                    response = "[]";
                }


                JSONArray jsonArray = new JSONArray(response);




                return jsonArray;


            } catch (IOException e) {
                e.printStackTrace();

                conrefused=true;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            if(conrefused){

                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }


            for(int i=0;i<jsonArray.length();i++){

                try {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    byuser.add(jsonObject.getString("byuser"));
                    content.add(jsonObject.getString("content"));
                    votes.add(jsonObject.getString("votes"));
                    answerids.add(jsonObject.getString("id"));



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            CustomAdapter1 adapter = new CustomAdapter1();

            listView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            fab.show();


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {


                    if(i==0){
                        return true;
                    }

                    PopupMenu popupMenu = new PopupMenu(AnswersActivity.this,view);
                    if(byuser.get(i-1).equals(user)) {
                        popupMenu.getMenuInflater().inflate(R.menu.longclickanswerswithdelete, popupMenu.getMenu());
                    }
                    else{
                        popupMenu.getMenuInflater().inflate(R.menu.longclickanswers, popupMenu.getMenu());
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getItemId()==R.id.upvoteb){

                                new voteat().execute(selectedsubject,answerids.get(i-1),"upvote");
                                return true;

                            }
                            else if(item.getItemId()==R.id.downvoteb){

                                new voteat().execute(selectedsubject,answerids.get(i-1),"downvote");
                                return true;

                            }
                            else if(item.getItemId()==R.id.deleteanswer){

                                AlertDialog.Builder builder = new AlertDialog.Builder(AnswersActivity.this);

                                    builder.setMessage("Are you sure you want to delete your answer?");

                                    builder.setNegativeButton("No",null);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int ii) {
                                        new deleteanswerat().execute(selectedsubject,answerids.get(i-1));
                                    }
                                });
                                builder.create().show();

                                return true;

                            }


                            return false;
                        }
                    });
                    popupMenu.show();
                    return true;
                }

            });








        }

        class CustomAdapter1 extends BaseAdapter {

            PopupMenu popupMenu;


            @Override
            public int getCount() {
                return byuser.size();
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
                    view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.answerlist,null);
                    holder.content2lv = (TextView)view.findViewById(R.id.content2lv);
                    holder.by2lv = (TextView)view.findViewById(R.id.by2lv);
                    holder.votes2lv = (TextView)view.findViewById(R.id.votes2lv);
                    holder.thumbsdownb = (ImageButton)view.findViewById(R.id.thumbsdownb);
                    holder.thumbsupb = (ImageButton)view.findViewById(R.id.thumbsupb);
                    holder.deleteb = (ImageButton)view.findViewById(R.id.deleteb);
                    view.setTag(holder);
                }
                else{
                    holder = (ViewHolder)view.getTag();
                }

                holder.content2lv.setText(content.get(i));
                holder.by2lv.setText("By : "+byuser.get(i));
                holder.votes2lv.setText(votes.get(i));

                holder.thumbsupb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new voteat().execute(selectedsubject,answerids.get(i),"upvote");
                    }
                });

                holder.thumbsdownb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new voteat().execute(selectedsubject,answerids.get(i),"downvote");
                    }
                });

                if(byuser.get(i).equals(user))
                {
                    holder.deleteb.setVisibility(View.VISIBLE);
                    holder.deleteb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AnswersActivity.this);

                                builder.setMessage("Are you sure you want to delete your answer?");

                            builder.setNegativeButton("No",null);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int ii) {
                                    new deleteanswerat().execute(selectedsubject,answerids.get(i));
                                }
                            });
                            builder.create().show();


                        }
                    });
                }
                else
                {
                    holder.deleteb.setVisibility(View.INVISIBLE);
                }



                return view;
            }
        }

        class ViewHolder{

            TextView content2lv;
            TextView votes2lv;
            TextView by2lv;

            ImageButton thumbsupb;
            ImageButton thumbsdownb;
            ImageButton deleteb;

        }

    }

    public class postanswersat extends AsyncTask<String,Void,String>{

        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {


            String doubtid = strings[0];
            String selectedsubject = strings[1];
            String user = strings[2];
            String answercontent = strings[3];

            String loginurl = "http://192.168.43.227/minip/postanswers.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("doubtid", doubtid).appendQueryParameter("subject", selectedsubject)
                        .appendQueryParameter("user",user).appendQueryParameter("content",answercontent)
                        .build().getEncodedQuery();


                // String postdata = "unit" + "=" + selectedunit+"&subject="+selectedsubject;


                bufferedWriter.write(postdata);


                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();




                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String response = "";
                response = bufferedReader.readLine();





                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                return response;


            } catch (IOException e) {
                e.printStackTrace();

                conrefused=true;

            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {

            if(conrefused){

                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if(response.equals("success")){
                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "Answer Posted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "Answer Could Not Be Posted!", Toast.LENGTH_SHORT).show();
                }
            }


            new loadanswerssat().execute(selectedsubject);





        }

    }

    public class voteat extends AsyncTask<String,Void,String>{

        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {

            String selectedsubject = strings[0];
            String answerid = strings[1];
            String vote = strings[2];

            String loginurl = "http://192.168.43.227/minip/vote.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("subject", selectedsubject)
                        .appendQueryParameter("requiredid",answerid).appendQueryParameter("vote",vote).appendQueryParameter("doubtoranswer","answer")
                        .appendQueryParameter("username",user)
                        .build().getEncodedQuery();


                // String postdata = "unit" + "=" + selectedunit+"&subject="+selectedsubject;


                bufferedWriter.write(postdata);


                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();




                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String response = "";
                response = bufferedReader.readLine();





                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                return response;


            } catch (IOException e) {
                e.printStackTrace();

                conrefused=true;

            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {

            if(conrefused){

                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            if(response.equals("already voted")){

                    Toast.makeText(AnswersActivity.this, "You've already voted on this answer!", Toast.LENGTH_SHORT).show();


            }

            else if(response.equals("success")){
                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "Voted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "Vote Could Not Be Posted!", Toast.LENGTH_SHORT).show();
                }
            }


            new loadanswerssat().execute(selectedsubject);





        }





    }

    public class deleteanswerat extends AsyncTask<String,Void,String>{

        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {

            String selectedsubject = strings[0];
            String answerid = strings[1];

            String loginurl = "http://192.168.43.227/minip/deletecontent.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("subject", selectedsubject).appendQueryParameter("requiredid",answerid)
                        .appendQueryParameter("type","answers")
                        .build().getEncodedQuery();


                // String postdata = "unit" + "=" + selectedunit+"&subject="+selectedsubject;


                bufferedWriter.write(postdata);


                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();




                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String response = "";
                response = bufferedReader.readLine();





                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                return response;


            } catch (IOException e) {
                e.printStackTrace();

                conrefused=true;

            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {

            if(conrefused){


                    Toast.makeText(AnswersActivity.this, "You're not connected to Internet!", Toast.LENGTH_SHORT).show();

                return;
            }

            if(response.equals("success")){

                    Toast.makeText(AnswersActivity.this, "Answer deleted Successfully!", Toast.LENGTH_SHORT).show();

            }
            else{


                    Toast.makeText(AnswersActivity.this, "Answer could not be deleted!", Toast.LENGTH_SHORT).show();

            }


            new loadanswerssat().execute(selectedsubject);





        }





    }

}
