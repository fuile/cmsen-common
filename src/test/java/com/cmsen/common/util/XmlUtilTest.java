package com.cmsen.common.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class XmlUtilTest {

    @Test
    public void xml() throws Exception {
        String strXML = "<xml> \n" +
                "  <ToUserName><![CDATA[touser]]></ToUserName>  \n" +
                "  <FromUserName><![CDATA[fromuser]]></FromUserName>  \n" +
                "  <CreateTime>1399197672</CreateTime>  \n" +
                "  <MsgType><![CDATA[transfer_customer_service]]></MsgType>  \n" +
                "  <TransInfo> \n" +
                "    <KfAccount><![CDATA[test1@test]]></KfAccount> \n" +
                "    <KfMessage><![CDATA[hallo]]></KfMessage> \n" +
                "  </TransInfo> \n" +
                "</xml>";

        Map<String, Object> objectMap = new LinkedHashMap<>();
        Map<String, Object> objectMap2 = new LinkedHashMap<>();
        objectMap2.put("KfAccount", "test1@test");
        objectMap2.put("KfMessage", "hallo");
        objectMap.put("ToUserName", "touser");
        objectMap.put("CreateTime", 1399197672);
        objectMap.put("TransInfo", objectMap2);
        System.err.println(XmlUtil.toObject(strXML));
        System.err.println(XmlUtil.toClass(strXML, XmlEntity.class));
        System.err.println(XmlUtil.toXml(objectMap));
        System.err.println("-----");
        System.err.println(XmlUtil.toXml(objectMap, true));
    }

    public static class XmlEntity {
        @JsonProperty("ToUserName")
        private String toUserName;
        @JsonProperty("FromUserName")
        private String fromUserName;
        @JsonProperty("CreateTime")
        private String createTime;

        public XmlEntity() {
        }

        public String getToUserName() {
            return toUserName;
        }

        public void setToUserName(String toUserName) {
            this.toUserName = toUserName;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}