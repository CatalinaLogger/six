package com.maybe.work.service;

public interface IWorkDraftService {
    String select(String defineId);

    void insert(String defineId, String jsonData);

    void delete(String defineId);
}
