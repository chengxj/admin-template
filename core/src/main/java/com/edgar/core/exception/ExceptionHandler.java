package com.edgar.core.exception;

public interface ExceptionHandler{

   public void handle(String context, String code,
                      String message, Throwable t);
   
}