package com.rafo.apps.task_openfeign.client;

import com.rafo.apps.task_openfeign.response.ResponseSUNAT;
import com.rafo.apps.task_openfeign.utils.Constants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client-sunat", url = Constants.BASE_URL + "/v2/sunat/ruc/")
public interface ClientSUNAT {

    @GetMapping("/full")
    ResponseSUNAT getDataSUNAT(@RequestParam("numero") String numero,
                                  @RequestHeader("Authorization") String authorization);

}
