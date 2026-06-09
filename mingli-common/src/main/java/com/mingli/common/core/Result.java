package com.mingli.common.core;

import java.util.HashMap;

public class Result extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "code";
    public static final String MSG_TAG = "msg";
    public static final String DATA_TAG = "data";

    public Result() {}

    public Result(int code, String msg) {
        put(CODE_TAG, code);
        put(MSG_TAG, msg);
    }

    public Result(int code, String msg, Object data) {
        put(CODE_TAG, code);
        put(MSG_TAG, msg);
        put(DATA_TAG, data);
    }

    public static Result success() {
        return new Result(200, "操作成功");
    }

    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    public static Result success(String msg, Object data) {
        return new Result(200, msg, data);
    }

    public static Result error() {
        return new Result(500, "操作失败");
    }

    public static Result error(String msg) {
        return new Result(500, msg);
    }

    public static Result error(int code, String msg) {
        return new Result(code, msg);
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
