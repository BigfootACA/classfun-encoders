package cn.classfun.encoders;
import java.util.Arrays;
@SuppressWarnings({"unused","RedundantSuppression"})
public final class Base64 extends Coder{
	private static final char pad='=';
	static final private byte[]chars=new byte[128];
	static final private byte[]lookup=new byte[64];
	static{
		Arrays.fill(chars,(byte)-1);
		for(int i='Z';i>='A';i--)chars[i]=(byte)(i-'A');
		for(int i='z';i>='a';i--)chars[i]=(byte)(i-'a'+26);
		for(int i='9';i>='0';i--)chars[i]=(byte)(i-'0'+52);
		chars['+']=62;
		chars['/']=63;
		for(int i=0;i<=25;i++)lookup[i]=(byte)('A'+i);
		for(int i=26,j=0;i<=51;i++,j++)lookup[i]=(byte)('a'+j);
		for(int i=52,j=0;i<=61;i++,j++)lookup[i]=(byte)('0'+j);
		lookup[62]='+';
		lookup[63]='/';

	}
	private static boolean isWhiteSpace(byte octet){return(octet==0x20||octet==0xD||octet==0xA||octet==0x9);}
	private static boolean isPad(byte octet){return(octet==pad);}
	private static boolean notData(byte octet){return((0xFF&octet)>=chars.length||chars[(0xFF&octet)]==-1);}
	private static int removeWhiteSpace(byte[]data){
		if(data==null)return 0;
		int s=0;
		for(int i=0;i<data.length;i++)if(!isWhiteSpace(data[i]))data[s++]=data[i];
		return s;
	}
	@Override
	public byte[]decode(byte[]e){
		if(e==null)return null;
		int l=removeWhiteSpace(e);
		if(l%4!=0)return null;
		int q=(l/4);
		if(q==0)return new byte[0];
		byte b1,b2,b3,b4,d1,d2,d3,d4;
		int i=0,n=0,a=0;
		byte[]d=new byte[(q)*3];
		for(;i<q-1;i++){
			if(notData((d1=e[a++]))||notData((d2=e[a++]))||notData((d3=e[a++]))||notData((d4=e[a++])))return null;
			b1=chars[d1];
			b2=chars[d2];
			b3=chars[d3];
			b4=chars[d4];
			d[n++]=(byte)(b1<<2|b2>>4);
			d[n++]=(byte)(((b2&0xf)<<4)|((b3>>2)&0xf));
			d[n++]=(byte)(b3<<6|b4);
		}
		if(notData((d1=e[a++]))||notData((d2=e[a++])))return null;
		b1=chars[d1];
		b2=chars[d2];
		d3=e[a++];
		d4=e[a];
		if(notData((d3))||notData((d4))){
			if(isPad(d3)&&isPad(d4)){
				if((b2&0xf)!=0)return null;
				byte[]tmp=new byte[i*3+1];
				System.arraycopy(d,0,tmp,0,i*3);
				tmp[n]=(byte)(b1<<2|b2>>4);
				return tmp;
			}else if(!isPad(d3)&&isPad(d4)){
				b3=chars[d3];
				if((b3&0x3)!=0)return null;
				byte[]tmp=new byte[i*3+2];
				System.arraycopy(d,0,tmp,0,i*3);
				tmp[n++]=(byte)(b1<<2|b2>>4);
				tmp[n]=(byte)(((b2&0xf)<<4)|((b3>>2)&0xf));
				return tmp;
			}else return null;
		}else{
			b3=chars[d3];
			b4=chars[d4];
			d[n++]=(byte)(b1<<2|b2>>4);
			d[n++]=(byte)(((b2&0xf)<<4)|((b3>>2)&0xf));
			d[n]=(byte)(b3<<6|b4);
		}
		return d;
	}
	@Override
	public byte[]encode(byte[]b){
		if(b==null)return null;
		int l=b.length*8;
		if(l==0)return new byte[0];
		int f=l%24,t=l/24,q=f!=0?t+1:t,n=0,d=0;
		byte[]e=new byte[q*4];
		byte b1,b2,b3;
		for(int i=0;i<t;i++){
			b1=b[d++];
			b2=b[d++];
			b3=b[d++];
			e[n++]=lookup[((b1&-128)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0)];
			e[n++]=lookup[(((b2&-128)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0))|((byte)(b1&0x03)<<4)];
			e[n++]=lookup[((byte)(b2&0x0f)<<2)|(((b3&-128)==0)?(byte)(b3>>6):(byte)((b3)>>6^0xfc))];
			e[n++]=lookup[b3&0x3f];
		}
		if(f==8){
			b1=b[d];
			e[n++]=lookup[((b1&-128)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0)];
			e[n++]=lookup[(byte)(b1&0x03)<<4];
			e[n++]=pad;
			e[n]=pad;
		}else if(f==16){
			b1=b[d];
			b2=b[d+1];
			e[n++]=lookup[((b1&-128)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0)];
			e[n++]=lookup[(((b2&-128)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0))|((byte)(b1&0x03)<<4)];
			e[n++]=lookup[(byte)(b2&0x0f)<<2];
			e[n]=pad;
		}
		return e;
	}
}
