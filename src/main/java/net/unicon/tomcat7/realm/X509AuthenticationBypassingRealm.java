package net.unicon.tomcat7.realm;

import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.security.Principal;
import java.util.Arrays;

/**
 * This custom Tomcat7 Realm's primary purpose is to bypass the X509 client certificate's Subject field comparison against pre-configured Tomcat's authentication sources
 * when <code>security-constraint(s)</code> is set up for web applications URLs deployed in that Tomcat instance with <i>CLIENT-CERT</i> login config.
 * <p/>
 * This is useful in cases where those application resources configured with <code>security-constraints</code> and <i>CLIENT-CERT</i> login config perform
 * authentication of client certificates themselves.
 * <p/>
 * This implementation just creates a valid instance of <code>GenericPrincipal</code> with username string containing the Subject line from client cert,
 * and default <i>X509</i> role - the one that must be configured inside <i>auth-constraint</i> element in applications' web.xml.
 * The role name is configurable via this class' property of <code>authnContraintRoleName</code>
 * <p/>
 * This implementation also turns off X509 certificate chain validation.
 *
 * @author Dmitriy Kopylenko
 * @author Unicon, inc.
 * @since 1.0
 */
public class X509AuthenticationBypassingRealm extends RealmBase {

    private static final Log log = LogFactory.getLog(X509AuthenticationBypassingRealm.class);

    private static final String DEFAULT_AUTHN_CONSTRAINT_ROLE_NAME = "X509";

    private String authnConstraintRoleName = DEFAULT_AUTHN_CONSTRAINT_ROLE_NAME;

    public void setAuthnConstraintRoleName(String authnConstraintRoleName) {
        this.authnConstraintRoleName = authnConstraintRoleName;
    }

    public X509AuthenticationBypassingRealm() {
        super();
        this.setValidate(false);
    }

    @Override
    protected String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected String getPassword(String username) {
        return null;
    }

    @Override
    protected Principal getPrincipal(String username) {
        if(log.isDebugEnabled()) {
            log.debug(String.format("Returning GenericPrincipal with username %s and role %s", username, this.authnConstraintRoleName));
        }
        return new GenericPrincipal(username, null, Arrays.asList(this.authnConstraintRoleName));
    }
}
