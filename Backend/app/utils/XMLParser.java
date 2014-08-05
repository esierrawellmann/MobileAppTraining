package utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.xml.sax.InputSource;

import com.sun.org.apache.xml.internal.security.utils.IgnoreAllErrorHandler;

import play.Logger;

public class XMLParser {

	private static MultiInstanceCache cache;

	private static HashMap< String, JAXBContext > contextCache;
	
	private static XMLInputFactory factory;

	private static int cacheSize;
	private static int cacheCleanInterval;

	protected static MultiInstanceCache getCache() {
		if ( cache == null ) {

			if ( getCacheSize() == 0 )
				setCacheSize( 30 );
			
			if ( getCacheCleanInterval() == 0 )
				setCacheCleanInterval( 3600 );
			
			cache = new MultiInstanceCache( "Unmarshallers", getCacheSize(), getCacheCleanInterval() );
			
			contextCache = new HashMap< String, JAXBContext >();
		}
		
		return cache;
	}
	
	public static void cleanup() {
		getCache().cleanup();
	}

	public static XMLInputFactory getInputFactory() {
		if ( factory == null ) 
			factory = XMLInputFactory.newInstance();
		
		return factory;
	}
	
	public static < T > T unmarshal( String xmlData, Class< T > objectClass ) {
//		Logger.info( xmlData + " " + objectClass.getName() );
		String key = objectClass.getName();
		
		javax.xml.bind.Unmarshaller unmarshaller = ( javax.xml.bind.Unmarshaller ) getCache().get( key );
		
		try {
			if ( unmarshaller == null )
				unmarshaller = getContext( objectClass ).createUnmarshaller();

			XMLStreamReader reader = getInputFactory().createXMLStreamReader( new StringReader( xmlData ) );
//			IgnoreCaseAttributeDelegate readerDelegate = new IgnoreCaseAttributeDelegate( reader );
			T data = ( T ) unmarshaller.unmarshal( reader );
//			T data = ( T ) unmarshaller.unmarshal( new StringReader( xmlData ) );
			getCache().put( key, unmarshaller );
			
			return data;
		} catch ( JAXBException e ) {
			Logger.error( e, "" );
		} catch ( XMLStreamException e ) {
			Logger.error( e, "" );
		}

		return null;
	}
	
	public static String marshal( Object object ) {
		String key = object.getClass().getName();
		
		Marshaller marshaller = ( Marshaller ) getCache().get( key );
		
		try {
			if ( marshaller == null ) {
				marshaller = getContext( object.getClass() ).createMarshaller();
//				marshaller.setProperty( "jaxb.formatted.output", true );
//				marshaller.setProperty("jaxb.fragment", Boolean.TRUE);
			}
			
			StringWriter out = new StringWriter();
			
			marshaller.marshal( object, out );
			
			getCache().put( key, marshaller );

			return out.toString();
		} catch ( JAXBException e ) {
			Logger.error( e, "" );
			return null;
		} 
	}
	
	protected static JAXBContext getContext( Class< ? > objectClass ) throws JAXBException {
		String className = objectClass.getName();

		JAXBContext context = contextCache.get( className );

		if ( context == null ) {
			context = JAXBContext.newInstance( objectClass );
			contextCache.put( className, context );
		}

		return context;
	}

	public static int getCacheSize() {
		return cacheSize;
	}

	public static void setCacheSize( int cacheSize ) {
		XMLParser.cacheSize = cacheSize;
	}

	public static int getCacheCleanInterval() {
		return cacheCleanInterval;
	}

	public static void setCacheCleanInterval( int cacheCleanInterval ) {
		XMLParser.cacheCleanInterval = cacheCleanInterval;
	}
	
//	public static class IgnoreCaseAttributeDelegate extends StreamReaderDelegate {
//		
//		public IgnoreCaseAttributeDelegate( XMLStreamReader reader ) {
//			super( reader );
//		}
//		
//		@Override
//		public String getAttributeLocalName( int index ) {
//			return super.getAttributeLocalName( index ).toLowerCase();
//		}
//		
//		@Override
//		public String getLocalName() {
//			return super.getLocalName().toLowerCase();
//		}
//		
//	}
}