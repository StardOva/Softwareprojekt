package com.example.fitforfit.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.fitforfit.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Fit for Fit von Christoph Manitz, Moritz Reichelt und Konrad Werner")
                .addItem(new Element().setTitle("Version 1.0").setGravity(Gravity.CENTER))
                .addItem(new Element().setTitle("Apache License 2.0").
                        setGravity(Gravity.CENTER).setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apache.org/licenses/LICENSE-2.0"))))
                .addGroup("CONNECT WITH US!")
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }

    private Element createCopyright() {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by Moritz Reichelt, Christoph Manitz, Konrad Werner", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        // copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(v -> Toast.makeText(AboutUsActivity.this, copyrightString, Toast.LENGTH_SHORT).show());
        return copyright;
    }
}