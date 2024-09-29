package com.fresh.xy.sample.service.seata.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fresh.common.exception.BizException;
import com.fresh.common.result.JsonResult;
import com.fresh.xy.common.enums.ScanTypeEnum;
import com.fresh.xy.sample.dto.scan.SampleScanAddDto;
import com.fresh.xy.sample.entity.scan.SampleScan;
import com.fresh.xy.sample.service.scan.SampleScanService;
import com.fresh.xy.sample.service.seata.SeataTestService;
import com.fresh.xy.sample2.api.Sample2ServiceApi;
import com.fresh.xy.sample2.api.bo.Sample2ScanAddBo;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SeataTestServiceImpl implements SeataTestService {

    @Autowired
    private SampleScanService sampleScanService;

    @Autowired
    private Sample2ServiceApi sample2ServiceApi;

    @GlobalTransactional
    @Override
    public void seataTxTest(SampleScanAddDto scanAddDto) {
        SampleScan sampleScan = SampleScan.builder().name(scanAddDto.getName()).scanType(scanAddDto.getScanType()).scanTime(scanAddDto.getScanTime()).build();
        sampleScanService.save(sampleScan);
        //同一数据源上两个独立 sql 分为两个本地事务
        sampleScan.setName(sampleScan.getName() + "_____随机名称");
        sampleScan.setId(null);
        sampleScanService.save(sampleScan);

        Sample2ScanAddBo scanAddBo = Sample2ScanAddBo.builder().name(scanAddDto.getName()).scanType(scanAddDto.getScanType()).scanTime(scanAddDto.getScanTime()).build();
        JsonResult<?> result = sample2ServiceApi.save(scanAddBo);
        if(!result.getSuccess())
            throw new BizException(() -> "RPC 调用失败(真失败，假失败), 抛出异常，致全局事务回滚");
    }

    @GlobalTransactional
    @Override
    public void seataTxTest2(SampleScanAddDto scanAddDto) {
        SampleScan sampleScan = SampleScan.builder().name(scanAddDto.getName()).scanType(scanAddDto.getScanType()).scanTime(scanAddDto.getScanTime()).build();
        sampleScanService.save(sampleScan);

        Sample2ScanAddBo scanAddBo = Sample2ScanAddBo.builder().name(scanAddDto.getName()).scanType(scanAddDto.getScanType()).scanTime(scanAddDto.getScanTime()).build();
        JsonResult<?> result = sample2ServiceApi.save2(scanAddBo);
    }

    @GlobalTransactional
    @Override
    public void seataTxTest3(SampleScanAddDto scanAddDto) {
        SampleScan sampleScan = SampleScan.builder().name(scanAddDto.getName()).scanType(scanAddDto.getScanType()).scanTime(scanAddDto.getScanTime()).build();
        sampleScanService.save(sampleScan);

        Sample2ScanAddBo scanAddBo = Sample2ScanAddBo.builder().name(scanAddDto.getName()).scanType(scanAddDto.getScanType()).scanTime(scanAddDto.getScanTime()).build();
        JsonResult<?> result = sample2ServiceApi.save2(scanAddBo);
        if(!result.getSuccess())
            throw new BizException(() -> "RPC 调用失败(真失败，假失败), 抛出异常，致全局事务回滚");
    }

    @GlobalTransactional
    @Override
    public void seataTxTest4_1() {
        //全局锁形态，锁定多条记录？
        sampleScanService.update(new UpdateWrapper<SampleScan>().lambda().set(SampleScan::getName, "what").eq(SampleScan::getScanType, ScanTypeEnum.SYSTEM.getValue()));

        int a = 1/0;  //or int a = 1/0;
    }

    @GlobalTransactional
    @Override
    public void seataTxTest4_2() {
        //更新 seataTxTest4_1 多条记录中的某几条，全局锁？
        sampleScanService.update(new UpdateWrapper<SampleScan>().lambda().set(SampleScan::getName, "no what").eq(SampleScan::getId, 1839557353308844034L));
    }

}
