/* Generated By:JavaCC: Do not edit this line. ParserTokenManager.java */
package org.graphviz;

import java.io.IOException;
import java.io.PrintStream;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;

/**
 * Token Manager.
 */
public class ParserTokenManager implements ParserConstants
{
	/**
	 * Used to determine if the graph is a digraph or not
	 */
	boolean isDigraph = false;

	/**
	 * Used to select the correct edge operator already on Tokenizer level
	 *
	 * @return true if the graph was defined as digraph
	 */
	public boolean isDigraph()
	{
		return this.isDigraph;
	}

	/**
	 * Debug output.
	 */
	public PrintStream debugStream = System.out;

	/**
	 * Set debug output.
	 *
	 * @param ds debug output stream
	 */
	public void setDebugStream(final PrintStream ds)
	{
		this.debugStream = ds;
	}

	private int jjStopAtPos(final int pos, final int kind)
	{
		this.jjmatchedKind = kind;
		this.jjmatchedPos = pos;
		return pos + 1;
	}

	private int jjMoveStringLiteralDfa0_2()
	{
		if (this.curChar == 13)
		{
			return jjStopAtPos(0, 8);
		}
		return jjMoveNfa_2(0, 0);
	}

	static final long[] jjbitVec0 = {0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL};

	static final long[] jjbitVec2 = {0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL};

