package com.example.Project.Management.bayport.dashboard.service;

import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.auth.user.service.UserService;
import com.example.Project.Management.bayport.dashboard.dto.DashboardDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    @Autowired
    private UserService userService;


    public DashboardDto createDashboard() {
            List<User> userList = userService.findAll();

            int users = userList.size();

            DashboardDto dashboardDto = new DashboardDto();
            dashboardDto.setUsers(users);

            return dashboardDto;

    }
}
