package cn.com.egova.examapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class FragActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);

        getSupportFragmentManager().beginTransaction().add(R.id.fr_container, new MainFragment()).commit();
    }
}
