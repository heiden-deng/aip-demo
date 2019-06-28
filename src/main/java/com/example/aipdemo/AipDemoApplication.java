package com.example.aipdemo;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;

@SpringBootApplication
public class AipDemoApplication {

	public static final String APP_ID = "";
	public static final String API_KEY = "";
	public static final String SECRET_KEY = "";


	public static AipSpeech getAipSpeech(){
		AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
		client.setConnectionTimeoutInMillis(5000);
		client.setSocketTimeoutInMillis(60000);
		return client;
	}

	public static  void testVoiceParse(AipSpeech client ){


		// 可选：设置网络连接参数


		// 可选：设置代理服务器地址, http和socket二选一，或者均不设置
		//client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
		//client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

		// 可选：设置log4j日志输出格式，若不设置，则使用默认配置
		// 也可以直接通过jvm启动参数设置此环境变量
		//System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

		// 调用接口
		JSONObject res = client.asr("/Users/dengjq/Downloads/pcm/LRTS0112_clip/LRTS0112_clip.pcm", "pcm", 16000, null);
		System.out.println(res.toString(2));
	}

	public static  void asr(AipSpeech client)
	{
		// 对本地语音文件进行识别
		String path = "D:\\code\\java-sdk\\speech_sdk\\src\\test\\resources\\16k_test.pcm";
		JSONObject asrRes = client.asr(path, "pcm", 16000, null);
		System.out.println(asrRes);

		// 对语音二进制数据进行识别
		byte[] data = new byte[0];     //readFileByBytes仅为获取二进制数据示例
		try {
			data = Util.readFileByBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject asrRes2 = client.asr(data, "pcm", 16000, null);
		System.out.println(asrRes2);

	}

	public static  void voiceMerge(AipSpeech client){//语音合成示例
		// 调用接口
		//TtsResponse res = client.synthesis("你好百度", "zh", 1, null);
		// 设置可选参数
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put("spd", "5");
		options.put("pit", "5");
		options.put("per", "4");
		TtsResponse res = client.synthesis("你好百度", "zh", 1, options);
		byte[] data = res.getData();
		JSONObject res1 = res.getResult();
		if (data != null) {
			try {
				Util.writeBytesToFileSystem(data, "output.mp3");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (res1 != null) {
			System.out.println(res1.toString(2));
		}
	}


	public static void main(String[] args) {
		// 初始化一个AipSpeech

		AipSpeech client = getAipSpeech();
		voiceMerge(client);
		SpringApplication.run(AipDemoApplication.class, args);
	}

}
