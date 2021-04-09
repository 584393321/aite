package com.aliyun.ayland.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ATRxBus {
    private static volatile ATRxBus defaultInstance;

    // 主题
    private final Subject<Object> bus;
    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private ATRxBus() {
        bus = PublishSubject.create().toSerialized();
    }
    // 单例RxBus
    public static ATRxBus getDefault() {
        ATRxBus ATRxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (ATRxBus.class) {
                ATRxBus = defaultInstance;
                if (defaultInstance == null) {
                    ATRxBus = new ATRxBus();
                    defaultInstance = ATRxBus;
                }
            }
        }
        return ATRxBus;
    }
    // 提供了一个新的事件
    public void post (Object o) {
        bus.onNext(o);
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObserverable (Class<T> eventType) {
        return bus.ofType(eventType);
    }
}