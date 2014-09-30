package bfd.hbase.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class testReadXml {
	   private static void parseElement(Element element){  
	          
	        String tagName = element.getNodeName();  
	        NodeList children = element.getChildNodes();  
	        System.out.print("<" + tagName);  
	          
	        //element 元素所构成的属性的NamedNodeMap对象，对其进行判断  
	        NamedNodeMap map = element.getAttributes();  
	          
	        if(null != map){  
	            for(int i = 0 ; i < map.getLength(); i++){  
	                  
	                //获取元素的每一个属性  
	                Attr attr = (Attr)map.item(i);  
	                  
	                String attrName = attr.getName();  
	                String attrValue = attr.getValue();  
	                System.out.print(" " + attrName + "=\"" + attrValue + "\"" );  
	            }  
	        }  
	        System.out.print(">");  
	        for(int i = 0; i < children.getLength(); i++ ){  
	            Node node = children.item(i);  
	            Short nodeType = node.getNodeType();  
	              
	            //是元素类性，进行递归  
	            if(nodeType == Node.ELEMENT_NODE){  
	                parseElement((Element)node);  
	            }  
	            //是文本类性，打印出来  
	            else if(nodeType ==Node.TEXT_NODE){  
	                System.out.print(node.getNodeValue());  
	            }  
	            //是注释，进行打印  
	            else if(nodeType ==Node.COMMENT_NODE){  
	                Comment comment = (Comment)node;  
	                String data = comment.getData();  
	                  
	                System.out.print("<!--" + data + "-->");  
	            }  
	              
	        }  
	        System.out.println("</" + tagName + ">");  
	          
	    }  
	   
	public static void main(String[] args){
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
			try {
				String hbaseConfig = "<?xml version=\"1.0\"?><?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?><configuration><property><name>hbase.master</name><value>192.168.48.11:60000</value></property><property><name>hbase.zookeeper.property.clientPort</name><value>2181</value></property><property><name>hbase.rootdir</name><value>hdfs://192.168.48.29:8020/hbase</value></property><property><name>hbase.cluster.distributed</name><value>true</value></property><property><name>hbase.zookeeper.quorum</name><value>192.168.48.12,192.168.48.13,192.168.48.14</value></property><property><name>zookeeper.session.timeout</name><value>300000</value></property><property><name>hbase.regionserver.lease.period</name><value>300000</value></property><property><name>zookeeper.znode.parent</name><value>/dp/hbase2</value></property><property><name>hbase.client.write.buffer</name><value>5242880</value></property></configuration>";
				Document doc = builder.parse(new BufferedInputStream(new ByteArrayInputStream(hbaseConfig.getBytes())));
//				Document doc = builder.parse(new BufferedInputStream(new FileInputStream(new File("conf/hbase-site.xml"))));
//				System.out.println(doc.getXmlVersion());
//				Document doc = builder.parse(new File("conf/hbase-site.xml"));
//				Element root = doc.getDocumentElement();
//				parseElement(root);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
