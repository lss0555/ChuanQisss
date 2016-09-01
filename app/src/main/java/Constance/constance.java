package Constance;
/**
 * Created by lss on 2016/7/25.
 */
public class constance {
    public static final class URL {
        /*   URL基类*/
        public static final String BASE_URL = "http://120.26.225.109/yz/Interface/";
        /*   一元元购*/
        public static final String ONE_SHOP = "http://www.yeytao.com/";
        /*   金额*/
        public static final String MONEY = BASE_URL+"income.php";
        /*   公告*/
        public static final String GONGGAO = BASE_URL+"notice.php#userconsent#";
        /*   聚钱庄余额*/
        public static final String JQZ_MONEY = BASE_URL+"account.php";
        /*   首页提现记录*/
        public static final String USER_TX = BASE_URL+"cashrecord.php";
        /*   首页聚钱庄存入记录*/
        public static final String JQZ_CR = BASE_URL+"deposit.php";
        /*   传应用信息*/
        public static final String APP_INFO = BASE_URL+"deviceinstallrecord.php";
        /*   快速任务*/
        public static final String FAST_TASK = BASE_URL+"ApplyPlan.php";
        /*   钱转入聚钱庄*/

        public static final String INTO_JQZACCOUNT = BASE_URL+"jqz_account.php";
        /*  徒弟列表*/
        public static final String TUDI_LIST = BASE_URL+"masterapprentice.php";
        /*  用户的udid*/
        public static final String USER_UDID = BASE_URL+"User.php";
        /*  用户资料*/
        public static final String USER_INFO = BASE_URL+"personal.php";
        /*  广告轮播BANNER*/
        public static final String BANNER = BASE_URL+"Advertisement.php";
        /*  绑定微信支付宝*/
        public static final String WX_ALIPAY_ACCOUNT = BASE_URL+"useraccount.php";
        /*  判断是否有绑定微信以及支付宝*/
        public static final String IS_BIND_WX_ALIPAY_ACCOUNT = BASE_URL+"isaccount.php";
        /*  获取验证码*/
        public static final String GET_YZM = BASE_URL+"duanxin/sms.php";
        /*  判断用户是否绑定过手机*/
        public static final String IS_BIND_PHONE = BASE_URL+"isBingtel.php";
        /*  提交绑定手机*/
        public static final String BIND_PHONE = BASE_URL+"Bingtel.php";
        /* 抢红包记录*/
        public static final String TAKE_TED_RECORD = BASE_URL+"hb_record.php";
        /* 每个时间段红包的前三名*/
        public static final String TAKE_TED_LIST = BASE_URL+"hb_timerecord.php";
        /* 付款余额到红包池*/
        public static final String PAYYUE2REDPOOL = BASE_URL+"hb_zh_chongzhi.php";
        /* 易钻石红包余额*/
        public static final String YIZUAN_RED = BASE_URL+"hb_account.php";
        /* 付款易钻红包到红包池*/
        public static final String PAY_YIZUANRED2REDPOOL = BASE_URL+"hb_yue_chongzhi.php";
        /* 抢红包*/
        public static final String TAKE_RED = BASE_URL+"hb_range.php";
        /* 更新用户地区*/
        public static final String UPDATE_USER_CITY = BASE_URL+"upregion.php";
        /* 用户点击签到*/
        public static final String SIGN = BASE_URL+"sign.php";
        /* 用户一个月内签到得天数*/
        public static final String MONTH_SIGN = BASE_URL+"month_sign.php";
        /* 获取连续签到的天数*/
        public static final String DAY_SIGN = BASE_URL+"day_sign.php";
    }
    public static final class INTENT {
        public static final int INTO_JQZ_SUCCESS = 2132;
    }
}
