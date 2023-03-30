package com.fresh.xy.sample.service.txTest.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.xy.common.enums.TxTypeEnum;
import com.fresh.xy.sample.entity.txTest.TxTestTwo;
import com.fresh.xy.sample.mapper.txTest.TxTestTwoMapper;
import com.fresh.xy.sample.service.txTest.TxTestTwoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Slf4j
@Service
public class TxTestTwoServiceImpl extends ServiceImpl<TxTestTwoMapper, TxTestTwo> implements TxTestTwoService {


    @Transactional
    @Override
    public void method_required() {
        log.info("method_required");
        TxTestTwo txTestTwo = TxTestTwo.builder().txName("method_required").txType(TxTypeEnum.TWO_REQUIRED).tx_money(new BigDecimal("1.0001")).build();
        this.save(txTestTwo);
    }

    @Transactional
    @Override
    public void method_required_e() {
        log.info("method_required_e");
        TxTestTwo txTestTwo = TxTestTwo.builder().txName("method_required_e").txType(TxTypeEnum.TWO_REQUIRED).tx_money(new BigDecimal("1.0001")).build();
        this.save(txTestTwo);
        throw new RuntimeException("some error");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void method_requiredNew() {
        log.info("method_requiredNew");
        TxTestTwo txTestTwo = TxTestTwo.builder().txName("method_requiredNew").txType(TxTypeEnum.TWO_REQUIRED_NEW).tx_money(new BigDecimal("1.0001")).build();
        this.save(txTestTwo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void method_requiredNew_e() {
        log.info("method_requiredNew_e");
        TxTestTwo txTestTwo = TxTestTwo.builder().txName("method_requiredNew_e").txType(TxTypeEnum.TWO_REQUIRED_NEW).tx_money(new BigDecimal("1.0001")).build();
        this.save(txTestTwo);
        throw new RuntimeException("some error");
    }

    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void method_nested() {
        log.info("method_nested");
        TxTestTwo txTestTwo = TxTestTwo.builder().txName("method_nested").txType(TxTypeEnum.TWO_NESTED).tx_money(new BigDecimal("1.0001")).build();
        this.save(txTestTwo);
    }

    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void method_nested_e() {
        log.info("method_nested_e");
        TxTestTwo txTestTwo = TxTestTwo.builder().txName("method_nested_e").txType(TxTypeEnum.TWO_NESTED).tx_money(new BigDecimal("1.0001")).build();
        this.save(txTestTwo);
        throw new RuntimeException("some error");
    }
}
