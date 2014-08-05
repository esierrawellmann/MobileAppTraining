package utils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

/**
 * Subclase de {@link javax.xml.ws.handler.HandlerResolver} que devuelve en el método {@link #getHandlerChain(javax.xml.ws.handler.PortInfo)} un objeto {@link java.util.List} con un elemento
 * del tipo {@link ExtractSoapPayloadHandler} que se encargará de obtener las tramas XML que son enviadas y recibidas a los servicios de la entidad
 * financiera cuando se utilice un cliente generado por JAX-WS.
 * 
 * @author clavarreda
 *
 */
public class JAXWSHandlerResolver implements HandlerResolver {

	/**
	 * Singleton de {@link utils.JAXWSHandlerResolver}.
	 */
	public static JAXWSHandlerResolver handlerResolver;
	
	/**
	 * Instancia de {@link ExtractSoapPayloadHandler} que se encargará de obtener las tramas XML enviadas y recibidas a los servicios de la entidad
	 * financiera cuando se utilice un cliente generado por JAX-WS.
	 */
	protected ExtractSoapEnvelopeHandler xmlHandler;
//	protected ExtractSoapPayloadHandler xmlHandler;

	/**
	 * Objeto {@link java.util.List} conteniendo solamente una instancia de {@link ExtractSoapPayloadHandler}.
	 */
	protected List< Handler > handlerChain;

	static {
		handlerResolver = new JAXWSHandlerResolver();
	}

	/**
	 * Inicializa un {@link utils.JAXWSHandlerResolver} y crea la información que será utilizada como handler chain.
	 */
	public JAXWSHandlerResolver() {
		handlerChain = new ArrayList< Handler >();
		
		xmlHandler = getMessageExtractorHandler();
		
		handlerChain.add( xmlHandler );
	}
	
	public void cleanup() {
//		xmlHandler.transformers.cleanup();
	}
	
	/**
	 * Crea la instancia de {@link ExtractSoapPayloadHandler} que será utilizada para obtener las tramas XML.
	 * 
	 * @return una nueva instancia de {@link ExtractSoapPayloadHandler} con que se obtendrán las tramas XML.
	 */
	protected ExtractSoapEnvelopeHandler getMessageExtractorHandler() {
		return new ExtractSoapEnvelopeHandler();
	}
	
//	protected ExtractSoapPayloadHandler getMessageExtractorHandler() {
//		return new ExtractSoapPayloadHandler();
//	}

	@Override
	public List< Handler > getHandlerChain( PortInfo portInfo ) {
		return handlerChain;
	}

	/**
	 * Le especifica a este {@link utils.JAXWSHandlerResolver} que su instancia de {@link ExtractSoapPayloadHandler} no debe procesar los mensajes
	 * de respuesta enviados por los servicios de la entidad financiera.
	 * @param shouldProcessIncomingMessages especifica a este {@link utils.JAXWSHandlerResolver} que su instancia de {@link ExtractSoapPayloadHandler} no debe procesar los mensajes
	 * de respuesta enviados por los servicios de la entidad financiera.
	 */
	public void setShouldProcessIncomingMessages( boolean shouldProcessIncomingMessages ) {
		xmlHandler.setShouldProcessIncomingMessages( shouldProcessIncomingMessages );
	}

	/**
	 * Le especifica a este {@link utils.JAXWSHandlerResolver} que su instancia de {@link ExtractSoapPayloadHandler} no debe procesar los mensajes
	 * de enviados a los servicios de la entidad financiera.
	 * @param shouldProcessOutgoingMessages especifica a este {@link utils.JAXWSHandlerResolver} que su instancia de {@link ExtractSoapPayloadHandler} no debe procesar los mensajes
	 * de enviados a los servicios de la entidad financiera.
	 */
	public void setShouldProcessOutgoingMessages( boolean shouldProcessOutgoingMessages ) {
		xmlHandler.setShouldProcessOutgoingMessages( shouldProcessOutgoingMessages );
	}
}