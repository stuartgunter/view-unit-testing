package org.stuartgunter.rendering.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class SomeController {

    @Resource
    private SomeComponent someComponent;

    @Resource
    private SomeService someService;

    @RequestMapping("/page")
    public String page(Model model) {

        model.addAttribute("component", someComponent);
        model.addAttribute("item", someService.getData());

        return "page";
    }
}
