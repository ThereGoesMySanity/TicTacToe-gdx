package tgms.ttt.server;

import java.util.HashMap;
import java.util.Set;

public class BiMap<K, V>  {
	private HashMap<K, V> keys;
	private HashMap<V, K> values;
	public BiMap() {
		keys = new HashMap<>();
		values = new HashMap<>();
	}
	
	public V get(K key) {
		return keys.get(key);
	}
	public K getKey(V value) {
		return values.get(value);
	}
	public void put(K key, V value) {
		keys.put(key, value);
		values.put(value, key);
	}
	public Set<K> keySet() {
		return keys.keySet();
	}
	public void remove(K key) {
		values.remove(keys.get(key));
		keys.remove(key);
	}
	public boolean containsKey(K key) {
		return keys.containsKey(key);
	}
}
