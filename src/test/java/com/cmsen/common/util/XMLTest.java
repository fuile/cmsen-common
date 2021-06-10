package com.cmsen.common.util;

import com.cmsen.common.lang.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import junit.framework.TestCase;

import java.util.List;

public class XMLTest extends TestCase {
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

    String sfXML =
            "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                    "<Response service=\"RouteService\">\n" +
                    "   <Head>OK</Head>\n" +
                    "   <Body>\n" +
                    "       <RouteResponse mailno=\"******\">\n" +
                    "           <Route remark=\"顺丰速运 已收取快件\" accept_time=\"2019-04-02 21:36:09\" accept_address=\"广州市\" opcode=\"54\"></Route>\n" +
                    "           <Route remark=\"顺丰速运 已收取快件\" accept_time=\"2019-04-02 21:54:43\" accept_address=\"广州市\" opcode=\"50\"></Route>\n" +
                    "       </RouteResponse>\n" +
                    "   </Body>\n" +
                    "</Response>\n";

    public void test() {
        Response parse = XML.parse(sfXML, Response.class);
        // System.err.println(parse);
        System.err.println("------------------");
        String string = XML.toString(parse);
        // 按按照视图类输出
        // String string = XML.toString(parse, XML.View.class);
        // System.err.println(string);

        Res<Response> xmlRes = new Res<Response>() {
        };


        System.err.println(xmlRes);
        System.err.println(xmlRes.getType());
        System.err.println(xmlRes.newInstanceType());

        System.err.println("Byte=" + Byte.MAX_VALUE + ":" + Byte.MIN_VALUE);
        System.err.println("Short=" + Short.MAX_VALUE + ":" + Short.MIN_VALUE);
        System.err.println("Integer=" + Integer.MAX_VALUE + ":" + Integer.MIN_VALUE);
        System.err.println("Long=" + Long.MAX_VALUE + ":" + Long.MIN_VALUE);

    }

    public static class Res<T> extends TypeReference<T> {
    }


    @JacksonXmlRootElement(localName = "Response")
    public static class Response {
        @JacksonXmlProperty(localName = "service", isAttribute = true)
        private String service;
        @JacksonXmlProperty(localName = "Head")
        private String Head;

        // @JsonView(XML.View.class)  // 按按照视图类输出
        @JacksonXmlProperty(localName = "Body")
        private Body Body;

        @Override
        public String toString() {
            return "Response{" +
                    "service='" + service + '\'' +
                    ", Head='" + Head + '\'' +
                    ", Body=" + Body +
                    '}';
        }
    }

    public static class Body {
        @JacksonXmlProperty(localName = "RouteResponse")
        private RouteResponse RouteResponse;

        public XMLTest.RouteResponse getRouteResponse() {
            return RouteResponse;
        }

        public void setRouteResponse(XMLTest.RouteResponse routeResponse) {
            RouteResponse = routeResponse;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "RouteResponse=" + RouteResponse +
                    '}';
        }
    }

    public static class RouteResponse {
        @JacksonXmlProperty(localName = "mailno", isAttribute = true)
        private String mailno;
        @JacksonXmlProperty(localName = "Route")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Route> Route;

        public String getMailno() {
            return mailno;
        }

        public void setMailno(String mailno) {
            this.mailno = mailno;
        }

        public List<XMLTest.Route> getRoute() {
            return Route;
        }

        public void setRoute(List<XMLTest.Route> route) {
            Route = route;
        }

        @Override
        public String toString() {
            return "RouteResponse{" +
                    "mailno='" + mailno + '\'' +
                    ", Route=" + Route +
                    '}';
        }
    }

    public static class Route {
        @JacksonXmlProperty(localName = "remark", isAttribute = true)
        private String remark;
        @JacksonXmlProperty(localName = "accept_time", isAttribute = true)
        private String accept_time;
        @JacksonXmlProperty(localName = "accept_address", isAttribute = true)
        private String accept_address;
        @JacksonXmlProperty(localName = "opcode", isAttribute = true)
        private String opcode;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAccept_time() {
            return accept_time;
        }

        public void setAccept_time(String accept_time) {
            this.accept_time = accept_time;
        }

        public String getAccept_address() {
            return accept_address;
        }

        public void setAccept_address(String accept_address) {
            this.accept_address = accept_address;
        }

        public String getOpcode() {
            return opcode;
        }

        public void setOpcode(String opcode) {
            this.opcode = opcode;
        }

        @Override
        public String toString() {
            return "Route{" +
                    "remark='" + remark + '\'' +
                    ", accept_time='" + accept_time + '\'' +
                    ", accept_address='" + accept_address + '\'' +
                    ", opcode='" + opcode + '\'' +
                    '}';
        }
    }
}