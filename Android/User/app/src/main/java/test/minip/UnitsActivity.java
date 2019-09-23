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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class UnitsActivity extends AppCompatActivity {

    String selectedsubject;
    String user;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);

        selectedsubject = getIntent().getStringExtra("selected");
        user = getIntent().getStringExtra("user");


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.createyearfab);


        setTitle("Units : "+selectedsubject);

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


        listView = (ListView)findViewById(R.id.unitslv);




        new Unitsat().execute(selectedsubject);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.refresh)
        {
            new Unitsat().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,selectedsubject);
            return true;
        }
        else if(item.getItemId()==R.id.about){
            Intent i = new Intent(UnitsActivity.this,AboutActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId()==R.id.help){
            Intent i = new Intent(UnitsActivity.this,HelpActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId()==android.R.id.home){

            finish();
            return true;

        }
        else  if(item.getItemId()==R.id.logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(UnitsActivity.this);
            builder.setMessage("Are you sure you want to logout?");
            builder.setNegativeButton("No",null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UnitsActivity.this);
                    preferences.edit().remove("user").putBoolean("loggedin",false).apply();
                    Intent intent = new Intent(UnitsActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.create().show();
            return  true;
        }
        return false;
    }

    public class Unitsat extends AsyncTask<String,Void,JSONArray> {

        String selectedsubject;

        boolean conrefused=false;
        Intent intent;

        @Override
        protected void onPreExecute() {

            intent = new Intent(UnitsActivity.this,TabbedActivity2.class);

        }

        @Override
        protected JSONArray doInBackground(String... params) {

            selectedsubject = params[0];

            String loginurl = "http://192.168.43.227/minip/units.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

                String postdata = new Uri.Builder().appendQueryParameter("subject",selectedsubject).build().getEncodedQuery();


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

                    Toast.makeText(UnitsActivity.this, "You're not connected to Internet!", Toast.LENGTH_SHORT).show();

                return;
            }

            final ArrayList<String> units = new ArrayList<>();

            for(int i=0;i<jsonArray.length();i++)
            {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    units.add(jsonObject.getString("name"));






                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(UnitsActivity.this,android.R.layout.simple_list_item_1,units);


            listView.setAdapter(arrayAdapter);

            arrayAdapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String selectedunit = (String) listView.getItemAtPosition(i);

                    intent.putExtra("selectedunit",selectedunit);
                    intent.putExtra("selectedsubject",selectedsubject);
                    intent.putExtra("user",user);


                    startActivity(intent);


                }
            });




        }
    }
/*
    public class insertunitat extends AsyncTask<String,Void,String>{

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


                String postdata = new Uri.Builder().appendQueryParameter("name", name).appendQueryParameter("type", "unit")
                        .appendQueryParameter("selectedsubject",selectedsubject).build().getEncodedQuery();


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

                Toast.makeText(UnitsActivity.this, "You're not connected to Internet!", Toast.LENGTH_SHORT).show();

                return;
            }

            if(response.equals("success")){

                Toast.makeText(UnitsActivity.this, "Unit Inserted Successfully!", Toast.LENGTH_SHORT).show();

            }
            else{


                Toast.makeText(UnitsActivity.this, "Unit Could Not Be Inserted!", Toast.LENGTH_SHORT).show();

            }


            new Unitsat().execute(selectedsubject);





        }

    }
    public class deleteunitat extends AsyncTask<String,Void,String>{

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


                String postdata = new Uri.Builder().appendQueryParameter("name", name).appendQueryParameter("type", "unit")
                        .appendQueryParameter("selectedsubject",selectedsubject).build().getEncodedQuery();


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

                Toast.makeText(UnitsActivity.this, "You're not connected to Internet!", Toast.LENGTH_SHORT).show();

                return;
            }

            if(response.equals("success")){

                Toast.makeText(UnitsActivity.this, "Unit Deleted Successfully!", Toast.LENGTH_SHORT).show();

            }
            else{


                Toast.makeText(UnitsActivity.this, "Unit Could Not Be Deleted!", Toast.LENGTH_SHORT).show();

            }


            new Unitsat().execute(selectedsubject);





        }

    }
    */
}
