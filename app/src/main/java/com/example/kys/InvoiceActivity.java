package com.example.kys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class InvoiceActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private TextView textViewInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applyTheme();

        setContentView(R.layout.activity_invoice);

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "sumarija-db").build();

        textViewInvoice = findViewById(R.id.textView_Invoice);

        Button dismissButton = findViewById(R.id.dismiss_button_2);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadNotes();
    }
    private void loadNotes() {
        new LoadNotesTask().execute();
    }

    private class LoadNotesTask extends AsyncTask<Void, Void, List<Note>> {
        @Override
        protected List<Note> doInBackground(Void... voids) {
            return appDatabase.noteDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Note note : notes) {
                stringBuilder.insert(0, "\n\n");
                stringBuilder.insert(0, note.text);
            }
            textViewInvoice.setText(stringBuilder.toString());
        }
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