package cn.classfun.encoders;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
public final class URL extends Coder{
	@Override
	public byte[]encode(byte[]v){
		if(v==null)return null;
		final Charset c=Charset.defaultCharset();
		try{return URLEncoder.encode(new String(v,c),c.name()).getBytes(c);}
		catch(UnsupportedEncodingException e){return null;}
	}
	@Override
	public byte[]encode(String v,Charset c){
		if(v==null)return null;
		try{return URLEncoder.encode(v,c.name()).getBytes(Charset.defaultCharset());}
		catch(UnsupportedEncodingException e){return null;}
	}
	@Override
	public byte[]decode(byte[]v){
		if(v==null)return null;
		final Charset c=Charset.defaultCharset();
		try{return URLDecoder.decode(new String(v,c),c.name()).getBytes(c);}
		catch(UnsupportedEncodingException e){return null;}
	}
	@Override
	public byte[]decode(String v,Charset c){
		if(v==null)return null;
		try{return URLDecoder.decode(v,c.name()).getBytes();}
		catch(UnsupportedEncodingException e){return null;}
	}
}
