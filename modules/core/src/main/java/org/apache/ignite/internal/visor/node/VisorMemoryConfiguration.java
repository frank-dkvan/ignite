/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.visor.node;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import org.apache.ignite.configuration.MemoryConfiguration;
import org.apache.ignite.configuration.MemoryPolicyConfiguration;
import org.apache.ignite.internal.util.typedef.F;
import org.apache.ignite.internal.util.typedef.internal.S;
import org.apache.ignite.internal.util.typedef.internal.U;
import org.apache.ignite.internal.visor.VisorDataTransferObject;

/**
 * Data transfer object for memory configuration.
 */
public class VisorMemoryConfiguration extends VisorDataTransferObject {
    /** */
    private static final long serialVersionUID = 0L;

    /** Size of a memory chunk reserved for system cache initially. */
    private long sysCacheInitSize;

    /** Size of memory for system cache. */
    private long sysCacheMaxSize;

    /** Page size. */
    private int pageSize;

    /** Concurrency level. */
    private int concLvl;

    /** Name of MemoryPolicy to be used as default. */
    private String dfltMemPlcName;

    /** Size of memory (in bytes) to use for default MemoryPolicy. */
    private long dfltMemPlcSize;

    /** Memory policies. */
    private List<VisorMemoryPolicyConfiguration> memPlcs;

    /**
     * Default constructor.
     */
    public VisorMemoryConfiguration() {
        // No-op.
    }

    /**
     * Create data transfer object.
     *
     * @param memCfg Memory configuration.
     */
    public VisorMemoryConfiguration(MemoryConfiguration memCfg) {
        assert memCfg != null;

        sysCacheInitSize = memCfg.getSystemCacheInitialSize();
        sysCacheMaxSize = memCfg.getSystemCacheMaxSize();
        pageSize = memCfg.getPageSize();
        concLvl = memCfg.getConcurrencyLevel();
        dfltMemPlcName = memCfg.getDefaultMemoryPolicyName();
        dfltMemPlcSize = memCfg.getDefaultMemoryPolicySize();

        MemoryPolicyConfiguration[] plcs = memCfg.getMemoryPolicies();

        if (!F.isEmpty(plcs)) {
            memPlcs = new ArrayList<>(plcs.length);

            for (MemoryPolicyConfiguration plc : plcs)
                memPlcs.add(new VisorMemoryPolicyConfiguration(plc));
        }
    }

    /**
     * @return Concurrency level.
     */
    public int getConcurrencyLevel() {
        return concLvl;
    }

    /**
     * @return Initial size of a memory region reserved for system cache.
     */
    public long getSystemCacheInitialSize() {
        return sysCacheInitSize;
    }

    /**
     * @return Maximum memory region size reserved for system cache.
     */
    public long getSystemCacheMaxSize() {
        return sysCacheMaxSize;
    }

    /**
     * @return Page size.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @return Name of MemoryPolicy to be used as default.
     */
    public String getDefaultMemoryPolicyName() {
        return dfltMemPlcName;
    }

    /**
     * @return Default memory policy size.
     */
    public long getDefaultMemoryPolicySize() {
        return dfltMemPlcSize;
    }

    /**
     * @return Collection of MemoryPolicyConfiguration objects.
     */
    public List<VisorMemoryPolicyConfiguration> getMemoryPolicies() {
        return memPlcs;
    }

    /** {@inheritDoc} */
    @Override protected void writeExternalData(ObjectOutput out) throws IOException {
        out.writeLong(sysCacheInitSize);
        out.writeLong(sysCacheMaxSize);
        out.writeInt(pageSize);
        out.writeInt(concLvl);
        U.writeString(out, dfltMemPlcName);
        out.writeLong(dfltMemPlcSize);
        U.writeCollection(out, memPlcs);
    }

    /** {@inheritDoc} */
    @Override protected void readExternalData(byte protoVer, ObjectInput in) throws IOException, ClassNotFoundException {
        sysCacheInitSize = in.readLong();
        sysCacheMaxSize = in.readLong();
        pageSize = in.readInt();
        concLvl = in.readInt();
        dfltMemPlcName = U.readString(in);
        dfltMemPlcSize = in.readLong();
        memPlcs = U.readList(in);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(VisorMemoryConfiguration.class, this);
    }
}
