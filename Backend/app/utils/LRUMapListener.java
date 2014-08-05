package utils;

/**
 * Interfaz utilizada por {@link LRUMap} para notificar cuando un elemento es eliminado de esa estructura.
 * 
 * @author clavarreda
 */
public interface LRUMapListener {

	/**
	 * Llamado cuando un elemento es eliminado de la estructura {@link LRUMap} al que esta asignado este {@link utils.LRUMapListener}.
	 * @param key la llave del elemento eliminado.
	 * @param value el valor que fué removido de la estructura.
	 */
	public void elementRemoved(Object key, Object value);

}