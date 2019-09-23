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

public class RegisterActivity extends AppCompatActivity {


     Button registrationb;
    EditText ruseret ;
   EditText rpasset;
    EditText repasset;
    EditText remailet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Register");
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



        registrationb = (Button)findViewById(R.id.registrationb);
        ruseret = (EditText)findViewById(R.id.ruseret);
        rpasset = (EditText)findViewById(R.id.rpasset);
        repasset = (EditText)findViewById(R.id.repasset);
        remailet = (EditText)findViewById(R.id.remailet);




        registrationb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registrationb.setEnabled(false);


                if(remailet.getText().toString().trim().equals("")){

                    registrationb.setEnabled(true);
                    remailet.requestFocus();
                    remailet.setError("Email ID cannot be empty!");

                }
                else  if(!Patterns.EMAIL_ADDRESS.matcher(remailet.getText().toString()).matches())
                {
                    registrationb.setEnabled(true);
                    remailet.requestFocus();
                    remailet.setError("Not a valid Email ID!");
                }
                else if(ruseret.getText().toString().trim().equals("")){

                    registrationb.setEnabled(true);
                    ruseret.requestFocus();
                    ruseret.setError("Username cannot be empty!");

                }

                else if(rpasset.getText().toString().trim().equals("")){

                    registrationb.setEnabled(true);
                    rpasset.requestFocus();
                    rpasset.setError("Password cannot be empty!");
                }
                else if(repasset.getText().toString().trim().equals("")){

                    registrationb.setEnabled(true);
                    repasset.requestFocus();
                    repasset.setError("Password cannot be empty!");
                }
                else if(!rpasset.getText().toString().equals(repasset.getText().toString())){

                    registrationb.setEnabled(true);
                    if(getApplicationContext()!=null) {
                        Toast.makeText(getApplicationContext(), "Both Passwords don't match!", Toast.LENGTH_SHORT).show();
                    }
                }

                else{

                    AlertDialog.Builder confirm = new AlertDialog.Builder(RegisterActivity.this);
                    confirm.setMessage("Are you sure "+remailet.getText().toString()+" is the correct email? We send a verification code!");
                    confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            registrationb.setEnabled(true);
                        }
                    });
                    confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Registerat registerat = new Registerat();
                            //registerat.execute();
                            new Registerat().execute(remailet.getText().toString(),ruseret.getText().toString(),rpasset.getText().toString()); //AsyncTask Serial Execution

                            //AsyncTask Parallel Execution : new Registerat().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,ruseret.getText().toString(),rpasset.getText().toString());


                        }
                    });
                    confirm.setCancelable(false);
                    confirm.show();


                }


            }
        });





    }


    @Override //--NOT BY DEFAULT--
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



    public class Registerat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;

        Intent intent;

        String email;
        String user;
        String pass;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            intent = new Intent(RegisterActivity.this,VerificationActivity.class);

            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Sending email..");
            progressDialog.setCancelable(false);
            progressDialog.show();;
        }

        @Override
        protected String doInBackground(String... params) {

             email = params[0];
             user = params[1];
             pass = params[2];


            String registerurl = "http://192.168.43.227/minip/register.php";

            try {
                URL url = new URL(registerurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("email",email).appendQueryParameter("user",user).build().getEncodedQuery();


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
            registrationb.setEnabled(true);


            if(conrefused){

                if(getApplicationContext()!=null) {
                    Toast.makeText(getApplicationContext(), "You're not connected to internet!", Toast.LENGTH_SHORT).show();
                }
                    return;
            }

            if(response.equals("email already registered")){

                remailet.requestFocus();
                remailet.setError("This Email ID is already registered!");

            }

            else if(response.equals("username not available")){

                ruseret.requestFocus();
                ruseret.setError("This Username is not available!");

            }

            else if(response.equals("Message has been sent")){

                intent.putExtra("email",email);
                intent.putExtra("user",user);
                intent.putExtra("pass",pass);

                startActivity(intent);

                finish();

            }
            else
            {
                Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_SHORT).show();
            }

        }
    }


}
