package Constance;
/**
 * Created by lss on 2016/7/25.
 */
public class constance {
    public static final class URL {
        /*   URL基类*/
        public static final String BASE_URL = "http://120.26.225.109/";
        /*   一元元购*/
        public static final String ONE_SHOP = "http://www.yeytao.com/";
        /*   金额*/
        public static final String MONEY = BASE_URL+"yz/Interface/income.php";
        /*   公告*/
        public static final String GONGGAO = BASE_URL+"yz/Interface/notice.php#userconsent#";
        /*   聚钱庄余额*/
        public static final String JQZ_MONEY = BASE_URL+"yz/Interface/account.php";
        /*   首页提现记录*/
        public static final String USER_TX = BASE_URL+"yz/Interface/cashrecord.php";
        /*   首页聚钱庄存入记录*/
        public static final String JQZ_CR = BASE_URL+"yz/Interface/deposit.php";
        /*   传应用信息*/
        public static final String APP_INFO = BASE_URL+"yz/Interface/deviceinstallrecord.php";
        /*   快速任务*/
        public static final String FAST_TASK = BASE_URL+"yz/Interface/ApplyPlan.php";
        /*   钱转入聚钱庄*/

        public static final String INTO_JQZACCOUNT = BASE_URL+"yz/Interface/jqz_account.php";
        /*  徒弟列表*/
        public static final String TUDI_LIST = BASE_URL+"yz/Interface/masterapprentice.php";
        /*  用户的udid*/
        public static final String USER_UDID = BASE_URL+"yz/Interface/User.php";
        /*  用户资料*/
        public static final String USER_INFO = BASE_URL+"yz/Interface/personal.php";
        /*  广告轮播BANNER*/
        public static final String BANNER = BASE_URL+"yz/Interface/Advertisement.php";
        /*  绑定微信支付宝*/
        public static final String WX_ALIPAY_ACCOUNT = BASE_URL+"yz/Interface/useraccount.php";
        /*  判断是否有绑定微信以及支付宝*/
        public static final String IS_BIND_WX_ALIPAY_ACCOUNT = BASE_URL+"yz/Interface/isaccount.php";
        /*  获取验证码*/
        public static final String GET_YZM = BASE_URL+"yz/Interface/duanxin/sms.php";
        /*  判断用户是否绑定过手机*/
        public static final String IS_BIND_PHONE = BASE_URL+"yz/Interface/isBingtel.php";
        /*  提交绑定手机*/
        public static final String BIND_PHONE = BASE_URL+"yz/Interface/Bingtel.php";
        /* 抢红包记录*/
        public static final String TAKE_TED_RECORD = BASE_URL+"yz/Interface/hb_record.php";
        /* 每个时间段红包的前三名*/
        public static final String TAKE_TED_LIST = BASE_URL+"yz/Interface/hb_timerecord.php";
        /* 付款余额到红包池*/
        public static final String PAYYUE2REDPOOL = BASE_URL+"yz/Interface/hb_zh_chongzhi.php";
        /* 易钻石红包余额*/
        public static final String YIZUAN_RED = BASE_URL+"yz/Interface/hb_account.php";
        /* 付款易钻红包到红包池*/
        public static final String PAY_YIZUANRED2REDPOOL = BASE_URL+"yz/Interface/hb_yue_chongzhi.php";
        /* 抢红包*/
        public static final String TAKE_RED = BASE_URL+"yz/Interface/hb_range.php";
        /* 更新用户地区*/
        public static final String UPDATE_USER_CITY = BASE_URL+"yz/Interface/upregion.php";
    }
    public static final class INTENT {
        public static final int INTO_JQZ_SUCCESS = 2132;
    }
}
