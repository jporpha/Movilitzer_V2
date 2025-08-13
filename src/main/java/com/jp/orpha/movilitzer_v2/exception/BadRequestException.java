package com.movilitzer.v2.exception;
public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg){ super(msg); }
}