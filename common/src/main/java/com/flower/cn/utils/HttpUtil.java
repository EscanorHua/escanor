package com.flower.cn.utils;



import com.flower.cn.exception.ExceptionEnum;
import com.flower.cn.exception.TradeException;
import jodd.io.StreamUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class HttpUtil {

    public static RequestWrapper getReq(HttpServletRequest req, String body) {
        try {
            if (body == null) return new RequestWrapper(req);
            return new RequestWrapper(req, body);
        } catch (Exception e) {
            throw new TradeException(ExceptionEnum.SERVER_ERROR);
        }
    }

    public static ResponseWrapper getResp(HttpServletResponse response) {
        try {
            return new ResponseWrapper(response);
        } catch (Exception e) {
            throw new TradeException(ExceptionEnum.SERVER_ERROR);
        }
    }

    public static class RequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body; //用于保存读取body中数据

        public RequestWrapper(HttpServletRequest request)
                throws IOException {
            super(request);
            body = StreamUtil.readBytes(request.getReader(), "UTF-8");
        }

        public RequestWrapper(HttpServletRequest request, String body) throws IOException {
            super(request);
            this.body = body.getBytes("UTF-8");
        }

        public byte[] getBody() {
            return this.body;
        }


        @Override
        public String getCharacterEncoding() {
            return super.getCharacterEncoding() == null ? "UTF-8" : super.getCharacterEncoding();
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream bais = new ByteArrayInputStream(body);
            return new ServletInputStream() {

                @Override
                public int read() throws IOException {
                    return bais.read();
                }

                @Override
                public boolean isFinished() {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean isReady() {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public void setReadListener(ReadListener arg0) {
                    // TODO Auto-generated method stub

                }
            };
        }
    }

    public static class ResponseWrapper extends HttpServletResponseWrapper {
        private ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        private HttpServletResponse response;
        private PrintWriter pwrite;

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
            this.response = response;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new MyServletOutputStream(bytes); // 将数据写到 byte 中
        }

        /**
         * 重写父类的 getWriter() 方法，将响应数据缓存在 PrintWriter 中
         */
        @Override
        public PrintWriter getWriter() throws IOException {
            try{
                pwrite = new PrintWriter(new OutputStreamWriter(bytes, "utf-8"));
            } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return pwrite;
        }

        public String getResult() {
            try {
                return new String(getBytes(), "UTF-8");
            } catch (Exception e) {
                return "";
            }
        }
        /**
         * 获取缓存在 PrintWriter 中的响应数据
         * @return
         */
        public byte[] getBytes() {
            if(null != pwrite) {
                pwrite.close();
                return bytes.toByteArray();
            }

            if(null != bytes) {
                try {
                    bytes.flush();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            return bytes.toByteArray();
        }
    }

    public static class MyServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream ostream ;

        public MyServletOutputStream(ByteArrayOutputStream ostream) {
            this.ostream = ostream;
        }

        @Override
        public void write(int b) throws IOException {
            ostream.write(b); // 将数据写到 stream　中
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }
    }
    
    
    public static String getIpAddress(HttpServletRequest request) {   
        String ip = request.getHeader("x-forwarded-for");   
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
          ip = request.getHeader("Proxy-Client-IP");   
        }   
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
          ip = request.getHeader("WL-Proxy-Client-IP");   
        }   
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
          ip = request.getHeader("HTTP_CLIENT_IP");   
        }   
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
          ip = request.getHeader("HTTP_X_FORWARDED_FOR");   
        }   
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
          ip = request.getRemoteAddr();   
        }   
        return ip;   
    } 
}
