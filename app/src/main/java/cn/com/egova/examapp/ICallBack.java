package cn.com.egova.examapp;

/**
 * 回调接口
 */
public interface ICallBack<T> {
	int STATUS_ERROR=-1;
	int STATUS_SUCCESS=1;

	public void onResult(int obj, T resultObj);
}
