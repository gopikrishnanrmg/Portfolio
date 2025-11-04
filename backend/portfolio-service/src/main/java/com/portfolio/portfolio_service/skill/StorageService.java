package com.portfolio.portfolio_service.skill;

public interface StorageService {
    String upload(byte[] data);
    byte[] download(String url);
}
