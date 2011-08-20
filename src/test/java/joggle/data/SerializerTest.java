package joggle.data;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

/**
 * @author  $Author$
 * @version $Revision$
 */
public class SerializerTest extends TestCase {

	public void testDecode() {
		assertEquals("R H P", Serializer.decode("R%20H+P"));
		assertNull(Serializer.decode(null));
	}

	public void testHash() throws NoSuchAlgorithmException, IOException {
		String hash = Serializer.hash("{}");
		assertNotNull(hash);
		assertEquals("BF21A9E8FBC5A3846FB05B4FA0859E0917B2202F", hash);
	}

	public void testHex() {
		assertNull(Serializer.hex(null));
	}

	public void testNormalize() {
		assertEquals("BJORK", Serializer.normalize("Björk"));
	}

	public void testToJson() {
		Song song = new Song();
		String json = Serializer.toJson(song);
		assertNotNull(json);
		assertEquals("{}", json);
	}

	public void testToString() {
		Song song = new Song();
		String string1 = Serializer.toString(song);
		String string2 = song.toString();
		assertNotNull(string1);
		assertEquals("Song{\"id\":null,\"type\":null,\"artist\":null,\"album\":null,\"track\":null,\"title\":null,\"genre\":null,\"artwork\":null,\"file\":null,\"modified\":null}",string1);
		assertEquals(string1, string2);
	}
}
