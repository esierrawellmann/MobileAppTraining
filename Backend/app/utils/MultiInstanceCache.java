package utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Estructura utilizada para guardar múltiples instancias de una clase para ser reutilizadas.
 * @author clavarreda
 */
public class MultiInstanceCache {
	
	protected String name;
	
	/**
	 * Llaves generales con las que pueden almacenarse múltiples instancias de un elemento.
	 */
	protected String[] groupedKeys;
	
	/**
	 * Todos los elementos ordenados paralelamente con {@link #groupedKeys}.
	 */
	protected InstanceHolder[] uniqueKeys;
	
	/**
	 * HEAD de la lista encadenada de reutilización de {@link utils.MultiInstanceCache.InstanceHolder}.
	 */
	protected InstanceHolder head;

	/**
	 * TAIL de la lista encadenada de reutilización de {@link utils.MultiInstanceCache.InstanceHolder}.
	 */
	protected InstanceHolder tail;

	/**
	 * Tamaño de la estructura.
	 */
	protected int size;

	/**
	 * Cantidad de {@link utils.MultiInstanceCache.InstanceHolder} que pueden ser reutilizados.
	 */
	protected int holderCount;
	
	/**
	 * Tamaño inicial de la estructura que será utilizado posteriormente para
	 * expandirla.
	 */
	protected int initialSize;
	
	/**
	 * Intervalo expresado en segundos utilizado por el {@link Thread} verificador para eliminar elementos que
	 * no han sido utilizados.
	 */
	protected int cleaningInterval;

	/**
	 * Subclase de {@link java.util.Comparator} que será utilizada para ordenar los elementos.
	 */
	protected static KeyComparator comparator = new KeyComparator();
	
	/**
	 * Inicializa esta estructura con el tamaño inicial especificado por <code>initialSize</code> y un intervalo expresado en segundos
	 * especificado por <code>cleaningInterval</code>.
	 * @param initialSize el tamaño inicial de la estructura.
	 * @param cleaningInterval el intervalo de tiempo expresado en segundos que utilizará el {@link Thread} verificador para eliminar elementos
	 * que no han sido utilizados.
	 */
	public MultiInstanceCache( String name, int initialSize, int cleaningInterval ) {
		this.name = name;
		this.initialSize = initialSize;
		
		groupedKeys = new String[ initialSize ];
		uniqueKeys = new InstanceHolder[ initialSize ];
		
		head = new InstanceHolder();
		tail = new InstanceHolder();
		
		head.next = tail;
		tail.prev = head;
		
		if ( cleaningInterval > 0 )
			new CacheCleaningThread( this, cleaningInterval );
	}

	
	public void cleanup() {
		synchronized ( this ) {
			InstanceHolder[] array = uniqueKeys;
			for ( int i=array.length; --i >= 0; ) {
				InstanceHolder holder = array[ i ];
				
				if ( holder != null ) {
					if ( holder.accessed )
						holder.accessed = false;
					else
						remove( i );
				}
			}
		}
	}
	
	/**
	 * Devuelve una instancia de los objetos representados por la llave grupal <code>key</code>.
	 * @param key la llave grupal que identifica a los objetos del que se devolverá una instancia.
	 * @return una instancia de los objetos representados por la llave grupal <code>key</code> o <code>null</code> si no 
	 * existe ninguna instancia identificada por esa llave.
	 */
	public Object get( String key ) {
		return removeKey( key );
	}

	/**
	 * Agrega una instancia a la agrupación representada por <code>key</code>.
	 * @param key la llave grupal que representa un grupo de objetos.
	 * @param value el elemento que debe agregarse bajo la llave grupal especificada por <code>key</code>.
	 */
	public void put( String key, Object value ) {
		addKey( key, value );
	}

