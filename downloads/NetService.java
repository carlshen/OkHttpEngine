package com.oradt.ecard.framework.net;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.oradt.ecard.framework.token.TokenServiceManager;

import org.apache.http.Header;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author liujj
 */
public class NetService {
    private static final String TAG = "NetService";
    //  API Server
    public static String domain = ServerNetConfigure.domain;                         //登录IP
    public static String domain_port = ServerNetConfigure.domain_port;               //登录端口
    //   Web Server
    public static String update_domain = ServerNetConfigure.update_domain;           //升级IP
    public static String domain_update_port = ServerNetConfigure.domain_update_port; //升级端口
    //  Designer Server
    public static String designer_domain = ServerNetConfigure.designer_domain;       //设计师IP
    public static String designer_port = ServerNetConfigure.designer_port;           //设计师端口
    // IM Server
    public static String sns_domain = ServerNetConfigure.sns_domain;
    public static String sns_domain_port = ServerNetConfigure.sns_domain_port;
    // Business Server
    public static String business_domain = ServerNetConfigure.business_domain;       //商汇
    public static String business_port = ServerNetConfigure.business_port;           //商汇端口

    public static String ImOra_domain = ServerNetConfigure.ImOra_domain;   //ImOra下载
    public static String ImOra_port = ServerNetConfigure.ImOra_port;

    public static String CardMail_domain = ServerNetConfigure.CardMail_domain;
    public static String CardMail_port = ServerNetConfigure.CardMail_port;
//    static {
//        xmlFileParsing(Environment.getExternalStorageDirectory().getAbsolutePath() + "/OradtAPP/OraConfig.xml");
//    }

    public static final String API_MY_ORANGE_MORE = "http://qr12.cn/E9zfZO";      //我的橙子了解更多地址
    public static final String API_MY_ORANGE_HELP_APK = ImOra_domain + ImOra_port + "/h5/imora/orangeHelp.html";      //我的橙子帮助文档地址
    public static final String API_QR_URL_HEAD = ImOra_domain + ImOra_port + "/h5/exchange/id.html";
    public static final String API_CHECK = domain + domain_port + "/detection";
    public static final String API_ACCOUNT = domain + domain_port + "/account";
    public static final String API_ACCOUNT_GET = domain + domain_port + "/account";//?fields=clientid,realname,gender,birthday,mobile,email";
    public static final String API_ACCOUNT_FRIEND = domain + domain_port + "/account/friend";
    public static final String API_ACCOUNT_AGREE_FRIEND_REQUEST = domain + domain_port + "/account/apistore/friendrequest";
    public static final String API_ACCOUNT_BUSINESSCARD_TO_HEAVY = domain + domain_port + "/contact/common/mergecard";
    public static final String API_ACCOUNT_IGNORE_BUSINESSCARD_TO_HEAVY_MESSAGE = domain + domain_port + "/contact/common/overlook";
    public static final String API_ACCOUNT_CHECK_ORANGE_SMALL_SERCRETARY_STATUS = domain + domain_port + "/admin/apistore/operation";
    /**
     * 设置重要联系人
     **/
    public static final String API_ACCOUNT_RELATION_PERMISSION = domain + domain_port + "/contact/common/relationpermission";

    public static final String API_ACCOUNT_PRIVATE_PERMISSION = domain + domain_port + "/contact/common/private";//设置禁止查看
    public static final String API_OAUTH = domain + domain_port + "/oauth";
    public static final String API_CONTACT = domain + domain_port + "/contact";
    public static final String API_CONTACT_CARD = domain + domain_port + "/contact/vcard";
    public static final String API_FIND_CARD = domain + domain_port + "/contact/common/getvcards";
    public static final String API_CONTACT_CARD_EXCHANGE = domain + domain_port + "/contact/vcard/exchange";
    public static final String API_CONTACT_CARD_EXCHANGE_DYNAMIC = domain + domain_port + "/contact/common/carddynamic";
    public static final String API_CONTACT_CARD_RES = domain + domain_port + "/contact/vcard/downloadcardres";
    public static final String API_CONTACT_MAP = domain + domain_port + "/relation/map";
    public static final String API_CONTACT_CARDGROUP = domain + domain_port + "/contact/vcard/group";
    public static final String API_CONTACT_CARDPROP = domain + domain_port + "/contact/cardproperty";
    public static final String API_VERIFICATION_SMS = domain + domain_port + "/verification/sms";
    public static final String API_RESETPASSWORD_SMS = domain + domain_port + "/resetpasswd/sms";
    public static final String API_CHECKPASS_WORD = domain + domain_port + "/account/apistore/checkpass";
    public static final String API_RESETPASSWORD = domain + domain_port + "/resetpasswd";
    public static final String API_MESSAGECHECK = domain + domain_port + "/messagecheck";
    public static final String API_OAUTH2 = domain + domain_port + "/oauth2";
    public static final String API_BIZSEARCH = domain + domain_port + "/bizsearch";
    public static final String API_BIZCARD = domain + domain_port + "/bizcard";
    public static final String API_SCANCARD = domain + domain_port + "/scancard";
    public static final String API_SCANCARD_MARKPOINT = domain + domain_port + "/scancard/markpoint";
    public static final String API_RELATION = domain + domain_port + "/relation";
    public static final String API_CALENDAR_EVENT = domain + domain_port + "/calendar/event";
    public static final String API_CALENDAR_NOTE = domain + domain_port + "/calendar/note";
    public static final String API_MESSAGE = domain + domain_port + "/message";
    public static final String API_INTRODUCATION = domain + domain_port + "/introducation";
    public static final String API_SYSUPDATE = domain + domain_port + "/sysupdate";
    public static final String API_APPUPDATE = update_domain + domain_update_port + "/home/software_update/app/version/"; //app 升级
    public static final String API_APPFORCEUPDATE = domain + domain_port + "/system/versions"; //app 强制升级
    public static final String API_APP_CHECKEDITION = domain + domain_port + "/system/checkedition";

    public static final String API_DIRECTORY = domain + domain_port + "/document/directory";
    public static final String API_DOCUMENT = domain + domain_port + "/document";
    public static final String API_DOCUMENT_DOWNLOAD = domain + domain_port + "/document/download";
    public static final String API_DOCUMENT_SYNC = domain + domain_port + "/document/sync";

    public static final String API_CONTACT_CARD_SYNC = domain + domain_port + "/contact/vcard/sync";
    public static final String API_CONTACT_CARDGROUP_SYNC = domain + domain_port + "/contact/vcard/group/sync";
    public static final String API_EVENT_SYNC = domain + domain_port + "/calendar/event/sync";

    public static final String API_CITY = domain + domain_port + "/cityinfo";
    public static final String API_WEATHER = domain + domain_port + "/weather";
    public static final String API_HISTORY_WEATHER = domain + domain_port + "/weather/apistore/history";

    public static final String API_THEME = domain + domain_port + "/admin/apistore/theme";

    //短信验证码登录
    public static final String API_SMS_VERIF_LOGIN = domain + domain_port + "/oauth/apistore/smslogin";
    //更新重要联系人最后联系时间
    public static final String API_RELATIONCONTACT = domain + domain_port + "/relation/apistore/relationcontact";
    //橙秀.
    public static final String WEB_URL = NetService.domain + domain_port;
    //橙脉咨询详情.
    public static final String API_ORANGE_SHOW = domain + domain_port + "/sns/show/lists";
    public static final String API_ORANGE_SHOW_COMMENT = domain + domain_port + "/sns/show/comment";
    public static final String API_ORANGE_SHOW_PRIVACY = domain + domain_port + "/account/apistore/privacysettings";  //免打扰
    //推荐有礼获取兑换码.
    public static final String API_GETREDEEM_CODE = domain + domain_port + "/admin/apistore/redeemcodeuselist";
    public static final String API_INVITE_FRIEND = domain + domain_port + "/account/apistore/invitefriend";
    // 钱包 兑换码兑换会员或容量
    public static final String API_CONVERT_CODE = domain + domain_port + "/account/apistore/redeemcode";
    //钱包  收益记录
    public static final String API_PROFIT_RECORD = domain + domain_port + "/account/order/profitlist";

    // card sync;
    public static final String API_CARD_SYNC = domain + domain_port + "/contact/vcard/sync";
    public static final String API_ANNUAL_RING = domain + domain_port + "/relation/annualring";

    // get mycard (contactcard)
    public static final String API_GET_CARD = domain + domain_port + "/contact/vcard";

    // get cardgroup
    public static final String API_GET_CARDGROUP = domain + domain_port + "/contact/vcard/group";

    // get event
    public static final String API_GET_EVENT = domain + domain_port + "/calendar/event";

    // get cardres
    public static final String API_GET_CARDRES = domain + domain_port + "/contact/vcard/cardres";

    // oauth
    public static final String API_GET_OAUTH = domain + domain_port + "/oauth";

