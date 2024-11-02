package com.taidev198.service;

import java.util.Optional;

public interface ChatRomService {

    public Optional<String> getChatRoomId(
        Integer senderId,
        Integer receiverId,
        boolean createNewIfNotExisted
    );
}
