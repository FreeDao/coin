package org.jq.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jq.info.I;

public class G {

	public static void main(String[] args) throws Exception {
		while (true) {
			init();
			setPackageName();
			setProperties();
			adtrun();
			antRun();
			sign();
			break;
		}
	}

	static void adtrun() throws Exception {
		File f = new File(I.proPth + "/local.properties");
		while (!f.exists()) {
			System.out.println("please input sdk pth:");
			Scanner sc=new Scanner(System.in);
			String pth=sc.nextLine();
			if(!pth.endsWith("/")||!pth.endsWith("\\")){
				pth+="/";
			}
			Process p = Runtime
					.getRuntime()
					.exec(pth+"tools/android update project -p "
							+ I.proPth);
			p.waitFor();
		}
	}

	static void init() {
		Scanner sc = new Scanner(System.in);
		System.out.println("site:");
		I.site = sc.nextLine();
		System.out.println("app name:");
		I.appName = sc.nextLine();
		System.out.println("package name:");
		I.packName = sc.nextLine();
		System.out.println("ad on menu:");
		I.ad1 = sc.nextLine();
		System.out.println("ad on buttom:");
		I.ad2 = sc.nextLine();
	}

	@SuppressWarnings("deprecation")
	static void setPackageName() throws Exception {
		XMLWriter writer = null;
		SAXReader reader = new SAXReader();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		Document document = reader.read(new File(I.maniPth));
		Element root = document.getRootElement();
		root.setAttributeValue("package", I.packName);
		//System.out.println(document.asXML());
		writer = new XMLWriter(new FileOutputStream(I.maniPth), format);
		writer.write(document);
		writer.close();
	}

	@SuppressWarnings("unchecked")
	static void setProperties() throws Exception {
		XMLWriter writer = null;
		SAXReader reader = new SAXReader();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		Document document = reader.read(new File(I.valuePth));
		Element root = document.getRootElement();
		System.out.println();
		List<Element> list = root.elements("string");
		for (Element ele : list) {
			if (ele.attributeValue("name").equals("app_name")) {
				ele.setText(I.appName);
			} else if (ele.attributeValue("name").equals("page_index")) {
				ele.setText(I.site);
			} else if (ele.attributeValue("name").equals("ad_id1")) {
				ele.setText(I.ad1);
			} else if (ele.attributeValue("name").equals("ad_id2")) {
				ele.setText(I.ad2);
			}
		}
		writer = new XMLWriter(new FileOutputStream(I.valuePth), format);
		writer.write(document);
		writer.close();
	}

	static void antRun() throws Exception {
		ProcessBuilder pro = new ProcessBuilder("ant", "release");
		pro.directory(new File(I.proPth));
		Process p = pro.start();
		InputStream fis = p.getInputStream();
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		p.waitFor();
	}

	static void sign() throws Exception {
		ProcessBuilder pro = new ProcessBuilder("jarsigner", "-keystore",
				"radarkey", "-storepass", "cptbtptp", "-keypass", "cptbtptp",
				"-signedjar", "./bin/book-release.apk",
				"./bin/book-release-unsigned.apk", "a");
		pro.directory(new File(I.proPth));
		Process p = pro.start();
		InputStream fis = p.getInputStream();
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		p.waitFor();
		System.out.println(p.exitValue());
	}
}
