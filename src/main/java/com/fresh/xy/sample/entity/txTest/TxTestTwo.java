package com.fresh.xy.sample.entity.txTest;

import com.fresh.xy.common.entity.BaseEntity;
import com.fresh.xy.common.enums.TxTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TxTestTwo extends BaseEntity<Long> {

    private String txName;
    private TxTypeEnum txType;
    private BigDecimal tx_money;


}
