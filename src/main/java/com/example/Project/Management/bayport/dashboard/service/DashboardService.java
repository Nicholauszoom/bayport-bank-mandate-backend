package com.example.Project.Management.bayport.dashboard.service;

import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.service.UserService;
import com.example.Project.Management.bayport.crdb.entity.DirectDebitMandate;
import com.example.Project.Management.bayport.crdb.enumerated.MandateRequestType;
import com.example.Project.Management.bayport.crdb.service.CrdbDirectMandateService;
import com.example.Project.Management.bayport.dashboard.dto.DashboardDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DashboardService {
    @Autowired
    private UserService userService;

    private final CrdbDirectMandateService crdbDirectMandateService;

    public DashboardService(CrdbDirectMandateService crdbDirectMandateService) {
        this.crdbDirectMandateService = crdbDirectMandateService;
    }


    public DashboardDto createDashboard() {
            List<User> userList = userService.findAll();

            int users = userList.size();

//            DashboardDto dashboardDto = new DashboardDto();
//            dashboardDto.setUsers(users);

//            return dashboardDto;
        return null;

    }

    public DashboardDto statistics() {
        List<User> userList = userService.findAll();
        if (userList.size() == 0) {
            return null;
        }

        List<DirectDebitMandate> mandates = crdbDirectMandateService.findAll();
        List<DirectDebitMandate> successMandates = crdbDirectMandateService.findDebitMandateByRequestType(MandateRequestType.SUBMITTED);
        List<DirectDebitMandate> failMandates = crdbDirectMandateService.findDebitMandateByRequestType(MandateRequestType.FAILED);

        DashboardDto statistics = DashboardDto.builder()
                .totalUsers(userList.size())
                .totalMandates(mandates.size())
                .successMandates(successMandates.size())
                .failedMandates(failMandates.size())
                .build();

        log.info("Statistics {}", statistics);
        return statistics;
    }

}
