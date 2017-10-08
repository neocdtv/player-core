/*
 * Copyright (C) 2013 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package io.neocdtv.player.core;

/**
 * Shared trivial procedures.
 *
 * @author Christian Bauer from cling
 */
public class ModelUtil {

    /**
     * @param seconds The number of seconds to convert.
     * @return A string representing hours, minutes, seconds, e.g. <code>11:23:44</code>
     */
    public static String toTimeString(long seconds) {
        long hours = seconds / 3600,
                remainder = seconds % 3600,
                minutes = remainder / 60,
                secs = remainder % 60;

        return ((hours < 10 ? "0" : "") + hours
                + ":" + (minutes < 10 ? "0" : "") + minutes
                + ":" + (secs < 10 ? "0" : "") + secs);
    }

    /**
     * @param s A string representing hours, minutes, seconds, e.g. <code>11:23:44</code>
     * @return The converted number of seconds.
     */
    public static long fromTimeString(String s) {
        // Handle "00:00:00.000" pattern, drop the milliseconds
        if (s.lastIndexOf(".") != -1)
            s = s.substring(0, s.lastIndexOf("."));
        String[] split = s.split(":");
        if (split.length != 3)
            throw new IllegalArgumentException("Can't parse time string: " + s);
        return (Long.parseLong(split[0]) * 3600) +
            (Long.parseLong(split[1]) * 60) +
            (Long.parseLong(split[2]));
    }
}
