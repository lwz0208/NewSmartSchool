package com.wust.newsmartschool.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
    /**
     * @param is 锟斤拷锟斤拷锟斤拷
     * @return String 锟斤拷锟截碉拷锟街凤拷锟斤拷
     * @throws IOException
     */
    public static String readFromStream(InputStream is) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = is.read(buffer))!=-1){
            baos.write(buffer, 0, len);
        }
        is.close();
        String result = baos.toString();
        baos.close();
        return result;
    }
}
