package com.ceylontrail.backend_server.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    private int totalUsers;
    private int totalTravellers;
    private int recentUsers;
    private int totalBusinessProfiles;
    private int recentBusinessProfiles;
    private int totalReports;
    private int recentReports;
    private double totalRevenue;
    private double recentRevenue;
    private ChartData subscriptionChartData;
    private List<SP> pendingBusinessProfiles;
    private List<Subscriber> recentSubscribers;

}
