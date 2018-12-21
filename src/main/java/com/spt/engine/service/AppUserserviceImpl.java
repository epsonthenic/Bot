package com.spt.engine.service;

import com.google.gson.Gson;
import com.spt.engine.entity.AppUserData;
import com.spt.engine.repository.AppUserDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserserviceImpl implements AppUserDataService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppUserDataRepository appUserDataRepository;

    @Override
    public List<AppUserData> getAllAppUserData() {
        return appUserDataRepository.findAll();
    }

    @Override
    public AppUserData findAppUserDataByID(Long id) {
        return appUserDataRepository.findById(id).get();
    }

    @Override
    public void DeletrByID(Long id) {
        appUserDataRepository.deleteById(id);
        LOGGER.info("Delete Success");
    }

    @Override
    public void Savebyclass(AppUserData appUserData) {
        appUserDataRepository.save(appUserData);
        LOGGER.info("Save By Class : {}",appUserData.getId());
    }

    @Override
    public void Savebyjson(String json) {
        LOGGER.info("{}",json);
        Gson gson = new Gson();
        AppUserData appUserData = gson.fromJson(json, AppUserData.class);
        appUserDataRepository.save(appUserData);
        LOGGER.info("Save By Json : {}",appUserData.getId());
    }


    @Override
    public AppUserData Putsavebyclass(AppUserData appUserData, Long id) {
        appUserData.setId(id);
        appUserDataRepository.save(appUserData);
        LOGGER.info("Put By Class : {}", appUserData.getId());
        return appUserDataRepository.findById(id).get();

    }

    @Override
    public List<AppUserData> Relee(String firstName) {
        return appUserDataRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    @Override
    public List<AppUserData> ReleelastName(String lastName) {
        return appUserDataRepository.findByLastNameIsContaining(lastName);
    }

    @Override
    public List<AppUserData> searchdeseription(String deseription) {
        return appUserDataRepository.searchdeseription(deseription);
    }

    @Override
    public List<AppUserData> searchfandl(String firstName, String lastName) {
        return appUserDataRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName,lastName);
    }
}
