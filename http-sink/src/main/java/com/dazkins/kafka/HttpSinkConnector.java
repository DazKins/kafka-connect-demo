package com.dazkins.kafka;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dazkins.kafka.config.HttpSinkConfig;

public class HttpSinkConnector extends SinkConnector {
  private static final Logger logger = LoggerFactory.getLogger(HttpSinkConnector.class);

  private Map<String, String> config;

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    return Collections.nCopies(maxTasks, config);
  }

  @Override
  public void start(Map<String, String> settings) {
    logger.info("test456");

    this.config = settings;
  }

  @Override
  public void stop() {
  }

  @Override
  public ConfigDef config() {
    return HttpSinkConfig.getConfigDef();
  }

  @Override
  public Class<? extends Task> taskClass() {
    return HttpSinkTask.class;
  }

  @Override
  public String version() {
    return "0.0.0";
  }
}
