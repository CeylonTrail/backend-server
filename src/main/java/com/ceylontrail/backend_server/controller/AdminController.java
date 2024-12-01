package com.ceylontrail.backend_server.controller;

import com.ceylontrail.backend_server.dto.subscription.AddSubscriptionDTO;
import com.ceylontrail.backend_server.dto.subscription.EditSubscriptionDTO;
import com.ceylontrail.backend_server.service.AdminService;
import com.ceylontrail.backend_server.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public StandardResponse getDashboard() {
        return adminService.getDashboard();
    }

    @GetMapping("/user/traveller")
    public StandardResponse getTravellers(
            @RequestParam(defaultValue = "") String key,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {
        return adminService.getTravellers(key, pageNumber, pageSize);
    }

    @GetMapping("/user/sp")
    public StandardResponse getSPs(
            @RequestParam(defaultValue = "") String key,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {
        return this.adminService.getSPs(key, pageNumber, pageSize);
    }

    @GetMapping("/user/traveller/{userId}")
    public StandardResponse getTraveller(@PathVariable int userId) {
        return this.adminService.getTraveller(userId);
    }

    @DeleteMapping("/user/traveller/{userId}")
    public StandardResponse deleteTraveller(@PathVariable int userId) {
        return this.adminService.deleteTraveller(userId);
    }

    @GetMapping("/user/sp/{userId}")
    public StandardResponse getSP(@PathVariable int userId) {
        return this.adminService.getSP(userId);
    }

    @DeleteMapping("/user/sp/{userId}")
    public StandardResponse deleteSP(@PathVariable int userId) {
        return this.adminService.deleteSP(userId);
    }

    @DeleteMapping("/post/{postId}")
    public StandardResponse deletePost(@PathVariable Long postId) {
        return this.adminService.deletePost(postId);
    }

    @PostMapping("/subscription")
    public StandardResponse addSubscription(@RequestBody AddSubscriptionDTO subscriptionDTO) {
        return this.adminService.addSubscription(subscriptionDTO);
    }

    @GetMapping("/subscription/{subscriptionId}")
    public StandardResponse getSubscription(@PathVariable Long subscriptionId) {
        return this.adminService.getSubscription(subscriptionId);
    }

    @PutMapping("/subscription/{subscriptionId}")
    public StandardResponse editSubscription(@PathVariable Long subscriptionId, @RequestBody EditSubscriptionDTO subscriptionDTO) {
        return this.adminService.editSubscription(subscriptionId, subscriptionDTO);
    }

    @DeleteMapping("/subscription/{subscriptionId}")
    public StandardResponse deleteSubscription(@PathVariable Long subscriptionId) {
        return this.adminService.deleteSubscription(subscriptionId);
    }

}
