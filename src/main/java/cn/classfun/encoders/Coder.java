package cn.classfun.encoders;
import java.nio.charset.Charset;
@SuppressWarnings({"unused","RedundantSuppression"})
public abstract class Coder{
	public abstract byte[]encode(byte[]v);
	public abstract byte[]decode(byte[]v);
	public byte[]encode(String s){return encode(s.getBytes());}
	public byte[]decode(String s){return decode(s.getBytes());}
	public byte[]encode(String s,Charset c){return encode(s.getBytes(c));}
	public byte[]decode(String s,Charset c){return decode(s.getBytes(c));}
	public String encodeString(byte[]s){return new String(encode(s));}
	public String decodeString(byte[]s){return new String(decode(s));}
	public String encodeString(String s){return new String(encode(s));}
	public String decodeString(String s){return new String(decode(s));}
	public String encodeString(byte[]s,Charset c){return new String(encode(s),c);}
	public String decodeString(byte[]s,Charset c){return new String(decode(s),c);}
	public String encodeString(String s,Charset c){return new String(encode(s,c),c);}
	public String decodeString(String s,Charset c){return new String(decode(s,c),c);}
	public static Coder forName(String name){
		switch(name.toLowerCase()){
			case "base16":return new Base16();
			case "base32":return new Base32();
			case "base64":return new Base64();
			case "url":return new URL();
			default:throw new IllegalArgumentException();
		}
	}
}
