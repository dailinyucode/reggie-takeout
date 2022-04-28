package com.DLY.utils;

import com.aliyun.tea.*;
import com.aliyun.dysmsapi20170525.*;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;

/**
 * 短信发送工具类
 */
public class SMSUtils {

	public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
		Config config = new Config()
				// 您的AccessKey ID
				.setAccessKeyId(accessKeyId)
				// 您的AccessKey Secret
				.setAccessKeySecret(accessKeySecret);
		// 访问的域名
		config.endpoint = "dysmsapi.aliyuncs.com";
		return new com.aliyun.dysmsapi20170525.Client(config);
	}

	/**
	 * 发送短信
	 * @param signName 签名
	 * @param templateCode 模板
	 * @param phoneNumbers 手机号
	 * @param param 参数
	 */
	public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param) throws Exception {
		com.aliyun.dysmsapi20170525.Client client = SMSUtils.createClient("accessKeyId", "accessKeySecret");
		SendSmsRequest sendSmsRequest = new SendSmsRequest()
				.setSignName(signName)
				.setTemplateCode(templateCode)
				.setPhoneNumbers(phoneNumbers)
				.setTemplateParam("{\"code\":\""+param+"\"}");
		// 复制代码运行请自行打印 API 的返回值
		client.sendSms(sendSmsRequest);
		System.out.println("短信发送成功");
	}

}
