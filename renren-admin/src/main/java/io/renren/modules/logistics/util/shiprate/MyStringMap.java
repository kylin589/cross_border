package io.renren.modules.logistics.util.shiprate;

import java.util.List;

public class MyStringMap {
    private List<Entry> entries;

    /**
     * @return entries
     */
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * @param entries the entries to set
     */
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public static class Entry {
        private String key;
        private String value;
        /**
         * @return key
         */
        public String getKey() {
            return key;
        }
        /**
         * @param key the key to set
         */
        public void setKey(String key) {
            this.key = key;
        }
        /**
         * @return value
         */
        public String getValue() {
            return value;
        }
        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }

    }
}