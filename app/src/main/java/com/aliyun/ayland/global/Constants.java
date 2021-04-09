package com.aliyun.ayland.global;

import android.os.Environment;

/**
 * @author fr
 * 常量类
 */
public class Constants {
    // 按键点击间隔
    public static final int clickDelay = 500;
    //传递的参数保存的图片的地址
    public static final String IntentKeyFilePath = "IntentKeyFilePath";

    public static class Config {
        public static final String PKG_NAME = "com.anthouse.lgcs";
        public static final int SIP_PORT = 8080;
        public static final String COMMUNITY_SERVER_NAME_REG = "com.atie.sipclient.door.community";
        public static final String DEFAULT_DNS = "8.8.8.8";
        public static final String APPKEY = "28065618";
        public static final String AESPWD = "atsmartlife12345";

        public static final String BASE_URL = "http://server.atsmartlife.com/";
//        public static final String BASE_ALISAAS_URL = "http://alisaas.atsmartlife.com";
        public static final String BASE_ALISAAS_URL = "https://smartlife.cifi.com.cn";
//        public static final String SERVER_BASE_URL = "https://smartlife.cifi.com.cn/villagecenter/%s";
//        public static final String SERVER_BASE_URL = "http://alisaas.atsmartlife.com:9001/villagecenter/%s";
        public static final String SERVER_BASE_URL = "https://smartlife.cifi.com.cn/villagecenter/%s";
        public static final String SERVER_URL_GETACCESSTOKEN = "login/auth/getAccessToken";
        public static final String SERVER_URL_APPLOGIN = "login/auth/applogin";

        public static final String SERVER_URL_SHORTCUTLIST = "application/shortCut/shortcutList";
        public static final String SERVER_URL_CAROUSELIMAGE = "application/common/carouselImage";
        public static final String SERVER_URL_GETAPPLIST = "application/app/getAppList";
        public static final String SERVER_URL_INSERTAPPLIST = "application/app/insertAppList";
        public static final String SERVER_URL_FINDALLAPPLICATION = "application/app/findAllApplication";
        public static final String SERVER_URL_FINDSAFETYAPPLICATION = "application/app/findSafetyApplication";
        public static final String SERVER_URL_GETWEATHER = "application/weather/getWeather";
        public static final String SERVER_URL_UPDATEAPPSORT = "application/app/updateAppSort";
        public static final String SERVER_URL_GETGOODSLIST = "application/shop/getGoodsList";
        public static final String SERVER_URL_GETGOODSTYPELIST = "application/shop/getGoodsTypeList";
        public static final String SERVER_URL_SHORTCUTUPDATE = "application/shortCut/shortcutUpdate";
        public static final String SERVER_URL_VISITORRESERVATION = "application/visitor/visitorReservation";
        public static final String SERVER_URL_ADDVISITORRESERVATION = "application/visitor/addVisitorReservation";
        public static final String SERVER_URL_GETVISITORRESERVATION = "application/visitor/getVisitorReservation";
        public static final String SERVER_URL_GETVISITORRESERVATIONQRCODE = "application/visitor/getVisitorReservationQRCode";
        public static final String SERVER_URL_GETVISITORRESERVATIONLIST = "application/visitor/getVisitorReservationList";
        public static final String SERVER_URL_GETVISITORRESERVATIONRECORDLIST = "application/visitor/getVisitorReservationRecordList";
        public static final String SERVER_URL_GETALLVISITORRESERVATIONLIST = "application/visitor/getAllVisitorReservationList";
        public static final String SERVER_URL_FINDVISITORPAGE = "application/visitor/findVisitorPage";
        public static final String SERVER_URL_ADDIMGFILE = "application/common/addImgFile";
        public static final String SERVER_URL_GETENVIRONMENTRECOMMENDSCENELIST = "application/recommendScene/getEnvironmentRecommendSceneList";
        public static final String SERVER_URL_GETENVIRONMENTRECOMMENDSCENEINFO = "application/recommendScene/getEnvironmentRecommendSceneInfo";
        public static final String SERVER_URL_UPDATEENVIRONMENTRECOMMENDSCENEDEVICEINFO = "application/recommendScene/updateEnvironmentRecommendSceneDeviceInfo";
        public static final String SERVER_URL_UPDATEENVIRONMENTRECOMMENDSCENEINFO = "application/recommendScene/updateEnvironmentRecommendSceneInfo";
        public static final String SERVER_URL_GETOUTDOORENVIRONMENTDATA = "application/environmentData/getOutdoorEnvironmentData";
        public static final String SERVER_URL_GETOUTDOORENVIRONMENTHISTORYDATA = "application/environmentData/getOutdoorEnvironmentHistoryData";
        public static final String SERVER_URL_GETINDOORENVIRONMENTDATA = "application/environmentData/getIndoorEnvironmentData";
        public static final String SERVER_URL_GETINDOORENVIRONMENTHISTORYDATA = "application/environmentData/getIndoorEnvironmentHistoryData";
        public static final String SERVER_URL_CREATEVISITOR = "application/visitor/createVisitor";
        public static final String SERVER_URL_FINDELDERLYALONECAREINFO = "application/security/findElderlyAloneCareInfo";
        public static final String SERVER_URL_ADDELDERLYALONECARE = "application/security/addElderlyAloneCare";