    // avatar
    public static final String API_ACCOUNT_AVATAR = domain + domain_port + "/account/avatar";
    public static final String API_PHONE_SMS = domain + domain_port + "/phonesms";
    // send leaf device location
    public static final String POST_DEVICE = domain + domain_port + "/common/apistore/postdevice";
    //latentfriends
    public static final String API_LATENT_FRIENDS = domain + domain_port + "/sns/latentfriends";

    public static final String API_SYSTEM_UPDATE = update_domain + domain_update_port + "/home/software_update/index/version/";

    // 设置头像
    public static final String API_MYSELFINFO_PHOTO = domain + domain_port + "/account/avatar";

    // 设置项配�?
    public static final String API_SETTING_CONFIG = domain + domain_port + "/config";

    //  用户反馈
    public static final String API_FEED_BACK = domain + domain_port + "/feedback";

    //  常见问题FAQ
    public static final String API_FAQ = domain + domain_port + "/faq/question";

    //  搜索关键�?
    public static final String API_SEARCH_KEYWORD = domain + domain_port + "/search/saveword";
    /*人脉地图*/
    public static final String API_RELATION_MAP = domain + domain_port + "/relation/map";

    // Upload Exception To Server
    public static final String API_REPORT_EXCEPTION = domain + domain_port + "/common/errorlist";
    public static AsyncHttpClient client = new AsyncHttpClient();
//    public static OkHttpEngine client = OkHttpEngine.getInstance();
//    public NetService() {
//        if (BaseApplication.HTTP_TYPE == 1) {
//            client = OkHttpEngine.getInstance();
//        } else {
//            client = new AsyncHttpClient();
//        }
//    }

    //get resource record
    public static final String API_RESOURCE_RECORD = domain + domain_port + "/common/apistore/getrecord";
    public static final String API_RESOURCE_DELRECORD = domain + domain_port + "/common/apistore/delrecord";
    public static final String API_COMPANY_LOGO = domain + domain_port + "/common/apistore/getcompanylogo";

    //工作经历
    public static final String API_EXPERIENCE = domain + domain_port + "/hr/candidate/experience";

    public static final String API_SYNC = domain + domain_port + "/common/sync";

    public static final String API_BAIDU = "http://api.map.baidu.com/geocoder/v2/";

    //信鸽Push
    public static final String API_XG_PUSH = domain + domain_port + "/account/pushtoken";
    public static final String API_XG_PUSH_MSG = domain + domain_port + "/accountbiz/pushmessage";

    public static final String API_ADDEXCHANGEBLACK = domain + domain_port + "/contact/common/addexchangeblack";
    public static final String API_PUTCATEGORY = domain + domain_port + "/account";
    public static final String API_GETCATEGORY = domain + domain_port + "/admin/apistore/getcategory";
    public static final String API_FINDPEOPLE = domain + domain_port + "/sharecard/sharecard/_search";

    public static final String API_GETSECFRIEND = domain + domain_port + "/relation/apistore/secfriend";
    public static final String API_GET_PEER = domain + domain_port + "/sharecard/sharecard/_peer";
    public static final String API_GET_COLLEAGUE = domain + domain_port + "/sharecard/sharecard/_fellow";
    public static final String API_GET_CONSUMELIST = domain + domain_port + "/account/order/consumelist";
    public static final String API_GET_GITEDITLOG = domain + domain_port + "/contact/common/geteditlog";
    public static final String API_GET_VCARDS = domain + domain_port + "/contact/common/getvcards";

    //120通知云端客户端更新完成
    public static final String API_SEND_CARD_SYNC_MESSAGE = domain + domain_port + "/contact/common/cardsyncmessage";

    /*活动点击量、分享量统计接口*/
    public static final String API_ACTIVE_EDITOPER = domain + domain_port + "/admin/apistore/editoper";

