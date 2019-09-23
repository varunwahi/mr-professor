package test.minip;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoubtsFragment extends Fragment {
    String user;

    Boolean oncreateviewshouldruncode=false;

    SearchView searchView;
    MenuItem refreshbutton;
    MenuItem logoutbutton;
    MenuItem aboutbutton;




    FloatingActionButton fab,fab3,fab4;
    String selectedunit;
    String selectedsubject;
    ListView listView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_doubts, container, false);



        ViewGroup footerview = (ViewGroup)inflater.inflate(R.layout.listfooter,null);
        footerview.setOnClickListener(null);




        final TabbedActivity2 tabbedActivity2 = (TabbedActivity2)getActivity();
        selectedunit = tabbedActivity2.selectedunit;
        selectedsubject = tabbedActivity2.selectedsubject;
        user = tabbedActivity2.user;



        getActivity().setTitle(selectedunit);


        fab3 = (FloatingActionButton)getActivity().findViewById(R.id.fab3);
        fab4 = (FloatingActionButton)getActivity().findViewById(R.id.fab4);





        fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                dialog.setTitle("Ask a doubt!");
                dialog.setView(R.layout.askadoubt);
                dialog.setPositiveButton("Ask!", null);
                dialog.setNegativeButton("Cancel", null);
                dialog.setCancelable(false);


                final AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        final EditText titledialog = (EditText)alertDialog.findViewById(R.id.titledialog);
                        final EditText contentdialog = (EditText)alertDialog.findViewById(R.id.contentdialog);

                        if(titledialog.getText().toString().trim().equals("")){
                            titledialog.requestFocus();
                            titledialog.setError("Title of the doubt cannot be empty!");
                        }
                        else if(contentdialog.getText().toString().trim().equals("")){

                            contentdialog.requestFocus();
                            contentdialog.setError("Content of the doubt cannot be empty!");

                        }
                        else {


                            AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext());
                            dialog2.setMessage("Are you sure you want to post this doubt?");
                            dialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                    new postdoubtsat().execute(selectedunit,selectedsubject,user,titledialog.getText().toString().trim(),contentdialog.getText().toString().trim());
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

                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext());
                        dialog2.setMessage("Are you sure you want to cancel? (No doubt is a silly doubt :P)");
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


        listView = (ListView)v.findViewById(R.id.doubtslv);

        listView.addFooterView(footerview);


        if(oncreateviewshouldruncode) {

            fab3.hide();
            fab4.hide();

            loaddoubtsat loaddoubtsat = new loaddoubtsat();

            new loaddoubtsat().execute(selectedunit, selectedsubject);
        }


        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if(isVisibleToUser){


            if(getView()!=null) {
                fab3.hide();
                fab4.hide();
                new loaddoubtsat().execute(selectedunit,selectedsubject);
            }
            else oncreateviewshouldruncode=true;
        }
        else oncreateviewshouldruncode=false;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {

        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)menu.findItem(R.id.actionbarsearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        refreshbutton = menu.getItem(1);
        logoutbutton = menu.getItem(2);
        aboutbutton = menu.getItem(3);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refreshbutton.setVisible(false);
                logoutbutton.setVisible(false);
                aboutbutton.setVisible(false);

            }
        });  //hiding other icons when search button is clicked

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                refreshbutton.setVisible(true);
                logoutbutton.setVisible(true);
                aboutbutton.setVisible(true);

                searchView.onActionViewCollapsed();

                return true;
            }
        });  // displaying the hidden icons on clicking the X (close ) button


        super.onCreateOptionsMenu(menu, inflater);
    } //not used for inflating menu since it is already done in the parent activity ( therefore calling super.oncreateoptionsmenu in the end) , only used for implement searchview for doubts

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.refresh){

            new loaddoubtsat().execute(selectedunit,selectedsubject);
            return true;

        }

        if (item.getItemId()==android.R.id.home){

            if(!searchView.isIconified()) {

                refreshbutton.setVisible(true);
                logoutbutton.setVisible(true);
                aboutbutton.setVisible(true);


                searchView.onActionViewCollapsed();
            }
            else getActivity().finish();

            return true;


        }

        return super.onOptionsItemSelected(item);
    }


    public class loaddoubtsat extends AsyncTask<String,Void,JSONArray>{

        ProgressDialog progressDialog;
        Intent intent;


        ArrayList<doubtclass> doubtsinfo = new ArrayList<>(); //original doubts info list
        ArrayList<doubtclass> doubtsinfo2;            // Visible doubts info list (Initially same as the original list , can change according to searchview)

        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            // progressDialog = new ProgressDialog(getContext());
            //  progressDialog.setMessage("Loading..");
            //   progressDialog.setCancelable(false);
            //   progressDialog.show();


            intent = new Intent(getActivity(),AnswersActivity.class);
        }

        @Override
        protected JSONArray doInBackground(String... strings) {

            String selectedunit = strings[0];
            String selectedsubject = strings[1];

            String loginurl = "http://192.168.43.227/minip/doubts.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("unit", selectedunit).appendQueryParameter("subject", selectedsubject)
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

        public class doubtclass{

            String doubtid;
            String byuser;
            String title;
            String content;
            String votes;
            String noa;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            //   progressDialog.dismiss();

            if(conrefused){

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }



            for(int i=0;i<jsonArray.length();i++){

                try {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    doubtclass doubtobject = new doubtclass();

                    doubtobject.doubtid = jsonObject.getString("id");
                    doubtobject.byuser = jsonObject.getString("byuser");
                    doubtobject.title = jsonObject.getString("title");
                    doubtobject.content = jsonObject.getString("content");
                    doubtobject.votes = jsonObject.getString("votes");
                    doubtobject.noa = jsonObject.getString("noa");

                    doubtsinfo.add(doubtobject);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            doubtsinfo2 = new ArrayList<>(doubtsinfo);




            final CustomAdapter1 adapter = new CustomAdapter1();

            listView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            fab.show();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText);
                    return true;
                }
            });



            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    TextView titleofselected = (TextView)view.findViewById(R.id.titlelvtv);
                    TextView contentofselected = (TextView)view.findViewById(R.id.contentlvtv);
                    TextView byofselected = (TextView)view.findViewById(R.id.bylvtv);
                    TextView votesofselected = (TextView)view.findViewById(R.id.voteslvtv);
                    TextView noa = (TextView)view.findViewById(R.id.noalvtv);

                    intent.putExtra("titleofselected",titleofselected.getText().toString());
                    intent.putExtra("contentofselected",contentofselected.getText().toString());
                    intent.putExtra("byofselected",byofselected.getText().toString());
                    intent.putExtra("votesofselected",votesofselected.getText().toString());
                    intent.putExtra("doubtid",doubtsinfo2.get(i).doubtid);
                    intent.putExtra("selectedsubject",selectedsubject);
                    intent.putExtra("user",user);


                    startActivity(intent);

                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),view);

                    if(doubtsinfo2.get(i).byuser.equals(user)) {
                        popupMenu.getMenuInflater().inflate(R.menu.longclickdoubtswithdelete, popupMenu.getMenu());
                    }
                    else
                    {
                        popupMenu.getMenuInflater().inflate(R.menu.longclickdoubts, popupMenu.getMenu());
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getItemId()==R.id.upvoteb){

                                new voteat().execute(selectedsubject,doubtsinfo2.get(i).doubtid,"upvote");
                                return true;

                            }
                            else if(item.getItemId()==R.id.downvoteb){

                                new voteat().execute(selectedsubject,doubtsinfo2.get(i).doubtid,"downvote");
                                return true;

                            }
                            else if(item.getItemId()==R.id.deletedoubt){

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Are you sure you want to delete this doubt? All the answers will also be deleted");
                                builder.setNegativeButton("No",null);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int ii) {
                                        new deletedoubtat().execute(selectedsubject,doubtsinfo2.get(i).doubtid);
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

        class CustomAdapter1 extends BaseAdapter{

            PopupMenu popupMenu;


            @Override
            public int getCount() {
                //return byuser.size();
                return doubtsinfo2.size();
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
                    view = LayoutInflater.from(getContext()).inflate(R.layout.doubtlist,null);
                    holder.contentlvtv = (TextView)view.findViewById(R.id.contentlvtv);
                    holder.titlelvtv = (TextView)view.findViewById(R.id.titlelvtv);
                    holder.byuserlvtv = (TextView)view.findViewById(R.id.bylvtv);
                    holder.voteslvtv = (TextView)view.findViewById(R.id.voteslvtv);
                    holder.thumbsupb = (ImageButton)view.findViewById(R.id.thumbsupb);
                    holder.thumbsdownb = (ImageButton)view.findViewById(R.id.thumbsdownb);
                    holder.deleteb = (ImageButton)view.findViewById(R.id.deleteb);
                    holder.noalvtv = (TextView)view.findViewById(R.id.noalvtv);
                    view.setTag(holder);
                }
                else{
                    holder = (ViewHolder)view.getTag();
                }


                  holder.titlelvtv.setText(doubtsinfo2.get(i).title);
                  holder.contentlvtv.setText(doubtsinfo2.get(i).content);
                  holder.byuserlvtv.setText("By : "+doubtsinfo2.get(i).byuser);
                  holder.voteslvtv.setText(doubtsinfo2.get(i).votes);
                  holder.noalvtv.setText("Answers : "+doubtsinfo2.get(i).noa);

                holder.thumbsupb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new voteat().execute(selectedsubject,doubtsinfo2.get(i).doubtid,"upvote");
                    }
                });

                holder.thumbsdownb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new voteat().execute(selectedsubject,doubtsinfo2.get(i).doubtid,"downvote");
                    }
                });


                if(doubtsinfo2.get(i).byuser.equals(user))
                {
                    holder.deleteb.setVisibility(View.VISIBLE);
                    holder.deleteb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Are you sure you want to delete this doubt? All the answers will also be deleted");
                            builder.setNegativeButton("No",null);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int ii) {
                                    new deletedoubtat().execute(selectedsubject,doubtsinfo2.get(i).doubtid);
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

            public void filter(String enteredtext){

                enteredtext = enteredtext.toLowerCase(Locale.getDefault());
                doubtsinfo2.clear();
                if(enteredtext.length()==0){
                    doubtsinfo2 = new ArrayList<>(doubtsinfo);
                }
                else{

                    for(doubtclass object : doubtsinfo){

                        if(object.title.toLowerCase(Locale.getDefault()).contains(enteredtext)||object.content.toLowerCase(Locale.getDefault()).contains(enteredtext)){
                            doubtsinfo2.add(object);
                        }

                    }

                }

                notifyDataSetChanged();

            } //for search filter

        }

         class ViewHolder{

            TextView titlelvtv;
            TextView contentlvtv;
            TextView voteslvtv;

            TextView byuserlvtv;
             TextView noalvtv;

             ImageButton thumbsupb;
             ImageButton thumbsdownb;
             ImageButton deleteb;

        }

    }

    public class postdoubtsat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            //  progressDialog = new ProgressDialog(getContext());
            //   progressDialog.setMessage("Posting your doubt..");
            //    progressDialog.setCancelable(false);
            //    progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String selectedunit = strings[0];
            String selectedsubject = strings[1];
            String user = strings[2];
            String title = strings[3];
            String content = strings[4];

            String loginurl = "http://192.168.43.227/minip/postdoubts.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("unit", selectedunit).appendQueryParameter("subject", selectedsubject)
                        .appendQueryParameter("user",user).appendQueryParameter("title",title).appendQueryParameter("content",content)
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

            // progressDialog.dismiss();

            if(conrefused){

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if(response.equals("success")){
                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Doubt Posted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Doubt Could Not Be Posted!", Toast.LENGTH_SHORT).show();
                }
            }


            new loaddoubtsat().execute(selectedunit,selectedsubject);





            }

        }

    public class voteat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            //    progressDialog = new ProgressDialog(getContext());
            //  progressDialog.setMessage("Voting..");
            //   progressDialog.setCancelable(false);
            //    progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String selectedsubject = strings[0];
            String doubtid = strings[1];
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
                        .appendQueryParameter("requiredid",doubtid).appendQueryParameter("vote",vote).appendQueryParameter("doubtoranswer","doubt")
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

            //    progressDialog.dismiss();

            if(conrefused){

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if(response.equals("already voted")){

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You've already voted on this doubt!", Toast.LENGTH_SHORT).show();
                }

            }

            else if(response.equals("success")){
                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Voted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Vote Could Not Be Posted!", Toast.LENGTH_SHORT).show();
                }
            }


            new loaddoubtsat().execute(selectedunit,selectedsubject);





        }





    }

    public class deletedoubtat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            // progressDialog = new ProgressDialog(getContext());
            //  progressDialog.setMessage("Deleting your doubt..");
            //  progressDialog.setCancelable(false);
            //   progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String selectedsubject = strings[0];
            String doubtid = strings[1];

            String loginurl = "http://192.168.43.227/minip/deletecontent.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("subject", selectedsubject).appendQueryParameter("requiredid",doubtid).appendQueryParameter("type","doubts")
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

            //   progressDialog.dismiss();

            if(conrefused){

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if(response.equals("success")){
                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Doubt deleted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Doubt could not be deleted!", Toast.LENGTH_SHORT).show();
                }
            }


            new loaddoubtsat().execute(selectedunit,selectedsubject);





        }





    }


}
