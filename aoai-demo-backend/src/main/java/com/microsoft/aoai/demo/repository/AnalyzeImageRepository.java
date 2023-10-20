package com.microsoft.aoai.demo.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aoai.demo.model.Settings;

import lombok.Data;

@Repository
public class AnalyzeImageRepository {

    Logger logger = Logger.getLogger(AnalyzeImageRepository.class.getName());

    private Settings settings;

    public AnalyzeImageRepository(Settings settings) throws Exception {
        try {

            if (settings == null || settings.getComputerVisionEndpoint() == null
                    || settings.getComputerVisionKey() == null) {
                throw new Exception("環境変数が設定されていません。");
            }

            this.settings = settings;

        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        }
    }

    public List<String> analyze(String filePath) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", this.settings.getComputerVisionKey());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        Path path = Paths.get(filePath);

        byte[] imageBytes = null;
        try {
            imageBytes = Files.readAllBytes(path);

            HttpEntity<byte[]> entity = new HttpEntity<>(imageBytes, headers);

            ResponseEntity<String> exchange = restTemplate.exchange(
                    this.settings.getComputerVisionEndpoint()
                            + "computervision/imageanalysis:analyze?features=tags&model-version=latest&language=ja&api-version=2023-02-01-preview",
                    HttpMethod.POST, entity, String.class);

            HttpStatusCode statusCode = exchange.getStatusCode(); // TODO:200以外は処理が必要
            String body = exchange.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            Response response = objectMapper.readValue(body, Response.class);
            // String tags = response.getTagsSDKFormatJson();
            List<String> tagsArray = response.getTagsArray();
            return tagsArray;

        } catch (Exception e) {
            logger.severe(e.getMessage());
            return null;
        }
    }

    @Data
    public static class Tag {
        private String name;
        private double confidence;
    }

    @Data
    public static class TagsResult {
        private List<Tag> values;
    }

    @Data
    public static class MetaData {
        private int width;
        private int height;
    }

    @Data
    public static class Response {
        @JsonProperty("tagsResult")
        private TagsResult tagsResult;

        @JsonProperty("modelVersion")
        private String modelVersion;

        @JsonProperty("metadata")
        private MetaData metaData;

        /**
         * タグのJSONを返す。Preview SDKの仕様に合わせている。
         */
        public String getTagsSDKFormatJson() {
            JSONArray tagsArray = new JSONArray();
            for (Tag tag : tagsResult.getValues()) {
                tagsArray.put(tag.getName());
            }

            JSONObject resultObject = new JSONObject();
            resultObject.put("tags", tagsArray);

            return resultObject.toString();
        }

        /**
         * タグのJSONを返す。APIからの戻り値と同じ書式。
         */
        public String getTagJson() {
            JSONArray tagsArray = new JSONArray();
            for (Tag tag : tagsResult.getValues()) {
                JSONObject tagObject = new JSONObject();
                tagObject.put("name", tag.getName());
                tagObject.put("confidence", tag.getConfidence());
                tagsArray.put(tagObject);
            }

            JSONObject resultObject = new JSONObject();
            resultObject.put("tags", tagsArray);

            return resultObject.toString();
        }

        /**
         * タグの配列を返す。
         */
        public List<String> getTagsArray() {
            List<String> tagsArray = new ArrayList<>(tagsResult.getValues().size());
            for (int i = 0; i < tagsResult.getValues().size(); i++) {
                tagsArray.add(tagsResult.getValues().get(i).getName());
            }
            return tagsArray;
        }
    }
}
