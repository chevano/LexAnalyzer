/**
PROJECT 1: Lexical Analyzer
Due: 03/06/20, Friday, 11 PM
Late projects will not be accepted.

Consider the following EBNF defining 18 token categories ⟨id⟩ through ⟨RBrace⟩:
 
This class is a lexical analyzer for the tokens defined by the grammar:

⟨letter⟩ → a | b | ... | z | A | B | ... | Z
⟨digit⟩ → 0 | 1 | ... | 9
⟨id⟩ → ⟨letter⟩ {⟨letter⟩ | ⟨digit⟩}
⟨int⟩ → [+|−] {⟨digit⟩}+
⟨float⟩ → [+|−] ( {⟨digit⟩}+ "." {⟨digit⟩}  |  "." {⟨digit⟩}+ )
⟨floatE⟩ → (⟨int⟩ | ⟨float⟩) (e|E) [+|−] {⟨digit⟩}+
⟨floatF⟩ → (⟨int⟩ | ⟨float⟩ | ⟨floatE⟩) ("f" | "F")
⟨add⟩ → +
⟨sub⟩ → −
⟨mul⟩ → *
⟨div⟩ → /
⟨lt⟩ → <
⟨le⟩ → "<="
⟨gt⟩ → >
⟨ge⟩ → ">="
⟨eq⟩ → =
⟨LParen⟩ → (
⟨RParen⟩ → )
⟨LBrace⟩ → {
⟨RBrace⟩ → }

This class implements a DFA that will accept the above tokens.

The DFA states are represented by the Enum type "State".
The DFA has the following 18 final states represented by enum-type literals:

state     token accepted

Id	 identifiers
Int	 integers
Float	 floats without exponentiation part
FloatE	 floats with exponentiation part
FloatF
Add	  +
Sub	  -
Mul	  *
Div	  /
LParen	  (
RParen	  )
LBrace	  {
RBrace	  }
le	  <=
ge	  >=
lt	  <
gt	  >
eq	  =


The DFA also uses the following 4 non-final states:

Start	   the empty string
Dot	   float parts ending with "."
E	   float parts ending with E or e
EPlusMinus float parts ending with + or - in exponentiation part

The function "driver" operates the DFA. 
The array "nextState" returns the next state given the current state and the input character.

To recognize a different token set, modify the following:

  enum type "State" and function "isFinal"
  size of array "nextState"
  function "setNextState" 

The functions "driver", "getToken", "setLex" remain the same.

**/


public abstract class LexAnalyzer extends IO
{
	public enum State 
       	{ 
	  // non-final states     ordinal number

		Start,             // 0
		Dot,               // 1
		E,                 // 2
		EPlusMinus,        // 3

	  // final states

		Id,                // 4
		Int,               // 5
		Float,             // 6
		FloatE,            // 7
		FloatF,		   // 8
		Add,               // 9
		Sub,               // 10
		Mul,               // 11
		Div,               // 12
		LParen,            // 13
		RParen,            // 14
		LBrace,		   // 15
		RBrace,		   // 16
		le,		   // 17
		ge,		   // 18
		lt,		   // 19
		gt,		   // 20
		eq,            	   // 21
		Keyword_if,	   // 22
		Keyword_then,	   // 23
		Keyword_else,	   // 24
		Keyword_or,	   // 25
		Keyword_and,	   // 26
		Keyword_not,	   // 27
		Keyword_pair,	   // 28
		Keyword_first,	   // 29
		Keyword_second,	   // 30
		Keyword_nil,	   // 31
		i,		   // 32
		t,		   // 33
		h,		   // 34
		e,		   // 35
		l,		   // 37
		s,		   // 38
		a,		   // 39
		n,		   // 40
		o,		   // 41
		p,		   // 42
		f,		   // 43
		r,		   // 44
		c,		   // 45		

		UNDEF;

		private boolean isFinal()
		{
			return ( this.compareTo(State.Id) >= 0 );  
		}	
	}

	// By enumerating the non-final states first and then the final states,
	// test for a final state can be done by testing if the state's ordinal number
	// is greater than or equal to that of Id.

	// The following variables of "IO" class are used:

	//   static int a; the current input character
	//   static char c; used to convert the variable "a" to the char type whenever necessary

