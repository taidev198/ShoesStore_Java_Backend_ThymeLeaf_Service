package com.taidev198.service;

import com.taidev198.model.Notification;

public interface NotificationService {

    public void sendNotify(int userId, Notification notification);
}
