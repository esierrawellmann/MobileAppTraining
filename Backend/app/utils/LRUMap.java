package utils;

import java.util.HashMap;
import java.util.Map;

import play.Logger;

/**
 * Implementación de estrategia Least Recently Used utilizando un {@link java.util.Map} y una lista encadenada para mantener el órden y un rápido acceso a
 * los datos contenidos por esta estructura.
 *  
 * @author clavarreda
 */
public class LRUMap {
	/**
	 * {@link java.util.Map} conteniendo todos los elementos de la estructura para realizar búsquedas con mejor tiempo de respuesta.
	 */
	protected Map< Object, LRUItem > items;
	
	/**
	 * Instancia de {@link LRUMapListener} que será notificada cuando un elemento sea eliminado de la lista.
	 */
	protected LRUMapListener listener;

	/**
	 * HEAD de la lista encadenada.
	 */
	protected LRUItem head;
	
	/**
	 * TAIL de la lista encadenada.
	 */
	protected LRUItem tail;
	
	/**
	 * Intervalo utilizado para verificar el uso de los elementos de esta estructura.
	 */
	protected int cleaningInterval;

	/**
	 * Inicializa este {@link utils.LRUMap} con verificación de elementos no utilizados cada <code>300</code> segundos y con longitud default para el objeto {@link java.util.Map}.
	 */
	public LRUMap( String name ) {
		this( name, 10 );
	}
	
	/**
	 * Inicializa este {@link utils.LRUMap} con verificación de elementos no utilizados cada <code>300</code> segundos y con longitud de <code>initialSize</code> para el objeto {@link java.util.Map}.
	 * @param initialSize la longitud inicial del objeto {@link java.util.Map} que contiene los elementos.
	 */
	public LRUMap( String name, int initialSize ) {
		this( name, initialSize, 1200 );
	}
	
	/**
	 * Inicializa este {@link utils.LRUMap} con verificación de elementos no utilizados cada <code>cleaningInterval</code> segundos y con longitud de <code>initialSize</code> para el objeto {@link java.util.Map}.
	 * @param initialSize la longitud inicial del objeto {@link java.util.Map} que contiene los elementos.
	 * @param cleaningInterval el intervalo en segundos que se verificará si debe removerse algún elemento en la estructura.
	 */
	public LRUMap( String name, int initialSize, int cleaningInterval ) {
		this.cleaningInterval = cleaningInterval;
		
		head = new LRUItem();
		tail = new LRUItem();

		head.next = tail;
		tail.prev = head;
		
		items = new HashMap< Object, LRUItem >( initialSize );
		
		if ( cleaningInterval > 0 )
			new LRUCleaningThread( this, cleaningInterval );
	}

	public void cleanup() {
//		Logger.debug("cleaning...");
		
		synchronized ( this ) {
			LRUItem item = tail.prev;
			
			while ( item != head ) {

				if ( item.accessed ) {
					item.accessed = false;	
					item = item.prev;
				} else {
					LRUItem toRemove = item;

					item = item.prev;

					remove( toRemove );
				}
			}
		}

//		Logger.debug("after cleaning size {" + size() + "}");
	}
	
	/**
	 * Devuelve la cantidad de elementos almacenados en esta estructura.
	 * @return la cantidad de elementos almacenados en esta estructura.
	 */
	public int size() {
		return items.size();
	}

	/**
	 * Devuelve el valor del elemento identificado por <code>key</code>.
	 * @param key la llave que identifica al elemento a buscar.
	 * @return el valor del elemento identificado por <code>key</code>.
	 */
	public Object get( Object key ) {
		LRUItem item;
		
		synchronized ( this ) {
			item = items.get( key );
			
			if ( item != null ) {
				promote( item );
				
				return item.value;
			} else
				return null;
		}
	}

	/**
	 * Agrega el elemento <code>value</code> a esta estructura identificándolo con la llave <code>key</code>
	 * @param key la llave con la que se identificará al elemento.
	 * @param value el elemento que debe agregarse a la estructura.
	 */
	public void put( Object key, Object value ) {
		synchronized ( this ) {
			LRUItem item = items.get( key );
			
			if ( item == null ) {
				item = new LRUItem( key, value );
				
				items.put( key, item );
			} else {
				item.value = value;
				removeFromList( item );
			}
			
			insertAtHead( item );
		}
	}

	/**
	 * Mueve un elemento de esta estructura a la cabeza de la lista.
	 * @param item el elemento que debe colocar al inicio de la lista.
	 */
	protected void promote( LRUItem item ) {
		removeFromList( item );
		insertAtHead( item );
	}

