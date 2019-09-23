package test.minip;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class VerificationActivity extends AppCompatActivity {

    EditText enteredemailet;
    EditText enteredcodeet;
    Button verifyb;
    Button resendmailb;
    String newemail;
    String oldemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        setTitle("Verification");
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //display back button on title bar
        }

        //------------------------------------------------------------------------------- To change status bar and action bar color
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#347d6f"));
            window.setNavigationBarColor(Color.parseColor("#347d6f"));


            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription("Mr Professor",null,Color.parseColor("#347d6f"));
            this.setTaskDescription(taskDescription);
        }

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#347d6f")));
        }
//-------------------------------------------------------------------------------------------------------------------------------

        newemail = getIntent().getStringExtra("email");
        final String user = getIntent().getStringExtra("user");
        final String pass = getIntent().getStringExtra("pass");

        enteredemailet = (EditText)findViewById(R.id.enteredemailet);
        enteredemailet.setText(newemail);

        enteredcodeet = (EditText)findViewById(R.id.enteredcode);


        resendmailb = (Button)findViewById(R.id.resendmailb);
         verifyb = (Button)findViewById(R.id.verifyb);

        resendmailb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendmailb.setEnabled(false);
                verifyb.setEnabled(false);


                if(enteredemailet.getText().toString().trim().equals(""))
                {
                    resendmailb.setEnabled(true);
                    verifyb.setEnabled(true);
                    enteredemailet.requestFocus();
                    enteredemailet.setError("Email ID cannot be empty!");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(enteredemailet.getText().toString()).matches())
                {
                    resendmailb.setEnabled(true);
                    verifyb.setEnabled(true);
                    enteredemailet.requestFocus();
                    enteredemailet.setError("Not a valid Email ID!");
                }

                else
                {

                    oldemail = newemail;
                    newemail = enteredemailet.getText().toString().trim();

                    AlertDialog.Builder confirm = new AlertDialog.Builder(VerificationActivity.this);
                    confirm.setMessage("Are you sure you want to send email to "+newemail+"? We send a verification code!");
                    confirm.setNegativeButton("No",null);
                    confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new resendmailat().execute(newemail);
                        }
                    });
                    confirm.show();

                }
            }
        });

        verifyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendmailb.setEnabled(false);
                verifyb.setEnabled(false);

                if(enteredcodeet.getText().toString().trim().equals(""))
                {
                    resendmailb.setEnabled(true);
                    verifyb.setEnabled(true);
                    enteredcodeet.requestFocus();
                    enteredcodeet.setError("Code cannot be empty!");
                }
                else if(enteredcodeet.getText().toString().length()<6){
                    resendmailb.setEnabled(true);
                    verifyb.setEnabled(true);
                    enteredcodeet.requestFocus();
                    enteredcodeet.setError("Code should be of 6 digits!");
                }

                else{

                    new verifyat().execute(newemail,enteredcodeet.getText().toString(),user,pass);


                }
            }
        });



    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home)
            finish();

        return true;
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


    public class resendmailat extends AsyncTask<String,Void,String> {

        ProgressDialog progressDialog;


        String newemail;

        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(VerificationActivity.this);
            progressDialog.setMessage("Sending email..");
            progressDialog.setCancelable(false);
            progressDialog.show();;
        }

        @Override
        protected String doInBackground(String... params) {

            newemail = params[0];


            String registerurl = "http://192.168.43.227/minip/resendemail.php";

            try {
                URL url = new URL(registerurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("newemail",newemail).build().getEncodedQuery();


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
            resendmailb.setEnabled(true);
            verifyb.setEnabled(true);


            if(conrefused){

                newemail = oldemail;

                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "You're not connected to internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if(response.equals("email already registered")){

                newemail = oldemail;

                enteredemailet.requestFocus();
                enteredemailet.setError("This Email ID is already registered!");

            }

            else if(response.equals("Message has been sent")){

                Toast.makeText(VerificationActivity.this,"Verification code has been sent. Check your mail!",Toast.LENGTH_SHORT).show();

            }

        }
    }


    public class verifyat extends AsyncTask<String,Void,String> {

        ProgressDialog progressDialog;

        Intent intent;

        String emailtobeverified;



        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            intent = new Intent(VerificationActivity.this,LoginActivity.class);

            progressDialog = new ProgressDialog(VerificationActivity.this);
            progressDialog.setMessage("Verifying..");
            progressDialog.setCancelable(false);
            progressDialog.show();;
        }

        @Override
        protected String doInBackground(String... params) {



            emailtobeverified = params[0];
            String enteredcode = params[1];
            String user = params[2];
            String pass = params[3];


            String registerurl = "http://192.168.43.227/minip/verify.php";

            try {
                URL url = new URL(registerurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("emailtobeverified",emailtobeverified).appendQueryParameter("enteredcode",enteredcode)
                        .appendQueryParameter("user",user).appendQueryParameter("pass",pass).build().getEncodedQuery();


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
            resendmailb.setEnabled(true);
            verifyb.setEnabled(true);


            if(conrefused){

                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "You're not connected to internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            else if(response.equals("Message has been sent")){

                Toast.makeText(VerificationActivity.this,"Your email "+emailtobeverified+" has been verified and your login credentials have also been mailed to you!",Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();

            }

            else if(response.equals("failed")){

                enteredcodeet.requestFocus();
                enteredcodeet.setError("Entered code is incorrect!");


            }
            else
            {
                Toast.makeText(VerificationActivity.this,response,Toast.LENGTH_SHORT).show();
            }

        }
    }

}
