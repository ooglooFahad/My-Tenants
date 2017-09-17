package ooglootenants.andorid.com.tenancyapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

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
import java.util.Arrays;
import java.util.HashMap;

public class Landlord_Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    //For facebook signin
    private static final String TAG = "Landlord_Login";
    private AccessTokenTracker mtracker = null;
    private ProfileTracker mprofiletracker = null;
    private CallbackManager callbackManager;
    LoginButton login_button;


    // For google signin
    private SignInButton signin;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private ProgressDialog mProgressDialog;


    // For shared preferences
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String Name = "nameKey";
    public static final String Id = "idkey";
    SharedPreferences sharedpreferences;

    Context context = this;
    EditText edt, edt1;
    TextView tv, tv1;
    String email, password;
    String myid;
    String name;
    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String L_ID = "lid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_EMAIL = "email";

    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // For Facebook signin
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.landlord__login);


        mtracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        mprofiletracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };
        mtracker.startTracking();
        mprofiletracker.startTracking();
        login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.setReadPermissions(Arrays.asList("public_profile", "email"));
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();

                if (Profile.getCurrentProfile() == null) {
                    mprofiletracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {

                            mprofiletracker.stopTracking();
                            nextActivity(profile2);
                        }
                    };
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    nextActivity(profile);
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Login attempt cancelled.");
                Toast.makeText(Landlord_Login.this, "You dont have permission to Access", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "Login attempt failed.");
                Toast.makeText(Landlord_Login.this, "Unable to Login/Connect", Toast.LENGTH_SHORT).show();
                deleteAccessToken();
            }
        });

        // For google signin
        signin = (SignInButton) findViewById(R.id.btn_login);

        setGooglePlusButtonText(signin, getString(R.string.gmaillogin));

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signin();
            }
        });

        personList = new ArrayList<HashMap<String, String>>();
        edt = (EditText) findViewById(R.id.L_Email);
        edt1 = (EditText) findViewById(R.id.L_Password);
        tv1 = (TextView) findViewById(R.id.newsignup);
        tv = (TextView) findViewById(R.id.tv_login);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edt.getText().toString();
                password = edt1.getText().toString();

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (email.length() == 0 || password.length() == 0) {
                        Toast.makeText(Landlord_Login.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                    } else {

                        if (validateEmail(email)) {
                            Signin signin = new Signin();

                            signin.execute(email, password);
                        } else {
                            edt.setError("Enter valid Email");
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(Landlord_Login.this, Landlord_Signup.class);
                startActivity(i);
                return false;
            }
        });
    }

    class Signin extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = new ProgressDialog(Landlord_Login.this);
        HttpURLConnection httpURLConnection;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("\tLoading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String email, password;
            email = params[0];
            password = params[1];

            try {
                try {
                    url = new URL("http://tenancyapp.thewebsupportdesk.com/sign_in.php");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
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
        protected void onPostExecute(String aVoid) {
            progressDialog.dismiss();
            if (aVoid.equals("Enter Valid Email/Password")) {
                Toast.makeText(Landlord_Login.this, "Enter Valid Email/Password", Toast.LENGTH_LONG).show();
            } else {
                myJSON = aVoid;
                showList();
            }
            super.onPostExecute(aVoid);
        }
    }

    protected void showList() {

        try {
            HashMap<String, String> persons = new HashMap<String, String>();
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(L_ID);
                String name = c.getString(TAG_NAME);
                String phone = c.getString(TAG_PHONE);
                String email = c.getString(TAG_EMAIL);

                persons.put(L_ID, id);
                persons.put(TAG_NAME, name);
                persons.put(TAG_PHONE, phone);
                persons.put(TAG_EMAIL, email);
                try {
                    personList.add(persons);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            SharedPreferences.Editor editor = sharedpreferences.edit();
            myid = persons.get(L_ID);
            name = persons.get(TAG_NAME);
            String newphone = persons.get(TAG_PHONE);
            Intent intent = new Intent(Landlord_Login.this, Tenants_List.class);

            editor.putString(Email, email);
            editor.putString(Phone, newphone);
            editor.putString(Name, name);
            editor.putString(Id, myid);
            editor.apply();
            intent.putExtra("id", myid);
            intent.putExtra("name", name);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Functions for Google signin
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(Landlord_Login.this, "Unable to Login/Connect", Toast.LENGTH_SHORT).show();

    }

    private void signin() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    private void handleresult(GoogleSignInResult result) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        GoogleSignInAccount account = result.getSignInAccount();

        if (result.isSuccess()) {
//            GoogleSignInAccount account = result.getSignInAccount();

            assert account != null;
            String id = account.getId();
            String nname = account.getDisplayName();
            updateUI(true);
            Intent intent = new Intent(Landlord_Login.this, Tenants_List.class);
            intent.putExtra("id", id);
            intent.putExtra("name", nname);
            editor.putString(Name, nname);
            editor.putString(Id, id);
            editor.apply();
            startActivity(intent);

        } else {
            updateUI(false);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
// To check user already signed in or not
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleresult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleresult(googleSignInResult);
                }
            });
        }
    }

    private void updateUI(boolean isLogin) {
        if (isLogin) {
            signin.setVisibility(View.GONE);
        } else {
            signin.setVisibility(View.VISIBLE);
        }
    }

    //Functions for Facebook login
    private void deleteAccessToken() {
        mtracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    //User logged out
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    private void nextActivity(Profile profile) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (profile != null) {
            Intent intent = new Intent(Landlord_Login.this, Tenants_List.class);
            String fbid = profile.getId();
            intent.putExtra("id", fbid);
            intent.putExtra("name", profile.getName());
            editor.putString(Name, profile.getName());
            editor.putString(Id, fbid);
            editor.apply();

            startActivity(intent);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    public void onStop() {
        super.onStop();
        mtracker.stopTracking();
        mprofiletracker.stopTracking();
    }

    //For Fb and gmail ActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCOde, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        callbackManager.onActivityResult(requestCode, resultCOde, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleresult(result);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("\tLoading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean validateEmail(String email) {

        boolean b = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(15);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                tv.setText(buttonText);
                return;
            }
        }
    }
}
