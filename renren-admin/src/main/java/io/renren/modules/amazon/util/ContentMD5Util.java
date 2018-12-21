package io.renren.modules.amazon.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ContentMD5Util {

    public static String computeContentMD5HeaderValue(FileInputStream fis)
            throws IOException, NoSuchAlgorithmException {

        DigestInputStream dis = new DigestInputStream(fis, MessageDigest.getInstance("MD5"));

        byte[] buffer = new byte[8192];
        while (dis.read(buffer) > 0);

        String md5Content = new String(
                org.apache.commons.codec.binary.Base64.encodeBase64(dis.getMessageDigest().digest()));

        // Effectively resets the stream to be beginning of the file via a FileChannel.
        fis.getChannel().position(0);

        return md5Content;
    }

    /**
     * Consume the stream and return its Base-64 encoded MD5 checksum.
     */
    public static String computeContentMD5Header(InputStream inputStream) {
        // Consume the stream to compute the MD5 as a side effect.
        DigestInputStream s;
        try {
            s = new DigestInputStream(inputStream,
                    MessageDigest.getInstance("MD5"));
            // drain the buffer, as the digest is computed as a side-effect
            byte[] buffer = new byte[8192];
            while(s.read(buffer) > 0);
            return new String(
                    org.apache.commons.codec.binary.Base64.encodeBase64(
                            s.getMessageDigest().digest()),
                    "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
