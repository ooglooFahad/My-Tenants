package ooglootenants.andorid.com.tenancyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splashactivity extends AppCompatActivity {

    int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    public static final String Name = "nameKey";
    public static final String Id = "idkey";

    SharedPreferences sharedpreferences;

    private static final long SPLASH_DISPLAY_LENGTH = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);

        ImageView imgv = (ImageView) findViewById(R.id.Iv);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
        imgv.startAnimation(animation1);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String email = sharedpreferences.getString(Email, "");
        final String phone = sharedpreferences.getString(Phone, "");
        final String name = sharedpreferences.getString(Name, "");
        final String id = sharedpreferences.getString(Id, "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ContextCompat.checkSelfPermission(splashactivity.this,
                        android.Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(splashactivity.this,
                            android.Manifest.permission.SEND_SMS)) {

                    } else {
                        ActivityCompat.requestPermissions(splashactivity.this,
                                new String[]{android.Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }
                }
                if ((email.isEmpty() && phone.isEmpty() && name.isEmpty() && id.isEmpty()) || (name.isEmpty() && id.isEmpty())) {
                    Intent intent = new Intent(splashactivity.this, WelcomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(splashactivity.this, Tenants_List.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