    // 解析 xml文件，获取新版本信息
    private static void xmlFileParsing(InputStream fileInputStream) {
        Log.d(TAG, "xxx xmlFileParsing =");
        InputStream inputStream = null;
        // 获得XmlPullParser解析器
        XmlPullParser xmlParser = Xml.newPullParser();
        try {
            // 得到文件流，并设置编码方式
            /*File f = new File(patch);
            if (!f.exists()) {
                return;
            }
            inputStream = new FileInputStream(f);*/
            if (fileInputStream == null) {
                Log.d(TAG, "Use IDC Server.");
                return;
            }
            xmlParser.setInput(fileInputStream, "utf-8");

            // 获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
            int evtType = xmlParser.getEventType();
            // 一直循环，直到文档结束
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tag = xmlParser.getName();
                        // 如果是开始标签，则说明需要实例化对象了

                        if (tag.equals("domain")) {
                            domain = xmlParser.nextText();
                            Log.d(TAG, "xxx xmlFileParsing domain =" + domain);
                        }

                        if (tag.equals("domainPort")) {
                            domain_port = xmlParser.nextText();
                            Log.d(TAG, "xxx xmlFileParsing domain_port =" + domain_port);

                        }

                        if (tag.equals("updatedomain")) {
                            update_domain = xmlParser.nextText();  // 下载路径
                            Log.d(TAG, "xxx xmlFileParsing update_domain =" + update_domain);
                        }

                        if (tag.equals("domainUpdatePort")) {
                            domain_update_port = xmlParser.nextText();
                            Log.d(TAG, "xxx xmlFileParsing domain_port_version =" + domain_update_port);
                        }

                        if (tag.equals("snsdomain")) {
                            sns_domain = xmlParser.nextText();  // 下载路径
                            Log.d(TAG, "xxx xmlFileParsing sns_domain =" + sns_domain);
                        }

                        if (tag.equals("snsdomainPort")) {
                            sns_domain_port = xmlParser.nextText();
                            Log.d(TAG, "xxx xmlFileParsing sns_domain_port =" + sns_domain_port);

                        }

                        if (tag.equals("designerdomain")) {
                            designer_domain = xmlParser.nextText();  // 设计师平台
                            Log.d(TAG, "xxx xmlFileParsing designer_domain =" + designer_domain);
                        }

                        if (tag.equals("designerPort")) {
                            designer_port = xmlParser.nextText();
                            Log.d(TAG, "xxx xmlFileParsing designer_port =" + designer_port);

                        }

                        if (tag.equals("businessdomain")) {
                            business_domain = xmlParser.nextText();  // 商汇
                            Log.d(TAG, "xxx xmlFileParsing business_domain =" + business_domain);
                        }

                        if (tag.equals("businessPort")) {
                            business_port = xmlParser.nextText();
                            Log.d(TAG, "xxx xmlFileParsing business_port =" + business_port);

                        }

                        break;

                    case XmlPullParser.END_TAG:

                        break;
                    default:
                        break;
                }
                // 如果xml没有结束，则导航到下一个节点
                evtType = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            Log.d(TAG, "xxx xmlFileParsing e =" + e);
            e.printStackTrace();
        } catch (IOException e1) {
            Log.d(TAG, "xxx xmlFileParsing e1 =" + e1);
            e1.printStackTrace();
        }

    }

    public static void postActiveEditoper(Context context, String token, Header[] headers, RequestParams params,
                                          JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_ACTIVE_EDITOPER, headers, params, null, responseHandler);
    }

    public static void getBaiduLocation(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_BAIDU, headers, params, responseHandler);
    }

    public static void getResourceRecord(Context context, String token, Header[] header, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_RESOURCE_RECORD, header, params, null, responseHandler);
    }

    public static void deleteResourceRecord(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_RESOURCE_DELRECORD, headers, params, null, responseHandler);
    }

    //修改账号信息
    public static void changeAccountMessage(Context context, String token, RequestParams params,
                                            JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_ACCOUNT, params, responseHandler);
    }

    public static void sendMesToPhone(Context context, String token, Header[] headers,
                                      RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.post(context, API_PHONE_SMS, headers, params, null, responseHandler);
    }


    public static void relationMap(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_RELATION_MAP, params, responseHandler);
    }

    public static void getCity(Context context, String token, Header[] headers, RequestParams params,
                               JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_CITY, headers, params, responseHandler);
    }


    public static void check(Context context, RequestParams params,
                             JsonHttpResponseHandler responseHandler) {

        client.post(context, API_CHECK, params, responseHandler);
    }

    public static void getMessageCheck(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_MESSAGECHECK, headers, params, responseHandler);
    }

    public static void postMessageCheck(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_MESSAGECHECK, headers, params, null, responseHandler);
    }

    public static void postAccount(Context context, String token, Header[] headers, RequestParams params,
                                   JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_ACCOUNT, headers, params, null, responseHandler);
    }

    public static void postException(Context context, String token, Header[] headers, RequestParams params,
                                     JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_REPORT_EXCEPTION, headers, params, null, responseHandler);
    }

    public static void addAccount(Context context, RequestParams params,
                                  JsonHttpResponseHandler responseHandler) {

        client.post(context, API_ACCOUNT, params, responseHandler);
    }

    public static void addFriend(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler, boolean async) {
        client.addHeader("AccessToken", token);
        post(context, API_ACCOUNT_FRIEND, params, responseHandler, async);
    }

    public static void agreeAddFriend(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler, boolean async) {
        client.addHeader("AccessToken", token);
        post(context, API_ACCOUNT_AGREE_FRIEND_REQUEST, params, responseHandler, async);
    }

    public static void businessCardToHeavy(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler, boolean async) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("AccessToken", token);
        client.setTimeout(30000);
        client.post(context, API_ACCOUNT_BUSINESSCARD_TO_HEAVY, params, responseHandler);
    }

    public static void IgnoreTheRepeatBusinessCard(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler, boolean async) {
        client.addHeader("AccessToken", token);
        post(context, API_ACCOUNT_IGNORE_BUSINESSCARD_TO_HEAVY_MESSAGE, params, responseHandler, async);
    }

    public static void checkOrangeSmallSecretaryStatus(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler, boolean async) {
        client.addHeader("AccessToken", token);
        get(context, API_ACCOUNT_CHECK_ORANGE_SMALL_SERCRETARY_STATUS, params, responseHandler, async);
    }

    /**
     * 设置重要联系人
     **/
    public static void setRelationPermission(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_ACCOUNT_RELATION_PERMISSION, params, responseHandler);
    }

    public static void setRelationPermissionSync(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_ACCOUNT_RELATION_PERMISSION, params, responseHandler);
    }

    /**
     * 设置禁止查看
     **/
    public static void setPrivatePermission(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_ACCOUNT_PRIVATE_PERMISSION, params, responseHandler);
    }

    public static void exchangeCard(Context context, String token, RequestParams params,
                                    JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_CONTACT_CARD_EXCHANGE, params, responseHandler);
        Log.i(TAG, "wuzj exchangeCard token is " + token + " url is " + API_CONTACT_CARD_EXCHANGE);
    }

    public static void pushToken(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_XG_PUSH, params, responseHandler);
    }

    public static void getXGPushMessageBySync(Context context, String token, RequestParams params,
                                              JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_XG_PUSH_MSG, params, responseHandler);
    }

    public static void getXGPushMessage(Context context, String token, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_XG_PUSH_MSG, params, responseHandler);
    }

    public static void getThemeList(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_THEME, headers, params, responseHandler);
    }

    public static void addExchangeBlack(Context context, String token, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_ADDEXCHANGEBLACK, params, responseHandler);
        Log.i(TAG, "wuzj addExchangeBlack token is " + token + " url is " + API_ADDEXCHANGEBLACK);
    }

    public static void getCategory(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_GETCATEGORY, params, responseHandler);
        Log.i(TAG, "wuzj getCategory token is " + token + " url is " + API_GETCATEGORY);
    }

    public static void findPeople(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_FINDPEOPLE, params, responseHandler);
        Log.i(TAG, "wuzj findPeople token is " + token + " url is " + API_FINDPEOPLE);
    }

    public static void getVcards(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_GET_VCARDS, params, responseHandler);
        Log.i(TAG, "wuzj getVcards token is " + token + " url is " + API_FINDPEOPLE);
    }

    public static void getTwoDegreeFriends(Context context, String token, Header[] headers,
                                           RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_GETSECFRIEND, params, responseHandler);
        Log.i(TAG, "wuzj getTwoDegreeFriends token is " + token + " url is " + API_GETSECFRIEND);
    }

    public static void getPeers(Context context, String token, Header[] headers,
                                RequestParams params, JsonHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("AccessToken", token);
        client.get(context, API_GET_PEER, params, responseHandler);
        client.setConnectTimeout(60000);
        client.setTimeout(60000);
        Log.i(TAG, "wuzj getPeers token is " + token + " url is " + API_GET_PEER);
    }

    public static void getConsumeList(Context context, String token, Header[] headers,
                                      RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_GET_CONSUMELIST, params, responseHandler);
        Log.i(TAG, "wuzj getConsumeList token is " + token + " url is " + API_GET_CONSUMELIST);
    }

    public static void getProfitRecord(Context context, String token, Header[] headers,
                                       RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_PROFIT_RECORD, params, responseHandler);
        Log.i(TAG, "wuzj getConsumeList token is " + token + " url is " + API_PROFIT_RECORD);
    }

    public static void getGitEditLog(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_GET_GITEDITLOG, params, responseHandler);
        Log.i(TAG, "wuzj getConsumeList token is " + token + " url is " + API_GET_GITEDITLOG);
    }

    //名片历史记录
    public static void getCardHistory(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_GET_GITEDITLOG, params, responseHandler);
        Log.i(TAG, "wuzj getConsumeList token is " + token + " url is " + API_GET_GITEDITLOG);
    }

    public static void getColleague(Context context, String token, Header[] headers,
                                    RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_GET_COLLEAGUE, params, responseHandler);
        Log.i(TAG, "wuzj getColleague token is " + token + " url is " + API_GET_COLLEAGUE);
    }

    public static void putCategory(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.put(context, API_PUTCATEGORY, params, responseHandler);
        Log.i(TAG, "wuzj putCategory token is " + token + " url is " + API_PUTCATEGORY);
    }

    public static RequestHandle delete(Context context, String url, Header[] headers, RequestParams params, ResponseHandlerInterface responseHandler, boolean async) {
        AsyncHttpClient tmpClient;
        if (async) {
            tmpClient = client;
        } else {
            tmpClient = syncHttpClient;
        }

        String token = TokenServiceManager.getServerToken();
        tmpClient.addHeader("AccessToken", token);
        return tmpClient.delete(context, url, headers, params, responseHandler);
    }

    public static void deleteFriend(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler, boolean async) {
        delete(context, API_CONTACT_CARD, headers, params, responseHandler, async);
    }

    public static void deleteFriendRelation(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler, boolean async) {
        client.addHeader("AccessToken", token);
        Log.d(TAG, "deleteFriendRelation token:" + token);
        client.delete(context, API_ACCOUNT_FRIEND, headers, params, responseHandler);
    }

    public static void putAccount(Context context, String token, RequestParams params,
                                  JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.put(context, API_ACCOUNT, params, responseHandler);

    }

    public static void getAccount(Context context, String token, Header[] headers, RequestParams params,
                                  JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_ACCOUNT, headers, params, responseHandler);

    }

    public static void getAccountSync(Context context, String token, Header[] headers, RequestParams params,
                                      JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_ACCOUNT, headers, params, responseHandler);
    }

    public static void getOauth2(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_OAUTH2, headers, params, responseHandler);
    }

    public static void addContact(Context context, String token, Header[] headers, RequestParams params,
                                  JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_CONTACT, headers, params, null, responseHandler);
    }

    public static void delContact(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.delete(context, API_CONTACT, headers, params, responseHandler);
    }

    public static void updateContact(Context context, String token, Header[] headers, RequestParams params,
                                     JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_CONTACT, params, responseHandler);
    }

    public static void getContact(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_CONTACT, headers, params, responseHandler);
    }

    public static void addContactCard(Context context, String token, Header[] headers, RequestParams params,
                                      JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_CONTACT_CARD, headers, params, null, responseHandler);
    }

    public static void delContactCard(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.delete(context, API_CONTACT_CARD, headers, params, responseHandler);
    }

    public static void updateContactCard(Context context, String token, Header[] headers, RequestParams params,
                                         JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_CONTACT_CARD, params, responseHandler);
    }

    public static void getContactCard(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_CONTACT_CARD, headers, params, responseHandler);
    }

    public static void getContactCardRes(Context context, String token, Header[] headers, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_CONTACT_CARD_RES, headers, params, responseHandler);
    }

    public static void getFindCard(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_FIND_CARD, headers, params, responseHandler);
    }

    public static void getBizSearch(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_BIZSEARCH, headers, params, responseHandler);
    }

    public static void getBizCard(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_BIZCARD, headers, params, responseHandler);
    }

    public static void putBizCard(Context context, String token, Header[] headers, RequestParams params,
                                  JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_BIZCARD, params, responseHandler);
    }

    public static void postScanCard(Context context, String token, Header[] headers, RequestParams params,
                                    JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_SCANCARD, headers, params, null, responseHandler);
    }

    public static void updateScanCard(Context context, String token, Header[] headers, RequestParams params,
                                      JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.put(context, API_SCANCARD, params, responseHandler);
    }

    public static void getScanCard(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_SCANCARD, headers, params, responseHandler);
    }


    public static void getContactCardGroups(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_CONTACT_CARDGROUP, headers, params, responseHandler);
    }

    public static void addContactCardGroup(Context context, String token, Header[] headers, RequestParams params,
                                           JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_CONTACT_CARDGROUP, headers, params, null, responseHandler);
    }

    public static void updateContactCardGroup(Context context, String token, Header[] headers, RequestParams params,
                                              JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_CONTACT_CARDGROUP, params, responseHandler);
    }

    public static void delContactCardGroup(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.delete(context, API_CONTACT_CARDGROUP, headers, params, responseHandler);
    }

    public static void getContactCardProp(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_CONTACT_CARDPROP, headers, params, responseHandler);
    }

    public static void getDevelopContactMap(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_CONTACT_MAP, headers, params, responseHandler);
    }

    public static void getContactMap(Context context, String token, Header[] headers, RequestParams params,
                                     JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_CONTACT_MAP, headers, params, responseHandler);
    }

    public static void getVerificationCodeBySMS(Context context, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.post(context, API_VERIFICATION_SMS, headers, params, null, responseHandler);
    }

    public static void checkVerificationCodeBySMS(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_VERIFICATION_SMS, headers, params, responseHandler);
    }

    //获取验证码用于修改密码（忘记密码）?
    public static void getVerifiResetPasswordCodeBySMS(Context context, RequestParams params,
                                                       JsonHttpResponseHandler responseHandler) {
        client.post(context, API_RESETPASSWORD_SMS, params, responseHandler);
    }

    //重置登录密码（修改密码）
    public static void resetPasswordCodeBySMS(Context context, RequestParams params,
                                              JsonHttpResponseHandler responseHandler) {
        client.put(context, API_RESETPASSWORD_SMS, params, responseHandler);
    }

    //修改密码
    public static void modifyAccountPassword(Context context, RequestParams params,
                                             JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", TokenServiceManager.getServerToken());
        client.put(context, API_ACCOUNT, params, responseHandler);
    }

    //上传头像
    public static void updateMySelfInfoPhoto(Context context, String token, Header[] headers, RequestParams params,
                                             JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.put(context, API_MYSELFINFO_PHOTO, params, responseHandler);
    }

    public static void relation(Context context, String token, Header[] headers, RequestParams params,
                                JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_RELATION, headers, params, null, responseHandler);
    }

    public static void addCalendarEvent(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_CALENDAR_EVENT, headers, params, null, responseHandler);
    }

    public static void getCalendarEvent(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_CALENDAR_EVENT, headers, params, responseHandler);
    }

    public static void updateCalendarEvent(Context context, String token, Header[] headers, RequestParams params,
                                           JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_CALENDAR_EVENT, params, responseHandler);
    }

    public static void delCalendarEvent(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.delete(context, API_CALENDAR_EVENT, headers, params, responseHandler);
    }

    public static void postCalendarNote(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_CALENDAR_NOTE, headers, params, null, responseHandler);
    }

    public static void getCalendarNote(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_CALENDAR_NOTE, headers, params, responseHandler);
    }

    public static void putCalendarNote(Context context, String token, Header[] headers, RequestParams params,
                                       JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_CALENDAR_NOTE, params, responseHandler);
    }

    public static void delCalendarNote(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.delete(context, API_CALENDAR_NOTE, headers, params, responseHandler);
    }

    public static void postMessage(Context context, String token, Header[] headers, RequestParams params,
                                   JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_MESSAGE, headers, params, null, responseHandler);
    }

    public static void getMessage(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_MESSAGE, headers, params, responseHandler);
    }

    public static void postIntroducation(Context context, String token, Header[] headers, RequestParams params,
                                         JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_INTRODUCATION, headers, params, null, responseHandler);
    }

    public static void putIntroducation(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_INTRODUCATION, params, responseHandler);
    }

    public static void getSysUpdate(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_SYSUPDATE, headers, params, responseHandler);
    }

    // App 升级
    public static void getAppUpdate(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_APPUPDATE, headers, params, responseHandler);
    }

    //App强制升级
    public static void getAppForceUpdate(Context context, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.get(context, API_APPFORCEUPDATE, headers, params, responseHandler);
    }

    public static void getAppCheckEdition(Context context, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(context, API_APP_CHECKEDITION, headers, params, null, responseHandler);
    }

    // 下载 App 新版本的Apk
    public static void checkAppVersionApk(Context context, String url, Header[] headers, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {
        client.get(context, API_APPUPDATE + url, headers, params, responseHandler);
    }

    public static void syncContactCard(Context context, String token, Header[] headers, RequestParams params,
                                       JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_CONTACT_CARD_SYNC, headers, params, null, responseHandler);
    }

    public static void syncCardGroup(Context context, String token, Header[] headers, RequestParams params,
                                     JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_CONTACT_CARDGROUP_SYNC, headers, params, null, responseHandler);
    }

    public static void syncEvent(Context context, String token, Header[] headers, RequestParams params,
                                 JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_EVENT_SYNC, headers, params, null, responseHandler);
    }

    // 更新重要联系人最后联系时间
    public static void relationcontact(Context context, int type, String vcardid, Header[] headers, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("type", type);
        params.put("vcardid", vcardid);
        Log.d(TAG, "relationcontact  vcardid= " + vcardid);
        Log.d(TAG, "relationcontact  type= " + type);
        client.addHeader("AccessToken", TokenServiceManager.getServerToken());
        client.post(context, API_RELATIONCONTACT, headers, params, null, responseHandler);
    }

    // 橙脉咨询详情
    public static void orangeShowDetails(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_ORANGE_SHOW, headers, params, responseHandler);
    }

    public static void orangeShowComment(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_ORANGE_SHOW_COMMENT, headers, params, responseHandler);
    }

    public static void postOrangeShowComment(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_ORANGE_SHOW_COMMENT, headers, params, null, responseHandler);
    }

    // 橙脉咨询 免打扰
    public static void postOrangeShowPrivacy(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_ORANGE_SHOW_PRIVACY, headers, params, null, responseHandler);
    }

    // 推荐有礼获取兑换码
    public static void getredeemCode(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        Log.d("RecommendedPolite", token);
        Log.d("RecommendedPolite", API_GETREDEEM_CODE);
        syncHttpClient.get(context, API_GETREDEEM_CODE, headers, params, responseHandler);
    }

    // 推荐有礼获取邀请好友列表
    public static void getInviteFriend(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_INVITE_FRIEND, headers, params, responseHandler);
    }

    // 钱包 兑换码兑换会员或容量
    public static void getConvertCode(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_CONVERT_CODE, headers, params, null, responseHandler);
    }

    // 閹存垹娈戦崥宥囧閸氬本顒�
    public static void cardSync(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_CARD_SYNC, headers, params, null, responseHandler);
    }


    // 閼辨梻閮存禍鍝勬倳閻�?��绮嶉崥灞绢劄
    public static void concactCardgroupSync(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_CONTACT_CARDGROUP_SYNC, headers, params, null, responseHandler);
    }

    // 閺冦儳鈻奸崥灞绢�?
    public static void eventSync(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_EVENT_SYNC, headers, params, null, responseHandler);
    }


    // 閼惧嘲褰囬幋鎴犳畱閸氬秶澧�
    public static void getMycard(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_GET_CARD, headers, params, responseHandler);
    }


    // 閼惧嘲褰囬懕鏃傞兇娴滃搫鎮曢悧锟�?
    public static void getConcactcard(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_GET_CARD, headers, params, responseHandler);
    }

    //获取名片详细信息（同步）
    public static void getConcactcardSync(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_GET_CARD, headers, params, responseHandler);
    }

    //获取名片交换历史记录（同步）
    public static void getCardSwapHistorySync(Context context, String token, RequestParams params,
                                              JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_CONTACT_CARD_EXCHANGE_DYNAMIC, params, responseHandler);
        Log.i(TAG, "wuzj getCardSwapHistorySync token is " + token + " url is " + API_CONTACT_CARD_EXCHANGE_DYNAMIC);
    }

    // 閼惧嘲褰囬崥宥囧閸掑棛绮�
    public static void getCardgroup(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_GET_CARDGROUP, headers, params, responseHandler);
    }


    // 閼惧嘲褰囬弮銉�?��娣団剝浼�?
    public static void getEvent(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_GET_EVENT, headers, params, responseHandler);
    }


    // 閼惧嘲褰囬崥宥囧鐠у嫭绨�?
    public static void getCardres(Context context, String token, Header[] headers, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_GET_CARDRES, headers, params, responseHandler);
    }

    public static void downloadFile(Context context, String url, FileAsyncHttpResponseHandler responseHandler) {
        client.get(context, url, null, new RequestParams(), responseHandler);
    }

    /*
    public static void login(Context context, RequestParams params,
            JsonHttpResponseHandler responseHandler) {

        client.post(context, API_GET_OAUTH, params, responseHandler);
    }  */

    /*
    public static void getAccount(Context context,String token,Header[] headers,RequestParams params,JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_ACCOUNT, headers, params, responseHandler);
    }    */

    public static void updateAvatar(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.put(context, API_ACCOUNT_AVATAR, params, responseHandler);
    }

    public static String getAvatarUrl(String clientId) {
        return API_ACCOUNT_AVATAR + "?path=" + clientId;
    }

    public static void getAvatar(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_ACCOUNT_AVATAR, params, responseHandler);
    }

    public static void getAvatar(Context context, String token, Header[] headers, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_ACCOUNT_AVATAR, headers, params, responseHandler);
    }

    public static void createAvatar(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_ACCOUNT_AVATAR, headers, params, null, responseHandler);
    }

    public static void addDirectory(Context context, String token, Header[] headers, RequestParams params,
                                    JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_DIRECTORY, headers, params, null, responseHandler);
    }

    public static void updateDirectory(Context context, String token, Header[] headers, RequestParams params,
                                       JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_DIRECTORY, params, responseHandler);
    }

    public static void delDirectory(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.delete(context, API_DIRECTORY, headers, params, responseHandler);
    }

    public static void getDirectory(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_DIRECTORY, headers, params, responseHandler);
    }

    public static void addDocument(Context context, String token, Header[] headers, RequestParams params,
                                   JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_DOCUMENT, headers, params, null, responseHandler);
    }

    public static void updateDocument(Context context, String token, Header[] headers, RequestParams params,
                                      JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_DOCUMENT, params, responseHandler);
    }

    public static void delDocument(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.delete(context, API_DOCUMENT, headers, params, responseHandler);
    }

    public static void getDocument(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_DOCUMENT, headers, params, responseHandler);
    }

    public static void syncDocument(Context context, String token, Header[] headers, RequestParams params,
                                    JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_DOCUMENT_SYNC, headers, params, null, responseHandler);
    }

    public static void downloadDocument(Context context, String token, Header[] headers, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_DOCUMENT_DOWNLOAD, headers, params, responseHandler);
    }

    // get latent friends
    public static void getLatentFriends(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_LATENT_FRIENDS, headers, params, responseHandler);
    }

    // System update
    public static void checkSysmteUpdate(Context context, String url, Header[] headers, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {
        client.get(context, API_SYSTEM_UPDATE + url, headers, params, responseHandler);
        android.util.Log.d("NetService", "wuzj checkSysmteUpdate url is " + API_SYSTEM_UPDATE + url);
    }

    public static void downloadSysmteUpdate(Context context, String url, Header[] headers, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {
        client.get(context, url, headers, params, responseHandler);
    }


    // 更新设置配置
    public static void updateSettingConfig(Context context, String token, Header[] headers, RequestParams params,
                                           JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_SETTING_CONFIG, params, responseHandler);
    }

    // 从云端得到设置配�?
    public static void getSettingConfig(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_SETTING_CONFIG, headers, params, responseHandler);
    }

    // 上传设置配�?到云�?
    public static void addSettingConfig(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_SETTING_CONFIG, headers, params, null, responseHandler);
    }

    // 删除设置配�?
    public static void deleteSettingConfig(Context context, String token, Header[] headers, RequestParams params,
                                           JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.delete(context, API_SETTING_CONFIG, headers, params, responseHandler);
    }

    // 用户反馈
    public static void addFeedBackInfo(Context context, String token, Header[] headers, RequestParams params,
                                       JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_FEED_BACK, headers, params, null, responseHandler);
    }


    //上传FAQ信息
    public static void addFaqInfo(Context context, String token, Header[] headers, RequestParams params,
                                  JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_FAQ, headers, params, null, responseHandler);
    }


    //获取FAQ信息
    public static void getFaqInfo(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_FAQ, headers, params, responseHandler);
    }

    // FAQ信息点赞
    public static void updateFaqInfo(Context context, String token, Header[] headers, RequestParams params,
                                     JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_FAQ, params, responseHandler);
    }

    public static void getAnnualRing(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.get(context, API_ANNUAL_RING, headers, params, responseHandler);
    }

    public static class Weather {
        private static final String DOMAIN = "192.168.30.191";
        public static final String HEAD = "head";
        public static final String BODY = "body";
        public static final String STATUS = "status";
        public static final String PARAMS = "params";
        public static final String ERROR = "error";
        public static final String FIELDS = "fields";
        public static final String WEATHER = "weather";
        public static final String DATE = "date";
        public static final String CITYCODE = "citycode";
        public static final String WHICHDAY = "whichday";
        public static final String ROWS = "rows";
        public static final String NUMFOUND = "numfound";
        public static final String START = "start";
        public static final String WEATHERS = "weathers";
        public static final String FEELSLIKES = "feelslikes";
        public static final String TEMPHI = "temphi";
        public static final String TEMPLO = "templo";
        public static final String WIND = "wind";
        public static final String HUMIDITY = "humidity";
        public static final String UVINDEX = "uvindex";
        public static final String VISIBILITY = "visibility";
        public static final String DEWPOINT = "dewpoint";
        public static final String BAROMETER = "barometer";
        public static final String DAYNIGHT = "daynight";

    }

    public static void getWeather(Context context, String token,
                                  Header[] headers, RequestParams params,
                                  JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_WEATHER, headers, params, responseHandler);
    }

    public static void getWeatherSync(Context context, String token,
                                      Header[] headers, RequestParams params,
                                      JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_WEATHER, headers, params, responseHandler);
    }

    public static void getHistoryWeather(Context context, String token,
                                         Header[] headers, RequestParams params,
                                         JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_HISTORY_WEATHER, headers, params, responseHandler);
    }


    //上传搜索关键�?
    public static void addSearchKeyWord(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_SEARCH_KEYWORD, headers, params, null, responseHandler);
    }

    public static class CardExchange {
        public static final String HEAD = "head";
        public static final String BODY = "body";
        public static final String LIST = "list";
        public static final String STATUS = "status";
        public static final String PARAMS = "params";
        public static final String ERROR = "error";
        public static final String ERROR_CODE = "errorcode";
        public static final String DESCRIPTION = "description";
        public static final String NUM_FOUND = "numfound";
        public static final String ROWS = "rows";
        public static final String START = "start";
        public static final String SORTING = "sorting";
        public static final String FIELDS = "fields";

        public static class RadarCardExchange {
            public static class ACT_COMMAND {
                public static String ENTER_MAP = "cardaddmap";
                public static String QUIT_MAP = "quitmap";
                public static String UPDATE_MAP = "updatemap";
                public static String GET_MAP_CARD_LIST = "getmapcardlist";
                public static String GET_HISTORY_SHAKE_LIST = "getmapcardlisthistory";
                public static String SAVE_MAP_CARD = "savemapcard";
                public static String DOWN_MAP_CARD_RES = "downmapcardres";
                public static String MAP_FRIEND = "mapfriend";
            }

            public static class RES_STATUS {
                public static String RES_NOT_EXIST = "0";
                public static String RES_EXIST = "1";
            }

            public static class SAVE_STATUS {
                public static String UNSAVED = "0";
                public static String SAVED = "1";
            }

            public static class ERROR_CODE {
                public static String PARAM_NOT_ENOUGH = "999003";
                public static String NO_DATA = "999004";
                public static String EXPIRED = "999021";
            }

            public static final String ACT = "act";
            public static final String VCARD_ID = "vcardid";
            public static final String RADIUS = "m";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";

            public static final String LIST = "list";
            public static final String CLIENT_ID = "clientid";
            public static final String CONTACT_NAME = "contact_name";
            public static final String VCARD = "vcard";
            public static final String RES_STATUS = "res";
            public static final String SAVED = "saved";
            public static final String CARD_IMG = "picture";
            public static final String LOGO = "logo";
            public static final String COMPANY = "ORG";
            public static final String IS_FRIEND = "friend";
            public static final String HAS_REQ = "ifsave";
            public static final String ROWS = "rows";

            public static final String CARD_CHANGE_PARAM_URL = "url";
            public static final String CARD_CHANGE_PARAM_SEQ = "seq";
            public static final String CARD_CHANGE_PARAM_OBJ = "object";
            public static final String CARD_CHANGE_PARAM_METHOD = "method";
            public static final String CARD_CHANGE_PARAM_POST = "post";
            public static final String API_FRIEND_REQ = "/contact/vcard/exchange";

            private static final String API_RADAR = domain + domain_port + "/contact/vcard/exchange";

            public static void sendRequest(Context context, String token, Header[] headers, RequestParams params,
                                           JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_RADAR, headers, params, null, responseHandler);
            }

            public static void sendRequestSync(Context context, String token, Header[] headers, RequestParams params,
                                               JsonHttpResponseHandler responseHandler) {
                syncHttpClient.addHeader("AccessToken", token);
                syncHttpClient.post(context, API_RADAR, headers, params, null, responseHandler);
            }

            public static void getCardKey(Context context, String token, RequestParams params,
                                          JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_RADAR, params, responseHandler);
            }

            public static void downloadCardRes(Context context, String token, Header[] headers, RequestParams params,
                                               FileAsyncHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_RADAR, headers, params, null, responseHandler);
            }

        }

        public static class QRCardExchange {
            public static class ACT_COMMAND {
                public static String GET_KEY = "getkey";
                public static String SAVE_CARD = "qrcopycard";
                public static String DOWNLOAD_CARD_RES = "downqrcardres";
            }

            public static final String ACT = "act";
            public static final String VCARD_ID = "vcardid";

            public static final String TIMESTAMP = "timestamp";
            public static final String KEY = "key";

            public static class ERROR_CODE {
                public static String PARAM_INCORRECT = "999002";
                public static String PARAM_NOT_ENOUGH = "999003";
                public static String NO_DATA = "999004";
                public static String UNKNOWN = "999001";
            }

            private static final String API_QR_CARD_EXCHANGE = domain + domain_port + "/contact/vcard/exchange";

            public static void sendRequest(Context context, String token, Header[] headers, RequestParams params,
                                           JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_QR_CARD_EXCHANGE, headers, params, null, responseHandler);
            }

            public static void downloadCardRes(Context context, String token, Header[] headers, RequestParams params,
                                               FileAsyncHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_QR_CARD_EXCHANGE, headers, params, null, responseHandler);
            }
        }

        public static class GroupCardExchange {
            public static class ACT_COMMAND {
                public static String CREATE_GROUP = "creategroup";
                public static String JOIN_GROUP = "joingroup";
                public static String QUIT_GROUP = "quitgroup";
                public static String GROUP_LIST = "grouplist";
                public static String GROUP_CARD_LIST = "groupcardlist";
                public static String SAVE_CARD = "savecard";
                public static String DOWN_CARD_RES = "downcardres";
            }

            public static class RES_STATUS {
                public static String RES_NOT_EXIST = "0";
                public static String RES_EXIST = "1";
            }

            public static class SAVE_STATUS {
                public static String UNSAVED = "0";
                public static String SAVED = "1";
            }

            public static final String ACT = "act";
            public static final String VCARD_ID = "vcardid";
            public static final String GROUP = "group";

            public static final String GROUP_ID = "groupid";
            public static final String GROUP_NAME = "groupname";
            public static final String CREATED_TIME = "createdtime";
            public static final String CLIENT_ID = "clientid";
            public static final String CONTACT_NAME = "contact_name";
            public static final String VCARD = "vcard";

            public static class ERROR_CODE {
                public static String GROUP_NOT_EXIST = "700006";
                public static String PARAM_NOT_ENOUGH = "999003";
                public static String GROUP_ID_INCORRECT = "999004";
                public static String ALREADY_JOINED = "999005";
            }

            private static final String API_GROUP_CARD_EXCHAGNE = domain + domain_port + "/contact/vcard/exchange";

            public static void sendRequest(Context context, String token, Header[] headers, RequestParams params,
                                           JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_GROUP_CARD_EXCHAGNE, headers, params, null, responseHandler);
            }

            public static void downloadCardRes(Context context, String token, Header[] headers, RequestParams params,
                                               FileAsyncHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_GROUP_CARD_EXCHAGNE, headers, params, null, responseHandler);
            }
        }

        public static class Company {
            public static final String ID = "bizid";
            public static final String NAME = "name";
            public static final String PHONE = "phone";
            public static final String EMAIL = "email";
            public static final String WEBSITE = "website";
            public static final String ADDRESS = "address";
            public static final String SIZE = "size";
            public static final String TYPE = "type";
            public static final String SUMMARY = "info";
            public static final String LOGO = "logo";
            public static final String FOLLOWED_SUM = "followtotal";
            public static final String INDUSTRY_CODE = "categoryid2";
            public static final String REGION_CODE = "region";

            public static final String COMPANIES = "accountbizs";
            public static final String FOLLOWED_COMPANIES = "list";

            private static final String API_COMPANY = domain + domain_port + "/accountbiz";
            private static final String API_RECOMMEND_COMPANY = domain + domain_port + "/accountbiz";
            private static final String API_FOLLOW_COMPANY = domain + domain_port + "/accountbiz/follows";

            public static void getCompany(Context context, String token, Header[] headers, RequestParams params,
                                          JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_COMPANY, headers, params, responseHandler);
            }

            public static void getRecommendedCompanies(Context context, String token, Header[] headers, RequestParams params,
                                                       JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_RECOMMEND_COMPANY, headers, params, responseHandler);
            }

            public static void getFollowedCompanies(Context context, String token, Header[] headers, RequestParams params,
                                                    JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_FOLLOW_COMPANY, headers, params, responseHandler);
            }

            public static void followCompany(Context context, String token, Header[] headers, RequestParams params,
                                             JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_FOLLOW_COMPANY, headers, params, null, responseHandler);
            }

            public static void unfollowCompany(Context context, String token, Header[] headers, RequestParams params,
                                               JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.delete(context, API_FOLLOW_COMPANY, headers, params, responseHandler);
            }

            public static void isCompanyFollowed(Context context, String token, Header[] headers, RequestParams params,
                                                 JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_FOLLOW_COMPANY, headers, params, responseHandler);
            }
        }

        public static class Person {
            public static final String CANDIDATE_ID = "candidateid";
            public static final String NAME = "candname";
            public static final String GENDER = "gender";
            public static final String BIRTHDAY = "birthday";
            public static final String PHONE = "mobile";
            public static final String EMAIL = "email";
            public static final String WEBSITE = "website";
            public static final String ADDRESS = "birthplace"; // Lack of "address" field, Use birthplace as address to keep align with "myself"
            public static final String REGION = "residence"; // Useless now.
            public static final String REGION_CODE = "residence"; // It's region code.
            public static final String PORTRAIT = "candavatar";
            public static final String MARRIAGE = "marriage";
            public static final String WORK_YEAR_CODE = "experience";
            public static final String LAN_CODE = "languageid";

            public static final String RESUME = "resume";
            public static final String RESUME_LIST = "resumelist";
            public static final String CANDIDATES = "candidate";

            public static final String CANDIDATEINFO = "candidateinfo";
            public static final String SAVED_CANDIDATES = "favoritecandidates";
            public static final String MBIZCODE = "mbizcode";

            private static final String API_RESUME = domain + domain_port + "/hr/resume";
            private static final String API_CANDIDATE = domain + domain_port + "/hr/candidate";
            private static final String API_RECOMMEND_RESUME = domain + domain_port + "/hr/resume/list";
            private static final String API_SAVE_CANDIDATE = domain + domain_port + "/hr/favorite/candidate";
            private static final String API_APPLICANTS_BY_JOB = domain + domain_port + "/hr/ent/job/applyinfo";


            public static void getResume(Context context, String token, Header[] headers, RequestParams params,
                                         JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_RESUME, headers, params, responseHandler);
            }

            public static void getCandidate(Context context, String token, Header[] headers, RequestParams params,
                                            JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_CANDIDATE, headers, params, responseHandler);
            }

            public static void getApplicantsByJob(Context context, String token, Header[] headers, RequestParams params,
                                                  JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_APPLICANTS_BY_JOB, headers, params, responseHandler);
            }

            public static void getRecommandCandidate(Context context, String token, Header[] headers, RequestParams params,
                                                     JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_RECOMMEND_RESUME, headers, params, responseHandler);
            }

            public static void getFollowedPersons(Context context, String token, Header[] headers, RequestParams params,
                                                  JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_SAVE_CANDIDATE, headers, params, responseHandler);
            }

            public static void followPerson(Context context, String token, Header[] headers, RequestParams params,
                                            JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_SAVE_CANDIDATE, headers, params, null, responseHandler);
            }

            public static void unfollowPerson(Context context, String token, Header[] headers, RequestParams params,
                                              JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.delete(context, API_SAVE_CANDIDATE, headers, params, responseHandler);
            }

            public static void isPersonFollowed(Context context, String token, Header[] headers, RequestParams params,
                                                JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_SAVE_CANDIDATE, headers, params, responseHandler);
            }
        }

        public static class WorkExperience {
            public static final String ID = "experienceid";
            public static final String COMPANY = "company";
            public static final String POSITION = "position";
            public static final String REGION = "workarea";
            public static final String REGION_CODE = "region";
            public static final String STARTDATE = "startdate";
            public static final String ENDDATE = "enddate";
            public static final String RESPONSIBILITY = "responsibility";

            public static final String WORKEXPERIENCES = "experienceinfo";
        }

        public static class Education {
            public static final String ID = "educationid";
            public static final String SCHOOL = "school";
            public static final String STARTDATE = "startdate";
            public static final String ENDDATE = "enddate";
            public static final String DEGREE_CODE = "degree";
            public static final String MAJOR = "major";

            public static final String EDUCATIONS = "educationinfo";
        }

        public static class Objective {
            public static final String ID = "objectiveid";
            public static final String POSITION_CODE = "position";
            public static final String REGION = "address";
            public static final String REGION_CODE = "region";
            public static final String SALARY_CODE = "yearsalary";
            public static final String INDUSTRY_CODE = "industrycode";

            public static final String OBJECTIVE = "objectiveinfo";
        }

        public static class Special {
            public static final String ID = "specialid";
            public static final String TYPE = "type";
            public static final String DESC = "description";
            public static final String LAN = "lan";
            public static final String INTEREST = "interest";

            public static final String SPECIALS = "specialinfo";
        }

        public static class Search {
            public static final String COMPANY_ID = "bizid";
            public static final String COMPANY_NAME = "bizname";
            public static final String LOGO = "logo";
            public static final String REGION_CODE = "region";
            public static final String INDUSTRY_CODE = "categoryid2";
            public static final String CANDIDATE_KEY = "key";
            public static final String REGION = "address"; // It's "residence" defined in Person, different field for the search person cloud api.
            public static final String SALARY_CODE_FOR_SEARCH_ONLY = "yearsaraly"; // The output field name is different with input field name. It's a typo.

            public static final String COMPANIES = "resources";
            public static final String SEARCH_PERSONS = "candidates";

            private static final String API_SEARCH_COMPANY = domain + domain_port + "/hr/companysearch";
            private static final String API_SEARCH_JOB = domain + domain_port + "/careerfair/job";
            private static final String API_SEARCH_PERSON = domain + domain_port + "/hr/search/candidate";

            public static void searchCompanies(Context context, String token, Header[] headers, RequestParams params,
                                               JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_SEARCH_COMPANY, headers, params, responseHandler);
            }

            public static void searchJobs(Context context, String token, Header[] headers, RequestParams params,
                                          JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_SEARCH_JOB, headers, params, responseHandler);
            }

            public static void searchPersons(Context context, String token, Header[] headers, RequestParams params,
                                             JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_SEARCH_PERSON, headers, params, responseHandler);
            }
        }

        public static class City {
            public static final String CITY_CODE = "citycode";
            public static final String CITY_NAME = "citynativename";
            public static final String COUNTRY_NAME = "countrynativename";
            public static final String LAN_ID = "languageid";

            public static final String BATCH_PARAM_URL = "url";
            public static final String BATCH_PARAM_SEQ = "seq";
            public static final String BATCH_PARAM_OBJ = "object";
            public static final String BATCH_PARAM_METHOD = "method";
            public static final String BATCH_PARAM_POST = "post";
            public static final String BATCH_PARAM_GET = "get";

            public static final String CITYINFOS = "cityinfo";

            public static final String API_CITYINFO = "/cityinfo";
            public static final String API_BATCH = domain + domain_port + "/batch";

            public static void applyBatch(Context context, String token, Header[] headers, RequestParams params,
                                          JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_BATCH, headers, params, null, responseHandler);
            }
        }
    }

    public static class MyWallet {

        public static final String HEAD = "head";
        public static final String BODY = "body";
        public static final String LIST = "list";
        public static final String STATUS = "status";
        public static final String PARAMS = "params";
        public static final String ERROR = "error";
        public static final String ERROR_CODE = "errorcode";
        public static final String DESCRIPTION = "description";
        public static final String NUM_FOUND = "numfound";
        public static final String ROWS = "rows";
        public static final String START = "start";
        public static final String SORTING = "sorting";
        public static final String FIELDS = "fields";

        public static class SortingType {
            public static final String ASC = "asc";
            public static final String DESC = "desc";
        }

        public static class Card {
            public static final String ID = "uuid";
            public static final String NAME = "cardname";
            public static final String SN = "cardid";
            public static final String BALANCE = "balance";
            public static final String TEL = "tel";
            public static final String IS_FAVOR = "isfavor";
            public static final String QR_TYPE = "type";
            public static final String REMARK = "remark";
            public static final String SORT_CARD_IN_FAVOR = "sorting1";
            public static final String SORT_CARD_IN_ALL = "sorting2";
            public static final String SORT_CARD_IN_GROUP = "sorting3";
            public static final String FRONT_IMG = "picturea";
            public static final String BACK_IMG = "pictureb";
            public static final String IS_EXPO_CARD = "isticket"; /* 0-no, 1-yes */
            public static final String EXPO_ID = "expoid";
            public static final String EXPO_NAME = "expotitle";
            public static final String ADDRESS = "address";
            public static final String LATITUDE = "lat";
            public static final String LONGITUDE = "lng";
            public static final String EXPO_START_TIME = "starttime";
            public static final String EXPO_END_TIME = "endtime";
            public static final String EXPO_VALID_STATUS = "status"; /* 0-invalid, 1-valid */
            public static final String EXPO_CHECKIN_STATUS = "sign";  /* 0-no, 1-yes */
            public static final String EXPO_OVERDUE = "overdue";
            public static final String CARD_HOLDER = "cardholder";
            public static final String LINKMAN = "linkman";
            public static final String CUSTOMER_SERVICE_TEL = "tel2";
            public static final String LOCATE_THE_PHONE = "tel3";
            public static final String VALIDITY_TIME = "validitytime";
            public static final String MODIFED_TIME = "modifedtime";
            public static final String CREATE_TIME = "createdtime";

            public static final String CARD = "card";


            private static final String API_CARD = domain + domain_port + "/cardpackage";
            private static final String API_DOWNLOAD = domain + domain_port + "/cardpackage/download";
            private static final String API_EXPO_CHECKIN = domain + domain_port + "/expo/ticket";
            private static final String API_EXPO_CHECK_STATUS = domain + domain_port + "/expo/find";


            public static void getCard(Context context, String token, Header[] headers, RequestParams params,
                                       JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_CARD, headers, params, responseHandler);
            }

            public static void addCard(Context context, String token, Header[] headers, RequestParams params,
                                       JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_CARD, headers, params, null, responseHandler);
            }

            public static void modifyCard(Context context, String token, Header[] headers, RequestParams params,
                                          JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.put(context, API_CARD, params, responseHandler);
            }

            public static void delCard(Context context, String token, Header[] headers, RequestParams params,
                                       JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.delete(context, API_CARD, headers, params, responseHandler);
            }

            public static void downloadImg(Context context, String token, Header[] headers, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {

                client.addHeader("AccessToken", token);
                client.get(context, API_DOWNLOAD, headers, params, responseHandler);
            }

            public static void checkinExpo(Context context, String token, Header[] headers, RequestParams params,
                                           JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.put(context, API_EXPO_CHECKIN, params, responseHandler);
            }

            public static void checkExpoStatus(Context context, String token, Header[] headers, RequestParams params,
                                               JsonHttpResponseHandler responseHandler) {

                client.addHeader("AccessToken", token);
                client.get(context, API_EXPO_CHECK_STATUS, headers, params, responseHandler);
            }
        }

        public static class Group {
            public static final String ID = "groupid";
            public static final String NAME = "groupname";
            public static final String SORT = "sorting1";

            public static final String GROUP = "group";


            private static final String API_GROUP = domain + domain_port + "/cardpackage/group";


            public static void getGroup(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_GROUP, headers, params, responseHandler);
            }

            public static void addGroup(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_GROUP, headers, params, null, responseHandler);
            }

            public static void modifyGroup(Context context, String token, Header[] headers, RequestParams params,
                                           JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.put(context, API_GROUP, params, responseHandler);
            }

            public static void delGroup(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.delete(context, API_GROUP, headers, params, responseHandler);
            }
        }

        public static class Consumption {
            public static final String ID = "logid";
            public static final String AMOUNT = "price";
            public static final String DATE = "date";

            public static final String CONSUMPTION = "log";


            private static final String API_CONSUMPTION = domain + domain_port + "/cardpackage/consume/log";


            public static void getConsumption(Context context, String token, Header[] headers, RequestParams params,
                                              JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.get(context, API_CONSUMPTION, headers, params, responseHandler);
            }

            public static void addConsumption(Context context, String token, Header[] headers, RequestParams params,
                                              JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_CONSUMPTION, headers, params, null, responseHandler);
            }

            public static void modifyConsumption(Context context, String token, Header[] headers, RequestParams params,
                                                 JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.put(context, API_CONSUMPTION, params, responseHandler);
            }

            public static void delConsumption(Context context, String token, Header[] headers, RequestParams params,
                                              JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.delete(context, API_CONSUMPTION, headers, params, responseHandler);
            }
        }

        public static class Sync {
            public static final String ID = "uuid";
            public static final String MODULE = "module";
            public static final String OPERATION = "operation";
            public static final String TIMESTAMP = "modifedtime";


            private static final String API_CHANGELOG = domain + domain_port + "/cardpackage/sync";


            public static void getChangelog(Context context, String token, Header[] headers, RequestParams params,
                                            JsonHttpResponseHandler responseHandler) {
                client.addHeader("AccessToken", token);
                client.post(context, API_CHANGELOG, headers, params, null, responseHandler);
            }
        }
    }

    // add for template
    private static final String API_TEMPLATE = domain + domain_port + "/vcard/template";

    public static void addTemplate(Context context, String token, Header[] headers, RequestParams params,
                                   JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_TEMPLATE, headers, params, null, responseHandler);
    }

    public static void deleteTemplate(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.delete(context, API_TEMPLATE, headers, params, responseHandler);
    }

    public static void updateTemplate(Context context, String token, Header[] headers, RequestParams params,
                                      JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.put(context, API_TEMPLATE, params, responseHandler);
    }

    public static void getTemplate(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_TEMPLATE, headers, params, responseHandler);
    }

    public static void getTemplateSync(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_TEMPLATE, headers, params, responseHandler);
    }

    public static void getCompanyLogo(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_COMPANY_LOGO, headers, params, responseHandler);
    }

    public static void syncTemplate(Context context, String token, Header[] headers, RequestParams params,
                                    JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.post(context, API_SYNC, headers, params, null, responseHandler);
    }

    public static final String API_TEMPLATE_RES = domain + domain_port + "/vcard/template/cardres";

    public static void getTemplateRes(Context context, String token, Header[] headers, RequestParams params,
                                      FileAsyncHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_TEMPLATE_RES, headers, params, responseHandler);
    }

    private static final String API_CONTACT_HOT_TAGS = domain + domain_port + "/contact/vcard/hottags";

    public static void getContactHotTags(Context context, String token, Header[] headers, RequestParams params,
                                         JsonHttpResponseHandler responseHandler) {

        client.addHeader("AccessToken", token);
        client.get(context, API_CONTACT_HOT_TAGS, headers, params, responseHandler);
    }

    public static class BatchRequest {
        public static final String URL = "url";
        public static final String METHOD = "method";
        public static final String SEQ = "seq";
        public static final String OBJECT = "object";

        public static final String POST = "POST";
        public static final String GET = "GET";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";

        private static final String API_BATCH = domain + domain_port + "/batch";

        public static void applyBatch(Context context, String token, Header[] headers, RequestParams params,
                                      JsonHttpResponseHandler responseHandler) {
            client.addHeader("AccessToken", token);
            client.post(context, API_BATCH, headers, params, null, responseHandler);
        }

        public static void applyBatchSync(Context context, String token, Header[] headers, RequestParams params,
                                          JsonHttpResponseHandler responseHandler) {
            syncHttpClient.addHeader("AccessToken", token);
            syncHttpClient.post(context, API_BATCH, headers, params, null, responseHandler);
        }
    }


    protected static SyncHttpClient syncHttpClient = new SyncHttpClient();

    public static void loginSync(Context context, RequestParams params,
                                 JsonHttpResponseHandler responseHandler) {
        syncHttpClient.post(context, API_OAUTH, params, responseHandler);
    }

    public static void getContactCardSync(Context context, String token, RequestParams params, JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_CONTACT_CARD, null, params, responseHandler);
    }

    public static void getContactCardResSync(Context context, String token, Header[] headers, RequestParams params, FileAsyncHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_CONTACT_CARD_RES, headers, params, responseHandler);
    }

    public static void downloadFileSync(Context context, String url, FileAsyncHttpResponseHandler responseHandler) {
        syncHttpClient.get(context, url, null, new RequestParams(), responseHandler);
    }

    public static void syncContactCardSync(Context context, String token, Header[] headers, RequestParams params,
                                           JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_CONTACT_CARD_SYNC, headers, params, null, responseHandler);
    }

    public static void addContactCardSync(Context context, String token, Header[] headers, RequestParams params,
                                          JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_CONTACT_CARD, headers, params, null, responseHandler);
    }

    public static void delContactCardSync(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.delete(context, API_CONTACT_CARD, headers, params, responseHandler);
    }

    public static void updateContactCardSync(Context context, String token, Header[] headers, RequestParams params,
                                             JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.put(context, API_CONTACT_CARD, params, responseHandler);
    }

    public static void postScanCardSync(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_SCANCARD, headers, params, null, responseHandler);
    }

    public static void updateScanCardMarkPointSync(Context context, String token, Header[] headers, RequestParams params,
                                                   JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.put(context, API_SCANCARD_MARKPOINT, params, responseHandler);
    }

    public static void getContactCardGroupsSync(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_CONTACT_CARDGROUP, headers, params, responseHandler);
    }

    public static void addContactCardGroupSync(Context context, String token, Header[] headers, RequestParams params,
                                               JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_CONTACT_CARDGROUP, headers, params, null, responseHandler);
    }

    public static void updateContactCardGroupSync(Context context, String token, Header[] headers, RequestParams params,
                                                  JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.put(context, API_CONTACT_CARDGROUP, params, responseHandler);
    }

    public static void delContactCardGroupSync(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.delete(context, API_CONTACT_CARDGROUP, headers, params, responseHandler);
    }

    public static void syncCardGroupSync(Context context, String token, Header[] headers, RequestParams params,
                                         JsonHttpResponseHandler responseHandler) {

        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_CONTACT_CARDGROUP_SYNC, headers, params, null, responseHandler);
    }

    public static RequestHandle postSync(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        String token = TokenServiceManager.getServerToken(false);
        syncHttpClient.addHeader("AccessToken", token);
        return syncHttpClient.post(context, url, params, responseHandler);
    }

    public static RequestHandle getSync(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        String token = TokenServiceManager.getServerToken(false);
        syncHttpClient.addHeader("AccessToken", token);
        return syncHttpClient.get(context, url, params, responseHandler);
    }

    public static RequestHandle putSync(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        String token = TokenServiceManager.getServerToken(false);
        syncHttpClient.addHeader("AccessToken", token);
        return syncHttpClient.put(context, url, params, responseHandler);
    }

    public static RequestHandle deleteSync(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        String token = TokenServiceManager.getServerToken(false);
        syncHttpClient.addHeader("AccessToken", token);
        return syncHttpClient.delete(context, url, null, params, responseHandler);
    }

    public static RequestHandle post(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler, boolean async) {
        AsyncHttpClient tmpClient;
        if (async) {
            tmpClient = client;
        } else {
            tmpClient = syncHttpClient;
        }

        String token = TokenServiceManager.getServerToken();
        tmpClient.addHeader("AccessToken", token);
        return tmpClient.post(context, url, params, responseHandler);
    }

    public static RequestHandle get(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler, boolean async) {
        AsyncHttpClient tmpClient;
        if (async) {
            tmpClient = client;
        } else {
            tmpClient = syncHttpClient;
        }

        String token = TokenServiceManager.getServerToken();
        tmpClient.addHeader("AccessToken", token);
        return tmpClient.get(context, url, params, responseHandler);
    }

    public static RequestHandle put(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler, boolean async) {
        AsyncHttpClient tmpClient;
        if (async) {
            tmpClient = client;
        } else {
            tmpClient = syncHttpClient;
        }

        String token = TokenServiceManager.getServerToken();
        tmpClient.addHeader("AccessToken", token);
        return tmpClient.put(context, url, params, responseHandler);
    }

    public static RequestHandle put(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        String token = TokenServiceManager.getServerToken();
        client.addHeader("AccessToken", token);
        return client.put(context, url, params, responseHandler);
    }

    public static RequestHandle delete(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        String token = TokenServiceManager.getServerToken();
        client.addHeader("AccessToken", token);
        return client.delete(context, url, null, params, responseHandler);
    }

    private static final String API_CARD = domain + domain_port + "/cardpackage";
    private static final String API_DOWNLOAD = domain + domain_port + "/cardpackage/download";
    private static final String API_CHANGELOG = domain + domain_port + "/cardpackage/sync";


    public static void getWalletChangelog(Context context, String token, Header[] headers, RequestParams params,
                                          JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_CHANGELOG, headers, params, null, responseHandler);
    }

    public static void getWalletCard(Context context, String token, Header[] headers, RequestParams params,
                                     JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_CARD, headers, params, responseHandler);
    }

    public static void addWalletCard(Context context, String token, Header[] headers, RequestParams params,
                                     JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_CARD, headers, params, null, responseHandler);
    }

    public static void modifyWalletCard(Context context, String token, Header[] headers, RequestParams params,
                                        JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.put(context, API_CARD, params, responseHandler);
    }

    public static void delWalletCard(Context context, String token, Header[] headers, RequestParams params,
                                     JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.delete(context, API_CARD, headers, params, responseHandler);
    }

    private static final String API_REMARK_SYNC = domain + domain_port + "/contact/common/syncremark";

    public static void syncRemark(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_REMARK_SYNC, headers, params, responseHandler);
    }

    public static void syncRelationPerson(Context context, String token, Header[] headers, RequestParams params, JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.get(context, API_REMARK_SYNC, headers, params, responseHandler);
    }

    public static void cardChangeMessageSync(Context context, String token, Header[] headers, RequestParams params,
                                             JsonHttpResponseHandler responseHandler) {
        syncHttpClient.addHeader("AccessToken", token);
        syncHttpClient.post(context, API_SEND_CARD_SYNC_MESSAGE, headers, params, null, responseHandler);
    }

    public static void cardChangeMessage(Context context, String token, Header[] headers, RequestParams params,
                                         JsonHttpResponseHandler responseHandler) {
        client.addHeader("AccessToken", token);
        client.post(context, API_SEND_CARD_SYNC_MESSAGE, headers, params, null, responseHandler);
    }
}
