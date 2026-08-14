package hyung.jin.seo.jae.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * PasswordEncoder that understands legacy plain-text passwords and bcrypt.
 * - encode() always returns {bcrypt}<hash>
 * - matches() accepts:
 *   {bcrypt}<hash>  → bcrypt verify
 *   $2a$...         → bcrypt verify (no prefix)
 *   {noop}<plain>   → plain compare
 *   {null}<plain>   → plain compare (legacy)
 *   <plain>         → plain compare (legacy)
 */
public class LegacyAwarePasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        String hash = bcrypt.encode(rawPassword);
        if (hash.startsWith("{")) {
            return hash;
        }
        return "{bcrypt}" + hash;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (!StringUtils.hasText(encodedPassword)) {
            return false;
        }

        String value = encodedPassword.trim();

        if (value.startsWith("{bcrypt}")) {
            String hash = value.substring("{bcrypt}".length());
            return bcrypt.matches(rawPassword, hash);
        }

        if (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$")) {
            return bcrypt.matches(rawPassword, value);
        }

        if (value.startsWith("{noop}")) {
            return value.substring("{noop}".length()).contentEquals(rawPassword);
        }

        if (value.startsWith("{null}")) {
            return value.substring("{null}".length()).contentEquals(rawPassword);
        }

        // No prefix → treat as legacy plain text
        return value.contentEquals(rawPassword);
    }
}


