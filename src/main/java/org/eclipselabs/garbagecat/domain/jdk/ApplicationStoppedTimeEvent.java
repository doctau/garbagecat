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

import org.eclipselabs.garbagecat.domain.LogEvent;
import org.eclipselabs.garbagecat.util.jdk.JdkUtil;

/**
 * <p>
 * APPLICATION_STOPPED_TIME
 * </p>
 * 
 * <p>
 * Logging enabled with the <code>-XX:+PrintGCApplicationStoppedTime</code> JVM option. It shows the length of a
 * collection pause. For example:
 * </p>
 * 
 * <p>
 * This option is redundant, as the same information can be calculated from the GC logging timestamps and durations.
 * Therefore, advise against using it, as it adds overhead with no analysis value.
 * </p>
 * 
 * <h3>Example Logging</h3>
 * 
 * <pre>
 * Total time for which application threads were stopped: 0.0968457 seconds
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class ApplicationStoppedTimeEvent implements LogEvent {

    /**
     * Regular expressions defining the logging.
     */
    private static final String REGEX = "^Total time for which application threads were "
            + "stopped: \\d{1,4}\\.\\d{7} seconds[ ]*$";

    /**
     * RegEx pattern.
     */
    private static Pattern PATTERN = Pattern.compile(ApplicationStoppedTimeEvent.REGEX);

    /**
     * The log entry for the event. Can be used for debugging purposes.
     */
    private CharSequence logEntry;


    /**
     * Create detail logging event from log entry.
     */
    public ApplicationStoppedTimeEvent(CharSequence logEntry) {
        this.logEntry = logEntry;
    }

    public CharSequence getLogEntry() {
        return logEntry;
    }

    public String getName() {
        return JdkUtil.LogEventType.APPLICATION_STOPPED_TIME.toString();
    }

    public long getTimestamp() {
        throw new UnsupportedOperationException("Event does not include timestamp information");
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
