package Constance;
/**
 * Created by lss on 2016/7/25.
 */
public class constance {
    public static final class URL {
        /*   URL基类*/
            public static final String BASE_URL = "http://i.qingyiyou.cn/yz/Interface/";
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
        public static final String FAST_TASK = BASE_URL+"android_applyPlan.php";
        /*   钱转入聚钱庄*/
        public static final String INTO_JQZACCOUNT = BASE_URL+"jqz_account.php";
        /*  徒弟列表*/
        public static final String TUDI_LIST = BASE_URL+"masterapprentice.php";
        /*  用户的udid*/
        public static final String USER_UDID = BASE_URL+"User.php";
        /*  用户资料*/
        public static final String USER_INFO = BASE_URL+"personal.php";
        /*  广告轮播BANNER*/
        public static final String BANNER = BASE_URL+"advertisement.php";
        /*  绑定微信和支付宝*/
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
        /* 分享获取奖励*/
        public static final String SHARE_GET = BASE_URL+"sharerecord.php";
        /* 版本更新*/
        public static final String VERSION_UPDATE = BASE_URL+"version.php";
        /* 了解平台*/
        public static final String KONWN_PLATFORM = BASE_URL+"ljpt.php";
        /* 明细收益*/
        public static final String ALL_PROFIT = BASE_URL+"incomerecord.php";
        /* 红包1000元升级*/
        public static final String SJ_RED = BASE_URL+"sj.php";
        /* 余额微信提现*/
        public static final String WX_WITHDRAW ="http://i.qingyiyou.cn/yz/wxpay/zhwxtx.php";
        /* 红包微信提现*/
        public static final String RED_WITHDRAW ="http://i.qingyiyou.cn/yz/wxpay/hbwxtx.php";
        /* 判断用户是否是用户*/
        public static final String IS_USER =BASE_URL+"iszhuce.php";
        /*
        *
        * */
        public static final String USERID_ISEXIST =BASE_URL+"yqm.php";
        /* 判断邀请码是否正确*/
        public static final String YQM_IS_ZQ =BASE_URL+"jl_masterapprentice.php";
        /* 微信支付*/
        public static final String WX_PAY ="http://i.qingyiyou.cn/yz/Interface/api_wxpay.php";
        /* 是否进行微信支付*/
        public static final String IS_WX_PAY =BASE_URL+"ischongzhi.php";
        /* 会员是否升级*/
        public static final String IS_USER_UPDATE =BASE_URL+"userdj.php";
        /* sBandleID应用标识码*/
        public static final String YINGYONG_BIAOSHI =BASE_URL+"wcapplytask.php";
        /* sBandleID应用标识码*/
        public static final String IS_DONE =BASE_URL+"isuserapp.php";
        /* sBandleID应用标识码*/
        public static final String SHAI_DAN =BASE_URL+"shaidan.php";
        /* 用户总余额*/
        public static final String USER_ALL_YUE =BASE_URL+"allsr.php";
        /* 判断任务状态*/
        public static final String IS_APPLYTASK =BASE_URL+"isapplytask.php";
        /* 放弃任务*/
        public static final String GIVE_UP_TASK =BASE_URL+"fqapply.php";
        /* 消息任务列表*/
        public static final String MESSAGE =BASE_URL+"fqapply.php";
        /* 引导图*/
        public static final String GUIDE =BASE_URL+"yd_advertisement.php";
        /* 师徒关系*/
        public static final String REGIST =BASE_URL+"bingsf.php";
        /* 是否了解平台*/
        public static final String IS_LJPT =BASE_URL+"isljpt.php";
    }
    public static final class INTENT {
        public static final int INTO_JQZ_SUCCESS = 2132;
        public static final String UPDATE_ADD_USER_MONEY = "update";
    }
}
