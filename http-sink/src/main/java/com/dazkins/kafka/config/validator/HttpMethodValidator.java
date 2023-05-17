package com.dazkins.kafka.config.validator;

import java.util.Arrays;

import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.ConfigDef.Validator;

public class HttpMethodValidator implements Validator {
  private static final String[] VALID_METHODS = new String[]{
    "GET",
    "POST",
    "PUT",
    "PATCH",
    "DELETE",
    "HEAD",
    "CONNECT",
    "OPTIONS",
    "TRACE",
  };

  @Override
  public void ensureValid(String _, Object value) {
    if (!Arrays.asList(VALID_METHODS).contains(value)) {
      throw new ConfigException("Invalid HTTP method: " + value);
    }
  }
}
