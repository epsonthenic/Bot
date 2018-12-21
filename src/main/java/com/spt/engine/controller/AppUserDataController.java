package com.spt.engine.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spt.engine.entity.AppUserData;
import com.spt.engine.service.AppUserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/appUserDataCustom")
public class AppUserDataController {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppUserDataService appUserDataService;

    @GetMapping("/getAppUserData")
    public List<AppUserData> getAppUserData(){
        return appUserDataService.getAllAppUserData();
    }

    @GetMapping("/findByID")
    public AppUserData findAppByIDByRequestParam(@RequestParam("id") Long id){
        return appUserDataService.findAppUserDataByID(id);
    }

    @GetMapping("/{id}")
    public AppUserData findAppByIDByParam(@PathVariable("id") Long id){
        return appUserDataService.findAppUserDataByID(id);
    }

    @DeleteMapping("/del/{id}")
    public void DelAppData(@PathVariable("id") Long id){
        appUserDataService.DeletrByID(id);
    }

    @PostMapping("/savebyclass")
    public void Savebyclass(@RequestBody AppUserData appUserData){
        appUserDataService.Savebyclass(appUserData);

    }

    @PostMapping("/savebyjson")
    public void Savebyjson(@RequestBody String json){
        appUserDataService.Savebyjson(json);
    }

    @PutMapping("/Putbyclass/{id}")
    public AppUserData Putsavebyclass(@RequestBody AppUserData appUserData , @PathVariable("id") Long id){
        return appUserDataService.Putsavebyclass(appUserData,id);
    }
    //produces

    @GetMapping(value = "/firstName")
    public List<AppUserData> Relee (@RequestParam("firstName")String firstName) throws UnsupportedEncodingException {
        firstName = "ห";
        //firstName = URLDecoder.decode(firstName, StandardCharsets.UTF_8.name());
        LOGGER.info("firstName : {}",firstName);
        LOGGER.info("appUserDataService {}",appUserDataService.Relee(firstName));
        return appUserDataService.Relee(firstName);
    }

    @GetMapping("/lastName")
    public List<AppUserData> ReleelastName (@RequestParam("lastName")String lastName){
        return appUserDataService.ReleelastName(lastName);
    }

    @GetMapping("/deseription")
    public List<AppUserData> searchdeseription (@RequestParam("deseription")String deseription){
        return appUserDataService.searchdeseription(deseription);
    }

    @GetMapping("/fandl")
    public String searchfandl (@RequestParam("firstName")String firstName, @RequestParam("lastName")String lastName)
    {
        appUserDataService.searchfandl(firstName,lastName);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(appUserDataService.searchfandl(firstName,lastName));
    }

}
