package com.fresh.xy.sample.service.txTest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.xy.sample.entity.txTest.TxTestTwo;

public interface TxTestTwoService extends IService<TxTestTwo> {

    void method_required();
    void method_required_e();

    void method_requiredNew();
    void method_requiredNew_e();

    void method_nested();
    void method_nested_e();

}