        public static final String SERVER_URL_UPDATEUSERHEADIMGE = "user/account/updateUserHeadImge";
        public static final String SERVER_URL_UPDATEUSERINFO = "user/account/updateUserInfo";
        public static final String SERVER_URL_GETUSERINFO = "user/account/getUserInfo";
        public static final String SERVER_URL_CODETOREGISTER = "user/register/codeToRegister";
        public static final String SERVER_URL_REGISTERPHONE = "user/register/registerPhone";
        public static final String SERVER_URL_GETMPASSWDCODE = "user/register/getMpasswdcode";
        public static final String SERVER_URL_RESETPASSWD = "user/register/resetPasswd";

        public static final String SERVER_URL_FINDTREEBUILDING = "baseservice/space/findTreeBuilding";
        public static final String SERVER_URL_FINDUSERHOUSE = "baseservice/space/findUserHouse";
        public static final String SERVER_URL_SETPRESENT = "baseservice/space/setPresent";
        public static final String SERVER_URL_FINDPRESENT = "baseservice/space/findPresent";
        public static final String SERVER_URL_HOUSEDEVICE = "baseservice/room/houseDevice";
        public static final String SERVER_URL_FINDORDERROOM = "baseservice/room/findOrderRoom";
        public static final String SERVER_URL_DELETEROOM = "baseservice/room/deleteRoom";
        public static final String SERVER_URL_FINDHOUSEDEV = "baseservice/room/findHouseDev";
        public static final String SERVER_URL_UPDATEROOM = "baseservice/room/updateRoom";
        public static final String SERVER_URL_BINDDEVROOM = "baseservice/room/bindDevRoom";
        public static final String SERVER_URL_UNBINDDEVROOM = "baseservice/room/unbindDevRoom";
        public static final String SERVER_URL_CREATEROOM = "baseservice/room/createRoom";
        public static final String SERVER_URL_CONTROL = "baseservice/device/control";
        public static final String SERVER_URL_BINDDEVBUILDING = "baseservice/device/bindDevbuilding";
        public static final String SERVER_URL_UNBINDDEVBUILDING = "baseservice/device/unbindDevbuilding";
        public static final String SERVER_URL_GETDEVICETYPELIST = "baseservice/device/getDeviceTypeList";
        public static final String SERVER_URL_RUNGYMDEVICE = "baseservice/device/runGymDevice";
        public static final String SERVER_URL_DEVICEDETAIL = "baseservice/device/deviceDetail";
        public static final String SERVER_URL_FINDDEVICETCA = "baseservice/device/findDeviceTca";
        public static final String SERVER_URL_FINDSHORTCUTDEVICE = "baseservice/device/findShortcutDevice";
        public static final String SERVER_URL_FINDUSERCAMERA = "baseservice/device/findUserCamera";
        public static final String SERVER_URL_GETSAFETYEQUIPMENT = "baseservice/device/getSafetyEquipment";
        public static final String SERVER_URL_OPERATORCAMERA = "baseservice/device/operatorCamera";
        public static final String SERVER_URL_GETBRIGHTNESSLIGHT = "baseservice/device/getBrightnessLight";
        public static final String SERVER_URL_GETTSL = "baseservice/device/getTsl";
        public static final String SERVER_URL_FINDDEVICES = "baseservice/device/findDevices";
        public static final String SERVER_URL_SHAREDEVICE = "baseservice/device/shareDevice";
        public static final String SERVER_URL_UNSHAREDDEVICE = "baseservice/device/unsharedDevice";
        public static final String SERVER_URL_PRODUCTLIST = "baseservice/product/productList";
        public static final String SERVER_URL_CATEGORY = "baseservice/product/category";
        public static final String SERVER_URL_SCENECREATE = "baseservice/scene/sceneCreate";
        public static final String SERVER_URL_SCENEBIND = "baseservice/scene/sceneBind";
        public static final String SERVER_URL_GETUSERTCALIST = "baseservice/scene/getUserTcaList";
        public static final String SERVER_URL_GETPRODUCTTCAGET = "baseservice/scene/getProductTcaGet";
        public static final String SERVER_URL_SCENELIST = "baseservice/scene/sceneList";
        public static final String SERVER_URL_USERSCENELIST = "baseservice/scene/userSceneList";
        public static final String SERVER_URL_GETLOCALUSERSCENELIST = "baseservice/scene/getLocalUserSceneList";
        public static final String SERVER_URL_GETSCENELIST = "baseservice/scene/getSceneList";
        public static final String SERVER_URL_SCENEDEPLOYREVOKE = "baseservice/scene/sceneDeployRevoke";
        public static final String SERVER_URL_SCENEGET = "baseservice/scene/sceneGet";
        public static final String SERVER_URL_SCENEDELETE = "baseservice/scene/sceneDelete";
        public static final String SERVER_URL_LINKAGEIMAGELIST = "baseservice/scene/linkageImageList";
        public static final String SERVER_URL_GETUSERSCENERECORD = "baseservice/scene/getUserSceneRecord";
        public static final String SERVER_URL_SCENETCAUPDATE = "baseservice/scene/sceneTcaUpdate";
        public static final String SERVER_URL_BASEINFOUPDATE = "baseservice/scene/baseinfoUpdate";
        public static final String SERVER_URL_SCENEINSTANCERUN = "baseservice/scene/sceneInstanceRun";
        public static final String SERVER_URL_ADDMORNINGGETUPPATTERN = "baseservice/scene/addMorningGetUpPattern";
        public static final String SERVER_URL_FINDPUBLICDEVICE = "baseservice/equipment/findPublicDevice";
        public static final String SERVER_URL_FINDFAMILYMEMBER = "baseservice/person/findFamilyMember";
        public static final String SERVER_URL_ENTRYFAMILYMEMBER = "baseservice/person/entryFamilyMember";
        public static final String SERVER_URL_UPDATEFAMILYMEMBER = "baseservice/person/updateFamilyMember";
        public static final String SERVER_URL_FINDFAMILYMEMBERFORCARE = "baseservice/person/findFamilyMemberForCare";
        public static final String SERVER_URL_TRANSFERADMINAUTHORITY = "baseservice/person/transferAdminAuthority";
        public static final String SERVER_URL_MEMBERROOM = "baseservice/person/memberRoom";
        public static final String SERVER_URL_CHANGEVIEW = "baseservice/person/changeView";
        public static final String SERVER_URL_DELETEMEMBER = "baseservice/person/deleteMember";
        public static final String SERVER_URL_INVITEPERSON = "baseservice/person/invitePerson";
        public static final String SERVER_URL_FINDFAMILYMEMBERFOROUTALONE = "baseservice/person/findFamilyMemberForOutAlone";
        public static final String SERVER_URL_FINDFAMILYMEMBERFORHABITABNORMAL = "baseservice/person/findFamilyMemberForHabitAbnormal";
        public static final String SERVER_URL_ADDMONITORRECORD = "baseservice/monitor/addMonitorRecord";
        public static final String SERVER_URL_MONITORHEARTBEAT = "baseservice/monitor/monitorHeartBeat";
        public static final String SERVER_URL_GETMONITORRECORD = "baseservice/monitor/getMonitorRecord";
        public static final String SERVER_URL_GETTALKBACKRECORD = "baseservice/talk/getTalkbackRecord";
        public static final String SERVER_URL_TALKBACKHEARTBEAT = "baseservice/talk/talkbackHeartBeat";
        public static final String SERVER_URL_SETREFUSEANSWER = "baseservice/talk/setRefuseAnswer";

