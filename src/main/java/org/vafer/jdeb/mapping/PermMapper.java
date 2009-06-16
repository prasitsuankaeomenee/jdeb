/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vafer.jdeb.mapping;

import org.apache.tools.tar.TarEntry;
import org.vafer.jdeb.utils.Utils;

/**
 * Applies a uniform set of permissions and ownership to all entries.
 * 
 * @author Bryan Sant <bryan.sant@gmail.com>
 */
public final class PermMapper extends PrefixMapper {

    private int uid = -1;
    private int gid = -1;
    private String user;
    private String group;
    private int mode;

    public static int toMode(String modeString) {
        int mode = -1;
        if (modeString != null && modeString.length() > 0) {
            mode = Integer.parseInt(modeString, 8);
        }
        return mode;
    }

    public PermMapper(int uid, int gid, String user, String group, int mode, int strip, String prefix) {
        super(strip, prefix);
        this.uid = uid;
        this.gid = gid;
        this.user = user;
        this.group = group;
        this.mode = mode;
    }

    public PermMapper(int uid, int gid, String user, String group, String mode, int strip, String prefix) {
        this(uid, gid, user, group, toMode(mode), strip, prefix);
    }

    public PermMapper(String user, String group, String mode, int strip, String prefix) {
        this(-1, -1, user, group, toMode(mode), strip, prefix);
    }

    public TarEntry map(final TarEntry entry) {
        final String name = entry.getName();

        final TarEntry newEntry = new TarEntry(prefix + '/' + Utils.stripPath(strip, name));

        if (uid > -1) {
            newEntry.setUserId(uid);
        } else {
            newEntry.setUserId(entry.getUserId());
        }
        if (gid > -1) {
            newEntry.setGroupId(gid);
        } else {
            newEntry.setGroupId(entry.getGroupId());
        }
        if (user != null) {
            newEntry.setUserName(user);
        } else {
            newEntry.setUserName(entry.getUserName());
        }
        if (group != null) {
            newEntry.setGroupName(group);
        } else {
            newEntry.setGroupName(entry.getGroupName());
        }
        if (mode > -1) {
            newEntry.setMode(mode);
        } else {
            newEntry.setMode(entry.getMode());
        }
        newEntry.setSize(entry.getSize());

        return newEntry;
    }
}
