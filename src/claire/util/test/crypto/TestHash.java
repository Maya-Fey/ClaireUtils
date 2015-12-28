package claire.util.test.crypto;

import claire.util.crypto.hash.primitive.CRC;
import claire.util.crypto.hash.primitive.HAVAL;
import claire.util.crypto.hash.primitive.MD2;
import claire.util.crypto.hash.primitive.MD4;
import claire.util.crypto.hash.primitive.MD5;
import claire.util.crypto.hash.primitive.RIPEMD;
import claire.util.crypto.hash.primitive.SHA1;
import claire.util.crypto.hash.primitive.SHA2;
import claire.util.crypto.hash.primitive.Tiger1;
import claire.util.crypto.hash.primitive.Tiger2;
import claire.util.crypto.hash.primitive.Whirlpool;
import claire.util.crypto.hash.primitive.Whirlpool_0;
import claire.util.crypto.hash.primitive.Whirlpool_T;
import claire.util.encoding.CString;
import claire.util.logging.Log;

public class TestHash {
	
	private static final HashTest[] CRC8;
	private static final HashTest[] CRC16;
	private static final HashTest[] CRC32;
	
	private static final HashTest[] MD2;
	private static final HashTest[] MD4;
	private static final HashTest[] MD5;
	
	private static final HashTest[] SHA1;
	
	private static final HashTest[] RMD128;
	private static final HashTest[] RMD160;
	private static final HashTest[] RMD256;
	private static final HashTest[] RMD320;
	
	private static final HashTest[] SHA2_224;
	private static final HashTest[] SHA2_256;
	private static final HashTest[] SHA2_384;
	private static final HashTest[] SHA2_512;
	
	private static final HashTest[] TIGER1;
	private static final HashTest[] TIGER2;
	
	private static final HashTest[] WHIRLPOOL0;
	private static final HashTest[] WHIRLPOOLT;
	private static final HashTest[] WHIRLPOOL;
	
	private static final HashTest[] HAVAL128_3;
	private static final HashTest[] HAVAL128_4;
	private static final HashTest[] HAVAL128_5;
	private static final HashTest[] HAVAL160_3;
	private static final HashTest[] HAVAL160_4;
	private static final HashTest[] HAVAL160_5;
	private static final HashTest[] HAVAL192_3;
	private static final HashTest[] HAVAL192_4;
	private static final HashTest[] HAVAL192_5;
	private static final HashTest[] HAVAL224_3;
	private static final HashTest[] HAVAL224_4;
	private static final HashTest[] HAVAL224_5;
	private static final HashTest[] HAVAL256_3;
	private static final HashTest[] HAVAL256_4;
	private static final HashTest[] HAVAL256_5;
	
