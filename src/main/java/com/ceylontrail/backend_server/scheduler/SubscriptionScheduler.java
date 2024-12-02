package com.ceylontrail.backend_server.scheduler;

import com.ceylontrail.backend_server.service.SPService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SubscriptionScheduler {

    private final SPService spService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkExpiredSubscriptions() {
        spService.handleExpiredSubscriptions();
    }

}
