package com.dazkins.kafka.config.validator;

import java.net.URL;

import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.ConfigDef.Validator;

public class HttpUrlValidator implements Validator {
  @Override
  public void ensureValid(String _, Object value) {
    try {
      new URL(value.toString());
    } catch (Exception e) {
      throw new ConfigException("Error parsing URL: " + e.getMessage());
    }
  }
}
