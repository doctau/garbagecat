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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipselabs.garbagecat.domain.BlockingEvent;
import org.eclipselabs.garbagecat.domain.CombinedData;
import org.eclipselabs.garbagecat.util.jdk.JdkMath;
import org.eclipselabs.garbagecat.util.jdk.JdkRegEx;
import org.eclipselabs.garbagecat.util.jdk.JdkUtil;

/**
 * <p>
 * G1_FULL_GC
 * </p>
 * 
 * <p>
 * G1 collector Full GC event. 
 * </p>
 * 
 * <h3>Example Logging</h3>
 * 
 * <p>
 * 1) Standard format:
 * </p>
 * 
 * <pre>
 * 5060.152: [Full GC (System.gc()) 2270M->2038M(3398M), 5.8360430 secs]
 * </pre>
 * 
 * <p>
 * 2) With -XX:+PrintGCDateStamps:
 * </p>
 * 
 * <pre>
 * 2010-02-26T08:31:51.990-0600: [Full GC (System.gc()) 2270M->2038M(3398M), 5.8360430 secs]
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * @author James Livingston
 * 
 */
public class G1FullGCEvent implements BlockingEvent, CombinedData {

    /**
     * Regular expressions defining the logging.
     */
    private static final String REGEX = "^(" + JdkRegEx.DATESTAMP + ": )?" + JdkRegEx.TIMESTAMP
            + ": \\[Full GC \\((.+?)\\) "
            + JdkRegEx.SIZE_JDK7 + "->" + JdkRegEx.SIZE_JDK7 + "\\(" + JdkRegEx.SIZE_JDK7 + "\\), "
            + JdkRegEx.DURATION + "\\]";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    /**
     * The log entry for the event. Can be used for debugging purposes.
     */
    private CharSequence logEntry;

    /**
     * The elapsed clock time for the GC event in milliseconds (rounded).
     */
    private int duration;

    /**
     * The time when the GC event happened in milliseconds after JVM startup.
     */
    private long timestamp;

    /**
     * The trigger for the Full GC event such as "System.gc()".
     */
    private String trigger;

    /**
     * Young generation size (kilobytes) at beginning of GC event.
     */
    private int combined;

    /**
     * Young generation size (kilobytes) at end of GC event.
     */
    private int combinedEnd;

    /**
     * Available space in young generation (kilobytes). Equals young generation allocation minus one survivor space.
     */
    private int combinedAvailable;

    /**
     * Create detail logging event from log entry.
     */
    public G1FullGCEvent(CharSequence logEntry) {
        this.logEntry = logEntry;
        Matcher matcher = PATTERN.matcher(logEntry);
        if (matcher.find()) {
            timestamp = JdkMath.convertSecsToMillis(matcher.group(12)).longValue();
            trigger = matcher.group(13);
            combined = JdkMath.parseSize(matcher.group(14), matcher.group(15));
            combinedEnd = JdkMath.parseSize(matcher.group(16), matcher.group(17));
            combinedAvailable = JdkMath.parseSize(matcher.group(18), matcher.group(19));
            duration = JdkMath.convertSecsToMillis(matcher.group(20)).intValue();
        } else {
            throw new IllegalArgumentException("log entry did not match " + REGEX);
        }
    }

    /**
     * Alternate constructor. Create detail logging event from values.
     * 
     * @param logEntry
     * @param timestamp
     * @param duration
     */
    public G1FullGCEvent(CharSequence logEntry, long timestamp, int duration) {
        this.logEntry = logEntry;
        this.timestamp = timestamp;
        this.duration = duration;
    }

    public CharSequence getLogEntry() {
        return logEntry;
    }

    public int getDuration() {
        return duration;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getCombinedOccupancyInit() {
        return combined;
    }

    public int getCombinedOccupancyEnd() {
        return combinedEnd;
    }

    public int getCombinedSpace() {
        return combinedAvailable;
    }

    public String getName() {
        return JdkUtil.LogEventType.G1_FULL_GC.toString();
    }

    public String getTrigger() {
        return trigger;
    }

    /**
     * Determine if the logLine matches the logging pattern(s) for this event.
     * 
     * @param logLine
     *            The log line to test.
     * @return true if the log line matches the event pattern, false otherwise.
     */
    public static final boolean match(CharSequence logLine) {
        return PATTERN.matcher(logLine).matches();
    }
}
