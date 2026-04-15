package com.garethevans.church.opensongtablet.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Manages a rolling list of "breadcrumbs" (recent app events) to aid in debugging crashes.
 */
public class BreadcrumbManager {
    private static final int MAX_BREADCRUMBS = 30;
    private final LinkedList<String> crumbs = new LinkedList<>();
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());

    public synchronized void add(String message) {
        String timestampedMessage = df.format(new Date()) + ": " + message;
        crumbs.add(timestampedMessage);
        if (crumbs.size() > MAX_BREADCRUMBS) {
            crumbs.removeFirst();
        }
    }

    public synchronized String getFormattedBreadcrumbs() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recent breadcrumbs (last ").append(crumbs.size()).append("):\n");
        for (String crumb : crumbs) {
            sb.append("- ").append(crumb).append("\n");
        }
        return sb.toString();
    }
}
