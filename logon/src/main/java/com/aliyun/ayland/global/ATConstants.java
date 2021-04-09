package com.aliyun.ayland.global;

/**
 * @author fr
 * 常量类
 */
public class ATConstants {
    // 按键点击间隔
    public static final int clickDelay = 500;
    //传递的参数保存的图片的地址
    public static final String IntentKeyFilePath = "IntentKeyFilePath";

    public static class Config {
        public static final String PKG_NAME = "com.anthouse.xuhui";
        public static final int SIP_PORT = 8080;
        public static final String COMMUNITY_SERVER_NAME_REG = "com.atie.sipclient.door.community";
        public static final String DEFAULT_DNS = "8.8.8.8";
        public static final String CUSTOMER_CODE = "10003";
        public static final String APPKEY = "32585218";
        public static final String AESPWD = "atsmartlife12345";
        public static final String SERVER_URL_TMALL = "http://47.119.154.141/villagecenter/baseservice/v1/tmall";

        public static final String PRIVACY_POLICY_URL = "https://smarthome.cifi.com.cn/cifi_privacy_agreement.html";
        public static final String RELEASE_AGREEMENT_URL = "https://smarthome.cifi.com.cn/cifi_user_agreement.html";

        public static final String BASE_URL = "http://server.atsmartlife.com/";
//        public static final String BASE_ALISAAS_URL = "http://alisaas.atsmartlife.com";
        public static final String BASE_ALISAAS_URL = "https://smartlife.cifi.com.cn";
//        public static final String SERVER_BASE_URL = "https://smartlife.cifi.com.cn/villagecenter/%s";
        public static final String SERVER_BASE_URL = "http://47.119.154.141/villagecenter/%s";
//        public static final String SERVER_BASE_URL = "http://47.115.112.23/aiyun-village-test/%s";
//        public static final String SERVER_BASE_URL = "https://smarthome-test.cifi.com.cn/villagecenter/%s";
        public static final String SERVER_MODEL_BASE_URL = "https://smartlife.cifi.com.cn/villagecenter/%s?accessToken=%s";
//        public static final String SERVER_MODEL_BASE_URL = "https://smarthome-test.cifi.com.cn/villagecenter/%s?accessToken=%s";

        public static final String SERVER_URL_GETACCESSTOKEN = "login/auth/getAccessToken";
//        public static final String SERVER_URL_APPLOGIN = "login/auth/ultimateLogin";
        public static final String SERVER_URL_QRCODELOGINCONFIRM = "login/auth/qrCodeLoginConfirm";
        public static final String SERVER_URL_GETAUTHCODE = "login/app/getAuthCode";
        public static final String SERVER_URL_LOGIN = "login/app/login";

        public static final String SERVER_URL_CAROUSELIMAGE = "application/common/carouselImage";
        public static final String SERVER_URL_ADDIMGFILE = "application/common/addImgFile";
        public static final String SERVER_URL_INSERTAPPLIST = "application/app/insertAppList";
        public static final String SERVER_URL_GETGOODSLIST = "application/shop/getGoodsList";
        public static final String SERVER_URL_GETGOODSTYPELIST = "application/shop/getGoodsTypeList";
        public static final String SERVER_URL_GETVISITORRESERVATIONRECORDLIST = "application/visitor/getVisitorReservationRecordList";
        public static final String SERVER_URL_GETVISITORRESERVATIONLIST = "application/visitor/getVisitorReservationList";
        public static final String SERVER_URL_CREATEVISITOR = "application/visitor/createVisitor";
        public static final String SERVER_URL_GETENVIRONMENTRECOMMENDSCENELIST = "application/recommendScene/getEnvironmentRecommendSceneList";
        public static final String SERVER_URL_GETENVIRONMENTRECOMMENDSCENEINFO = "application/recommendScene/getEnvironmentRecommendSceneInfo";
        public static final String SERVER_URL_UPDATEENVIRONMENTRECOMMENDSCENEDEVICEINFO = "application/recommendScene/updateEnvironmentRecommendSceneDeviceInfo";
        public static final String SERVER_URL_UPDATEENVIRONMENTRECOMMENDSCENEINFO = "application/recommendScene/updateEnvironmentRecommendSceneInfo";