	public static String t; // holds an extracted token
	public static State state; // the current state of the FA

	private static State nextState[][] = new State[46][128];
 
          // This array implements the state transition function State x (ASCII char set) --> State.
          // The state argument is converted to its ordinal number used as
          // the first array index from 0 through 31. 

	private static int driver()

	// This is the driver of the FA. 
	// If a valid token is found, assigns it to "t" and returns 1.
	// If an invalid token is found, assigns it to "t" and returns 0.
	// If end-of-stream is reached without finding any non-whitespace character, returns -1.

	{
		State nextSt; // the next state of the FA

		t = "";
		state = State.Start;

		if ( Character.isWhitespace((char) a) )
			a = getChar(); // get the next non-whitespace character
		if ( a == -1 ) // end-of-stream is reached
			return -1;

		while ( a != -1 ) // do the body if "a" is not end-of-stream
		{
			c = (char) a;
			nextSt = nextState[state.ordinal()][a];
			if ( nextSt == State.UNDEF ) // The FA will halt.
			{
				if ( state.isFinal() )
					return 1; // valid token extracted
				else // "c" is an unexpected character
				{
					t = t+c;
					a = getNextChar();
					return 0; // invalid token found
				}
			}
			else // The FA will go on.
			{
				state = nextSt;
				t = t+c;
				a = getNextChar();
			}
		}

		// end-of-stream is reached while a token is being extracted

		if ( state.isFinal() )
			return 1; // valid token extracted
		else
			return 0; // invalid token found
	} // end driver

	public static void getToken()

	// Extract the next token using the driver of the FA.
	// If an invalid token is found, issue an error message.

	{
		int i = driver();
		if ( i == 0 )
			displayln(t + " : Lexical Error, invalid token");
	}

