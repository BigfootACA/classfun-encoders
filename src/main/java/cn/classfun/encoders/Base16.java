package cn.classfun.encoders;
import java.util.Arrays;
@SuppressWarnings({"unused","RedundantSuppression"})
public final class Base16 extends Coder{
	private static final byte[]chars={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	private static final byte[]table;
	static{
		table=new byte[128];
		Arrays.fill(table,(byte)0xFF);
		for (int i=0;i<chars.length;i++)table[chars[i]]=(byte)i;
	}
	@Override
	public byte[]decode(byte[]e){
		if(e==null)return null;
		byte[]d=new byte[e.length/2];
		int i=0;
		for(int a=0;a<e.length;){
			byte c0=table[e[a++]],c1=table[e[a++]];
			d[i++]=(byte)((c0<<4)|c1);
		}
		return d;
	}
	@Override
	public byte[]encode(byte[]b){
		if(b==null)return null;
		byte[]e=new byte[b.length*2];
		int i=0;
		for(byte v:b){
			e[i++]=chars[((v>>4)&0x0F)];
			e[i++]=chars[(v&0x0F)];
		}
		return e;
	}
}