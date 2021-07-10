package cn.classfun.encoders;
import java.util.Arrays;
@SuppressWarnings({"unused","RedundantSuppression"})
public final class Base32 extends Coder{
	private static final char PAD='=';
	private static final byte[]chars=new byte[128];
	private static final byte[]lookup=new byte[32];
	static{
		Arrays.fill(chars,(byte)-1);
		for(int i='Z';i>='A';i--)chars[i]=(byte)(i-'A');
		for(int i='7';i>='2';i--)chars[i]=(byte)(i-'2'+26);
		for(int i=0;i<=25;i++)lookup[i]=(byte)('A'+i);
		for(int i=26,j=0;i<=31;i++,j++)lookup[i]=(byte)('2'+j);
	}
	private static boolean isWhiteSpace(byte octet){return(octet==0x20||octet==0xD||octet==0xA||octet==0x9);}
	private static boolean isPad(byte octet){return(octet==PAD);}
	private static boolean notData(byte octet){return((0xFF&octet)>chars.length||chars[(0xFF&octet)]==-1);}
	private static int removeWhiteSpace(byte[]d){
		if(d==null)return 0;
		int s=0;
		for(int i=0;i<d.length;i++)if(!isWhiteSpace(d[i]))d[s++]=d[i];
		return s;
	}
	@Override
	public byte[]decode(byte[]c){
		if(c==null)return null;
		int len=removeWhiteSpace(c);
		if(len%8!=0)return null;
		int o=(len/8);
		if(o==0)return new byte[0];
		byte b1,b2,b3,b4,b5,b6,b7,b8,d1,d2,d3,d4,d5,d6,d7,d8;
		int i=0,e=0,d=0;
		byte[]t=new byte[o*5];
		for(;i<o-1;i++){
			if(
				notData((d1=c[d++]))||notData((d2=c[d++]))||
				notData((d3=c[d++]))||notData((d4=c[d++]))||
				notData((d5=c[d++]))||notData((d6=c[d++]))||
				notData((d7=c[d++]))||notData((d8=c[d++]))
			)return null;
			b1=chars[d1];
			b2=chars[d2];
			b3=chars[d3];
			b4=chars[d4];
			b5=chars[d5];
			b6=chars[d6];
			b7=chars[d7];
			b8=chars[d8];
			t[e++]=(byte)(b1<<3|b2>>2);
			t[e++]=(byte)(((b2&0xf)<<6)|(b3<<1)|((b4>>4)&0xf));
			t[e++]=(byte)(((b4&0xf)<<4)|((b5>>1)&0xf));
			t[e++]=(byte)(((b5&0xf)<<7)|(b6<<2)|((b7>>3)&0xf));
			t[e++]=(byte)(b7<<5|b8);
		}
		if(notData((d1=c[d++]))||notData((d2=c[d++])))return null;
		b1=chars[d1];
		b2=chars[d2];
		d3=c[d++];
		d4=c[d++];
		d5=c[d++];
		d6=c[d++];
		d7=c[d++];
		d8=c[d];
		if(notData((d3))||notData((d4))||notData((d5))||notData((d6))||notData((d7))||notData((d8))){
			if(isPad(d3)&&isPad(d4)&&isPad(d5)&&isPad(d6)&&isPad(d7)&&isPad(d8)){
				if((b2&0x3)!=0)return null;
				byte[]tmp=new byte[i*5+1];
				System.arraycopy(t,0,tmp,0,i*5);
				tmp[e]=(byte)(b1<<3|b2>>2);
				return tmp;
			}else if(!isPad(d3)&&!isPad(d4)&&isPad(d5)&&isPad(d6)&&isPad(d7)&&isPad(d8)){
				b3=chars[d3];
				b4=chars[d4];
				if((b4&0xf)!=0)return null;
				byte[]tmp=new byte[i*5+2];
				System.arraycopy(t,0,tmp,0,i*5);
				tmp[e++]=(byte)(b1<<3|b2>>2);
				tmp[e]=(byte)(((b2&0xf)<<6)|(b3<<1)|((b4>>4)&0xf));
				return tmp;
			}else if(!isPad(d3)&&!isPad(d4)&&!isPad(d5)&&isPad(d6)&&isPad(d7)&&isPad(d8)){
				b3=chars[d3];
				b4=chars[d4];
				b5=chars[d5];
				if((b5&0x1)!=0)return null;
				byte[]tmp=new byte[i*5+3];
				System.arraycopy(t,0,tmp,0,i*5);
				tmp[e++]=(byte)(b1<<3|b2>>2);
				tmp[e++]=(byte)(((b2&0xf)<<6)|(b3<<1)|((b4>>4)&0xf));
				tmp[e]=(byte)(((b4&0xf)<<4)|((b5>>1)&0xf));
				return tmp;
			}else if(!isPad(d3)&&!isPad(d4)&&!isPad(d5)&&!isPad(d6)&&!isPad(d7)&&isPad(d8)){
				b3=chars[d3];
				b4=chars[d4];
				b5=chars[d5];
				b6=chars[d6];
				b7=chars[d7];
				if((b7&0x7)!=0)return null;
				byte[]tmp=new byte[i*5+4];
				System.arraycopy(t,0,tmp,0,i*5);
				tmp[e++]=(byte)(b1<<3|b2>>2);
				tmp[e++]=(byte)(((b2&0xf)<<6)|(b3<<1)|((b4>>4)&0xf));
				tmp[e++]=(byte)(((b4&0xf)<<4)|((b5>>1)&0xf));
				tmp[e]=(byte)(((b5&0xf)<<7)|(b6<<2)|((b7>>3)&0xf));
				return tmp;
			}else return null;
		}else{
			b3=chars[d3];
			b4=chars[d4];
			b5=chars[d5];
			b6=chars[d6];
			b7=chars[d7];
			b8=chars[d8];
			t[e++]=(byte)(b1<<3|b2>>2);
			t[e++]=(byte)(((b2&0xf)<<6)|(b3<<1)|((b4>>4)&0xf));
			t[e++]=(byte)(((b4&0xf)<<4)|((b5>>1)&0xf));
			t[e++]=(byte)(((b5&0xf)<<7)|(b6<<2)|((b7>>3)&0xf));
			t[e]=(byte)(b7<<5|b8);
		}
		return t;
	}
	@Override
	public byte[]encode(byte[]b){
		if(b==null)return null;
		int l=b.length*8;
		if(l==0)return new byte[0];
		int f=l%40,q=l/40,o=f!=0?q+1:q;
		byte[]c=new byte[o*8];
		byte b1,b2,b3,b4,b5;
		int e=0,d=0;
		for(int i=0;i<q;i++){
			b1=b[d++];
			b2=b[d++];
			b3=b[d++];
			b4=b[d++];
			b5=b[d++];
			c[e++]=lookup[(((b1&-128)==0)?(byte)(b1>>3):(byte)((b1)>>3^0xe0))&0x1f];
			c[e++]=lookup[(((byte)(b1&0x07))<<2)|(((b2&-128)==0)?(byte)(b2>>6):(byte)((b2)>>6^0xfc))];
			c[e++]=lookup[(((b2&-128)==0)?(byte)(b2>>1):(byte)((b2)>>1^0xe0))&0x1f];
			c[e++]=lookup[((byte)(b2&0x01)<<4)|(((b3&-128)==0)?(byte)(b3>>4):(byte)((b3)>>4^0xf0))];
			c[e++]=lookup[((byte)(b3&0x0f)<<1)|(((b4&-128)==0)?(byte)(b4>>7):(byte)((b4)>>7^0xfe))];
			c[e++]=lookup[(((b4&-128)==0)?(byte)(b4>>2):(byte)((b4)>>2^0xe0))&0x1f];
			c[e++]=lookup[((byte)(b4&0x03)<<3)|(((b5&-128)==0)?(byte)(b5>>5):(byte)((b5)>>5^0xf8))];
			c[e++]=lookup[(byte)(b5&0x1f)&0x1f];
		}
		if(f==8){
			b1=b[d];
			c[e++]=lookup[(((b1&-128)==0)?(byte)(b1>>3):(byte)((b1)>>3^0xe0))&0x1f];
			c[e++]=lookup[(byte)(b1&0x07)<<2];
			c[e++]=PAD;
			c[e++]=PAD;
			c[e++]=PAD;
			c[e++]=PAD;
			c[e++]=PAD;
			c[e]=PAD;
		}else if(f==16){
			b1=b[d++];
			b2=b[d];
			c[e++]=lookup[(((b1&-128)==0)?(byte)(b1>>3):(byte)((b1)>>3^0xe0))&0x1f];
			c[e++]=lookup[((byte)(b1&0x07)<<2)|(((b2&-128)==0)?(byte)(b2>>6):(byte)((b2)>>6^0xfc))];
			c[e++]=lookup[(((b2&-128)==0)?(byte)(b2>>1):(byte)((b2)>>1^0xe0))&0x1f];
			c[e++]=lookup[((byte)(b2&0x01))<<4];
			c[e++]=PAD;
			c[e++]=PAD;
			c[e++]=PAD;
			c[e]=PAD;
		}else if(f==24){
			b1=b[d++];
			b2=b[d++];
			b3=b[d];
			c[e++]=lookup[(((b1&-128)==0)?(byte)(b1>>3):(byte)((b1)>>3^0xe0))&0x1f];
			c[e++]=lookup[(((byte)(b1&0x07))<<2)|(((b2&-128)==0)?(byte)(b2>>6):(byte)((b2)>>6^0xfc))];
			c[e++]=lookup[(((b2&-128)==0)?(byte)(b2>>1):(byte)((b2)>>1^0xe0))&0x1f];
			c[e++]=lookup[((byte)(b2&0x01)<<4)|(((b3&-128)==0)?(byte)(b3>>4):(byte)((b3)>>4^0xf0))];
			c[e++]=lookup[((byte)(b3&0x0f)<<1)];
			c[e++]=PAD;
			c[e++]=PAD;
			c[e]=PAD;
		}else if(f==32){
			b1=b[d++];
			b2=b[d++];
			b3=b[d++];
			b4=b[d];
			c[e++]=lookup[(((b1&-128)==0)?(byte)(b1>>3):(byte)((b1)>>3^0xe0))&0x1f];
			c[e++]=lookup[((byte)(b1&0x07)<<2)|(((b2&-128)==0)?(byte)(b2>>6):(byte)((b2)>>6^0xfc))];
			c[e++]=lookup[(((b2&-128)==0)?(byte)(b2>>1):(byte)((b2)>>1^0xe0))&0x1f];
			c[e++]=lookup[((byte)(b2&0x01)<<4)|(((b3&-128)==0)?(byte)(b3>>4):(byte)((b3)>>4^0xf0))];
			c[e++]=lookup[((byte)(b3&0x0f)<<1)|(((b4&-128)==0)?(byte)(b4>>7):(byte)((b4)>>7^0xfe))];
			c[e++]=lookup[(((b4&-128)==0)?(byte)(b4>>2):(byte)((b4)>>2^0xe0))&0x1f];
			c[e++]=lookup[((byte)(b4&0x03)<<3)];
			c[e]=PAD;
		}
		return c;
	}
}