package cn.com.egova.examapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.com.egova.rankview.view.RankView;


public class MainFragment extends Fragment {
    private View root;
    private RankView rankView;


    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = View.inflate(getContext(),R.layout.fragment,null);
        rankView = root.findViewById(R.id.rank_view);

        initData();

        return root;
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

}
