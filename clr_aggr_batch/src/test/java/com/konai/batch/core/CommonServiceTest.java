package com.konai.batch.core;

import com.konai.Application;
import com.konai.batch.common.Utils;
import com.konai.batch.core.service.CommonService;
import com.konai.batch.core.vo.TblAggrCodeVo;
import com.konai.batch.core.vo.TblClearingAggrDVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class CommonServiceTest {

	@Autowired
	CommonService service;


	TblClearingAggrDVo vo = new TblClearingAggrDVo();


	@Before
	public void init() {

		vo.setTrCnt(new BigDecimal(12));
		vo.setTrAmt(new BigDecimal(15800));
		vo.setTaxYn("Y");
		vo.setFeeType("F");
		vo.setVatYn("Y");

		vo.setFeeRate(new BigDecimal(1.4));
		vo.setFeeUnitAmt(new BigDecimal(20));

		vo.setRoundCd(-1);



	}

	@Test
	public void 면세_건별_수수료() throws Exception {
		vo.setTaxYn("N");

		TblClearingAggrDVo result = service.commonCalculateFee(vo);

		// 면세/건별: 12*20 원 = 240원

		Assert.assertEquals(new BigDecimal(240), result.getFeeSupAmt());
		Assert.assertEquals(BigDecimal.ZERO, result.getFeeVatAmt());
		Assert.assertEquals(new BigDecimal(240), result.getFeeAmt());

	}

	@Test
	public void 과세_부가세포함_건별_수수료() throws Exception {

		TblClearingAggrDVo result = service.commonCalculateFee(vo);

		// 과세/부가세포함/건별: 12*20 원 = 240원 -> 수수료, 공급가액: 240/1.1 = 218.1818(절사) 부가세 포함에서 공급가액은 반올림

		Assert.assertEquals(new BigDecimal(218), result.getFeeSupAmt());
		Assert.assertEquals(new BigDecimal(22), result.getFeeVatAmt());
		Assert.assertEquals(new BigDecimal(240), result.getFeeAmt());

	}

	@Test
	public void 과세_부가세별도_건별_수수료() throws Exception {
		vo.setVatYn("N");

		TblClearingAggrDVo result = service.commonCalculateFee(vo);

		// 과세/부가세별도/건별: 12*20 원 = 240원 -> 공급가액 // 24 부가세액

		Assert.assertEquals(new BigDecimal(240), result.getFeeSupAmt());
		Assert.assertEquals(new BigDecimal(24), result.getFeeVatAmt());
		Assert.assertEquals(new BigDecimal(264), result.getFeeAmt());

	}


	@Test
	public void 과세_부가세포함_정율_수수료() throws Exception {
		vo.setFeeType("R");


		TblClearingAggrDVo result = service.commonCalculateFee(vo);

		// 과세/부가세포함/정율: 15800 * 0.014 원 = 221.2 (절사) = 221 -> 수수료, 공급가액: 221/1.1 = 200.9090(공급가액은 반올림..)

		Assert.assertEquals(new BigDecimal(201), result.getFeeSupAmt());
		Assert.assertEquals(new BigDecimal(20), result.getFeeVatAmt());
		Assert.assertEquals(new BigDecimal(221), result.getFeeAmt());

	}

	@Test
	public void 과세_부가세별도_정율_수수료() throws Exception {
		vo.setFeeType("R");
		vo.setVatYn("N");

		TblClearingAggrDVo result = service.commonCalculateFee(vo);

		// 과세/부가세별도/정율: 15800 * 0.014 원 = 221.2 (절사) = 221 -> 공급가액

		Assert.assertEquals(new BigDecimal(221), result.getFeeSupAmt());
		Assert.assertEquals(new BigDecimal(22), result.getFeeVatAmt());
		Assert.assertEquals(new BigDecimal(243), result.getFeeAmt());

	}

	@Test
	public void 코드_테스트() throws Exception {

		List<TblAggrCodeVo> codeList = new ArrayList<>();
		List<TblAggrCodeVo> oneList = new ArrayList<>();
		TblAggrCodeVo vo = new TblAggrCodeVo();
		vo.setAspId("1");
		oneList.add(vo);
		TblAggrCodeVo vo2 = new TblAggrCodeVo();
		vo2.setAspId("2");
		oneList.add(vo2);

		List<TblAggrCodeVo> emptyList = new ArrayList<>();

		codeList.addAll(oneList);
		codeList.addAll(emptyList);


		TblClearingAggrDVo aggrVo = new TblClearingAggrDVo();
		aggrVo.setAspId("4");



		TblAggrCodeVo codeVo = codeList.stream()
				.filter(code -> aggrVo.getAspId().equalsIgnoreCase(code.getAspId()))
				.findFirst()
				.orElseGet(() -> null)
				;

		Assert.assertEquals(null, codeVo);



	}

	@Test
	public void substring_Test() throws Exception {

		String str = "DO01WD";
		log.error("############ [{}]",
				String.format("%s,%s,%s", str.substring(0, 2), str.substring(2, 4), str.substring(4, 6)));


	}

	@Test
	public void calendar_test() throws Exception {

		String nextMonthFirstDay = Utils.addMonthFirstDay("20201203", 2);
		log.error("################ [{}]", nextMonthFirstDay);

	}

	@Test
	public void test_stl_function() throws Exception {

		String clrDt = "20201203";
		String aspId = "953365000000000";


		Assert.assertEquals("20201208", service.getStlDt(clrDt, "DO03WD", aspId));
		Assert.assertEquals("20201207", service.getStlDt(clrDt, "DO03ED", aspId));

		Assert.assertEquals("20201210", service.getStlDt(clrDt, "DO05WD", aspId));
		Assert.assertEquals("20201208", service.getStlDt(clrDt, "DO05ED", aspId));

		Assert.assertEquals("20201228", service.getStlDt("20201224", "DO01WD", aspId));
		Assert.assertEquals("20201228", service.getStlDt("20201224", "DO01ED", aspId));

		Assert.assertEquals("20201228", service.getStlDt("20201224", "DO01WD", aspId));
		Assert.assertEquals("20201228", service.getStlDt("20201224", "DO01ED", aspId));

		Assert.assertEquals("20201013", service.getStlDt("20200930", "DO06WD", aspId));
		Assert.assertEquals("20201006", service.getStlDt("20200930", "DO06ED", aspId));

		Assert.assertEquals("20200303", service.getStlDt("20200229", "DO02WD", aspId));
		Assert.assertEquals("20200302", service.getStlDt("20200229", "DO02ED", aspId));

		Assert.assertEquals("20240229", service.getStlDt("20240227", "DO02WD", aspId));
		Assert.assertEquals("20240229", service.getStlDt("20240227", "DO02ED", aspId));

		Assert.assertEquals("20240307", service.getStlDt("20240229", "DO04WD", aspId));
		Assert.assertEquals("20240304", service.getStlDt("20240229", "DO04ED", aspId));



		Assert.assertEquals("20210101", service.getStlDt("20201224", "MO01WD", aspId));
		Assert.assertEquals("20210101", service.getStlDt("20201224", "MO01ED", aspId));

		Assert.assertEquals("20201201", service.getStlDt("20200930", "MO03WD", aspId));
		Assert.assertEquals("20201201", service.getStlDt("20200930", "MO03ED", aspId));

		Assert.assertEquals("20200301", service.getStlDt("20200229", "MO01WD", aspId));
		Assert.assertEquals("20200301", service.getStlDt("20200229", "MO01ED", aspId));

		Assert.assertEquals("20210101", service.getStlDt(clrDt, "MS01WD", aspId));
		Assert.assertEquals("20210101", service.getStlDt(clrDt, "MS01ED", aspId));
		Assert.assertEquals("20210108", service.getStlDt(clrDt, "MS08WD", aspId));
		Assert.assertEquals("20210108", service.getStlDt(clrDt, "MS08ED", aspId));
		Assert.assertEquals("20210111", service.getStlDt(clrDt, "MS10WD", aspId));
		Assert.assertEquals("20210111", service.getStlDt(clrDt, "MS10ED", aspId));
		Assert.assertEquals("20210125", service.getStlDt(clrDt, "MS25WD", aspId));
		Assert.assertEquals("20210125", service.getStlDt(clrDt, "MS25ED", aspId));

		Assert.assertEquals("20240229", service.getStlDt("20240101", "MS29WD", aspId));
		Assert.assertEquals("20240229", service.getStlDt("20240101", "MS29ED", aspId));


	}

	@Test
	public void test_stringutils() throws Exception {
		String str = null;

		if(StringUtils.isEmpty(str)) {
			log.error("null is empty");
		}

	/*	if(StringUtils.isBlank(str)) {
			log.error("null is Blank");
		}*/

		if(str == "") {
			log.error("null is ''");
		}


		String str2 = "";
		if(StringUtils.isEmpty(str2)) {
			log.error("'' is empty");
		}

	/*	if(StringUtils.isBlank(str2)) {
			log.error("'' is Blank");
		}*/

		if(str2 == "") {
			log.error("'' is ''");
		}

	}



}
