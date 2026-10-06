package com.fresh.xy.sample.service.seata;

import com.fresh.xy.sample.dto.scan.SampleScanAddDto;

import javax.validation.Valid;

public interface SeataTestService {

    void seataTxTest(SampleScanAddDto scanAddDto);

    void seataTxTest1_1(@Valid SampleScanAddDto scanAddDto);

    void seataTxTest2(SampleScanAddDto scanAddDto);

    void seataTxTest3(SampleScanAddDto scanAddDto);

    void seataTxTest4_1();

    void seataTxTest4_2();

}
