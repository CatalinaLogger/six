package com.maybe.sys.common.util;

import com.maybe.sys.common.dto.ImagesDto;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotoLinkUtil {
    // 地址
    private static final String URL = "http://www.tooopen.com/view/1439719.html";
    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "[a-zA-z]+://[^\\s]*";

    //获取HTML内容
    public String getHtml(String url)throws Exception{
        URL url1=new URL(url);
        URLConnection connection=url1.openConnection();
        InputStream in=connection.getInputStream();
        InputStreamReader isr=new InputStreamReader(in);
        BufferedReader br=new BufferedReader(isr);

        String line;
        StringBuffer sb=new StringBuffer();
        while((line=br.readLine())!=null){
            sb.append(line,0,line.length());
            sb.append('\n');
        }
        br.close();
        isr.close();
        in.close();
        return sb.toString();
    }

    //获取ImageUrl地址
    public List<String> getImageUrl(String html){
        Matcher matcher=Pattern.compile(IMGURL_REG).matcher(html);
        List<String>listimgurl=new ArrayList<String>();
        while (matcher.find()){
            listimgurl.add(matcher.group());
        }
        return listimgurl;
    }

    //获取ImageSrc地址
    public List<ImagesDto> getImageSrc(List<String> listimageurl){
        List<ImagesDto> images = new ArrayList<ImagesDto>();
        for (String image:listimageurl){
            Matcher matcher=Pattern.compile(IMGSRC_REG).matcher(image);
            while (matcher.find()){
                ImagesDto dto = new ImagesDto();
                dto.setWidth(300);
                dto.setHeight(400);
                dto.setLink(matcher.group().substring(0, matcher.group().length()-1));
                images.add(dto);
            }
        }
        return images;
    }
}
