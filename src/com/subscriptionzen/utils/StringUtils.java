package com.subscriptionzen.utils;

import java.util.StringTokenizer;

/**
 * Utility class for String manipulation.
 * Demonstrates: StringBuffer and StringTokenizer (Unit 2 syllabus concepts).
 */
public class StringUtils {

    /**
     * Builds a formatted table row using StringBuffer.
     * Uses StringBuffer to efficiently concatenate column values with padding.
     *
     * @param columns the column values
     * @return formatted row string
     */
    public static String buildTableRow(String... columns) {
        StringBuffer sb = new StringBuffer();
        sb.append("| ");
        for (int i = 0; i < columns.length; i++) {
            sb.append(padRight(columns[i], 20));
            sb.append(" | ");
        }
        return sb.toString();
    }

    /**
     * Builds a separator line for table formatting using StringBuffer.
     *
     * @param columnCount number of columns
     * @return separator string
     */
    public static String buildTableSeparator(int columnCount) {
        StringBuffer sb = new StringBuffer();
        sb.append("+");
        for (int i = 0; i < columnCount; i++) {
            sb.append("----------------------+");
        }
        return sb.toString();
    }

    /**
     * Parses a comma-separated string into an array of trimmed tokens
     * using StringTokenizer.
     *
     * @param csv the comma-separated string
     * @return array of tokens
     */
    public static String[] parseCSV(String csv) {
        StringTokenizer tokenizer = new StringTokenizer(csv, ",");
        String[] tokens = new String[tokenizer.countTokens()];
        int index = 0;
        while (tokenizer.hasMoreTokens()) {
            tokens[index++] = tokenizer.nextToken().trim();
        }
        return tokens;
    }

    /**
     * Builds a formatted subscription summary using StringBuffer.
     *
     * @param id      subscription ID
     * @param service service name
     * @param cost    monthly cost
     * @param status  current status
     * @return formatted summary string
     */
    public static String buildSubscriptionSummary(int id, String service, double cost, String status) {
        StringBuffer sb = new StringBuffer();
        sb.append("[#").append(id).append("] ");
        sb.append(service);
        sb.append(" — Rs. ").append(String.format("%.2f", cost));
        sb.append(" (").append(status).append(")");
        return sb.toString();
    }

    /**
     * Right-pads a string to a given length using StringBuffer.
     *
     * @param text  text to pad
     * @param length desired length
     * @return padded string
     */
    public static String padRight(String text, int length) {
        if (text == null) {
            text = "";
        }
        StringBuffer sb = new StringBuffer(text);
        while (sb.length() < length) {
            sb.append(' ');
        }
        // Truncate if text is longer than the desired length
        if (sb.length() > length) {
            return sb.substring(0, length);
        }
        return sb.toString();
    }

    /**
     * Tokenizes a search query and checks if any token matches a target string.
     * Demonstrates StringTokenizer for keyword-based search.
     *
     * @param query  space-separated search query
     * @param target string to search within
     * @return true if any token is contained in the target (case-insensitive)
     */
    public static boolean matchesAnyToken(String query, String target) {
        if (query == null || target == null) {
            return false;
        }
        StringTokenizer tokenizer = new StringTokenizer(query, " ");
        String lowerTarget = target.toLowerCase();
        while (tokenizer.hasMoreTokens()) {
            if (lowerTarget.contains(tokenizer.nextToken().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
