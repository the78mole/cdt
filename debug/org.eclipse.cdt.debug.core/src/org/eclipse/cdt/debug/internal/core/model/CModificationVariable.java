/*
 *(c) Copyright QNX Software Systems Ltd. 2002.
 * All Rights Reserved.
 * 
 */

package org.eclipse.cdt.debug.internal.core.model;

import java.text.MessageFormat;

import org.eclipse.cdt.debug.core.cdi.CDIException;
import org.eclipse.cdt.debug.core.cdi.ICDIFormat;
import org.eclipse.cdt.debug.core.cdi.model.ICDIValue;
import org.eclipse.cdt.debug.core.cdi.model.ICDIVariable;
import org.eclipse.cdt.debug.core.model.ICValue;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;

/**
 * 
 * Common functionality for variables that support value modification
 * 
 * @since Aug 9, 2002
 */
public abstract class CModificationVariable extends CVariable
{
	private Boolean fEditable = null;

	/**
	 * Constructor for CModificationVariable.
	 * @param parent
	 * @param cdiVariable
	 */
	public CModificationVariable( CDebugElement parent, ICDIVariable cdiVariable )
	{
		super( parent, cdiVariable );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IValueModification#supportsValueModification()
	 */
	public boolean supportsValueModification()
	{
		if ( fEditable == null )
		{
			CDebugTarget target = (CDebugTarget)getDebugTarget().getAdapter( CDebugTarget.class );
			if ( target != null && !target.isCoreDumpTarget() )
			{
				try
				{
					fEditable = new Boolean( getCDIVariable().isEditable() );
				}
				catch( CDIException e )
				{
					logError( e );
				}
			}
			else
			{
				fEditable = new Boolean( false );
			}
		}
		return ( fEditable != null ) ? fEditable.booleanValue() : false;
	}

	/**
	 * @see IValueModification#verifyValue(String)
	 */
	public boolean verifyValue( String expression )
	{
		return true;
	}

	/**
	 * @see IValueModification#verifyValue(IValue)
	 */
	public boolean verifyValue( IValue value )
	{
		return value.getDebugTarget().equals( getDebugTarget() );
	}

	/**
	 * @see IValueModification#setValue(String)
	 */
	public final void setValue( String expression ) throws DebugException
	{
		String newExpression = processExpression( expression );
		ICDIVariable cdiVariable = getCDIVariable();
		if ( cdiVariable == null )
		{
			logError( "Error in IValueModification#setValue: no cdi variable." );
			requestFailed( "Unable to set value.", null );
			return;
		}
		try
		{
			cdiVariable.setValue( newExpression );
		}
		catch( CDIException e )
		{
			targetRequestFailed( e.getMessage(), null );
		}
	}

	/**
	 * Set this variable's value to the given value
	 */
	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.internal.core.CModificationVariable#setValue(ICDIValue)
	 */
	protected void setValue( ICDIValue value ) throws DebugException
	{
		ICDIVariable cdiVariable = getCDIVariable();
		if ( cdiVariable == null )
		{
			logError( "Error in IValueModification#setValue: no cdi variable." );
			requestFailed( "Unable to set value.", null );
			return;
		}
		try
		{
			cdiVariable.setValue( value );
		}
		catch( CDIException e )
		{
			targetRequestFailed( e.getMessage(), null );
		}			
	}
	
	private String processExpression( String oldExpression ) throws DebugException
	{
		CValue value = (CValue)getValue();
		if ( value == null )
		{
			logError( "Error in IValueModification#setValue: no value." );
			requestFailed( "Unable to set value.", null );
			return null;
		}
		if ( value.getType() == ICValue.TYPE_CHAR && getFormat() == ICDIFormat.NATURAL )
		{
			char[] chars = oldExpression.toCharArray();
			if ( chars.length != 1 )
			{
				requestFailed( MessageFormat.format( "Invalid value: ''{0}''.", new Object[] { oldExpression } ), null );
				return null;
			}
			return Short.toString( (short)chars[0] );
		}
		return oldExpression;
	}
}
