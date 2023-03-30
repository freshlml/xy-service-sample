package com.fresh.xy.sample.service.txTest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.xy.sample.entity.txTest.TxTestOne;

public interface TxTestOneService extends IService<TxTestOne> {
    void test_required();
    void test_required_e();

    void test_requiredNew();
    void test_requiredNew_e();

    void test_nested();
    void test_nested_e();

    void test_inline();
    void test_inline_required();
    void test_inline_requiredNew();
    void test_inline_nested();

}
