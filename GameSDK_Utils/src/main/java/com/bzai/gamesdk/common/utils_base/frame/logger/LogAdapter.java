package com.bzai.gamesdk.common.utils_base.frame.logger;

public interface LogAdapter {

  boolean isLoggable(int priority, String tag);

  void log(int priority, String tag, String message);
}