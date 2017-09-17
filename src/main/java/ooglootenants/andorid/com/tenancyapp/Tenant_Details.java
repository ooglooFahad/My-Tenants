package ooglootenants.andorid.com.tenancyapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Tenant_Details extends AppCompatActivity {

    private AdView mAdView;
    Context context = this;
    String myrid, mylid, myname, myphone, mycnic, myemail, mypaymentdate, myrent, mysecurity, mypaymentreceived, mynotification, myrentnotificationdate;
    TextView name, phone, cnic, email, paymentdate, rent, security, paymentreceived, notification, rentnotificationdate;
    HttpURLConnection httpURLConnection;
    URL url;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String Name = "nameKey";
    public static final String Id = "idkey";
    SharedPreferences sharedpreferences;
    String sid, sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tenant__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sname = sharedpreferences.getString(Name, "");
        sid = sharedpreferences.getString(Id, "");

        name = (TextView) findViewById(R.id.detail_name);
        phone = (TextView) findViewById(R.id.detail_phone);
        cnic = (TextView) findViewById(R.id.detail_cnic);
        email = (TextView) findViewById(R.id.detail_email);
        paymentdate = (TextView) findViewById(R.id.detail_paymentdate);
        rent = (TextView) findViewById(R.id.detail_rent);
        security = (TextView) findViewById(R.id.detail_security);
        paymentreceived = (TextView) findViewById(R.id.detail_paymentreceived);
        notification = (TextView) findViewById(R.id.detail_notification);
        rentnotificationdate = (TextView) findViewById(R.id.detail_rentnotificationdate);

        myrid = getIntent().getStringExtra("rid");
        mylid = getIntent().getStringExtra("lid");
        myname = getIntent().getStringExtra("name");
        name.setText(myname);
        myphone = getIntent().getStringExtra("phone");
        phone.setText(myphone);
        myemail = getIntent().getStringExtra("email");
        email.setText(myemail);
        mycnic = getIntent().getStringExtra("cnic");
        cnic.setText(mycnic);
        myrent = getIntent().getStringExtra("rent");
        rent.setText(myrent);
        mysecurity = getIntent().getStringExtra("security");
        security.setText(mysecurity);
        mypaymentdate = getIntent().getStringExtra("paymentdate");
        paymentdate.setText(mypaymentdate);

        mypaymentreceived = getIntent().getStringExtra("paymentreceived");
        paymentreceived.setText(mypaymentreceived);
        mynotification = getIntent().getStringExtra("notification");
        notification.setText(mynotification);
        myrentnotificationdate = getIntent().getStringExtra("rentnotificationdate");
        rentnotificationdate.setText(myrentnotificationdate);
    }

    class DeleteTenant extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = new ProgressDialog(Tenant_Details.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String rid, lid, mname, memail;
            rid = params[0];
            lid = params[1];
            mname = params[2];
            memail = params[3];

            try {
                try {
                    url = new URL("http://tenancyapp.thewebsupportdesk.com/Delete_Tenant.php");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("rid", "UTF-8") + "=" + URLEncoder.encode(rid, "UTF-8") + "&" +
                        URLEncoder.encode("lid", "UTF-8") + "=" + URLEncoder.encode(lid, "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(mname, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(memail, "UTF-8");
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
            progressDialog.dismiss();
            super.onPostExecute(s);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addordelete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if (res_id == (R.id.edit)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                Intent intent = new Intent(Tenant_Details.this, Update_Tenant.class);
                intent.putExtra("rid", myrid);
                intent.putExtra("lid", mylid);
                intent.putExtra("name", myname);
                intent.putExtra("phone", myphone);
                intent.putExtra("cnic", mycnic);
                intent.putExtra("email", myemail);
                intent.putExtra("rent", myrent);
                intent.putExtra("security", mysecurity);
                intent.putExtra("paymentdate", mypaymentdate);
                intent.putExtra("paymentreceived", mypaymentreceived);
                intent.putExtra("notification", mynotification);
                intent.putExtra("rentnotificationdate", myrentnotificationdate);
                intent.putExtra("id", sid);
                intent.putExtra("lname", sname);
                startActivity(intent);
            } else {
                Toast.makeText(Tenant_Details.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } else {

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete this Task?")

                        .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                DeleteTenant deleteTenant = new DeleteTenant();
                                deleteTenant.execute(myrid, mylid, myname, myemail);
                                Intent intent = new Intent(Tenant_Details.this, Tenants_List.class);
                                intent.putExtra("id", sid);
                                intent.putExtra("name", sname);
                                Toast.makeText(getApplicationContext(), "Deleted!!!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        })
                        .setIcon(R.drawable.calender)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(), "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    //For Adview life cycle
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
