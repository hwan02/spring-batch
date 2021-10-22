package com.konai.batch.core.vo;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Alias("TblClearingAggrDVo")
public class TblClearingAggrDVo {

    private String clrDt;
    private BigDecimal clrSeqNo;
    private String stlDt;
    private String aspId;
    // 제휴사 코드
    private String mbrCd;
    // 정산 서비스 번호
    private String clrSvcNo;
    private String clrSvcNm;
    // 항목 대분류 코드
    private String majCd;
    private String majNm;
    // 항목 중분류 코드
    private String midCd;
    private String midNm;
    // 항목 소분류 코드
    private String minCd;
    private String minNm;
    // 수수료 유형. R: 비율 / F: 단가
    private String feeType;
    // 수수료율
    private BigDecimal feeRate = BigDecimal.ZERO;
    // 수수료 단가
    private BigDecimal feeUnitAmt = BigDecimal.ZERO;
    // 부가세 포함 여부. Y: 포함 / N: 별도
    private String vatYn;
    // 과세 여부. Y: 과세 / N: 면세
    private String taxYn;
    // 수수료 절사 정책. 1: 올림 / 0: 반올림 / -1: 내림(절사)
    private int roundCd;
    // 이체/입금 예정일자 주기
    private String stlGenCd;
    // 거래 건수
    private BigDecimal trCnt = BigDecimal.ZERO;
    // 거래 금액
    private BigDecimal trAmt = BigDecimal.ZERO;
    // 수수료 공급가액
    private BigDecimal feeSupAmt = BigDecimal.ZERO;
    // 수수료 부가세액
    private BigDecimal feeVatAmt = BigDecimal.ZERO;
    // 수수료 금액
    private BigDecimal feeAmt = BigDecimal.ZERO;
    // 수수료 등록 요청자
    private String requester = "CLR";
    // 집계 데이터에 대한 상세 설명
    private String etc;

    private Date regDt;
    private String regId;
    private Date updDt;
    private String updId;

    // DB 에는 넣지 않고 processor에서 가공할 때 참고하는 값
    private String tmpType;

}
