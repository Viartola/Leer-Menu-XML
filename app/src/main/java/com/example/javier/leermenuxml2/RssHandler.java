package com.example.javier.leermenuxml2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Javier on 27/10/2016.
 */
public class RssHandler extends DefaultHandler {

    private List<Menu> menu;
    private Menu menus;
    private StringBuilder texto;

    public List<Menu> getMenu() {
        return menu;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        menu = new ArrayList<Menu>();
        texto = new StringBuilder();


    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equals("Dia")) {
            menus = new Menu();

        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (this.menus != null) {
            texto.append(ch, start, length);
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (this.menus != null) {
            if (localName.equals("Plato_1")) {
                menus.setP1(texto.toString().trim().replace("\n", ""));
            } else if (localName.equals("Plato_2")) {
                menus.setP2(texto.toString().trim().replace("\n", ""));
            } else if (localName.equals("Postre")) {
                menus.setPostre(texto.toString().trim().replace("\n", ""));
            } else if (localName.equals("Fecha")) {
                menus.setFecha(texto.toString().trim().replace("\n", ""));
            } else if (localName.equals("Dia")) {
                menu.add(menus);
            }
            texto.setLength(0);
        }
    }
}
