package andoop.android.amstory.net.user.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wsy on 17/5/14.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String nickname;//用户名
    private String accessToken;//story token
    private String sex;//性别
    private String headImgUrl;//头像
    private String wxUnionId;//微信的唯一id
    private Date createTime;
    private Date updateTime;

    //预留字段
    private String mobile;//手机号
    private String verifyCode;//验证码
    private Date expireTime;//验证码的过期事件
    private String password;//密码
    private String company;//公司
    private String city;//城市
    private String email;//邮箱
    private String weChatOpenId;//Add on 2017/4/11 微信的openId
    private String weChatAccessToken;//微信的accessToken
    private String weChatRefreshToken;//微信的refreshToken

    //后续添加字段
    private Integer followerCount = 0;//粉丝数
    private Integer followeeCount = 0;//关注的人数
    private Integer workCount = 0;//作品数
}
