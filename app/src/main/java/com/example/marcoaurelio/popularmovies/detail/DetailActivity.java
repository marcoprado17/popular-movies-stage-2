/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.marcoaurelio.popularmovies.R;

import hugo.weaving.DebugLog;

/**
 * Contains the DetailFragment. Returns to MainActivity with an empty result, MainActivity uses this
 * empty result to put or delete the selected movie to favorites db when the detail view isn't
 * active any more.
 */
public class DetailActivity extends AppCompatActivity {

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @DebugLog
    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
