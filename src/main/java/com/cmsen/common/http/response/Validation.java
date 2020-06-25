package com.cmsen.common.http.response;

/**
 * 组验证接口
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public interface Validation {
    /**
     * 必需
     */
    String required = "{validation.required}";

    interface Find {
    }

    interface Select {
        /**
         * 示例组能力
         */
        String required = "{validation.select.required}";
    }

    interface Search {
    }

    interface Create {
    }

    interface Update {
    }

    interface Delete {
    }
}
