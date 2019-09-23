package test.minip;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    CheckBox rememberme;

    EditText useret;
    EditText passet;
    Button loginb;
    Button registerb;
    TextView forgotuptv;
    AlertDialog alertDialog;
    EditText regemailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rememberme = (CheckBox)findViewById(R.id.rememberme);
        registerb = (Button)findViewById(R.id.registerb);
        loginb = (Button)findViewById(R.id.loginb);
        useret = (EditText)findViewById(R.id.useret);
        passet = (EditText)findViewById(R.id.passet);
        forgotuptv = (TextView) findViewById(R.id.forgotuptv);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        if(preferences.getBoolean("loggedin",false)){
            Intent i = new Intent(LoginActivity.this,YearActivity.class);
            i.putExtra("user",preferences.getString("user",null));

            startActivity(i);
            finish();
        }

        if(preferences.getBoolean("rememberme",false)){
            rememberme.setChecked(true);
            useret.setText(preferences.getString("ruser",null));
            passet.setText(preferences.getString("rpass",null));
        }



        setTitle("   Mr Professor");
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




        registerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });


        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerb.setEnabled(false);
                loginb.setEnabled(false);
                forgotuptv.setEnabled(false);

                if(useret.getText().toString().trim().equals("")){

                    registerb.setEnabled(true);
                    loginb.setEnabled(true);
                    forgotuptv.setEnabled(true);
                    useret.requestFocus();
                    useret.setError("Username cannot be empty!");
                }
                else if(passet.getText().toString().trim().equals("")){

                    registerb.setEnabled(true);
                    loginb.setEnabled(true);
                    forgotuptv.setEnabled(true);
                    passet.requestFocus();
                    passet.setError("Password cannot be empty!");
                }

                else{

                    //Registerat registerat = new Registerat();
                    //registerat.execute();
                    new Loginat().execute(useret.getText().toString(),passet.getText().toString()); //AsyncTask Serial Execution

                    //AsyncTask Parallel Execution : new Registerat().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,ruseret.getText().toString(),rpasset.getText().toString());


                }

            }
        });

        forgotuptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                builder.setTitle("Forgot Username / Password");
                builder.setNegativeButton("Cancel",null);
                builder.setPositiveButton("Send Email",null);
                builder.setCancelable(false);
                builder.setView(R.layout.forgotup);

                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        regemailid = (EditText)alertDialog.findViewById(R.id.regemailid);

                        if(regemailid.getText().toString().trim().equals("")){

                            regemailid.requestFocus();
                            regemailid.setError("Email ID cannot be empty!");

                        }
                        else  if(!Patterns.EMAIL_ADDRESS.matcher(regemailid.getText().toString()).matches())
                        {
                            regemailid.requestFocus();
                            regemailid.setError("Not a valid Email ID!");
                        }

                        else{


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                            builder1.setTitle("Confirm Email");
                            builder1.setMessage("Are you sure "+regemailid.getText().toString()+" is the correct email?");
                            builder1.setNegativeButton("No",null);
                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    new forgotupat().execute(regemailid.getText().toString());

                                }
                            });
                            builder1.show();


                        }
                    }
                });




            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.about){

            Intent i = new Intent(LoginActivity.this,AboutActivity.class);
            startActivity(i);
            return true;
        }
        if(item.getItemId()==R.id.help){

            Intent i = new Intent(LoginActivity.this,HelpActivity.class);
            startActivity(i);
            return true;
        }

        return false;


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {  //for hiding keyboard if touch is outside EditText's focus
        if(ev.getAction()==MotionEvent.ACTION_DOWN)
        {
            View v = getCurrentFocus();
            if(v instanceof EditText){
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)ev.getRawX(),(int)ev.getRawY()))
                {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
            }
        }
        return  super.dispatchTouchEvent(ev);
    } //for hiding keyboard if touch is outside EditText's focus


    public class Loginat extends AsyncTask<String,Void,String>{

        String user;
        String pass;
        ProgressDialog progressDialog;
        Intent i;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

             i = new Intent(LoginActivity.this,YearActivity.class);
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Logging In ..");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            user = params[0];
            pass = params[1];


            String loginurl = "http://192.168.43.227/minip/login.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

                String postdata = new Uri.Builder().appendQueryParameter("user",user).appendQueryParameter("pass",pass).build().getEncodedQuery();


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

            progressDialog.hide();

            registerb.setEnabled(true);
            loginb.setEnabled(true);
            forgotuptv.setEnabled(true);

            if(conrefused){

                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("You're not connected to the Internet!");
                builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginb.performClick();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.create().show();
*/
                if(getApplicationContext()!=null) {
                   Toast.makeText(getApplicationContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                    return;
            }

            if(response.equals("invalid")){

                Toast.makeText(getApplicationContext(),"Username/Password is not valid!",Toast.LENGTH_SHORT).show();

            }

            else if (response.equals("success")){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                preferences.edit().putBoolean("loggedin",true).putString("user",user).apply();


                i.putExtra("user",useret.getText().toString().trim());

                useret.setText("");
                passet.setText("");


                if(rememberme.isChecked()){
                    preferences.edit().putBoolean("rememberme",true).putString("ruser",user).putString("rpass",pass).apply();
                }
                else
                {
                    preferences.edit().remove("ruser").remove("rpass").putBoolean("rememberme",false).apply();
                }


                startActivity(i);
                finish();

            }
            else
            {
                Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
            }


        }
    }

    public class forgotupat extends AsyncTask<String,Void,String>{


        ProgressDialog progressDialog;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Sending Email..");
            progressDialog.setCancelable(false);
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {

            String email = params[0];


            String forgotupurl = "http://192.168.43.227/minip/forgotup.php";

            try {
                URL url = new URL(forgotupurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

                String postdata = new Uri.Builder().appendQueryParameter("emailaddress",email).build().getEncodedQuery();


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

            progressDialog.hide();

            if(conrefused){

                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if(response.equals("invalid")){

                regemailid.requestFocus();
                regemailid.setError("Invalid Email ID!");
                return;


            }

            else if(response.equals("Message has been sent")){

              alertDialog.hide();
                Toast.makeText(getApplicationContext(),"Your login credentials have been mailed to you!",Toast.LENGTH_SHORT).show();


            }


        }
    }

}
