/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-03,  Wolfgang M. Meier (meier@ifs.tu-darmstadt.de)
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 *  $Id$
 */
package org.exist.xpath.value;

import org.exist.xpath.XPathException;

public class DoubleValue extends NumericValue {

	public final static DoubleValue NaN = new DoubleValue(Double.NaN);
	public final static DoubleValue ZERO = new DoubleValue(0.0E0);
	
	private double value;
	
	public DoubleValue(double value) {
		this.value = value;
	}
	
	public DoubleValue(String stringValue) throws XPathException {
		try {
			value = Double.parseDouble(stringValue);
		} catch(NumberFormatException e) {
			throw new XPathException("cannot convert string '" + stringValue + "' into a double");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#getType()
	 */
	public int getType() {
		return Type.DOUBLE;
	}
	
	/* (non-Javadoc)
	 * @see org.exist.xpath.value.Item#getStringValue()
	 */
	public String getStringValue() throws XPathException {
		return Double.toString(value);
	}

	public double getValue() {
		return value;
	}
	
	public Item itemAt(int pos) {
		return pos == 0 ? this : null;
	}
	
	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#convertTo(int)
	 */
	public AtomicValue convertTo(int requiredType) throws XPathException {
		switch(requiredType) {
			case Type.ATOMIC:
			case Type.ITEM:
			case Type.NUMBER:
			case Type.DOUBLE:
				return this;
			case Type.FLOAT:
				if(value < Float.MIN_VALUE || value > Float.MAX_VALUE)
					throw new XPathException("Value is out of range for type xs:float");
				return new FloatValue((float)value);
			case Type.STRING:
				return new StringValue(getStringValue());
			case Type.DECIMAL:
			case Type.INTEGER:
			case Type.NON_POSITIVE_INTEGER:
			case Type.NEGATIVE_INTEGER:
			case Type.LONG:
			case Type.INT:
			case Type.SHORT:
			case Type.BYTE:
			case Type.NON_NEGATIVE_INTEGER:
			case Type.UNSIGNED_LONG:
			case Type.UNSIGNED_INT:
			case Type.UNSIGNED_SHORT:
			case Type.UNSIGNED_BYTE:
			case Type.POSITIVE_INTEGER:
				return new IntegerValue((long)value, requiredType);
			case Type.BOOLEAN:
				return (value == 0.0 && value == Double.NaN) ? BooleanValue.FALSE : 
					BooleanValue.TRUE;
			default:
				throw new XPathException("cannot convert double value '" + value + "' into " + 
					Type.getTypeName(requiredType));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.exist.xpath.value.AtomicValue#effectiveBooleanValue()
	 */
	public boolean effectiveBooleanValue() throws XPathException {
		return !(value == 0 || value == Double.NaN);
	}
	
	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#getDouble()
	 */
	public double getDouble() throws XPathException {
		return value;
	}
	
	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#getInt()
	 */
	public int getInt() throws XPathException {
		return (int)Math.round(value);
	}
	
	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#getLong()
	 */
	public long getLong() throws XPathException {
		return (long)Math.round(value);
	}
	
	public void setValue(double val) {
		value = val;
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#ceiling()
	 */
	public NumericValue ceiling() {
		return new DoubleValue(Math.ceil(value));
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#floor()
	 */
	public NumericValue floor() {
		return new DoubleValue(Math.floor(value));
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#round()
	 */
	public NumericValue round() {
		return new DoubleValue(Math.round(value));
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#minus(org.exist.xpath.value.NumericValue)
	 */
	public NumericValue minus(NumericValue other) throws XPathException {
		if(other instanceof DoubleValue)
			return new DoubleValue(value - ((DoubleValue)other).value);
		else
			return ((NumericValue)convertTo(other.getType())).minus(other);
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#plus(org.exist.xpath.value.NumericValue)
	 */
	public NumericValue plus(NumericValue other) throws XPathException {
		if(other instanceof DoubleValue)
			return new DoubleValue(value + ((DoubleValue)other).value);
		else
			return ((NumericValue)convertTo(other.getType())).plus(other);
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#mult(org.exist.xpath.value.NumericValue)
	 */
	public NumericValue mult(NumericValue other) throws XPathException {
		if(other instanceof DoubleValue)
			return new DoubleValue(value * ((DoubleValue)other).value);
		else
			return ((NumericValue)convertTo(other.getType())).mult(other);
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#div(org.exist.xpath.value.NumericValue)
	 */
	public NumericValue div(NumericValue other) throws XPathException {
		if(other instanceof DoubleValue)
			return new DoubleValue(value / ((DoubleValue)other).value);
		else
			return ((NumericValue)convertTo(other.getType())).div(other);
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#mod(org.exist.xpath.value.NumericValue)
	 */
	public NumericValue mod(NumericValue other) throws XPathException {
		if(other instanceof DoubleValue)
			return new DoubleValue(value % ((DoubleValue)other).value);
		else
			return ((NumericValue)convertTo(other.getType())).mod(other);
	}

	/* (non-Javadoc)
	 * @see org.exist.xpath.value.NumericValue#negate()
	 */
	public NumericValue negate() {
		return new DoubleValue(-value);
	}
}
