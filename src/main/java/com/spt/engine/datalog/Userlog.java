package com.spt.engine.datalog;

import com.spt.engine.entity.AppUserData;
import com.spt.engine.repository.AppUserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Userlog implements CommandLineRunner {

    @Autowired
    private AppUserDataRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        AppUserData use1 = new AppUserData();
        AppUserData use2 = new AppUserData();

        use1.setFirstName("หกฟหกฟหก");
        use1.setLastName("ฟหกฟหกฟห");
        use1.setDeseription("nn");
        use2.setFirstName("Uotsa");
        use2.setLastName("Kotcha");
        use2.setDeseription("uu");
        userRepository.save(use1);
        userRepository.save(use2);

    }
}
