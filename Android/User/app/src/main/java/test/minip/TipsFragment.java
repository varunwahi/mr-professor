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
public class TipsFragment extends Fragment {


    Boolean oncreateviewshouldruncode = false;


    SearchView searchView;
    MenuItem refreshbutton;
    MenuItem logoutbutton;
    MenuItem aboutbutton;


    String selectedunit,selectedsubject,user;
    FloatingActionButton fab3,fab,fab4;
    ListView listView;

    public TipsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_tips, container, false);




        ViewGroup footerview = (ViewGroup)inflater.inflate(R.layout.listfooter,null);
        footerview.setOnClickListener(null);

        fab3  = (FloatingActionButton)getActivity().findViewById(R.id.fab3);
        fab  = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab4  = (FloatingActionButton)getActivity().findViewById(R.id.fab4);



        listView = (ListView)v.findViewById(R.id.tipslv);

        listView.addFooterView(footerview);

        TabbedActivity2 tabbedActivity2 = (TabbedActivity2)getActivity();
        selectedunit = tabbedActivity2.selectedunit;
        selectedsubject = tabbedActivity2.selectedsubject;
        user = tabbedActivity2.user;

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                dialog.setTitle("Post a Tip!");
                dialog.setView(R.layout.posttip);
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
                            titledialog.setError("Title of the tip cannot be empty!");
                        }
                        else if(contentdialog.getText().toString().trim().equals("")){

                            contentdialog.requestFocus();
                            contentdialog.setError("Content of the tip cannot be empty!");

                        }
                        else {


                            AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext());
                            dialog2.setMessage("Are you sure you want to post this tip?");
                            dialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                    new posttipsat().execute(selectedunit,selectedsubject,user,titledialog.getText().toString().trim(),contentdialog.getText().toString().trim());
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

        fab4.hide();



        if(oncreateviewshouldruncode)
        {
            System.out.println("AAA : oncreateview is running code");
            fab.hide();
            fab3.hide();
            new loadtipsat().execute(selectedunit,selectedsubject);
        }



        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {


        if(isVisibleToUser) {


            if(getView()!=null){

                fab.hide();
                fab3.hide();
                new loadtipsat().execute(selectedunit,selectedsubject);
            }
            else oncreateviewshouldruncode=true;


         } else oncreateviewshouldruncode=false;



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

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



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.refresh){

           new loadtipsat().execute(selectedunit,selectedsubject);
            return true;

        }

        if (item.getItemId()==android.R.id.home){

            if(!searchView.isIconified()) {

                refreshbutton.setVisible(true);
                logoutbutton.setVisible(true);
                aboutbutton.setVisible(true);


                searchView.onActionViewCollapsed();
            }
            else {
                getActivity().finish();
            }
            return true;


        }

        return super.onOptionsItemSelected(item);
    }

    public class loadtipsat extends AsyncTask<String,Void,JSONArray> {

        ProgressDialog progressDialog;


        Intent intent;

        ArrayList<tipsclass> tipsinfo = new ArrayList<>();//original tips info list
        ArrayList<tipsclass> tipsinfo2;       // Visible tips info list (Initially same as the original list , can change according to searchview)


        boolean conrefused=false;

        @Override
        protected void onPreExecute() {
            //  progressDialog = new ProgressDialog(getContext());
            //  progressDialog.setMessage("Loading..");
            //  progressDialog.setCancelable(false);
            //  progressDialog.show();

        }

        @Override
        protected JSONArray doInBackground(String... strings) {

            String selectedunit = strings[0];
            String selectedsubject = strings[1];

            String loginurl = "http://192.168.43.227/minip/tips.php";

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

        public class tipsclass{

            String tipid;
            String byuser;
            String title;
            String content;
            String votes;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            // progressDialog.dismiss();

            if(conrefused){

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }


            for(int i=0;i<jsonArray.length();i++){

                try {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    tipsclass tipsobject = new tipsclass();

                    tipsobject.tipid = jsonObject.getString("id");
                    tipsobject.byuser = jsonObject.getString("byuser");
                    tipsobject.title = jsonObject.getString("title");
                    tipsobject.content = jsonObject.getString("content");
                    tipsobject.votes = jsonObject.getString("votes");

                    tipsinfo.add(tipsobject);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            tipsinfo2 = new ArrayList<>(tipsinfo);


            final CustomAdapter1 adapter = new CustomAdapter1();

            listView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            fab4.show();

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

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    final TextView tipcontent = (TextView)view.findViewById(R.id.tipcontent);

                    PopupMenu popupMenu = new PopupMenu(getContext(),view);
                    if(tipsinfo2.get(i).byuser.equals(user)) {
                        popupMenu.getMenuInflater().inflate(R.menu.longclicktipswithdelete, popupMenu.getMenu());
                    }
                    else
                    {
                        popupMenu.getMenuInflater().inflate(R.menu.longclicktips, popupMenu.getMenu());
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {


                            if(item.getItemId()==R.id.upvoteb){

                                new voteat().execute(selectedsubject,tipsinfo2.get(i).tipid,"upvote");
                                return true;

                            }
                            else if(item.getItemId()==R.id.downvoteb){

                                new voteat().execute(selectedsubject,tipsinfo2.get(i).tipid,"downvote");
                                return true;

                            }
                            else if(item.getItemId()==R.id.deletetip){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Are you sure you want to delete this tip?");
                                builder.setNegativeButton("No",null);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int ii) {
                                        new deletetipat().execute(selectedsubject,tipsinfo2.get(i).tipid);
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
                return tipsinfo2.size();
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
                    view = LayoutInflater.from(getContext()).inflate(R.layout.tipslist,null);
                    holder.tipcontent = (TextView)view.findViewById(R.id.tipcontent);
                    holder.tiptitle = (TextView)view.findViewById(R.id.tiptitle);
                    holder.tipby = (TextView)view.findViewById(R.id.tipby);
                    holder.tipvotes = (TextView)view.findViewById(R.id.tipvotes);

                    holder.thumbsupb = (ImageButton)view.findViewById(R.id.thumbsupb);
                    holder.thumbsdownb = (ImageButton)view.findViewById(R.id.thumbsdownb);
                    holder.deleteb = (ImageButton)view.findViewById(R.id.deleteb);

                    view.setTag(holder);
                }
                else{
                    holder = (ViewHolder)view.getTag();
                }

                holder.tiptitle.setText(tipsinfo2.get(i).title);
                holder.tipcontent.setText(tipsinfo2.get(i).content);
                holder.tipby.setText("By : "+tipsinfo2.get(i).byuser);
                holder.tipvotes.setText(tipsinfo2.get(i).votes);


                holder.thumbsdownb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new voteat().execute(selectedsubject,tipsinfo.get(i).tipid,"downvote");
                    }
                });

                holder.thumbsupb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new voteat().execute(selectedsubject,tipsinfo.get(i).tipid,"upvote");
                    }
                });


                if(tipsinfo2.get(i).byuser.equals(user))
                {
                    holder.deleteb.setVisibility(View.VISIBLE);
                    holder.deleteb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Are you sure you want to delete this tip?");
                            builder.setNegativeButton("No",null);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int ii) {
                                    new deletetipat().execute(selectedsubject,tipsinfo.get(i).tipid);
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
                tipsinfo2.clear();
                if(enteredtext.length()==0){
                    tipsinfo2 = new ArrayList<>(tipsinfo);
                }
                else{

                    for(tipsclass object : tipsinfo){

                        if(object.title.toLowerCase(Locale.getDefault()).contains(enteredtext)||object.content.toLowerCase(Locale.getDefault()).contains(enteredtext)){
                            tipsinfo2.add(object);
                        }

                    }

                }

                notifyDataSetChanged();

            } //for search filter
        }

        class ViewHolder{

            TextView tiptitle;
            TextView tipcontent;
            TextView tipvotes;
            TextView tipby;

            ImageButton thumbsupb;
            ImageButton thumbsdownb;
            ImageButton deleteb;

        }

    }

    public class posttipsat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;

        boolean conrefused=false;

        @Override
        protected void onPreExecute() {
            // progressDialog = new ProgressDialog(getContext());
            // progressDialog.setMessage("Posting your tip..");
            // progressDialog.setCancelable(false);
            // progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String selectedunit = strings[0];
            String selectedsubject = strings[1];
            String user = strings[2];
            String title = strings[3];
            String content = strings[4];

            String loginurl = "http://192.168.43.227/minip/posttips.php";

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

            //    progressDialog.dismiss();

            if(conrefused){

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if(response.equals("success")){
                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Tip Posted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Tip Could Not Be Posted!", Toast.LENGTH_SHORT).show();
                }
            }


            new loadtipsat().execute(selectedunit,selectedsubject);





        }

    }

    public class voteat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {
            // progressDialog = new ProgressDialog(getContext());
            // progressDialog.setMessage("Voting..");
            // progressDialog.setCancelable(false);
            //  progressDialog.show();
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
                        .appendQueryParameter("requiredid",doubtid).appendQueryParameter("vote",vote).appendQueryParameter("doubtoranswer","tip")
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

            // progressDialog.dismiss();

            if(conrefused){

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if(response.equals("already voted")){

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You've already voted on this tip!", Toast.LENGTH_SHORT).show();
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


            new loadtipsat().execute(selectedunit,selectedsubject);





        }





    }

    public class deletetipat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;

        boolean conrefused=false;

        @Override
        protected void onPreExecute() {
            // progressDialog = new ProgressDialog(getContext());
            // progressDialog.setMessage("Deleting your tip..");
            // progressDialog.setCancelable(false);
            // progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String selectedsubject = strings[0];
            String linkid = strings[1];

            String loginurl = "http://192.168.43.227/minip/deletecontent.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("subject", selectedsubject).appendQueryParameter("requiredid",linkid).appendQueryParameter("type","tips")
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

            //progressDialog.dismiss();

            if(conrefused){

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "You're not connected to Internet!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if(response.equals("success")){
                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Tip deleted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Tip could not be deleted!", Toast.LENGTH_SHORT).show();
                }
            }


            new loadtipsat().execute(selectedunit,selectedsubject);





        }





    }


}
