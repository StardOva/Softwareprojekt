package com.example.fitforfit.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.fitforfit.R;
import com.example.fitforfit.database.AppDatabase;
import com.example.fitforfit.entity.Product;
import com.example.fitforfit.singleton.Database;
import com.google.zxing.Result;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddNewIngredientScanProductActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    //TextView codeText;
    //String nameAdel;
    String eanCode;
    Product product;
    String error;
    int mealId;

    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ingredient_scan_product);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.CAMERA};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else {

            //codeText = findViewById(R.id.codeTextView);

            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    Log.d("CHECKPOINT", "0");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("CHECKPOINT", "1");
                            Toast.makeText(AddNewIngredientScanProductActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                            eanCode = result.getText().toString();
                            new fetchData().start();
                        }
                    });
                }
            });
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CHECKPOINT", "2");
                    mCodeScanner.startPreview();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    class fetchData extends Thread{

        String data = "";





        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    progressDialog = new ProgressDialog(AddNewIngredientScanProductActivity.this);
                    progressDialog.setMessage("Fetching Data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                }
            });

            try {
                URL url = new URL("https://world.openfoodfacts.org/api/v0/product/"+eanCode);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                String line;

                while((line = bufferedReader.readLine()) != null){
                    data = data+ line;
                }

                if(!data.isEmpty()){
                    product = new Product();

                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject users = jsonObject.getJSONObject("product");
                    if(users.has("product_name_de")) {
                    product.product_name = users.getString("product_name_de");}
                    JSONObject nutris = users.getJSONObject("nutriments");
                    //String name = nutris.getString("carbohydrates_100g");
                    if(nutris.has("energy-kcal_100g")) {
                        product.ckal = Integer.parseInt(nutris.getString("energy-kcal_100g"));}
                    if(nutris.has("fat_100g")){
                        product.fat = Float.parseFloat(nutris.getString("fat_100g"));}
                    if(nutris.has("saturated-fat_100g")){
                        product.saturated_fat = Float.parseFloat(nutris.getString("saturated-fat_100g"));}
                    if(nutris.has("carbohydrates_100g")){
                        product.carb = Float.parseFloat(nutris.getString("carbohydrates_100g"));}
                    if(nutris.has("sugars_100g")){
                        product.sugar = Float.parseFloat(nutris.getString("sugars_100g"));}
                    if(nutris.has("fiber_100g")){
                        product.fiber = Float.parseFloat(nutris.getString("fiber_100g"));}
                    if(nutris.has("proteins_100g")){
                        product.protein = Float.parseFloat(nutris.getString("proteins_100g"));}
                    if(nutris.has("salt_100g")){
                        product.salt = Float.parseFloat(nutris.getString("salt_100g"));}


                    //codeText.setText(name);

                    //Intent mir product an ShowProductActivity und finish();
                    startShowActivity();


                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                //codeText.setText("SEITE NICHT ERREICHBAR");
                error ="SEITE NICHT ERREICHBAR";
                startErrorActivity(error);
                //Intent mir error String an ErrorScanActivity
            } catch (IOException e) {
                e.printStackTrace();
                //codeText.setText("KEINE NETZWERKVERBINDUNG");
                error = "KEINE NETZWERKVERBINDUNG";
                startErrorActivity(error);
            } catch (JSONException e) {
                e.printStackTrace();
                //codeText.setText("JSON FEHLER");
                error = "KEIN EINTRAG VORHANDEN (JSON FEHLER)";
                startErrorActivity(error);
            }

            //progrssBar disappear
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                }
            });
        }
    }

    private void startErrorActivity(String errorMsg) {
        Intent intent = new Intent(this, ScanErrorActivity.class);
        intent.putExtra("error", errorMsg);

        this.startActivity(intent);
        finish();

    }

    private void startShowActivity() {
        AppDatabase db = Database.getInstance(this);
        int id;
        try {
            db.productDao().insert(product);
            id = db.productDao().getLastProductId();

        } catch (SQLiteConstraintException e) {
            id = db.productDao().findByManyName(product.product_name).id;
        }
        if(getIntent().hasExtra("mealId")) {
            String mealIdS = getIntent().getStringExtra("mealId");
            this.mealId = Integer.valueOf(mealIdS);
            Log.d("GET_MEAL_ID", String.valueOf(this.mealId));
        }

        Intent intent = new Intent(this, ShowProductActivity.class);
        intent.putExtra("prodId", String.valueOf(id));
        intent.putExtra("mealId", String.valueOf(mealId)); //TODO

        this.startActivity(intent);
        finish();



    }

    //TODO nach permission camera fragen
    public static boolean hasPermissions(Context context, String... permissions){
        if(context != null && permissions != null){
            for (String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }
}