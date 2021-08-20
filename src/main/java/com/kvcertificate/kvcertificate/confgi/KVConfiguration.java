package com.kvcertificate.kvcertificate.confgi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;

@Configuration
public class KVConfiguration {

	String keyVaultName = "tpo-dv-ss-kv";
	String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";

	public SecretClient SecretClient() {
		System.out.println("client initated");
		return new SecretClientBuilder().vaultUrl(keyVaultUri).credential(new DefaultAzureCredentialBuilder().build())
				.buildClient();
	}

	 void pfxFileWrite(String secretValue, File filePath) {
		try {
			FileWriter writer = new FileWriter(filePath);
			writer.write(secretValue);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean deletePfx(File absoluteFile) {
		return absoluteFile.delete();
	}

	public File createNewFile(String fileName) {
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsoluteFile();

	}

}
