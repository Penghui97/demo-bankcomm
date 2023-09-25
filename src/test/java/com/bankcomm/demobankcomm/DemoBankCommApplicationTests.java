package com.bankcomm.demobankcomm;

import com.bankcomm.demobankcomm.entity.OldSign;
import com.bankcomm.demobankcomm.service.IOldSignService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@SpringBootTest
class DemoBankCommApplicationTests {
    @Resource
    private IOldSignService oldSignService;
    private final Random random = new Random();
    @Test
    @Transactional
    @Rollback(value = false)
    // 添加老假数据
    void addOld() {
        // Jan
        randomOldSign("01", 31);
        // Feb
        randomOldSign("02", 28);
        randomOldSign("03", 31);
        randomOldSign("04", 30);
        randomOldSign("05", 31);
        randomOldSign("06", 30);
        randomOldSign("07", 31);
        randomOldSign("08", 31);
    }

    @Test
    void queryOld() {
        List<OldSign> signedList = oldSignService.getSignedList("123:2023-01");
        log.info(signedList.toString());
    }

    private void randomOldSign(String month, int days) {
        List<OldSign> list = new ArrayList<>();
        String key = "123:2023-" + month + "-";
        for (int i = 1; i < days + 1; i++) {
            OldSign oldSign = new OldSign();
            if (i < 10) {
                oldSign.setId(key + "0" + i);
            } else {
                oldSign.setId(key + i);
            }
            oldSign.setSigned(random.nextInt(2));
            list.add(oldSign);
        }
        oldSignService.saveBatch(list);
    }

}
