package cn.com.egova.examapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cn.com.egova.rankview.view.RankView;


public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private RankView rankView;
    private Button btnFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rankView = findViewById(R.id.rank_view);
        btnFrag = findViewById(R.id.btnFrag);
        btnFrag.setOnClickListener(this);

        initData();
    }

    private void initData() {
        new MyTask(new ICallBack<List<Object>>() {
            @Override
            public void onResult(int obj, List<Object> data) {
                showRank(data);
            }
        }).execute();
    }

    private void showRank(List<Object> data) {
        rankView.setData(data);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,FragActivity.class);
        startActivity(intent);
    }
}
