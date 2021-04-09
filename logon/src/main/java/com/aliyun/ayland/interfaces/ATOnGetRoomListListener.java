package com.aliyun.ayland.interfaces;

import com.aliyun.ayland.data.ATRoom;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;

import java.util.List;

/**
 * The interface On get room list listener.
 *
 * @author sinyuk
 * @date 2018 /6/25
 */
public interface ATOnGetRoomListListener {
    /**
     * On succeed.
     *
     * @param total the total
     * @param ATRooms the ATRooms
     */
    void onSucceed(int total, List<ATRoom> ATRooms);


    /**
     * On failed.
     *
     * @param e the e
     */
    void onFailed(Exception e);

    /**
     * On failed.
     *
     * @param response the response
     */
    void onFailed(IoTResponse response);

}