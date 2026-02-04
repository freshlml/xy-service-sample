package com.fresh.xy.sample.controller.scan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fresh.core.enums.JsonResultEnum;
import com.fresh.core.result.JsonResult;
import com.fresh.core.result.PageJsonResultVo;
import com.fresh.core.utils.AssertUtils;
import com.fresh.xy.common.dto.PageDto;
import com.fresh.xy.common.enums.ScanTypeEnum;
import com.fresh.xy.mbp.utils.MybatisPlusPageUtils;
import com.fresh.xy.sample.dto.scan.SampleScanAddDto;
import com.fresh.xy.sample.dto.scan.SampleScanDelDto;
import com.fresh.xy.sample.dto.scan.SampleScanSelDto;
import com.fresh.xy.sample.dto.scan.SampleScanUpdateDto;
import com.fresh.xy.sample.entity.scan.SampleScan;
import com.fresh.xy.sample.service.scan.SampleScanService;
import com.fresh.xy.sample.vo.scan.SampleScanVo;
import com.fresh.xy.sample2.api.Sample2ServiceApi;
import com.fresh.xy.sample2.api.bo.Sample2ScanBo;
import com.fresh.xy.sample2.api.bo.Sample2ScanPageBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("sampleScan")
public class SampleScanController {
    
    @Autowired
    private SampleScanService sampleScanService;
    @Autowired
    private Sample2ServiceApi sample2ServiceApi;

    @PostMapping("save")
    public JsonResult<?> save(@RequestBody @Valid SampleScanAddDto scanDto) {
        SampleScan scan = SampleScan.builder().build().setName(scanDto.getName()).setScanType(scanDto.getScanType()).setScanTime(scanDto.getScanTime());
        sampleScanService.save(scan);
        return JsonResult.buildSuccessResult("保存成功");
    }

    @PostMapping("del")
    public JsonResult<?> del(@RequestBody @Valid SampleScanDelDto scan) {
        sampleScanService.removeById(scan.getId());
        return JsonResult.buildSuccessResult("删除成功");
    }

    @PostMapping("update")
    public JsonResult<?> update(@RequestBody @Valid SampleScanUpdateDto scanDto) {
        SampleScan scan = SampleScan.builder().build().setName(scanDto.getName()).setScanType(scanDto.getScanType()).setScanTime(scanDto.getScanTime());
        scan.setId(scanDto.getId());
        sampleScanService.updateById(scan);
        return JsonResult.buildSuccessResult("修改成功");
    }

    @GetMapping("getById")
    public JsonResult<SampleScanVo> getById(Long id) {
        //AssertUtils.notNull(id, () -> "id不能为空", () -> JsonResultEnum.FAIL.getCode());
        SampleScan scan = sampleScanService.getById(id);
        SampleScanVo scanVo = null;
        if(scan != null) {
            scanVo = SampleScanVo.builder().id(scan.getId()).name(scan.getName()).scanType(scan.getScanType()).scanTime(scan.getScanTime()).createTime(scan.getCreateTime()).modifyTime(scan.getModifyTime()).build();
        }
        JsonResult<SampleScanVo> result = JsonResult.buildSuccessResult(scanVo);

        return result;
    }

    @GetMapping("getByIds")
    public JsonResult<List<SampleScanVo>> getByIds(@RequestParam(name = "ids", required = false) List<Long> ids) {
        List<SampleScanVo> result = null;
        if(ids != null && !ids.isEmpty()) {
            List<SampleScan> scanResult = sampleScanService.list(new QueryWrapper<SampleScan>().lambda().select(SampleScan::getId, SampleScan::getName, SampleScan::getScanTime, SampleScan::getScanType).in(SampleScan::getId, ids));
            result = scanResult.stream().map(scan -> SampleScanVo.builder().id(scan.getId()).name(scan.getName()).scanType(scan.getScanType()).scanTime(scan.getScanTime()).createTime(scan.getCreateTime()).modifyTime(scan.getModifyTime()).build()).collect(Collectors.toList());
        }
        return JsonResult.buildSuccessResult(result);
    }