	/**
	 * Remueve un elemento de esta estructura identificado por la llave grupal <code>key</code> y lo devuelve.
	 * @param key la llave grupal que representa al grupo de objetos del que debe eliminarse una instancia.
	 * @return un elemento de esta estructura identificado por la llave grupal <code>key</code> y lo devuelve o <code>null</code>
	 * si no existe ningun elemento identificado con esa llave.
	 */
	public Object removeKey( String key ) {
		synchronized ( this ) {
			int index = Arrays.binarySearch( groupedKeys, key, comparator );
			
			if ( index >= 0 ) {
				return remove( index );
			} else
				return null;
		}
	}

	/**
	 * Remueve el elemento en posición <code>index</code> dentro de esta estructura.
	 * @param index la posición del elemento que debe ser removido.
	 * @return el elemento en posición <code>index</code> dentro de esta estructura.
	 */
	protected Object remove( int index ) {
		InstanceHolder holder = uniqueKeys[ index ];

		Object value = holder.instance;
		
		int init = index + 1;
		int length = groupedKeys.length - index - 1;
		
		System.arraycopy( groupedKeys, init, groupedKeys, index, length );
		System.arraycopy( uniqueKeys, init, uniqueKeys, index, length );

		int lastItem = groupedKeys.length - 1;
		
		groupedKeys[ lastItem ] = null;
		uniqueKeys[ lastItem ] = null;

		holder.instance = null;
		
		addHolderToQueue( holder );
		
		size --;
		
		return value;
	}

	/**
	 * Agrega una instancia de {@link utils.MultiInstanceCache.InstanceHolder} a la lista encadenada para ser reutilizados posteriormente
	 * minimizando la creación de objetos.
	 * @param holder la instancia de {@link utils.MultiInstanceCache.InstanceHolder} a la lista encadenada para ser reutilizados posteriormente.
	 */
	protected void addHolderToQueue( InstanceHolder holder ) {
		InstanceHolder prev = tail.prev;

		prev.next = holder;
		
		holder.prev = prev;
		holder.next = tail;
		
		tail.prev = holder;
		
		holderCount ++;
	}

	public int getSize() {
		return size;
	}
	
	/**
	 * Remueve una instancia de {@link utils.MultiInstanceCache.InstanceHolder} de la lista de objetos reutilizables y si no hay ningún elemento crea uno
	 * nuevo y le asigna como valor el parámetro <code>value</code>.
	 * @param value el objeto que debe estar contenido dentro del objeto {@link utils.MultiInstanceCache.InstanceHolder}.
	 * @return una instancia reutilizada o nueva de {@link utils.MultiInstanceCache.InstanceHolder} que contiene como valor <code>value</code>.
	 */
	protected InstanceHolder getHolderFromQueue( Object value ) {
		InstanceHolder holder;
		
		if ( holderCount > 0 ) {
			holder = tail.prev;
			InstanceHolder prev = holder.prev;

			prev.next = tail;
			tail.prev = prev;
			
			holder.prev = null;
			holder.next = null;
			
			holder.accessed = true;

			holderCount --;
		} else
			holder = new InstanceHolder();
		
		holder.instance = value;
		holder.accessed = true;
		
		return holder;
	}

	/**
	 * Agrega un nuevo elemento a esta estructura bajo la llave grupal <code>baseKey</code>.
	 * @param baseKey la llave bajo la cual quedará agrupado el valor especificado por <code>value</code>.
	 * @param value el valor que será agregado a la estructura.
	 */
	public void addKey( String baseKey, Object value ) {
		synchronized ( this ) {
			int index = Arrays.binarySearch( groupedKeys, baseKey, comparator );
			
			if ( index < 0 )
				index = -index;
			
			if ( index >= groupedKeys.length ) {
				groupedKeys = expandArray( String.class, groupedKeys );
				uniqueKeys = expandArray( InstanceHolder.class, uniqueKeys );
			} else {
				int init = index + 1;
				int length = groupedKeys.length - index - 1;
				
				System.arraycopy( groupedKeys, index, groupedKeys, init, length );
				System.arraycopy( uniqueKeys, index, uniqueKeys, init, length );
			}
			
			groupedKeys[ index ] = baseKey;
			uniqueKeys[ index ] = getHolderFromQueue( value );
			
			size ++;
		}
	}
	
