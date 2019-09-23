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

public class YearActivity extends AppCompatActivity {

    ListView listView;

    String uora;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);

        user = getIntent().getStringExtra("user");
        uora = getIntent().getStringExtra("uora");

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.createyearfab);


        if(uora.equals("a"))
        {
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder =  new AlertDialog.Builder(YearActivity.this);
                    builder.setTitle("Insert New Year");
                    builder.setView(R.layout.insertyear);
                    builder.setNegativeButton("Cancel",null);
                    builder.setPositiveButton("Insert",null);
                    builder.setCancelable(false);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(YearActivity.this);
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

                            final EditText editText = (EditText)alertDialog.findViewById(R.id.insertyearet);

                            if(editText.getText().toString().trim().equals("")){
                                editText.setError("Year name cannot be empty!");
                            }
                            else
                            {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(YearActivity.this);
                                builder1.setMessage("Are you sure you want to insert year "+editText.getText().toString()+" to the database?");
                                builder1.setNegativeButton("No",null);
                                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog.dismiss();
                                        new insertyearat().execute(editText.getText().toString());
                                    }
                                });
                                builder1.create().show();
                            }
                        }
                    });
                }
            });
        }


        setTitle("   Select Year");



        //------------------------------------------------------------------------------- To change status bar and action bar color
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#961b1b"));
            window.setNavigationBarColor(Color.parseColor("#961b1b"));

            ;
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription("Mr Professor",null,Color.parseColor("#961b1b"));
            this.setTaskDescription(taskDescription);
        }

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#961b1b")));
        }
//-------------------------------------------------------------------------------------------------------------------------------

        listView = (ListView)findViewById(R.id.yearlv);


        new Yearat().execute();


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
            new Yearat().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return true;
        }
        else if(item.getItemId()==R.id.about){
            Intent i = new Intent(YearActivity.this,AboutActivity.class);
            startActivity(i);
            return true;
        }
        else if(item.getItemId()==R.id.help){
            Intent i = new Intent(YearActivity.this,HelpActivity.class);
            startActivity(i);
            return true;
        }
        else  if(item.getItemId()==R.id.logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(YearActivity.this);
            builder.setMessage("Are you sure you want to logout?");
            builder.setNegativeButton("No",null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(YearActivity.this);
                    preferences.edit().remove("user").remove("uora").putBoolean("loggedin",false).apply();
                    Intent intent = new Intent(YearActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.create().show();
            return  true;
        }
        return false;
    }


    public class Yearat extends AsyncTask<Void,Void,JSONArray>{

        Intent intent;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            intent = new Intent(YearActivity.this,SubjectsActivity.class);

        }

        @Override
        protected JSONArray doInBackground(Void... voids) {


            String loginurl = "http://192.168.43.227/minip/year.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);



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
                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                    return;
            }


            ArrayList<String> year = new ArrayList<>();

            for(int i=0;i<jsonArray.length();i++)
            {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    year.add(jsonObject.getString("year"));






                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            ArrayAdapter arrayAdapter = new ArrayAdapter(YearActivity.this,android.R.layout.simple_list_item_1,year);

            listView.setAdapter(arrayAdapter);

            arrayAdapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                    String selectedyear = (String) listView.getItemAtPosition(i);

                    intent.putExtra("user",user);
                    intent.putExtra("selected",selectedyear);
                    intent.putExtra("uora",uora);

                    startActivity(intent);



                }
            });

            if(uora.equals("a")){
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        PopupMenu popupMenu = new PopupMenu(YearActivity.this,view);
                        popupMenu.getMenuInflater().inflate(R.menu.delete,popupMenu.getMenu());

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if(item.getItemId()==R.id.delete){

                                    AlertDialog.Builder builder = new AlertDialog.Builder(YearActivity.this);
                                    builder.setMessage("Are you sure you want to delete "+listView.getItemAtPosition(i)+"?");
                                    builder.setNegativeButton("No",null);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int ii) {
                                            new deleteyearat().execute(listView.getItemAtPosition(i).toString());
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

    public class insertyearat extends AsyncTask<String,Void,String>{

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


                String postdata = new Uri.Builder().appendQueryParameter("name", name).appendQueryParameter("type", "year").build().getEncodedQuery();


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

                    Toast.makeText(YearActivity.this, "You're not connected to Internet!", Toast.LENGTH_SHORT).show();

                return;
            }

            if(response.equals("success")){

                    Toast.makeText(YearActivity.this, "Year Inserted Successfully!", Toast.LENGTH_SHORT).show();

            }
            else{


                    Toast.makeText(YearActivity.this, "Year Could Not Be Inserted!", Toast.LENGTH_SHORT).show();

            }


            new Yearat().execute();





        }

    }
    public class deleteyearat extends AsyncTask<String,Void,String>{

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


                String postdata = new Uri.Builder().appendQueryParameter("name", name).appendQueryParameter("type", "year").build().getEncodedQuery();


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

                Toast.makeText(YearActivity.this, "You're not connected to Internet!", Toast.LENGTH_SHORT).show();

                return;
            }

            if(response.equals("success")){

                Toast.makeText(YearActivity.this, "Year Deleted Successfully!", Toast.LENGTH_SHORT).show();

            }
            else{


                Toast.makeText(YearActivity.this, "Year Could Not Be Deleted!", Toast.LENGTH_SHORT).show();

            }


            new Yearat().execute();





        }

    }


}
