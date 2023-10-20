package com.microsoft.aoai.demo;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.microsoft.aoai.demo.model.RequestModel;
import com.microsoft.aoai.demo.model.ResponseModel;
import com.microsoft.aoai.demo.service.AnalyzeImageService;
import com.microsoft.aoai.demo.service.DeleteUploadedFileService;
import com.microsoft.aoai.demo.service.GetMessageService;
import com.microsoft.aoai.demo.service.LocalStoreService;

@SpringBootApplication
@RestController
@EnableScheduling
public class Controller {

	// Factory factory = new Factory();

	private AnalyzeImageService analyzeImageService;
	private GetMessageService getMessageService;
	private LocalStoreService localStoreService;
	private DeleteUploadedFileService deleteUploadedFileService;

	public static void main(String[] args) {
		SpringApplication.run(Controller.class, args);
	}

	public Controller(
			AnalyzeImageService analyzeImageService,
			GetMessageService getMessageService,
			LocalStoreService localStoreService,
			DeleteUploadedFileService deleteUploadedFileService) {
		this.analyzeImageService = analyzeImageService;
		this.getMessageService = getMessageService;
		this.localStoreService = localStoreService;
		this.deleteUploadedFileService = deleteUploadedFileService;
	}

	@PostMapping("/images")
	public ResponseModel analyzeImage(@RequestPart("file") MultipartFile multipartFile) {
		System.out.println(multipartFile.getSize());

		try {
			this.localStoreService.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			List<String> tags = this.analyzeImageService.analyze(localStoreService.store(multipartFile));
			return new ResponseModel("image", tags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO: リファクタリング
		return null;
	}

	@PostMapping("/messages")
	public ResponseModel getMessage(@RequestBody RequestModel request) {
		List<String> keywords = request.getKeywords();
		List<String> message = this.getMessageService.getMessage(keywords);
		return new ResponseModel("message", message);
	}

	/**
	 * 1時間ごとに一時ディレクトリのファイルを削除する
	 * 
	 * @throws IOException
	 */
	@Scheduled(cron = "* 0 * * * *", zone = "Asia/Tokyo")
	public void deleteTmpFile() throws IOException {
		this.deleteUploadedFileService.deleteTmpFile();
	}

}