	private int jjMoveNfa_2(final int startState, int curPos)
	{
		int startsAt = 0;
		this.jjnewStateCnt = 1;
		int i = 1;
		this.jjstateSet[0] = startState;
		int kind = 0x7fffffff;
		for (; ; )
		{
			if (++this.jjround == 0x7fffffff)
			{
				ReInitRounds();
			}
			if (this.curChar < 64)
			{
				final long l = 1L << this.curChar;
				do
				{
					if (this.jjstateSet[--i] == 0)
					{
						if ((0xffffffffffffdbffL & l) != 0L)
						{
							kind = 6;
						}
					}
				}
				while (i != startsAt);
			}
			else if (this.curChar < 128)
			{
				do
				{
					if (this.jjstateSet[--i] == 0)
					{
						kind = 6;
						break;
					}
				}
				while (i != startsAt);
			}
			else
			{
				final int hiByte = this.curChar >> 8;
				final int i1 = hiByte >> 6;
				final long l1 = 1L << (hiByte & 077);
				final int i2 = (this.curChar & 0xff) >> 6;
				final long l2 = 1L << (this.curChar & 077);
				do
				{
					if (this.jjstateSet[--i] == 0)
					{
						if (ParserTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 6)
						{
							kind = 6;
						}
					}
				}
				while (i != startsAt);
			}
			if (kind != 0x7fffffff)
			{
				this.jjmatchedKind = kind;
				this.jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = this.jjnewStateCnt) == (startsAt = 1 - (this.jjnewStateCnt = startsAt)))
			{
				return curPos;
			}
			try
			{
				this.curChar = this.input_stream.readChar();
			}
			catch (final IOException e)
			{
				return curPos;
			}
		}
	}

	private int jjStopStringLiteralDfa_0(final int pos, final long active0)
	{
		switch (pos)
		{
			case 0:
				if ((active0 & 0x7e00000L) != 0L)
				{
					this.jjmatchedKind = 28;
					return 13;
				}
				if ((active0 & 0x600L) != 0L)
				{
					return 22;
				}
				return -1;
			case 1:
				if ((active0 & 0x7e00000L) != 0L)
				{
					this.jjmatchedKind = 28;
					this.jjmatchedPos = 1;
					return 13;
				}
				return -1;
			case 2:
				if ((active0 & 0x7e00000L) != 0L)
				{
					this.jjmatchedKind = 28;
					this.jjmatchedPos = 2;
					return 13;
				}
				return -1;
			case 3:
				if ((active0 & 0xc00000L) != 0L)
				{
					return 13;
				}
				if ((active0 & 0x7200000L) != 0L)
				{
					this.jjmatchedKind = 28;
					this.jjmatchedPos = 3;
					return 13;
				}
				return -1;
			case 4:
				if ((active0 & 0x5200000L) != 0L)
				{
					this.jjmatchedKind = 28;
					this.jjmatchedPos = 4;
					return 13;
				}
				if ((active0 & 0x2000000L) != 0L)
				{
					return 13;
				}
				return -1;
			case 5:
				if ((active0 & 0x1200000L) != 0L)
				{
					this.jjmatchedKind = 28;
					this.jjmatchedPos = 5;
					return 13;
				}
				if ((active0 & 0x4000000L) != 0L)
				{
					return 13;
				}
				return -1;
			case 6:
				if ((active0 & 0x1000000L) != 0L)
				{
					return 13;
				}
				if ((active0 & 0x200000L) != 0L)
				{
					this.jjmatchedKind = 28;
					this.jjmatchedPos = 6;
					return 13;
				}
				return -1;
			default:
				return -1;
		}
	}

	private int jjStartNfa_0(final int pos, final long active0)
	{
		return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
	}

	private int jjMoveStringLiteralDfa0_0()
	{
		switch (this.curChar)
		{
			case 32:
				return jjStopAtPos(0, 15);
			case 44:
				return jjStopAtPos(0, 13);
			case 45:
				return jjMoveStringLiteralDfa1_0(0x600L);
			case 47:
				return jjMoveStringLiteralDfa1_0(0x24L);
			case 58:
				return jjStopAtPos(0, 12);
			case 59:
				return jjStopAtPos(0, 11);
			case 61:
				return jjStopAtPos(0, 14);
			case 91:
				return jjStopAtPos(0, 16);
			case 93:
				return jjStopAtPos(0, 17);
			case 68:
			case 100:
				return jjMoveStringLiteralDfa1_0(0x1000000L);
			case 69:
			case 101:
				return jjMoveStringLiteralDfa1_0(0x800000L);
			case 71:
			case 103:
				return jjMoveStringLiteralDfa1_0(0x2000000L);
			case 78:
			case 110:
				return jjMoveStringLiteralDfa1_0(0x400000L);
			case 83:
			case 115:
				return jjMoveStringLiteralDfa1_0(0x4200000L);
			case 123:
				return jjStopAtPos(0, 18);
			case 125:
				return jjStopAtPos(0, 19);
			default:
				return jjMoveNfa_0(1, 0);
		}
	}

	private int jjMoveStringLiteralDfa1_0(final long active0)
	{
		try
		{
			this.curChar = this.input_stream.readChar();
		}
		catch (final IOException e)
		{
			jjStopStringLiteralDfa_0(0, active0);
			return 1;
		}
		switch (this.curChar)
		{
			case 42:
				if ((active0 & 0x4L) != 0L)
				{
					return jjStopAtPos(1, 2);
				}
				break;
			case 45:
				if ((active0 & 0x400L) != 0L)
				{
					return jjStopAtPos(1, 10);
				}
				break;
			case 47:
				if ((active0 & 0x20L) != 0L)
				{
					return jjStopAtPos(1, 5);
				}
				break;
			case 62:
				if ((active0 & 0x200L) != 0L)
				{
					return jjStopAtPos(1, 9);
				}
				break;
			case 68:
			case 100:
				return jjMoveStringLiteralDfa2_0(active0, 0x800000L);
			case 73:
			case 105:
				return jjMoveStringLiteralDfa2_0(active0, 0x1000000L);
			case 79:
			case 111:
				return jjMoveStringLiteralDfa2_0(active0, 0x400000L);
			case 82:
			case 114:
				return jjMoveStringLiteralDfa2_0(active0, 0x2000000L);
			case 84:
			case 116:
				return jjMoveStringLiteralDfa2_0(active0, 0x4000000L);
			case 85:
			case 117:
				return jjMoveStringLiteralDfa2_0(active0, 0x200000L);
			default:
				break;
		}
		return jjStartNfa_0(0, active0);
	}

	private int jjMoveStringLiteralDfa2_0(final long old0, long active0)
	{
		if ((active0 &= old0) == 0L)
		{
			return jjStartNfa_0(0, old0);
		}
		try
		{
			this.curChar = this.input_stream.readChar();
		}
		catch (final IOException e)
		{
			jjStopStringLiteralDfa_0(1, active0);
			return 2;
		}
		switch (this.curChar)
		{
			case 65:
			case 97:
				return jjMoveStringLiteralDfa3_0(active0, 0x2000000L);
			case 66:
			case 98:
				return jjMoveStringLiteralDfa3_0(active0, 0x200000L);
			case 68:
			case 100:
				return jjMoveStringLiteralDfa3_0(active0, 0x400000L);
			case 71:
			case 103:
				return jjMoveStringLiteralDfa3_0(active0, 0x1800000L);
			case 82:
			case 114:
				return jjMoveStringLiteralDfa3_0(active0, 0x4000000L);
			default:
				break;
		}
		return jjStartNfa_0(1, active0);
	}

	private int jjMoveStringLiteralDfa3_0(final long old0, long active0)
	{
		if ((active0 &= old0) == 0L)
		{
			return jjStartNfa_0(1, old0);
		}
		try
		{
			this.curChar = this.input_stream.readChar();
		}
		catch (final IOException e)
		{
			jjStopStringLiteralDfa_0(2, active0);
			return 3;
		}
		switch (this.curChar)
		{
			case 69:
			case 101:
				if ((active0 & 0x400000L) != 0L)
				{
					return jjStartNfaWithStates_0(3, 22, 13);
				}
				else if ((active0 & 0x800000L) != 0L)
				{
					return jjStartNfaWithStates_0(3, 23, 13);
				}
				break;
			case 71:
			case 103:
				return jjMoveStringLiteralDfa4_0(active0, 0x200000L);
			case 73:
			case 105:
				return jjMoveStringLiteralDfa4_0(active0, 0x4000000L);
			case 80:
			case 112:
				return jjMoveStringLiteralDfa4_0(active0, 0x2000000L);
			case 82:
			case 114:
				return jjMoveStringLiteralDfa4_0(active0, 0x1000000L);
			default:
				break;
		}
		return jjStartNfa_0(2, active0);
	}

	private int jjMoveStringLiteralDfa4_0(final long old0, long active0)
	{
		if ((active0 &= old0) == 0L)
		{
			return jjStartNfa_0(2, old0);
		}
		try
		{
			this.curChar = this.input_stream.readChar();
		}
		catch (final IOException e)
		{
			jjStopStringLiteralDfa_0(3, active0);
			return 4;
		}
		switch (this.curChar)
		{
			case 65:
			case 97:
				return jjMoveStringLiteralDfa5_0(active0, 0x1000000L);
			case 67:
			case 99:
				return jjMoveStringLiteralDfa5_0(active0, 0x4000000L);
			case 72:
			case 104:
				if ((active0 & 0x2000000L) != 0L)
				{
					return jjStartNfaWithStates_0(4, 25, 13);
				}
				break;
			case 82:
			case 114:
				return jjMoveStringLiteralDfa5_0(active0, 0x200000L);
			default:
				break;
		}
		return jjStartNfa_0(3, active0);
	}

	private int jjMoveStringLiteralDfa5_0(final long old0, long active0)
	{
		if ((active0 &= old0) == 0L)
		{
			return jjStartNfa_0(3, old0);
		}
		try
		{
			this.curChar = this.input_stream.readChar();
		}
		catch (final IOException e)
		{
			jjStopStringLiteralDfa_0(4, active0);
			return 5;
		}
		switch (this.curChar)
		{
			case 65:
			case 97:
				return jjMoveStringLiteralDfa6_0(active0, 0x200000L);
			case 80:
			case 112:
				return jjMoveStringLiteralDfa6_0(active0, 0x1000000L);
			case 84:
			case 116:
				if ((active0 & 0x4000000L) != 0L)
				{
					return jjStartNfaWithStates_0(5, 26, 13);
				}
				break;
			default:
				break;
		}
		return jjStartNfa_0(4, active0);
	}

	private int jjMoveStringLiteralDfa6_0(final long old0, long active0)
	{
		if ((active0 &= old0) == 0L)
		{
			return jjStartNfa_0(4, old0);
		}
		try
		{
			this.curChar = this.input_stream.readChar();
		}
		catch (final IOException e)
		{
			jjStopStringLiteralDfa_0(5, active0);
			return 6;
		}
		switch (this.curChar)
		{
			case 72:
			case 104:
				if ((active0 & 0x1000000L) != 0L)
				{
					return jjStartNfaWithStates_0(6, 24, 13);
				}
				break;
			case 80:
			case 112:
				return jjMoveStringLiteralDfa7_0(active0, 0x200000L);
			default:
				break;
		}
		return jjStartNfa_0(5, active0);
	}

	private int jjMoveStringLiteralDfa7_0(final long old0, long active0)
	{
		if ((active0 &= old0) == 0L)
		{
			return jjStartNfa_0(5, old0);
		}
		try
		{
			this.curChar = this.input_stream.readChar();
		}
		catch (final IOException e)
		{
			jjStopStringLiteralDfa_0(6, active0);
			return 7;
		}
		switch (this.curChar)
		{
			case 72:
			case 104:
				if ((active0 & 0x200000L) != 0L)
				{
					return jjStartNfaWithStates_0(7, 21, 13);
				}
				break;
			default:
				break;
		}
		return jjStartNfa_0(6, active0);
	}

	private int jjStartNfaWithStates_0(final int pos, final int kind, @SuppressWarnings("SameParameterValue") final int state)
	{
		this.jjmatchedKind = kind;
		this.jjmatchedPos = pos;
		try
		{
			this.curChar = this.input_stream.readChar();
		}
		catch (final IOException e)
		{
			return pos + 1;
		}
		return jjMoveNfa_0(state, pos + 1);
	}

	private int jjMoveNfa_0(final int startState, int curPos)
	{
		int startsAt = 0;
		this.jjnewStateCnt = 22;
		int i = 1;
		this.jjstateSet[0] = startState;
		int kind = 0x7fffffff;
		for (; ; )
		{
			if (++this.jjround == 0x7fffffff)
			{
				ReInitRounds();
			}
			if (this.curChar < 64)
			{
				final long l = 1L << this.curChar;
				do
				{
					switch (this.jjstateSet[--i])
					{
						case 22:
							if ((0x3ff000000000000L & l) != 0L)
							{
								if (kind > 29)
								{
									kind = 29;
								}
								jjCheckNAdd(18);
							}
							else if (this.curChar == 46)
							{
								jjCheckNAdd(17);
							}
							if ((0x3ff000000000000L & l) != 0L)
							{
								jjCheckNAddTwoStates(15, 16);
							}
							break;
						case 1:
							if ((0x3ff000000000000L & l) != 0L)
							{
								if (kind > 29)
								{
									kind = 29;
								}
								jjCheckNAddStates(0, 2);
							}
							else if ((0x2400L & l) != 0L)
							{
								if (kind > 20)
								{
									kind = 20;
								}
							}
							else if (this.curChar == 46)
							{
								jjCheckNAddTwoStates(17, 21);
							}
							else if (this.curChar == 45)
							{
								jjCheckNAddStates(0, 2);
							}
							else if (this.curChar == 34)
							{
								jjCheckNAddStates(3, 5);
							}
							if (this.curChar == 13)
							{
								this.jjstateSet[this.jjnewStateCnt++] = 0;
							}
							break;
						case 0:
							if (this.curChar == 10 && kind > 20)
							{
								kind = 20;
							}
							break;
						case 2:
							if ((0x2400L & l) != 0L && kind > 20)
							{
								kind = 20;
							}
							break;
						case 3:
							if (this.curChar == 34)
							{
								jjCheckNAddStates(3, 5);
							}
							break;
						case 4:
							if ((0xfffffffbffffdbffL & l) != 0L)
							{
								jjCheckNAddStates(3, 5);
							}
							break;
						case 6:
							if ((0x8400000000L & l) != 0L)
							{
								jjCheckNAddStates(3, 5);
							}
							break;
						case 7:
							if (this.curChar == 34 && kind > 27)
							{
								kind = 27;
							}
							break;
						case 8:
							if ((0xff000000000000L & l) != 0L)
							{
								jjCheckNAddStates(6, 9);
							}
							break;
						case 9:
							if ((0xff000000000000L & l) != 0L)
							{
								jjCheckNAddStates(3, 5);
							}
							break;
						case 10:
							if ((0xf000000000000L & l) != 0L)
							{
								this.jjstateSet[this.jjnewStateCnt++] = 11;
							}
							break;
						case 11:
							if ((0xff000000000000L & l) != 0L)
							{
								jjCheckNAdd(9);
							}
							break;
						case 13:
							if ((0x3ff000000000000L & l) == 0L)
							{
								break;
							}
							if (kind > 28)
							{
								kind = 28;
							}
							this.jjstateSet[this.jjnewStateCnt++] = 13;
							break;
						case 14:
							if (this.curChar == 45)
							{
								jjCheckNAddStates(0, 2);
							}
							break;
						case 15:
							if ((0x3ff000000000000L & l) != 0L)
							{
								jjCheckNAddTwoStates(15, 16);
							}
							break;
						case 16:
							if (this.curChar == 46)
							{
								jjCheckNAdd(17);
							}
							break;
						case 17:
							if ((0x3ff000000000000L & l) == 0L)
							{
								break;
							}
							if (kind > 29)
							{
								kind = 29;
							}
							jjCheckNAdd(17);
							break;
						case 18:
							if ((0x3ff000000000000L & l) == 0L)
							{
								break;
							}
							if (kind > 29)
							{
								kind = 29;
							}
							jjCheckNAdd(18);
							break;
						case 19:
							if ((0x3ff000000000000L & l) == 0L)
							{
								break;
							}
							if (kind > 29)
							{
								kind = 29;
							}
							jjCheckNAddStates(0, 2);
							break;
						case 20:
							if (this.curChar == 46)
							{
								jjCheckNAddTwoStates(17, 21);
							}
							break;
						case 21:
							if ((0x3ff000000000000L & l) == 0L)
							{
								break;
							}
							if (kind > 29)
							{
								kind = 29;
							}
							jjCheckNAdd(21);
							break;
						default:
							break;
					}
				}
				while (i != startsAt);
			}
			else if (this.curChar < 128)
			{
				final long l = 1L << (this.curChar & 077);
				do
				{
					switch (this.jjstateSet[--i])
					{
						case 1:
						case 13:
							if ((0x7fffffe87fffffeL & l) == 0L)
							{
								break;
							}
							if (kind > 28)
							{
								kind = 28;
							}
							jjCheckNAdd(13);
							break;
						case 4:
							if ((0xffffffffefffffffL & l) != 0L)
							{
								jjCheckNAddStates(3, 5);
							}
							break;
						case 5:
							if (this.curChar == 92)
							{
								jjAddStates(10, 12);
							}
							break;
						case 6:
							if ((0x14404410144044L & l) != 0L)
							{
								jjCheckNAddStates(3, 5);
							}
							break;
						default:
							break;
					}
				}
				while (i != startsAt);
			}
			else
			{
				final int hiByte = this.curChar >> 8;
				final int i1 = hiByte >> 6;
				final long l1 = 1L << (hiByte & 077);
				final int i2 = (this.curChar & 0xff) >> 6;
				final long l2 = 1L << (this.curChar & 077);
				do
				{
					if (this.jjstateSet[--i] == 4)
					{
						if (ParserTokenManager.jjCanMove_0(hiByte, i1, i2, l1, l2))
						{
							jjAddStates(3, 5);
						}
					}
				}
				while (i != startsAt);
			}
			if (kind != 0x7fffffff)
			{
				this.jjmatchedKind = kind;
				this.jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = this.jjnewStateCnt) == (startsAt = 22 - (this.jjnewStateCnt = startsAt)))
			{
				return curPos;
			}
			try
			{
				this.curChar = this.input_stream.readChar();
			}
			catch (final IOException e)
			{
				return curPos;
			}
		}
	}

	private int jjMoveStringLiteralDfa0_1()
	{
		if (this.curChar == 42)
		{
			return jjMoveStringLiteralDfa1_1(0x10L);
		}
		return 1;
	}

	private int jjMoveStringLiteralDfa1_1(@SuppressWarnings("SameParameterValue") final long active0)
	{
		try
		{
			this.curChar = this.input_stream.readChar();
		}
		catch (final IOException e)
		{
			return 1;
		}
		if (this.curChar == 47)
		{
			if ((active0 & 0x10L) != 0L)
			{
				return jjStopAtPos(1, 4);
			}
		}
		else
		{
			return 2;
		}
		return 2;
	}

	static final int[] jjnextStates = {15, 16, 18, 4, 5, 7, 4, 5, 9, 7, 6, 8, 10,};

	private static boolean jjCanMove_0(final int hiByte, final int i1, final int i2, final long l1, final long l2)
	{
		if (hiByte == 0)
		{
			return (ParserTokenManager.jjbitVec2[i2] & l2) != 0L;
		}
		return (ParserTokenManager.jjbitVec0[i1] & l1) != 0L;
	}

	/**
	 * Token literal values.
	 */
	public static final String[] jjstrLiteralImages = {"", null, null, null, null, null, null, null, null, "\55\76", "\55\55", "\73", "\72", "\54", "\75", "\40", "\133", "\135", "\173", "\175", null, null, null, null, null, null, null, null, null, null,};

	/**
	 * Lexer state names.
	 */
	public static final String[] lexStateNames = {"DEFAULT", "IN_COMMENT", "IN_COMMENT_OL",};

	/**
	 * Lex State array.
	 */
	public static final int[] jjnewLexState = {-1, -1, 1, -1, 0, 2, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,};

	static final long[] jjtoToken = {0x3ffffe01L,};

	static final long[] jjtoSkip = {0x192L,};

	static final long[] jjtoMore = {0x6cL,};

	protected JavaCharStream input_stream;

	private final int[] jjrounds = new int[22];

	private final int[] jjstateSet = new int[44];

	private final StringBuilder jjimage = new StringBuilder();

	@NonNull
	private StringBuilder image = this.jjimage;

	private int jjimageLen;

	protected char curChar;

	/**
	 * Constructor.
	 *
	 * @param stream stream
	 */
	public ParserTokenManager(final JavaCharStream stream)
	{
		if (JavaCharStream.staticFlag)
		{
			throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
		}
		this.input_stream = stream;
	}

	/**
	 * Constructor.
	 *
	 * @param stream   stream
	 * @param lexState lex state
	 */
	public ParserTokenManager(final JavaCharStream stream, final int lexState)
	{
		this(stream);
		SwitchTo(lexState);
	}

	/**
	 * Reinitialise parser.
	 *
	 * @param stream stream
	 */
	public void ReInit(final JavaCharStream stream)
	{
		this.jjmatchedPos = this.jjnewStateCnt = 0;
		this.curLexState = this.defaultLexState;
		this.input_stream = stream;
		ReInitRounds();
	}

	private void ReInitRounds()
	{
		int i;
		this.jjround = 0x80000001;
		for (i = 22; i-- > 0; )
		{
			this.jjrounds[i] = 0x80000000;
		}
	}

	/**
	 * Reinitialise parser.
	 *
	 * @param stream   stream
	 * @param lexState lex state
	 */
	public void ReInit(final JavaCharStream stream, final int lexState)
	{
		ReInit(stream);
		SwitchTo(lexState);
	}

	/**
	 * Switch to specified lex state.
	 *
	 * @param lexState lex state
	 */
	public void SwitchTo(final int lexState)
	{
		if (lexState >= 3 || lexState < 0)
		{
			throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
		}
		else
		{
			this.curLexState = lexState;
		}
	}

	@NonNull
	protected Token jjFillToken()
	{
		@NonNull final Token t;
		@NonNull final String curTokenImage;
		final int beginLine;
		final int endLine;
		final int beginColumn;
		final int endColumn;
		final String im = ParserTokenManager.jjstrLiteralImages[this.jjmatchedKind];
		curTokenImage = im == null ? this.input_stream.GetImage() : im;
		beginLine = this.input_stream.getBeginLine();
		beginColumn = this.input_stream.getBeginColumn();
		endLine = this.input_stream.getEndLine();
		endColumn = this.input_stream.getEndColumn();
		t = Token.newToken(this.jjmatchedKind, curTokenImage);

		t.beginLine = beginLine;
		t.endLine = endLine;
		t.beginColumn = beginColumn;
		t.endColumn = endColumn;

		return t;
	}

	int curLexState = 0;

	final int defaultLexState = 0;

	int jjnewStateCnt;

	int jjround;

	int jjmatchedPos;

	int jjmatchedKind;

	/**
	 * Get the next Token.
	 *
	 * @return next token
	 */
	@NonNull
	public Token getNextToken()
	{
		Token matchedToken;
		int curPos = 0;

		EOFLoop:
		for (; ; )
		{
			try
			{
				this.curChar = this.input_stream.BeginToken();
			}
			catch (final IOException e)
			{
				this.jjmatchedKind = 0;
				matchedToken = jjFillToken();
				return matchedToken;
			}
			this.image = this.jjimage;
			this.image.setLength(0);
			this.jjimageLen = 0;

			for (; ; )
			{
				switch (this.curLexState)
				{
					case 0:
						try
						{
							this.input_stream.backup(0);
							while (this.curChar <= 9 && (0x200L & 1L << this.curChar) != 0L)
							{
								this.curChar = this.input_stream.BeginToken();
							}
						}
						catch (final IOException e1)
						{
							continue EOFLoop;
						}
						this.jjmatchedKind = 0x7fffffff;
						this.jjmatchedPos = 0;
						curPos = jjMoveStringLiteralDfa0_0();
						break;
					case 1:
						this.jjmatchedKind = 0x7fffffff;
						this.jjmatchedPos = 0;
						curPos = jjMoveStringLiteralDfa0_1();
						if (this.jjmatchedPos == 0 && this.jjmatchedKind > 3)
						{
							this.jjmatchedKind = 3;
						}
						break;
					case 2:
						try
						{
							this.input_stream.backup(0);
							while (this.curChar <= 10 && (0x400L & 1L << this.curChar) != 0L)
							{
								this.curChar = this.input_stream.BeginToken();
							}
						}
						catch (final IOException e1)
						{
							continue EOFLoop;
						}
						this.jjmatchedKind = 0x7fffffff;
						this.jjmatchedPos = 0;
						curPos = jjMoveStringLiteralDfa0_2();
						break;
				}
				if (this.jjmatchedKind != 0x7fffffff)
				{
					if (this.jjmatchedPos + 1 < curPos)
					{
						this.input_stream.backup(curPos - this.jjmatchedPos - 1);
					}
					if ((ParserTokenManager.jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 077)) != 0L)
					{
						matchedToken = jjFillToken();
						TokenLexicalActions(matchedToken);
						if (ParserTokenManager.jjnewLexState[this.jjmatchedKind] != -1)
						{
							this.curLexState = ParserTokenManager.jjnewLexState[this.jjmatchedKind];
						}
						return matchedToken;
					}
					else if ((ParserTokenManager.jjtoSkip[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 077)) != 0L)
					{
						if (ParserTokenManager.jjnewLexState[this.jjmatchedKind] != -1)
						{
							this.curLexState = ParserTokenManager.jjnewLexState[this.jjmatchedKind];
						}
						continue EOFLoop;
					}
					this.jjimageLen += this.jjmatchedPos + 1;
					if (ParserTokenManager.jjnewLexState[this.jjmatchedKind] != -1)
					{
						this.curLexState = ParserTokenManager.jjnewLexState[this.jjmatchedKind];
					}
					curPos = 0;
					this.jjmatchedKind = 0x7fffffff;
					try
					{
						this.curChar = this.input_stream.readChar();
						continue;
					}
					catch (final IOException ignored)
					{
					}
				}
				int error_line = this.input_stream.getEndLine();
				int error_column = this.input_stream.getEndColumn();
				@Nullable String error_after = null;
				boolean EOFSeen = false;
				try
				{
					this.input_stream.readChar();
					this.input_stream.backup(1);
				}
				catch (final IOException e1)
				{
					EOFSeen = true;
					error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
					if (this.curChar == '\n' || this.curChar == '\r')
					{
						error_line++;
						error_column = 0;
					}
					else
					{
						error_column++;
					}
				}
				if (!EOFSeen)
				{
					this.input_stream.backup(1);
					error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
				}
				throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, TokenMgrError.LEXICAL_ERROR);
			}
		}
	}

	void TokenLexicalActions(@NonNull final Token matchedToken)
	{
		switch (this.jjmatchedKind)
		{
			case 9:
				this.image.append(ParserTokenManager.jjstrLiteralImages[9]);
				// int lengthOfMatch = ParserTokenManager.jjstrLiteralImages[9].length();
				if (!isDigraph())
				{
					throw GraphvizTokenMgrError.create(GraphvizTokenMgrError.DIRECTED_EDGE_NOT_ALLOWED, matchedToken.beginLine, matchedToken.beginColumn);
				}
				break;
			case 10:
				this.image.append(ParserTokenManager.jjstrLiteralImages[10]);
				// lengthOfMatch = ParserTokenManager.jjstrLiteralImages[10].length();
				if (isDigraph())
				{
					throw GraphvizTokenMgrError.create(GraphvizTokenMgrError.UNDIRECTED_EDGE_NOT_ALLOWED, matchedToken.beginLine, matchedToken.beginColumn);
				}
				break;
			case 24:
				this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (/* lengthOfMatch = */ this.jjmatchedPos + 1)));
				this.isDigraph = true;
				break;
			default:
				break;
		}
	}

	private void jjCheckNAdd(final int state)
	{
		if (this.jjrounds[state] != this.jjround)
		{
			this.jjstateSet[this.jjnewStateCnt++] = state;
			this.jjrounds[state] = this.jjround;
		}
	}

	private void jjAddStates(int start, final int end)
	{
		do
		{
			this.jjstateSet[this.jjnewStateCnt++] = ParserTokenManager.jjnextStates[start];
		}
		while (start++ != end);
	}

	private void jjCheckNAddTwoStates(final int state1, final int state2)
	{
		jjCheckNAdd(state1);
		jjCheckNAdd(state2);
	}

	private void jjCheckNAddStates(int start, final int end)
	{
		do
		{
			jjCheckNAdd(ParserTokenManager.jjnextStates[start]);
		}
		while (start++ != end);
	}

}
