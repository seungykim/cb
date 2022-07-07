package com.seungkim.cb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlbumController {
    private final AlbumService service;

    @Autowired
    public AlbumController(final AlbumService service) {
        this.service = service;
    }
    
    @GetMapping("/albums")
    public String albums() {
        return service.getAlbumList(false);
    }

    @GetMapping("/albumsWithDelay")
    public String albumsWithDelay(@RequestParam(required = false) Integer waitSecParam) {
        int waitSec = waitSecParam != null ? waitSecParam.intValue() : 0;
        return service.getAlbumList(waitSec);
    }

    @GetMapping("/albumsWithException")
    public String albumsWithException(@RequestParam(required = false) Boolean throwExceptionParam) {
        boolean throwException = throwExceptionParam != null ? throwExceptionParam.booleanValue() : false;
        return service.getAlbumList(throwException);
    }
}
