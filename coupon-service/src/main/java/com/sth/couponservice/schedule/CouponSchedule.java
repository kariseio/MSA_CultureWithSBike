package com.sth.couponservice.schedule;

import com.sth.couponservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponSchedule {
    private final CouponService couponService;
    private final Environment env;

    @Scheduled(fixedDelay = 300000)
    public void hello() {
        log.info("fixedRate Scheduler");
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 12시
    public void CreateCouponSchedule() {
        log.info("Create Coupon Schedule");
//        if(!env.getProperty("server.port").equals("8600")) {
//            return;
//        }
        String couponName = "따릉이 1시간 이용권("+ LocalDate.now().getMonth()+"/"+LocalDate.now().getDayOfMonth() +")";
        int quantity = 1000;

        couponService.createCoupon(couponName, quantity);
    }
}
