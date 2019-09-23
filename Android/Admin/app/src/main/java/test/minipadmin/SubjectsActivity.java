package test.minipadmin;

import android.app.ActivityManager;
import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

public class SubjectsActivity extends AppCompatActivity {

    String user;
    String selectedyear;
    ListView listView;
    String uora;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        user = getIntent().getStringExtra("user");
        selectedyear = getIntent().getStringExtra("selected");
        uora = getIntent().getStringExtra("uora");

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.createyearfab);


        if(uora.equals("a"))
        {
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder =  new AlertDialog.Builder(SubjectsActivity.this);
                    builder.setTitle("Insert New Subject in "+selectedyear);
                    builder.setView(R.layout.insertsubject);
                    builder.setNegativeButton("Cancel",null);
                    builder.setPositiveButton("Insert",null);
                    builder.setCancelable(false);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SubjectsActivity.this);
                            builder1.setMessage("Are you sure you want to cancel?");
                            builder1.setNegativeButton("No",null);
                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            });
                            builder1.create().show();

                        }

                    });
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final EditText editText = (EditText)alertDialog.findViewById(R.id.insertsubjectet);

                            if(editText.getText().toString().trim().equals("")){
                                editText.setError("Subject name cannot be empty!");
                            }
                            else
                            {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(SubjectsActivity.this);
                                builder1.setMessage("Are you sure you want to insert subject "+editText.getText().toString()+" in year "+selectedyear+" to the database?");
                                builder1.setNegativeButton("No",null);
                                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog.dismiss();
                                        new insertsubjectat().execute(editText.getText().toString());
                                    }
                                });
                                builder1.create().show();
                            }
                        }
                    });
                }
            });
        }


        setTitle("Subjects : "+selectedyear);

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

        listView = (ListView)findViewById(R.id.subjectslv);



        new Subjectsat().execute(selectedyear);


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
            new Subjectsat().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,selectedyear);
            return true;
        }
        else if(item.getItemId()==R.id.about){
            Intent i = new Intent(SubjectsActivity.this,AboutActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId()==R.id.help){
            Intent i = new Intent(SubjectsActivity.this,HelpActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId()==android.R.id.home){

            finish();
            return true;

        }
        else  if(item.getItemId()==R.id.logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(SubjectsActivity.this);
            builder.setMessage("Are you sure you want to logout?");
            builder.setNegativeButton("No",null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SubjectsActivity.this);
                    preferences.edit().remove("user").remove("uora").putBoolean("loggedin",false).apply();
                    Intent intent = new Intent(SubjectsActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.create().show();
            return  true;
        }
        return false;
    }

    public class Subjectsat extends AsyncTask<String,Void,JSONArray>{

        boolean conrefused=false;
        Intent intent;

        @Override
        protected void onPreExecute() {

           intent = new Intent(SubjectsActivity.this,UnitsActivity.class);
      //  intent = new Intent(SubjectsActivity.this,TabbedActivity1.class);

        }

        @Override
        protected JSONArray doInBackground(String... params) {

            String selectedyear = params[0];

            String loginurl = "http://192.168.43.227/minip/subjects.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

                String postdata = new Uri.Builder().appendQueryParameter("year",selectedyear).build().getEncodedQuery();


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

                Toast.makeText(getApplicationContext(),"You're not connected to Internet!",Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> subjects = new ArrayList<>();

            for(int i=0;i<jsonArray.length();i++)
            {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    subjects.add(jsonObject.getString("name"));






                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(SubjectsActivity.this,android.R.layout.simple_list_item_1,subjects);


            listView.setAdapter(arrayAdapter);

            arrayAdapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                    String selectedsubject = (String) listView.getItemAtPosition(i);

                    intent.putExtra("user",user);
                    intent.putExtra("selected",selectedsubject);
                    intent.putExtra("uora",uora);

                    startActivity(intent);



                }
            });

            if(uora.equals("a")){
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        PopupMenu popupMenu = new PopupMenu(SubjectsActivity.this,view);
                        popupMenu.getMenuInflater().inflate(R.menu.delete,popupMenu.getMenu());

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if(item.getItemId()==R.id.delete){

                                    AlertDialog.Builder builder = new AlertDialog.Builder(SubjectsActivity.this);
                                    builder.setMessage("Are you sure you want to delete "+listView.getItemAtPosition(i)+"?");
                                    builder.setNegativeButton("No",null);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int ii) {

                                            new deletesubjectat().execute(listView.getItemAtPosition(i).toString());

                                        }
                                    });
                                    builder.create().show();
                                    return true;

                                }

                                return  false;

                            }
                        });
                        popupMenu.show();
                        return true;
                    }
                });
            }


        }
    }

    public class insertsubjectat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            //  progressDialog = new ProgressDialog(getContext());
            //   progressDialog.setMessage("Inserting year..");
            //    progressDialog.setCancelable(false);
            //    progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String name = strings[0];

            String loginurl = "http://192.168.43.227/minip/admininsert.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("name", name).appendQueryParameter("type", "subject")
                .appendQueryParameter("selectedyear",selectedyear).build().getEncodedQuery();


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

            // progressDialog.dismiss();

            if(conrefused){

                Toast.makeText(SubjectsActivity.this, "You're not connected to Internet!", Toast.LENGTH_SHORT).show();

                return;
            }

            if(response.equals("success")){

                Toast.makeText(SubjectsActivity.this, "Subject Inserted Successfully!", Toast.LENGTH_SHORT).show();

            }
            else{


                Toast.makeText(SubjectsActivity.this, "Subject Could Not Be Inserted!", Toast.LENGTH_SHORT).show();

            }


            new Subjectsat().execute(selectedyear);





        }

    }
    public class deletesubjectat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            //  progressDialog = new ProgressDialog(getContext());
            //   progressDialog.setMessage("Inserting year..");
            //    progressDialog.setCancelable(false);
            //    progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String name = strings[0];

            String loginurl = "http://192.168.43.227/minip/admindelete.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("name", name).appendQueryParameter("type", "subject")
                        .appendQueryParameter("selectedyear",selectedyear).build().getEncodedQuery();


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

            // progressDialog.dismiss();

            if(conrefused){

                Toast.makeText(SubjectsActivity.this, "You're not connected to Internet!", Toast.LENGTH_SHORT).show();

                return;
            }

            if(response.equals("success")){

                Toast.makeText(SubjectsActivity.this, "Subject Deleted Successfully!", Toast.LENGTH_SHORT).show();

            }
            else{


                Toast.makeText(SubjectsActivity.this, "Subject Could Not Be Deleted!", Toast.LENGTH_SHORT).show();

            }


            new Subjectsat().execute(selectedyear);





        }

    }
}
