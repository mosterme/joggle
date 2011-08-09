package joggle.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

public class SerializerTest extends TestCase {

	public void testCopy() throws IOException {
		String string = "^1234567890ß´qwertzuiopü+asdfghjklöä#<yxcvbnm,.-";
		ByteArrayInputStream bis = new ByteArrayInputStream(string.getBytes());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Serializer.copy(bis, bos);
		assertEquals(string, bos.toString());
	}

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
		String string = Serializer.toString(song);
		assertNotNull(string);
		assertEquals("Song{\"id\":null,\"type\":null,\"artist\":null,\"album\":null,\"track\":null,\"title\":null,\"genre\":null,\"artwork\":null,\"file\":null}",string);
	}

	public void testToString() {
		Song song = new Song();
		String string = Serializer.toString(song);
		assertNotNull(string);
		assertEquals("Song{\"id\":null,\"type\":null,\"artist\":null,\"album\":null,\"track\":null,\"title\":null,\"genre\":null,\"artwork\":null,\"file\":null}",string);
	}
}
