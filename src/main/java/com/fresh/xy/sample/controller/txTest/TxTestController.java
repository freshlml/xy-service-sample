package com.fresh.xy.sample.controller.txTest;

import com.fresh.core.result.JsonResult;
import com.fresh.xy.sample.service.txTest.TxTestOneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("txTest")
public class TxTestController {

    @Autowired
    private TxTestOneService txTestOneService;


    @PostMapping("testRequired")
    public JsonResult<?> test_required() {
        txTestOneService.test_required();
        return JsonResult.buildSuccessResult("nothing");
    }

    @PostMapping("testRequiredE")
    public JsonResult<?> test_required_e() {
        txTestOneService.test_required_e();
        return JsonResult.buildSuccessResult("nothing");
    }

    @PostMapping("testRequiredNew")
    public JsonResult<?> test_requiredNew() {
        txTestOneService.test_requiredNew();
        return JsonResult.buildSuccessResult("nothing");
    }

    @PostMapping("testRequiredNewE")
    public JsonResult<?> test_requiredNew_e() {
        txTestOneService.test_requiredNew_e();
        return JsonResult.buildSuccessResult("nothing");
    }

    @PostMapping("testNested")
    public JsonResult<?> test_nested() {
        txTestOneService.test_nested();
        return JsonResult.buildSuccessResult("nothing");
    }

    @PostMapping("testNestedE")
    public JsonResult<?> test_nested_e() {
        txTestOneService.test_nested_e();
        return JsonResult.buildSuccessResult("nothing");
    }


    @PostMapping("testInline")
    public JsonResult<?> testInline() {
        txTestOneService.test_inline();
        return JsonResult.buildSuccessResult("nothing");
    }


}
