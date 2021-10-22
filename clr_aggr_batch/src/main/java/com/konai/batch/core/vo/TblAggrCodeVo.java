package com.konai.batch.core.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Alias("TblAggrCodeVo")
public class TblAggrCodeVo {

    private String id;
    private String aspId;
    private String mbrCd;
    private String mbrNm;
    private String clrKindCd;
    private String clrSvcNo;
    private String clrSvcNm;
    private String majCd;
    private String majNm;
    private String midCd;
    private String midNm;
    private String minCd;
    private String minNm;
    private String feeType;
    private BigDecimal feeUnit;
    private String taxYn;
    private String vatYn;
    private int roundCd;
    private String stlGenCd;
    private String description;
    private String etc;
    private String useYn;
    private Date regDt;
    private String regId;
    private Date updDt;
    private String updId;

}