        public static final String SERVER_URL_GETGYMLIST = "colorfulbox/gym/getGymList";
        public static final String SERVER_URL_GETGYMPROJECTLIST = "colorfulbox/gym/getGymProjectList";
        public static final String SERVER_URL_GETCHILDRENROOMPROJECTLIST = "colorfulbox/childrenroom/getChildrenRoomProjectList";
        public static final String SERVER_URL_ADDCHILDRENROOMAPPOINTMENT = "colorfulbox/childrenroom/addChildrenRoomAppointment";
        public static final String SERVER_URL_GETCHILDRENROOMHISTORICALAPPOINTMENTLIST = "colorfulbox/childrenroom/getChildrenRoomHistoricalAppointmentList";
        public static final String SERVER_URL_GETCHILDRENROOMAPPOINTMENTLISTRECORD = "colorfulbox/childrenroom/getChildrenRoomAppointmentListRecord";
        public static final String SERVER_URL_GETAPPOINTMENTLISTRECORD = "colorfulbox/gym/getAppointmentListRecord";
        public static final String SERVER_URL_GETHHISTORICALAPPOINTMENTLIST = "colorfulbox/gym/gethHistoricalAppointmentList";
        public static final String SERVER_URL_GETALLAPPOINTMENT = "colorfulbox/common/getAllAppointment";
        public static final String SERVER_URL_ADDAPPOINTMENT = "colorfulbox/gym/addAppointment";
        public static final String SERVER_URL_CREATECOLORFULBOXORDER = "colorfulbox/aliPay/createColorfulBoxOrder";

