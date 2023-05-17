package com.dazkins.kafka.config;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Width;

import com.dazkins.kafka.config.validator.HttpMethodValidator;
import com.dazkins.kafka.config.validator.HttpUrlValidator;

public class HttpSinkConfig {
  private static final String HTTP_GROUP = "Http";

  public static final String HTTP_URL = "http.url";
  public static final String HTTP_METHOD = "http.method";

  private static ConfigDef instance;

  public static ConfigDef getConfigDef() {
    if (instance == null)
      instance = generateConfigDef();

    return instance;
  }

  private static ConfigDef generateConfigDef() {
    ConfigDef configDef = new ConfigDef();

    int httpGroupOrder = 0;

    configDef.define(
      HTTP_URL,
      ConfigDef.Type.STRING,
      ConfigDef.NO_DEFAULT_VALUE,
      new HttpUrlValidator(),
      ConfigDef.Importance.HIGH,
      "URL to call",
      HTTP_GROUP,
      httpGroupOrder++,
      Width.LONG,
      HTTP_URL
    );

    configDef.define(
      HTTP_METHOD,
      ConfigDef.Type.STRING,
      "POST",
      new HttpMethodValidator(),
      ConfigDef.Importance.LOW,
      "HTTP method to use when calling the HTTP URL",
      HTTP_GROUP,
      httpGroupOrder++,
      Width.SHORT,
      HTTP_METHOD
    );

    return configDef;
  }
}
