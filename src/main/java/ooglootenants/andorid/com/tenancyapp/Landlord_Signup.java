package ooglootenants.andorid.com.tenancyapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class Landlord_Signup extends AppCompatActivity {

    String myJSON;
    Context context = this;

    TextView tv, tv1;
    EditText edt1, edt2, edt3, edt4, edt5;
    String name, phonenumber, cnic, email, password;

    // For shared preferences
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String Name = "nameKey";
    public static final String Id = "idkey";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landlord__signup);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();

        tv = (TextView) findViewById(R.id.tv2_signin);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landlord_Signup.this, Landlord_Login.class);
                startActivity(intent);
            }
        });
        tv1 = (TextView) findViewById(R.id.Signup);
        edt1 = (EditText) findViewById(R.id.Name_Signup);
        edt2 = (EditText) findViewById(R.id.Phone_Signup);
        edt3 = (EditText) findViewById(R.id.CNIC_Signup);
        edt4 = (EditText) findViewById(R.id.Email_Signup);
        edt5 = (EditText) findViewById(R.id.Password_Signup);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edt1.getText().toString();
                password = edt5.getText().toString();
                phonenumber = edt2.getText().toString();
                cnic = edt3.getText().toString();
                email = edt4.getText().toString();

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (email.length() == 0 || phonenumber.length() == 0 || name.length() == 0 || cnic.length() == 0) {
                        Toast.makeText(Landlord_Signup.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                    } else {

                        if (validatePhone(phonenumber)) {
                            if (validatecnic(cnic)) {
                                if (validateEmail(email)) {
                                    VarifyLogin varifyLogin = new VarifyLogin();
                                    varifyLogin.execute(name, email);

                                } else {
                                    edt4.setError("Enter valid Email");
                                }

                            } else {
                                edt3.setError("Enter valid Cnic");
                            }
                        } else {
                            edt2.setError("Enter valid Phone number");
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class VarifyLogin extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(Landlord_Signup.this);
        HttpURLConnection httpURLConnection;
        URL url = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String name, email;
            name = params[0];
            email = params[1];

            try {
                url = new URL("http://tenancyapp.thewebsupportdesk.com/varify_login.php");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
        protected void onPostExecute(String text) {
            pdLoading.dismiss();
            if (text.equals("Account Exist!!")) {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            } else {
                Signup signup = new Signup();
                signup.execute(name, phonenumber, cnic, email, password);
            }
            super.onPostExecute(text);
        }
    }

    class Signup extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(Landlord_Signup.this);
        HttpURLConnection httpURLConnection;
        URL url = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String name, phone, cnic, email, password;
            name = params[0];
            phone = params[1];
            cnic = params[2];
            email = params[3];
            password = params[4];

            try {
                url = new URL("http://tenancyapp.thewebsupportdesk.com/add_info.php");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                        URLEncoder.encode("cnic", "UTF-8") + "=" + URLEncoder.encode(cnic, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
        protected void onPostExecute(String text) {
            pdLoading.dismiss();
            if (text.equals("Account Created!!")) {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                GetId getId = new GetId();
                getId.execute(name, email);
            } else {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(text);
        }
    }

    class GetId extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(Landlord_Signup.this);
        HttpURLConnection httpURLConnection;
        URL url = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String name, email;
            name = params[0];
            email = params[1];

            try {
                url = new URL("http://tenancyapp.thewebsupportdesk.com/get_id.php");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
        protected void onPostExecute(String text) {

            if (text.equals("No ID Found")) {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            } else {
                myJSON = text;
                String id = showList();
                pdLoading.dismiss();

                Intent intent = new Intent(Landlord_Signup.this, Tenants_List.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Email, email);
                editor.putString(Phone, phonenumber);
                editor.putString(Name, name);
                editor.putString(Id, id);
                editor.apply();
                startActivity(intent);
            }

            super.onPostExecute(text);
        }
    }

    protected String showList() {
        String id = null;
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            id = (String) jsonObj.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    // For input Validation
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
}
