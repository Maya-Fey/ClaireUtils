package claire.util.crypto.hash.primitive;

import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.memory.Bits;

abstract class GrostlCore<State extends MerkleState<State, ?>, Hash extends GrostlCore<State, Hash>>
		 extends MerkleHash<State, Hash> {
	
	protected static final long[][] PCUBE = 
	{
		{
			0xc632f4a5f497a5c6L, 0xf86f978497eb84f8L,
			0xee5eb099b0c799eeL, 0xf67a8c8d8cf78df6L,
			0xffe8170d17e50dffL, 0xd60adcbddcb7bdd6L,
			0xde16c8b1c8a7b1deL, 0x916dfc54fc395491L,
			0x6090f050f0c05060L, 0x0207050305040302L,
			0xce2ee0a9e087a9ceL, 0x56d1877d87ac7d56L,
			0xe7cc2b192bd519e7L, 0xb513a662a67162b5L,
			0x4d7c31e6319ae64dL, 0xec59b59ab5c39aecL,
			0x8f40cf45cf05458fL, 0x1fa3bc9dbc3e9d1fL,
			0x8949c040c0094089L, 0xfa68928792ef87faL,
			0xefd03f153fc515efL, 0xb29426eb267febb2L,
			0x8ece40c94007c98eL, 0xfbe61d0b1ded0bfbL,
			0x416e2fec2f82ec41L, 0xb31aa967a97d67b3L,
			0x5f431cfd1cbefd5fL, 0x456025ea258aea45L,
			0x23f9dabfda46bf23L, 0x535102f702a6f753L,
			0xe445a196a1d396e4L, 0x9b76ed5bed2d5b9bL,
			0x75285dc25deac275L, 0xe1c5241c24d91ce1L,
			0x3dd4e9aee97aae3dL, 0x4cf2be6abe986a4cL,
			0x6c82ee5aeed85a6cL, 0x7ebdc341c3fc417eL,
			0xf5f3060206f102f5L, 0x8352d14fd11d4f83L,
			0x688ce45ce4d05c68L, 0x515607f407a2f451L,
			0xd18d5c345cb934d1L, 0xf9e1180818e908f9L,
			0xe24cae93aedf93e2L, 0xab3e9573954d73abL,
			0x6297f553f5c45362L, 0x2a6b413f41543f2aL,
			0x081c140c14100c08L, 0x9563f652f6315295L,
			0x46e9af65af8c6546L, 0x9d7fe25ee2215e9dL,
			0x3048782878602830L, 0x37cff8a1f86ea137L,
			0x0a1b110f11140f0aL, 0x2febc4b5c45eb52fL,
			0x0e151b091b1c090eL, 0x247e5a365a483624L,
			0x1badb69bb6369b1bL, 0xdf98473d47a53ddfL,
			0xcda76a266a8126cdL, 0x4ef5bb69bb9c694eL,
			0x7f334ccd4cfecd7fL, 0xea50ba9fbacf9feaL,
			0x123f2d1b2d241b12L, 0x1da4b99eb93a9e1dL,
			0x58c49c749cb07458L, 0x3446722e72682e34L,
			0x3641772d776c2d36L, 0xdc11cdb2cda3b2dcL,
			0xb49d29ee2973eeb4L, 0x5b4d16fb16b6fb5bL,
			0xa4a501f60153f6a4L, 0x76a1d74dd7ec4d76L,
			0xb714a361a37561b7L, 0x7d3449ce49face7dL,
			0x52df8d7b8da47b52L, 0xdd9f423e42a13eddL,
			0x5ecd937193bc715eL, 0x13b1a297a2269713L,
			0xa6a204f50457f5a6L, 0xb901b868b86968b9L,
			0x0000000000000000L, 0xc1b5742c74992cc1L,
			0x40e0a060a0806040L, 0xe3c2211f21dd1fe3L,
			0x793a43c843f2c879L, 0xb69a2ced2c77edb6L,
			0xd40dd9bed9b3bed4L, 0x8d47ca46ca01468dL,
			0x671770d970ced967L, 0x72afdd4bdde44b72L,
			0x94ed79de7933de94L, 0x98ff67d4672bd498L,
			0xb09323e8237be8b0L, 0x855bde4ade114a85L,
			0xbb06bd6bbd6d6bbbL, 0xc5bb7e2a7e912ac5L,
			0x4f7b34e5349ee54fL, 0xedd73a163ac116edL,
			0x86d254c55417c586L, 0x9af862d7622fd79aL,
			0x6699ff55ffcc5566L, 0x11b6a794a7229411L,
			0x8ac04acf4a0fcf8aL, 0xe9d9301030c910e9L,
			0x040e0a060a080604L, 0xfe66988198e781feL,
			0xa0ab0bf00b5bf0a0L, 0x78b4cc44ccf04478L,
			0x25f0d5bad54aba25L, 0x4b753ee33e96e34bL,
			0xa2ac0ef30e5ff3a2L, 0x5d4419fe19bafe5dL,
			0x80db5bc05b1bc080L, 0x0580858a850a8a05L,
			0x3fd3ecadec7ead3fL, 0x21fedfbcdf42bc21L,
			0x70a8d848d8e04870L, 0xf1fd0c040cf904f1L,
			0x63197adf7ac6df63L, 0x772f58c158eec177L,
			0xaf309f759f4575afL, 0x42e7a563a5846342L,
			0x2070503050403020L, 0xe5cb2e1a2ed11ae5L,
			0xfdef120e12e10efdL, 0xbf08b76db7656dbfL,
			0x8155d44cd4194c81L, 0x18243c143c301418L,
			0x26795f355f4c3526L, 0xc3b2712f719d2fc3L,
			0xbe8638e13867e1beL, 0x35c8fda2fd6aa235L,
			0x88c74fcc4f0bcc88L, 0x2e654b394b5c392eL,
			0x936af957f93d5793L, 0x55580df20daaf255L,
			0xfc619d829de382fcL, 0x7ab3c947c9f4477aL,
			0xc827efacef8bacc8L, 0xba8832e7326fe7baL,
			0x324f7d2b7d642b32L, 0xe642a495a4d795e6L,
			0xc03bfba0fb9ba0c0L, 0x19aab398b3329819L,
			0x9ef668d16827d19eL, 0xa322817f815d7fa3L,
			0x44eeaa66aa886644L, 0x54d6827e82a87e54L,
			0x3bdde6abe676ab3bL, 0x0b959e839e16830bL,
			0x8cc945ca4503ca8cL, 0xc7bc7b297b9529c7L,
			0x6b056ed36ed6d36bL, 0x286c443c44503c28L,
			0xa72c8b798b5579a7L, 0xbc813de23d63e2bcL,
			0x1631271d272c1d16L, 0xad379a769a4176adL,
			0xdb964d3b4dad3bdbL, 0x649efa56fac85664L,
			0x74a6d24ed2e84e74L, 0x1436221e22281e14L,
			0x92e476db763fdb92L, 0x0c121e0a1e180a0cL,
			0x48fcb46cb4906c48L, 0xb88f37e4376be4b8L,
			0x9f78e75de7255d9fL, 0xbd0fb26eb2616ebdL,
			0x43692aef2a86ef43L, 0xc435f1a6f193a6c4L,
			0x39dae3a8e372a839L, 0x31c6f7a4f762a431L,
			0xd38a593759bd37d3L, 0xf274868b86ff8bf2L,
			0xd583563256b132d5L, 0x8b4ec543c50d438bL,
			0x6e85eb59ebdc596eL, 0xda18c2b7c2afb7daL,
			0x018e8f8c8f028c01L, 0xb11dac64ac7964b1L,
			0x9cf16dd26d23d29cL, 0x49723be03b92e049L,
			0xd81fc7b4c7abb4d8L, 0xacb915fa1543faacL,
			0xf3fa090709fd07f3L, 0xcfa06f256f8525cfL,
			0xca20eaafea8fafcaL, 0xf47d898e89f38ef4L,
			0x476720e9208ee947L, 0x1038281828201810L,
			0x6f0b64d564ded56fL, 0xf073838883fb88f0L,
			0x4afbb16fb1946f4aL, 0x5cca967296b8725cL,
			0x38546c246c702438L, 0x575f08f108aef157L,
			0x732152c752e6c773L, 0x9764f351f3355197L,
			0xcbae6523658d23cbL, 0xa125847c84597ca1L,
			0xe857bf9cbfcb9ce8L, 0x3e5d6321637c213eL,
			0x96ea7cdd7c37dd96L, 0x611e7fdc7fc2dc61L,
			0x0d9c9186911a860dL, 0x0f9b9485941e850fL,
			0xe04bab90abdb90e0L, 0x7cbac642c6f8427cL,
			0x712657c457e2c471L, 0xcc29e5aae583aaccL,
			0x90e373d8733bd890L, 0x06090f050f0c0506L,
			0xf7f4030103f501f7L, 0x1c2a36123638121cL,
			0xc23cfea3fe9fa3c2L, 0x6a8be15fe1d45f6aL,
			0xaebe10f91047f9aeL, 0x69026bd06bd2d069L,
			0x17bfa891a82e9117L, 0x9971e858e8295899L,
			0x3a5369276974273aL, 0x27f7d0b9d04eb927L,
			0xd991483848a938d9L, 0xebde351335cd13ebL,
			0x2be5ceb3ce56b32bL, 0x2277553355443322L,
			0xd204d6bbd6bfbbd2L, 0xa9399070904970a9L,
			0x07878089800e8907L, 0x33c1f2a7f266a733L,
			0x2decc1b6c15ab62dL, 0x3c5a66226678223cL,
			0x15b8ad92ad2a9215L, 0xc9a96020608920c9L,
			0x875cdb49db154987L, 0xaab01aff1a4fffaaL,
			0x50d8887888a07850L, 0xa52b8e7a8e517aa5L,
			0x03898a8f8a068f03L, 0x594a13f813b2f859L,
			0x09929b809b128009L, 0x1a2339173934171aL,
			0x651075da75cada65L, 0xd784533153b531d7L,
			0x84d551c65113c684L, 0xd003d3b8d3bbb8d0L,
			0x82dc5ec35e1fc382L, 0x29e2cbb0cb52b029L,
			0x5ac3997799b4775aL, 0x1e2d3311333c111eL,
			0x7b3d46cb46f6cb7bL, 0xa8b71ffc1f4bfca8L,
			0x6d0c61d661dad66dL, 0x2c624e3a4e583a2cL
		},
			new long[256],
			new long[256],
			new long[256],
			new long[256],
			new long[256],
			new long[256],
			new long[256]
	};
		
	static
	{
		long T;
		for(int i = 0; i < 256; i++)
		{
			T = PCUBE[0][i];
			PCUBE[1][i] = Bits.rotateLeft(T, 56);
			PCUBE[2][i] = Bits.rotateLeft(T, 48);
			PCUBE[3][i] = Bits.rotateLeft(T, 40);
			PCUBE[4][i] = Bits.rotateLeft(T, 32);
			PCUBE[5][i] = Bits.rotateLeft(T, 24);
			PCUBE[6][i] = Bits.rotateLeft(T, 16);
			PCUBE[7][i] = Bits.rotateLeft(T,  8);
		}
	}

	public GrostlCore(int size, int out)
	{
		super(size, out);
	}

}
