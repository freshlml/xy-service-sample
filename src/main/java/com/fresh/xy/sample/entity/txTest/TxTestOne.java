package com.fresh.xy.sample.entity.txTest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fresh.xy.common.entity.BaseEntity;
import com.fresh.xy.common.enums.TxTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TxTestOne extends BaseEntity<Long> {

    private String txName;
    private TxTypeEnum txType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")  //json序列化与反序列化的格式化器
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  //spring中日期格式化器
    private LocalDateTime txTime;

}
