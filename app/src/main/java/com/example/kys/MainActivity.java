package com.example.kys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int SETTINGS_REQUEST = 1;

    private AppDatabase appDatabase;
    private EditText companyText;
    private EditText classText;
    private EditText amountText;
    private TextView textViewAmmountClassA;
    private TextView textViewAmmountClassB;
    private TextView textViewAmmountClassC;

    private int classAAmount;
    private int classBAmount;
    private int classCAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        applyTheme();

        setContentView(R.layout.activity_main);

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "sumarija-db").build();



        loadValues();

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDatabase();
            }
        });

        companyText = findViewById(R.id.company_text);
        classText = findViewById(R.id.class_text);
        amountText = findViewById(R.id.amount_text);
        textViewAmmountClassA = findViewById(R.id.textViewAmmountClassA);
        textViewAmmountClassB = findViewById(R.id.textViewAmmountClassB);
        textViewAmmountClassC = findViewById(R.id.textViewAmmountClassC);


        updateTextViews();

        Button saveButton = findViewById(R.id.dismiss_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        Button showPopupButton = findViewById(R.id.price_button);
        showPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        Button showInvoiceButton = findViewById(R.id.invoice_button);
        showInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInvoice();
            }
        });

        ImageButton settingsButton = findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

        ImageButton goBackButton = findViewById(R.id.button_go_back);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshHomeActivity();
                finish();
            }
        });

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputs();
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

    private void openSettings() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivityForResult(intent, SETTINGS_REQUEST);
    }

    private void refreshHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST) {
            recreate();
        }
    }

    private void saveNote() {
        String company = companyText.getText().toString();
        String classTextContent = classText.getText().toString();
        String amountStr = amountText.getText().toString();


        if (company.isEmpty() || classTextContent.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Molim vas popunite sva polja", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Molim vas unesite broj", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!classTextContent.equals("A") && !classTextContent.equals("B") && !classTextContent.equals("C")) {
            Toast.makeText(this, "Klasa mora biti A, B ili C", Toast.LENGTH_SHORT).show();
            return;
        }


        boolean validTransaction = false;
        switch (classTextContent) {
            case "A":
                if (classAAmount - amount >= 0) {
                    classAAmount -= amount;
                    validTransaction = true;
                }
                break;
            case "B":
                if (classBAmount - amount >= 0) {
                    classBAmount -= amount;
                    validTransaction = true;
                }
                break;
            case "C":
                if (classCAmount - amount >= 0) {
                    classCAmount -= amount;
                    validTransaction = true;
                }
                break;
        }

        if (!validTransaction) {
            Toast.makeText(this, "Previše drva. Molim vas pokušajte ponovno", Toast.LENGTH_SHORT).show();
            return;
        }

        updateTextViews();
        saveValues();

        Note note = new Note();
        note.text = "Kompanija: " + company + "\nKlasa: " + classTextContent + "\nKoličina: " + amount;
        new InsertNoteTask().execute(note);

        companyText.setText("");
        classText.setText("");
        amountText.setText("");


        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateTextViews() {
        textViewAmmountClassA.setText("Količina drva klase A: " + classAAmount);
        textViewAmmountClassB.setText("Količina drva klase B: " + classBAmount);
        textViewAmmountClassC.setText("Količina drva klase C: " + classCAmount);
    }

    private class InsertNoteTask extends AsyncTask<Note, Void, Void> {
        @Override
        protected Void doInBackground(Note... notes) {
            appDatabase.noteDao().insert(notes[0]);
            return null;
        }
    }

    private void showPopup() {
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

    private void showInvoice() {
        Intent intent = new Intent(MainActivity.this, InvoiceActivity.class);
        startActivity(intent);
    }

    private void deleteDatabase() {
        if (appDatabase != null) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.clearAllTables();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Baza podataka je izbrisana", Toast.LENGTH_SHORT).show();

                            classAAmount = 5000;
                            classBAmount = 6000;
                            classCAmount = 7500;
                            updateTextViews();
                            saveValues();
                        }
                    });
                }
            });
        }
    }

    private void clearInputs() {
        companyText.setText("");
        classText.setText("");
        amountText.setText("");
    }

    private void saveValues() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("classAAmount", classAAmount);
        editor.putInt("classBAmount", classBAmount);
        editor.putInt("classCAmount", classCAmount);
        editor.apply();
    }

    private void loadValues() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        classAAmount = prefs.getInt("classAAmount", 5000);
        classBAmount = prefs.getInt("classBAmount", 6000);
        classCAmount = prefs.getInt("classCAmount", 7500);
    }
}