        public static final String SERVER_URL_UPDATEUSERHEADIMGE = "user/account/updateUserHeadImge";
        public static final String SERVER_URL_UPDATEUSERINFO = "user/account/updateUserInfo";
        public static final String SERVER_URL_GETUSERINFO = "user/account/getUserInfo";
        public static final String SERVER_URL_CODETOREGISTER = "user/client/codeToRegister";
        public static final String SERVER_URL_REGISTERPHONE = "user/register/registerPhone";
        public static final String SERVER_URL_GETMPASSWDCODE = "user/register/getMpasswdcode";
        public static final String SERVER_URL_RESETPASSWD = "user/register/resetPasswd";
        public static final String SERVER_URL_REGISTERCLIENT = "user/client/registerClient";

        public static final String SERVER_URL_SHORTCUTUPDATE = "baseservice/shortcut/shortcutUpdate";
        public static final String SERVER_URL_SHORTCUTLIST = "baseservice/shortcut/shortcutList";
        public static final String SERVER_URL_GETAPPLIST = "baseservice/app/getAppList";
        public static final String SERVER_URL_UPDATEAPPSORT = "baseservice/app/updateAppSort";
        public static final String SERVER_URL_GETWEATHER = "baseservice/weather/getWeather";
        public static final String SERVER_URL_GETOUTDOORENVIRONMENTDATA = "baseservice/environmentData/getOutdoorEnvironmentData";
        public static final String SERVER_URL_GETOUTDOORENVIRONMENTHISTORYDATA = "baseservice/environmentData/getOutdoorEnvironmentHistoryData";
        public static final String SERVER_URL_GETINDOORENVIRONMENTDATA = "baseservice/environmentData/getIndoorEnvironmentData";
        public static final String SERVER_URL_GETINDOORENVIRONMENTHISTORYDATA = "baseservice/environmentData/getIndoorEnvironmentHistoryData";
        public static final String SERVER_URL_FINDUSERHOUSE = "baseservice/space/findUserHouse";
        public static final String SERVER_URL_UPDATEPRESENTPLUS = "baseservice/space/updatePresentPlus";
        public static final String SERVER_URL_SETPRESENT = "baseservice/space/setPresent";
        public static final String SERVER_URL_FINDPRESENT = "baseservice/space/findPresent";
        public static final String SERVER_URL_HOUSEDEVICE = "smarthouse/room/houseDevice";
        public static final String SERVER_URL_FINDORDERROOM = "smarthouse/room/findOrderRoom";
        public static final String SERVER_URL_DELETEROOM = "smarthouse/room/deleteRoom";
        public static final String SERVER_URL_FINDHOUSEDEV = "smarthouse/room/findHouseDev";
        public static final String SERVER_URL_UPDATEROOM = "smarthouse/room/updateRoom";
        public static final String SERVER_URL_BINDDEVROOM = "smarthouse/room/bindDevRoom";
        public static final String SERVER_URL_UNBINDDEVROOM = "smarthouse/room/unbindDevRoom";
        public static final String SERVER_URL_CREATEROOM = "smarthouse/room/createRoom";
        public static final String SERVER_URL_PRODUCTLIST = "baseservice/product/productList";
        public static final String SERVER_URL_CATEGORY = "baseservice/product/category";
        public static final String SERVER_URL_SCENECREATE = "smarthouse/scene/sceneCreate";
        public static final String SERVER_URL_SCENEBIND = "smarthouse/scene/sceneBind";
        public static final String SERVER_URL_GETLOCALUSERSCENELIST = "smarthouse/scene/getLocalUserSceneList";
        public static final String SERVER_URL_FINDRECOMMENDTEMPLATELIST = "smarthouse/scene/findRecommendTemplateList";
        public static final String SERVER_URL_GETUSERTCALIST = "smarthouse/scene/getUserTcaList";
        public static final String SERVER_URL_SCENELIST = "smarthouse/scene/sceneList";
        public static final String SERVER_URL_SCENEDEPLOYREVOKE = "smarthouse/scene/sceneDeployRevoke";
        public static final String SERVER_URL_FINDRECOMMENDTEMPLATEACTIONLIST = "smarthouse/scene/findRecommendTemplateActionList";

