package model.vo;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by jinglun on 2020-03-14
 */
public class ApiResult {
    private Integer code;
    private String message;
    private Object data;

    public ApiResult success() {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(200);
        apiResult.setMessage("success");
        return apiResult;
    }

    public ApiResult success(Object data) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(200);
        apiResult.setMessage("success");
        apiResult.setData(data);
        return apiResult;
    }

    public ApiResult fail() {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(-1);
        apiResult.setMessage("fail");
        return apiResult;
    }

    public ApiResult fail(String message) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(-1);
        apiResult.setMessage(StringUtils.isEmpty(message) ? "fail" : message);
        return apiResult;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
