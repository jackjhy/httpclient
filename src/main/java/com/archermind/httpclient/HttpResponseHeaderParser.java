
// line 1 "HttpResponseHeaderParser.rl"
package com.archermind.httpclient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class HttpResponseHeaderParser {
public static final Charset UTF8 = Charset.forName("UTF-8");

	
// line 65 "HttpResponseHeaderParser.rl"

	
	
// line 4 "HttpResponseHeaderParser.java"
private static byte[] init__http_header_parser_actions_0()
{
	return new byte [] {
	    0,    1,    0,    1,    1,    1,    2,    1,    3,    1,    4,    2,
	    0,    3
	};
}

private static final byte _http_header_parser_actions[] = init__http_header_parser_actions_0();


private static byte[] init__http_header_parser_key_offsets_0()
{
	return new byte [] {
	    0,    0,    1,    2,    3,    4,    5,    7,   10,   12,   15,   18,
	   21,   29,   30,   32,   33,   35,   36,   38,   45
	};
}

private static final byte _http_header_parser_key_offsets[] = init__http_header_parser_key_offsets_0();


private static char[] init__http_header_parser_trans_keys_0()
{
	return new char [] {
	   72,   84,   84,   80,   47,   48,   57,   46,   48,   57,   48,   57,
	   32,   48,   57,   32,   48,   57,   32,   48,   57,   13,   32,   48,
	   57,   65,   90,   97,  122,   10,   13,   58,   58,   13,   32,   13,
	   10,   58,   13,   48,   57,   65,   90,   97,  122,   58,    0
	};
}

private static final char _http_header_parser_trans_keys[] = init__http_header_parser_trans_keys_0();


private static byte[] init__http_header_parser_single_lengths_0()
{
	return new byte [] {
	    0,    1,    1,    1,    1,    1,    0,    1,    0,    1,    1,    1,
	    2,    1,    2,    1,    2,    1,    2,    1,    1
	};
}

private static final byte _http_header_parser_single_lengths[] = init__http_header_parser_single_lengths_0();


private static byte[] init__http_header_parser_range_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    1,    1,    1,    1,    1,    1,
	    3,    0,    0,    0,    0,    0,    0,    3,    0
	};
}

private static final byte _http_header_parser_range_lengths[] = init__http_header_parser_range_lengths_0();


private static byte[] init__http_header_parser_index_offsets_0()
{
	return new byte [] {
	    0,    0,    2,    4,    6,    8,   10,   12,   15,   17,   20,   23,
	   26,   32,   34,   37,   39,   42,   44,   47,   52
	};
}

private static final byte _http_header_parser_index_offsets[] = init__http_header_parser_index_offsets_0();


private static byte[] init__http_header_parser_trans_targs_0()
{
	return new byte [] {
	    2,    0,    3,    0,    4,    0,    5,    0,    6,    0,    7,    0,
	    8,    7,    0,    9,    0,   10,    9,    0,   10,   11,    0,   12,
	   11,    0,   13,   12,   19,   19,   19,    0,   14,    0,   18,    0,
	   15,   16,   15,   13,   16,   17,   13,   17,   20,   16,   15,   13,
	   19,   19,   19,    0,   16,   15,    0
	};
}

private static final byte _http_header_parser_trans_targs[] = init__http_header_parser_trans_targs_0();


private static byte[] init__http_header_parser_trans_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,    0,
	    0,    0,    0,    0,    0,    9,    0,    0,    0,    1,    0,    5,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,    0,
	    1,    3,    0,   11,    1,    1,    7,    0,    0,    3,    0,    0,
	    0,    0,    0,    0,    3,    0,    0
	};
}

private static final byte _http_header_parser_trans_actions[] = init__http_header_parser_trans_actions_0();


static final int http_header_parser_start = 1;
static final int http_header_parser_first_final = 20;
static final int http_header_parser_error = 0;

static final int http_header_parser_en_main = 1;


