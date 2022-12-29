/* Generated By:JavaCC: Do not edit this line. TokenMgrError.java Version 5.0 */
/* JavaCCOptions: */
package org.graphviz;

/**
 * Token Manager Error.
 */
public class TokenMgrError extends Error
{

	/**
	 * The version identifier for this Serializable class. Increment only if the <i>serialized</i> form of the class changes.
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Ordinals for various reasons why an Error of this type can be thrown.
	 */

	/**
	 * Lexical error occurred.
	 */
	static final int LEXICAL_ERROR = 0;

	/**
	 * An attempt was made to create a second instance of a static token manager.
	 */
	static final int STATIC_LEXER_ERROR = 1;

	/**
	 * Tried to change to an invalid lexical state.
	 */
	static final int INVALID_LEXICAL_STATE = 2;

	/**
	 * Detected (and bailed out of) an infinite loop in the token manager.
	 */
	static final int LOOP_DETECTED = 3;

	/**
	 * Indicates the reason why the exception is thrown. It will have one of the above 4 values.
	 */
	int errorCode;

	/**
	 * Replaces unprintable characters by their escaped (or unicode escaped) equivalents in the given string
	 */
	protected static String addEscapes(final String str)
	{
		final StringBuilder retval = new StringBuilder();
		char ch;
		for (int i = 0; i < str.length(); i++)
		{
			switch (str.charAt(i))
			{
				case 0:
					continue;
				case '\b':
					retval.append("\\b");
					continue;
				case '\t':
					retval.append("\\t");
					continue;
				case '\n':
					retval.append("\\n");
					continue;
				case '\f':
					retval.append("\\f");
					continue;
				case '\r':
					retval.append("\\r");
					continue;
				case '\"':
					retval.append("\\\"");
					continue;
				case '\'':
					retval.append("\\'");
					continue;
				case '\\':
					retval.append("\\\\");
					continue;
				default:
					if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e)
					{
						final String s = "0000" + Integer.toString(ch, 16);
						retval.append("\\u").append(s.substring(s.length() - 4));
					}
					else
					{
						retval.append(ch);
					}
			}
		}
		return retval.toString();
	}

	/**
	 * Returns a detailed message for the Error when it is thrown by the token manager to indicate a lexical error. Parameters : EOFSeen : indicates if EOF
	 * caused the lexical error curLexState : lexical state in which this error occurred errorLine : line number when the error occurred errorColumn : column
	 * number when the error occurred errorAfter : prefix that was seen before this error occurred curchar : the offending character Note: You can customize the
	 * lexical error message by modifying this method.
	 */
	protected static String LexicalError(final boolean EOFSeen, final int lexState, final int errorLine, final int errorColumn, final String errorAfter, final char curChar)
	{
		return "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ?
				"<EOF> " :
				"\"" + TokenMgrError.addEscapes(String.valueOf(curChar)) + "\"" + " (" + (int) curChar + "), ") + "after : \"" + TokenMgrError.addEscapes(errorAfter) + "\"";
	}

	/**
	 * You can also modify the body of this method to customize your error messages. For example, cases like LOOP_DETECTED and INVALID_LEXICAL_STATE are not of
	 * end-users concern, so you can return something like : "Internal Error : Please file a bug report .... " from this method for such cases in the release
	 * Version : 3.x
	 */
	@Override
	public String getMessage()
	{
		return super.getMessage();
	}

	/*
	 * Constructors of various flavors follow.
	 */

	/**
	 * No arg constructor.
	 */
	public TokenMgrError()
	{
	}

	/**
	 * Constructor with message and reason.
	 *
	 * @param message message
	 * @param reason  reason
	 */
	public TokenMgrError(final String message, final int reason)
	{
		super(message);
		this.errorCode = reason;
	}

	/**
	 * Full Constructor.
	 *
	 * @param EOFSeen     eof seen
	 * @param lexState    lex state
	 * @param errorLine   error line
	 * @param errorColumn error column
	 * @param errorAfter  error after
	 * @param curChar     current character
	 * @param reason      reason
	 */
	public TokenMgrError(final boolean EOFSeen, final int lexState, final int errorLine, final int errorColumn, final String errorAfter, final char curChar, final int reason)
	{
		this(TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
	}
}
/* JavaCC - OriginalChecksum=24f0ec7a3be6fd9557d207ecc471166b (do not edit this line) */
