package com.example.javier.leermenuxml2;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Javier on 27/10/2016.
 */

public class RssParserSax {
    private URL rssUrl;

    public RssParserSax(String url){
        try {
            rssUrl = new URL(url);
        }catch (MalformedURLException e){ }
    }

    public List<Menu> cargar(){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            RssHandler handler = new RssHandler();
            parser.parse(getInputStream(),handler);
            return handler.getMenu();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getInputStream(){
        try{
            return rssUrl.openConnection().getInputStream();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
