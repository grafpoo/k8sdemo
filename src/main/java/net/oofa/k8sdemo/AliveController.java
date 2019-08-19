package net.oofa.k8sdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AliveController {

    @GetMapping("/urp")
    public String imAlive() {
        return "i feel so alive";
    }
}