        public static final String SERVER_URL_GETPRODUCTTCAGET = "smarthouse/scene/getProductTcaGet";
        public static final String SERVER_URL_USERSCENELIST = "smarthouse/scene/userSceneList";
        public static final String SERVER_URL_GETSCENELIST = "smarthouse/scene/getSceneList";
        public static final String SERVER_URL_SCENEGET = "smarthouse/scene/sceneGet";
        public static final String SERVER_URL_SCENEDELETE = "smarthouse/scene/sceneDelete";
        public static final String SERVER_URL_LINKAGEIMAGELIST = "smarthouse/scene/linkageImageList";
        public static final String SERVER_URL_GETUSERSCENERECORD = "smarthouse/scene/getUserSceneRecord";
        public static final String SERVER_URL_ADDTEMPLATESCENE = "smarthouse/scene/addTemplateScene";
        public static final String SERVER_URL_SCENETCAUPDATE = "smarthouse/scene/sceneTcaUpdate";
        public static final String SERVER_URL_BASEINFOUPDATE = "smarthouse/scene/baseinfoUpdate";
        public static final String SERVER_URL_SCENEINSTANCERUN = "smarthouse/scene/sceneInstanceRun";

        public static final String SERVER_URL_FINDDELIVERYSTATUS = "smarthouse/installer/findDeliveryStatus";
        public static final String SERVER_URL_DELIVERYDEVICE = "smarthouse/installer/deliveryDevice";
        public static final String SERVER_URL_DELIVERYHOUSE = "smarthouse/installer/deliveryHouse";
        public static final String SERVER_URL_FINDHOUSESTATUS = "smarthouse/installer/findHouseStatus";

