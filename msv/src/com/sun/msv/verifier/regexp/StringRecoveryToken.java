package com.sun.tranquilo.verifier.regexp;

import com.sun.tranquilo.grammar.TypedStringExp;
import java.util.Set;

/**
 * special StringToken that acts as a wild card.
 * 
 * This object is used for error recovery.
 */
class StringRecoveryToken extends StringToken
{
	StringRecoveryToken( StringToken base )
	{
		super( base.literal, base.context );
	}
	
	/**
	 * DataTypes that signals an error is collected into this object.
	 */
	final Set failedTypes = new java.util.HashSet();
	
	boolean match( TypedStringExp exp )
	{
		if( exp.dt.verify( literal, context ) )
			return true;
		
		// this datatype didn't accept me. so record it for diagnosis.
		failedTypes.add( exp.dt );
		return true;
	}
}