package com.common.dto;

/**
 * 类描述: 跨域参数. <br />
 *
 * @author hanjun.hw
 * @since 2018/10/1
 */
public class AllowOriginDto {
    /***
     *  对应 Access-Control-Allow-Origin
     */
    private String accessControlAllowOrigin = "*";
    /**
     * 对应 Access-Control-Allow-Credentials
     */
    private Boolean accessControlAllowCredentials = false;

    public String getAccessControlAllowOrigin() {
        return accessControlAllowOrigin;
    }

    public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
        this.accessControlAllowOrigin = accessControlAllowOrigin;
    }

    public Boolean getAccessControlAllowCredentials() {
        return accessControlAllowCredentials;
    }

    public void setAccessControlAllowCredentials(Boolean accessControlAllowCredentials) {
        this.accessControlAllowCredentials = accessControlAllowCredentials;
    }
}