        public static final String SERVER_URL_FINDSCENETMALLLIST = "baseservice/tmscene/findSceneTmallList";
        public static final String SERVER_URL_UPDATESCENETMALLRELATION = "baseservice/tmscene/updateSceneTmallRelation";
        public static final String SERVER_URL_RESETDEVICE = "baseservice/device/resetDevice";
        public static final String SERVER_URL_RUNGYMDEVICE = "smarthouse/device/runGymDevice";
        public static final String SERVER_URL_GETDEVICETYPELIST = "smarthouse/device/getDeviceTypeList";
        public static final String SERVER_URL_GETTSL = "smarthouse/device/getTsl";
        public static final String SERVER_URL_BINDDEVBUILDING = "smarthouse/device/bindDevbuilding";
        public static final String SERVER_URL_CONTROL = "smarthouse/device/control";
        public static final String SERVER_URL_UNBINDDEVBUILDING = "smarthouse/device/unbindDevbuilding";
        public static final String SERVER_URL_FINDDEVICETCA = "smarthouse/device/findDeviceTca";
        public static final String SERVER_URL_GETSAFETYEQUIPMENT = "smarthouse/device/getSafetyEquipment";
        public static final String SERVER_URL_OPERATORCAMERA = "smarthouse/device/operatorCamera";
        public static final String SERVER_URL_FINDDEVICES = "smarthouse/device/findDevices";
        public static final String SERVER_URL_SHAREDEVICE = "smarthouse/device/shareDevice";
        public static final String SERVER_URL_FINDUSERCAMERA = "smarthouse/device/findUserCamera";
        public static final String SERVER_URL_UNSHAREDDEVICE = "smarthouse/device/unsharedDevice";
        public static final String SERVER_URL_FINDSHORTCUTDEVICE = "smarthouse/device/findShortcutDevice";
        public static final String SERVER_URL_DEVICEDETAIL = "smarthouse/device/deviceDetail";
        public static final String SERVER_URL_GETBRIGHTNESSLIGHT = "smarthouse/device/getBrightnessLight";
        public static final String SERVER_URL_SETSENSORDEPLOY = "smarthouse/device/setSensorDeploy";
        public static final String SERVER_URL_SETALLSENSORDEPLOY = "smarthouse/device/setAllSensorDeploy";
        public static final String SERVER_URL_GETSENSORDEPLOYMENT = "smarthouse/device/getSensorDeployment";
        public static final String SERVER_URL_GETFAMILYSECURITY = "smarthouse/device/getFamilySecurity";
        public static final String SERVER_URL_FINDFAMILYMEMBER = "baseservice/basePerson/findFamilyMember";
        public static final String SERVER_URL_ENTRYFAMILYMEMBER = "baseservice/basePerson/entryFamilyMember";
        public static final String SERVER_URL_FINDFAMILYMEMBERFORCARE = "baseservice/basePerson/findFamilyMemberForCare";
        public static final String SERVER_URL_UPDATEMEMBERIDENTITY = "baseservice/basePerson/updateMemberIdentity";
        public static final String SERVER_URL_FINDFAMILYCHILDANDOLD = "baseservice/basePerson/findFamilyChildAndOld";
        public static final String SERVER_URL_UPDATEFAMILYMEMBER = "baseservice/basePerson/updateFamilyMember";
        public static final String SERVER_URL_TRANSFERADMINAUTHORITY = "baseservice/basePerson/transferAdminAuthority";
        public static final String SERVER_URL_MEMBERROOM = "baseservice/basePerson/memberRoom";
        public static final String SERVER_URL_CHANGEVIEW = "baseservice/basePerson/changeView";
        public static final String SERVER_URL_DELETEMEMBER = "baseservice/basePerson/deleteMember";
        public static final String SERVER_URL_INVITEPERSON = "baseservice/basePerson/invitePerson";
        public static final String SERVER_URL_ADDMONITORRECORD = "baseservice/monitor/addMonitorRecord";
        public static final String SERVER_URL_MONITORHEARTBEAT = "baseservice/monitor/monitorHeartBeat";
        public static final String SERVER_URL_GETMONITORRECORD = "baseservice/monitor/getMonitorRecord";
        public static final String SERVER_URL_TALKBACKHEARTBEAT = "baseservice/talk/talkbackHeartBeat";
        public static final String SERVER_URL_HANGUPTALKBACK = "baseservice/talk/hangupTalkback";
        public static final String SERVER_URL_FINDPUBLICDEVICE = "baseservice/baseEquipment/findPublicDevice";
        public static final String SERVER_URL_SYNCINFO = "baseservice/tmall/syncInfo";

        public static final String SERVER_URL_GETALLAPPOINTMENT = "colorfulbox/common/getAllAppointment";
        public static final String SERVER_URL_GETPAYDATA = "colorfulbox/common/getPayData";
        public static final String SERVER_URL_DELETEAPPOINTMENTRECORD = "colorfulbox/common/deleteAppointmentRecord";
        public static final String SERVER_URL_GETGYMLIST = "colorfulbox/gym/getGymList";
        public static final String SERVER_URL_GETGYMPROJECTLIST = "colorfulbox/gym/getGymProjectList";
        public static final String SERVER_URL_GETHHISTORICALAPPOINTMENTLIST = "colorfulbox/gym/gethHistoricalAppointmentList";
        public static final String SERVER_URL_ADDAPPOINTMENT = "colorfulbox/gym/addAppointment";
        public static final String SERVER_URL_GETAPPOINTMENTLISTRECORD = "colorfulbox/gym/getAppointmentListRecord";
        public static final String SERVER_URL_GETCHILDRENROOMPROJECTLIST = "colorfulbox/childrenroom/getChildrenRoomProjectList";
        public static final String SERVER_URL_ADDCHILDRENROOMAPPOINTMENT = "colorfulbox/childrenroom/addChildrenRoomAppointment";
        public static final String SERVER_URL_GETCHILDRENROOMHISTORICALAPPOINTMENTLIST = "colorfulbox/childrenroom/getChildrenRoomHistoricalAppointmentList";
        public static final String SERVER_URL_GETCHILDRENROOMAPPOINTMENTLISTRECORD = "colorfulbox/childrenroom/getChildrenRoomAppointmentListRecord";
        public static final String SERVER_URL_CREATECOLORFULBOXORDER = "colorfulbox/aliPay/createColorfulBoxOrder";
        public static final String SERVER_URL_GETKITCHENROOMLIST = "colorfulbox/kitchen/getKitchenRoomList";
        public static final String SERVER_URL_GETKITCHENROOMPROJECTLIST = "colorfulbox/kitchen/getKitchenRoomProjectList";
        public static final String SERVER_URL_ADDKITCHENROOMAPPOINTMENT = "colorfulbox/kitchen/addKitchenRoomAppointment";
        public static final String SERVER_URL_GETKITCHENROOMHISTORICALAPPOINTMENTLIST = "colorfulbox/kitchen/getKitchenRoomHistoricalAppointmentList";
        public static final String SERVER_URL_GETKITCHENROOMAPPOINTMENTLISTRECORD = "colorfulbox/kitchen/getKitchenRoomAppointmentListRecord";
        public static final String SERVER_URL_FINDCALORIERANKLIST = "colorfulbox/calorie/findCalorieRankList";
        public static final String SERVER_URL_UPDATEAGREE = "colorfulbox/calorie/updateAgree";

