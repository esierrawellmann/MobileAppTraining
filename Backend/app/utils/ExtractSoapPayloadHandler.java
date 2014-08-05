package utils;

import java.io.StringWriter;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import play.Logger;

//import com.digitalgeko.mobile.backend.util.log.Logger;

/**
 * Implementación de {@link javax.xml.ws.handler.LogicalHandler} utilizada para obtener los requests y response de clientes JAX-WS generados y de esta forma
 * almacenarlos en la bitácora.
 * 
 * @author clavarreda
 *
 */
public class ExtractSoapPayloadHandler implements LogicalHandler< LogicalMessageContext > {

	/**
	 * Flag indicando si debe obtener los mensajes de respuesta de los servicios.
	 */
	protected boolean shouldProcessIncomingMessages;
	
	/**
	 * Flag indicando si debe obtener los mensajes de solicitud a los servicios.
	 */
	protected boolean shouldProcessOutgoingMessages;

	protected MultiInstanceCache transformers;
	/**
	 * Inicializa un {@link utils.ExtractSoapPayloadHandler} y crea el {@link javax.xml.transform.Transformer} para no crearlo al momento
	 * de transformar un mensaje.
	 */
	public ExtractSoapPayloadHandler() {
		shouldProcessIncomingMessages = true;
		shouldProcessOutgoingMessages = true;
		
		transformers = new MultiInstanceCache( "Transformers", 20, -1 );
	}
	
	@Override
	public void close( MessageContext context ) {}

	@Override
	public boolean handleFault( LogicalMessageContext context ) {
		return processMessage( context );
	}

	@Override
	public boolean handleMessage( LogicalMessageContext context ) {
		return processMessage( context );
	}

	protected Transformer getTransformer() {
		Transformer transformer = ( Transformer ) transformers.get( "" );
		
		if ( transformer == null ) {
			TransformerFactory factory = TransformerFactory.newInstance();
//		factory.setAttribute("indent-number", 4);
			
			try {
				transformer = factory.newTransformer();
				transformer.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "yes" );
				transformer.setOutputProperty( OutputKeys.METHOD, "xml" );
				transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
			} catch ( TransformerConfigurationException e ) {
				Logger.error( e, "Unable to create Transformer" );
			}
		}

		return transformer;
	}
	
	protected void reuseTransformer( Transformer transformer ) {
		transformers.put( "", transformer );
	}
	
	/**
	 * Procesa el mensaje dentro del parámetro <code>context</code> y lo convierte a una cadena de caracteres si aplica.
	 * 
	 * @param context el objeto {@link javax.xml.ws.handler.LogicalMessageContext} que contiene la información del mensaje recibido.
	 * @return true.
	 */
	protected boolean processMessage( LogicalMessageContext context ) {
//		long time = System.currentTimeMillis();
		boolean outboundProperty = ( ( Boolean ) context.get( MessageContext.MESSAGE_OUTBOUND_PROPERTY ) ).booleanValue();
		
//		System.out.println( ( outboundProperty ? "Saliente " + shouldProcessOutgoingMessages() : "Entrante " + shouldProcessIncomingMessages() ) );

		if ( ( outboundProperty && shouldProcessOutgoingMessages() ) || ( !outboundProperty && shouldProcessIncomingMessages() ) ) {
//			LogicalMessage message = context.getMessage();

			try {
				StringWriter out = new StringWriter();
				
				Transformer transformer = getTransformer();
				
				transformer.transform( context.getMessage().getPayload(), new StreamResult( out ) );

				String value = out.toString();
				
				if ( outboundProperty && shouldProcessOutgoingMessages() )
					MessageHolder.defaultHolder.setInternalRequest( value );
				else if ( !outboundProperty && shouldProcessIncomingMessages() )
					MessageHolder.defaultHolder.setInternalResponse( value );
				
				reuseTransformer( transformer );
			} catch ( Exception e ) {
				//Logger.error( null, e );
			}
		}

//		System.out.println( System.currentTimeMillis() - time );
		
		return true;
	}

	/**
	 * Devuelve un flag indicando si debe obtener los mensajes de respuesta de los servicios.
	 * @return un flag indicando si debe obtener los mensajes de respuesta de los servicios.
	 */
	public boolean shouldProcessIncomingMessages() {
		return shouldProcessIncomingMessages;
	}
	
	/**
	 * Devuelve un flag indicando si debe obtener los mensajes de solicitud a los servicios.
	 * @return un flag indicando si debe obtener los mensajes de solicitud a los servicios.
	 */
	public boolean shouldProcessOutgoingMessages() {
		return shouldProcessOutgoingMessages;
	}

	/**
	 * Asigna un flag indicando si debe obtener los mensajes de respuesta de los servicios.
	 * @param shouldProcessIncomingMessages un flag indicando si debe obtener los mensajes de respuesta de los servicios.
	 */
	public void setShouldProcessIncomingMessages( boolean shouldProcessIncomingMessages ) {
		this.shouldProcessIncomingMessages = shouldProcessIncomingMessages;
	}

	/**
	 * Asigna un flag indicando si debe obtener los mensajes de solicitud a los servicios.
	 * @param shouldProcessOutgoingMessages un flag indicando si debe obtener los mensajes de solicitud a los servicios.
	 */
	public void setShouldProcessOutgoingMessages( boolean shouldProcessOutgoingMessages ) {
		this.shouldProcessOutgoingMessages = shouldProcessOutgoingMessages;
	}
}