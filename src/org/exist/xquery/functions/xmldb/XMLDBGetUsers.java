/*
 * eXist Open Source Native XML Database
 * Copyright (C) 2004-2010 The eXist Project
 * http://exist-db.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *  
 *  $Id$
 */
package org.exist.xquery.functions.xmldb;

import java.util.Collection;
import org.apache.log4j.Logger;

import org.exist.dom.QName;
import org.exist.security.Account;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.exist.security.SecurityManager;
import org.exist.xquery.value.ValueSequence;
import org.exist.xquery.value.StringValue;

/**
 * @author Adam Retter <adam@existsolutions.com>
 */
public class XMLDBGetUsers extends BasicFunction {

    protected static final Logger logger = Logger.getLogger(XMLDBGetUsers.class);

    public final static FunctionSignature signature = new FunctionSignature(
        new QName("get-users", XMLDBModule.NAMESPACE_URI, XMLDBModule.PREFIX),
        "Returns the list of users in the group",
        new SequenceType[]{
            new FunctionParameterSequenceType("group-name", Type.STRING, Cardinality.EXACTLY_ONE, "The group-name"),
        },
        new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_MORE, "The list of users in the group identified by $group-name")
    );
	
    public XMLDBGetUsers(XQueryContext context) {
        super(context, signature);
    }
	
    /* (non-Javadoc)
     * @see org.exist.xquery.BasicFunction#eval(org.exist.xquery.value.Sequence[], org.exist.xquery.value.Sequence)
     */
    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        String groupName = args[0].getStringValue();

        //TODO replace with SecurityManager.getUsers(groupName)
        SecurityManager manager = context.getBroker().getBrokerPool().getSecurityManager();
        Collection<Account> users = manager.getUsers();
        ValueSequence userNames = new ValueSequence(users.size());
        for(Account user : users) {
            if(user.hasGroup(groupName)) {
                userNames.add(new StringValue(user.getName()));
            }
        }

        return userNames;
    }
}