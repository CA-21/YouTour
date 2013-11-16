package com.sysu.youtour.util;

public class GlobalConst {
    public static final String   SDCARD_JSONCACHE_DIR    = "/youtour/jsoncache";

    public static final String   SDCARD_CACHE_DIR        = "/youtour/cache";

    public static final String   SDCARD_HOSTIP_DIR       = "/youtour/hostip";

    public static final String[] TOPIC_ITEMS             = new String[] { "全部主题", "历史", "艺术", "时尚", "体育", "家庭", "生活方式",
            "自然"                                        };

    public static final String[] SORT_METHORD            = new String[] { "默认排序", "按旅程时间", "按旅程距离", "价格由低到高", "价格由高到低",
            "最新发布"                                      };

    public static final String   EXPLORE_PLACE           = "roughPlace";

    public static final String   EXPLORE_TOPIC           = "throughTopic";

    // public static final String HOST = "http://103.31.20.60:3000";
    public static String         HOST                    = "";

    public static final String   HOST_URL                = "https://raw.github.com/shenguojun/YouTour/master/init.properties";

    // public static String EXPLORE_URL = HOST + "/androidtest";
    public static final String   URL_HEADER_ADDTOP       = "/browsebyaddresstopic?";

    public static final String   URL_HAEDER_ADD          = "/browsebyaddress?";

    public static final String   URL_HAEDER_TOP          = "/browsebytopic?";

    public static final String   URL_HAEDER_ALL          = "/browseallbytime?";

    public static final String   URL_HAEDER_LOC          = "/browsebylocation?";

    public static final String   URL_HAEDER_LINEID       = "/browsebyid?";

    // public static final String URL_APP_UPDATE =
    // "https://dl.dropboxusercontent.com/u/109241536/update2.json";
    public static final String   URL_APP_UPDATE          = "https://raw.github.com/shenguojun/YouTour/master/update.json";

    public static final String   _ID                     = "_id";

    public static final String   JSON_FILE_NAME          = "lineJson";

    public static final String   LINE_THUMBNAIL_DIR_NAME = "thumbnail_dir";

    public static final String   ALL_DOWNLOADED          = "alldownload";
}