        public static final String SERVER_URL_GETPUBLICCAMERA = "aibox/face/getPublicCamera";
        public static final String SERVER_URL_QUERYLIVESTREAMING = "aibox/visual/queryLiveStreaming";
        public static final String SERVER_URL_FINDCARINGRECORD = "aibox/care/findCaringRecord";
        public static final String SERVER_URL_SETMONITORING = "aibox/care/setMonitoring";
        public static final String SERVER_URL_FINDLASTCARINGRECORDBYPERSONCODE = "aibox/care/findLastCaringRecordByPersonCode";
        public static final String SERVER_URL_CANCELOUTALONE = "aibox/property/cancelOutAlone";
        public static final String SERVER_URL_SETOUTALONE = "aibox/property/setOutAlone";
        public static final String SERVER_URL_ADDHABITABNORMAL = "aibox/habitabnormal/addHabitAbnormal";
        public static final String SERVER_URL_CANCELHABITABONRMALITY = "aibox/habitabnormal/cancelHabitabonrmality";

        public static final String SERVER_URL_GETPARKLOTLIST = "pass/carScene/getParkLotList";
        public static final String SERVER_URL_GETDEVICELIST = "pass/carScene/getDeviceList";
        public static final String SERVER_URL_GETUSERLICENCELIST = "pass/carScene/getUserLicenceList";
        public static final String SERVER_URL_GETFACE = "pass/face/getFace";
        public static final String SERVER_URL_ADDFACE = "pass/face/addFace";
        public static final String SERVER_URL_ADDFACE2 = "pass/face/addFace2";
        public static final String SERVER_URL_DELETEFACE = "pass/face/deleteFace";
        public static final String SERVER_URL_ADDUSERFACEVILLAGE = "pass/face/addUserFaceVillage";
        public static final String SERVER_URL_FACEVILLAGELIST = "pass/face/faceVillageList";
        public static final String SERVER_URL_FINDCARPARKRECORD = "pass/access/findCarParkRecord";
        public static final String SERVER_URL_QUERYVISITORRECORD = "pass/access/queryVisitorRecord";
        public static final String SERVER_URL_FINDPARKINGDATA = "pass/park/findParkingData";
        public static final String SERVER_URL_ADDMYCAR = "pass/park/addMyCar";
        public static final String SERVER_URL_FINDSPACE = "pass/park/findSpace";
        public static final String SERVER_URL_FINDVEHICLEBYLICENCE = "pass/park/findVehicleByLicence";
        public static final String SERVER_URL_DELMYCAR = "pass/park/delMyCar";
        public static final String SERVER_URL_GETLICENCEINFO = "pass/park/getLicenceInfo";
        public static final String SERVER_URL_FINDMYLICENCE = "pass/park/findMyLicence";
        public static final String SERVER_URL_CREATEQRCODE = "pass/guard/createQrcode";

