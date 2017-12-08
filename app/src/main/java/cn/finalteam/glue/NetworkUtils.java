package cn.finalteam.glue;


public class NetworkUtils {

    public static final String TAG = "NetworkUtils";

    public static final String start = "start";
    public static final String rows = "rows";
    public static final String fields = "fields";
    public static final String sort = "sort";
    public static final String lastsynctime = "lastsynctime";
    public static final String lastmodify = "lastmodify";

    // Cards
    public static final String localuuid = "localuuid";
    public static final String contact = "contact";
    public static final String self = "self";
    public static final String remark = "remark";
    public static final String selfmark = "selfmark";
    public static final String nindex = "nindex";
    public static final String sorting = "sorting";
    public static final String cardtype = "cardtype";
    public static final String cardfrom = "cardfrom";
    public static final String contactid = "contactid";
    public static final String vcard = "vcard";
    public static final String picture = "picture";
    public static final String cardres = "cardres";
    public static final String ifsetempty = "ifsetempty";//是否置空名片背景1，电子名片正面2、反面图片3；正反面图片4；默认值0
    public static final String picturea = "picturea";
    public static final String pictureb = "pictureb";
    public static final String accuracy = "accuracy";
    public static final String picpatha = "picpatha";
    public static final String picpathb = "picpathb";
    public static final String direction = "direction";
    public static final String vcardid = "vcardid";
    public static final String selfvcardid = "selfvcardid";
    public static final String vcards = "vcards";
    public static final String path = "path";
    public static final String contacts = "contacts";
    public static final String clientid = "clientid";
    public static final String language = "language";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String mobile = "mobile";
    public static final String markpoint = "markpoint";
    public static final String layout = "layout";
    public static final String xyz = "xyz";// exchange card time and address
    public static final String xyztime = "xyztime";
    public static final String onlinetime = "onlinetime";// card exchange time field.
    public static final String reasonid = "reasonid";
    public static final String avatar = "avatar";
    public static final String background = "background";
    public static final String signature = "signature";
    public static final String payfee = "payfee";
    public static final String identityname = "identityname";
    public static final String exchid = "exchid";
    public static final String sourceuuid = "sourceuuid";
    public static final String iskf = "iskf";
    public static final String cardpublic = "public";
    public static final String certifcation = "certifcation";
    public static final String privateset = "privateset";
    public static final String uuid = "uuid";
    public static final String isimportant = "isimportant";
    public static final String source = "source";
    public static final String cardprivate = "private";
    public static final String ocrhandlestate = "ocrhandlestate";
    public static final String showbtn = "showbtn";

    // scan card handle state
    public static final String handlestate = "handlestate";
    public static final String neverhandle = "neverhandle";
    public static final String createtime = "createtime";
    public static final String origin = "origin";
    public static final String scannerid = "scannerid";
    public static final String batchid = "batchid";
    // for share card.
    public static final String key = "key";
    public static final String timestamp = "timestamp";
    public static final String deadline = "deadline";

    public static final String friend = "friend";
    public static final String ifupdate = "ifupdate";
    // for im
    public static final String disturb = "disturb";
    public static final String shielded = "shielded";
    public static final String im_client_id = "im_client_id";
    public static final String personal_background = "personal_background";

    // friend
    public static final String fuserid = "fuserid";
    public static final String list = "list";
    public static final String fmobile = "fmobile";
    public static final String lastcontacted = "lastcontacted";
    public static final String createdtime = "createdtime";
    public static final String realname = "realname";
    public static final String imid = "imid";
    public static final String fromuuid = "fromuuid";
    public static final String touuid = "touuid";


    // Templates
    public static final String tempid = "tempid";
    public static final String bizuuid = "bizuuid";
    public static final String type = "type";
    public static final String templates = "templates";
    public static final String isbuy = "isbuy";
    public static final String snapshot = "snapshot";
    public static final String persondata = "persondata";
    public static final String cardtypename = "cardtypename";
    public static final String kwds = "kwds";
    public static final String cardunits = "cardunits";
    public static final String cardunitsname = "cardunitsname";
    public static final String keyword = "keyword";
    public static final String tagid = "tagid";
    public static final String agreement = "agreement";
    public static final String rule = "rule";//规则json 	123
    public static final String bin = "bin";  //ｂｉｎ码列表 	124
    public static final String issynch = "issynch"; //是否同步资源
    public static final String isre = "isre";


    // Groups
    public static final String group = "group";
    public static final String color = "color";
    public static final String groupid = "groupid";
    public static final String groups = "groups";
    public static final String cardgroupid = "cardgroupid";
    public static final String numcards = "numcards";

    // Events
    public static final String title = "title";
    public static final String content = "content";
    public static final String starttime = "starttime";
    public static final String endtime = "endtime";
    public static final String timezone = "timezone";
    public static final String repeating = "repeating";
    public static final String rendtime = "rendtime";
    public static final String remindtime = "remindtime";
    public static final String eventid = "eventid";
    public static final String events = "events";
    public static final String style = "style";
    public static final String founder = "isfounder";
    public static final String location = "address";
    public static final String allday = "allday";

    // Files
    public static final String directoryid = "directoryid";
    public static final String name = "name";
    public static final String meta = "meta";
    public static final String fileid = "fileid";
    public static final String directories = "directories";
    public static final String files = "files";
    public static final String id = "id";
    public static final String logo = "logo";

    //remark
    public static final String REMARK_SCHEDULE = "remark_schedule";
    public static final String VCARDID = "vcardid";
    public static final String TYPE = "type";
    public static final String STATUS = "status";
    public static final String LASTMODIFY = "lastmodify";

    // Basic response
    public static final String head = "head";
    public static final String body = "body";
    public static final String status = "status";
    public static final String sendstatus = "sendstatus";
    public static final String error = "error";
    public static final String errorcode = "errorcode";
    public static final String description = "description";
    public static final String operation = "operation";
    public static final String modifiedtime = "modifiedtime"; // 新建和修改接口返回
    public static final String numfound = "numfound";
    public static final String modifedtime = "modifedtime"; // 同步接口返回
    public static final String add = "add";
    public static final String modify = "modify";
    public static final String delete = "delete";
    public static final String resmd5 = "resmd5";
    public static final String respath = "respath";
    public static final String resthumbpath = "resthumbpath";

    //function card
    public static final String word = "word";
    public static final String q = "q";

    public static final String RESPONSE_CONTENT_BINARY = "application/binary";
    public static final String RESPONSE_CONTENT_JSON = "application/json";
    public static final String RESPONSE_CONTENT_TYPE = "Content-Type";
    public static final String RESPONSE_CONTENT_MD5 = "Content-md5";
    public static final String RESPONSE_CONTENT_LENGTH = "Content-Length";

    public static class ErrorCode {
        public static int NO_DATA = 999004; // 数据不存在
        public static int DATA_EXISTS = 999005; // 删除的该张名片 有好友拥有，不允许删除
        public static int UNKNOWN = 999001; // 未知错误
    }

    public static class Error {
        public static String CONNECT_TIMEOUT_EXCEPTION = "org.apache.http.conn.ConnectTimeoutException";
        public static String SOCKET_TIMEOUT_EXCEPTION = "java.net.SocketTimeoutException";
    }

}
