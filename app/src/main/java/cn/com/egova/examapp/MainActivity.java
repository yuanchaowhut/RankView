package cn.com.egova.examapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.com.egova.rankview.view.RankView;


public class MainActivity extends Activity {
    private RankView rankView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        rankView = findViewById(R.id.rank_view);
        List<Object> data = getMockData();
        rankView.setData(data);
    }


    private List<Object> getMockData() {
        List<Object> list = new ArrayList<>();
        RankBO rankBO1 = new RankBO("01", "宋远桥", "100", Color.GREEN);
        RankBO rankBO2 = new RankBO("02", "俞莲舟", "90", Color.GREEN);
        RankBO rankBO3 = new RankBO("03", "俞岱岩", "80", Color.GREEN);
        RankBO rankBO4 = new RankBO("04", "张松溪", "70", Color.YELLOW);
        RankBO rankBO5 = new RankBO("05", "张翠山", "60", Color.YELLOW);
        RankBO rankBO6 = new RankBO("06", "殷梨亭", "50", Color.RED);
        RankBO rankBO7 = new RankBO("07", "莫声谷", "40", Color.RED);
        list.add(rankBO1);
        list.add(rankBO2);
        list.add(rankBO3);
        list.add(rankBO4);
        list.add(rankBO5);
        list.add(rankBO6);
        list.add(rankBO7);
        return list;
    }
}
