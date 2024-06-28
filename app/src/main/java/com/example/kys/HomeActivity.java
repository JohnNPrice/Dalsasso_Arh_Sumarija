package com.example.kys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applyTheme();

        setContentView(R.layout.activity_home);


        Button buttonInvoice = findViewById(R.id.button_Invoice);
        Button buttonPrice = findViewById(R.id.button_Price);
        Button buttonShowInvoice = findViewById(R.id.button_Show_Invoice);
        ImageButton homeSettingsButton = findViewById(R.id.homeSettingsButton);


        buttonInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        buttonPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPricePopup();
            }
        });

        buttonShowInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInvoiceActivity();
            }
        });

        homeSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });
    }

    private void applyTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useBlueTheme = prefs.getBoolean("useBlueTheme", false);
        if (useBlueTheme) {
            setTheme(R.style.Theme_KYS_Blue);
        } else {
            setTheme(R.style.Theme_KYS);
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void openInvoiceActivity() {
        Intent intent = new Intent(HomeActivity.this, InvoiceActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivityForResult(intent, 1);
    }

    private void showPricePopup() {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button priceButton = popupView.findViewById(R.id.dismiss_button);
        priceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            recreate();
        }
    }
}
