package ooglootenants.andorid.com.tenancyapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tenants_List extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    GoogleApiClient mGoogleApiClient;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String Id = "idkey";
    public static final String Name = "nameKey";
    SharedPreferences sharedpreferences;
    Tenancy_Dbhelper tenancy_dbhelper;
    Context context = this;
    Tenant_class tenant_class;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv;
    FloatingActionButton addnewtenant;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<Tenant_class> Recycler_List = new ArrayList<>();
    String myId;
    String myName;
    HttpURLConnection httpURLConnection;
    URL url;

    private static final String TAG_RESULTS = "result";
    private static final String R_ID = "rid";
    private static final String L_ID = "lid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_CNIC = "cnic";
    private static final String TAG_PAYMENTDATE = "paymentdate";
    private static final String TAG_RENTAMOUNT = "rentamount";
    private static final String TAG_SECURITY = "rentsecurity";
    private static final String TAG_PAYMENTRECEIVED = "paymentreceived";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_RENT_NOTIFICATIONDATE = "rentnotificationdate";

    String myJSON;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenants__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // For getting notification
        Intent alarmIntent = new Intent(this, NotificationManager.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);

        Intent i = getIntent();
        myId = i.getStringExtra("id");
        myName = i.getStringExtra("name");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        addnewtenant = (FloatingActionButton) findViewById(R.id.fab);

        tv = (TextView) findViewById(R.id.tv_landlordtenants);
        tv.setText("Welcome " + myName);
        recyclerView = (RecyclerView) findViewById(R.id.Rv_landlordtenants);
        recyclerViewAdapter = new RecyclerViewAdapter(Recycler_List, this);
        RecyclerView.LayoutManager MyLayoutmanager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(MyLayoutmanager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);
        tenancy_dbhelper = new Tenancy_Dbhelper(context);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            ShowTenants showTenants = new ShowTenants();
            showTenants.execute(myId);
        } else {
            Cursor cursor = tenancy_dbhelper.getallrenters(myId);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            String tRid, tLid, tname, tphone, temail, tcnic, tpaymentdate, trentamount, trentsecurity, tpaymentreceived, tnotifications, trentnotificationdate;
                            tRid = cursor.getString(0);
                            tLid = cursor.getString(1);
                            tname = cursor.getString(2);
                            tphone = cursor.getString(3);
                            temail = cursor.getString(4);
                            tcnic = cursor.getString(5);
                            tpaymentdate = cursor.getString(6);
                            trentamount = cursor.getString(7);
                            trentsecurity = cursor.getString(8);
                            tpaymentreceived = cursor.getString(9);
                            tnotifications = cursor.getString(10);
                            trentnotificationdate = cursor.getString(11);

                            tenant_class = new Tenant_class(tRid, tLid, tname, tphone, temail, tcnic, tpaymentdate, trentamount, trentsecurity, tpaymentreceived, tnotifications, trentnotificationdate);
                            Recycler_List.add(tenant_class);
                        } while (cursor.moveToNext());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        addnewtenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Intent intent = new Intent(Tenants_List.this, Add_Newtenant.class);
                    intent.putExtra("id", myId);
                    intent.putExtra("name", myName);
                    startActivity(intent);
                } else {
                    Toast.makeText(Tenants_List.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        FetchData fetchData = new FetchData();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            fetchData.execute(myId);
        } else {
            Toast.makeText(Tenants_List.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    class ShowTenants extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = new ProgressDialog(Tenants_List.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            try {
                try {
                    url = new URL("http://tenancyapp.thewebsupportdesk.com/Tenant_List.php");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("lid", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String text;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                if (reader != null) {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            text = sb.toString();
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpURLConnection.disconnect();
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            Tenancy_Dbhelper tenancy_dbhelper = new Tenancy_Dbhelper(context);
            if (s.equals("No Tenant Found")) {
                Toast.makeText(Tenants_List.this, s, Toast.LENGTH_SHORT).show();
                tenancy_dbhelper.deletetenentcolumns();
                recyclerViewAdapter.notifyDataSetChanged();
                Recycler_List.clear();





                new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert)
//                        .setTitle("Delete Task")
                        .setMessage("Click on Add button to Add new Tenant!")

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.calender)
                        .show();

            } else {
                myJSON = s;
                showList();
            }
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }

    protected void showList() {
        personList = new ArrayList<>();
        try {
            HashMap<String, String> persons;
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String rid = c.getString(R_ID);
                String lid = c.getString(L_ID);
                String name = c.getString(TAG_NAME);
                String phone = c.getString(TAG_PHONE);
                String email = c.getString(TAG_EMAIL);
                String cnic = c.getString(TAG_CNIC);
                String paymentdate = c.getString(TAG_PAYMENTDATE);
                String rentamount = c.getString(TAG_RENTAMOUNT);
                String rentsecurity = c.getString(TAG_SECURITY);
                String paymentreceived = c.getString(TAG_PAYMENTRECEIVED);
                String notifications = c.getString(TAG_NOTIFICATIONS);
                String rentnotificationdate = c.getString(TAG_RENT_NOTIFICATIONDATE);
                persons = new HashMap<String, String>();
                persons.put(R_ID, rid);
                persons.put(L_ID, lid);
                persons.put(TAG_NAME, name);
                persons.put(TAG_PHONE, phone);
                persons.put(TAG_EMAIL, email);
                persons.put(TAG_CNIC, cnic);
                persons.put(TAG_PAYMENTDATE, paymentdate);
                persons.put(TAG_RENTAMOUNT, rentamount);
                persons.put(TAG_SECURITY, rentsecurity);
                persons.put(TAG_PAYMENTRECEIVED, paymentreceived);
                persons.put(TAG_NOTIFICATIONS, notifications);
                persons.put(TAG_RENT_NOTIFICATIONDATE, rentnotificationdate);
                try {
                    personList.add(persons);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tenancy_dbhelper.deletetenentcolumns();

            for (int i = 0; i < peoples.length(); i++) {
                String rid = personList.get(i).get(R_ID);
                String lid = personList.get(i).get(L_ID);
                String name = personList.get(i).get(TAG_NAME);
                String phone = personList.get(i).get(TAG_PHONE);
                String email = personList.get(i).get(TAG_EMAIL);
                String cnic = personList.get(i).get(TAG_CNIC);
                String paymentdate = personList.get(i).get(TAG_PAYMENTDATE);
                String rent = personList.get(i).get(TAG_RENTAMOUNT);
                String security = personList.get(i).get(TAG_SECURITY);
                String paymentreceived = personList.get(i).get(TAG_PAYMENTRECEIVED);
                String notifications = personList.get(i).get(TAG_NOTIFICATIONS);
                String rentnotificationdate = personList.get(i).get(TAG_RENT_NOTIFICATIONDATE);

                tenancy_dbhelper.synchronize(rid, lid, name, phone, email, cnic, paymentdate, rent, security, paymentreceived, notifications, rentnotificationdate);
            }
            Recycler_List.clear();
            Cursor cursor = tenancy_dbhelper.getallrenters(myId);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            String tRid, tLid, tname, tphone, temail, tcnic, tpaymentdate, trentamount, trentsecurity, tpaymentreceived, tnotifications, trentpaymentdate;
                            tRid = cursor.getString(0);
                            tLid = cursor.getString(1);
                            tname = cursor.getString(2);
                            tphone = cursor.getString(3);
                            temail = cursor.getString(4);
                            tcnic = cursor.getString(5);
                            tpaymentdate = cursor.getString(6);
                            trentamount = cursor.getString(7);
                            trentsecurity = cursor.getString(8);
                            tpaymentreceived = cursor.getString(9);
                            tnotifications = cursor.getString(10);
                            trentpaymentdate = cursor.getString(11);

                            tenant_class = new Tenant_class(tRid, tLid, tname, tphone, temail, tcnic, tpaymentdate, trentamount, trentsecurity, tpaymentreceived, tnotifications, trentpaymentdate);
                            Recycler_List.add(tenant_class);
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class FetchData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            try {
                    try {
                        url = new URL("http://tenancyapp.thewebsupportdesk.com/Tenant_List.php");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("lid", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String text;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                if (reader != null) {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            text = sb.toString();
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpURLConnection.disconnect();
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            Tenancy_Dbhelper tenancy_dbhelper = new Tenancy_Dbhelper(context);
            if (s.equals("No Tenant Found")) {

                Toast.makeText(Tenants_List.this, s, Toast.LENGTH_SHORT).show();
                tenancy_dbhelper.deletetenentcolumns();
                recyclerViewAdapter.notifyDataSetChanged();
                Recycler_List.clear();
            } else {
                myJSON = s;
                showList();
            }
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(s);
        }
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if (res_id == (R.id.logout)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove(Id);
                editor.remove(Email);
                editor.remove(Phone);
                editor.remove(Name);
                editor.clear();
                editor.apply();
                LoginManager.getInstance().logOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Toast.makeText(Tenants_List.this, "Logged out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Tenants_List.this, Landlord_Login.class);
                        startActivity(intent);
                    }
                });
                Intent intent = new Intent(Tenants_List.this, Landlord_Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(Tenants_List.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

