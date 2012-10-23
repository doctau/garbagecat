/******************************************************************************
 * Garbage Cat                                                                *
 *                                                                            *
 * Copyright (c) 2008-2012 Red Hat, Inc.                                      *
 * All rights reserved. This program and the accompanying materials           *
 * are made available under the terms of the Eclipse Public License v1.0      *
 * which accompanies this distribution, and is available at                   *
 * http://www.eclipse.org/legal/epl-v10.html                                  *
 ******************************************************************************/
package org.eclipselabs.garbagecat.domain.jdk;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipselabs.garbagecat.util.jdk.JdkUtil;

/**
 * @author James Livingston
 */
public class TestG1RemarkEvent extends TestCase {
    public void testRemark() {
        String logLine = "106.129: [GC remark, 0.0450170 secs]";
        Assert.assertTrue("Log line not recognized as " + JdkUtil.LogEventType.G1_REMARK.toString() + ".", G1RemarkEvent.match(logLine));
        G1RemarkEvent event = new G1RemarkEvent(logLine);
        Assert.assertEquals(106129, event.getTimestamp());
        Assert.assertEquals(45, event.getDuration());
    }
}
