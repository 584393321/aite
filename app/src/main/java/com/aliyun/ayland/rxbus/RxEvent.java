package com.aliyun.ayland.rxbus;

public class RxEvent {

    public int receiveType;
    public int threadType;
    public int what;
    public Object event;

    public RxEvent() {
    }

    /**
     * RxBus 事件
     * @param receiveType 接收者类型
     * @param threadType 事件类型
     * @param what       事件类型
     * @param event      事件对象
     */
    public RxEvent(@EventType.ReceiveType int receiveType, @EventType.ThreadType int threadType, int what, Object event) {
        this.receiveType = receiveType;
        this.threadType = threadType;
        this.what = what;
        this.event = event;
    }

    public Object getEvent() {
        return event;
    }

}
