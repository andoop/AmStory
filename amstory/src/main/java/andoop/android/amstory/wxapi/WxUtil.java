package andoop.android.amstory.wxapi;

import android.content.Context;
import android.graphics.Bitmap;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import andoop.android.amstory.ImApplication;
import andoop.android.amstory.base.AppConfig;
import andoop.android.amstory.utils.BitmapUtils;

/**
 * Created by wsy on 17/5/8.
 */

public class WxUtil {
    private static final int WX_THUMB_SIZE = 120;

    private IWXAPI iwxapi;

    private static WxUtil instance;

    private WxUtil() {
        iwxapi = WXAPIFactory.createWXAPI(ImApplication.getContext(), AppConfig.WECHAT_APP_ID, true);
        iwxapi.registerApp(AppConfig.WECHAT_APP_ID);
    }

    public static WxUtil getInstance() {
        if (instance == null)
            instance = new WxUtil();
        return instance;
    }

    public void wxLogin(Context context) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        iwxapi.sendReq(req);
    }

    public void shareMusic() {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = "";
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = "";
        msg.description = "";
        Bitmap thumb = null;
        msg.thumbData = BitmapUtils.Bitmap2Bytes(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
//                SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }

    public void shareWeb() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "";
        msg.description = "";
        Bitmap thumb;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }
}
