package ru.example.rest.utils;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

public class JAXBUtils {

	public static final Logger logger = Logger.getLogger(JAXBUtils.class);
	
	public static <T> String getXmlStringFromBean(T bean) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(bean.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			StringWriter writer = new StringWriter();
			marshaller.marshal(bean, writer);
			return writer.toString();
		} catch (JAXBException e) {
			logger.error("Error while marshal", e);
			return "";
		}
	}

}
