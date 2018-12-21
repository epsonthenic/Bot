package com.spt.engine.service;

import com.spt.engine.entity.AppUserData;

import java.util.List;

public interface AppUserDataService {

    List<AppUserData> getAllAppUserData();

    AppUserData findAppUserDataByID(Long id);

    void DeletrByID(Long id);

    void Savebyclass(AppUserData appUserData);

    void Savebyjson(String json);

    AppUserData Putsavebyclass(AppUserData appUserData, Long id);

    List<AppUserData> Relee(String firstName);

    List<AppUserData> ReleelastName(String lastName);

    List<AppUserData> searchdeseription(String deseription);

    List<AppUserData> searchfandl(String firstName, String lastName);


}
