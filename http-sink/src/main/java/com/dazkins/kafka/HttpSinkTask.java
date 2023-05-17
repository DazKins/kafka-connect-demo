package com.dazkins.kafka;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dazkins.kafka.config.HttpSinkConfig;

public class HttpSinkTask extends SinkTask {
  private static final Logger logger = LoggerFactory.getLogger(HttpSinkTask.class);

  private Map<String, String> config;

  @Override
  public void start(Map<String, String> config) {
    this.config = config;
  }

  @Override
  public void put(Collection<SinkRecord> records) {
    for (SinkRecord record : records) {
      logger.info("Got record: {}", record.toString());

      try {
        URL url = new URL(config.get(HttpSinkConfig.HTTP_URL));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod(config.get(HttpSinkConfig.HTTP_METHOD));

        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(record.value().toString());
        osw.flush();
        osw.close();
        os.close();

        logger.info("Response code: {}", connection.getResponseCode());

        connection.connect();
      } catch (Exception e) {
        logger.error("Error: {}", e);
      }
    }
  }

  @Override
  public void flush(Map<TopicPartition, OffsetAndMetadata> map) {

  }

  @Override
  public void stop() {
  }

  @Override
  public String version() {
    return "0.0.0";
  }
}