	static 
	{
		HashTest.TestBuilder md2 = new HashTest.TestBuilder(new MD2(), "MD2");
		md2.addTest(new CString(""), new CString("8350e5a3e24c153df2275c9f80692773"));
		md2.addTest(new CString("donuts"), new CString("4b6bcc39d1e9a81ae44c033ea030af97"));
		md2.addTest(new CString("123456789"), new CString("12bd4efdd922b5c8c7b773f26ef4e35f"));
		md2.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("bd05bac45b0e0be4b71399b026192c54"));
		md2.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("4213e7a64521b09c63b042c985294e97"));
		md2.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("faa94768611c0f8c3c0e535fe6002393"));
		MD2 = md2.finish();
		
		HashTest.TestBuilder md4 = new HashTest.TestBuilder(new MD4(), "MD4");
		md4.addTest(new CString(""), new CString("31d6cfe0d16ae931b73c59d7e0c089c0"));
		md4.addTest(new CString("donuts"), new CString("e80a0f47a97479feb2ee4fdaa21908dc"));
		md4.addTest(new CString("123456789"), new CString("2ae523785d0caf4d2fb557c12016185c"));
		md4.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("fb0486e0f4cf59d4d479973f30dcad90"));
		md4.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("e63cfaca116b3a5d2d350a0a3d6fb1c1"));
		md4.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("5ce2735ecc54326db46cb6712c0a834f"));
		MD4 = md4.finish();
		
		HashTest.TestBuilder md5 = new HashTest.TestBuilder(new MD5(), "MD5");
		md5.addTest(new CString(""), new CString("d41d8cd98f00b204e9800998ecf8427e"));
		md5.addTest(new CString("donuts"), new CString("6c493f3632cf8c85348ebd89d1e3cafa"));
		md5.addTest(new CString("123456789"), new CString("25f9e794323b453885f5181f1b624d0b"));
		md5.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("11a6dd8fb4c35bc39f35d4d7884edc31"));
		md5.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("d4eb428dd96bbb6dc283af19ba8266f4"));
		md5.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("2ea2c47dddc4aa49cace7fde595bff08"));
		MD5 = md5.finish();
		
		HashTest.TestBuilder sha1 = new HashTest.TestBuilder(new SHA1(), "SHA-1");
		sha1.addTest(new CString(""), new CString("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
		sha1.addTest(new CString("donuts"), new CString("5bd9f7248df0f3a6a86ab6c95f48787d546efa14"));
		sha1.addTest(new CString("123456789"), new CString("f7c3bc1d808e04732adf679965ccc34ca7ae3441"));
		sha1.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("7dc2c9f3768cd9d7ad7cd7b143d37b455c32ca5e"));
		sha1.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("de7290aa782de6c5348f904775602dee36401859"));
		sha1.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("7141d16f077273d3d6d3d9094e4708bb42b865c1"));
		SHA1 = sha1.finish();
		
		HashTest.TestBuilder rmd128 = new HashTest.TestBuilder(new RIPEMD(128), "RIPEMD-128");
		rmd128.addTest(new CString(""), new CString("cdf26213a150dc3ecb610f18f6b38b46"));
		rmd128.addTest(new CString("donuts"), new CString("bc6fc725976e5994b0f57ac34e7019b2"));
		rmd128.addTest(new CString("123456789"), new CString("1886db8acdcbfeab1e7ee3780400536f"));
		rmd128.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("201d360a7475533bd8fc7db938b70fea"));
		rmd128.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("557569988d25e52025c9b3817d3acd0e"));
		rmd128.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("8af7fb53065d9e8d8a226f4b0246877d"));	
		RMD128 = rmd128.finish();
		
		HashTest.TestBuilder rmd160 = new HashTest.TestBuilder(new RIPEMD(160), "RIPEMD-160");
		rmd160.addTest(new CString(""), new CString("9c1185a5c5e9fc54612808977ee8f548b2258d31"));
		rmd160.addTest(new CString("donuts"), new CString("f133880e4a7532d534638ce893121782e24f2507"));
		rmd160.addTest(new CString("123456789"), new CString("d3d0379126c1e5e0ba70ad6e5e53ff6aeab9f4fa"));
		rmd160.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("1136bb274bba4d7f144a13caf17dca184b6ace3a"));
		rmd160.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("728709d5a6d45c1ebb74ecce9d8f1f5239291c53"));
		rmd160.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("9a5a4d477d03a8074318bb008049e723737e14de"));	
		RMD160 = rmd160.finish();
		
		HashTest.TestBuilder rmd256 = new HashTest.TestBuilder(new RIPEMD(256), "RIPEMD-256");
		rmd256.addTest(new CString(""), new CString("02ba4c4e5f8ecd1877fc52d64d30e37a2d9774fb1e5d026380ae0168e3c5522d"));
		rmd256.addTest(new CString("donuts"), new CString("7c3217564ec9f861f1847a04b5b0aa920283f3dc29cfca1b17b5d7e299f19902"));
		rmd256.addTest(new CString("123456789"), new CString("6be43ff65dd40ea4f2ff4ad58a7c1acc7c8019137698945b16149eb95df244b7"));
		rmd256.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("01d843e94683fda40a8e60f046164f5e91d184141136d548128f9ac6f52336b4"));
		rmd256.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("39f1671c4662240cca981b32b6ac1d74b60224ae2c3db582d44ebc766ef51230"));
		rmd256.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("088c11e8f6fe21348488f7c5ea40ddc798ccb4067638e22ad20661f68f877fe9"));	
		RMD256 = rmd256.finish();
		
		HashTest.TestBuilder rmd320 = new HashTest.TestBuilder(new RIPEMD(320), "RIPEMD-320");
		rmd320.addTest(new CString(""), new CString("22d65d5661536cdc75c1fdf5c6de7b41b9f27325ebc61e8557177d705a0ec880151c3a32a00899b8"));
		rmd320.addTest(new CString("donuts"), new CString("10decd0926ca06109e7335565ff4d4ff49ec1db8c0e243f47d27b2ae322ba3c088b22011b57f949a"));
		rmd320.addTest(new CString("123456789"), new CString("7e36771775a8d279475d4fd76b0c8e412b6ad085a0002475a148923ccfa5d71492e12fa88eeaf1a9"));
		rmd320.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("e2d3a181be86b02f5024e1a6627e0359b3b238769e904e9a9c721774fa98ea86f48c87be86a4a2b6"));
		rmd320.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("9a82e55bf14a3e51a54ea25d499965728c77e51408d2dd037f2bf6927684ff8bf77fd367df544cee"));
		rmd320.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("1d1a3a6bb71bea87cee4f785d8d7596eeb0f8b82b9559306d5c8edda6a73731d5b3a35f703336b64"));	
		RMD320 = rmd320.finish();
		
		HashTest.TestBuilder sha2_224 = new HashTest.TestBuilder(new SHA2(224), "SHA-2/224");
		sha2_224.addTest(new CString(""), new CString("d14a028c2a3a2bc9476102bb288234c415a2b01f828ea62ac5b3e42f"));
		sha2_224.addTest(new CString("donuts"), new CString("584f7f205eb4049e22a249f646a3690aa714608c3bda4862995843a9"));
		sha2_224.addTest(new CString("123456789"), new CString("9b3e61bf29f17c75572fae2e86e17809a4513d07c8a18152acf34521"));
		sha2_224.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("fa2240c47480466df5e033207c9f1c451d1ec3008ddc04d6545e94ff"));
		sha2_224.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("ac3dea4d31870a53bc6b0074b297b54001b4dd0b3cb82e20ce729976"));
		sha2_224.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("1abbe2a4a1a2f6a20ee99d5961616a2a136abcc5fdd6485691345bb7"));	
		SHA2_224 = sha2_224.finish();
		
		HashTest.TestBuilder sha2_256 = new HashTest.TestBuilder(new SHA2(256), "SHA-2/256");
		sha2_256.addTest(new CString(""), new CString("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"));
		sha2_256.addTest(new CString("donuts"), new CString("0245178074fd042e19b7c3885b360fc21064b30e73f5626c7e3b005d048069c5"));
		sha2_256.addTest(new CString("123456789"), new CString("15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225"));
		sha2_256.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("ce9c907630d816f0b8e6f872144c4f2e61d6bd406a83a4fbefc13114701c2ce8"));
		sha2_256.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("e8dc3e79180afa926ec1862cc79f553f2ca896c30b30eb96afd8e59a78fa4485"));
		sha2_256.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("028fe06e15ef580a25ba63f78a21b222de57bcbe5f9b2ce2576393135ffa655d"));	
		SHA2_256 = sha2_256.finish();
		
		HashTest.TestBuilder sha2_384 = new HashTest.TestBuilder(new SHA2(384), "SHA-2/384");
		sha2_384.addTest(new CString(""), new CString("38b060a751ac96384cd9327eb1b1e36a21fdb71114be07434c0cc7bf63f6e1da274edebfe76f65fbd51ad2f14898b95b"));
		sha2_384.addTest(new CString("donuts"), new CString("de71f0b43e379150f206260dfaaecacf0b48f008a1364646d00f18699e41e384e729f39b954a74ee4cfe27f1967be293"));
		sha2_384.addTest(new CString("123456789"), new CString("eb455d56d2c1a69de64e832011f3393d45f3fa31d6842f21af92d2fe469c499da5e3179847334a18479c8d1dedea1be3"));
		sha2_384.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("89ecc72eac5476943f3d40bb2ddcf7d18f376636c35b15f3e2e1112f8926ec44a5bbba91f339de1e7cf7801cd0b63156"));
		sha2_384.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("0bac283056d189d672cf0f8a276ccda518641668f5849cd392f691035d8930c3af26468ea283a354246cceefbf7dc7b1"));
		sha2_384.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("3341181527e5391ce107aa5560e1d8b0b6398f3559834f1c2b7b73f5ca8bd17f649d2ff985f294d33c8b24deecdfbeac"));	
		SHA2_384 = sha2_384.finish();
		
		HashTest.TestBuilder sha2_512 = new HashTest.TestBuilder(new SHA2(512), "SHA-2/512");
		sha2_512.addTest(new CString(""), new CString("cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e"));
		sha2_512.addTest(new CString("donuts"), new CString("2e0c70974011518e2e02093c125687f362c6a0b72cfd4f729aeee3dbfe5072e6e7efd88e088c034d836e6d8ddbe85049f4d2bd5b07deb21701d78a140b5adbf6"));
		sha2_512.addTest(new CString("123456789"), new CString("d9e6762dd1c8eaf6d61b3c6192fc408d4d6d5f1176d0c29169bc24e71c3f274ad27fcd5811b313d681f7e55ec02d73d499c95455b6b5bb503acf574fba8ffe85"));
		sha2_512.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("afc7ebc160bef965691e38d6a1e985836a0465c1a272e4ab12bab3aab2a4007e08eb08ded5196080ae45f6a6df1987b256bce9c6de66dd773caafcc3f3d13d4c"));
		sha2_512.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("05f9e4c3ea6fe6e3f752ee155f1fd40ad45264e96f13448e6abef1db12c558d8f8b89705aaac38c2dcba3cd3501dc2cc47ab268c05044b7e0defb5741ee98ba4"));
		sha2_512.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("0cf7d93788898c6d6d9ac8298b687eed7811e8194acb487df03859076b41522a5cfaf9b9a6f3459fd7c149cb8b6c26b03725497d65cd1e924e4f27ace7f16830"));	
		SHA2_512 = sha2_512.finish();
		
		//Couldn't find an online version, so grabbed a few from wikipedia and NESSIE
		HashTest.TestBuilder tiger2 = new HashTest.TestBuilder(new Tiger2(), "Tiger-2");
		tiger2.addTest(new CString(""), new CString("4441be75f6018773c206c22745374b924aa8313fef919f41"));
		tiger2.addTest(new CString("The quick brown fox jumps over the lazy dog"), new CString("976abff8062a2e9dcea3a1ace966ed9c19cb85558b4976d8"));
		tiger2.addTest(new CString("The quick brown fox jumps over the lazy cog"), new CString("09c11330283a27efb51930aa7dc1ec624ff738a8d9bdd3df"));
		tiger2.addTest(new CString("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"), new CString("a6737f3997e8fbb63d20d2df88f86376b5fe2d5ce36646a9"));
		TIGER2 = tiger2.finish();
		
		//Again, grabbed off wikipedia
		HashTest.TestBuilder tiger1 = new HashTest.TestBuilder(new Tiger1(), "Tiger-1");
		tiger1.addTest(new CString(""), new CString("3293ac630c13f0245f92bbb1766e16167a4e58492dde73f3"));
		tiger1.addTest(new CString("The quick brown fox jumps over the lazy dog"), new CString("6d12a41e72e644f017b6f0e2f7b44c6285f06dd5d2c5b075"));
		tiger1.addTest(new CString("The quick brown fox jumps over the lazy cog"), new CString("a8f04b0f7201a0d728101c9d26525b31764a3493fcd8458f"));
		TIGER1 = tiger1.finish();
		
		HashTest.TestBuilder crc8 = new HashTest.TestBuilder(new CRC(8), "CRC-8");
		crc8.addTest(new CString(""), new CString("00"));
		crc8.addTest(new CString("donuts"), new CString("eb"));
		crc8.addTest(new CString("123456789"), new CString("f4"));
		crc8.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("77"));
		crc8.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("23"));
		CRC8 = crc8.finish();
		
		HashTest.TestBuilder crc16 = new HashTest.TestBuilder(new CRC(16), "CRC-16");
		crc16.addTest(new CString(""), new CString("0000"));
		crc16.addTest(new CString("donuts"), new CString("fab7"));
		crc16.addTest(new CString("123456789"), new CString("bb3d"));
		crc16.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("efc1"));
		crc16.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("2fc3"));
		crc16.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("3cbb"));	
		CRC16 = crc16.finish();
		
		HashTest.TestBuilder crc32 = new HashTest.TestBuilder(new CRC(32), "CRC-32");
		crc32.addTest(new CString(""), new CString("00000000"));
		crc32.addTest(new CString("donuts"), new CString("3259ba8c"));
		crc32.addTest(new CString("123456789"), new CString("cbf43926"));
		crc32.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("e94df84c"));
		crc32.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("8d213d48"));
		crc16.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("02d35313"));	
		CRC32 = crc32.finish();
		
		//Had to grab off of wiki
		HashTest.TestBuilder whirlpool0 = new HashTest.TestBuilder(new Whirlpool_0(), "Whirlpool-0");
		whirlpool0.addTest(new CString(""), new CString("b3e1ab6eaf640a34f784593f2074416accd3b8e62c620175fca0997b1ba2347339aa0d79e754c308209ea36811dfa40c1c32f1a2b9004725d987d3635165d3c8"));
		whirlpool0.addTest(new CString("The quick brown fox jumps over the lazy dog"), new CString("4f8f5cb531e3d49a61cf417cd133792ccfa501fd8da53ee368fed20e5fe0248c3a0b64f98a6533cee1da614c3a8ddec791ff05fee6d971d57c1348320f4eb42d"));
		whirlpool0.addTest(new CString("The quick brown fox jumps over the lazy eog"), new CString("228fbf76b2a93469d4b25929836a12b7d7f2a0803e43daba0c7fc38bc11c8f2a9416bbcf8ab8392eb2ab7bcb565a64ac50c26179164b26084a253caf2e012676"));
		WHIRLPOOL0 = whirlpool0.finish();
		
		HashTest.TestBuilder whirlpoolT = new HashTest.TestBuilder(new Whirlpool_T(), "Whirlpool-T");
		whirlpoolT.addTest(new CString(""), new CString("470f0409abaa446e49667d4ebe12a14387cedbd10dd17b8243cad550a089dc0feea7aa40f6c2aaab71c6ebd076e43c7cfca0ad32567897dcb5969861049a0f5a"));
		whirlpoolT.addTest(new CString("The quick brown fox jumps over the lazy dog"), new CString("3ccf8252d8bbb258460d9aa999c06ee38e67cb546cffcf48e91f700f6fc7c183ac8cc3d3096dd30a35b01f4620a1e3a20d79cd5168544d9e1b7cdf49970e87f1"));
		whirlpoolT.addTest(new CString("The quick brown fox jumps over the lazy eog"), new CString("c8c15d2a0e0de6e6885e8a7d9b8a9139746da299ad50158f5fa9eecddef744f91b8b83c617080d77cb4247b1e964c2959c507ab2db0f1f3bf3e3b299ca00cae3"));
		WHIRLPOOLT = whirlpoolT.finish();
		
		HashTest.TestBuilder whirlpool = new HashTest.TestBuilder(new Whirlpool(), "Whirlpool");
		whirlpool.addTest(new CString(""), new CString("19fa61d75522a4669b44e39c1d2e1726c530232130d407f89afee0964997f7a73e83be698b288febcf88e3e03c4f0757ea8964e59b63d93708b138cc42a66eb3"));
		whirlpool.addTest(new CString("donuts"), new CString("2d218cf3d1065c424144dfa6a9454a85f441dab1397d664607a49492839a641103aedb2121e5131f362fa21845378c7f62279b1b2b5a8c777830495eb165789d"));
		whirlpool.addTest(new CString("123456789"), new CString("21d5cb651222c347ea1284c0acf162000b4d3e34766f0d00312e3480f633088822809b6a54ba7edfa17e8fcb5713f8912ee3a218dd98d88c38bbf611b1b1ed2b"));
		whirlpool.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("60f0cfad58cf48d8f4ffee4dc3b5a68a19761f7e9857e82d17745840b85b92786173570a2a1d41b7e657826a1d7d4e779e72be6dbfbf7d32c649251e4e972ef3"));
		whirlpool.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("32f22d2b39cdcdf153e28c10f39bc33c01894d5de4301f982d0a17adcc379e8f53439023e6907c6f6b76432f48ac751445b55813cece12854e78bb9a201d5874"));
		whirlpool.addTest(new CString("This, is a bucket. Dear god. There's more. No. It contains the dying wish of every man here. Scout! You did collect everyone's dying with. You bet. Excellent. Gentlemen, synchronize your death watches. We have 70 hours to live, for most men no time at all. We are not most men. We are mercenaries. We have the resources, the will, to make these hours COUNT! The clock is ticking gentlemen. Lets begin. Our first dying wish is scouts. He's drawn a picture... of me getting hit by a car. I have something radiating off of me. Heh those are stink lines. That's why they caught him, because he smells. Yes I see. Here you've drawn me having sexual congress with the Eiffel tower. Eiffel tower having sexual congress with me. Both of us relaxing post coitus. I'm crying and the Eiffel tower has stink lines coming off of it. DID ANYONE BESIDES SCOUT PUT A CARD INTO THE BUCKET. Oh man, classic scout. Fantastic, this was a huge waste of my time. You did not read mine. Does it say you want the bucket. Yes! See you all in hell!"), new CString("1ac440bc29ecab55928ee0c57b4763e02c6cf3adc00bcffc38dd58d4342444f70d038e3c4515e2c3c1d5fe64dd63182e27ad28aedbffc4c61aaa8e1583367b04"));	
		WHIRLPOOL = whirlpool.finish();
		
		HashTest.TestBuilder haval128_3 = new HashTest.TestBuilder(new HAVAL(4, 3), "HAVAL-128, 3");
		haval128_3.addTest(new CString(""), new CString("c68f39913f901f3ddf44c707357a7d70"));
		haval128_3.addTest(new CString("donuts"), new CString("baa1053a35be2151d2f8ecc74c872188"));
		haval128_3.addTest(new CString("123456789"), new CString("f2f92d4e5ca6b92a5b5fc5ac822c39d2"));
		haval128_3.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("9ee9bf34eadc15837a3ac15b08c73c32"));
		haval128_3.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("745cf8bd910e9ca540bee102db24e86b"));		
		HAVAL128_3 = haval128_3.finish();
		
		HashTest.TestBuilder haval128_4 = new HashTest.TestBuilder(new HAVAL(4, 4), "HAVAL-128, 4");
		haval128_4.addTest(new CString(""), new CString("ee6bbf4d6a46a679b3a856c88538bb98"));
		haval128_4.addTest(new CString("donuts"), new CString("78f8a683e1279b9548da13ae64851ccc"));
		haval128_4.addTest(new CString("123456789"), new CString("52dfe2f3da02591061b02dbdc1510f1c"));
		haval128_4.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("81d4cfb1e3f75bb2e6e78bcdbceb3cf3"));
		haval128_4.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("9a3c4ec4ea148bbf0ba03540a3d93b6d"));		
		HAVAL128_4 = haval128_4.finish();
		
		HashTest.TestBuilder haval128_5 = new HashTest.TestBuilder(new HAVAL(4, 5), "HAVAL-128, 5");
		haval128_5.addTest(new CString(""), new CString("184b8482a0c050dca54b59c7f05bf5dd"));
		haval128_5.addTest(new CString("donuts"), new CString("7b83ed670d43f722540a13bb81198140"));
		haval128_5.addTest(new CString("123456789"), new CString("8aa1c1ca3a7e4f983654c4f689de6f8d"));
		haval128_5.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("ecffc46b100e05f5c503650ca7d0c8aa"));
		haval128_5.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("bc8c082c3222c564feaa25630bd2ae91"));		
		HAVAL128_5 = haval128_5.finish();
		
		HashTest.TestBuilder haval160_3 = new HashTest.TestBuilder(new HAVAL(5, 3), "HAVAL-160, 3");
		haval160_3.addTest(new CString(""), new CString("d353c3ae22a25401d257643836d7231a9a95f953"));
		haval160_3.addTest(new CString("donuts"), new CString("ca50d2cf8f490211d09778c10c010936dffc3749"));
		haval160_3.addTest(new CString("123456789"), new CString("39a83af3293cdac04de1df3d0be7a1f9d8aab923"));
		haval160_3.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("ee3c99feaa1d6faf7c600ac4006f05f02f659181"));
		haval160_3.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("2c44cc1ef80820ef746aac7fa54703a0ccd28354"));		
		HAVAL160_3 = haval160_3.finish();
		
		HashTest.TestBuilder haval160_4 = new HashTest.TestBuilder(new HAVAL(5, 4), "HAVAL-160, 4");
		haval160_4.addTest(new CString(""), new CString("1d33aae1be4146dbaaca0b6e70d7a11f10801525"));
		haval160_4.addTest(new CString("donuts"), new CString("83c79d36e62b4919cb15e8bbc14c1c49b898b325"));
		haval160_4.addTest(new CString("123456789"), new CString("b03439be6f2a3ebed93ac86846d029d76f62fd99"));
		haval160_4.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("18197403f509f50845d2f211f12db3e8812fd11e"));
		haval160_4.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("aca0b172a541741d4172666b09707b2d93ce2576"));		
		HAVAL160_4 = haval160_4.finish();
		
		HashTest.TestBuilder haval160_5 = new HashTest.TestBuilder(new HAVAL(5, 5), "HAVAL-160, 5");
		haval160_5.addTest(new CString(""), new CString("255158cfc1eed1a7be7c55ddd64d9790415b933b"));
		haval160_5.addTest(new CString("donuts"), new CString("81624e275515f381555d6aa423f372180b25226a"));
		haval160_5.addTest(new CString("123456789"), new CString("11f592b3a1a1a9c0f9c638c33b69e442d06c1d99"));
		haval160_5.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("82f11e9e00cb8f1af5f4f719613c10ff1eb2877d"));
		haval160_5.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("011984308ed118ff30bdeadb18e3caecce67bab5"));		
		HAVAL160_5 = haval160_5.finish();
		
		HashTest.TestBuilder haval192_3 = new HashTest.TestBuilder(new HAVAL(6, 3), "HAVAL-192, 3");
		haval192_3.addTest(new CString(""), new CString("e9c48d7903eaf2a91c5b350151efcb175c0fc82de2289a4e"));
		haval192_3.addTest(new CString("donuts"), new CString("b35b5cc60b6d72ed04cc2108f4ae2775edb30e7e9ca8cf63"));
		haval192_3.addTest(new CString("123456789"), new CString("6b92f078e73af2e0f9f049faa5016d32173a3d62d2f08554"));
		haval192_3.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("31b14e5c7158bd77d980f8389b8446fa4acdd62c87f484bf"));
		haval192_3.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("05d0c903928485b4589d75eb9d7a700f1b589733440c995e"));		
		HAVAL192_3 = haval192_3.finish();
		
		HashTest.TestBuilder haval192_4 = new HashTest.TestBuilder(new HAVAL(6, 4), "HAVAL-192, 4");
		haval192_4.addTest(new CString(""), new CString("4a8372945afa55c7dead800311272523ca19d42ea47b72da"));
		haval192_4.addTest(new CString("donuts"), new CString("e52de094921b0b46abe124840fe53830d58ffdee251652a4"));
		haval192_4.addTest(new CString("123456789"), new CString("a5c285ead0ff2f47c15c27b991c4a3a5007ba57137b18d07"));
		haval192_4.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("0fd35c5c64cb1ae521baf11a2ca9990bf0e5de535c9d1225"));
		haval192_4.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("99c85cd28c58a7732d070d6d3cd93e4bac277ac0b526fd03"));		
		HAVAL192_4 = haval192_4.finish();
		
		HashTest.TestBuilder haval192_5 = new HashTest.TestBuilder(new HAVAL(6, 5), "HAVAL-192, 5");
		haval192_5.addTest(new CString(""), new CString("4839d0626f95935e17ee2fc4509387bbe2cc46cb382ffe85"));
		haval192_5.addTest(new CString("donuts"), new CString("95a129f8205649d4fdc620cd0931269675c60a987ed3c29f"));
		haval192_5.addTest(new CString("123456789"), new CString("ec32312aa79775539675c9ba83d079ffc7ea498fa6173a46"));
		haval192_5.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("307dc9c5d805fb65ec59be0158c36b559e903dd308e043f2"));
		haval192_5.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("53a05212612769945fd71aaab25b770581c89f45e2f0d273"));		
		HAVAL192_5 = haval192_5.finish();
		
		HashTest.TestBuilder haval224_3 = new HashTest.TestBuilder(new HAVAL(7, 3), "HAVAL-224, 3");
		haval224_3.addTest(new CString(""), new CString("c5aae9d47bffcaaf84a8c6e7ccacd60a0dd1932be7b1a192b9214b6d"));
		haval224_3.addTest(new CString("donuts"), new CString("beb41485f4a458423a8037f8c6e10ff83d8d176c9abb0a1b44cae0f7"));
		haval224_3.addTest(new CString("123456789"), new CString("28e8cc65356b43acbed4dd70f11d0827f17c4442d323aaa0a0de285f"));
		haval224_3.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("cd1367d9dcf303ec08bbf7db7d43950d0a83b2596dd8b7012489a613"));
		haval224_3.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("7c8aad6a003632889f95152d802911b638c80783a9d49d183e9e3fc0"));		
		HAVAL224_3 = haval224_3.finish();
		
		HashTest.TestBuilder haval224_4 = new HashTest.TestBuilder(new HAVAL(7, 4), "HAVAL-224, 4");
		haval224_4.addTest(new CString(""), new CString("3e56243275b3b81561750550e36fcd676ad2f5dd9e15f2e89e6ed78e"));
		haval224_4.addTest(new CString("donuts"), new CString("205ff2a5abf1be068518b5b63949f82ac21b9f35816b0e3ffb7c1e1c"));
		haval224_4.addTest(new CString("123456789"), new CString("9a08d0cf1d52bb1ac22f6421cfb902e700c4c496b3e990f4606f577d"));
		haval224_4.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("f4c221a76209e924f334a537b5af36b308e251a2cb2db0ba5545fc6a"));
		haval224_4.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("5d84b9714372d8a870c6131e6c0fb9d87ec5f59e55da0272198b0a46"));		
		HAVAL224_4 = haval224_4.finish();
		
		HashTest.TestBuilder haval224_5 = new HashTest.TestBuilder(new HAVAL(7, 5), "HAVAL-224, 5");
		haval224_5.addTest(new CString(""), new CString("4a0513c032754f5582a758d35917ac9adf3854219b39e3ac77d1837e"));
		haval224_5.addTest(new CString("donuts"), new CString("5a95a339d50e759af22a3de4f89afc0c79c12e26e8df450b2612e9b8"));
		haval224_5.addTest(new CString("123456789"), new CString("2eaadfb8007d9a4d8d7f21182c2913d569f801b44d0920d4ce8a01f0"));
		haval224_5.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("4cc2c71f1f8142a76f1bec993750ffd60aa62db7a66780bfdd34ee13"));
		haval224_5.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("51829b9384d140efe77a303514710d18162807434c99421276040f01"));		
		HAVAL224_5 = haval224_5.finish();
		
		HashTest.TestBuilder haval256_3 = new HashTest.TestBuilder(new HAVAL(8, 3), "HAVAL-256, 3");
		haval256_3.addTest(new CString(""), new CString("4f6938531f0bc8991f62da7bbd6f7de3fad44562b8c6f4ebf146d5b4e46f7c17"));
		haval256_3.addTest(new CString("donuts"), new CString("566cae95df7c1e0e84923547acc74b7ecec112db3dbb8b8d71ca682c0e6c6cdf"));
		haval256_3.addTest(new CString("123456789"), new CString("63e8d0aeec87738f1e820294cbdf7961cd2246b3620b4bac81be0b9827d612c7"));
		haval256_3.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("8e52fe8bf55d6003f88ac47f486607e4d59626846ec6f7693e7f6e96c8fe766d"));
		haval256_3.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("170e1d66b85104735c89ba36d3b6d3334697d34de82049457ff4534266177b12"));		
		HAVAL256_3 = haval256_3.finish();
		
		HashTest.TestBuilder haval256_4 = new HashTest.TestBuilder(new HAVAL(8, 4), "HAVAL-256, 4");
		haval256_4.addTest(new CString(""), new CString("c92b2e23091e80e375dadce26982482d197b1a2521be82da819f8ca2c579b99b"));
		haval256_4.addTest(new CString("donuts"), new CString("f659c25a5a358d4c30c84b51fd2693f8da6a1411aa6e59d882febdb407b6d95f"));
		haval256_4.addTest(new CString("123456789"), new CString("ddc95df473dd169456484beb4b04edca83a5572d9d7eccd00092365ae4ef8d79"));
		haval256_4.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("a38170a15ca5ba3d750ad53f2750a96d44c27c1b1b326b2aa4391769765abbe3"));
		haval256_4.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("d8da8bec85f9e9fa506b789a82599839dd9fa8f13ee3802197719af98304aa86"));		
		HAVAL256_4 = haval256_4.finish();
		
		HashTest.TestBuilder haval256_5 = new HashTest.TestBuilder(new HAVAL(8, 5), "HAVAL-256, 5");
		haval256_5.addTest(new CString(""), new CString("be417bb4dd5cfb76c7126f4f8eeb1553a449039307b1a3cd451dbfdc0fbbe330"));
		haval256_5.addTest(new CString("donuts"), new CString("ca40f3ec0e1a7fe0d5e0b06e79d7c4fbc9d1fcb4ef44c404d3d6f600afdd0137"));
		haval256_5.addTest(new CString("123456789"), new CString("77fd61460db5f89defc9a9296fab68a1730ea6c9c0037a9793dac8492c0a953c"));
		haval256_5.addTest(new CString("The crazy ass fox jumps over a sleeping bear"), new CString("dc4654148cd250eaeaef1dc85ef11efe1eab1c5c7a9fc40dbd95b11f16542e56"));
		haval256_5.addTest(new CString("The crazy ass fox jumps over a sleeping beer"), new CString("598780e18003ef95ca09a7882649d9198fdded7ae67f5443f1d7eb3a04c2cb99"));		
		HAVAL256_5 = haval256_5.finish();
		
	}
	
	public static void runTestVectors(HashTest[] tests)
	{
		for(HashTest test : tests)
			test.run();
	}

	public static void runTests()
	{
		Log.info.println("Testing checksums...");
		Log.info.println("Testing CRC-8...");
		runTestVectors(CRC8);
		Log.info.println("Testing CRC-16...");
		runTestVectors(CRC16);
		Log.info.println("Testing CRC-32...");
		runTestVectors(CRC32);
		Log.info.println("Testing rivest hash functions...");
		Log.info.println("Testing MD2...");
		runTestVectors(MD2);
		Log.info.println("Testing MD4...");
		runTestVectors(MD4);
		Log.info.println("Testing MD5...");
		runTestVectors(MD5);
		Log.info.println("Testing SHA hash functions...");
		Log.info.println("Testing SHA-1...");
		runTestVectors(SHA1);
		Log.info.println("Testing SHA-2/224...");
		runTestVectors(SHA2_224);
		Log.info.println("Testing SHA-2/256...");
		runTestVectors(SHA2_256);
		Log.info.println("Testing SHA-2/384...");
		runTestVectors(SHA2_384);
		Log.info.println("Testing SHA-2/512...");
		runTestVectors(SHA2_512);
		Log.info.println("Testing RIPEMD-128...");
		runTestVectors(RMD128);
		Log.info.println("Testing RIPEMD-160...");
		runTestVectors(RMD160);
		Log.info.println("Testing RIPEMD-256...");
		runTestVectors(RMD256);
		Log.info.println("Testing RIPEMD-320...");
		runTestVectors(RMD320);
		Log.info.println("Testing Tiger-1...");
		runTestVectors(TIGER1);
		Log.info.println("Testing Tiger-2...");
		runTestVectors(TIGER2);
		Log.info.println("Testing Whirlpool-0...");
		runTestVectors(WHIRLPOOL0);
		Log.info.println("Testing Whirlpool-T...");
		runTestVectors(WHIRLPOOLT);
		Log.info.println("Testing Whirlpool...");
		runTestVectors(WHIRLPOOL);
		Log.info.println("Testing HAVAL-128, 3...");
		runTestVectors(HAVAL128_3);
		Log.info.println("Testing HAVAL-128, 4...");
		runTestVectors(HAVAL128_4);
		Log.info.println("Testing HAVAL-128, 5...");
		runTestVectors(HAVAL128_5);
		Log.info.println("Testing HAVAL-160, 3...");
		runTestVectors(HAVAL160_3);
		Log.info.println("Testing HAVAL-160, 4...");
		runTestVectors(HAVAL160_4);
		Log.info.println("Testing HAVAL-160, 5...");
		runTestVectors(HAVAL160_5);
		Log.info.println("Testing HAVAL-192, 3...");
		runTestVectors(HAVAL192_3);
		Log.info.println("Testing HAVAL-192, 4...");
		runTestVectors(HAVAL192_4);
		Log.info.println("Testing HAVAL-192, 5...");
		runTestVectors(	HAVAL192_5);
		Log.info.println("Testing HAVAL-224, 3...");
		runTestVectors(HAVAL224_3);
		Log.info.println("Testing HAVAL-224, 4...");
		runTestVectors(HAVAL224_4);
		Log.info.println("Testing HAVAL-224, 5...");
		runTestVectors(HAVAL224_5);
		Log.info.println("Testing HAVAL-256, 3...");
		runTestVectors(HAVAL256_3);
		Log.info.println("Testing HAVAL-256, 4...");
		runTestVectors(HAVAL256_4);
		Log.info.println("Testing HAVAL-256, 5...");
		runTestVectors(HAVAL256_5);
	}
}
