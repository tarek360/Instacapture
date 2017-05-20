package com.tarek360.instacapture;

/**
 * Created by tarek on 5/17/16.
 */
class InstacaptureConfiguration {

    final boolean logging;

    private InstacaptureConfiguration(final Builder builder) {

        logging = builder.logging;
    }

    public static InstacaptureConfiguration createDefault() {
        return new Builder().build();
    }

    /**
     * Builder for {@link InstacaptureConfiguration}
     */
    public static class Builder {

        private boolean logging;

        public Builder logging(boolean logging) {
            this.logging = logging;
            return this;
        }

        /**
         * Builds configured {@link InstacaptureConfiguration} object
         */
        public InstacaptureConfiguration build() {
            return new InstacaptureConfiguration(this);
        }
    }
}