// line 68 "HttpResponseHeaderParser.rl"
	
	public static void err(String msg) throws IOException{
    	throw new IOException(msg);
  	}
  	
  	public static void initHeader(HttpResponse resp, int headerLength) throws IOException {
    ByteBuffer bb = resp.buffer;
    /* required variables */
    byte[] data = bb.array();
    int p = 0;
    int pe = headerLength;
    int cs = 0;

    // variables used by actions in http_resp_parser machine above.
    int query_start = 0;
    int mark = 0;
    String field_name = "";

    
// line 108 "HttpResponseHeaderParser.java"
	{
	cs = http_header_parser_start;
	}

// line 87 "HttpResponseHeaderParser.rl"
    
// line 113 "HttpResponseHeaderParser.java"
	{
	int _klen;
	int _trans = 0;
	int _acts;
	int _nacts;
	int _keys;
	int _goto_targ = 0;

	_goto: while (true) {
	switch ( _goto_targ ) {
	case 0:
	if ( p == pe ) {
		_goto_targ = 4;
		continue _goto;
	}
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
case 1:
	_match: do {
	_keys = _http_header_parser_key_offsets[cs];
	_trans = _http_header_parser_index_offsets[cs];
	_klen = _http_header_parser_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( data[p] < _http_header_parser_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( data[p] > _http_header_parser_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _http_header_parser_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( data[p] < _http_header_parser_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( data[p] > _http_header_parser_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	cs = _http_header_parser_trans_targs[_trans];

	if ( _http_header_parser_trans_actions[_trans] != 0 ) {
		_acts = _http_header_parser_trans_actions[_trans];
		_nacts = (int) _http_header_parser_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _http_header_parser_actions[_acts++] )
			{
	case 0:
// line 13 "HttpResponseHeaderParser.rl"
	{mark = p; }
	break;
	case 1:
// line 15 "HttpResponseHeaderParser.rl"
	{ 
      		field_name = kw_lookup(data, mark, p);
      		if (field_name == null) {// not a known keyword
        		field_name = resp.extractRange(mark, p);
      		}
    	}
	break;
	case 2:
// line 22 "HttpResponseHeaderParser.rl"
	{
    		resp.statusRange = encodeRange(mark, p);
    	}
	break;
	case 3:
// line 26 "HttpResponseHeaderParser.rl"
	{
      		int value = encodeRange(mark, p);
      		resp.addField(field_name, value);
    	}
	break;
	case 4:
// line 31 "HttpResponseHeaderParser.rl"
	{
      		resp.versionRange = encodeRange(mark, p);
    	}
	break;
// line 224 "HttpResponseHeaderParser.java"
			}
		}
	}

case 2:
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
	if ( ++p != pe ) {
		_goto_targ = 1;
		continue _goto;
	}
case 4:
case 5:
	}
	break; }
	}

// line 88 "HttpResponseHeaderParser.rl"
    
    if (cs == http_header_parser_error) {
      throw new IOException("Malformed HTTP Header. p = " + p +", cs = " + cs);
    }
  }
  	
  	/**
	 * encode the start pos and length as ints;
 	*/
  	public static int encodeRange(int start, int end) {
    	return (start << 16) + end ;
  	}
  	
  
// line 244 "HttpResponseHeaderParser.java"
private static byte[] init__http_keywords_actions_0()
{
	return new byte [] {
	    0,    1,    0,    1,    1,    1,    2,    1,    3,    1,    4,    1,
	    5,    1,    6,    1,    7,    1,    8,    1,    9,    1,   10,    1,
	   11,    1,   12,    1,   13,    1,   14,    1,   15,    1,   16,    1,
	   17,    1,   18,    1,   19,    1,   20,    1,   21,    1,   22,    1,
	   23,    1,   24,    1,   25,    1,   26,    1,   27,    1,   28,    1,
	   29
	};
}

private static final byte _http_keywords_actions[] = init__http_keywords_actions_0();


private static short[] init__http_keywords_key_offsets_0()
{
	return new short [] {
	    0,    0,    6,    8,   10,   12,   14,   15,   17,   19,   21,   23,
	   25,   27,   29,   31,   33,   35,   39,   41,   43,   45,   46,   48,
	   50,   52,   54,   56,   58,   60,   62,   66,   68,   70,   72,   74,
	   76,   78,   80,   82,   84,   85,   95,   97,   99,  101,  103,  105,
	  107,  109,  115,  117,  119,  121,  123,  125,  127,  129,  131,  133,
	  135,  137,  139,  141,  143,  145,  147,  149,  150,  152,  154,  156,
	  158,  160,  162,  164,  166,  168,  170,  174,  176,  178,  180,  182,
	  184,  186,  188,  192,  194,  196,  197,  199,  201,  203,  205,  207,
	  209,  211,  213,  215,  217,  219,  221,  223,  225,  227,  231,  233,
	  235,  237,  239,  241,  242,  244,  246,  248,  250,  252,  254,  256,
	  258,  260,  262,  264,  266,  268,  270,  272,  274,  275,  277,  279,
	  281,  283,  285,  287,  289,  291,  293,  295,  297,  299,  303,  305,
	  307,  309,  311,  313,  315,  317,  318,  320,  322,  324,  326,  328,
	  330,  332,  334,  336,  338,  340,  342,  344,  346,  350,  352,  354,
	  356,  360,  362,  364,  366,  368,  370,  372,  373,  375,  377,  379,
	  381,  383,  385,  387,  389,  391,  393,  395,  397
	};
}

private static final short _http_keywords_key_offsets[] = init__http_keywords_key_offsets_0();


private static char[] init__http_keywords_trans_keys_0()
{
	return new char [] {
	   67,   71,   76,   99,  103,  108,   67,   99,   69,  101,   80,  112,
	   84,  116,   45,   82,  114,   65,   97,   78,  110,   71,  103,   69,
	  101,   83,  115,   69,  101,   76,  108,   79,  111,   87,  119,   65,
	   79,   97,  111,   67,   99,   72,  104,   69,  101,   45,   67,   99,
	   79,  111,   78,  110,   84,  116,   82,  114,   79,  111,   76,  108,
	   78,  110,   78,   84,  110,  116,   69,  101,   67,   99,   84,  116,
	   73,  105,   79,  111,   78,  110,   69,  101,   78,  110,   84,  116,
	   45,   69,   76,   77,   82,   84,  101,  108,  109,  114,  116,   78,
	  110,   67,   99,   79,  111,   68,  100,   73,  105,   78,  110,   71,
	  103,   65,   69,   79,   97,  101,  111,   78,  110,   71,  103,   85,
	  117,   65,   97,   71,  103,   69,  101,   78,  110,   71,  103,   84,
	  116,   72,  104,   67,   99,   65,   97,   84,  116,   73,  105,   79,
	  111,   78,  110,   68,  100,   53,   65,   97,   78,  110,   71,  103,
	   69,  101,   89,  121,   80,  112,   69,  101,   65,   97,   84,  116,
	   69,  101,   84,   88,  116,  120,   65,   97,   71,  103,   80,  112,
	   73,  105,   82,  114,   69,  101,   83,  115,   65,   79,   97,  111,
	   83,  115,   84,  116,   45,   77,  109,   79,  111,   68,  100,   73,
	  105,   70,  102,   73,  105,   69,  101,   68,  100,   67,   99,   65,
	   97,   84,  116,   73,  105,   79,  111,   78,  110,   82,  114,   65,
	   79,   97,  111,   71,  103,   77,  109,   65,   97,   88,  120,   89,
	  121,   45,   65,   97,   85,  117,   84,  116,   72,  104,   69,  101,
	   78,  110,   84,  116,   73,  105,   67,   99,   65,   97,   84,  116,
	   69,  101,   69,  101,   84,  116,   82,  114,   89,  121,   45,   65,
	   97,   70,  102,   84,  116,   69,  101,   82,  114,   69,  101,   82,
	  114,   86,  118,   69,  101,   82,  114,   82,  114,   65,   97,   73,
	   78,  105,  110,   76,  108,   69,  101,   82,  114,   83,  115,   70,
	  102,   69,  101,   82,  114,   45,   69,  101,   78,  110,   67,   99,
	   79,  111,   68,  100,   73,  105,   78,  110,   71,  103,   80,  112,
	   71,  103,   82,  114,   65,   97,   68,  100,   69,  101,   65,   73,
	   97,  105,   82,  114,   89,  121,   65,   97,   65,   87,   97,  119,
	   82,  114,   78,  110,   73,  105,   78,  110,   71,  103,   87,  119,
	   45,   65,   97,   85,  117,   84,  116,   72,  104,   69,  101,   78,
	  110,   84,  116,   73,  105,   67,   99,   65,   97,   84,  116,   69,
	  101,   65,   67,   68,   69,   76,   80,   82,   83,   84,   85,   86,
	   87,   97,   99,  100,  101,  108,  112,  114,  115,  116,  117,  118,
	  119,    0
	};
}

private static final char _http_keywords_trans_keys[] = init__http_keywords_trans_keys_0();


private static byte[] init__http_keywords_single_lengths_0()
{
	return new byte [] {
	    0,    6,    2,    2,    2,    2,    1,    2,    2,    2,    2,    2,
	    2,    2,    2,    2,    2,    4,    2,    2,    2,    1,    2,    2,
	    2,    2,    2,    2,    2,    2,    4,    2,    2,    2,    2,    2,
	    2,    2,    2,    2,    1,   10,    2,    2,    2,    2,    2,    2,
	    2,    6,    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
	    2,    2,    2,    2,    2,    2,    2,    1,    2,    2,    2,    2,
	    2,    2,    2,    2,    2,    2,    4,    2,    2,    2,    2,    2,
	    2,    2,    4,    2,    2,    1,    2,    2,    2,    2,    2,    2,
	    2,    2,    2,    2,    2,    2,    2,    2,    2,    4,    2,    2,
	    2,    2,    2,    1,    2,    2,    2,    2,    2,    2,    2,    2,
	    2,    2,    2,    2,    2,    2,    2,    2,    1,    2,    2,    2,
	    2,    2,    2,    2,    2,    2,    2,    2,    2,    4,    2,    2,
	    2,    2,    2,    2,    2,    1,    2,    2,    2,    2,    2,    2,
	    2,    2,    2,    2,    2,    2,    2,    2,    4,    2,    2,    2,
	    4,    2,    2,    2,    2,    2,    2,    1,    2,    2,    2,    2,
	    2,    2,    2,    2,    2,    2,    2,    2,   24
	};
}

private static final byte _http_keywords_single_lengths[] = init__http_keywords_single_lengths_0();


private static byte[] init__http_keywords_range_lengths_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0
	};
}

private static final byte _http_keywords_range_lengths[] = init__http_keywords_range_lengths_0();


private static short[] init__http_keywords_index_offsets_0()
{
	return new short [] {
	    0,    0,    7,   10,   13,   16,   19,   21,   24,   27,   30,   33,
	   36,   39,   42,   45,   48,   51,   56,   59,   62,   65,   67,   70,
	   73,   76,   79,   82,   85,   88,   91,   96,   99,  102,  105,  108,
	  111,  114,  117,  120,  123,  125,  136,  139,  142,  145,  148,  151,
	  154,  157,  164,  167,  170,  173,  176,  179,  182,  185,  188,  191,
	  194,  197,  200,  203,  206,  209,  212,  215,  217,  220,  223,  226,
	  229,  232,  235,  238,  241,  244,  247,  252,  255,  258,  261,  264,
	  267,  270,  273,  278,  281,  284,  286,  289,  292,  295,  298,  301,
	  304,  307,  310,  313,  316,  319,  322,  325,  328,  331,  336,  339,
	  342,  345,  348,  351,  353,  356,  359,  362,  365,  368,  371,  374,
	  377,  380,  383,  386,  389,  392,  395,  398,  401,  403,  406,  409,
	  412,  415,  418,  421,  424,  427,  430,  433,  436,  439,  444,  447,
	  450,  453,  456,  459,  462,  465,  467,  470,  473,  476,  479,  482,
	  485,  488,  491,  494,  497,  500,  503,  506,  509,  514,  517,  520,
	  523,  528,  531,  534,  537,  540,  543,  546,  548,  551,  554,  557,
	  560,  563,  566,  569,  572,  575,  578,  581,  584
	};
}

private static final short _http_keywords_index_offsets[] = init__http_keywords_index_offsets_0();


private static short[] init__http_keywords_trans_targs_0()
{
	return new short [] {
	    2,   13,   14,    2,   13,   14,    0,    3,    3,    0,    4,    4,
	    0,    5,    5,    0,    6,    6,    0,    7,    0,    8,    8,    0,
	    9,    9,    0,   10,   10,    0,   11,   11,    0,   12,   12,    0,
	  188,  188,    0,  188,  188,    0,   15,   15,    0,   16,   16,    0,
	  188,  188,    0,   18,   29,   18,   29,    0,   19,   19,    0,   20,
	   20,    0,   21,   21,    0,   22,    0,   23,   23,    0,   24,   24,
	    0,   25,   25,    0,   26,   26,    0,   27,   27,    0,   28,   28,
	    0,  188,  188,    0,   30,   30,    0,   31,   37,   31,   37,    0,
	   32,   32,    0,   33,   33,    0,   34,   34,    0,   35,   35,    0,
	   36,   36,    0,  188,  188,    0,   38,   38,    0,   39,   39,    0,
	   40,   40,    0,   41,    0,   42,   49,   66,   68,   72,   42,   49,
	   66,   68,   72,    0,   43,   43,    0,   44,   44,    0,   45,   45,
	    0,   46,   46,    0,   47,   47,    0,   48,   48,    0,  188,  188,
	    0,   50,   56,   60,   50,   56,   60,    0,   51,   51,    0,   52,
	   52,    0,   53,   53,    0,   54,   54,    0,   55,   55,    0,  188,
	  188,    0,   57,   57,    0,   58,   58,    0,   59,   59,    0,  188,
	  188,    0,   61,   61,    0,   62,   62,    0,   63,   63,    0,   64,
	   64,    0,   65,   65,    0,  188,  188,    0,   67,   67,    0,  188,
	    0,   69,   69,    0,   70,   70,    0,   71,   71,    0,  188,  188,
	    0,   73,   73,    0,   74,   74,    0,  188,  188,    0,   76,   76,
	    0,   77,   77,    0,  188,  188,    0,   79,   81,   79,   81,    0,
	   80,   80,    0,  188,  188,    0,   82,   82,    0,   83,   83,    0,
	   84,   84,    0,   85,   85,    0,  188,  188,    0,   87,   98,   87,
	   98,    0,   88,   88,    0,   89,   89,    0,   90,    0,   91,   91,
	    0,   92,   92,    0,   93,   93,    0,   94,   94,    0,   95,   95,
	    0,   96,   96,    0,   97,   97,    0,  188,  188,    0,   99,   99,
	    0,  100,  100,    0,  101,  101,    0,  102,  102,    0,  103,  103,
	    0,  188,  188,    0,  105,  105,    0,  106,  109,  106,  109,    0,
	  107,  107,    0,  108,  108,    0,  188,  188,    0,  110,  110,    0,
	  111,  111,    0,  112,    0,  113,  113,    0,  114,  114,    0,  115,
	  115,    0,  116,  116,    0,  117,  117,    0,  118,  118,    0,  119,
	  119,    0,  120,  120,    0,  121,  121,    0,  122,  122,    0,  123,
	  123,    0,  188,  188,    0,  125,  125,    0,  126,  126,    0,  127,
	  127,    0,  128,  128,    0,  129,    0,  130,  130,    0,  131,  131,
	    0,  132,  132,    0,  133,  133,    0,  188,  188,    0,  135,  135,
	    0,  136,  136,    0,  137,  137,    0,  138,  138,    0,  188,  188,
	    0,  140,  140,    0,  141,  141,    0,  142,  145,  142,  145,    0,
	  143,  143,    0,  144,  144,    0,  188,  188,    0,  146,  146,    0,
	  147,  147,    0,  148,  148,    0,  149,  149,    0,  150,    0,  151,
	  151,    0,  152,  152,    0,  153,  153,    0,  154,  154,    0,  155,
	  155,    0,  156,  156,    0,  157,  157,    0,  188,  188,    0,  159,
	  159,    0,  160,  160,    0,  161,  161,    0,  162,  162,    0,  163,
	  163,    0,  188,  188,    0,  165,  167,  165,  167,    0,  166,  166,
	    0,  188,  188,    0,  188,  188,    0,  169,  174,  169,  174,    0,
	  170,  170,    0,  171,  171,    0,  172,  172,    0,  173,  173,    0,
	  188,  188,    0,  175,  175,    0,  176,    0,  177,  177,    0,  178,
	  178,    0,  179,  179,    0,  180,  180,    0,  181,  181,    0,  182,
	  182,    0,  183,  183,    0,  184,  184,    0,  185,  185,    0,  186,
	  186,    0,  187,  187,    0,  188,  188,    0,    1,   17,   75,   78,
	   86,  104,  124,  134,  139,  158,  164,  168,    1,   17,   75,   78,
	   86,  104,  124,  134,  139,  158,  164,  168,    0,    0
	};
}

private static final short _http_keywords_trans_targs[] = init__http_keywords_trans_targs_0();


private static byte[] init__http_keywords_trans_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    5,    5,    0,    7,    7,    0,    0,    0,    0,    0,    0,    0,
	    9,    9,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,   11,   11,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,   13,   13,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,   15,   15,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,   17,
	   17,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,   19,
	   19,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,   21,   21,    0,    0,    0,    0,   23,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,   25,   25,
	    0,    0,    0,    0,    0,    0,    0,   27,   27,    0,    0,    0,
	    0,    0,    0,    0,   29,   29,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,   31,   31,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,   33,   33,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,   35,   35,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,   37,   37,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,   39,   39,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,   41,   41,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,   43,   43,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,   45,   45,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,   47,   47,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,   49,   49,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,   51,   51,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,   53,   53,    0,   55,   55,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	   57,   57,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,   59,   59,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0
	};
}

