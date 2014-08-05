package utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Set;

import javax.xml.namespace.QName;
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
 * Implementación de {@link javax.xml.ws.handler.soap.SOAPHandler} utilizada para obtener los requests y response de clientes JAX-WS generados y de esta forma
 * almacenarlos en la bitácora.
 * 
 * @author clavarreda
 *
 */
public class ExtractSoapEnvelopeHandler implements SOAPHandler< SOAPMessageContext > {

	/**
	 * Flag indicando si debe obtener los mensajes de respuesta de los servicios.
	 */
	protected boolean shouldProcessIncomingMessages;
	
	/**
	 * Flag indicando si debe obtener los mensajes de solicitud a los servicios.
	 */
	protected boolean shouldProcessOutgoingMessages;

	/**
	 * Inicializa un {@link utils.ExtractSoapEnvelopeHandler}
	 */
	public ExtractSoapEnvelopeHandler() {
		shouldProcessIncomingMessages = true;
		shouldProcessOutgoingMessages = true;
	}
	
	@Override
	public void close( MessageContext context ) {}

	@Override
	public boolean handleFault( SOAPMessageContext context ) {
		return processMessage( context );
	}

	@Override
	public boolean handleMessage( SOAPMessageContext context ) {
		return processMessage( context );
	}

	/**
	 * Procesa el mensaje dentro del parámetro <code>context</code> y lo convierte a una cadena de caracteres si aplica.
	 * 
	 * @param context el objeto {@link javax.xml.ws.handler.soap.SOAPMessageContext} que contiene la información del mensaje recibido.
	 * @return true.
	 */
	protected boolean processMessage( SOAPMessageContext context ) {
//		long time = System.currentTimeMillis();
		boolean outboundProperty = ( ( Boolean ) context.get( MessageContext.MESSAGE_OUTBOUND_PROPERTY ) ).booleanValue();
//		System.out.println( ( outboundProperty ? "Saliente " + shouldProcessOutgoingMessages() : "Entrante " + shouldProcessIncomingMessages() ) );

		if ( ( outboundProperty && shouldProcessOutgoingMessages() ) || ( !outboundProperty && shouldProcessIncomingMessages() ) ) {

			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream( 100 );
				context.getMessage().writeTo( out );
				
				if ( outboundProperty && shouldProcessOutgoingMessages() )
					MessageHolder.defaultHolder.setInternalRequest( out.toString() );
				else if ( !outboundProperty && shouldProcessIncomingMessages() )
					MessageHolder.defaultHolder.setInternalResponse( out.toString() );

			} catch ( Exception e ) {
				Logger.error( e, "processMessage" );
			}
		}

//		System.out.println( ".......................... " + (System.currentTimeMillis() - time) );
		
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

	@Override
	public Set< QName > getHeaders() {
		return null;
	}
}