	/**
	 * Expande un arreglo agregándole {@link #initialSize}/2 a la longitud actual del mismo.
	 * @param componentType el tipo de elemento que contiene el arrelgo.
	 * @param array el arreglo que debe ser expandido.
	 * @return una nueva instancia expandida del arreglo. 
	 */
	protected < E > E[] expandArray( Class< E > componentType, E[] array ) {
		int newLength = array.length + initialSize / 2;

		E[] newArray = ( E[] ) Array.newInstance( componentType, newLength );

		System.arraycopy( array, 0, newArray, 0, array.length );
		
		return newArray;
	}
	
	/**
	 * Clase que se encarga de especificar el órden de los elementos contenidos dentro de la estructura.
	 * 
	 * @author clavarreda
	 */
	protected static class KeyComparator implements Comparator< String > {
		@Override
		public int compare( String o1, String o2 ) {
			if ( o1 != null && o2 != null )
				return o1.compareTo( o2 );
			else
				return 1;
		}
	}

	/**
	 * Objeto utilizado para contener los valores de la estructura. Esta clase contiene propiedades útiles para la reutilización de los mismos
	 * y para realizar la limpieza de la estructura con los elementos que no han sido utilizados.
	 * 
	 * @author clavarreda
	 */
	protected static class InstanceHolder {
		/**
		 * Flag indicando si el objeto contenido en esta instancia ha sido accesado.
		 */
		protected boolean accessed;
		
		/**
		 * El valor contenido por este objeto.
		 */
		protected Object instance;
		
		/**
		 * Elemento posterior de la lista encadenada de reutilización.
		 */
		protected InstanceHolder next;
		
		/**
		 * Elemento anterior de la lista encadenada de reutilización.
		 */
		protected InstanceHolder prev;
	}

	/**
	 * {@link Thread} responsable de realizar la limpieza de los elementos que no han sido utilizados en esta estructura.
	 * 
	 * @author clavarreda
	 */
	protected static class CacheCleaningThread extends Thread {

		/**
		 * Estructura que este {@link Thread} estará verificando para eliminar los elementos que no han sido utilizados.
		 */
		protected MultiInstanceCache cache;
		
		/**
		 * Intervalo de tiempo expresado en segundos que este {@link Thread} esperará antes de verificar por elementos
		 * que no han sido utilizados.
		 */
		protected int cleaningInterval;

		/**
		 * Inicializa un {@link utils.MultiInstanceCache.CacheCleaningThread} que verificará la estructura especificada por <code>cache</code> cada <code>cleaningInterval</code>
		 * segundos.
		 * @param cache la estructura que debe verificarse por elementos que no han sido utilizados.
		 * @param cleaningInterval intervalo de tiempo expresado en segundos que este {@link Thread} esperará antes de verificar por elementos
		 * que no han sido utilizados.
		 */
		public CacheCleaningThread( MultiInstanceCache cache, int cleaningInterval ) {
			this.cache = cache;
			this.cleaningInterval = cleaningInterval * 1000;
			setDaemon( true );
			
			start();
		}

		/**
		 * Método encargado de verificar la estructura {@link #cache} cada {@link #cleaningInterval} segundos para marcar los elementos
		 * que no han sido utilizados y posteriormente eliminarlos cuando se cumpla otro intervalo.
		 */
		public void run() {
			for ( ; ; ) {
				try {
					Thread.sleep( cleaningInterval );
				} catch ( InterruptedException e ) {
					//Logger.error( null, e );
				}
				
				//Logger.info( "MultiInstanceCache[ {} ]: before cleaning size {}", cache.name, cache.size );
				
				synchronized ( cache ) {
					InstanceHolder[] array = cache.uniqueKeys;
					for ( int i=array.length; --i >= 0; ) {
						InstanceHolder holder = array[ i ];
						
						if ( holder != null ) {
							if ( holder.accessed )
								holder.accessed = false;
							else
								cache.remove( i );
						}
					}
				}

				//Logger.info( "MultiInstanceCache[ {} ]: after cleaning size {}", cache.name, cache.size );
			}
		}
	}
}