private static final byte _http_keywords_trans_actions[] = init__http_keywords_trans_actions_0();


private static byte[] init__http_keywords_to_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    1
	};
}

private static final byte _http_keywords_to_state_actions[] = init__http_keywords_to_state_actions_0();


private static byte[] init__http_keywords_from_state_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,    0,    0,    0,    0,    0,    0,    3
	};
}

private static final byte _http_keywords_from_state_actions[] = init__http_keywords_from_state_actions_0();


static final int http_keywords_start = 188;
static final int http_keywords_first_final = 188;
static final int http_keywords_error = 0;

static final int http_keywords_en_main = 188;


// line 136 "HttpResponseHeaderParser.rl"


  @SuppressWarnings("unused")
  public static String kw_lookup(byte[] data, int start, int len) {
    int ts, te, act;

    int p = start;
    int pe = start + len;
    int eof = pe;
    int cs;
    String kw = null;
    
// line 582 "HttpResponseHeaderParser.java"
	{
	cs = http_keywords_start;
	ts = -1;
	te = -1;
	act = 0;
	}

// line 148 "HttpResponseHeaderParser.rl"
    
// line 590 "HttpResponseHeaderParser.java"
	{
	int _klen;
	int _trans = 0;
	int _acts;
	int _nacts;
	int _keys;
	int _goto_targ = 0;

	_goto: while (true) {
	switch ( _goto_targ ) {
	case 0:
	if ( p == pe ) {
		_goto_targ = 4;
		continue _goto;
	}
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
case 1:
	_acts = _http_keywords_from_state_actions[cs];
	_nacts = (int) _http_keywords_actions[_acts++];
	while ( _nacts-- > 0 ) {
		switch ( _http_keywords_actions[_acts++] ) {
	case 1:
// line 1 "NONE"
	{ts = p;}
	break;
// line 619 "HttpResponseHeaderParser.java"
		}
	}

	_match: do {
	_keys = _http_keywords_key_offsets[cs];
	_trans = _http_keywords_index_offsets[cs];
	_klen = _http_keywords_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( data[p] < _http_keywords_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( data[p] > _http_keywords_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _http_keywords_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( data[p] < _http_keywords_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( data[p] > _http_keywords_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	cs = _http_keywords_trans_targs[_trans];

	if ( _http_keywords_trans_actions[_trans] != 0 ) {
		_acts = _http_keywords_trans_actions[_trans];
		_nacts = (int) _http_keywords_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _http_keywords_actions[_acts++] )
			{
	case 2:
// line 105 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Accept-Ranges";}}
	break;
	case 3:
// line 106 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Age";}}
	break;
	case 4:
// line 107 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Allow";}}
	break;
	case 5:
// line 108 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Cache-Control";}}
	break;
	case 6:
// line 109 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Connection";}}
	break;
	case 7:
// line 110 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Content-Encoding";}}
	break;
	case 8:
// line 111 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Content-Language";}}
	break;
	case 9:
// line 112 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Content-Length";}}
	break;
	case 10:
// line 113 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Content-Location";}}
	break;
	case 11:
// line 114 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Content-MD5";}}
	break;
	case 12:
// line 115 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Content-Range";}}
	break;
	case 13:
// line 116 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Content-Type";}}
	break;
	case 14:
// line 117 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Date";}}
	break;
	case 15:
// line 118 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "ETag";}}
	break;
	case 16:
// line 119 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Expires";}}
	break;
	case 17:
// line 120 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Last-Modified";}}
	break;
	case 18:
// line 121 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Location";}}
	break;
	case 19:
// line 122 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Pragma";}}
	break;
	case 20:
// line 123 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Proxy-Authenticate";}}
	break;
	case 21:
// line 124 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Retry-After";}}
	break;
	case 22:
// line 125 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Server";}}
	break;
	case 23:
// line 126 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Trailer";}}
	break;
	case 24:
// line 127 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Transfer-Encoding";}}
	break;
	case 25:
// line 128 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Upgrade";}}
	break;
	case 26:
// line 129 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Vary";}}
	break;
	case 27:
// line 130 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Via";}}
	break;
	case 28:
// line 131 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "Warning";}}
	break;
	case 29:
// line 132 "HttpResponseHeaderParser.rl"
	{te = p+1;{ kw = "WWW-Authenticate";}}
	break;
// line 793 "HttpResponseHeaderParser.java"
			}
		}
	}

case 2:
	_acts = _http_keywords_to_state_actions[cs];
	_nacts = (int) _http_keywords_actions[_acts++];
	while ( _nacts-- > 0 ) {
		switch ( _http_keywords_actions[_acts++] ) {
	case 0:
// line 1 "NONE"
	{ts = -1;}
	break;
// line 807 "HttpResponseHeaderParser.java"
		}
	}

	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
	if ( ++p != pe ) {
		_goto_targ = 1;
		continue _goto;
	}
case 4:
case 5:
	}
	break; }
	}

// line 149 "HttpResponseHeaderParser.rl"

    return kw;
  }
  	
  public static String crlf = "\r\n";
  public static void main(String args[]) throws Exception {
    /// Testing
    String s = 
      "HTTP/1.1 200 OK\r\n" +
      "Date: Mon, 22 Nov 2010 06:50:42 GMT\r\n" +
      "Content-Encoding: gzip\r\n" +
      "Content-Type: text/javascript;charset=UTF-8\r\n" +
      "Keep-Alive: 300\r\n" +
      "Connection: keep-alive\r\n\r\n";
    System.out.println("Input Resp: (" + s.length() + " bytes)");System.out.println(s);
    byte[] data = s.getBytes();
    int len = data.length;
    
    System.out.println("=============================================================");
    HttpResponse resp = new HttpResponse();
    resp.buffer = ByteBuffer.allocate(2048);
    resp.buffer.put(data);
    initHeader(resp, len);
    System.out.println(resp);
  }  	
  	
}