        public static final String SERVER_URL_GETPARKLOTLIST = "pass/carScene/getParkLotList";
        public static final String SERVER_URL_GETDEVICELIST = "smarthouse/scene/getDeviceList";
        public static final String SERVER_URL_GETUSERLICENCELIST = "pass/carScene/getUserLicenceList";
        public static final String SERVER_URL_FINDCARPARKRECORD = "pass/access/findCarParkRecord";
        public static final String SERVER_URL_FINDPARKINGDATA = "pass/park/findParkingData";
        public static final String SERVER_URL_FINDSPACE = "pass/park/findSpace";
        public static final String SERVER_URL_FINDVEHICLEBYLICENCE = "pass/park/findVehicleByLicence";
        public static final String SERVER_URL_DELMYCAR = "pass/park/delMyCar";
        public static final String SERVER_URL_GETLICENCEINFO = "pass/park/getLicenceInfo";
        public static final String SERVER_URL_FINDMYLICENCE = "pass/park/findMyLicence";

        public static final String SERVER_URL_GETTALKBACKRECORD = "manpass/talk/getTalkbackRecord";
        public static final String SERVER_URL_SETREFUSEANSWER = "manpass/talk/setRefuseAnswer";
        public static final String SERVER_URL_ADDVISITORRESERVATION = "manpass/visitor/addVisitorReservation";
        public static final String SERVER_URL_GETVISITORRESERVATION = "manpass/visitor/getVisitorReservation";
        public static final String SERVER_URL_GETVISITORRESERVATIONQRCODE = "manpass/visitor/getVisitorReservationQRCode";
        public static final String SERVER_URL_GETALLVISITORRESERVATIONLIST = "manpass/visitor/getAllVisitorReservationList";
        public static final String SERVER_URL_CREATEQRCODE = "manpass/guard/createQrcode";
        public static final String SERVER_URL_GETFACE = "manpass/face/getFace";
        public static final String SERVER_URL_ADDFACE = "manpass/face/addFace";
        public static final String SERVER_URL_DELETEFACE = "manpass/face/deleteFace";
        public static final String SERVER_URL_ADDUSERFACEVILLAGE = "manpass/face/addUserFaceVillage";
        public static final String SERVER_URL_FACEVILLAGELIST = "manpass/face/faceVillageList";
        public static final String SERVER_URL_QUERYVISITORRECORD = "manpass/face/queryVisitorRecord";
        public static final String SERVER_URL_LIST = "manpass/sip/list";
        public static final String SERVER_URL_OPEN = "manpass/sip/open";
        public static final String SERVER_URL_CLOSE = "manpass/sip/close";
        public static final String SERVER_URL_SHOWNAME = "manpass/sip/find/showName";
        public static final String SERVER_URL_LOG = "manpass/sip/app/log";
        public static final String SERVER_URL_CARPASSLIST = "carpass/app/car/user/list";
        public static final String SERVER_URL_PASSLOGS = "carpass/app/park/pass/logs";
        public static final String SERVER_URL_ADDCAR = "carpass/app/add/car";
        public static final String SERVER_URL_PARKINGLIST = "carpass/app/parking/list";
        public static final String SERVER_URL_ADDVISIT = "carpass/app/add/visit";
        public static final String SERVER_URL_UNIFORMLOGIN = "login/app/uniformLogin";

