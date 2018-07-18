package cn.com.egova.examapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cn.com.egova.rankview.view.RankViewV2;


public class MainActivity extends FragmentActivity implements View.OnClickListener{
//    private RankView rankView;
    private RankViewV2 rankView;
    private Button btnFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rankView = findViewById(R.id.rank_view);
        btnFrag = findViewById(R.id.btnFrag);
        btnFrag.setOnClickListener(this);

        showRank();
    }

    private void showRank() {
        List<Object> data = Mock.getMockData2();
        rankView.setData(data);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,FragActivity.class);
        startActivity(intent);
    }
}
