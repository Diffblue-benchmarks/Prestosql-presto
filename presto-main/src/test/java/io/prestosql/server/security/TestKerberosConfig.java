/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.prestosql.server.security;

import com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static io.airlift.configuration.testing.ConfigAssertions.assertFullMapping;
import static io.airlift.configuration.testing.ConfigAssertions.assertRecordedDefaults;
import static io.airlift.configuration.testing.ConfigAssertions.recordDefaults;
import static io.prestosql.server.security.KerberosNameType.HOSTBASED_SERVICE;
import static io.prestosql.server.security.KerberosNameType.USER_NAME;

public class TestKerberosConfig
{
    @Test
    public void testDefaults()
    {
        assertRecordedDefaults(recordDefaults(KerberosConfig.class)
                .setKerberosConfig(null)
                .setServiceName(null)
                .setKeytab(null)
                .setPrincipalHostname(null)
                .setNameType(HOSTBASED_SERVICE));
    }

    @Test
    public void testExplicitPropertyMappings()
    {
        Map<String, String> properties = new ImmutableMap.Builder<String, String>()
                .put("http.authentication.krb5.config", "/etc/krb5.conf")
                .put("http.server.authentication.krb5.service-name", "airlift")
                .put("http.server.authentication.krb5.keytab", "/tmp/presto.keytab")
                .put("http.server.authentication.krb5.principal-hostname", "presto.prestosql.io")
                .put("http.server.authentication.krb5.name-type", "USER_NAME")
                .build();

        KerberosConfig expected = new KerberosConfig()
                .setKerberosConfig(new File("/etc/krb5.conf"))
                .setServiceName("airlift")
                .setKeytab(new File("/tmp/presto.keytab"))
                .setPrincipalHostname("presto.prestosql.io")
                .setNameType(USER_NAME);

        assertFullMapping(properties, expected);
    }
}
