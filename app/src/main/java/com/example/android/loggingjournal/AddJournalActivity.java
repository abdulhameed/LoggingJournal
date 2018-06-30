package com.example.android.loggingjournal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.android.loggingjournal.JournalDatabase.Journal;
import com.example.android.loggingjournal.JournalDatabase.JournalDatabase;

import java.lang.ref.WeakReference;

public class AddJournalActivity extends AppCompatActivity {


    private TextInputEditText et_title,et_content;
    private JournalDatabase journalDatabase;
    private Journal journal;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        journalDatabase = JournalDatabase.getInstance(AddJournalActivity.this);
        Button button = findViewById(R.id.but_save);

        if ( (journal = (Journal) getIntent().getSerializableExtra("journal"))!=null ){
            getSupportActionBar().setTitle("Update Journal");
            update = true;
            button.setText("Update");
            et_title.setText(journal.getTitle());
            et_content.setText(journal.getContent());
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (update){
                    journal.setContent(et_content.getText().toString());
                    journal.setTitle(et_title.getText().toString());
                    journalDatabase.getJournalDAO().updateJournals(journal);
                    setResult(journal,2);
                }else {

                    // fetch data and create note object
                    journal = new Journal(et_content.getText().toString(), et_title.getText().toString());

                    // create worker thread to insertJournals data into database
                    new InsertTask(AddJournalActivity.this, journal).execute();
                }
            }
        });

    }

    private void setResult(Journal journal, int flag){
        setResult(flag,new Intent().putExtra("journal", (Parcelable) journal));
        finish();
    }

    private static class InsertTask extends AsyncTask<Void,Void,Boolean> {

        private WeakReference<AddJournalActivity> activityReference;
        private Journal journal;

        // only retain a weak reference to the activity
        InsertTask(AddJournalActivity context, Journal note) {
            activityReference = new WeakReference<>(context);
            this.journal = journal;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {

            // retrieve auto incremented note id
            long j = activityReference.get().journalDatabase.getJournalDAO().insertJournals(journal);
            journal.setJournal_id((int) j);
            Log.e("ID ", "doInBackground: "+j );
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool){
                activityReference.get().setResult(journal,1);
                activityReference.get().finish();
            }
        }

    }

}
