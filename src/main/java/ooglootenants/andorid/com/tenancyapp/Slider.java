package ooglootenants.andorid.com.tenancyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;

public class Slider extends AppCompatActivity {

    SliderLayout sliderLayout;
//    Button signin, createaccount;
    FancyButton signin, createaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        signin = (FancyButton) findViewById(R.id.btn);
        createaccount = (FancyButton) findViewById(R.id.btn1);
        sliderLayout = (SliderLayout) findViewById(R.id.slider);

        HashMap<String, Integer> myImages = new HashMap();
        myImages.put("title", R.drawable.title);
        myImages.put("sweet", R.drawable.sweet);
        myImages.put("chicken", R.drawable.chicken);

        for (String name : myImages.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
//                    .description(name)
                    .image(myImages.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Slider.this,Landlord_Login.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

}
