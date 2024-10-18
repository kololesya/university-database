package com.laba.solvd;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class UniversitySAXParser {
    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                boolean university = false;
                boolean student = false;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("university")) {
                        university = true;
                    } else if (qName.equalsIgnoreCase("student")) {
                        student = true;
                        System.out.println("Student ID: " + attributes.getValue("id"));
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (university) {
                        System.out.println("University Name: " + new String(ch, start, length));
                    } else if (student) {
                        System.out.println("Student Name: " + new String(ch, start, length));
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("university")) {
                        university = false;
                    } else if (qName.equalsIgnoreCase("student")) {
                        student = false;
                    }
                }
            };

            saxParser.parse("src/main/resources/university.xml", handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
