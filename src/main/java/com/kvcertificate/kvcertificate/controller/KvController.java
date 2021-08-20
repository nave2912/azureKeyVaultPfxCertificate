package com.kvcertificate.kvcertificate.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.security.cert.CertificateException;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.keys.KeyClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;
import com.azure.security.keyvault.keys.models.JsonWebKey;
import com.azure.security.keyvault.keys.models.KeyVaultKey;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.kvcertificate.kvcertificate.confgi.KVConfiguration;

@RestController
public class KvController {

	@Value("${pass}")
	private String pass;
	
	String pfxName = "www.tempomortgage.com";
	String pfxExtension = "pfx";
	String fileName = "www.tempomortgage.com.pfx";
	String secretValue = null;
	String keyVaultName = "tpo-dv-ss-kv";
	String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";
	@Autowired
	KVConfiguration kv;
	ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
			.clientId("").clientSecret("<>")
			.tenantId("").build();

	SecretClient secretClient = new SecretClientBuilder().vaultUrl(keyVaultUri).credential(clientSecretCredential)
			.buildClient();

	KeyClient keyClient = new KeyClientBuilder().vaultUrl(keyVaultUri).credential(clientSecretCredential).buildClient();
	File file = new File("src" + "\\" + "main" + "\\" + "resources" + "\\" + fileName);

	@GetMapping("/createPfx")
	void pfxdownload() throws CertificateException, KeyStoreException, NoSuchAlgorithmException,
			java.security.cert.CertificateException, IOException {
		KeyVaultSecret secret = secretClient.getSecret("uat-tempomortgage-com");
		String s = secret.getProperties().getContentType();
		byte[] decoded = Base64.getDecoder().decode(secret.getValue().getBytes());
	//	file.getParentFile().mkdirs();
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(decoded);
		System.out.println(s);
		fos.close();
	}

	@GetMapping("/createPfxBck")
	void pfxdownload_bck() {
		KeyVaultSecret secret = secretClient.getSecret("uat-tempomortgage-com");

		try {
			file.getParentFile().mkdirs();
			boolean NewPfxfile = file.createNewFile();
			if (NewPfxfile) {
				FileWriter writer = new FileWriter(file);
				writer.write(secret.getValue());
				writer.close();
			} else {
				System.out.println("file created succesfully" + NewPfxfile);
				System.out.println("file not created");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/deletepfx")
	void pfxdelete() {
		System.out.println("file deleted succesfully");
		file.getParentFile().delete();

	}

	@GetMapping("/readPfx")
	void pfxread() throws KeyStoreException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());	
		
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			FileInputStream fis = new FileInputStream(file);
			System.out.println(file);
			ks.load(fis,pass.toCharArray());
			System.out.println("ks loaded");
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, pass.toCharArray());
			System.out.println("Initialized KeyStoreManager");
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, new SecureRandom());
			System.out.println("initialized SSLContext");

			SSLConnectionSocketFactory sslConSocFactory = new SSLConnectionSocketFactory(sc);
			System.out.println("file Loaded successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
