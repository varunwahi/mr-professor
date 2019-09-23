package test.minip;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class UnitsFragment extends Fragment {

    String user;
    String selectedsubject;
    ListView listView;

    public UnitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);  //for overriding TabbedActivity1's onOptionsItemSelected() for the refresh button
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_units, container, false);

        TabbedActivity1 tabbedActivity1 = (TabbedActivity1)getActivity();
        selectedsubject = tabbedActivity1.selectedsubject;

        user = tabbedActivity1.user;

        getActivity().setTitle(selectedsubject);


 listView = (ListView) v.findViewById(R.id.unitsfragmentlv);



        new Unitsat().execute(selectedsubject);





        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){

            if(getView()!=null) {
                new Unitsat().execute(selectedsubject);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.refresh){

            new Unitsat().execute(selectedsubject);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public class Unitsat extends AsyncTask<String,Void,JSONArray> {

        String selectedsubject;

        boolean conrefused=false;
        Intent intent;

        @Override
        protected void onPreExecute() {

            intent = new Intent(getActivity(),TabbedActivity2.class);

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

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
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

            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,units);


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


}
