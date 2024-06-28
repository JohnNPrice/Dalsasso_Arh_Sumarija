package com.example.kys;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        applyTheme();

        setContentView(R.layout.activity_settings);

        Switch colorSwitch = findViewById(R.id.switch_color);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        colorSwitch.setChecked(prefs.getBoolean("useBlueTheme", false));

        colorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("useBlueTheme", isChecked);
                editor.apply();

                setResult(RESULT_OK);
                recreate();
            }
        });

        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
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
}