	/**
	 * Remueve el elemento <code>item</code> de esta estructura e informa al objeto {@link LRUMapListener} sobre la eliminación del mismo.
	 * @param item el elemento que debe ser removido de la estructura.
	 */
	protected void remove( LRUItem item ) {
		removeFromList( item );
		
		item.prev = null;
		item.next = null;
		
		items.remove( item.key );
		
		if ( listener != null )
			listener.elementRemoved( item.key, item.value );
		
		item.key = null;
		item.value = null;
	}

	/**
	 * Extrae el elemento <code>item</code> de la lista para agregarlo en otra ubicación.
	 * @param item el elemento que debe extraerse de la estructura.
	 */
	protected void removeFromList( LRUItem item ) {
		LRUItem prev = item.prev;
		LRUItem next = item.next;
		
		prev.next = next;
		next.prev = prev;
	}

	/**
	 * Coloca el elemento <code>item</code> al inicio de la lista.
	 * @param item el elemento que debe colocarse al inicio de la lista.
	 */
	protected void insertAtHead( LRUItem item ) {
		LRUItem next = head.next;
		
		item.next = next;
		next.prev = item;
		
		item.prev = head;
		head.next = item;
		
		item.accessed = true;
	}

	/**
	 * Devuelve la instancia de {@link LRUMapListener} que debe ser notificada al momento de eliminar un elemento de la lista.
	 * @return la instancia de {@link LRUMapListener} que debe ser notificada al momento de eliminar un elemento de la lista.
	 */
	public LRUMapListener getListener() {
		return listener;
	}

	/**
	 * Asigna la instancia de {@link LRUMapListener} que debe ser notificada al momento de eliminar un elemento de la lista.
	 * @param listener la instancia de {@link LRUMapListener} que debe ser notificada al momento de eliminar un elemento de la lista.
	 */
	public void setListener( LRUMapListener listener ) {
		this.listener = listener;
	}

	/**
	 * Clase que encapsula la información de los elementos y sirve de eslabon dentro de la lista encadenada
	 * creada por la estructura {@link utils.LRUMap}.
	 * 
	 * @author clavarreda
	 */
	public static class LRUItem {
		/**
		 * La llave que identifica al valor almacenado.
		 */
		protected Object key;
		
		/**
		 * El valor que debe almacenarse en la estructura.
		 */
		protected Object value;
		
		/**
		 * Flag utilizado por el {@link Thread} que realiza la verificación de los elementos que no han sido accesados
		 * para marcarlo y de esta forma identificar los elementos que deben ser eliminados posteriormente.
		 */
		protected boolean accessed;

		/**
		 * Elemento anterior en la lista.
		 */
		LRUItem prev;
		
		/**
		 * Elemento posterior en la lista.
		 */
		LRUItem next;

		/**
		 * Inicializa un elemento vacío.
		 */
		public LRUItem() {}

		/**
		 * Inicializa un elemento con la llave especificada por el parámetro <code>key</code> y el valor especificado por 
		 * el parámetro <code>value</code>.
		 * @param key la llave que identifica al valor agregado.
		 * @param value el valor que debe ser almacenado.
		 */
		public LRUItem( Object key, Object value ) {
			this.key = key;
			this.value = value;
		}
	}

	/**
	 * {@link Thread} que se encarga de verificar los elementos que no han sido accesados dentro de la estructura, los marca y posteriormente
	 * los elimina de la estructura.
	 * 
	 * @author clavarreda
	 *
	 */
	public static class LRUCleaningThread extends Thread {

		/**
		 * La instancia de {@link utils.LRUMap} que debe verificar.
		 */
		protected LRUMap map;
		
		/**
		 * El intervalo que debe esperar antes de verificar por elementos sin accesar. Esta especificado en segundos.
		 */
		protected int cleaningInterval;
		
		/**
		 * Inicializa un {@link utils.LRUMap.LRUCleaningThread} especificando la estructra a revisar y el intervalo que tendrá para revisar
		 * los elementos.
		 * 
		 * @param map la estructura que debe revisar.
		 * @param cleaningInterval el intervalo de tiempo expresado en segundos que debe esperar para verificar los elementos.
		 */
		public LRUCleaningThread( LRUMap map, int cleaningInterval ) {
			this.map = map;
			this.cleaningInterval = cleaningInterval * 1000;
			setDaemon( true );
			
			start();
		}

		/**
		 * Realiza la verificación de los elementos que no han sido accesados dentro del intervalo de tiempo especificado por la propiedad
		 * {@link #cleaningInterval}. Marca los elementos y despues de cumplirse otro intervalo elimina los elementos que no fueron accesados.
		 */
		public void run() {
			for ( ; ; ) {
				try {
					Thread.sleep( cleaningInterval );
				} catch ( InterruptedException e ) {
					Logger.error(e, "Error al limpiar");
				}
				
				Logger.debug("cleaning...");
				
				map.cleanup();

				Logger.debug("after cleaning size {" + map.size() + "}");
			}
		}
	}
}