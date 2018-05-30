package cn.com.egova.examapp;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by yuanchao on 2018/5/27.
 */

public class MyTask extends AsyncTask<Object,Void,List<Object>> {
    public ICallBack<List<Object>> callBack;

    public MyTask(ICallBack<List<Object>> callBack) {
        this.callBack = callBack;
    }

    @Override
    protected List<Object> doInBackground(Object... objects) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Object> data = Mock.getMockData();
        return data;
    }

    @Override
    protected void onPostExecute(List<Object> list) {
        if(callBack!=null){
            callBack.onResult(ICallBack.STATUS_SUCCESS,list);
        }
    }
}
