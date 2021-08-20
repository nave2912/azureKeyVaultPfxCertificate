package com.kvcertificate.kvcertificate.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KvController2 {
	@Value("${secretValue}")
	private String secretValueFromAppProperties;
	String pfxName = "uat-tempomortgage-com";
	String pfxExtension = "pfx";
	String fileName = pfxName + "." + pfxExtension;
	String keyVaultName = "tpo-dv-ss-kv";
	String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";

	File file = new File("src\\main\\resources\\"+fileName);

	@GetMapping("/createPfxFromAppProp")
	void pfxdownload() throws IOException{
		
		//file.getParentFile().mkdirs();
	
		File pfxFile = new File(System.getProperty("user.dir")+"\\");
		byte[] decoded = Base64.getDecoder().decode(secretValueFromAppProperties.getBytes());
		pfxFile.delete();
			
	 
	}

}
