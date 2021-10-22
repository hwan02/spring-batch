package com.konai.batch.common;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cubeone.CubeOneAPI;

@Slf4j
@Component
public class KonaEncryptUtil {

    private boolean useEncDb = false;

	@Value("${db.enc.yn:N}")
	private String encYn;

	@PostConstruct
	void initilize(){

		log.info("CubeOne Encrypt Y/N [{}]", encYn);

		if("Y".equals(encYn) || "y".equals(encYn))
		{
			useEncDb = true;
			CubeOneAPI.jcoinit("API");
		}
	}

	public String encryptCardInfo(String plainStr){
		if(useEncDb && plainStr != null)
		{
			plainStr = encCubeone("CARD_INFO", plainStr);
		}
		return plainStr;
	}

	public String decryptCardInfo(String plainStr){
		if(useEncDb && plainStr != null)
		{
			plainStr = decCubeone("CARD_INFO", plainStr);
		}
		return plainStr;
	}

	public String encryptCustomerInfo(String plainStr){
		if(useEncDb && plainStr != null)
		{
			plainStr = encCubeone("CUSTOMER_INFO", plainStr);
		}
		return plainStr;
	}

	public String decryptCustomerInfo(String plainStr) {
		if(useEncDb && plainStr != null)
		{
			plainStr = decCubeone("CUSTOMER_INFO", plainStr);
		}
		return plainStr;
	}

	private String encCubeone(String item, String plainStr){

		byte[] errbyte = new byte[5];
		String result = CubeOneAPI.coencchar(plainStr, item,10,null,null,errbyte);
		try {
			checkError(item, plainStr, errbyte);
		} catch (Exception e) {
			return plainStr;
		}
		return result;
	}

	private String decCubeone(String item, String plainStr){
		byte[] errbyte = new byte[5];
		String result = CubeOneAPI.codecchar(plainStr, item,10,null,null,errbyte);
		try {
			checkError(item, plainStr, errbyte);
		} catch (Exception e) {
			return plainStr;
		}
		return result;
	}

	private void checkError(String item, String plainStr, byte[] errbyte) throws Exception {
		String errorStr = new String(errbyte, StandardCharsets.UTF_8);
		if(!("00000".equals(errorStr)))
		{
			log.error("CubeOne Error : item[{}], str[{}], error[{}]", item, plainStr, errorStr);
//			throw new ApiException(IASErrorEnum.SERVER_EXCEPTION.getValue(), "CubeOne Error : "+ errorStr);
		}

	}

}