        public static final String SERVER_URL_UPLOADCOMMUNITYIMG = "community/uploadCommunityImg";
        public static final String SERVER_URL_GETCOMMUNITYLIST = "community/getCommunityList";
        public static final String SERVER_URL_INSERTCOMMUNITYDYNAMIC = "community/insertCommunityDynamic";
        public static final String SERVER_URL_GETCOMMUNITYDYNAMICLIST = "community/getCommunityDynamicList";
        public static final String SERVER_URL_OPERATEAGREE = "community/operateAgree";
        public static final String SERVER_URL_ADDCOMMENT = "community/addComment";
        public static final String SERVER_URL_GETMYCOMMUNITYLIST = "community/getMyCommunityList";
        public static final String SERVER_URL_GETOTHERCOMMUNITYLIST = "community/getOtherCommunityList";
        public static final String SERVER_URL_ADDUSERCOMMUNITY = "community/addUserCommunity";
        public static final String SERVER_URL_INSERTCOMMUNITYIMG = "community/insertCommunityImg";
        public static final String SERVER_URL_INSERTCOMMUNITY = "community/insertCommunity";
        public static final String SERVER_URL_GETCOMMUNITYTYPELIST = "community/getCommunityTypeList";
        public static final String SERVER_URL_GETCOMMUNITYINFO = "community/getCommunityInfo";
        public static final String SERVER_URL_OUTCOMMUNITY = "community/outCommunity";
        public static final String SERVER_URL_REPORTCOMMUNITY = "community/reportCommunity";
        public static final String SERVER_URL_REPORTDYNAMIC = "community/reportDynamic";

        public static final String SERVER_URL_QUERYUSERFACERECORD = "messagepush/face/queryUserFaceRecord";
        public static final String SERVER_URL_FINDALARMRECORD = "messagepush/record/findAlarmRecord";
    }

    public static class FaceConfig {
        // 为了apiKey,secretKey为您调用百度人脸在线接口的，如注册，识别等。
        // 为了的安全，建议放在您的服务端，端把人脸传给服务器，在服务端端进行人脸注册、识别放在示例里面是为了您快速看到效果
//        public static String apiKey = "HGIgFXpMWWccIo34d9Y9q1C1";//替换为你的apiKey(ak);
//        public static String secretKey = "2iMxeBjG6tZI8Npm24NS2u5Y7QPCcMd0";//替换为你的secretKey(sk);

        public static String licenseID = "AiyunLandZhuoyue-face-android";
        public static String licenseFileName = "idl-license.face-android";

        /**
         * groupId，标识一组用户（由数字、字母、下划线组成），长度限制128B，可以自行定义，只要注册和识别都是同一个组。
         * 详情见 http://ai.baidu.com/docs#/Face-API/top
         * <p>
         * 人脸识别 接口 https://aip.baidubce.com/rest/2.0/face/v2/identify
         * 人脸注册 接口 https://aip.baidubce.com/rest/2.0/face/v2/faceset/user/add
         */

        public static String groupID = "sign_in";//替换为groupID;
    }

    public static class DialType {
        public static final int DIAL_NUMBER = 1;
        public static final int DIAL_LETTER = 2;
    }

    public static class RouterUrl {
        public static final String PLUGIN_ID_DEVICE_CONFIG = "link://router/connectConfig";
    }

    public static class Version {
        public static final int AT = 0;
        public static final int PUJI = 1;
        public static final int HK = 2;
        public static final int TV = 3;
        public static final int CLOUD = 8;

        //香港保利的子版本
        public static final int HK_ANN_AB = 4;
        public static final int HK_ANN_CD = 5;
        public static final int HK_NA = 6;
        public static final int HK_DK = 7;

    }

    public static class ChipVersion {
        public static final int Chip3368 = 3368;
    }

    public static class Key {
        public static final String EffectState = "effect_state";
        public static final String PrefDoorPwd = "pref_door_pwd";
        public static final String PrefAdminPwd = "pref_admin_pwd";
        public static final String PrefDelayClose = "pref_delay_close";
        public static final String prefNotify = "pref_notify";
        public static final String PrefRole = "pref_role";
        public static final String PrefSubRole = "pref_sub_role";
        public static final String PrefMainDoorIp = "main_door_ip";
        public static final String PrefCardAuthPwd = "card_auth_pwd";
        public static final String PrefRemoveAlarm = "remove_alarm";
        public static final String liftIp = "lift_ip";
        public static final String PrefDoorList = "door_list";
        public static final String PrefImageUrls = "pref_image_urls";
        public static final String PrefResetPhone = "reset_phone";
        public static final String PrefResetPhoneArea = "reset_phone_area";
        public static final String ArrearageInfo = "arrearage_info";
        public static final String HomePage = "home_page";
        public static final String PrefInfraredCamera = "enable_infrared_camera";
        public static final String PrefCallVolume = "pref_call_volume";
    }

