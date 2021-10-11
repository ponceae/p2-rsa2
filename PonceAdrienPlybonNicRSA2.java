import java.math.BigInteger;

/**
 * Project 2 code.
 * 
 * @author Adrien Ponce & Nic Plybon
 * @version 10/6/21
 */
public class PonceAdrienPlybonNicRSA2 {

	/**
	 * Main driver & code initialization.
	 * 
	 * @param args command-line args
	 */
	public static void main(String[] args)
	{
		// given
		BigInteger n = new BigInteger("715807601171794636036517537835702333816404394822135187");
		BigInteger e = new BigInteger("17");
		
		// calculate z from sage p & q ((p - 1) * (q - 1))
		BigInteger p = new BigInteger("832449947372296248363898157");
		BigInteger q = new BigInteger("859880649198556914917049791");
		BigInteger z = ((p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE))));
				
		// d = e^-1 mod z
		BigInteger d = new BigInteger(e.modInverse(z).toString());	
			
		// m = 3 and c = m^e mod N
		BigInteger m = new BigInteger("3");
		BigInteger c = new BigInteger(m.modPow(e, n).toString());
		
		// m = c^d mod N
		BigInteger mD = new BigInteger(c.modPow(d, n).toString());
				
		System.out.println("***************Part II output begins***************");
		System.out.println("N = 0x" + n.toString(16));
		System.out.println("p = 0x" + p.toString(16));
		System.out.println("q = 0x" + q.toString(16));
		System.out.println("Big-length of N = 0x" + Integer.toString(n.bitLength(), 16));		
		System.out.println("d = 0x" + d.toString(16));
		System.out.println("c = 0x" + c.toString(16));
		System.out.println("m2 = 0x" + mD.toString(16));
		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// given variables (e2 = 2^16 + 1)
		BigInteger p2 = new BigInteger("9299ff7061bff2d10d9b19653454453d6aed058b5331bea66a4b24e997a"
				+ "ca5b6408e050f8e53d99be3f81f563a46b1dbfb51ff739c98f9ad38de2e2d48fdc6ba125604e15f6"
				+ "b76a03e3d64c09bfc7f5c635f80ca55747cf7d0f4839da6ceeb2c43e329021c6fd91f030251ef951"
				+ "80226d50dc1b4395471c69d60a676b263d2bb9f59884914db356bc6fe58d00a999c605a8cf6d2469"
				+ "88531ffb79881501383dc092dcb97173c68d2548b7155006b31444cc7ab5c42b57128cd806d02c76"
				+ "0e391", 16);
		BigInteger q2 = new BigInteger("a3ec1a1cf64063fd97ad6a24e3509e6d04c36d5be75e3e567b4c713ee6b"
				+ "bbb3bbdcdfb6f89796a6e5d16624ccccff1d154a3b7e5d08a183be9b6e269031224f2d8e454541e2"
				+ "2b6a71754a25385b5fdb1b54c69840d6336129d1f02bc39c155a849dfbed96bac2588a50b316499b"
				+ "84430b6104008852ba2b0c09601ca94aa591ff9f31fc6a8df338019e3bb83b5cad61a3bc76dede4d"
				+ "1224aed8c9d7883f8bbcb677164a2138592973af4dbd92bd9e7fcfcc4bbbf19e295bbb6ed14dc5c6"
				+ "80311", 16);
		BigInteger e2 = new BigInteger("65537");
		
		// n = p * q
		BigInteger n2 = new BigInteger(p2.multiply(q2).toString());
		
		// z = (p - 1) * (q - 1)
		BigInteger z2 = ((p2.subtract(new BigInteger("1"))).multiply((q2.subtract(new BigInteger("1")))));
		
		// d = e^-1 mod z
		BigInteger d2 = new BigInteger(e2.modInverse(z2).toString());
		
		// c = m^e mod N while m = 3
		BigInteger c2 = new BigInteger(m.modPow(e2, n2).toString());		
		
		// measures how long decryption will take 1000 times (square & multiply)
		long startTime = System.currentTimeMillis();
		BigInteger mD2 = c2.modPow(d2, n2);
		for (int i = 0; i < 1000; i++) 
		{
			// mD2 = c^d mod N
			BigInteger timeDecrypt = c2.modPow(d2, n2); // variable used for timing
		}
		long endTime = System.currentTimeMillis();
		long seconds = (endTime - startTime) / 1000;
		double bps = (1000 * n2.bitLength()) / seconds;
		double kbps = bps / 1000;
		double gbps = kbps / 1000;
		
		// CRT calculations and timing
		long startTimeCRT = System.currentTimeMillis();
		BigInteger CRTmessage = BigInteger.ONE;
		for (int i = 0; i < 1000; i++)
		{
			BigInteger dp = d2.mod(p2.subtract(BigInteger.ONE));
			BigInteger qp = d2.mod(q2.subtract(BigInteger.ONE));
			BigInteger qinv = q2.modInverse(p2);
			BigInteger m1 = c2.modPow(dp, p2);
			BigInteger m2 = c2.modPow(qp, q2);
			BigInteger h = qinv.multiply(m1.subtract(m2).mod(p2));
			CRTmessage = m2.add(h.multiply(q2));
		}
		long endTimeCRT = System.currentTimeMillis();
		long CRTseconds = (endTimeCRT - startTimeCRT) / 1000;
		double CRTbps = (1000 * n2.bitLength()) / CRTseconds;
		double CRTkbps = CRTbps / 1000;
		double CRTgbps = CRTkbps / 1000;
		
		System.out.println("p = 0x" + p2.toString(16));
		System.out.println("q = 0x" + q2.toString(16));
		System.out.println("N = 0x" + n2.toString(16));
		System.out.println("Bit-length of N = 0x" + Integer.toString(n2.bitLength(), 16));
		System.out.println("e = 0x" + e2.toString(16));
		System.out.println("d = 0x" + d2.toString(16));
		System.out.println("c = 0x" + c2.toString(16));
		System.out.println("m2 = 0x" + mD2.toString(16));
		System.out.println("RSA Decryption took " + endTime + " milliseconds");
		System.out.println("RSA Decryption in terms of kilobits/second " + kbps);
		System.out.println("The speed is " + gbps + " gigabit/second Internet speed.");
		System.out.println("m2 = 0x" + CRTmessage.toString(16));
		System.out.println("CRT Decryption took " + endTimeCRT + " milliseconds");
		System.out.println("CRT Decryption in terms of kilobits/second " + CRTkbps);
		System.out.println("The speed is " + CRTgbps + " gigabit/second Internet speed.");

				
	}
}
