package com.yshi.ldap;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

public class LdapTest {

  public static void main(String[] args) throws NamingException {
    if (args.length != 4) {
      System.err.println("Need 4 parameters: <ldap-server-url> <bind-principal> <password>" +
          " <search-base-dn>");
      return;
    }

    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY,
        "com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.PROVIDER_URL, args[0]);
    env.put(Context.SECURITY_AUTHENTICATION, "simple");
    env.put(Context.SECURITY_PRINCIPAL, args[1]);
    env.put(Context.SECURITY_CREDENTIALS, args[2]);

    DirContext ctx = new InitialDirContext(env);

    SearchControls searchControls = new SearchControls();
    searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    searchControls.setReturningAttributes(null);
    NamingEnumeration<SearchResult> results = ctx.search(args[3], "objectClass=inetOrgPerson", searchControls);
    while (results.hasMoreElements()) {
      SearchResult searchResult = results.nextElement();
      System.out.println(searchResult.getNameInNamespace());
    }
  }
}
