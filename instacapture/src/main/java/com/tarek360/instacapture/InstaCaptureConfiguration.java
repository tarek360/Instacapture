package com.tarek360.instacapture;

/**
 * Created by tarek on 5/17/16.
 */
public class InstaCaptureConfiguration {

  final boolean logging;

  private InstaCaptureConfiguration(final Builder builder) {

    logging = builder.logging;
  }

  public static InstaCaptureConfiguration createDefault() {
    return new Builder().build();
  }

  /**
   * Builder for {@link InstaCaptureConfiguration}
   */
  public static class Builder {

    private boolean logging;

    public Builder logging(boolean logging) {
      this.logging = logging;
      return this;
    }

    /** Builds configured {@link InstaCaptureConfiguration} object */
    public InstaCaptureConfiguration build() {
      return new InstaCaptureConfiguration(this);
    }
  }
}
