package test.minipadmin;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Button;
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
public class LinksFragment extends Fragment {

    Boolean oncreateviewshouldruncode = false;

    SearchView searchView;
    MenuItem refreshbutton;
    MenuItem logoutbutton;
    MenuItem aboutbutton;


    String selectedunit,selectedsubject,user,uora;
    FloatingActionButton fab3,fab,fab4;
    ListView listView;

    public LinksFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_links, container, false);

        ViewGroup footerview = (ViewGroup)inflater.inflate(R.layout.listfooter,null);
        footerview.setOnClickListener(null);

        fab3  = (FloatingActionButton)getActivity().findViewById(R.id.fab3);
        fab  = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab4  = (FloatingActionButton)getActivity().findViewById(R.id.fab4);


        listView = (ListView)v.findViewById(R.id.linkslv);

        listView.addFooterView(footerview);

        TabbedActivity2 tabbedActivity2 = (TabbedActivity2)getActivity();
        selectedunit = tabbedActivity2.selectedunit;
        selectedsubject = tabbedActivity2.selectedsubject;
        user = tabbedActivity2.user;
        uora = tabbedActivity2.uora;


        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                dialog.setTitle("Post a link!");
                dialog.setView(R.layout.postalink);
                dialog.setPositiveButton("Post!", null);
                dialog.setNegativeButton("Cancel", null);
                dialog.setCancelable(false);


                final AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        final EditText topicdialog = (EditText)alertDialog.findViewById(R.id.topicdialog);
                        final EditText urldialog = (EditText)alertDialog.findViewById(R.id.urldialog);

                        if(topicdialog.getText().toString().trim().equals("")){
                            topicdialog.requestFocus();
                            topicdialog.setError("Topic of the link cannot be empty!");
                        }
                        else if(urldialog.getText().toString().trim().equals("")){

                            urldialog.requestFocus();
                            urldialog.setError("URL cannot be empty!");

                        }
                        else {

                            if(!urldialog.getText().toString().trim().toLowerCase().startsWith("https://"))
                            {
                                if(!urldialog.getText().toString().trim().toLowerCase().startsWith("http://"))
                                urldialog.setText("http://" + urldialog.getText().toString().trim());
                            }


                            AlertDialog.Builder dialog2 = new AlertDialog.Builder(getContext());
                            dialog2.setMessage("Are you sure you want to post this link?");
                            dialog2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                    new postlinkat().execute(selectedunit,selectedsubject,user,topicdialog.getText().toString().trim(),urldialog.getText().toString().trim());
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

        fab3.hide();

        if(oncreateviewshouldruncode)
        {
            fab.hide();
            fab4.hide();
            new loadlinksat().execute(selectedunit, selectedsubject);
        }


        return  v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){

            if(getView()!=null) {

                fab.hide();
                fab4.hide();
                new loadlinksat().execute(selectedunit, selectedsubject);
            }
            else oncreateviewshouldruncode=true;

        }
        oncreateviewshouldruncode = false;
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

            new loadlinksat().execute(selectedunit,selectedsubject);
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

    public class loadlinksat extends AsyncTask<String,Void,JSONArray> {

        ProgressDialog progressDialog;

        Intent intent;

        ArrayList<linksclass> linksinfo = new ArrayList<>(); //original doubts info list
        ArrayList<linksclass> linksinfo2;       // Visible doubts info list (Initially same as the original list , can change according to searchview)


        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            //    progressDialog = new ProgressDialog(getContext());
            //  progressDialog.setMessage("Loading..");
            //  progressDialog.setCancelable(false);
            //   progressDialog.show();

        }

        @Override
        protected JSONArray doInBackground(String... strings) {

            String selectedunit = strings[0];
            String selectedsubject = strings[1];

            String loginurl = "http://192.168.43.227/minip/links.php";

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

        public class linksclass{

            String linkid;
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

                    linksclass linkobject = new linksclass();

                    linkobject.linkid = jsonObject.getString("id");
                    linkobject.byuser = jsonObject.getString("byuser");
                    linkobject.title = jsonObject.getString("title");
                    linkobject.content = jsonObject.getString("url");
                    linkobject.votes = jsonObject.getString("votes");

                    linksinfo.add(linkobject);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            linksinfo2 = new ArrayList<>(linksinfo);


            final CustomAdapter1 adapter = new CustomAdapter1();

            listView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            fab3.show();

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
                public void onItemClick(AdapterView<?> adapterView, final View view, final int i, long l) {


                    AlertDialog.Builder builder = new  AlertDialog.Builder(getContext());
                    builder.setView(R.layout.linkclick);
                    builder.setNegativeButton("Cancel",null);
                    builder.setCancelable(false);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    final TextView linkcontent = (TextView)view.findViewById(R.id.linkcontent);
                    final Button openlink = (Button) alertDialog.findViewById(R.id.openlink);
                    final Button copytoc = (Button)alertDialog.findViewById(R.id.copytoc);

                    if (openlink != null) {
                        openlink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(linkcontent.getText().toString()));
                                startActivity(intent);
                                alertDialog.hide();

                            }
                        });
                    }

                    if (copytoc != null) {
                        copytoc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ClipboardManager clipboardManager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("link",linkcontent.getText().toString());
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.makeText(getContext(),"Link Copied to Clipboard!",Toast.LENGTH_SHORT).show();
                                alertDialog.hide();
                            }
                        });
                    }



                    /* for Popup Menu
                    final TextView linkcontent = (TextView)view.findViewById(R.id.linkcontent);

                    PopupMenu popupMenu = new PopupMenu(getContext(),view);
                    popupMenu.getMenuInflater().inflate(R.menu.linkoptions,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getItemId()==R.id.openlink){

                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(linkcontent.getText().toString()));
                                startActivity(intent);

                                return true;

                            }
                            else if(item.getItemId()==R.id.copytoc){

                                ClipboardManager clipboardManager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("link",linkcontent.getText().toString());
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.makeText(getContext(),"Link Copied to Clipboard!",Toast.LENGTH_SHORT).show();


                                return true;

                            }


                            return false;
                        }
                    });
                    popupMenu.show();

*/

                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    final TextView linkcontent = (TextView)view.findViewById(R.id.linkcontent);

                    PopupMenu popupMenu = new PopupMenu(getContext(),view);
                    if(linksinfo2.get(i).byuser.equals(user)||uora.equals("a")) {
                        popupMenu.getMenuInflater().inflate(R.menu.longclicklinkswithdelete, popupMenu.getMenu());
                    }
                    else
                    {
                        popupMenu.getMenuInflater().inflate(R.menu.longclicklinks, popupMenu.getMenu());
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getItemId()==R.id.openlink){

                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(linkcontent.getText().toString()));
                                startActivity(intent);
                                return true;

                            }
                            else if(item.getItemId()==R.id.copytoc){

                                ClipboardManager clipboardManager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("link",linkcontent.getText().toString());
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.makeText(getContext(),"Link Copied to Clipboard!",Toast.LENGTH_SHORT).show();
                                return true;

                            }
                            if(item.getItemId()==R.id.upvoteb){

                                new voteat().execute(selectedsubject,linksinfo2.get(i).linkid,"upvote");
                                return true;

                            }
                            else if(item.getItemId()==R.id.downvoteb){

                                new voteat().execute(selectedsubject,linksinfo2.get(i).linkid,"downvote");
                                return true;

                            }
                            else if(item.getItemId()==R.id.deletelink){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Are you sure you want to delete this link?");
                                builder.setNegativeButton("No",null);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int ii) {
                                        new deletelinkat().execute(selectedsubject,linksinfo2.get(i).linkid);
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
                return linksinfo2.size();
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
                    view = LayoutInflater.from(getContext()).inflate(R.layout.linkslist,null);
                    holder.linkcontent = (TextView)view.findViewById(R.id.linkcontent);
                    holder.linktitle = (TextView)view.findViewById(R.id.linktitle);
                    holder.linkby = (TextView)view.findViewById(R.id.linkby);
                    holder.linkvotes = (TextView)view.findViewById(R.id.linkvotes);

                    holder.thumbsupb = (ImageButton)view.findViewById(R.id.thumbsupb);
                    holder.thumbsdownb = (ImageButton)view.findViewById(R.id.thumbsdownb);
                    holder.deleteb = (ImageButton)view.findViewById(R.id.deleteb);

                    view.setTag(holder);
                }
                else{
                    holder = (ViewHolder)view.getTag();
                }

                holder.linktitle.setText(linksinfo2.get(i).title);
                holder.linkcontent.setText(linksinfo2.get(i).content);
                holder.linkby.setText("By : "+linksinfo2.get(i).byuser);
                holder.linkvotes.setText(linksinfo2.get(i).votes);


                holder.thumbsdownb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new voteat().execute(selectedsubject,linksinfo2.get(i).linkid,"downvote");
                    }
                });

                holder.thumbsupb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new voteat().execute(selectedsubject,linksinfo2.get(i).linkid,"upvote");
                    }
                });


                if(linksinfo2.get(i).byuser.equals(user)||uora.equals("a"))
                {
                    holder.deleteb.setVisibility(View.VISIBLE);
                    holder.deleteb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Are you sure you want to delete this link?");
                            builder.setNegativeButton("No",null);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int ii) {
                                    new deletelinkat().execute(selectedsubject,linksinfo2.get(i).linkid);
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
                linksinfo2.clear();
                if(enteredtext.length()==0){
                    linksinfo2 = new ArrayList<>(linksinfo);
                }
                else{

                    for(linksclass object : linksinfo){

                        if(object.title.toLowerCase(Locale.getDefault()).contains(enteredtext)){
                            linksinfo2.add(object);
                        }

                    }

                }

                notifyDataSetChanged();

            } //for search filter
        }

        class ViewHolder{

            TextView linktitle;
            TextView linkcontent;
            TextView linkvotes;
            TextView linkby;

            ImageButton thumbsupb;
            ImageButton thumbsdownb;
            ImageButton deleteb;

        }

    }

    public class postlinkat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {

            // progressDialog = new ProgressDialog(getContext());
            //   progressDialog.setMessage("Posting your link..");
            //   progressDialog.setCancelable(false);
            //   progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String selectedunit = strings[0];
            String selectedsubject = strings[1];
            String user = strings[2];
            String title = strings[3];
            String urll = strings[4];

            String loginurl = "http://192.168.43.227/minip/postlinks.php";

            try {
                URL url = new URL(loginurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(1 * 1000);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));


                String postdata = new Uri.Builder().appendQueryParameter("unit", selectedunit).appendQueryParameter("subject", selectedsubject)
                        .appendQueryParameter("user",user).appendQueryParameter("title",title).appendQueryParameter("url",urll)
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
                    Toast.makeText(getContext(), "Link Posted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Link Could Not Be Posted!", Toast.LENGTH_SHORT).show();
                }
            }


            new loadlinksat().execute(selectedunit,selectedsubject);





        }

    }

    public class voteat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {
            //  progressDialog = new ProgressDialog(getContext());
            //  progressDialog.setMessage("Voting..");
            //   progressDialog.setCancelable(false);
            //   progressDialog.show();
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
                        .appendQueryParameter("requiredid",doubtid).appendQueryParameter("vote",vote).appendQueryParameter("doubtoranswer","link")
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
                    Toast.makeText(getContext(), "You've already voted on this link!", Toast.LENGTH_SHORT).show();
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


            new loadlinksat().execute(selectedunit,selectedsubject);





        }





    }

    public class deletelinkat extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;
        boolean conrefused=false;

        @Override
        protected void onPreExecute() {
            // progressDialog = new ProgressDialog(getContext());
            //   progressDialog.setMessage("Deleting your link..");
            //   progressDialog.setCancelable(false);
            //   progressDialog.show();
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


                String postdata = new Uri.Builder().appendQueryParameter("subject", selectedsubject).appendQueryParameter("requiredid",linkid).appendQueryParameter("type","links")
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
                    Toast.makeText(getContext(), "Link deleted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                if(getContext()!=null) {
                    Toast.makeText(getContext(), "Link could not be deleted!", Toast.LENGTH_SHORT).show();
                }
            }


            new loadlinksat().execute(selectedunit,selectedsubject);





        }





    }

}
