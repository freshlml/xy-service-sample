package com.fresh.xy.sample.service.txTest.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.xy.common.enums.TxTypeEnum;
import com.fresh.xy.common.utils.FlApplicationContextCatch;
import com.fresh.xy.sample.entity.txTest.TxTestOne;
import com.fresh.xy.sample.mapper.txTest.TxTestOneMapper;
import com.fresh.xy.sample.service.txTest.TxTestOneService;
import com.fresh.xy.sample.service.txTest.TxTestTwoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TxTestOneServiceImpl extends ServiceImpl<TxTestOneMapper, TxTestOne> implements TxTestOneService {

    @Autowired
    private TxTestTwoService txTestTwoService;

    @Autowired
    private FlApplicationContextCatch applicationContextCatch;


    @Transactional
    @Override
    public void test_required() {
        log.info("test_required");
        TxTestOne txTestOne = TxTestOne.builder().txName("test_required").txTime(LocalDateTime.now()).txType(TxTypeEnum.ONE_REQUIRED).build();
        this.save(txTestOne);

        //1. 调用目标方法，该方法上声明了 @Transactional with propagation = Propagation.REQUIRED, 目标方法未抛出异常
        /*
        test_required 方法上 @Transactional 前置处理：开启“事务”
        test_required 执行... 如果发生异常，test_transactional 上 @Transactional 后置处理：执行 rollback；否则
        调用 method_required 方法:
            method_required 方法上 @Transactional 前置处理：Participating in existing transaction
            method_required 方法代码执行，没有抛出异常
            method_required 方法上 @Transactional 后置处理：do nothing
        test_required 执行 ... 如果发生异常, test_required 上 @Transactional 后置处理：执行 rollback；否则
        test_required 方法上 @Transactional 后置处理: 执行 commit
         */
        txTestTwoService.method_required();

        //throw new RuntimeException("some error");
    }

    @Transactional
    @Override
    public void test_required_e() {
        log.info("test_required_e");
        TxTestOne txTestOne = TxTestOne.builder().txName("test_required_e").txTime(LocalDateTime.now()).txType(TxTypeEnum.ONE_REQUIRED).build();
        this.save(txTestOne);

        //2. 调用目标方法，该方法上声明了 @Transactional with propagation = Propagation.REQUIRED, 目标方法抛出异常
        /*
        test_required_e 方法上 @Transactional 前置处理：开启“事务”
        test_required_e 执行... 如果发生异常，test_required_e 上 @Transactional 后置处理：执行 rollback；否则
        调用 method_required_e 方法:
            method_required_e 方法上 @Transactional 前置处理：Participating in existing transaction
            method_required_e 方法代码执行，抛出"异常1"
            method_required_e 方法上 @Transactional 后置处理：Participating transaction failed - marking existing transaction as rollback-only, 并抛出"异常1"
        如果 test_required_e 不捕获"异常1", "异常1"向上抛，test_required_e 方法上 @Transactional 后置处理：执行 rollback 并且抛出"异常1"
        如果 test_required_e 捕获"异常1", test_required_e 方法上 @Transactional 后置处理：执行 commit，由于”事务“有 rollback-only 标记，会执行 rollback 并且抛出"异常2"
         */
        txTestTwoService.method_required_e();

        //throw new RuntimeException("some error");
    }

    @Transactional
    @Override
    public void test_requiredNew() {
        log.info("test_requiredNew");
        TxTestOne txTestOne = TxTestOne.builder().txName("test_requiredNew").txTime(LocalDateTime.now()).txType(TxTypeEnum.ONE_REQUIRED_NEW).build();
        this.save(txTestOne);

        //3. 调用目标方法，该方法上声明了 @Transactional with propagation = Propagation.REQUIRES_NEW, 目标方法未抛出异常
        /*
        test_requiredNew 方法上 @Transactional 前置处理：开启“事务”
        test_requiredNew 方法执行..., 如果发生异常，test_requiredNew 方法上 @Transactional 后置处理：执行 rollback
        调用 method_requiredNew 方法：
            method_requiredNew 方法上 @Transactional 前置处理：Suspending current transaction, creating new transaction
            method_requiredNew 方法代码执行，没有抛出异常
            method_requiredNew 方法上 @Transactional 后置处理：执行 commit
        test_requiredNew 方法执行... 如果发生异常, test_requiredNew 方法上 @Transactional 后置处理：执行 rollback
        test_requiredNew 方法上 @Transactional 后置处理: Resuming suspended transaction after completion of inner transaction, 执行 commit
         */
        txTestTwoService.method_requiredNew();

        //throw new RuntimeException("some error");
    }

    @Transactional
    @Override
    public void test_requiredNew_e() {
        log.info("test_requiredNew_e");
        TxTestOne txTestOne = TxTestOne.builder().txName("test_requiredNew_e").txTime(LocalDateTime.now()).txType(TxTypeEnum.ONE_REQUIRED_NEW).build();
        this.save(txTestOne);

        //4. 调用目标方法，该方法上声明了 @Transactional with propagation = Propagation.REQUIRES_NEW, 目标方法抛出异常
        /*
        test_requiredNew_e 方法上 @Transactional 前置处理：开启“事务”
        test_requiredNew_e 方法执行..., 如果发生异常，test_requiredNew_e 方法上 @Transactional 后置处理：执行 rollback
        调用 method_requiredNew_e 方法：
            method_requiredNew_e 方法上 @Transactional 前置处理：Suspending current transaction, creating new transaction
            method_requiredNew_e 方法代码执行，抛出"异常1"
            method_requiredNew_e 方法上 @Transactional 后置处理：执行 rollback
        method_requiredNew_e 方法不捕获"异常1", "异常1"向上抛，那么 method_requiredNew_e 方法上 @Transactional 后置处理：执行 rollback 并且抛出"异常1"
        method_requiredNew_e 方法捕获"异常1", method_requiredNew_e 方法上 @Transactional 后置处理：Resuming suspended transaction after completion of inner transaction，执行 commit
         */
        try {
            txTestTwoService.method_requiredNew_e();
        } catch (Exception e) {
            //
        }

        //throw new RuntimeException("some error");
    }

    @Transactional
    @Override
    public void test_nested() {
        log.info("test_nested");
        TxTestOne txTestOne = TxTestOne.builder().txName("test_nested").txTime(LocalDateTime.now()).txType(TxTypeEnum.ONE_NESTED).build();
        this.save(txTestOne);

        //5. 调用目标方法，该方法上声明了 @Transactional with propagation = Propagation.NESTED, 目标方法未抛出异常
        /*
        test_nested 方法上 @Transactional 前置处理：开启“事务”
        test_nested 方法执行..., 如果发生异常，test_nested 方法上 @Transactional 后置处理：执行 rollback
        调用 method_nested 方法：
            method_nested 方法上 @Transactional 前置处理：Creating nested transaction
            method_nested 方法代码执行，没有抛出异常
            method_nested 方法上 @Transactional 后置处理：Releasing transaction savepoint(nested 事务 commit 状态，未提交)
        test_nested 方法执行... 如果发生异常, test_nested 方法上 @Transactional 后置处理：执行 rollback，同时 rollback nested
        test_nested 方法上 @Transactional 后置处理: 执行 commit, 同时 commit nested
         */
        txTestTwoService.method_nested();

        throw new RuntimeException("some error");
    }

    @Transactional
    @Override
    public void test_nested_e() {
        log.info("test_nested_e");
        TxTestOne txTestOne = TxTestOne.builder().txName("test_nested_e").txTime(LocalDateTime.now()).txType(TxTypeEnum.ONE_NESTED).build();
        this.save(txTestOne);

        //6. 调用目标方法，该方法上声明了 @Transactional with propagation = Propagation.NESTED, 目标方法抛出异常
        /*
        test_nested_e 方法上 @Transactional 前置处理：开启“事务”
        test_nested_e 方法执行..., 如果发生异常，test_nested_e 方法上 @Transactional 后置处理：执行 rollback
        调用 method_nested_e 方法：
            method_nested_e 方法上 @Transactional 前置处理：Creating nested transaction
            method_nested_e 方法代码执行，抛出"异常1"
            method_nested_e 方法上 @Transactional 后置处理：Rolling back transaction to savepoint(nested 事务 rollback 状态，未回滚)
        如果 test_nested_e 方法不捕获"异常1", "异常1"向上抛，那么 test_nested_e 方法上 @Transactional 后置处理：执行 rollback 同时 rollback nested 并且抛出"异常1"
        如果 test_nested_e 方法捕获"异常1", test_nested_e 方法上 @Transactional 后置处理：执行 commit, 同时 rollback nested
         */
        try {
            txTestTwoService.method_nested_e();
        } catch (Exception e) {
            //
        }

        //throw new RuntimeException("some error");
    }


    @Transactional
    @Override
    public void test_inline() { //使用 this 调用此 service 中方法
        log.info("test_inline");
        TxTestOne txTestOne = TxTestOne.builder().txName("test_inline").txTime(LocalDateTime.now()).txType(TxTypeEnum.ONE_REQUIRED).build();
        this.save(txTestOne);

        System.out.println("this 是否是 AopProxy: " + AopUtils.isAopProxy(this)); //false
        System.out.println("this 是否是 JdkProxy: " + AopUtils.isJdkDynamicProxy(this)); //false
        System.out.println("this 是否是 CglibProxy: " + AopUtils.isCglibProxy(this)); //false

        //1、此 this 是被代理对象, 方法上面的 @Transactional 注解不会触发
        //this.test_inline_required();
        //this.test_inline_requiredNew();

        //获取 this 的代理对象
        TxTestOneServiceImpl proxy_of_this = applicationContextCatch.getBean(TxTestOneServiceImpl.class);
        System.out.println(proxy_of_this.equals(this));  //false

        //2、使用代理对象调用 test_inline_requiredNew 方法, 触发 @Transactional
        //   Suspending current transaction, creating new transaction with name "test_inline_requiredNew",
        //   "test_inline_requiredNew" commit or rollback, Resuming suspended transaction after completion of inner transaction
        proxy_of_this.test_inline_requiredNew();

        //3、使用代理对象调用 test_inline_required 方法, 触发 @Transactional
        //   Participating in existing transaction
        proxy_of_this.test_inline_required();

        //4、使用代理对象调用 test_inline_nested 方法, 触发 @Transactional
        //   Creating nested transaction
        proxy_of_this.test_inline_nested();

        //throw new RuntimeException("some error");
    }

    @Transactional
    @Override
    public void test_inline_required() {
        log.info("test_inline_required");
        TxTestOne txTestOne = TxTestOne.builder().txName("test_inline_required").txTime(LocalDateTime.now()).txType(TxTypeEnum.ONE_REQUIRED).build();
        this.save(txTestOne);
        //throw new RuntimeException("some error");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void test_inline_requiredNew() {
        log.info("test_inline_requiredNew");
        TxTestOne txTestOne = TxTestOne.builder().txName("test_inline_requiredNew").txTime(LocalDateTime.now()).txType(TxTypeEnum.ONE_REQUIRED_NEW).build();
        this.save(txTestOne);
        //throw new RuntimeException("some error");
    }

    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void test_inline_nested() {
        log.info("test_inline_nested");
        TxTestOne txTestOne = TxTestOne.builder().txName("test_inline_nested").txTime(LocalDateTime.now()).txType(TxTypeEnum.ONE_NESTED).build();
        this.save(txTestOne);
        //throw new RuntimeException("some error");
    }


}
