package com.sun.tranquilo.verifier.regexp;

import com.sun.tranquilo.grammar.*;
import com.sun.tranquilo.verifier.Acceptor;

/**
 * calculates how character literals should be treated.
 * 
 * This class is thread-safe: multiple threads can simultaneously
 * access the same instance. Note that there is no guarantee that the
 * derived class is thread-safe.
 */
public class StringCareLevelCalculator implements ExpressionVisitorBoolean
{
	// those expressions which are sensitive about string must return true
	public boolean onAttribute( AttributeExp exp )		{ return false; }
	public boolean onChoice( ChoiceExp exp )			{ return exp.exp1.visit(this)||exp.exp2.visit(this); }
	public boolean onElement( ElementExp exp )			{ return false; }
	public boolean onOneOrMore( OneOrMoreExp exp )		{ return exp.exp.visit(this); }
	public boolean onMixed( MixedExp exp )				{ return true; }
	public boolean onRef( ReferenceExp exp )			{ return exp.exp.visit(this); }
	public boolean onEpsilon()							{ return false; }
	public boolean onNullSet()							{ return false; }
	public boolean onAnyString()						{ return true; }
	public boolean onSequence( SequenceExp exp )		{ return exp.exp1.visit(this)||exp.exp2.visit(this); }
	public boolean onTypedString( TypedStringExp exp )	{ return true; }

	public int calc( Expression exp )
	{
		// if and only if the top-level element is mixed,
		// it can ignores strings.
		if( exp instanceof MixedExp )	return Acceptor.STRING_IGNORE;
		
		if( exp.visit(this) )
			// somebody claims that string is necessary.
			return Acceptor.STRING_STRICT;
		else
			// nobody claims that string is necessary.
			return Acceptor.STRING_PROHIBITED;
	}
}