	private static void setNextState()
	{
		for (int s = 0; s <= 45; s++ )
			for (int c = 0; c <= 127; c++ )
				nextState[s][c] = State.UNDEF;

		for (char c = 'A'; c <= 'Z'; c++)
		{
			nextState[State.Start.ordinal()][c] = State.Id;
			nextState[State.Id   .ordinal()][c] = State.Id;
		}

		for (char c = 'a'; c <= 'z'; c++)
		{
			nextState[State.Start.ordinal()][c] = State.Id;
			nextState[State.Id   .ordinal()][c] = State.Id;
		}

		for (char d = '0'; d <= '9'; d++)
		{
			nextState[State.Start     .ordinal()][d] = State.Int;
			nextState[State.Id        .ordinal()][d] = State.Id;
			nextState[State.Int       .ordinal()][d] = State.Int;
			nextState[State.Dot       .ordinal()][d] = State.Float;
			nextState[State.Float     .ordinal()][d] = State.Float;
			nextState[State.E         .ordinal()][d] = State.FloatE;
			nextState[State.EPlusMinus.ordinal()][d] = State.FloatE;
			nextState[State.FloatE    .ordinal()][d] = State.FloatE;
			nextState[State.Add	  .ordinal()][d] = State.Int;
			nextState[State.Sub	  .ordinal()][d] = State.Int;
		}

		nextState[State.Start.ordinal()]['.'] = State.Dot;
		nextState[State.Start.ordinal()]['+'] = State.Add;
		nextState[State.Start.ordinal()]['-'] = State.Sub;
		nextState[State.Start.ordinal()]['*'] = State.Mul;
		nextState[State.Start.ordinal()]['/'] = State.Div;
		nextState[State.Start.ordinal()]['('] = State.LParen;
		nextState[State.Start.ordinal()][')'] = State.RParen;
		nextState[State.Start.ordinal()]['{'] = State.LBrace;
		nextState[State.Start.ordinal()]['}'] = State.RBrace;
		nextState[State.Start.ordinal()]['<'] = State.lt;
		nextState[State.lt   .ordinal()]['='] = State.le;
		nextState[State.Start.ordinal()]['>'] = State.gt;
		nextState[State.gt   .ordinal()]['='] = State.ge;
		nextState[State.Start.ordinal()]['='] = State.eq;
		
					
		nextState[State.Int.ordinal()]['.'] = State.Float;
		nextState[State.Int.ordinal()]['e'] = State.E;
		nextState[State.Int.ordinal()]['E'] = State.E;
		
		nextState[State.Float.ordinal()]['f'] = State.FloatF;
		nextState[State.Float.ordinal()]['F'] = State.FloatF;
		nextState[State.FloatE.ordinal()]['f'] = State.FloatF;
		nextState[State.FloatE.ordinal()]['F'] = State.FloatF;

		nextState[State.Int.ordinal()]['f'] = State.FloatF;
		nextState[State.Int.ordinal()]['F'] = State.FloatF;
			
		nextState[State.Float.ordinal()]['E'] = State.E;
		nextState[State.Float.ordinal()]['e'] = State.E;
			
		nextState[State.E.ordinal()]['+'] = State.EPlusMinus;
		nextState[State.E.ordinal()]['-'] = State.EPlusMinus;

		nextState[State.Add.ordinal()]['.'] = State.Dot;
		nextState[State.Sub.ordinal()]['.'] = State.Dot;

		/* 		
				Keywords Start

		nextState[State.Start.ordinal()]['i'] = State.i;
		nextState[State.i    .ordinal()]['f'] = State.Keyword_if;

		nextState[State.Start.ordinal()]['t'] = State.t;
		nextState[State.t    .ordinal()]['h'] = State.h;
		nextState[State.h    .ordinal()]['e'] = State.e;
		nextState[State.e    .ordinal()]['n'] = State.Keyword_then;

		nextState[State.Start.ordinal()]['e'] = State.e;
		nextState[State.e    .ordinal()]['l'] = State.l;
		nextState[State.l    .ordinal()]['s'] = State.s;
		nextState[State.s    .ordinal()]['e'] = State.Keyword_else;

		nextState[State.Start.ordinal()]['o'] = State.o;
		nextState[State.o    .ordinal()]['r'] = State.Keyword_or;

		nextState[State.Start.ordinal()]['a'] = State.a;
		nextState[State.a    .ordinal()]['n'] = State.n;
		nextState[State.n    .ordinal()]['d'] = State.Keyword_and;

		nextState[State.Start.ordinal()]['n'] = State.n;
		nextState[State.n    .ordinal()]['o'] = State.o;
		nextState[State.o    .ordinal()]['t'] = State.Keyword_not;

		nextState[State.Start.ordinal()]['p'] = State.p;
		nextState[State.p    .ordinal()]['a'] = State.a;
		nextState[State.a    .ordinal()]['i'] = State.i;
		nextState[State.i    .ordinal()]['r'] = State.Keyword_pair;

		nextState[State.Start.ordinal()]['f'] = State.f;
		nextState[State.f    .ordinal()]['i'] = State.i;
		nextState[State.i    .ordinal()]['r'] = State.r;
		nextState[State.r    .ordinal()]['s'] = State.s;
		nextState[State.s    .ordinal()]['t'] = State.Keyword_first;

		nextState[State.Start.ordinal()]['s'] = State.s;
		nextState[State.s    .ordinal()]['e'] = State.e;
		nextState[State.e    .ordinal()]['c'] = State.c;
		nextState[State.c    .ordinal()]['o'] = State.o;
		nextState[State.o    .ordinal()]['n'] = State.n;
		nextState[State.n    .ordinal()]['d'] = State.Keyword_second;

		nextState[State.Start.ordinal()]['n'] = State.n;
		nextState[State.n    .ordinal()]['i'] = State.i;
		nextState[State.i    .ordinal()]['l'] = State.Keyword_nil;

				Keywords End
		*/

	} // end setNextState

	public static void setLex()

	// Sets the nextState array.

	{
		setNextState();
	}

	public static void main(String argv[])

	{		
		// argv[0]: input file containing source code using tokens defined above
		// argv[1]: output file displaying a list of the tokens 

		setIO( argv[0], argv[1] );
		setLex();
		
		int i;

		while ( a != -1 ) // while "a" is not end-of-stream
		{
			i = driver(); // extract the next token
			if ( i == 1 )
				displayln( t+"   : "+state.toString() );
			else if ( i == 0 )
				displayln( t+" : Lexical Error, invalid token");
		} 

		closeIO();
	}
} 
