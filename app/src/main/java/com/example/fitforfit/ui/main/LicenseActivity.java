package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.fitforfit.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Lizensierung:")
                .addItem(new Element().setTitle("Androidx [Apache License 2.0]").setGravity(Gravity.CENTER).
                        setGravity(Gravity.CENTER).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apache.org/licenses/LICENSE-2.0"))))
                .addItem(new Element().setTitle("github.PhilJay [Apache License 2.0]").setGravity(Gravity.CENTER).
                        setGravity(Gravity.CENTER).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apache.org/licenses/LICENSE-2.0"))))
                .addItem(new Element().setTitle("android.tools [Apache License 2.0]").setGravity(Gravity.CENTER).
                        setGravity(Gravity.CENTER).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apache.org/licenses/LICENSE-2.0"))))
                .addItem(new Element().setTitle("budiyev.android [MIT License]").setGravity(Gravity.CENTER).
                        setGravity(Gravity.CENTER).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://choosealicense.com/licenses/mit/"))))
                .addItem(new Element().setTitle("github.kizitonwose [MIT License]").setGravity(Gravity.CENTER).
                        setGravity(Gravity.CENTER).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://choosealicense.com/licenses/mit/"))))
                .addItem(new Element().setTitle("Flaticon Icon: 'Gym' [Flaticon License]").setGravity(Gravity.CENTER).
                        setGravity(Gravity.CENTER).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/premium-icon/gym_1010943?term=barbell&page=1&position=70&page=1&position=70&related_id=1010943&origin=search"))))
                .addItem(new Element().setTitle("Flaticon Icon: 'Trend' [Flaticon License]").setGravity(Gravity.CENTER).
                        setGravity(Gravity.CENTER).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/free-icon/trend_3121571?term=statistics&page=1&position=7&page=1&position=7&related_id=3121571&origin=search"))))
                .addItem(new Element().setTitle("Flaticon Icon: 'Add' [Flaticon License]").setGravity(Gravity.CENTER).
                        setGravity(Gravity.CENTER).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flaticon.com/free-icon/add_1237946?term=add&page=1&position=3&page=1&position=3&related_id=1237946&origin=search"))))
                .create();
        setContentView(aboutPage);
    }
}