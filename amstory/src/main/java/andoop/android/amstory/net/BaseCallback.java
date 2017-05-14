package andoop.android.amstory.net;

/**
 * Created by wsy on 17/5/14.
 */

public interface BaseCallback<T> {
    boolean result(int status, T data);
}
