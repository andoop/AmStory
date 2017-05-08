package andoop.android.amstory.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import andoop.android.amstory.R;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = WXEntryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.i(TAG, "onReq() called with: baseReq = [" + baseReq + "]");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i(TAG, "onResp() called with: baseResp = [" + baseResp + "]");
    }
}
