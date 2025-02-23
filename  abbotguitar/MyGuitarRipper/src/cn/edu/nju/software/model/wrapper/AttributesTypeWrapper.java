/*  
 *  Copyright (c) 2009-@year@. The GUITAR group at the University of Maryland. Names of owners of this group may
 *  be obtained by sending an e-mail to atif@cs.umd.edu
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 *  documentation files (the "Software"), to deal in the Software without restriction, including without 
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *  the Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 *  conditions:
 * 
 *  The above copyright notice and this permission notice shall be included in all copies or substantial 
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 *  LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO 
 *  EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER 
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 *  THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package cn.edu.nju.software.model.wrapper;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.model.data.AttributesType;
import cn.edu.nju.software.model.data.PropertyType;


/**
 * Wrapper for {@link cn.edu.nju.software.model.data.AttributesType}
 * 
 * <p>
 * 
 * @author     </a>
 */
public class AttributesTypeWrapper {
	AttributesType dAtrributeType;

	/**
	 * @return the dAtrributeType
	 */
	public AttributesType getdAtrributeType() {
		return dAtrributeType;
	}

	/**
	 * @param dAtrributeType
	 */
	public AttributesTypeWrapper(AttributesType dAtrributeType) {
		super();
		this.dAtrributeType = dAtrributeType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributesTypeWrapper other = (AttributesTypeWrapper) obj;
		if (dAtrributeType == null) {
			if (other.dAtrributeType != null)
				return false;
		} else {
			List<PropertyType> properties = dAtrributeType.getProperty();
			List<PropertyType> otherProperties = other.dAtrributeType
					.getProperty();

			if (properties == null && otherProperties == null)
				return true;
			else if (properties == null || otherProperties == null)
				return false;

			List<PropertyTypeWrapper> aProperties = new ArrayList<PropertyTypeWrapper>();
			for (PropertyType p : properties)
				aProperties.add(new PropertyTypeWrapper(p));

			List<PropertyTypeWrapper> aOtherProperties = new ArrayList<PropertyTypeWrapper>();
			for (PropertyType p : otherProperties)
				aOtherProperties.add(new PropertyTypeWrapper(p));

			return aProperties.equals(aOtherProperties);

		}

		return true;
	}

	/**
	 * Check if a set of Attributes is a subset of the current Attributes
	 * 
	 * <p>
	 * 
	 * @param otherAttributes
	 * @return <code>true</code> if the attribute contains all
	 *         <code>otherAttributes</code>
	 */
	public boolean containsAll(AttributesTypeWrapper otherAttributes) {
		List<PropertyType> friendProperites = otherAttributes.dAtrributeType
				.getProperty();
		for (PropertyType property : friendProperites) {
			if (!contains(property))
				return false;
		}
		return true;
	}

	/**
	 * Check if the current attribute set contains a certain property with a
	 * certain value.
	 * 
	 * <p>
	 * 
	 * @param pChild
	 * @return <code>true </code> if the property is in the attribute
	 */
	public boolean contains(PropertyType pChild) {
		List<PropertyType> currentProperites = dAtrributeType.getProperty();
		String sName = pChild.getName();
		List<String> lChildValue = pChild.getValue();

		for (PropertyType aPropertyType : currentProperites) {

			String currentName = aPropertyType.getName();

			if (sName.equals(currentName)) {
				List<String> lcurrentValue = aPropertyType.getValue();
				if (lChildValue == null)
					return true;
				else {

					for (String aChildValue : lChildValue) {

						if (!lcurrentValue.contains(aChildValue))
							return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if the current attribute set contains a certain property with a
	 * certain value using wildcats comparison.
	 * 
	 * <p>
	 * 
	 * @param pChild
	 * @return <code>true</code> if the attribute contain the property
	 */
	public boolean wildcatContains(PropertyType pChild) {

		List<PropertyType> currentProperites = dAtrributeType.getProperty();
		String sName = pChild.getName();
		List<String> lChildValue = pChild.getValue();

		for (PropertyType aPropertyType : currentProperites) {

			String currentName = aPropertyType.getName();

			if (sName.equals(currentName)) {
				List<String> lcurrentValue = aPropertyType.getValue();
				if (lChildValue == null)
					return true;
				else {

					for (String aChildValue : lChildValue) {

						if (!lcurrentValue.contains(aChildValue))
							return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get all an attribute by name
	 * 
	 * @param sName
	 *            : attribute name
	 * @return the name-values property pair
	 * 
	 */
	public PropertyType getPropertyByName(String sName) {
		List<PropertyType> lProperty = this.dAtrributeType.getProperty();

		for (PropertyType p : lProperty) {
			if (sName.equals(p.getName())) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Get the first value of an attribute
	 * 
	 * @param sName
	 * @return The attribute value
	 */
	public String getFirstValByName(String sName) {
		List<PropertyType> lProperty = this.dAtrributeType.getProperty();

		for (PropertyType p : lProperty) {
			if (sName.equals(p.getName())) {
				List<String> lValues = p.getValue();
				if (lValues == null)
					return null;
				if (lValues.size() == 0)
					return null;
				return lValues.get(0);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (dAtrributeType == null)
			return "";
		String retString = "";
		PropertyTypeWrapper pAdapter;
		for (PropertyType p : dAtrributeType.getProperty()) {
			pAdapter = new PropertyTypeWrapper(p);
			retString += pAdapter.toString();
		}
		return retString;
	}

	/**
	 * Print the information of the component, used for debugging
	 */
	public void printInfo() {
		List<PropertyType> properties = this.dAtrributeType.getProperty();
		for (PropertyType p : properties) {
			String out;
			out = p.getName();
			for (String value : p.getValue())
				out += "\t" + value;
			System.out.println(out);
		}

	}

}