    public static class OpenDoorType {
        public static final String TypeCard = "card";//卡
        public static final String TypeQrCode = "qrcode";//扫码
        public static final String TypeFace = "face";    //人脸
        public static final String TypePwd = "password";//密码
        public static final String TypeRemote = "remote";//远程
        public static final String TypePhone = "phone";//手机
    }

    public static class CallLiftType {
        public static final String TypeTalk = "talk";
    }

    public static class EventType {
        public static final int CALLLIFT_SUCCESS = 0x10000;
        public static final int PLAY_OPEN_DOOR_SOUND = 0x10001;
        public static final int REFRESH_FACE_USER_LIST = 0x10002;
        public static final int SETTING_EDIT_EVENT = 0x10003;
        public static final int EDIT_MACHINE_INFO = 0x10004;
        public static final int ADD_MACHINE_INFO = 0x10005;
        public static final int REFRESH_MACHINE_LIST = 0x10006;
        public static final int OPEN_DOOR = 0x10007;
        public static final int FACE_OEPN_DOOR = 0x10008;
        public static final int OPEN_FACE_DLG = 0x10009;
        public static final int CLOSE_FACE_DLG = 0x10010;
        public static final int REMOTE_RESTART = 0x10011;
        public static final int FACE_DETECT_MANAGER = 0x10012;
        public static final int OPEN_FACE_USER_ACTIVITY = 0x10013;
        public static final int ADD_USER_FACE = 0x10014;
        public static final int SETTING_SCROLL_PAGE = 0x10015;
        public static final int MONITOR_NEW_INTENT = 0x10016;
        public static final int CALL_PHONE_APP = 0x10017;

        public static final int WEBSOCK_MSG_INFO = 0x20000;
        public static final int WEBSOCK_STATE = 0x20001;
        public static final int WEBSOCK_NOTIFICATION = 0x20002;
        public static final int RESET_PWD = 0x20004;

        public static final int READ_CARD_INFO = 0x99999;
        public static final int AD_IMAGE_LOAD_OVER = 0x30000;
        public static final int SET_DATETIME = 0x30001;

        public static final int SAVE_OPER = 0x30002;
        public static final int CANCEL_OPER = 0x30003;

        public static final int FOCUS_FORWARD = 0x30004;
        public static final int FOCUS_BACKWARD = 0x30005;

        public static final int ADD_AUTH_CARD = 0x30006;
        public static final int DEL_AUTH_CARD = 0x30007;

        public static final int KEY_POINT = 0x30008;
        public static final int KEY_NUMBER = 0x30009;
        public static final int KEY_DEL = 0x30010;

        public static final int MINUS_SYS_VOL = 0x30011;
        public static final int ADD_SYS_VOL = 0x30012;
        public static final int SCAN_QR_SETTING = 0x30013;
        public static final int UPDATE_VERSION = 0x300014;

        public static final int FACE_SETTING = 0x300015;

        public static final int HUMAN_DETECT = 0x300016;
        public static final int USER_INTERACTION = 0x300017;
        public static final int HUMAN_DETECT_TIMEOUT = 0x300018;
        public static final int SET_DETECT_FLAG = 0x300019;

        public static final int GET_TOKEN_SUCCESS = 0x40001;
//        public static final int LOGIN_MODEL = 0x40002;
//        public static final int SCENE_MODEL = 0x40003;
    }

    public static class FaceUserRole {
        public static final String Manager = "manager";
        public static final String User = "user";
        public static final String Super = "super";
    }

    public static class FaceUserType {
        public static final String LOCAL = "local";
        public static final String REMOTE = "remote";
    }

    public static class State {
        public static final String Enable = "enable";
        public static final String Disable = "disable";
    }

    public static class Action {
        public static final String OpenDoor = "open_door";
        public static final String TOUCH_PAD = "com.system.touchpad";
    }

    public static class ProductKey {
        public static final String CAMERA_HAIKANG = "a1Q8ZmSKd4f";
        public static final String CAMERA_AITE = "a1PqY2neklA";
//		public static final int FACE_3D = 2;
//		public static final int FACE_AT = 3;
    }
}