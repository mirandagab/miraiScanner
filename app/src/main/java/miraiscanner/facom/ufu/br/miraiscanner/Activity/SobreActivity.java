package miraiscanner.facom.ufu.br.miraiscanner.Activity;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import miraiscanner.facom.ufu.br.miraiscanner.R;

/**
 * Created by mirandagab and MarceloPrado on 11/03/2018.
 */

public class SobreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        ActionBar ts = getSupportActionBar();
        if(ts != null)
            ts.setDisplayHomeAsUpEnabled(true);
    }
}