    @GetMapping("listByParams")
    public JsonResult<PageJsonResultVo<SampleScanVo>> listByParams(@RequestParam(name = "id", required = false) Long id,
                                   @RequestParam(name = "scanType", required = false) ScanTypeEnum scanType, //ScanTypeEnum.valueOf("SYSTEM")，根据name(而不是ScanTypeEnum.value)获取实例
                                   @RequestParam(name = "scanTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime scanTime,
                                   @RequestParam(name = "page", required = false) Long page,
                                   @RequestParam(name = "pageSize", required = false) Long pageSize) {

        SampleScanSelDto scanSelDto = SampleScanSelDto.builder().id(id).scanType(scanType).scanTime(scanTime).build();
        scanSelDto.setPage(page);
        scanSelDto.setPageSize(pageSize);
        IPage<SampleScanVo> result = sampleScanService.listByPojo(scanSelDto);

        return JsonResult.buildSuccessResult(MybatisPlusPageUtils.pageJsonResultVo(result));
    }

    @GetMapping("listByPojo")
    public JsonResult<PageJsonResultVo<SampleScanVo>> listByPojo(SampleScanSelDto scanSelDto) { //scanSelDto中scanType字段: ScanTypeEnum.valueOf("SYSTEM")，根据name(而不是ScanTypeEnum.value)获取实例
        AssertUtils.notNull(scanSelDto, () -> "查询参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(scanSelDto.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());

        IPage<SampleScanVo> result = sampleScanService.listByPojo(scanSelDto);

        return JsonResult.buildSuccessResult(MybatisPlusPageUtils.pageJsonResultVo(result));
    }

    //Get请求中，将参数放在请求体中传递
    @GetMapping("listByPojo2")
    public JsonResult<PageJsonResultVo<SampleScanVo>> listByPojo2(@RequestBody SampleScanSelDto scanSelDto) {
        AssertUtils.notNull(scanSelDto, () -> "查询参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(scanSelDto.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());
        IPage<SampleScanVo> result = sampleScanService.listByPojo(scanSelDto);

        return JsonResult.buildSuccessResult(MybatisPlusPageUtils.pageJsonResultVo(result));
    }


    @GetMapping("list")
    public JsonResult<PageJsonResultVo<SampleScan>> list(PageDto pageDto) {
        AssertUtils.notNull(pageDto, () -> "分页参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(pageDto.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());

        IPage<SampleScan> pageParam = MybatisPlusPageUtils.mybatisPlusPage(pageDto);
        IPage<SampleScan> result = sampleScanService.page(pageParam);

        return JsonResult.buildSuccessResult(MybatisPlusPageUtils.pageJsonResultVo(result));
    }

    @GetMapping("listAll")
    public JsonResult<List<SampleScanVo>> listAll() {
        List<SampleScan> scanResult = sampleScanService.list();
        List<SampleScanVo> result = scanResult.stream().map(scan -> SampleScanVo.builder().id(scan.getId()).name(scan.getName()).scanType(scan.getScanType()).scanTime(scan.getScanTime()).createTime(scan.getCreateTime()).modifyTime(scan.getModifyTime()).build()).collect(Collectors.toList());

        return JsonResult.buildSuccessResult(result);
    }



    /*rpc调用 service-sample2*/

    @GetMapping("getByIdFromSample2")
    public JsonResult<Sample2ScanBo> getByIdFromSample2(Long id) {
        JsonResult<Sample2ScanBo> result = sample2ServiceApi.getById(id);
        return result;
    }
    @GetMapping("getByIdsFromSample2")
    public JsonResult<List<Sample2ScanBo>> getByIdsFromSample2(@RequestParam(name = "ids", required = false) List<Long> ids) {
        JsonResult<List<Sample2ScanBo>> result = sample2ServiceApi.getByIds(ids);
        return result;
    }

    //Get请求的Enum中@JsonCreator
    @GetMapping("listByPojoFromSample2")
    public JsonResult<PageJsonResultVo<Sample2ScanBo>> listByPojoFromSample2(SampleScanSelDto scanSelDto) {
        AssertUtils.notNull(scanSelDto, () -> "查询参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(scanSelDto.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());

        Sample2ScanPageBo scanPageBo = Sample2ScanPageBo.builder().id(scanSelDto.getId()).name(scanSelDto.getName()).scanType(scanSelDto.getScanType()).scanTime(scanSelDto.getScanTime()).build();
        JsonResult<PageJsonResultVo<Sample2ScanBo>> result = sample2ServiceApi.listByPojo(scanPageBo);
        return result;
    }

    //Get请求的Enum中@JsonCreator
    @GetMapping("listByParamsFromSample2")
    public JsonResult<PageJsonResultVo<Sample2ScanBo>> listByParamsFromSample2(@RequestParam(name = "id", required = false) Long id,
                                   @RequestParam(name = "scanType", required = false) ScanTypeEnum scanType,  //String convert to Enum, 使用默认的StringCovertToEnum
                                   @RequestParam(name = "scanTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime scanTime, //String covert to LocalDateTime，String转化为LocalDateTime，默认的String格式是iso格式
                                   @RequestParam(name = "page", required = false) Long page,
                                   @RequestParam(name = "pageSize", required = false) Long pageSize) {

        JsonResult<PageJsonResultVo<Sample2ScanBo>> result = sample2ServiceApi.listByParams(id, scanType, scanTime, page, pageSize);
        return result;
    }


    //compare with listByPojo, Get请求将参数放在请求体，使用@RequestBody解析请求体参数
    @GetMapping("listByPojo2FromSample2")
    public JsonResult<PageJsonResultVo<Sample2ScanBo>> listByPojo2FromSample2(@RequestBody Sample2ScanPageBo scanPageBo) {
        AssertUtils.notNull(scanPageBo, () -> "查询参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(scanPageBo.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());

        JsonResult<PageJsonResultVo<Sample2ScanBo>> result = sample2ServiceApi.listByPojo2(scanPageBo);
        return result;
    }

    @PostMapping("saveSample2")
    public JsonResult<?> saveRpc(@RequestBody @Valid SampleScanAddDto scanAddDto) {
        try {
            sampleScanService.saveAndRpc(scanAddDto);
        } catch(Exception e) {
            log.error(e.getMessage());
            return JsonResult.buildFailedResult("保存失败");
        }
        return JsonResult.buildSuccessResult("保存成功");
    }

}
