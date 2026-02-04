package com.fresh.xy.sample.controller.seata;

import com.fresh.core.result.JsonResult;
import com.fresh.xy.sample.dto.scan.SampleScanAddDto;
import com.fresh.xy.sample.service.seata.SeataTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("seataTest")
public class SeataTestController {

    @Autowired
    private SeataTestService seataTestService;

    @PostMapping("test1")
    public JsonResult<?> test1(@RequestBody @Valid SampleScanAddDto scanAddDto) {
        try {
            seataTestService.seataTxTest(scanAddDto);
        } catch(Exception e) {
            log.error(e.getMessage());
            return JsonResult.buildFailedResult("保存失败");
        }
        return JsonResult.buildSuccessResult("保存成功");
    }

    @PostMapping("test2")
    public JsonResult<?> test2(@RequestBody @Valid SampleScanAddDto scanAddDto) {
        try {
            seataTestService.seataTxTest2(scanAddDto);
        } catch(Exception e) {
            log.error(e.getMessage());
            return JsonResult.buildFailedResult("保存失败");
        }
        return JsonResult.buildSuccessResult("保存成功");
    }

    @PostMapping("test3")
    public JsonResult<?> test3(@RequestBody @Valid SampleScanAddDto scanAddDto) {
        try {
            seataTestService.seataTxTest3(scanAddDto);
        } catch(Exception e) {
            log.error(e.getMessage());
            return JsonResult.buildFailedResult("保存失败");
        }
        return JsonResult.buildSuccessResult("保存成功");
    }

    @PostMapping("test4_1")
    public JsonResult<?> test4_1() {
        try {
            seataTestService.seataTxTest4_1();
        } catch(Exception e) {
            log.error(e.getMessage());
            return JsonResult.buildFailedResult("操作失败");
        }
        return JsonResult.buildSuccessResult("操作成功");
    }

    @PostMapping("test4_2")
    public JsonResult<?> test4_2() {
        try {
            seataTestService.seataTxTest4_2();
        } catch(Exception e) {
            log.error(e.getMessage());
            return JsonResult.buildFailedResult("操作失败");
        }
        return JsonResult.buildSuccessResult("操作成功");
    }


}
