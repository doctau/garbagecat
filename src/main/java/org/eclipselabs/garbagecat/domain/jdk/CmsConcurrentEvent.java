/******************************************************************************
 * Garbage Cat                                                                *
 *                                                                            *
 * Copyright (c) 2008-2010 Red Hat, Inc.                                      *
 * All rights reserved. This program and the accompanying materials           *
 * are made available under the terms of the Eclipse Public License v1.0      *
 * which accompanies this distribution, and is available at                   *
 * http://www.eclipse.org/legal/epl-v10.html                                  *
 *                                                                            *
 * Contributors:                                                              *
 *    Red Hat, Inc. - initial API and implementation                          *
 ******************************************************************************/
package org.eclipselabs.garbagecat.domain.jdk;

import java.util.regex.Pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipselabs.garbagecat.domain.LogEvent;
import org.eclipselabs.garbagecat.util.jdk.JdkMath;
import org.eclipselabs.garbagecat.util.jdk.JdkRegEx;
import org.eclipselabs.garbagecat.util.jdk.JdkUtil;

/**
 * <p>
 * CMS_CONCURRENT
 * </p>
 * 
 * <p>
 * Any number of events that happen concurrently with the JVM's execution of application threads. These events are not
 * included in the GC analysis since there is no application pause time.
 * </p>
 * 
 * <h3>Example Logging</h3>
 * 
 * <pre>
 * 251.781: [CMS-concurrent-mark-start]
 * </pre>
 * 
 * <pre>
 * 252.707: [CMS-concurrent-mark: 0.796/0.926 secs] [Times: user=6.04 sys=0.14, real=0.93 secs]
 * </pre>
 * 
 * <pre>
 * 252.707: [CMS-concurrent-preclean-start]
 * </pre>
 * 
 * <pre>
 * 252.888: [CMS-concurrent-preclean: 0.141/0.182 secs] [Times: user=0.86 sys=0.03, real=0.18 secs]
 * </pre>
 * 
 * <pre>
 * 252.889: [CMS-concurrent-abortable-preclean-start]
 * </pre>
 * 
 * <pre>
 * 253.102: [CMS-concurrent-abortable-preclean: 0.083/0.214 secs] [Times: user=1.23 sys=0.02, real=0.21 secs]
 * </pre>
 * 
 * <pre>
 * CMS: abort preclean due to time 32633.935: [CMS-concurrent-abortable-preclean: 0.622/5.054 secs] [Times: user=2.42 sys=0.01, real=5.05 secs]
 * </pre>
 * 
 * <pre>
 * 253.189: [CMS-concurrent-sweep-start]
 * </pre>
 * 
 * <pre>
 * 258.265: [CMS-concurrent-sweep: 4.134/5.076 secs] [Times: user=31.65 sys=1.01, real=5.08 secs]
 * </pre>
 * 
 * <pre>
 * 258.265: [CMS-concurrent-reset-start]
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class CmsConcurrentEvent implements LogEvent {

    /**
     * Regular expressions defining the logging.
     */
    private static final String REGEX = "^( CMS: abort preclean due to time )?(" + JdkRegEx.DATESTAMP + ": )?"
            + JdkRegEx.TIMESTAMP + ": \\[CMS-concurrent.+$";

    /**
     * RegEx pattern.
     */
    private static final Pattern pattern = Pattern.compile(REGEX);

    /**
     * The log entry for the event. Can be used for debugging purposes.
     */
    private String logEntry;

    /**
     * The time when the GC event happened in milliseconds after JVM startup.
     */
    private long timestamp;


    /**
     * Create detail logging event from log entry.
     */
    public CmsConcurrentEvent(String logEntry) {
        this.logEntry = logEntry;

        Matcher matcher = pattern.matcher(logEntry);
        if (matcher.find()) {
            timestamp = JdkMath.convertSecsToMillis(matcher.group(13)).longValue();
        }
    }

    public String getLogEntry() {
        return logEntry;
    }

    public String getName() {
        return JdkUtil.LogEventType.CMS_CONCURRENT.toString();
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Determine if the logLine matches the logging pattern(s) for this event.
     * 
     * @param logLine
     *            The log line to test.
     * @return true if the log line matches the event pattern, false otherwise.
     */
    public static final boolean match(String logLine) {
        return pattern.matcher(logLine).matches();
    }

}
