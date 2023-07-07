package com.honda.olympus.ms.statusgm_hdm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honda.olympus.ms.statusgm_hdm.service.StatusGmHdmService;


@RestController
public class StatusGmHdmController {

    @Autowired
    private StatusGmHdmService statusGmHdmService;


    @GetMapping(path = "/launch-process", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> launchProcess() {

        statusGmHdmService.launchProcess();
        return new ResponseEntity<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK);
    }
}
