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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

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
import java.util.Calendar;

public class Add_Newtenant extends AppCompatActivity {

    Context context = this;

    String name, phone, cnic, email, amount, security, paymentdate, notification, notify, amountreceived, paymentreceivingdate;
    EditText edt_name, edt_phone, edt_cnic, edt_email, edt_amount, edt_security, edt_amountreceived;
    TextView tv_paymentdate, tv_paymentreceivingdate, add_tenant, no_connection;
    CheckBox notifications;
    ImageView dateicon, receivingdateicon;

    int year, month, day;
    static final int DIALOG_ID = 0;
    static final int DIALOG_IDD = 1;

    HttpURLConnection httpURLConnection;
    URL url;
    String idd;
    String namm;

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add__newtenant);

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });


        idd = getIntent().getExtras().getString("id");
        namm = getIntent().getExtras().getString("name");
        edt_amountreceived = (EditText) findViewById(R.id.Et_Received_Amount);
        no_connection = (TextView) findViewById(R.id.No_Internet);
        edt_name = (EditText) findViewById(R.id.Name_Newtenant);
        edt_phone = (EditText) findViewById(R.id.Phone_Newtenant);
        edt_cnic = (EditText) findViewById(R.id.CNIC_Newtenant);
        edt_email = (EditText) findViewById(R.id.Email_Newtenant);
        edt_amount = (EditText) findViewById(R.id.Et_Rentamount);
        edt_security = (EditText) findViewById(R.id.Et_Security);
        tv_paymentdate = (TextView) findViewById(R.id.Et_Paymentdate);
        notifications = (CheckBox) findViewById(R.id.Cb_Notification);
        tv_paymentreceivingdate = (TextView) findViewById(R.id.Et_Payment_Receiving_Date);


        final java.util.Calendar cal = java.util.Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        dateicon = (ImageView) findViewById(R.id.Iv_Paymentdate);
        dateicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }

        });

        receivingdateicon = (ImageView) findViewById(R.id.Iv_Payment_Receiving_Date);
        receivingdateicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DIALOG_IDD);
            }
        });

        add_tenant = (TextView) findViewById(R.id.Add_Newtenant);
        add_tenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountreceived = edt_amountreceived.getText().toString();
                name = edt_name.getText().toString();
                phone = edt_phone.getText().toString();
                cnic = edt_cnic.getText().toString();
                email = edt_email.getText().toString();
                amount = edt_amount.getText().toString();
                security = edt_security.getText().toString();
                paymentdate = tv_paymentdate.getText().toString();
                paymentreceivingdate = tv_paymentreceivingdate.getText().toString();
                notification = getnotification();
// Add tenant
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (name.length() == 0 || phone.length() == 0 || cnic.length() == 0 || email.length() == 0 || amount.length() == 0 || security.length() == 0 || paymentdate.length() == 0 || amountreceived.length() == 0 || paymentreceivingdate.length() == 0) {

                        Toast.makeText(getApplicationContext(), "Complete all fields", Toast.LENGTH_SHORT).show();

                    } else {

                        if (validatePhone(phone)) {
                            if (validatecnic(cnic)) {
                                if (validateEmail(email)) {
                                    if (validatesalary(amount)) {
                                        if (validatesalary(security)) {
                                            if (validatesalary(amountreceived)) {

                                                int rentamount = Integer.valueOf(amount);
                                                int rentamountreceived = Integer.valueOf(amountreceived);
                                                int rentsecurity = Integer.valueOf(security);

                                                if (rentsecurity > rentamount) {
                                                    if (rentamountreceived <= rentamount) {

                                                        AddnewTenant addnewTenant = new AddnewTenant();
                                                        addnewTenant.execute(name, phone, cnic, email, amount, security, paymentdate, amountreceived, notification, paymentreceivingdate);
                                                        Intent intent = new Intent(Add_Newtenant.this, Tenants_List.class);
                                                        intent.putExtra("id", idd);
                                                        intent.putExtra("name", namm);
                                                        startActivity(intent);

                                                    } else {
                                                        edt_amountreceived.setError("Amount Received should must be less than Rent amount");
                                                    }
                                                } else {
                                                    edt_security.setError("Security should be greater than Rent amount");
                                                }
                                            } else {
                                                edt_amountreceived.setError("Enter valid Amount Received");
                                            }
                                        } else {
                                            edt_security.setError("Enter Valid Security");
                                        }

                                    } else {
                                        edt_amount.setError("Enter Valid Amount");
                                    }

                                } else {
                                    edt_email.setError("Enter Valid Email");
                                }

                            } else {
                                edt_cnic.setError("Enter Valid Cnic");
                            }

                        } else {
                            edt_phone.setError("Enter Valid Phone number");
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    no_connection.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    class AddnewTenant extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = new ProgressDialog(Add_Newtenant.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String name, phone, cnic, email, amount, security, paymentdate, paymentreceived, notification, paymentreceivingdate;
            name = params[0];
            phone = params[1];
            cnic = params[2];
            email = params[3];
            amount = params[4];
            security = params[5];
            paymentdate = params[6];
            paymentreceived = params[7];
            notification = params[8];
            paymentreceivingdate = params[9];

            try {
                try {
                    url = new URL("http://tenancyapp.thewebsupportdesk.com/Add_newTenant.php");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(idd, "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                        URLEncoder.encode("cnic", "UTF-8") + "=" + URLEncoder.encode(cnic, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8") + "&" +
                        URLEncoder.encode("security", "UTF-8") + "=" + URLEncoder.encode(security, "UTF-8") + "&" +
                        URLEncoder.encode("paymentdate", "UTF-8") + "=" + URLEncoder.encode(paymentdate, "UTF-8") + "&" +
                        URLEncoder.encode("paymentreceived", "UTF-8") + "=" + URLEncoder.encode(paymentreceived, "UTF-8") + "&" +
                        URLEncoder.encode("notification", "UTF-8") + "=" + URLEncoder.encode(notification, "UTF-8") + "&" +
                        URLEncoder.encode("paymentreceivingdate", "UTF-8") + "=" + URLEncoder.encode(paymentreceivingdate, "UTF-8");
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
            // Read Server Response
            try {
                if (reader != null) {
                    while ((line = reader.readLine()) != null) {
                        // Append server response in string
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

    public String getnotification() {
        if (notifications.isChecked())
            return "Yes";
        return "No";
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_ID) {
            return new DatePickerDialog(Add_Newtenant.this, kDatePickerListener, year, month, day);
        } else if (id == DIALOG_IDD) {
            return new DatePickerDialog(Add_Newtenant.this, mDatePickerListener, year, month, day);
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
                    tv_paymentdate.setText(date);
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
                    tv_paymentreceivingdate.setText(date);
                }
            };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Tenants_List.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id", idd);
        intent.putExtra("name", namm);
        startActivity(intent);
    }


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

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}