        public static final String SERVER_URL_GETPUBLICCAMERA = "aibox/face/getPublicCamera";
        public static final String SERVER_URL_QUERYLIVESTREAMING = "aibox/visual/queryLiveStreaming";
        public static final String SERVER_URL_FINDCARINGRECORD = "aibox/care/findCaringRecord";
        public static final String SERVER_URL_SETMONITORING = "aibox/care/setMonitoring";
        public static final String SERVER_URL_FINDLASTCARINGRECORDBYPERSONCODE = "aibox/care/findLastCaringRecordByPersonCode";
        public static final String SERVER_URL_FINDOUTALONERECORD = "aibox/care/findOutAloneRecord";

        public static final String SERVER_URL_ADDMYCAR = "pass/park/addMyCar";
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

        public static final String SERVER_URL_FINDALARMRECORD = "messagepush/record/findAlarmRecord";
        public static final String SERVER_URL_FINDSMARTDOORALARMRECORD = "messagepush/record/findSmartDoorAlarmRecord";
        public static final String SERVER_URL_JUDGETMALLONLINE = "messagepush/message/judgeTmallOnline";
        public static final String SERVER_URL_QUERYUSERFACERECORD = "messagepush/face/queryUserFaceRecord";

        public static final String SERVER_URL_CREATEODERAPP = "sharedspace/sharedSpaceAppointmentOrderController/createOderApp";
        public static final String SERVER_URL_FINDUSERBYPHONE = "sharedspace/sharedSpaceAppointmentOrderController/findUserByPhone";
        public static final String SERVER_URL_FINDAPPOINTMENTTIME = "sharedspace/sharedSpaceAppointmentOrderController/findAppointmentTime";
        public static final String SERVER_URL_FINDAPPOINTMENTSTATUS = "sharedspace/sharedSpaceAppointmentOrderController/findAppointmentStatus";
        public static final String SERVER_URL_FINDAPPOINTMENTSTATUSWUYE = "sharedspace/sharedSpaceAppointmentOrderController/findAppointmentStatusWuye";
        public static final String SERVER_URL_SETAPPOINTMENTSTATUSAPP = "sharedspace/sharedSpaceAppointmentOrderController/setAppointmentStatusApp";
        public static final String SERVER_URL_FINDSHAREDSPACEAPP = "sharedspace/sharedSpaceAppointmentOrderController/findSharedSpaceApp";
        public static final String SERVER_URL_FINDPROJECTRELATIONAPP = "sharedspace/sharedSpaceAppointmentOrderController/findProjectRelationApp";
    }

    public static class FaceConfig {
        // 为了apiKey,secretKey为您调用百度人脸在线接口的，如注册，识别等。
        // 为了的安全，建议放在您的服务端，端把人脸传给服务器，在服务端端进行人脸注册、识别放在示例里面是为了您快速看到效果
//        public static String apiKey = "HGIgFXpMWWccIo34d9Y9q1C1";//替换为你的apiKey(ak);
//        public static String secretKey = "2iMxeBjG6tZI8Npm24NS2u5Y7QPCcMd0";//替换为你的secretKey(sk);
//        public static String licenseID = "faceinputforcifi-face-android";
//        public static String licenseID = "yongshenghuoceshi-face-android";
        public static String licenseID = "longguangsmart-face-android";
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
        public static final String CAMERA_XIAOMIYAN = "a19GfYdH3vb";
        public static final String CAMERA_IVP = "a1CiCR1ot9F";
        public static final String ESP_TOUCH = "a1BTXvSu3s9";
//		public static final int FACE_3D = 2;
//		public static final int FACE_AT = 3;
    }
}