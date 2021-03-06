/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.test.system;

import io.pravega.test.system.framework.Environment;
import io.pravega.test.system.framework.SystemTestRunner;
import io.pravega.test.system.framework.Utils;
import io.pravega.test.system.framework.services.Service;
import lombok.extern.slf4j.Slf4j;
import mesosphere.marathon.client.MarathonException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import java.net.URI;
import java.util.List;
import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SystemTestRunner.class)
public class BookkeeperTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5 * 60);

    /**
     * This is used to setup the various services required by the system test framework.
     *
     * @throws MarathonException if error in setup
     */
    @Environment
    public static void initialize() throws MarathonException {
        Service zk = Utils.createZookeeperService();
        if (!zk.isRunning()) {
            zk.start(true);
        }
        Service bk = Utils.createBookkeeperService(zk.getServiceDetails().get(0));
        if (!bk.isRunning()) {
            bk.start(true);
        }
    }

    /**
     * Invoke the bookkeeper test.
     * The test fails in case bookkeeper is not running on given port.
     */

    @Test
    public void bkTest() {
        log.debug("Start execution of bkTest");
        Service bk = Utils.createBookkeeperService(null);
        List<URI> bkUri = bk.getServiceDetails();
        log.debug("Bk Service URI details: {} ", bkUri);
        for (int i = 0; i < bkUri.size(); i++) {
            assertEquals(3181, bkUri.get(i).getPort());
        }
        log.debug("BkTest  execution completed");
    }
}
