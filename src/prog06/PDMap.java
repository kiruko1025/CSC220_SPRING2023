package prog06;

import prog02.PhoneDirectory;
import prog02.ArrayBasedPD;
import prog02.SortedPD;
import prog02.DirectoryEntry;
import java.util.Map;
import java.util.AbstractMap;
import java.util.Set;

public class PDMap extends AbstractMap<String, String> {
    PhoneDirectory pd = new SortedPD();

    public String put (String key, String value) {
	return pd.addOrChangeEntry(key, value);
    }

    @Override
    public boolean containsKey (Object key) {
	System.out.println("containsKey " + key);
	return pd.lookupEntry((String) key) != null;
    }

    @Override
    public String get (Object key) {
	System.out.println("get " + key);
	return pd.lookupEntry((String) key);
    }

    public String remove (String key) {
	return pd.removeEntry(key);
    }

    public Set<Map.Entry<String, String>> entrySet () { return null; }
}
