package com.konai.batch.core.service.processor;

import com.konai.batch.core.service.CommonService;
import com.konai.batch.core.vo.TblAggrCodeVo;
import com.konai.batch.core.vo.TblClearingAggrDVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class CommonProcessor implements ItemProcessor<TblClearingAggrDVo, TblClearingAggrDVo> {

	@Autowired
	private CommonService commonService;

	@lombok.Setter
	private List<TblAggrCodeVo> codeList;

	/*@PostConstruct
    public void init() {
//	    List<TblAggrCodeVo> konaCodeList = repository.selectAggrCodeList(ClrServiceCode.KONA_DISC_AMT.getCode());
//        List<TblAggrCodeVo> mctCodeList = repository.selectAggrCodeList(ClrServiceCode.KONA_DISC_AMT.getCode());
//        List<TblAggrCodeVo> thirdCodeList = repository.selectAggrCodeList(ClrServiceCode.KONA_DISC_AMT.getCode());
//
        codeList.addAll(repository.selectAggrCodeList(ClrServiceCode.KONA_DISC_AMT.getCode()));
        codeList.addAll(repository.selectAggrCodeList(ClrServiceCode.MC_DISC_AMT.getCode()));
        codeList.addAll(repository.selectAggrCodeList(ClrServiceCode.THIRD_DISC_AMT.getCode()));
    }*/

	@Override
	public TblClearingAggrDVo process(TblClearingAggrDVo item) throws Exception {

		TblClearingAggrDVo vo = new TblClearingAggrDVo();
		BeanUtils.copyProperties(item, vo);

        TblAggrCodeVo codeVo = codeList.stream()
				.filter(code -> item.getAspId().equalsIgnoreCase(code.getAspId()) && item.getClrSvcNo().equalsIgnoreCase(code.getClrSvcNo()))
				.findFirst().orElseGet(() -> null);

		if( codeVo == null ) {
			return null;
		}

		vo = commonService.commonCalculateFee(vo, codeVo);

		return vo;
	}
}
