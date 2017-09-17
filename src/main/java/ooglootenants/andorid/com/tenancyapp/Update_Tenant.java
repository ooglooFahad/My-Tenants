package ooglootenants.andorid.com.tenancyapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Calendar;

public class Update_Tenant extends AppCompatActivity {

    private AdView mAdView;
    String r_id, l_id, up_name, up_phone, up_cnic, up_email, up_amount, up_security, up_paymentdate, up_paymentreceived, up_notification, up_rentnotificationdate;
    EditText upedt_name, upedt_phone, upedt_cnic, upedt_email, upedt_amount, upedt_security, upedt_amountreceived;
    TextView uptv_paymentdate, uptv_paymentreceivingdate, upupdate_tenant, upno_connection;
    CheckBox upnotifications;
    ImageView up_dateicon, up_receivingdateicon;
    int year, month, day;
    static final int DIALOG_ID = 0;
    static final int DIALOG_IDD = 1;

    HttpURLConnection httpURLConnection;
    URL url;
    String idd;
    String namm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update__tenant);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        final java.util.Calendar cal = java.util.Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        idd = getIntent().getExtras().getString("id");
        namm = getIntent().getExtras().getString("lname");

        upedt_amountreceived = (EditText) findViewById(R.id.Et_Received_Amount_Update);
        up_receivingdateicon = (ImageView) findViewById(R.id.Iv_Payment_Receiving_Date_Update);
        uptv_paymentreceivingdate = (TextView) findViewById(R.id.Et_Payment_Receiving_Date_Update);
        upno_connection = (TextView) findViewById(R.id.upno_Internet);
        upedt_name = (EditText) findViewById(R.id.Name_updatetenant);
        upedt_phone = (EditText) findViewById(R.id.Phone_updatetenant);
        upedt_cnic = (EditText) findViewById(R.id.CNIC_updatetenant);
        upedt_email = (EditText) findViewById(R.id.Email_updatetenant);
        upedt_amount = (EditText) findViewById(R.id.Et_Rentamount_update);
        upedt_security = (EditText) findViewById(R.id.Et_Security_update);
        uptv_paymentdate = (TextView) findViewById(R.id.Et_Paymentdate_update);
        upnotifications = (CheckBox) findViewById(R.id.Cb_Notification_update);
        up_dateicon = (ImageView) findViewById(R.id.Iv_Paymentdate_update);
        up_dateicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });
        up_receivingdateicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DIALOG_IDD);
            }
        });

        r_id = getIntent().getStringExtra("rid");
        l_id = getIntent().getStringExtra("lid");
        String myup_name = getIntent().getStringExtra("name");
        upedt_name.setText(myup_name);
        String myup_phone = getIntent().getStringExtra("phone");
        upedt_phone.setText(myup_phone);
        String myup_cnic = getIntent().getStringExtra("cnic");
        upedt_cnic.setText(myup_cnic);
        String myup_email = getIntent().getStringExtra("email");
        upedt_email.setText(myup_email);
        String myup_amount = getIntent().getStringExtra("rent");
        upedt_amount.setText(myup_amount);
        String myup_security = getIntent().getStringExtra("security");
        upedt_security.setText(myup_security);
        String myup_paymentdate = getIntent().getStringExtra("paymentdate");
        uptv_paymentdate.setText(myup_paymentdate);
        String myup_paymentreceived = getIntent().getStringExtra("paymentreceived");
        upedt_amountreceived.setText(myup_paymentreceived);
        String myup_notification = getIntent().getStringExtra("notification");
        boolean check = checknotify(myup_notification);
        upnotifications.setChecked(check);
        String my_rentnotificationdate = getIntent().getStringExtra("rentnotificationdate");
        uptv_paymentreceivingdate.setText(my_rentnotificationdate);

        upupdate_tenant = (TextView) findViewById(R.id.Update_Newtenant);
        upupdate_tenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_name = upedt_name.getText().toString();
                up_phone = upedt_phone.getText().toString();
                up_cnic = upedt_cnic.getText().toString();
                up_email = upedt_email.getText().toString();
                up_amount = upedt_amount.getText().toString();
                up_security = upedt_security.getText().toString();
                up_paymentreceived = upedt_amountreceived.getText().toString();
                up_paymentdate = uptv_paymentdate.getText().toString();
                up_rentnotificationdate = uptv_paymentreceivingdate.getText().toString();
                up_notification = checkbox();

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (up_name.length() == 0 || up_phone.length() == 0 || up_cnic.length() == 0 || up_email.length() == 0 || up_amount.length() == 0 || up_security.length() == 0 || up_paymentdate.length() == 0 || up_rentnotificationdate.length() == 0 || up_paymentreceived.length() == 0) {

                        Toast.makeText(getApplicationContext(), "Complete all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        if (validatePhone(up_phone)) {
                            if (validatecnic(up_cnic)) {
                                if (validateEmail(up_email)) {
                                    if (validatesalary(up_amount)) {
                                        if (validatesalary(up_security)) {
                                            if (validatesalary(up_paymentreceived)) {

                                                UpdateTenant updateTenant = new UpdateTenant();
                                                updateTenant.execute(r_id, l_id, up_name, up_phone, up_cnic, up_email, up_amount, up_security, up_paymentdate, up_paymentreceived, up_notification, up_rentnotificationdate);
                                                Intent intent = new Intent(Update_Tenant.this, Tenants_List.class);
                                                intent.putExtra("id", idd);
                                                intent.putExtra("name", namm);
                                                startActivity(intent);

                                            } else {
                                                upedt_amountreceived.setError("Enter valid Amount Received");
                                            }

                                        } else {
                                            upedt_security.setError("Enter Valid Security");
                                        }
                                    } else {
                                        upedt_amount.setError("Enter Valid Amount");
                                    }
                                } else {
                                    upedt_email.setError("Enter Valid Email");
                                }

                            } else {
                                upedt_cnic.setError("Enter Valid Cnic");
                            }

                        } else {
                            upedt_phone.setError("Enter Valid Phone Number");
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class UpdateTenant extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = new ProgressDialog(Update_Tenant.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String rid, lid, name, phone, cnic, email, amount, security, paymentdate, paymentreceived, notification, rentnotificationdate;
            rid = params[0];
            lid = params[1];
            name = params[2];
            phone = params[3];
            cnic = params[4];
            email = params[5];
            amount = params[6];
            security = params[7];
            paymentdate = params[8];
            paymentreceived = params[9];
            notification = params[10];
            rentnotificationdate = params[11];
            try {
                try {
                    url = new URL("http://tenancyapp.thewebsupportdesk.com/Update_Tenant.php");
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
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                        URLEncoder.encode("cnic", "UTF-8") + "=" + URLEncoder.encode(cnic, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8") + "&" +
                        URLEncoder.encode("security", "UTF-8") + "=" + URLEncoder.encode(security, "UTF-8") + "&" +
                        URLEncoder.encode("paymentdate", "UTF-8") + "=" + URLEncoder.encode(paymentdate, "UTF-8") + "&" +
                        URLEncoder.encode("paymentreceived", "UTF-8") + "=" + URLEncoder.encode(paymentreceived, "UTF-8") + "&" +
                        URLEncoder.encode("notification", "UTF-8") + "=" + URLEncoder.encode(notification, "UTF-8") + "&" +
                        URLEncoder.encode("rentnotificationdate", "UTF-8") + "=" + URLEncoder.encode(rentnotificationdate, "UTF-8");
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

    public boolean checknotify(String s) {
        if (s.equals("No"))
            return false;
        return true;
    }

    public String checkbox() {

        boolean notifyme = ((CheckBox) findViewById(R.id.Cb_Notification_update)).isChecked();
        if (notifyme)
            return "Yes";
        return "No";
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_ID) {
            return new DatePickerDialog(Update_Tenant.this, kDatePickerListener, year, month, day);
        } else if (id == DIALOG_IDD) {
            return new DatePickerDialog(Update_Tenant.this, mDatePickerListener, year, month, day);
        }
        return null;
    }

    protected DatePickerDialog.OnDateSetListener kDatePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yearr, int monthh, int dayy) {
                    year = yearr;
                    month = monthh + 1;
                    String newmonth = String.valueOf(month);
                    if (month < 10) {
                        newmonth = "0" + month;
                    }
                    day = dayy;
                    String newday = String.valueOf(day);
                    if (day < 10) {
                        newday = "0" + day;
                    }
                    String date = newday + "/" + newmonth + "/" + year;
                    uptv_paymentdate.setText(date);
                }
            };

    protected DatePickerDialog.OnDateSetListener mDatePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yearr, int monthh, int dayy) {
                    year = yearr;
                    month = monthh + 1;
                    String newmonth = String.valueOf(month);
                    if (month < 10) {
                        newmonth = "0" + month;
                    }
                    day = dayy;
                    String newday = String.valueOf(day);
                    if (day < 10) {

                        newday = "0" + day;
                    }
                    String date = newday + "/" + newmonth + "/" + year;
                    uptv_paymentreceivingdate.setText(date);
                }
            };


    public boolean validatePhone(String phone) {
        String regex = "^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}$|^\\d{11}$|^\\d{4}-\\d{7}$";
        boolean b = phone.matches(regex);
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateEmail(String email) {

        boolean b = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validatecnic(String cnic) {

        String cnicregex = "^[0-9+]{5}-[0-9+]{7}-[0-9]{1}$";
        boolean b = cnic.matches(cnicregex);
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validatesalary(String salary) {
        String salaryregex = "[0-9]+";
        boolean b = salary.matches(salaryregex);
        if (b) {
            return true;
        } else {
            return false;
        }
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
