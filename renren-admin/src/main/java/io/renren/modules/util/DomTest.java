package io.renren.modules.util;


import org.dom4j.Document;
import org.dom4j.Element;

public class DomTest {
    public static void main(String[] args)throws Exception {
       Document node= DOMUtils.getXMLByFilePath("C:\\Users\\asus\\Desktop\\ProductsFeedSubmissionResult.xml");
        Element elemRoot=node.getRootElement();
       DOMUtils.getNodes(elemRoot);
    }
}
